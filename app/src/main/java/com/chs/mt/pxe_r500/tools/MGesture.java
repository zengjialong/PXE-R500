package com.chs.mt.pxe_r500.tools;



import android.annotation.SuppressLint;
import android.content.Context;  
import android.graphics.Canvas;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
  
@SuppressLint("ClickableViewAccessibility") public class MGesture extends View {
	
	private OnMGestureChangeListener mOnMGestureChangeListener;
	private float windHeight = 0;
	private float windWidth = 0;

	private float mThumbTX = 0;
	private float mThumbTY = 0;
	private int MoveEvent=0;
	private float mThumbDownTX = 0;
	private float mThumbDownTY = 0;
	public static final int EventKEY_Down       =1;
	public static final int EventKEY_MOVE_LEFT  =2;
	public static final int EventKEY_MOVE_RIGHT =3;
	public static final int EventKEY_MOVE_UP    =4;
	public static final int EventKEY_MOVE_DOWN  =5;
	public static final int EventKEY_UP         =6;
	public static final int EventKEY_RELEASE    =7;
	private static final int Threshold    = 20;
	 
	public MGesture(Context context) {      	
        super(context);  
        init(null, 0);  
    }  
  
    public MGesture(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(attrs, 0);  
    }  
  
    public MGesture(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init(attrs, defStyle);  
    }  
  
    private void init(AttributeSet attrs, int defStyle) {  
       
        invalidate();  
    }  
  
   
    @SuppressLint("NewApi") @Override  
    protected void onDraw(Canvas canvas) {   

     }
 

    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        Math.min(width, height);  
        //setMeasuredDimension(min, min);  
        windHeight = height;
    	windWidth = width; 
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
    	}else if(y>windHeight){
    		y=windHeight;
    	}
        mThumbTX = x;
    	mThumbTY = y;
    	
        //System.out.println("onTouchEvent x:"+x+",y:"+y);
        switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
            	mThumbDownTX=mThumbTX;
            	mThumbDownTY=mThumbTY;
            	MoveEvent=EventKEY_Down;
 	            break;  
	        case MotionEvent.ACTION_MOVE:  
	        	if(((mThumbDownTX+Threshold)>mThumbTX) && ((mThumbDownTX-Threshold)<mThumbTX)&&
            			((mThumbDownTY+Threshold)>mThumbTY) && ((mThumbDownTY-Threshold)<mThumbTY)){
            		MoveEvent=EventKEY_Down;
            	}else if(Math.abs(mThumbTX-mThumbDownTX)>=Math.abs(mThumbTY-mThumbDownTY)){
            		if((mThumbDownTX+Threshold) <= mThumbTX){
                		MoveEvent=EventKEY_MOVE_RIGHT;
                	}else if((mThumbDownTX+Threshold) >= mThumbTX){
                		MoveEvent=EventKEY_MOVE_LEFT;
                	}
            	}else{
            		if((mThumbDownTY+Threshold) <= mThumbTY){
                		MoveEvent=EventKEY_MOVE_DOWN;
                	}else if((mThumbDownTY+Threshold) >= mThumbTY){
                		MoveEvent=EventKEY_MOVE_UP;
                	}
            	}
	        	if (mOnMGestureChangeListener != null){
	         	   mOnMGestureChangeListener.onMGestureChanged(this, MoveEvent, true);  
	        	}
	            break;  
	        case MotionEvent.ACTION_UP:  
			if(MoveEvent==EventKEY_Down){
	        		MoveEvent=EventKEY_UP;
	        	}else{
	        		MoveEvent=EventKEY_RELEASE;
	        	}
	        		
        		if (mOnMGestureChangeListener != null){
	         	   mOnMGestureChangeListener.onMGestureChanged(this, MoveEvent, false);  
	        	}
	            break;  
	        default: 
	        	break;
        }  
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {  
            getParent().requestDisallowInterceptTouchEvent(true);  
        }  
        return false;  
    }  


  
    public void setMGestureChangeListener(OnMGestureChangeListener l) {  
        mOnMGestureChangeListener = l;  
    }  
  
    public interface OnMGestureChangeListener {  
  
        public abstract void onMGestureChanged(MGesture mGesture,  
                int MoveEvent, boolean fromUser);  
  
    }  
}  