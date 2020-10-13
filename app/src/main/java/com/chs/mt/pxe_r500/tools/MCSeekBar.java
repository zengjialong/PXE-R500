package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;



import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.RectF;  
import android.graphics.SweepGradient;  
import android.graphics.drawable.Drawable;
import android.os.Bundle;  
import android.os.Parcelable;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
  
public class MCSeekBar extends View {
	private OnMCSeekBarChangeListener mOnMCSeekBarChangeListener;  
    //����״̬    
    private static final String STATE_PARENT = "parent";  
    private static final String STATE_ANGLE = "angle";  
  
    //Բ��paint����   
    private Paint mColorOutsideWheelPaint;//�⻷  
    private Paint mColorWheelPaint;       //seekbar����
    private Paint mColorWheelProgresPaint;//������seekbar
    private Paint mColorInsideCirclePaint;//��Բ
    private Paint mColorUnitPaint;//��λ
    private Paint mColorProgressPaint;//chs_progress
    //Բ���Ŀ��  
    private int mColorWheelStrokeWidth = 10;  
    private RectF mColorWheelRectangle = new RectF();  
    private float mTranslationOffset;  
  
    //thumb
    private Drawable mThumbDrawable = null;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private float mThumbLeft = 0;
    private float mThumbTop = 0;
    
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private float mSeekBarSize = 0;
    private int mSeekBaroutsideRadius = 0;//�⻷�뾶
    private int mSeekBarRadius = 0;//seekbar�뾶
    private int mSeekBarInsideCircleRadius = 0;//seekbar��Բ�뾶
    private float mSeekBarDegree = 0;
    private float mSeekBarMaxDegree = 0;
    private int mCurrentProgress = 0;
    //private boolean DEBUG=false;
    private float seekbar_outside_width = 0;
    //��ɫ
    private int seekbar_outside_color;
    private int seekbar_bg_color;
    private int seekbar_inside_color;
    private int seekbar_progress_bg_color;
    private int seekbar_progress_text_color;
    private int seekbar_progress_unit_color;
    //��ʾ���嵥λ�ʹ�С
    private String progress_unit_text="dB";
    private int progress_textsize=0;
    private int progress_unit_textsize=0; 
    private String text_seekbar_progress="0";  
    
    private int conversion = 0;  
    private int mSeekBarMax = 100;  
    private SweepGradient s;  
    // ���½ǿ�ʼ  
    private int start_arc = 135;  
    private int end_wheel;  
    
    

    
    
    private Context mContext = null;
  
    public MCSeekBar(Context context) {      	
        super(context);  
        mContext = context;
        init(null, 0);  
    }  
  
