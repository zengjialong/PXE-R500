package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;


import android.annotation.SuppressLint;
import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Canvas;  
import android.graphics.SweepGradient;  
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
  
@SuppressLint("ClickableViewAccessibility") public class MoveButton extends View {
	
	//private boolean DEBUG = true false ;
	private boolean DEBUG = false;
	private OnMoveButtonChangeListener mOnMoveButtonChangeListener;  
    //���ڴ�С
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
	public static final int EventKEY_Down=0;
	public static final int EventKEY_MOVE=1;
	public static final int EventKEY_UP=2;
	//���ڽ����
	private float mThumbDrawX = 0;
	private float mThumbDrawY = 0;
		
	private int progress = 0;
    private SweepGradient s;  
    
    
    private Context mContext = null;  
    public MoveButton(Context context) {      	
        super(context);  
        mContext = context;
        init(null, 0);  
    }  
  
    public MoveButton(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
        init(attrs, 0);  
    }  
  
    public MoveButton(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        mContext = context;
        init(attrs, defStyle);  
    }  
  
    private void init(AttributeSet attrs, int defStyle) {  
        final TypedArray a = getContext().obtainStyledAttributes(attrs,  
                R.styleable.MoveButton, defStyle, 0);  
  
        initAttributes(a);    
        
        invalidate();  
    }  
  
    private void initAttributes(TypedArray a) {  
     	
        /*thumb*/
        mThumbDrawable = a.getDrawable(R.styleable.MoveButton_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
                android.R.attr.state_selected, android.R.attr.state_checked};

    }  

    @SuppressLint("NewApi") @Override  
    protected void onDraw(Canvas canvas) {   
     	//Thumb
    	drawThumb(canvas);
     }
 
    private void drawThumb(Canvas canvas) {
    	this.mThumbDrawable.setBounds(
    			(int) mThumbDrawX, 
    			(int) mThumbDrawY,
    			(int) (mThumbDrawX + mThumbWidth), 
    			(int) (mThumbDrawY + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
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
  
   private void SeekTo(){
         invalidate(); 
         
         if (mOnMoveButtonChangeListener != null){
        	   mOnMoveButtonChangeListener.onProgressChanged(this, progress, true);  
         }
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
	        	if(((mThumbTX+mThumbWidth*2)>mThumbDrawX)&&((mThumbTX-mThumbWidth)<mThumbDrawX)&&
	        	   ((mThumbTY+mThumbHeight*2)>mThumbDrawY)&&((mThumbTY-mThumbHeight)<mThumbDrawY)){
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
	        		//System.out.println("BUG ACTION_MOVE:");
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
	            	if(((mThumbDownTX+mThumbWidth)>mThumbTX) && ((mThumbDownTX-mThumbWidth)<mThumbTX)&&
	            			((mThumbDownTY+mThumbHeight)>mThumbTY) && ((mThumbDownTY-mThumbHeight)<mThumbTY)){
	            		MoveEvent=EventKEY_Down;
	            	}else{
	            		MoveEvent=EventKEY_MOVE;
	            	}
	            	
	            	mThumbDrawable.setState(mThumbPressed);	 
	            	
	            	invalidate();  
	        	}
	        	
	        	
	            break;  
	        case MotionEvent.ACTION_UP:  
	        	//System.out.println("BUG ACTION_UP:");
	        	ThumbTouch=false;
	        	if(MoveEvent==EventKEY_Down){
	        		if (mOnMoveButtonChangeListener != null){
		         	   mOnMoveButtonChangeListener.onProgressChanged(this, EventKEY_UP, false);  
		        	}
	        	}
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
        return ThumbTouch;  
    }  


  
    public void setMoveButtonChangeListener(OnMoveButtonChangeListener l) {  
        mOnMoveButtonChangeListener = l;  
    }  
  
    public interface OnMoveButtonChangeListener {  
  
        public abstract void onProgressChanged(MoveButton moveButton,  
                int progress, boolean fromUser);  
  
    }  
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
    

}  