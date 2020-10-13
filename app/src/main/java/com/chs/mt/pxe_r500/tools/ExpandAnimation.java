package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;


public class ExpandAnimation extends Animation {
    private View mTargetView;
    private boolean isExpandDown;
    public ExpandAnimation(View mTargetView, boolean isExpandDown,int defaultHeight) {
        super();
        this.mTargetView = mTargetView;
        this.isExpandDown = isExpandDown;
        setDuration(100L);
        setInterpolator(new AccelerateDecelerateInterpolator());
        setFillAfter(true);
        resetViewHeight(mTargetView, defaultHeight);
    }
    /**
     * 动画开始前，务必保证targetView的高度是存在的
     * @param v
     * @param defaultHeight
     */
    private void resetViewHeight(View v,int defaultHeight)
    {
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        lp.height = v.getContext().getResources().getDimensionPixelSize(R.dimen.seff_item_height);
        v.setLayoutParams(lp);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        int height = mTargetView.getHeight();
         /**
          * 注意 这里的 LayoutParams 类型应该和mTargetView的parentView相关，
          * 比如LinearLayout,这里的类型应该是android.widget.LinearLayout.LayoutParams
          * 不推荐使用 ViewGroup.LayoutParams,因为不能改变外边距
          */
        LayoutParams layoutParams = (LayoutParams) mTargetView.getLayoutParams();
        if(isExpandDown)
        {
            layoutParams.bottomMargin = -height  + (int) (height*interpolatedTime);
        }else{

            layoutParams.bottomMargin = -(int) (height*interpolatedTime);
        }
        mTargetView.setLayoutParams(layoutParams);
        mTargetView.getParent().requestLayout();
    }
}