package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;


import android.content.Context;
import android.content.res.TypedArray;  
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.graphics.RectF;  
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;  
import android.util.Log;
import android.view.MotionEvent;  
import android.view.View;  

public class MHSM_SeekBar extends View {

    private final boolean DEBUG = false;
    private final String TAG = "MHS_SeekBar";
    
    private Context mContext = null;
    private AttributeSet mAttrs = null;
    
    private Drawable mThumbDrawable = null;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private boolean CanTouch=true;
    private boolean ThumbTouch=false;
    private float mSeekBarMax = 0;
    private float mThumbX = 0;
    private float mThumbY = 0;
    private Paint mhsSeekBarBackgroundPaint = null;
    private Paint mhsSeekbarProgressPaint = null;
    private Paint mhsSeekBarMarkS = null;
    private Paint mhsSeekBarMarkM = null;
    private Paint P_MarkText = null;
    private RectF RectF_mhsbg = null;
    private RectF RectF_mhsprogress = null;
    private float mhsSeekbarLong=0;
    private int mhsSeekbarWidth=0;
    private int mhsSeekbarRound=0;
    
    private int mhsm_mark_with=0;
    private int mark_textsize=0;
        	
        	
    private float mhsprogress_set = 0;
    private int sb_left=0;
    private int sb_top=0;
    private int sb_right=0;
    private int sb_bottom=0;
    
    private int mViewHeight = 0;
    private int mViewWidth = 0;
    //private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private int mCurrentProgress = 0;
    private int mCurrentProgressMax = 0;
    private int old_mCurrentProgress = 0;
    /*�¼����� */  
    private OnMHSM_SeekBarChangeListener mOnMHSM_SeekBarChangeListener=null; 
    
