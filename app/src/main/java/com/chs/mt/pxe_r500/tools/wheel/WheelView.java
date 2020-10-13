package com.chs.mt.pxe_r500.tools.wheel;
import com.chs.mt.pxe_r500.R;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


@SuppressLint({ "DrawAllocation", "ClickableViewAccessibility", "WrongCall" }) public class WheelView extends View {

    public static final int FONT_COLOR = Color.BLACK;
    public static final int FONT_SIZE = 30;
    public static final int PADDING = 10;
    public static final int SHOW_COUNT = 3;
    public static final int SELECT = 0;
    //总体宽度、高度、Item的高度
    private int width;
    private int mid;
//    private int height;
    private int itemHeight;
    private int itemWidth;
    //需要显示的行数
    private int showCount = SHOW_COUNT;
    //当前默认选择的位置
    private int select = SELECT;
    //字体颜色、大小、补白
    private int fontColor = FONT_COLOR;
    private int fontSize = FONT_SIZE;
    private int padding = PADDING;
    //文本列表
    private List<String> lists;
    //选中项的辅助文本，可为空
    private String selectTip;
    //每一项Item和选中项
    private List<WheelItem> wheelItems = new ArrayList<WheelItem>();
    private WheelSelect wheelSelect = null;
    
    //private Bitmap pointerBitmap; 
    //private Bitmap pointerBitmapShow; 
//    //手点击的Y坐标
//    private float mTouchY;
    //手点击的X坐标
    private float mTouchX;
    private float mTouchDownX;
    private int KEY_EVENT=0;
    private static final int KEY_EVENT_DWON=1;
    private static final int KEY_EVENT_MOVE=2;
    private static final int KEY_EVENT_UP=3;
    private float mTouchTH=10;
    //监听器
    private Context mContext = null;
    private AttributeSet mAttrs = null;
    private OnWheelViewItemSelectListener listener;

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
    	super(context, attrs, defStyleAttr);
    	mContext = context;
        mAttrs = attrs;
    	initView();
        
    }

    public WheelView(Context context, AttributeSet attrs) {
    	super(context, attrs);   	
    	mContext = context;
        mAttrs = attrs;
        initView();
        
    }
    public WheelView(Context context) {
		super(context);
		//initView();
		mContext = context;
  
   }
    

    /**
     * 设置字体的颜色，不设置的话默认为黑色
     * @param fontColor
     * @return
     */
    public WheelView fontColor(int fontColor){
        this.fontColor = fontColor;
        return this;
    }
    private void initView(){
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.wheelview);

        fontSize = (int) localTypedArray.getDimension(R.styleable.wheelview_fontSize, 5);        
        fontColor = localTypedArray.getColor(R.styleable.wheelview_fontColor, getResources().getColor(R.color.text_color_xoverset));
        //pointerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chs_channel_bg);
        localTypedArray.recycle();
    }
    /**
     * 设置字体的大小，不设置的话默认为30
     * @param fontSize
     * @return
     */
    public WheelView fontSize(int fontSize){
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 设置文本到上下两边的补白，不合适的话默认为10
     * @param padding
     * @return
     */
    public WheelView padding(int padding){
        this.padding = padding;
        return this;
    }

    /**
     * 设置选中项的复制文本，可以不设置
     * @param selectTip
     * @return
     */
    public WheelView selectTip(String selectTip){
        this.selectTip = selectTip;
        return this;
    }

    /**
     * 设置文本列表，必须且必须在build方法之前设置
     * @param lists
     * @return
     */
    public WheelView lists(List<String> lists){
        this.lists = lists;
        return this;
    }

    /**
     * 设置显示行数，不设置的话默认为3
     * @param showCount
     * @return
     */
    public WheelView showCount(int showCount){
        if(showCount % 2 == 0){
            throw new IllegalStateException("the showCount must be odd");
        }
        this.showCount = showCount;
        return this;
    }

    /**
     * 设置默认选中的文本的索引，不设置默认为0
     * @param select
     * @return
     */
    public WheelView select(int select){
        this.select = select;
        //invalidate();
        return this;
    }

    /**
     * 最后调用的方法，判断是否有必要函数没有被调用
     * @return
     */
    public WheelView build(){
        if(lists == null){
            throw new IllegalStateException("this method must invoke after the method [lists]");
        }
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到总体宽度
        //width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
    	width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
    	mid=width/2;
        // 得到每一个Item的高度
//        Paint mPaint = new Paint();
//        mPaint.setTextSize(fontSize);
//        Paint.FontMetrics metrics =  mPaint.getFontMetrics();
//        itemHeight = (int) (metrics.bottom - metrics.top) + 2 * padding;
        itemHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        //初始化每一个WheelItem
        initWheelItems(width, itemHeight);
        //初始化WheelSelect
        itemWidth=width/showCount;
        wheelSelect = new WheelSelect(showCount / 2 * itemWidth, itemWidth, itemHeight,
        		selectTip, fontColor, (fontSize+fontSize/2), padding);
        //得到所有的高度
//        height = itemHeight * showCount;
        //super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        
        //pointerBitmapShow=zoomImage(pointerBitmap,width,itemHeight);
        
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, 
            double newHeight) { 
	    // 获取这个图片的宽和高 
	    float width = bgimage.getWidth(); 
	    float height = bgimage.getHeight(); 
	    // 创建操作图片用的matrix对象 
	    Matrix matrix = new Matrix(); 
	    // 计算宽高缩放率 
	    float scaleWidth = ((float) newWidth) / width; 
	    float scaleHeight = ((float) newHeight) / height; 
	    // 缩放图片动作 
	    matrix.postScale(scaleWidth, scaleHeight); 
	    Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, 
	                    (int) height, matrix, true); 
	    return bitmap; 
    } 
    
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //得到总体宽度
//        width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
//        // 得到每一个Item的高度
//        Paint mPaint = new Paint();
//        mPaint.setTextSize(fontSize);
//        Paint.FontMetrics metrics =  mPaint.getFontMetrics();
//        itemHeight = (int) (metrics.bottom - metrics.top) + 2 * padding;
//        //初始化每一个WheelItem
//        initWheelItems(width, itemHeight);
//        //初始化WheelSelect
//        wheelSelect = new WheelSelect(showCount / 2 * itemHeight, width, itemHeight, selectTip, fontColor, fontSize, padding);
//        //得到所有的高度
//        height = itemHeight * showCount;
//        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
//    }

    /**
     * 创建显示个数+2个WheelItem
     * @param width
     * @param itemHeight
     */
