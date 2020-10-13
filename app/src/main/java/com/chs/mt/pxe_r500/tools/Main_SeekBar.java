package com.chs.mt.pxe_r500.tools;

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

import com.chs.mt.pxe_r500.R;

public class Main_SeekBar extends View {

    private final boolean DEBUG = false;
    private final String TAG = "MVS_SeekBar";

    private Context mContext = null;
    private AttributeSet mAttrs = null;

    private Drawable mThumbDrawable = null;
    private Drawable mProgressBgDrawable = null;
    private Drawable mProgressDrawable = null;


    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private boolean CanTouch=true;
    private boolean ThumbTouch=false;
    private boolean ThumbYMove=false;
    private boolean ThumbXMove=false;
    private float mSeekBarMax = 0;
    private float mThumbX = 0;
    private float mThumbY = 0;
    private float mDownY = 0;
    private float mDownX = 0;
    private float mDownMoveTH = 15;
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
    private float mseekbarLong = 0;
    private float mtap = 0;

    private OnMSBEQSeekBarChangeListener mOnMSBEQSeekBarChangeListener=null;

    public Main_SeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    public Main_SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    public Main_SeekBar(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView(){
        if(DEBUG) Log.d(TAG, "initView");
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.eq_seekbar);
        mThumbDrawable = localTypedArray.getDrawable(R.styleable.eq_seekbar_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();

        mProgressBgDrawable = getResources().getDrawable(R.drawable.chs_main_val_progress_normal);
       mProgressDrawable = getResources().getDrawable(R.drawable.chs_main_val_progress_press);

        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed,
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed,
                android.R.attr.state_selected, android.R.attr.state_checked};
        localTypedArray.getDimension(R.styleable.eq_seekbar_eq_progress_width, 5);
        mvsSeekbarWidth = (int) localTypedArray.getDimension(R.styleable.eq_seekbar_eq_progress_width, 5);
        mvsSeekbarRound = (int) localTypedArray.getDimension(R.styleable.eq_seekbar_eq_progress_bar_round, 4);

        int progressBackgroundColor = localTypedArray.getColor(R.styleable.eq_seekbar_eq_progress_background_color, getResources().getColor(R.color.text_color_xoverset) );
        int progressFrontColor = localTypedArray.getColor(R.styleable.eq_seekbar_eq_progress_color, getResources().getColor(R.color.text_color_xoverset));
        mSeekBarMax = localTypedArray.getInteger(R.styleable.eq_seekbar_eq_progress_max, 60);
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
        sb_left=mSeekBarCenterX-mvsSeekbarWidth/2;
        sb_top=mThumbHeight/2;
        sb_right=sb_left+mvsSeekbarWidth;
        sb_bottom=mViewHeight-mThumbHeight/2;
        mseekbarLong=mViewHeight-mThumbHeight;
        mvsSeekbarHight=mViewHeight-mThumbHeight;
        //seekbar chs_progress
        mvsprogress_set= mvsSeekbarHight/mSeekBarMax*(mSeekBarMax-mCurrentProgress);
        //draw Rect seekbar bg
        RectF_mvsbg = new RectF(sb_left,sb_top,sb_right,sb_bottom);
        //draw Rect seekbar chs_progress
        RectF_mvsprogress = new RectF(sb_left,mvsprogress_set,sb_right,sb_bottom);
        //seekbar Thumb
        mThumbX=mSeekBarCenterX-mThumbWidth/2;
        mThumbY=mvsprogress_set;
        if(DEBUG) System.out.println("MVS mViewWidth:"+mViewWidth);
        if(DEBUG) System.out.println("MVS mViewHeight:"+mViewHeight);

        mtap =  mseekbarLong/mSeekBarMax;
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	int p_top=(int) (mvsprogress_set+mThumbHeight/2);

    	int left=sb_left-mvsSeekbarWidth/2;
    	int right=sb_right+mvsSeekbarWidth/2;
    	RectF_mvsprogress = new RectF(left,p_top,right,sb_bottom);
        mvsSeekbarRound=mvsSeekbarWidth/2;
    	RectF_mvsprogress = new RectF(sb_left,p_top,sb_right,sb_bottom);
    	//±³¾°£¬½ø¶È£¬½¹µã

       drawBitmapProgressBg(canvas);
    //   drawBitmapProgress(canvas);
        canvas.drawRoundRect(RectF_mvsbg, mvsSeekbarRound, mvsSeekbarRound, mvsSeekBarBackgroundPaint);
    	canvas.drawRoundRect(RectF_mvsprogress, mvsSeekbarRound, mvsSeekbarRound, mvsSeekbarProgressPaint);
        drawThumbBitmap(canvas);
        //drawProgressText(canvas);

        super.onDraw(canvas);
    }

