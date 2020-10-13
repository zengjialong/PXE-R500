package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;



import java.text.DecimalFormat;

import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ResourceAsColor") public class MHS_Num_SeekBar extends View {
	private String mText;   
    private String unit_show="";
    private int DelayUnit=2;
    private float mImgWidth;    
    //private Paint mPaint;
    private Resources res;    
    //private Bitmap bm;
    private int textsize = 13;    
    private int textColor = 13;    
    private int mmin=0;
    private int showhz=0;
    private int Freqhz=0;
    private boolean bool_showhz = false;
    private boolean ThumbTouch=false;
    private final boolean DEBUG = false;
    private final String TAG = "MHS_SeekBar";
    private Paint pai; 
    private Context mContext = null;
    private AttributeSet mAttrs = null;

    private Drawable mProgressBgDrawable = null;
    private Drawable mProgressDrawable = null;


    private Drawable mThumbTextBgDrawable = null;
    private Drawable mThumbDrawable = null;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int mThumbTextBgHeight = 0;
    private int mThumbTextBgWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private boolean CanTouch=true;
    private float mSeekBarMax = 0;
    private float mTextX = 0;
    private float mThumbX = 0;
    private float mThumbY = 0;
    
    private Paint mhsSeekBarBackgroundPaint = null;
    private Paint mhsSeekbarProgressPaint = null;
    private RectF RectF_mhsbg = null;
    private RectF RectF_mhsprogress = null;
    private float mtap = 0;
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
    private int HLP_Flag = 0;//0：其他模式，1：高通模式，3：低通模式
    private int HP_MaxP= 0;
    private int LP_MinP = 0;
    /*事件监听 */  
    private OnMHS_NumSeekBarChangeListener mOnMHS_NumSeekBarChangeListener=null; 
    
    public MHS_Num_SeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        //init();
        initView();
        
    }
    
    public MHS_Num_SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        //init();
        initView();
        
    }
    
    public MHS_Num_SeekBar(Context context) {
        super(context);
        mContext = context;
        //init();
        initView();
        
    }
    
    private void initView(){
        if(DEBUG) Log.d(TAG, "initView");
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.mhsb_seekbar);
        
        //thumb的属性是使用android:thumb属性进行设置的
        //返回的Drawable为一个StateListDrawable类型，即可以实现选中效果的drawable list
        //mThumbNormal和mThumbPressed则是用于设置不同状态的效果，当点击thumb时设置mThumbPressed，否则设置mThumbNormal
        mThumbTextBgDrawable = getResources().getDrawable(R.drawable.chs_popwindow_bg2);
        mThumbTextBgWidth = this.mThumbTextBgDrawable.getIntrinsicWidth();
        mThumbTextBgHeight = this.mThumbTextBgDrawable.getIntrinsicHeight();


        mProgressBgDrawable = getResources().getDrawable(R.drawable.chs_mhs_progress_normal);
        mProgressDrawable = getResources().getDrawable(R.drawable.chs_mhs_progress_press);


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
        
        textsize = (int) localTypedArray.getDimension(R.styleable.mhsb_seekbar_mhs_numtext_size, 5);     
        textColor = localTypedArray.getColor(R.styleable.mhsb_seekbar_mhs_numtext_color, getResources().getColor(R.color.blue));
        
        int progressBackgroundColor = localTypedArray.getColor(R.styleable.mhsb_seekbar_mhs_progress_background_color, getResources().getColor(R.color.text_color_xoverset));
        int progressFrontColor = localTypedArray.getColor(R.styleable.mhsb_seekbar_mhs_progress_color, getResources().getColor(R.color.text_color_xoverset));
        mSeekBarMax = localTypedArray.getInteger(R.styleable.mhsb_seekbar_mhs_progress_max, 60);
        //----
        mhsSeekBarBackgroundPaint = new Paint();
        mhsSeekbarProgressPaint = new Paint();
        
        mhsSeekBarBackgroundPaint.setColor(progressBackgroundColor);
        mhsSeekbarProgressPaint.setColor(progressFrontColor);
        
        mhsSeekBarBackgroundPaint.setAntiAlias(true);
        mhsSeekbarProgressPaint.setAntiAlias(true);
        
        // 添加游标上的文字  
        pai = new Paint(Paint.ANTI_ALIAS_FLAG); 
        pai.setColor(textColor);  
        pai.setStrokeWidth(mhsSeekbarWidth); 
		//P_FrameText.setTextAlign(Align.CENTER);
        pai.setTextSize(textsize);
        
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
        //控件的X轴偏左轴宽的二分之一，Y轴偏下轴宽的二分之一画，seekbar总长为设置的高度减去焦点的直径 
        //seekbar的大小
        //seekbar的长度
        mhsSeekbarLong = mViewWidth-mThumbWidth*2;
        //seekbar chs_progress and bar start point and stop point
        sb_left=(int) mThumbWidth;
       // sb_top=mViewHeight-mThumbHeight-mhsSeekbarWidth/2;
        sb_right=(int) (sb_left+mhsSeekbarLong);
        //sb_bottom=mViewHeight-mThumbHeight+mhsSeekbarWidth/2;
        sb_top=mSeekBarCenterY-mhsSeekbarWidth/2;
        sb_bottom=mSeekBarCenterY+mhsSeekbarWidth/2;

        //seekbar chs_progress
        mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
        //draw Rect seekbar bg
        RectF_mhsbg = new RectF(sb_left,sb_top,sb_right,sb_bottom);
        //draw Rect seekbar chs_progress
        RectF_mhsprogress = new RectF(sb_left,sb_top,mhsprogress_set,sb_bottom);  
        //seekbar Thumb
        //mThumbX=mhsprogress_set;
        mThumbX=mhsprogress_set + mThumbWidth/2;
        mThumbY=mSeekBarCenterY-mThumbHeight/2;
      //  mThumbY=mViewHeight-mThumbHeight;
        if(DEBUG) System.out.println("MHS mViewWidth:"+mViewWidth);
        if(DEBUG) System.out.println("MHS mViewHeight:"+mViewHeight);


        mtap =  mhsSeekbarLong/mSeekBarMax;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	if(HLP_Flag==1){//高通
    		if(mCurrentProgress>=HP_MaxP){
    			mCurrentProgress=HP_MaxP;
    		}
    	}else if(HLP_Flag==2){//低通
    		if(mCurrentProgress<=LP_MinP){
    			mCurrentProgress=LP_MinP;
    		}
    	}
    	mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
        mThumbX=mhsprogress_set + mThumbWidth/2;
    	
    	
    	if(unit_show.equals("dB")){
        	mText = (String.valueOf(getProgress()));
        }else if(unit_show.equals("Hz")){
        	if(bool_showhz==false){
        		showhz=(int) Define.FREQ241[mCurrentProgress];
        	}else{
        		showhz=Freqhz;
        	}
        	
        	if(showhz<mmin){
        		showhz=mmin;
        	}
        	mText = (String.valueOf(showhz)+unit_show);
        }else if(unit_show.equals("Delay")){
        	mText=(ChannelShowDelay(getProgress()));         	
        }else if(unit_show.equals("BW")){
        	mText=(ChangeBWValume(Define.EQ_BW_MAX-getProgress()));         	
        }else if(unit_show.equals("GainEQ")){       	
        	mText=ChangeGainValume(getProgress());     
        }else{
       
        	mText = (String.valueOf(getProgress()));
        }
    	//canvas.drawBitmap(bm, mThumbX-mThumbWidth/3, 0, mPaint);
    	mTextX = mThumbX+mThumbWidth/2+textsize-(mText.length())*textsize/4;
        //canvas.drawText(mText, mTextX, mSeekBarCenterY/2-textsize/2,pai);
        //canvas.drawText(mText, mTextX, textsize,pai);
    	//-------------------------
    	int p_top=(int) (mhsprogress_set+mThumbWidth);
    	RectF_mhsprogress = new RectF(sb_left,sb_top,p_top,sb_bottom);
    	//背景，进度，焦点
    	canvas.drawRoundRect(RectF_mhsbg, mhsSeekbarRound, mhsSeekbarRound, mhsSeekBarBackgroundPaint);
    	canvas.drawRoundRect(RectF_mhsprogress, mhsSeekbarRound, mhsSeekbarRound, mhsSeekbarProgressPaint);
