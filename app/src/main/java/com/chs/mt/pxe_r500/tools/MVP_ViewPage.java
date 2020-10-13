package com.chs.mt.pxe_r500.tools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MVP_ViewPage extends ViewPager {
    private boolean noScroll = false;
    private boolean noScrollonIntercept = false;
    public MVP_ViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Auto-generated constructor stub
    }

    public MVP_ViewPage(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }
    
    public void setNoScrollOnIntercept(boolean noScroll) {
        this.noScrollonIntercept = noScroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if ((noScroll)||(noScrollonIntercept))
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
    	//smoothScroll==false, ������Ӧ��ҳ
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}