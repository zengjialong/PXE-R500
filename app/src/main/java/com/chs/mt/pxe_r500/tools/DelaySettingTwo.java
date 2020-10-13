package com.chs.mt.pxe_r500.tools;
  
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.R;
import android.annotation.SuppressLint;
import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.RectF;  
import android.graphics.SweepGradient;  
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
  
@SuppressLint("ClickableViewAccessibility") public class DelaySettingTwo extends View {
	
	//private boolean DEBUG = true false ;
	private boolean DEBUG = false;
	private OnDelaySettingTwoChangeListener mOnDelaySettingTwoChangeListener;  

	private float windHeight = 0;
	private float windWidth = 0;
	private float MidWindWidth = 0;
	private float MidWindHeight = 0;
    //thumb
    private Drawable mThumbDrawable = null;
	private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private float mThumbTX = 0;
	private float mThumbTY = 0;
	private float mThumbDownTX = 0;
	private float mThumbDownTY = 0;
	private boolean ThumbTouch=false;
	private float mThumbDrawX = 0;
	private float mThumbDrawY = 0;

	private float mThumbPoint1X = 0;
	private float mThumbPoint1Y = 0;

    private int arc_color;
    private int startarc=1;
    private float rd1 = 0;
    private float rd2 = 0;
    private float rd3 = 0;
    private float rd4 = 0;
    
    private double xtap=0;
    private double ytap=0;
    private double rtap=0;
    private int[] progress={0,0,0,0};
    private double[] rr={0,0,0,0};
    
    private double xMaxtap=0;
    private double yMaxtap=0;
    private boolean booSetDraw=false;
    //RectF
    private RectF RectF_Point1 = new RectF();  
    private RectF RectF_Point2 = new RectF();  
	//Paint
    private Paint Paint_Arc;
    //private Paint Paint_Point2;
    
    private float arc_width = 0;
    private int arc_angle = 0;
    private float arc_interval = 0;
    private SweepGradient s;  
    
    
    private Context mContext = null;  
    public DelaySettingTwo(Context context) {      	
        super(context);  
        mContext = context;
        init(null, 0);  
    }  
  
    public DelaySettingTwo(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
        init(attrs, 0);  
    }  
  
    public DelaySettingTwo(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        mContext = context;
        init(attrs, defStyle);  
    }  
  
    private void init(AttributeSet attrs, int defStyle) {  
        final TypedArray a = getContext().obtainStyledAttributes(attrs,  
                R.styleable.SField, defStyle, 0);  
  
        initAttributes(a);    
        a.recycle();
        //TODO
        
        Paint_Arc = new Paint(Paint.ANTI_ALIAS_FLAG);  
        Paint_Arc.setShader(s);  
        Paint_Arc.setColor(arc_color);  
        Paint_Arc.setStyle(Paint.Style.STROKE);  
        Paint_Arc.setStrokeWidth(arc_width);
        Paint_Arc.setAntiAlias(true);//���û���Ϊ�޾��
        Paint_Arc.setStrokeCap(Paint.Cap.ROUND);
        
        
        invalidate();  
    }  
  
