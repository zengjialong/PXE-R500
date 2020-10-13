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

public class MHS_SeekBarProgress extends View {

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
    private RectF RectF_mhsbg = null;
    private RectF RectF_mhsprogress = null;
    private float mhsSeekbarLong=0;
    private int mhsSeekbarWidth=0;
    private int mhsSeekbarRound=0;
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
    private int old_mCurrentProgress = 0;
    /*�¼����� */
    private OnMSBSeekBarChangeListener mOnMSBSeekBarChangeListener=null;

    public MHS_SeekBarProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    public MHS_SeekBarProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    public MHS_SeekBarProgress(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView(){
        if(DEBUG) Log.d(TAG, "initView");
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.mhsb_seekbar);

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
        localTypedArray.getDimension(R.styleable.mhsb_seekbar_mhs_progress_width, 5);
        mhsSeekbarWidth = (int) localTypedArray.getDimension(R.styleable.mhsb_seekbar_mhs_progress_width, 5);
        mhsSeekbarRound = (int) localTypedArray.getDimension(R.styleable.mhsb_seekbar_mhs_progress_bar_round, 4);

        int progressBackgroundColor = localTypedArray.getColor(R.styleable.mhsb_seekbar_mhs_progress_background_color, getResources().getColor(R.color.color_grey));
        int progressFrontColor = localTypedArray.getColor(R.styleable.mhsb_seekbar_mhs_progress_color, getResources().getColor(R.color.text_color_xoverset));
        mSeekBarMax = localTypedArray.getInteger(R.styleable.mhsb_seekbar_mhs_progress_max, 60);
        //----
        mhsSeekBarBackgroundPaint = new Paint();
        mhsSeekbarProgressPaint = new Paint();

        mhsSeekBarBackgroundPaint.setColor(progressBackgroundColor);
        mhsSeekbarProgressPaint.setColor(progressFrontColor);

        mhsSeekBarBackgroundPaint.setAntiAlias(true);
        mhsSeekbarProgressPaint.setAntiAlias(true);

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
        //mhsSeekbarLong = mViewWidth-mThumbWidth;
        mhsSeekbarLong = mViewWidth;//*2
        //seekbar chs_progress and bar start point and stop point
        //sb_left=mThumbWidth/2;
        sb_left=0;

        sb_top=mSeekBarCenterY-mhsSeekbarWidth/2;
        sb_right=(int) (sb_left+mhsSeekbarLong);
        sb_bottom=mSeekBarCenterY+mhsSeekbarWidth/2;

        //seekbar chs_progress
        mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
        //draw Rect seekbar bg
        RectF_mhsbg = new RectF(sb_left,sb_top,sb_right,sb_bottom);
        //draw Rect seekbar chs_progress
        RectF_mhsprogress = new RectF(sb_left,sb_top,mhsprogress_set,sb_bottom);
        //seekbar Thumb
        mThumbX=mhsprogress_set + mThumbWidth/2;
        mThumbY=mSeekBarCenterY-mThumbHeight/2;
        if(DEBUG) System.out.println("MHS mViewWidth:"+mViewWidth);
        if(DEBUG) System.out.println("MHS mViewHeight:"+mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	int p_top=(int) (mhsprogress_set);

//    	int top=sb_top-mhsSeekbarWidth/2;
//    	int bottom=sb_bottom+mhsSeekbarWidth/2;
//    	RectF_mhsprogress = new RectF(sb_left,top,p_top,bottom);

    	RectF_mhsprogress = new RectF(sb_left,sb_top,p_top,sb_bottom);
    	//±³¾°£¬½ø¶È£¬½¹µã
    	canvas.drawRoundRect(RectF_mhsbg, mhsSeekbarRound, mhsSeekbarRound, mhsSeekBarBackgroundPaint);
    	canvas.drawRoundRect(RectF_mhsprogress, mhsSeekbarRound, mhsSeekbarRound, mhsSeekbarProgressPaint);
//        drawThumbBitmap(canvas);
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
    	if(!CanTouch){
    		return true;
    	}
//        float eventX = event.getX();
//        float eventY = event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            	//Log.v(TAG, "MHS ACTION_DOWN eventX = " + eventX);
//            	if(((eventX+mThumbWidth/4)>mThumbX)&&((eventX-mThumbWidth)<mThumbX)){
//                	ThumbTouch=true;
//                }else{
//                	ThumbTouch=false;
//                }
//                break ;
//
//            case MotionEvent.ACTION_MOVE:
//            	//Log.v(TAG, "MHS ACTION_MOVE eventX = " + eventX);
//                seekTo(eventX, eventY, ThumbTouch, true);
//                break ;
//
//            case MotionEvent.ACTION_UP:
//                seekTo(eventX, eventY, false, false);
//            	//ThumbTouch=true;
//            	mThumbDrawable.setState(mThumbNormal);
//	            invalidate();
//                break ;
//            default:
//            	seekTo(eventX, eventY, false, false);
//            	//ThumbTouch=false;
//            	mThumbDrawable.setState(mThumbNormal);
//	            invalidate();
//            	break;
//        }
        return ThumbTouch;
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

	            if ((mOnMSBSeekBarChangeListener != null)&&(mCurrentProgress!=old_mCurrentProgress)){
	        		old_mCurrentProgress=mCurrentProgress;
	            	mOnMSBSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, boolean_MotionEvent);
	            }

	        	mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
	        	//mThumbY=mhsprogress_set+mThumbHeight/2;
	        	mThumbX=mhsprogress_set + mThumbWidth/2;

	        	//Log.v(TAG, "MHS mCurrentProgress = " + mCurrentProgress);

	            invalidate();
	        }else{
	        	if ((mOnMSBSeekBarChangeListener != null)&&(mCurrentProgress!=old_mCurrentProgress)){
	        		old_mCurrentProgress=mCurrentProgress;
		        	mOnMSBSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, false);
		    	}
	            mThumbDrawable.setState(mThumbNormal);
	            invalidate();
	        }
    	}else if(CanTouch==false){
    		if ((mOnMSBSeekBarChangeListener != null)&&(mCurrentProgress!=old_mCurrentProgress)){
        		old_mCurrentProgress=mCurrentProgress;
	        	mOnMSBSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, boolean_MotionEvent);
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
        mThumbX=mhsprogress_set + mThumbWidth/2;
        //if (mOnMSBSeekBarChangeListener != null){
        //	mOnMSBSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, false);
    	//}�����ã�
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
    	mhsSeekBarBackgroundPaint.setColor(color);
    }

    public void setProgressFrontColor(int color){
    	mhsSeekbarProgressPaint.setColor(color);
    }

    public void setIsShowProgressText(boolean isShow){
    }

    public void setOnSeekBarChangeListener(OnMSBSeekBarChangeListener l) {
    	mOnMSBSeekBarChangeListener = l;
    }

    public interface OnMSBSeekBarChangeListener {

        public abstract void onProgressChanged(MHS_SeekBarProgress mhs_SeekBar,
                                               int progress, boolean fromUser);
  
    }  
}

