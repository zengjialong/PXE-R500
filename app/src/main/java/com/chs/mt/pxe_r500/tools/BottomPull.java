package com.chs.mt.pxe_r500.tools;
  
import android.annotation.SuppressLint;
import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Canvas;  
import android.graphics.SweepGradient;  
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
  
@SuppressLint("ClickableViewAccessibility") public class BottomPull extends View {
	
	//private boolean DEBUG = true false ;
	private boolean DEBUG = false;
	private OnPButtonChangeListener mOnPButtonChangeListener;
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
	private boolean ThumbTouch=false;
	private int MoveEvent=0;
	private float mThumbDownTX = 0;
	private float mThumbDownTY = 0;
	public static final int EventKEY_Down=1;
	public static final int EventKEY_MOVE=2;
	public static final int EventKEY_UP=3;
	private float mThumbDrawX = 0;
	private float mThumbDrawY = 0;
		
	private int progress = 0;
    private SweepGradient s;  
    
    
    private Context mContext = null;  
    public BottomPull(Context context) {      	
        super(context);  
        mContext = context;
        init(null, 0);  
    }  
  
    public BottomPull(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
        init(attrs, 0);  
    }  
  
    public BottomPull(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        mContext = context;
        init(attrs, defStyle);  
    }  
  
    private void init(AttributeSet attrs, int defStyle) {  
  
        
        invalidate();  
    }  
  
    private void initAttributes(TypedArray a) {  
     	
//        /*thumb*/
//        mThumbDrawable = a.getDrawable(R.styleable.PButton_android_thumb);
//        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
//        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
//        
//        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
//                -android.R.attr.state_selected, -android.R.attr.state_checked};
//        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
//                android.R.attr.state_selected, android.R.attr.state_checked};

    }  

    @SuppressLint("NewApi") @Override  
    protected void onDraw(Canvas canvas) {   
    
     }
 
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        int min = Math.min(width, height);  
        //setMeasuredDimension(min, min);  
        windHeight = height;
    	windWidth = width;
    	MidWindWidth = windWidth/2; 
    	MidWindHeight = windHeight/2; 
      }  
  

    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        float x = event.getX();  
        float y = event.getY();  
//        if(x<0){
//    		x=0;
//    	}else if(x>windWidth){
//    		x=windWidth;
//    	}
//    	if(y<0){
//    		y=0;
//    	}else if(y>windHeight){
//    		y=windHeight;
//    	}
        mThumbTX = x;
    	mThumbTY = y;
    	
        //System.out.println("onTouchEvent x:"+x+",y:"+y);
        switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	        	if (mOnPButtonChangeListener != null){
	        		mOnPButtonChangeListener.onProgressChanged(this,(int)mThumbTY, EventKEY_Down, true);  
		        }
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	        	if (mOnPButtonChangeListener != null){
	        		mOnPButtonChangeListener.onProgressChanged(this,(int)mThumbTY, EventKEY_MOVE, true);  
		        }
	            break;  
	        case MotionEvent.ACTION_UP:  
	        	if (mOnPButtonChangeListener != null){
	        		mOnPButtonChangeListener.onProgressChanged(this,(int)mThumbTY, EventKEY_UP, false);  
		        }
	            break;  
	        default: 
	        	if (mOnPButtonChangeListener != null){
	        		mOnPButtonChangeListener.onProgressChanged(this,0, EventKEY_UP, false);  
		        }
	        	break;
        }  
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {  
            getParent().requestDisallowInterceptTouchEvent(true);  
        }  
        return true;  
    }  


  
    public void setPButtonChangeListener(OnPButtonChangeListener l) {  
        mOnPButtonChangeListener = l;  
    }  
  
    public interface OnPButtonChangeListener {  
  
        public abstract void onProgressChanged(BottomPull PButton,int y,  
                int KeyEvent, boolean fromUser);  
  
    }  
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
    

}  