    private void initAttributes(TypedArray a) {  
    	arc_color = a.getColor(R.styleable.SField_soundfield_arc_color,Color.YELLOW);
    	arc_width = (int) a.getDimension(R.styleable.SField_soundfield_arc_line_width, 1);
     	arc_angle = a.getInt(R.styleable.SField_soundfield_arc_angle, 30);
     	arc_interval = (int) a.getDimension(R.styleable.SField_soundfield_arc_line_interval, 10);
         mThumbDrawable = a.getDrawable(R.styleable.SField_android_thumb);
        mThumbWidth = (int) (this.mThumbDrawable.getIntrinsicWidth()*0.5);
        mThumbHeight = (int) (this.mThumbDrawable.getIntrinsicHeight()*0.5);
        
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
                android.R.attr.state_selected, android.R.attr.state_checked};

    }  

    @SuppressLint("NewApi") @Override  
    protected void onDraw(Canvas canvas) {   
    	//Flash ARC
    	drawARCPoint1(canvas);
    	drawARCPoint2(canvas);
    	drawThumb(canvas);
    	
    	if(booSetDraw){
    		booSetDraw=false;
    	}else{
    		countDelayTime();
    	}
    }
    private void drawARCPoint1(Canvas canvas){
    	rd1 = (float) Math.sqrt(mThumbTX*mThumbTX+(MidWindHeight-mThumbTY)*(MidWindHeight-mThumbTY))-mThumbWidth/2;

    	int n = (int) (rd1/arc_interval);
    	int startAngle=(int) GetDegree(0,MidWindHeight,mThumbTX,mThumbTY)-arc_angle/2;
    	//System.out.println("ARC Point1 startAngle:"+startAngle);
    	for(int i=startarc;i <= n;i++){   
    		if(i>12){
    			break;
    		}
    		rd1 = arc_interval*i;		
    		RectF_Point1.set(0-rd1, MidWindHeight-rd1, 0+rd1, MidWindHeight+rd1);
    		canvas.drawArc(RectF_Point1, startAngle, arc_angle, false, Paint_Arc);    		
    	}
    	
		double y=(MidWindHeight-mThumbTY)/ytap;
    	rr[0] = mThumbTX/xtap;
		rr[2]=(double) y;
    }
    private void drawARCPoint2(Canvas canvas){
    	rd2 = (float) Math.sqrt((windWidth-mThumbTX)*(windWidth-mThumbTX)
    			+(MidWindHeight-mThumbTY)*(MidWindHeight-mThumbTY))-mThumbWidth/2;
    	int n = (int) (rd2/arc_interval);
    	int startAngle=(int) GetDegree(windWidth,MidWindHeight,mThumbTX,mThumbTY)-arc_angle/2;
    	//System.out.println("ARC Point2 startAngle:"+startAngle);
    	for(int i=startarc;i <= n;i++){  
    		if(i>12){
    			break;
    		}
    		rd2 = arc_interval*i; 		
    		RectF_Point2.set(windWidth-rd2, MidWindHeight-rd2, windWidth+rd2, MidWindHeight+rd2);
    		canvas.drawArc(RectF_Point2, startAngle, arc_angle, false, Paint_Arc);    		
    	}
    	
		double y=(MidWindHeight-mThumbTY)/ytap;
    	rr[1] = (windWidth-mThumbTX)/xtap;
		rr[3]=(double) y;
    }
    
    private void countDelayTime(){

		//int ytap=WIND_Height/5;//固定5M

		int hd=(int) ((rr[2]/0.34)*48);
		if(rr[0]>=rr[1]){
			progress[1]=hd;
			progress[0]=(int) (((rr[0]-rr[1])/0.34)*48)+hd;
		}else{
			progress[0]=hd;
			progress[1]=(int) (((rr[1]-rr[0])/0.34)*48)+hd;

		}
    	//printf
//    	System.out.println("BUG ##############################################################");
//    	String rrString="BUG___RR:";
//    	for(int i=0;i<2;i++){
//    		rrString+=(","+rr[i]);
//    	}
//    	System.out.println("BUG___rrString:"+rrString);
//    	System.out.println("BUG___min:"+min);
//    	String progressString="BUG___progress:";
//    	for(int i=0;i<2;i++){
//    		progressString+=(","+chs_progress[i]);
//    	}
//    	System.out.println("BUG___progressString:"+progressString);
    }
    private void drawThumb(Canvas canvas) {
     	this.mThumbDrawable.setBounds(
    			(int) mThumbDrawX, 
    			(int) mThumbDrawY,
    			(int) (mThumbDrawX + mThumbWidth), 
    			(int) (mThumbDrawY + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
    }
    //TODO
    public void SetDrawThumb(int delay1, int delay2) {

		int local=Math.abs(delay1-delay2);
		float lx=(float)(local*xMaxtap);
		booSetDraw=true;
//		System.out.println("BUG delay local:"+local);
//		System.out.println("BUG delay lx:"+lx);
//		System.out.println("BUG delay windWidth:"+windWidth);
//		System.out.println("BUG delay mThumbWidth:"+mThumbWidth);
//		if(lx > (windWidth-mThumbWidth)){
//			lx=(windWidth-mThumbWidth);
//		}
		lx=(float) ((float) (lx/48*0.34)*xtap)+mThumbWidth*3/2;
		if(lx > (windWidth-mThumbWidth)){
			lx=(windWidth-mThumbWidth);
		}
		
//		System.out.println("BUG delay mThumbDrawX:"+mThumbDrawX);
//		System.out.println("BUG delay MidWindHeight:"+MidWindHeight);
//		System.out.println("BUG delay mThumbHeight:"+mThumbHeight);
		
		mThumbDrawX=lx;
		mThumbPoint1X=lx;

		mThumbDrawable.setState(mThumbNormal);
		
		invalidate();  
	}
    //    private float[] calculatePointerPosition(float angle,float Radius ) {
//    	float x, y;
//    	x=(float) Math.sin(2*Math.PI / 360*angle)*Radius;
//    	y=(float) Math.cos(2*Math.PI / 360*angle)*Radius;
// 
//        //System.out.println("angle x:"+x+",y:"+y);
//        return new float[] { x, y };  
//    }  
    private float GetDegree(float centX,float centY,float x,float y){
    	float Degree=0;
    	double radian = Math.atan2(y - centY, x - centX);
        if (radian < 0){
             radian = radian + 2*Math.PI;
        }  
        Degree = Math.round(Math.toDegrees(radian));
        return Degree;
    }
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        //int min = Math.min(width, height);  
        //setMeasuredDimension(min, min);  
        windHeight = height;
    	windWidth = width;
    	MidWindWidth = windWidth/2; 
    	MidWindHeight = windHeight/2; 
    	
    	
    	xMaxtap=windWidth/DataStruct.CurMacMode.Delay.MAX;
    	yMaxtap=windHeight/ DataStruct.CurMacMode.Delay.MAX;
    	
    	xtap=windWidth/MacCfg.CCar[0];
    	ytap=windWidth/5;//固定
    }  
  

    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        float x = event.getX();  
        float y = event.getY();  
        if(x<0){
    		x=0;
    	}else if(x>windWidth){
    		x=windWidth;
    	}
    	if(y<0){
    		y=0;
    	}else if(y>MidWindHeight){
    		y=MidWindHeight;
    	}
        mThumbTX = x;
    	mThumbTY = y;
    	
    	mThumbPoint1X = mThumbTX-mThumbWidth/2;
    	mThumbPoint1Y = mThumbTY-mThumbHeight/2;
        //System.out.println("onTouchEvent x:"+x+",y:"+y);
        switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	        	if(((mThumbTX+mThumbWidth*2)>mThumbDrawX)&&((mThumbTX-mThumbWidth)<mThumbDrawX)){
                 	ThumbTouch=true;
                 	mThumbDrawable.setState(mThumbPressed);	 
                 	
                 	mThumbDownTX=mThumbTX;
                 	mThumbDownTY=mThumbTY;
                 	
                 	//System.out.println("BUG mThumbPressed:");
                 }else{
                 	ThumbTouch=false;
                 }
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	        	if(ThumbTouch){
	        		mThumbTX = x;
	            	mThumbTY = y;
	            	mThumbDrawX = mThumbTX-mThumbWidth/2;    	
	            	mThumbDrawY = mThumbTY-mThumbHeight/2;
	            	if(mThumbDrawX<0){
	            		mThumbDrawX=0;
	            	}else if(mThumbDrawX>(windWidth-mThumbWidth)){
	            		mThumbDrawX=(windWidth-mThumbWidth);
	            	}
	            	if(mThumbDrawY<0){
	            		mThumbDrawY=0;
	            	}else if(mThumbDrawY>(windHeight-mThumbHeight)){
	            		mThumbDrawY=(windHeight-mThumbHeight);
	            	}
	        		
	        		
	        		mThumbDrawable.setState(mThumbNormal);
	        		if (mOnDelaySettingTwoChangeListener != null){
	             	   mOnDelaySettingTwoChangeListener.onProgressChanged(this, progress, true);  
	              }
		            invalidate();  
	        	}

	            break;  
	        case MotionEvent.ACTION_UP:  
	        	if (mOnDelaySettingTwoChangeListener != null){
		         	   mOnDelaySettingTwoChangeListener.onProgressChanged(this, progress, false);  
		        	}

	        	mThumbDrawable.setState(mThumbNormal);
	            invalidate();  
	            break;  
	        default: 
	        	if (mOnDelaySettingTwoChangeListener != null){
	         	   mOnDelaySettingTwoChangeListener.onProgressChanged(this, progress, false);  
	        	}
	        	mThumbDrawable.setState(mThumbNormal);
	        	invalidate();  
	        	break;
        }  
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {  
            getParent().requestDisallowInterceptTouchEvent(true);  
        }  
        return ThumbTouch;  
    }  


  
    public void setDelaySettingTwoChangeListener(OnDelaySettingTwoChangeListener l) {  
        mOnDelaySettingTwoChangeListener = l;  
    }  
  
    public interface OnDelaySettingTwoChangeListener {  
  
        public abstract void onProgressChanged(DelaySettingTwo delaySettingTwo,  
                int[] progress, boolean fromUser);  
  
    }  
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
    

}  