    public MCSeekBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
        init(attrs, 0);  
    }  
  
    public MCSeekBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        mContext = context;
        init(attrs, defStyle);  
    }  
  
    private void init(AttributeSet attrs, int defStyle) {  
        final TypedArray a = getContext().obtainStyledAttributes(attrs,  
                R.styleable.MC_SeekBar, defStyle, 0);  
  
        initAttributes(a);  
  
        a.recycle();  
        // mAngle = (float) (-Math.PI / 2);  
        //�⻷
        mColorOutsideWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorOutsideWheelPaint.setShader(s);  
        mColorOutsideWheelPaint.setColor(seekbar_outside_color);  
        mColorOutsideWheelPaint.setStyle(Paint.Style.STROKE);  
        mColorOutsideWheelPaint.setStrokeWidth(seekbar_outside_width);  
        //seekbar ����
        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorWheelPaint.setShader(s);  
        mColorWheelPaint.setColor(seekbar_bg_color);  
        mColorWheelPaint.setStyle(Paint.Style.STROKE);  
        mColorWheelPaint.setStrokeWidth(mColorWheelStrokeWidth); 
        //seekbar Progress        
        mColorWheelProgresPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorWheelProgresPaint.setShader(s);  
        mColorWheelProgresPaint.setColor(seekbar_progress_bg_color);  
        mColorWheelProgresPaint.setStyle(Paint.Style.STROKE);  
        mColorWheelProgresPaint.setStrokeWidth(mColorWheelStrokeWidth); 
        //��Բ
        mColorInsideCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorInsideCirclePaint.setShader(s);  
        mColorInsideCirclePaint.setColor(seekbar_inside_color); 
        //Text ��λ
        mColorUnitPaint = new Paint();  
        mColorUnitPaint.setColor(seekbar_progress_unit_color);  
        mColorUnitPaint.setTextSize(progress_unit_textsize);  
        //Text chs_progress
        mColorProgressPaint = new Paint();  
        mColorProgressPaint.setColor(seekbar_progress_text_color);  
        mColorProgressPaint.setTextSize(progress_textsize);  
        invalidate();  
    }  
  
    private void initAttributes(TypedArray a) {  
  
    	mSeekBarMax = a.getInteger(R.styleable.MC_SeekBar_mc_max, 100);  
        //seekbar����ʼ�ǶȺͽ����Ƕ�
        start_arc = a.getInteger(R.styleable.MC_SeekBar_mc_start_angle, 0);  
        end_wheel = a.getInteger(R.styleable.MC_SeekBar_mc_end_angle, 360);  
        mSeekBarMaxDegree = end_wheel-start_arc;
        if(mSeekBarMaxDegree<0){
        	mSeekBarMaxDegree+=360;
        }
        mSeekBarDegree = (mCurrentProgress * mSeekBarMaxDegree / mSeekBarMax);

        //���δ�С����
        mColorWheelStrokeWidth = (int) a.getDimension(R.styleable.MC_SeekBar_mc_seekbar_bg_width, 6);
        //��ɫ����
        seekbar_outside_color = a.getColor(R.styleable.MC_SeekBar_mc_seekbar_outside_color, Color.BLUE);
        seekbar_bg_color = a.getColor(R.styleable.MC_SeekBar_mc_seekbar_bg_color,Color.GRAY);
        seekbar_inside_color = a.getColor(R.styleable.MC_SeekBar_mc_seekbar_inside_color,Color.WHITE);
        seekbar_progress_bg_color = a.getColor(R.styleable.MC_SeekBar_mc_seekbar_progress_bg_color,Color.GREEN);
        seekbar_progress_text_color = a.getColor(R.styleable.MC_SeekBar_mc_seekbar_progress_text_color,Color.YELLOW);
        seekbar_progress_unit_color = a.getColor(R.styleable.MC_SeekBar_mc_seekbar_progress_unit_color,Color.BLACK);
  
        /*thumb*/
        mThumbDrawable = a.getDrawable(R.styleable.MC_SeekBar_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
                android.R.attr.state_selected, android.R.attr.state_checked};
        seekbar_outside_width=mThumbWidth/2;
        //��ʾ���嵥λ�ʹ�С
        progress_unit_text=a.getString(R.styleable.MC_SeekBar_mc_seekbar_progress_unit_text);  
        progress_textsize=a.getInteger(R.styleable.MC_SeekBar_mc_seekbar_progress_textsize, 100);  
        progress_unit_textsize=a.getInteger(R.styleable.MC_SeekBar_mc_seekbar_progress_unit_textsize, 50);  
        text_seekbar_progress=String.valueOf(-mSeekBarMax);
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        canvas.translate(mTranslationOffset, mTranslationOffset);  
        //�⻷
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBaroutsideRadius, mColorOutsideWheelPaint);
        //��Բ
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarInsideCircleRadius, mColorInsideCirclePaint);
        //seekbar����
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius, mColorWheelPaint);
        //seekbar Progress
        canvas.drawArc(mColorWheelRectangle, start_arc, mSeekBarDegree, false, mColorWheelProgresPaint);  
        //seekbar ��λ���ַ�
        canvas.drawText(progress_unit_text, mSeekBarCenterX-progress_unit_textsize/2, 
        		mSeekBarCenterY-progress_textsize/2,mColorUnitPaint); 
        //seekbar chs_progress���ַ�
        int text_seekbar_progress_x=0;
        switch(text_seekbar_progress.length()){
	        case 1:
	        	text_seekbar_progress_x=progress_textsize*3/13;
	        	break;
	        case 2:
	        	text_seekbar_progress_x=progress_textsize*3/7;
	        	break;
	        case 3:
	        	text_seekbar_progress_x = progress_textsize*3/4;
	        	break;
        	default: 
        		text_seekbar_progress_x = progress_textsize*3/4;
        		break;
        }
        canvas.drawText(text_seekbar_progress, mSeekBarCenterX-text_seekbar_progress_x,
        		mSeekBarCenterY+progress_textsize/3+progress_unit_textsize/2,mColorProgressPaint); 
        //thumb
        drawThumbBitmap(canvas);
    }  
    
    private void drawThumbBitmap(Canvas canvas) {
        this.mThumbDrawable.setBounds((int) mThumbLeft, (int) mThumbTop,
                (int) (mThumbLeft + mThumbWidth), (int) (mThumbTop + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
    }
    
    private void setThumbPosition(double radian) {
        //if(DEBUG) Log.v(TAG, "setThumbPosition radian = " + radian);
        double x = mSeekBarCenterX + mSeekBarRadius * Math.cos(radian);
        double y = mSeekBarCenterY + mSeekBarRadius * Math.sin(radian);
        mThumbLeft = (float) (x - mThumbWidth / 2);
        mThumbTop = (float) (y - mThumbHeight / 2);
    }
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        int min = Math.min(width, height);  
        setMeasuredDimension(min, min);  

        mSeekBarCenterX = min / 2;
        mSeekBarCenterY = min / 2;   
        mSeekBarSize = min;
        //��Բ�İ뾶
        mSeekBarRadius = (int) (mSeekBarSize / 2 - mThumbWidth);
        mSeekBaroutsideRadius = mSeekBarRadius+mThumbWidth/4;
        mSeekBarInsideCircleRadius=mSeekBarRadius-mColorWheelStrokeWidth/2;
        //thumbλ��
        setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
        //seekbar progressλ��
        mColorWheelRectangle.set(mSeekBarCenterX-mSeekBarRadius, mSeekBarCenterX-mSeekBarRadius, 
        		mSeekBarCenterX+mSeekBarRadius, mSeekBarCenterX+mSeekBarRadius);         
    }  
  
    public int getValue() {  
        return conversion;  
    }  
    private void SeekTo(float x,float y, boolean press){
    	if(press){
    		mThumbDrawable.setState(mThumbPressed);
    	}else{
    		mThumbDrawable.setState(mThumbNormal);
    	}
    	
    	double radian = Math.atan2(y - mSeekBarCenterY, x - mSeekBarCenterX);
        /*
          * ����atan2���ص�ֵΪ[-pi,pi]
          * �����Ҫ������ֵת��һ�£�ʹ������Ϊ[0,2*pi]
         */
        if (radian < 0){
             radian = radian + 2*Math.PI;
        }            
        
        mSeekBarDegree = Math.round(Math.toDegrees(radian));
        mSeekBarDegree-=start_arc;
        if(mSeekBarDegree<0){
        	mSeekBarDegree += 360;
        }
        if((mSeekBarDegree >= 0) && (mSeekBarDegree <= mSeekBarMaxDegree)){//�����Ĵ��Բ
        	mCurrentProgress = (int) (mSeekBarMax * mSeekBarDegree / mSeekBarMaxDegree);
        }else if((mSeekBarDegree>mSeekBarMaxDegree)&&(mSeekBarDegree<(mSeekBarMaxDegree+15))){//�ҽ���
        	mSeekBarDegree = mSeekBarMaxDegree;
        	mCurrentProgress = mSeekBarMax;
        }else if((mSeekBarDegree > (360-15))&&(mSeekBarDegree<360)){//�����
        	mSeekBarDegree = 0;
        	mCurrentProgress = 0;
        }else if((mSeekBarDegree >= (mSeekBarMaxDegree+15)&&(mSeekBarDegree < (360-15)))){//�ײ�С��Բ
            mCurrentProgress = (int) (mSeekBarMax * (mSeekBarDegree-(mSeekBarMaxDegree +15) )/ (90-15*2));
            mCurrentProgress = mSeekBarMax - mCurrentProgress;
            mSeekBarDegree = mSeekBarMaxDegree/mSeekBarMax*mCurrentProgress;
        }
        setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
        
        if(mCurrentProgress==mSeekBarMax){
        	text_seekbar_progress=String.valueOf(mSeekBarMax-mCurrentProgress);
        }else{
        	text_seekbar_progress=String.valueOf(0-(mSeekBarMax-mCurrentProgress));
        }
        
        invalidate();
        if (mOnMCSeekBarChangeListener != null)  
            mOnMCSeekBarChangeListener.onProgressChanged(this,mCurrentProgress, true);  
    }
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        // Convert coordinates to our internal coordinate system  
        float x = event.getX() - mTranslationOffset;  
        float y = event.getY() - mTranslationOffset;  
  
        switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	        	SeekTo(x,y, true);
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	            SeekTo(x,y, true);
	            break;  
	        case MotionEvent.ACTION_UP:  
	        	mThumbDrawable.setState(mThumbNormal);
	            invalidate();  
	            break;  
	        default: 
	        	mThumbDrawable.setState(mThumbNormal);
	        	invalidate();  
	        	break;
        }  
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {  
            getParent().requestDisallowInterceptTouchEvent(true);  
        }  
        return true;  
    }  
  
    @Override  
    protected Parcelable onSaveInstanceState() {  
        Parcelable superState = super.onSaveInstanceState();  
  
        Bundle state = new Bundle();  
        state.putParcelable(STATE_PARENT, superState);  
        state.putFloat(STATE_ANGLE, mCurrentProgress);  
  
        return state;  
    }  
  
    @Override  
    protected void onRestoreInstanceState(Parcelable state) {  
        Bundle savedState = (Bundle) state;  
  
        Parcelable superState = savedState.getParcelable(STATE_PARENT);  
        super.onRestoreInstanceState(superState);  
  
        mCurrentProgress = (int) savedState.getFloat(STATE_ANGLE);  
        mSeekBarDegree = (mCurrentProgress * mSeekBarMaxDegree / mSeekBarMax);
        if(mCurrentProgress==mSeekBarMax){
        	text_seekbar_progress=String.valueOf(mSeekBarMax-mCurrentProgress);
        }else{
        	text_seekbar_progress=String.valueOf(0-(mSeekBarMax-mCurrentProgress));
        }
        setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
  
    }  
  
    public void setOnMCSeekBarChangeListener(OnMCSeekBarChangeListener l) {  
        mOnMCSeekBarChangeListener = l;  
    }  
  
    public interface OnMCSeekBarChangeListener {  
  
        public abstract void onProgressChanged(MCSeekBar mcSeekBar,  
                int progress, boolean fromUser);  
  
    }  
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
    
    public void setProgressMax(int max){
        mSeekBarMax = max;
    }
    
    /*
     * ����set������������java�����е���
     */
    public void setProgress(int progress) {
        if (progress > mSeekBarMax){
            progress = mSeekBarMax;
        }
        if (progress < 0){
            progress = 0;
        }
        mCurrentProgress = progress;
        mSeekBarDegree = (progress * mSeekBarMaxDegree / mSeekBarMax);
        if(mCurrentProgress==mSeekBarMax){
        	text_seekbar_progress=String.valueOf(mSeekBarMax-mCurrentProgress);
        }else{
        	text_seekbar_progress=String.valueOf(0-(mSeekBarMax-mCurrentProgress));
        }
        setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
        
        invalidate();
    }
}  