//    private void initWheelItems(int width, int itemHeight) {
//        wheelItems.clear();
//        for(int i = 0; i < showCount + 2; i++){
//            int startY = itemHeight * (i - 1);
//            int stringIndex = select - showCount / 2 - 1 + i;
//            if(stringIndex < 0){
//                stringIndex = lists.size() + stringIndex;
//            }
//            wheelItems.add(new WheelItem(startY, width, itemHeight, fontColor, fontSize, lists.get(stringIndex)));
//        }
//    }
    private void initWheelItems(int width, int itemHeight) {
        wheelItems.clear();
        itemWidth=width/showCount;
        int listsize=lists.size();
        for(int i = 0; i < showCount + 2; i++){
            int startX = itemWidth * (i - 1);
            int stringIndex = select - showCount / 2 - 1 + i;
            if(stringIndex < 0){
                stringIndex = lists.size() + stringIndex;
            }
            if(stringIndex >= listsize){
            	stringIndex-=listsize;
            }
            //System.out.println("BUG stringIndex:" + stringIndex);
            wheelItems.add(new WheelItem(startX, itemWidth, itemHeight, fontColor, fontSize, lists.get(stringIndex)));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //mTouchY = event.getY();
                mTouchX = event.getX();
                mTouchDownX=mTouchX;
                KEY_EVENT=KEY_EVENT_DWON;
                return true;
            case MotionEvent.ACTION_MOVE:
//                float dy = event.getY() - mTouchY;
//                mTouchY = event.getY();
//                handleMove(dy);
            	
            	float dx = event.getX() - mTouchX;
                mTouchX = event.getX();
                handleMove(dx);
                if(((mTouchDownX+mTouchTH)<mTouchX)||((mTouchDownX-mTouchTH)>mTouchX)){
                	KEY_EVENT=KEY_EVENT_MOVE;
                }else{
                	KEY_EVENT=KEY_EVENT_DWON;
                }
                break;
            case MotionEvent.ACTION_UP:
            	//System.out.println("BUG KEY_EVENT:" + KEY_EVENT);
            	if(KEY_EVENT==KEY_EVENT_MOVE){
            		handleUp();
            	}else if(KEY_EVENT==KEY_EVENT_DWON){
            		KeyDownRelease(mTouchX);
            	}
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setIndex(int val) {
        select = val;
        initWheelItems(width, itemHeight);
        invalidate();
    }


    //TODO
    private void KeyDownRelease(float dx) {
    	int click=(int) (dx/itemWidth);
    	//System.out.println("BUG KEY_select 0:" + select);
    	select+=(click-showCount/2);
    	//System.out.println("BUG KEY_select 1:" + select);
    	int indexCNT=lists.size();
    	if(select>=indexCNT){
    		select-=indexCNT;
    	}else if(select<0){
    		select+=indexCNT;
    	}
    	if(listener != null){
            listener.onItemSelect(select,false);
        }
    	//System.out.println("BUG KEY_select 2:" + select);
    	initWheelItems(width, itemHeight);
    	invalidate();
    }
    /**
     * 处理移动操作
     * @param dy
     */
    private void handleMove(float dy) {
        //调整坐标
        for(WheelItem item : wheelItems){
            item.adjust(dy);
        }
//        invalidate();
        //调整
        adjust();
        if(listener != null){
            listener.onItemSelect(select,true);
        }
        invalidate();
    }

    /**
     * 处理抬起操作
     */
    private void handleUp(){
        int index = -1;
        //得到应该选择的那一项
        for(int i = 0; i < wheelItems.size(); i++){
            WheelItem item = wheelItems.get(i);
            //如果startY在selectItem的中点上面，则将该项作为选择项
            if(item.getStartX() > wheelSelect.getStartX() && item.getStartX() < (wheelSelect.getStartX() + itemWidth / 2)){
                index = i;
                break;
            }
            //如果startY在selectItem的中点下面，则将上一项作为选择项
            if(item.getStartX() >= (wheelSelect.getStartX() + itemWidth / 2) && item.getStartX() < (wheelSelect.getStartX() + itemWidth)){
                index = i - 1;
                break;
            }
        }
        //如果没找到或者其他因素，直接返回
        if(index == -1){
            return;
        }
        //得到偏移的位移
        float dy = wheelSelect.getStartX() - wheelItems.get(index).getStartX();
        //调整坐标
        for(WheelItem item : wheelItems){
            item.adjust(dy);
        }
//        invalidate();
        // 调整
        adjust();
        //设置选择项
        int stringIndex = lists.indexOf(wheelItems.get(index).getText());
        if(stringIndex != -1){
            select = stringIndex;
            if(listener != null){
                listener.onItemSelect(select,false);
            }
        }
        invalidate();
    }
//    private void handleUp(){
//        int chs_index = -1;
//        //得到应该选择的那一项
//        for(int i = 0; i < wheelItems.size(); i++){
//            WheelItem item = wheelItems.get(i);
//            //如果startY在selectItem的中点上面，则将该项作为选择项
//            if(item.getStartX() > wheelSelect.getStartY() && item.getStartX() < (wheelSelect.getStartY() + itemHeight / 2)){
//                chs_index = i;
//                break;
//            }
//            //如果startY在selectItem的中点下面，则将上一项作为选择项
//            if(item.getStartX() >= (wheelSelect.getStartY() + itemHeight / 2) && item.getStartX() < (wheelSelect.getStartY() + itemHeight)){
//                chs_index = i - 1;
//                break;
//            }
//        }
//        //如果没找到或者其他因素，直接返回
//        if(chs_index == -1){
//            return;
//        }
//        //得到偏移的位移
//        float dy = wheelSelect.getStartY() - wheelItems.get(chs_index).getStartX();
//        //调整坐标
//        for(WheelItem item : wheelItems){
//            item.adjust(dy);
//        }
//        invalidate();
//        // 调整
//        adjust();
//        //设置选择项
//        int stringIndex = lists.indexOf(wheelItems.get(chs_index).getText());
//        if(stringIndex != -1){
//            select = stringIndex;
//            if(listener != null){
//                listener.onItemSelect(select,false);
//            }
//        }
//    }

    /**
     * 调整Item移动和循环显示
     */
//    private void adjust(){
//        //如果向下滑动超出半个Item的高度，则调整容器
//        if(wheelItems.get(0).getStartX() >= -itemHeight / 2 ){
//            //移除最后一个Item重用
//            WheelItem item = wheelItems.remove(wheelItems.size() - 1);
//            //设置起点Y坐标
//            item.setStartX(wheelItems.get(0).getStartX() - itemHeight);
//            //得到文本在容器中的索引
//            int chs_index = lists.indexOf(wheelItems.get(0).getText());
//            if(chs_index == -1){
//                return;
//            }
//            chs_index -= 1;
//            if(chs_index < 0){
//                chs_index = lists.size() + chs_index;
//            }
//            //设置文本
//            item.setText(lists.get(chs_index));
//            //添加到最开始
//            wheelItems.add(0, item);
//            invalidate();
//            return;
//        }
//        //如果向上滑超出半个Item的高度，则调整容器
//        if(wheelItems.get(0).getStartX() <= (-itemHeight / 2 - itemHeight)){
//            //移除第一个Item重用
//            WheelItem item = wheelItems.remove(0);
//            //设置起点Y坐标
//            item.setStartX(wheelItems.get(wheelItems.size() - 1).getStartX() + itemHeight);
//            //得到文本在容器中的索引
//            int chs_index = lists.indexOf(wheelItems.get(wheelItems.size() - 1).getText());
//            if(chs_index == -1){
//                return;
//            }
//            chs_index += 1;
//            if(chs_index >= lists.size()){
//                chs_index = 0;
//            }
//            //设置文本
//            item.setText(lists.get(chs_index));
//            //添加到最后面
//            wheelItems.add(item);
//            invalidate();
//            return;
//        }
//    }
    private void adjust(){
        //如果向下滑动超出半个Item的高度，则调整容器
        if(wheelItems.get(0).getStartX() >= -itemWidth / 2 ){
            //移除最后一个Item重用
            WheelItem item = wheelItems.remove(wheelItems.size() - 1);
            //设置起点Y坐标
            item.setStartX(wheelItems.get(0).getStartX() - itemWidth);
            //得到文本在容器中的索引
            int index = lists.indexOf(wheelItems.get(0).getText());
            if(index == -1){
                return;
            }
            index -= 1;
            if(index < 0){
                index = lists.size() + index;
            }
            //设置文本
            item.setText(lists.get(index));
            //添加到最开始
            wheelItems.add(0, item);
            invalidate();
            return;
        }
        //如果向上滑超出半个Item的高度，则调整容器
        if(wheelItems.get(0).getStartX() <= (-itemWidth / 2 - itemWidth)){
            //移除第一个Item重用
            WheelItem item = wheelItems.remove(0);
            //设置起点Y坐标
            item.setStartX(wheelItems.get(wheelItems.size() - 1).getStartX() + itemWidth);
            //得到文本在容器中的索引
            int index = lists.indexOf(wheelItems.get(wheelItems.size() - 1).getText());
            if(index == -1){
                return;
            }
            index += 1;
            if(index >= lists.size()){
                index = 0;
            }
            //设置文本
            item.setText(lists.get(index));
            //添加到最后面
            wheelItems.add(item);
            invalidate();
            return;
        }
    }
    /**
     * 得到当前的选择项
     */
    public int getSelectItem(){
        return select;
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	//canvas.drawBitmap(pointerBitmapShow, width, itemHeight, null);
        //绘制每一项Item
        for(WheelItem item : wheelItems){
            item.onDraw(canvas);
        }
        //绘制阴影
        if(wheelSelect != null){
            wheelSelect.onDraw(canvas);
        }
//        WheelSelect(canvas,showCount / 2 * itemWidth, itemWidth, itemHeight,
//        		selectTip, fontColor, (fontSize+fontSize/2), padding);
    }
//    private void WheelSelect(Canvas mCanvas,int startX, int width, int height, String selectText, int fontColor, int fontSize, int padding) {
//        //四点坐标
//        Rect rect = new Rect();
//        //需要选择文本的颜色、大小、补白
//        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//    	
//        rect.left = startX;
//        rect.top = 0;
//        rect.right = startX + width;
//        rect.bottom = height;
//        
//      //绘制背景
//        mPaint.setStyle(Paint.Style.FILL);
//
//        //绘制提醒文字
//        if(selectText != null){
//            //设置钢笔属性
//        	Log.d("BUG", "WheelSelect fontSize:" + fontSize);
//            mPaint.setTextSize(fontSize);
//            mPaint.setColor(fontColor);
//            //得到字体的宽度
//            int textWidth = (int)mPaint.measureText(selectText);
//            //drawText的绘制起点是左下角,y轴起点为baseLine
//            Paint.FontMetrics metrics =  mPaint.getFontMetrics();
//            int baseLine = (int)(rect.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
//            //在靠右边绘制文本
//            mCanvas.drawText(selectText, rect.right - padding - textWidth, baseLine, mPaint);
//        }
//    }
    
    /**
     * 设置监听器
     * @param listener
     * @return
     */
    public WheelView listener(OnWheelViewItemSelectListener listener){
        this.listener = listener;
        return this;
    }

    public interface OnWheelViewItemSelectListener{
        void onItemSelect(int index,boolean fromUser);
    }
}