    private void drawBitmapProgressBg(Canvas canvas) {
        //int left, int top, int right, int bottom
        System.out.println("BUG 背景值未左边sb_left="+sb_left+"=上边="+sb_top+"==右边=="+sb_left+"==下边=="+(sb_top + mseekbarLong));

        this.mProgressBgDrawable.setBounds(mSeekBarCenterX-60, (int) sb_top,
                mSeekBarCenterX+60, (int) (sb_top + mseekbarLong));
        this.mProgressBgDrawable.draw(canvas);
    }
    private void drawBitmapProgress(Canvas canvas) {
        float drawlong = mtap*(mSeekBarMax - mCurrentProgress);
        this.mProgressDrawable.setBounds((int) mSeekBarCenterX-60, (int) (sb_top),
                mSeekBarCenterX+60, (int) (sb_top + drawlong));
        this.mProgressDrawable.draw(canvas);
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
            	//Log.v(TAG, "MVS mThumbY = " + mThumbY);
	            //Log.v(TAG, "MVS eventY = " + eventY);
                //if(((eventY+mThumbHeight/4)>mThumbY)&&((eventY-mThumbHeight)<mThumbY)){
            	if((((mThumbY+mThumbHeight*2)>eventY)&&((mThumbY-mThumbHeight)<eventY))&&
            	   (((mThumbX+mThumbWidth*3/2)>eventX)&&((mThumbX-mThumbWidth/2)<eventX))){
                	ThumbTouch=true;
                	mDownY=eventY;
                	mDownX=eventX;
                }else{
                	ThumbTouch=false;
                	return false;
                }
            	ThumbXMove=false;
            	ThumbYMove=false;
//            	Log.v(TAG, "MVS mDownY= " + mDownY);
//            	Log.v(TAG, "MVS ThumbTouch= " + ThumbTouch);
                break ;

            case MotionEvent.ACTION_MOVE:
//            	Log.v(TAG, "MVS mDownMoveTH = " + mDownMoveTH);
//            	Log.v(TAG, "MVS mDownY = " + mDownY);
//            	Log.v(TAG, "MVS eventY = " + eventY);
//            	Log.v(TAG, "MVS eventX = " + eventX);
//            	Log.v(TAG, "MVS eventX = " + eventX);
//            	Log.v(TAG, "MVS mDownX = " + mDownX);
            	if(((mDownY+mDownMoveTH)<eventY)||((mDownY-mDownMoveTH)>eventY)){
            		ThumbYMove=true;
            	}

        		if(((mDownX+20)<eventX)||((mDownX-20)>eventX)){
        			if(!ThumbYMove){
        				ThumbYMove=false;
        				ThumbTouch=false;
//        				Log.v(TAG, "MVS ##############");

                		return false;
        			}
            	}

            	//Log.v(TAG, "MVS ThumbMove = " + ThumbYMove);

            	if((ThumbYMove)&&(ThumbTouch)){
            		seekTo(eventX, eventY, ThumbTouch);
            	}
                break ;

            case MotionEvent.ACTION_UP:

            	ThumbTouch=false;
            	mThumbDrawable.setState(mThumbNormal);
	            invalidate();
                break ;
            default:
            	ThumbTouch=false;
            	mThumbDrawable.setState(mThumbNormal);
	            invalidate();
            	break;
        }
        return true;
    }

    private void seekTo(float eventX, float eventY, boolean isUp) {
    	eventY = eventY-mThumbHeight/2;
    	if(CanTouch==true){
	        if (true == isUp) {
	            mThumbDrawable.setState(mThumbPressed);
	            if((eventY>mvsSeekbarWidth/4)&&(eventY < (mvsSeekbarHight))){
	            	mCurrentProgress = (int) (mSeekBarMax - (int) (mSeekBarMax * eventY / mvsSeekbarHight));
	            }else if(eventY>mvsSeekbarHight){
	            	mCurrentProgress = 0;
	            }else if(eventY<mvsSeekbarWidth/4){
	            	mCurrentProgress = (int) mSeekBarMax;
	            }

	            if ((mOnMSBEQSeekBarChangeListener != null)){
	            	old_mCurrentProgress = mCurrentProgress;
	            	mOnMSBEQSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, true);
	            }

	        	mvsprogress_set= mvsSeekbarHight/mSeekBarMax*(mSeekBarMax-mCurrentProgress);
	        	mThumbY=mvsprogress_set;

	        	//Log.v(TAG, "MVS mvsprogress_set = " + mvsprogress_set);
	            //Log.v(TAG, "MVS mvsSeekbarHight = " + mvsSeekbarHight);
	            //Log.v(TAG, "MVS mSeekBarMax = " + mSeekBarMax);
	        	//Log.v(TAG, "MVS mCurrentProgress = " + mCurrentProgress);
	            //Log.v(TAG, "MVS mThumbY = " + mThumbY);
	            //Log.v(TAG, "MVS eventY = " + eventY);
	            invalidate();
	        }else{
	        	ThumbTouch=false;
	            mThumbDrawable.setState(mThumbNormal);
	            invalidate();
	        }
    	}else if(CanTouch==false){
    		ThumbTouch=false;
    		if ((mOnMSBEQSeekBarChangeListener != null)){
            	old_mCurrentProgress = mCurrentProgress;
	        	mOnMSBEQSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, true);
	    	}
        }
    }

    public void setTouch(boolean touch){
    	CanTouch=touch;
    }

    public void setProgress(int progress) {
    	CanTouch=true;
        if(DEBUG) Log.v(TAG, "setProgress chs_progress = " + progress);
        if (progress > mSeekBarMax){
            progress = (int) mSeekBarMax;
        }
        if (progress < 0){
            progress = 0;
        }
        mCurrentProgress = progress;

        mvsprogress_set= mvsSeekbarHight/mSeekBarMax*(mSeekBarMax-mCurrentProgress);
    	mThumbY=mvsprogress_set;

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

    public void setOnSeekBarChangeListener(OnMSBEQSeekBarChangeListener l) {
    	mOnMSBEQSeekBarChangeListener = l;
    }

    public interface OnMSBEQSeekBarChangeListener {

        public abstract void onProgressChanged(Main_SeekBar mvs_SeekBar,
                                               int progress, boolean fromUser);
  
    }  
}