//        drawBitmapProgressBg(canvas);
//        drawBitmapProgress(canvas);


        drawThumbBitmap(canvas);
      //  drawText(canvas);

        super.onDraw(canvas);
    }

    private void drawText(Canvas canvas) {
        Rect bounds = new Rect();
        pai.getTextBounds(mText, 0, mText.length(), bounds);
        Paint.FontMetricsInt fontMetrics = pai.getFontMetricsInt();
        int baseline = (mThumbHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mText,mTextX, baseline, pai);

        //canvas.drawText(mText, mTextX, textsize,pai);
    }

    private void drawBitmapProgressBg(Canvas canvas) {
        this.mProgressDrawable.setBounds((int) sb_left, (int) sb_top,
                (int) (sb_left + mhsSeekbarLong), (int) (sb_top + mhsSeekbarWidth));
        this.mProgressDrawable.draw(canvas);
    }
    private void drawBitmapProgress(Canvas canvas) {
        float drawlong = mtap* mCurrentProgress;
        this.mProgressBgDrawable.setBounds((int) (sb_left+drawlong), (int) (sb_top),
                (int) (sb_left + mhsSeekbarLong), (int) (sb_top + mhsSeekbarWidth));
        this.mProgressBgDrawable.draw(canvas);
    }

    private void drawThumbBitmap(Canvas canvas) {
//    	this.mThumbDrawable.setBounds((int) mThumbX+mThumbWidth/2, (int) mThumbY-mThumbHeight/2,
//                (int) (mThumbX + mThumbWidth+mThumbWidth/2), (int) (mThumbY + mThumbHeight/2));
//        this.mThumbDrawable.draw(canvas);

        this.mThumbDrawable.setBounds((int) mThumbX, (int) mThumbY,
                (int) (mThumbX + mThumbWidth), (int) (mThumbY + mThumbHeight));
        this.mThumbDrawable.draw(canvas);

        float x=mThumbX+mThumbWidth/2;
        float y=mThumbY-mThumbHeight-mThumbHeight/4;
        //Text Bg
//        this.mThumbTextBgDrawable.setBounds((int) x-mThumbWidth/2, (int) 0,
//                (int) (x+mThumbWidth*1.5), (int) 0+mThumbHeight);
//        this.mThumbTextBgDrawable.draw(canvas);
    }

  
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	//Log.v(TAG, "MHS ACTION_DOWN eventX = " + eventX);
            	bool_showhz=false;
            	//if(((eventX+mThumbWidth/2)>mThumbDrawX)&&((eventX-mThumbWidth*3/2)<mThumbDrawX)){
                	ThumbTouch=true;
                	seekTo(eventX, eventY, false, false);
               // }else{
               // 	ThumbTouch=false;
               // }
                
                break ;
                
            case MotionEvent.ACTION_MOVE:
            	if(ThumbTouch){
            		seekTo(eventX, eventY, false, true);
            	}                
                break ;
                
            case MotionEvent.ACTION_UP:
            	if(ThumbTouch){
            		seekTo(eventX, eventY, true, false);
            	}                  
                break ;
            default: 
            	seekTo(eventX, eventY, true, false);
            	break;
        }
        return ThumbTouch;
    }

    private void seekTo(float eventX, float eventY, boolean isUp, boolean boolean_MotionEvent) {
    	eventX -= mThumbWidth/4;
    	eventX -= mImgWidth/2; 
    	if(CanTouch==true){    	
	        if (false == isUp) {
	            mThumbDrawable.setState(mThumbPressed);
	            if((eventX>mhsSeekbarWidth/4)&&(eventX < (mhsSeekbarLong))){
	            	mCurrentProgress =(int) (mSeekBarMax * eventX / mhsSeekbarLong);
	            }else if(eventX>mhsSeekbarLong){
	            	mCurrentProgress = (int) mSeekBarMax;
	            }else if(eventX<mhsSeekbarWidth/4){
	            	mCurrentProgress = 0;
	            }
	            
	            if ((mOnMHS_NumSeekBarChangeListener != null)&&(mCurrentProgress!=old_mCurrentProgress)){  
	        		old_mCurrentProgress=mCurrentProgress;
	            	mOnMHS_NumSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, boolean_MotionEvent);  
	            }
	            
	        	mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
	        	//mThumbY=mhsprogress_set+mThumbHeight/2;
	        	//mThumbX=mhsprogress_set;
                mThumbX=mhsprogress_set + mThumbWidth/2;
	        	//Log.v(TAG, "MHS mCurrentProgress = " + mCurrentProgress);
	        	
	            invalidate();
	        }else{
	            mThumbDrawable.setState(mThumbNormal);
	            invalidate();
	        }
    	}else if(CanTouch==false){    	
    		if ((mOnMHS_NumSeekBarChangeListener != null)&&(mCurrentProgress!=old_mCurrentProgress)){  
        		old_mCurrentProgress=mCurrentProgress;
	        	mOnMHS_NumSeekBarChangeListener.onProgressChanged(this, mCurrentProgress, boolean_MotionEvent);  
	    	}
        }
    }
    /*
     * 增加setCanTouch方法，用于在java代码中调用
     */
    public void setTouch(boolean touch){
    	CanTouch=touch;
    }
    
    /*
     * 增加set方法，用于在java代码中调用
     */
    public void setProgress(int progress) {
    	CanTouch=true;//在更新完成时会调用setProgress(),此时可滑动
        if(DEBUG) Log.v(TAG, "setProgress chs_progress = " + progress);
        if (progress > mSeekBarMax){
            progress = (int) mSeekBarMax;
        }
        if (progress < 0){
            progress = 0;
        }
        mCurrentProgress = progress;
        
        mhsprogress_set= mhsSeekbarLong/mSeekBarMax*mCurrentProgress;
        mThumbX=mhsprogress_set+ mThumbWidth/2;;
        invalidate();
    }
    //获取Equalizer 的EQ的Q值数据显示
  	private String ChangeBWValume(int num){
  		String show = null;
  		DecimalFormat decimalFormat=new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
  		show=decimalFormat.format(Define.EQ_BW[num]);//format 返回的是字符串
  		return show;
  	}
  //获取Equalizer 的EQ的增益数据显示
  	private String ChangeGainValume(int num){
  		//System.out.println("ChangeValume:"+num);
  		String show = null;
  		DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
  		show=decimalFormat.format(0.0-(DataStruct.CurMacMode.EQ.EQ_Gain_MAX/2-num)/10.0);//format 返回的是字符串
  		show=show+"dB";
  		return show;
  	}
  	public void SetSeekbarGain(int mshowhz) {
  		//B_Gain[EQ_Num].setText(ChangeGainValume(RcvDeviceData.OUT_CH[OutputChannelSel].EQ[EQ_Num].level-DataStruct.EQ_LEVEL_MIN));
    	mText=(ChangeGainValume(mshowhz-DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
    	invalidate();
    }
    public void SetSeekbarBW(int mshowhz) {
    	mText=(ChangeBWValume(mshowhz));      
    	invalidate();
    }
    public void SetSeekbarFreq(int mshowhz) {
    	bool_showhz=true;
    	Freqhz=mshowhz;
    	invalidate();
    }
    public int getProgress(){
        return mCurrentProgress;
    }
    
    public void setMax(int max){
        if(DEBUG) Log.v(TAG, "setProgressMax max = " + max);
        mSeekBarMax = max;
    }

    //0：其他模式，1：高通模式，3：低通模式
    public void setHP_MaxP(int max){
    	HLP_Flag=1;
    	HP_MaxP = max;
    }
    public void setLP_MinP(int min){
    	HLP_Flag=2;
    	LP_MinP = min;
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
    
    public void setOnMHS_NumSeekBarChangeListener(OnMHS_NumSeekBarChangeListener l) {  
    	mOnMHS_NumSeekBarChangeListener = l;  
    }  
  
    public interface OnMHS_NumSeekBarChangeListener {  
  
        public abstract void onProgressChanged(MHS_Num_SeekBar mhs_SeekBar,  
                int progress, boolean fromUser);  
  
    }
    //----------------------------------
    /******* 延时时间转换  *******/
    private String ChannelShowDelay(int timedelay){
    	String delaytimes=new String();
    	switch(DelayUnit){
	    	case 1:delaytimes=CountDelayCM(timedelay);
	    		break;
	    	case 2:delaytimes=CountDelayMs(timedelay);
	    		break;
	    	case 3:delaytimes=CountDelayInch(timedelay);
	    		break;
    		default: break;
    	}
    	return delaytimes;
    }
    private String  CountDelayCM(int num){
    	DecimalFormat decimalFormat=new DecimalFormat("0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
    	String p="";
    	int m_nTemp=75;
    	float Time = (float) (num/48.0); //当Delay〈476时STEP是0.021MS；
    	float LMT = (float) (((m_nTemp-50)*0.6+331.0)/1000.0*Time);
    	LMT = LMT*100;
    	//float LFT = (float) (LMT*3.2808*12.0);

    	int fr=(int) (LMT*10);
    	int ir = fr%10;
    	int ri = 0;
    	if(ir>=5){
    		ri=fr/10+1;
    	}else{
    		ri=fr/10;
    	}
    	p=decimalFormat.format(ri);
    	return p;
    }
    private String  CountDelayMs(int num){
    	DecimalFormat decimalFormat=new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
    	String p="";
    	int fr = num*10000/48;
    	int ir = fr%10;
    	int ri = 0;
    	if(ir>=5){
    		ri=fr/10+1;
    	}else{
    		ri=fr/10;
    	}
    	p=decimalFormat.format(ri/1000.0);
    	return p;
    }/******* 延时时间转换  *******/
    private String  CountDelayInch(int num){
    	DecimalFormat decimalFormat=new DecimalFormat("0");
    	String p="";
    	
    	float base=(float) 331.0;
    	if(num==DataStruct.CurMacMode.Delay.MAX){
    		base=(float) 331.4;
    	}
    	int m_nTemp=75;
    	float Time = (float) (num/48.0); //当Delay〈476时STEP是0.021MS；
    	float LMT = (float) (((m_nTemp-50)*0.6+base)/1000.0*Time);   
 	
    	float LFT = (float) (LMT*3.2808*12.0);

    	int fr=(int) (LFT*10);
    	int ir = fr%10;
    	int ri = 0;
    	if(ir>=5){
    		ri=fr/10+1;
    	}else{
    		ri=fr/10;
    	}
    	p=decimalFormat.format(ri);
    	return p;
    }
    ///--------------------------
    public void setTextUnit(String unit) {
    	unit_show=unit;
    }
    public void setDelayUnit(int unit) {
    	DelayUnit=unit;
    }
    public void SetSeekbarMin(int min) {
        mmin=min;
    }
    
}

