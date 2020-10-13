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

public class MVS_SeekBar extends View {

    private final boolean DEBUG = false;
    private final String TAG = "MVS_SeekBar";
    
    private Context mContext = null;
    private AttributeSet mAttrs = null;
    
    private Drawable mThumbDrawable = null;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private boolean CanTouch=true;
    private float mSeekBarMax = 0;
    private float mThumbX = 0;
    private float mThumbY = 0;
    private Paint mvsSeekBarBackgroundPaint = null;
    private Paint mvsSeekbarProgressPaint = null;
    private RectF RectF_mvsbg = null;
    private RectF RectF_mvsprogress = null;
    private float mvsSeekbarHight=0;
    private int mvsSeekbarWidth=0;
    private int mvsSeekbarRound=0;
    private float mvsprogress_set = 0;
    private int sb_left=0;
    private int sb_top=0;
    private int sb_right=0;
    private int sb_bottom=0;
    
    private int mViewHeight = 0;
    private int mViewWidth = 0;
    private int mSeekBarCenterX = 0;
    private int mCurrentProgress = 0;
    private int old_mCurrentProgress = 0;
    /*�¼����� */  
    private OnMSBSeekBarChangeListener mOnMSBSeekBarChangeListener=null; 
    
    public MVS_SeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initView();
    }
    
    public MVS_SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initView();
    }
    
    public MVS_SeekBar(Context context) {
        super(context);
        mContext = context;
        initView();
    }
    
    private void initView(){
        if(DEBUG) Log.d(TAG, "initView");
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.mvsb_seekbar);
        
        //thumb��������ʹ��android:thumb���Խ������õ�
        //���ص�DrawableΪһ��StateListDrawable���ͣ�������ʵ��ѡ��Ч����drawable list
        //mThumbNormal��mThumbPressed�����������ò�ͬ״̬��Ч���������thumbʱ����mThumbPressed����������mThumbNormal
        mThumbDrawable = localTypedArray.getDrawable(R.styleable.mvsb_seekbar_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
                android.R.attr.state_selected, android.R.attr.state_checked};
        localTypedArray.getDimension(R.styleable.mvsb_seekbar_mvs_progress_width, 5);
        mvsSeekbarWidth = (int) localTypedArray.getDimension(R.styleable.mvsb_seekbar_mvs_progress_width, 5);        
        mvsSeekbarRound = (int) localTypedArray.getDimension(R.styleable.mvsb_seekbar_mvs_progress_bar_round, 4);
        
        int progressBackgroundColor = localTypedArray.getColor(R.styleable.mvsb_seekbar_mvs_progress_background_color, getResources().getColor(R.color.text_color_xoverset));
        int progressFrontColor = localTypedArray.getColor(R.styleable.mvsb_seekbar_mvs_progress_color, getResources().getColor(R.color.text_color_xoverset));
        mSeekBarMax = localTypedArray.getInteger(R.styleable.mvsb_seekbar_mvs_progress_max, 60);
        //----
        mvsSeekBarBackgroundPaint = new Paint();
        mvsSeekbarProgressPaint = new Paint();
        
        mvsSeekBarBackgroundPaint.setColor(progressBackgroundColor);
        mvsSeekbarProgressPaint.setColor(progressFrontColor);
        
        mvsSeekBarBackgroundPaint.setAntiAlias(true);
        mvsSeekbarProgressPaint.setAntiAlias(true);
   
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
        
        mSeekBarCenterX = mViewWidth / 2;
        //�ؼ���X��ƫ�����Ķ���֮һ��Y��ƫ�����Ķ���֮һ����seekbar�ܳ�Ϊ���õĸ߶ȼ�ȥ�����ֱ�� 
        //seekbar�Ĵ�С
        sb_left=mSeekBarCenterX-mvsSeekbarWidth/2;
        sb_top=mThumbHeight;
        sb_right=sb_left+mvsSeekbarWidth;
        sb_bottom=mViewHeight-mThumbHeight;
        //seekbar�ĳ���
        mvsSeekbarHight=mViewHeight-2*mThumbHeight;
        //seekbar chs_progress
        mvsprogress_set= mvsSeekbarHight/mSeekBarMax*(mSeekBarMax-mCurrentProgress);
        //draw Rect seekbar bg
        RectF_mvsbg = new RectF(sb_left,sb_top,sb_right,sb_bottom);
        //draw Rect seekbar chs_progress
        RectF_mvsprogress = new RectF(sb_left,mvsprogress_set,sb_right,sb_bottom);  
        //seekbar Thumb
        mThumbX=mSeekBarCenterX-mThumbWidth/2;
        mThumbY=mvsprogress_set+mThumbHeight/2;
        if(DEBUG) System.out.println("MVS mViewWidth:"+mViewWidth);
        if(DEBUG) System.out.println("MVS mViewHeight:"+mViewHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	int p_top=(int) (mvsprogress_set+mThumbHeight);
    	RectF_mvsprogress = new RectF(sb_left,p_top,sb_right,sb_bottom);
    	//���������ȣ�����
    	canvas.drawRoundRect(RectF_mvsbg, mvsSeekbarRound, mvsSeekbarRound, mvsSeekBarBackgroundPaint);
    	canvas.drawRoundRect(RectF_mvsprogress, mvsSeekbarRound, mvsSeekbarRound, mvsSeekbarProgressPaint);
        drawThumbBitmap(canvas);
        //drawProgressText(canvas);
        
        super.onDraw(canvas);
    }
    
    private void drawThumbBitmap(Canvas canvas) {
    	this.mThumbDrawable.setBounds((int) mThumbX, (int) mThumbY,
                (int) (mThumbX + mThumbWidth), (int) (mThumbY + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                seekTo(eventX, eventY, false);
                break ;
                
            case MotionEvent.ACTION_MOVE:
                seekTo(eventX, eventY, false);
                break ;
                
            case MotionEvent.ACTION_UP:
                seekTo(eventX, eventY, true);
                break ;
            default: 
            	seekTo(eventX, eventY, true);
            	break;
        }
        return true;
    }

    private void seekTo(float eventX, float eventY, boolean isUp) {
    	eventY -= mThumbHeight;
    	if(CanTouch==true){    	
	        if (false == isUp) {
	            mThumbDrawable.setState(mThumbPressed);
	            if((eventY>mvsSeekbarWidth/4)&&(eventY < (mvsSeekbarHight))){
	            	mCurrentProgress = (int) (mSeekBarMax - (int) (mSeekBarMax * eventY / mvsSeekbarHight));
	            }else if(eventY>mvsSeekbarHight){
	            	mCurrentProgress = 0;
	            }else if(eventY<mvsSeekbarWidth/4){
	            	mCurrentProgress = (int) mSeekBarMax;
	            }
	            
	            if ((mOnMSBSeekBarChangeListener != null)&&(old_mCurrentProgress!=mCurrentProgress)){  
	            	old_mCurrentProgress=mCurrentProgress;
	            	mOnMSBSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, true);  
	            }
	            
	        	mvsprogress_set= mvsSeekbarHight/mSeekBarMax*(mSeekBarMax-mCurrentProgress);
	        	mThumbY=mvsprogress_set+mThumbHeight/2;
	        	/*
	        	Log.v(TAG, "MVS mvsprogress_set = " + mvsprogress_set);
	            Log.v(TAG, "MVS mvsSeekbarHight = " + mvsSeekbarHight);
	            Log.v(TAG, "MVS mSeekBarMax = " + mSeekBarMax);
	        	Log.v(TAG, "MVS mCurrentProgress = " + mCurrentProgress);
	            Log.v(TAG, "MVS mThumbY = " + mThumbY);
	            */
	            invalidate();
	        }else{
	            mThumbDrawable.setState(mThumbNormal);
	            invalidate();
	        }
    	}else if(CanTouch==false){    	
    		if ((mOnMSBSeekBarChangeListener != null)&&(old_mCurrentProgress!=mCurrentProgress)){  
            	old_mCurrentProgress=mCurrentProgress;
	        	mOnMSBSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, true);  
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
        
        mvsprogress_set= mvsSeekbarHight/mSeekBarMax*(mSeekBarMax-mCurrentProgress);
    	mThumbY=mvsprogress_set+mThumbHeight/2;
    	//if (mOnMSBSeekBarChangeListener != null){  
        //    mOnMSBSeekBarChangeListener.onProgressChanged(this,mCurrentProgress, true); 
        //}
        invalidate();
    }
    
    public int getProgress(){
        return mCurrentProgress;
    }
    
    public void setProgressMax(int max){
        if(DEBUG) Log.v(TAG, "setProgressMax max = " + max);
        mSeekBarMax = max;
    }
    
    public int getProgressMax(){
        return (int) mSeekBarMax;
    }
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
       
    public void setProgressBackgroundColor(int color){
    	mvsSeekBarBackgroundPaint.setColor(color);
    }
    
    public void setProgressFrontColor(int color){
    	mvsSeekbarProgressPaint.setColor(color);
    }
       
    public void setIsShowProgressText(boolean isShow){
    }
    
    public void setOnSeekBarChangeListener(OnMSBSeekBarChangeListener l) {  
    	mOnMSBSeekBarChangeListener = l;  
    }  
  
    public interface OnMSBSeekBarChangeListener {  
  
        public abstract void onProgressChanged(MVS_SeekBar mvs_SeekBar,  
                int progress, boolean fromUser);  
  
    }  
}

