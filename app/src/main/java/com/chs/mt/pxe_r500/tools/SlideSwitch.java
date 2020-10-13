package com.chs.mt.pxe_r500.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.chs.mt.pxe_r500.R;

public class SlideSwitch extends View{
    
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //�����
    
    boolean isOn = false;
    float curX = 0;
    float centerY; 
    float viewWidth;
    float radius;
    float lineStart; 
    float lineEnd; 
    float lineWidth;
    final int SCALE = 8; 
    OnStateChangedListener onStateChangedListener = null;

    public SlideSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public SlideSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SlideSwitch(Context context) {
        super(context);
    }
    
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        curX = event.getX();
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(curX > viewWidth / 2) {
                curX = lineEnd;
                if(false == isOn) {
                    if(onStateChangedListener != null){
                        onStateChangedListener.onStateChanged(true);
                    }
                    isOn = true;
                }
            } else {
                curX = lineStart;
                if(true == isOn) {
                    if(onStateChangedListener != null){
                        onStateChangedListener.onStateChanged(false);
                    }
                    isOn = false;
                }
            }
        }
        this.postInvalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredWidth() * 2 / SCALE);
        viewWidth = this.getMeasuredWidth();
        radius = viewWidth / SCALE;
        lineWidth = radius * 2f;
        curX = radius;
        centerY = this.getMeasuredWidth() / SCALE;
        lineStart = radius;
        lineEnd = (SCALE - 1) * radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        
        curX = curX > lineEnd?lineEnd:curX;
        curX = curX < lineStart?lineStart:curX;
        
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(getResources().getColor(R.color.sliderswitch_normal_color));
        canvas.drawLine(lineStart, centerY, curX, centerY, mPaint);
        mPaint.setColor(getResources().getColor(R.color.sliderswitch_normal_color));
        canvas.drawLine(curX, centerY, lineEnd, centerY, mPaint);
        
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.sliderswitch_normal_color));
        canvas.drawCircle(lineEnd, centerY, lineWidth / 2, mPaint);  
        mPaint.setColor(getResources().getColor(R.color.sliderswitch_normal_color));
        canvas.drawCircle(lineStart, centerY, lineWidth / 2, mPaint);

        mPaint.setColor(getResources().getColor(R.color.sliderswitch_press_color));
        canvas.drawCircle(curX, centerY, radius , mPaint);
        
        
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(lineWidth);
//        mPaint.setColor(Color.LTGRAY);
//        canvas.drawLine(lineStart, centerY, curX, centerY, mPaint);
        
        
        
        
        
        
        //System.out.println("SlideSwitch.onDraw() curX="+curX);
        //canvas.drawLine(curX, centerY, curX+viewWidth/2, centerY, mPaint);
    }

    public void setOnStateChangedListener(OnStateChangedListener o) {
        this.onStateChangedListener = o;
    }
    
    public interface OnStateChangedListener {
        public void onStateChanged(boolean state);
    }

    public void setChecked(boolean state){
        isOn = state;
        if(isOn){
            curX = getMeasuredWidth();
        }else{
            curX = 0;
        }

        invalidate();
    }

}