    public MHSM_SeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initView();
    }
    
    public MHSM_SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initView();
    }
    
    public MHSM_SeekBar(Context context) {
        super(context);
        mContext = context;
        initView();
    }
    
    private void initView(){
        if(DEBUG) Log.d(TAG, "initView");
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.mhsm_seekbar);
        
        //thumb��������ʹ��android:thumb���Խ������õ�
        //���ص�DrawableΪһ��StateListDrawable���ͣ�������ʵ��ѡ��Ч����drawable list
        //mThumbNormal��mThumbPressed�����������ò�ͬ״̬��Ч���������thumbʱ����mThumbPressed����������mThumbNormal
        mThumbDrawable = localTypedArray.getDrawable(R.styleable.mhsb_seekbar_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
                android.R.attr.state_selected, android.R.attr.state_checked};
        localTypedArray.getDimension(R.styleable.mhsm_seekbar_mhsm_progress_width, 5);
        mhsSeekbarWidth = (int) localTypedArray.getDimension(R.styleable.mhsm_seekbar_mhsm_progress_width, 5);        
        mhsSeekbarRound = (int) localTypedArray.getDimension(R.styleable.mhsm_seekbar_mhsm_progress_bar_round, 4);
        
        int progressBackgroundColor = localTypedArray.getColor(R.styleable.mhsm_seekbar_mhsm_progress_background_color, getResources().getColor(R.color.text_color_xoverset));
        int progressFrontColor = localTypedArray.getColor(R.styleable.mhsm_seekbar_mhsm_progress_color, getResources().getColor(R.color.text_color_xoverset));
        mSeekBarMax = localTypedArray.getInteger(R.styleable.mhsm_seekbar_mhsm_progress_max, 60);
        mCurrentProgressMax=(int)mSeekBarMax;
        
        mhsm_mark_with=(int) localTypedArray.getDimension(R.styleable.mhsm_seekbar_mhsm_mark_with, 2);   
        mark_textsize=(int) localTypedArray.getDimension(R.styleable.mhsm_seekbar_mark_textsize, 12);   
        //----
        mhsSeekBarBackgroundPaint = new Paint();
        mhsSeekbarProgressPaint = new Paint();
        
        mhsSeekBarBackgroundPaint.setColor(progressBackgroundColor);
        mhsSeekbarProgressPaint.setColor(progressFrontColor);
        
        mhsSeekBarBackgroundPaint.setAntiAlias(true);
        mhsSeekbarProgressPaint.setAntiAlias(true);
        
        mhsSeekBarMarkS = new Paint();
        mhsSeekBarMarkS.setColor(progressBackgroundColor);
        mhsSeekBarMarkS.setAntiAlias(true);
        mhsSeekBarMarkS.setStrokeWidth(mhsm_mark_with); 
        mhsSeekBarMarkM = new Paint();
        mhsSeekBarMarkM.setColor(progressFrontColor);
        mhsSeekBarMarkM.setAntiAlias(true);
        mhsSeekBarMarkM.setStrokeWidth(mhsm_mark_with); 
        P_MarkText = new Paint(Paint.ANTI_ALIAS_FLAG); 
        P_MarkText.setColor(progressFrontColor);  
        P_MarkText.setStrokeWidth(mhsm_mark_with); 
		//P_FrameText.setTextAlign(Align.CENTER);
        P_MarkText.setTextSize(mark_textsize);
        localTypedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        setMeasuredDimension(width, height);
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(DEBUG) Log.d(TAG, "onMeasure");
        mViewWidth = width;//getWidth();
        mViewHeight = height;//getHeight();
        
        //mSeekBarCenterX = mViewWidth / 2;
        mSeekBarCenterY = mViewHeight / 2;
        //�ؼ���X��ƫ�����Ķ���֮һ��Y��ƫ�����Ķ���֮һ����seekbar�ܳ�Ϊ���õĸ߶ȼ�ȥ�����ֱ�� 
        //seekbar�Ĵ�С
        //seekbar�ĳ���
        mhsSeekbarLong = mViewWidth-mThumbWidth;
        //seekbar chs_progress and bar start point and stop point
        sb_left=mThumbWidth/2;
        sb_top=mViewHeight/6-mhsSeekbarWidth/2;
        
        sb_right=(int) (sb_left+mhsSeekbarLong);
        sb_bottom=mViewHeight/6+mhsSeekbarWidth/2;

        //seekbar chs_progress
        mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
        //draw Rect seekbar bg
        RectF_mhsbg = new RectF(sb_left,sb_top,sb_right,sb_bottom);
        //draw Rect seekbar chs_progress
        RectF_mhsprogress = new RectF(sb_left,sb_top,mhsprogress_set,sb_bottom);  
        //seekbar Thumb
        mThumbX=mhsprogress_set;
        mThumbY=mViewHeight/6-mThumbHeight/2;
        if(DEBUG) System.out.println("MHS mViewWidth:"+mViewWidth);
        if(DEBUG) System.out.println("MHS mViewHeight:"+mViewHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	if(mCurrentProgressMax<mCurrentProgress){
    		mCurrentProgress=mCurrentProgressMax;
    		mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
            //draw Rect seekbar bg
            RectF_mhsbg = new RectF(sb_left,sb_top,sb_right,sb_bottom);
            //draw Rect seekbar chs_progress
            RectF_mhsprogress = new RectF(sb_left,sb_top,mhsprogress_set,sb_bottom);  
            //seekbar Thumb
            mThumbX=mhsprogress_set;
    	}  
    	int p_top=(int) (mhsprogress_set+mThumbWidth/2);
    	RectF_mhsprogress = new RectF(sb_left,sb_top,p_top,sb_bottom);
    	//���������ȣ�����
    	canvas.drawRoundRect(RectF_mhsbg, mhsSeekbarRound, mhsSeekbarRound, mhsSeekBarBackgroundPaint);    	
    	canvas.drawRoundRect(RectF_mhsprogress, mhsSeekbarRound, mhsSeekbarRound, mhsSeekbarProgressPaint);
        drawThumbBitmap(canvas);
        drawMark(canvas);
        //drawProgressText(canvas);
        
        super.onDraw(canvas);
    }
    
    private void drawThumbBitmap(Canvas canvas) {
    	this.mThumbDrawable.setBounds((int) mThumbX, (int) mThumbY,
                (int) (mThumbX + mThumbWidth), (int) (mThumbY + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
    }
    private void drawMark(Canvas canvas){
    	float tap=(mhsSeekbarLong/mSeekBarMax);
    	float hs=mViewHeight/3+mViewHeight/18;
    	float hm=mViewHeight/3+mViewHeight/8;
    	float xst=0;
    	float ym=mSeekBarCenterY-mViewHeight/8;
    	String mint="0";
    	String maxt="-"+String.valueOf((int)mSeekBarMax);
    	for(int i=0;i<=mSeekBarMax;i++){
    		xst=sb_left+tap*i;
    		if(i%5==0){
    			canvas.drawLine(xst, mSeekBarCenterY, xst, hs, mhsSeekBarMarkM);
    		}else{
    			canvas.drawLine(xst, ym, xst, hm, mhsSeekBarMarkS);
    		}
    	}
    	canvas.drawText(maxt, sb_left-mark_textsize/3, mViewHeight*5/6, P_MarkText);
    	canvas.drawText("0", sb_left+mhsSeekbarLong-mark_textsize/3, mViewHeight*5/6, P_MarkText);
    }
  
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	//Log.v(TAG, "MHS ACTION_DOWN eventX = " + eventX);
            	//if(((eventX+mThumbWidth/4)>mThumbX)&&((eventX-mThumbWidth)<mThumbX)){
            	if(((eventX+mThumbWidth*3/2)>mThumbX)&&((eventX-mThumbWidth*2)<mThumbX)){
                	ThumbTouch=true;
                }else{
                	ThumbTouch=false;
                }
                break ;
                
            case MotionEvent.ACTION_MOVE:
            	//Log.v(TAG, "MHS ACTION_MOVE eventX = " + eventX);
                seekTo(eventX, eventY, ThumbTouch, true);
                break ;
                
            case MotionEvent.ACTION_UP:
                seekTo(eventX, eventY, false, false);
            	ThumbTouch=false;
            	mThumbDrawable.setState(mThumbNormal);
	            invalidate();
                break ;
            default: 
            	seekTo(eventX, eventY, false, false);
            	ThumbTouch=false;
            	mThumbDrawable.setState(mThumbNormal);
	            invalidate();
            	break;
        }
        return true;
    }

    private void seekTo(float eventX, float eventY, boolean isUp, boolean boolean_MotionEvent) {
    	eventX -= mThumbWidth/4;
    	if(CanTouch==true){    	
	        if (true == isUp) {
	            mThumbDrawable.setState(mThumbPressed);
	            if((eventX>mhsSeekbarWidth/4)&&(eventX < (mhsSeekbarLong))){
	            	mCurrentProgress =(int) (mSeekBarMax * eventX / mhsSeekbarLong);
	            }else if(eventX>mhsSeekbarLong){
	            	mCurrentProgress = (int) mSeekBarMax;
	            }else if(eventX<mhsSeekbarWidth/4){
	            	mCurrentProgress = 0;
	            }
	            
	            if ((mOnMHSM_SeekBarChangeListener != null)&&(old_mCurrentProgress!=mCurrentProgress)){ 
	            	old_mCurrentProgress=mCurrentProgress;
	            	if(mCurrentProgress<=mCurrentProgressMax){
	            		mOnMHSM_SeekBarChangeListener.onProgressChanged(this, mCurrentProgress, boolean_MotionEvent);  
	            	}	            	
	            }
	            
	        	mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
	        	//mThumbY=mhsprogress_set+mThumbHeight/2;
	        	mThumbX=mhsprogress_set;
	        	
	        	//Log.v(TAG, "MHS mCurrentProgress = " + mCurrentProgress);
	        	
	            invalidate();
	        }else{
	            mThumbDrawable.setState(mThumbNormal);
	            invalidate();
	        }
    	}else if(CanTouch==false){    	
    		 if ((mOnMHSM_SeekBarChangeListener != null)&&(old_mCurrentProgress!=mCurrentProgress)){ 
	            	old_mCurrentProgress=mCurrentProgress;
	        	mOnMHSM_SeekBarChangeListener.onProgressChanged(this, mCurrentProgress, boolean_MotionEvent);  
	    	}
        }
    }
    /*
     * ����setCanTouch������������java�����е���
     */
    public void setTouch(boolean touch){
    	CanTouch=touch;
    }
    
    /*
     * ����set������������java�����е���
     */
    public void setProgress(int progress) {
    	CanTouch=true;//�ڸ������ʱ�����setProgress(),��ʱ�ɻ���
        if(DEBUG) Log.v(TAG, "setProgress chs_progress = " + progress);
        if (progress > mSeekBarMax){
            progress = (int) mSeekBarMax;
        }
        if (progress < 0){
            progress = 0;
        }
        mCurrentProgress = progress;
        
        mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
    	//mThumbY=mhsprogress_set+mThumbHeight/2;
        mThumbX=mhsprogress_set;
    	
        invalidate();
    }
    public void setProgressStop(int max){
    	mCurrentProgressMax=max;
    }
    public int getProgress(){
        return mCurrentProgress;
    }
    
    public void setProgressMax(int max){
        if(DEBUG) Log.v(TAG, "setProgressMax max = " + max);
        mSeekBarMax = max;
        mCurrentProgressMax = max;
    }
    
    public int getProgressMax(){
        return (int) mSeekBarMax;
    }
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
       
    public void setProgressBackgroundColor(int color){
    	mhsSeekBarBackgroundPaint.setColor(color);
    }
    
    public void setProgressFrontColor(int color){
    	mhsSeekbarProgressPaint.setColor(color);
    }
       
    public void setIsShowProgressText(boolean isShow){
    }
    
    public void setOnSeekBarChangeListener(OnMHSM_SeekBarChangeListener l) {  
    	mOnMHSM_SeekBarChangeListener = l;  
    }  
  
    public interface OnMHSM_SeekBarChangeListener {  
  
        public abstract void onProgressChanged(MHSM_SeekBar mhs_SeekBar,  
                int progress, boolean fromUser);  
  
    }  
}

