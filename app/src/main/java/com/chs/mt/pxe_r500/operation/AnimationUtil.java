package com.chs.mt.pxe_r500.operation;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class AnimationUtil {

	public static void AnimScale(Context mContext, View view){
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(500);
		view.startAnimation(sa);
	}

    public static void AnimScaleI(Context mContext, View view){
        ScaleAnimation sa = new ScaleAnimation(0.9f, 1f, 0.9f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(500);
        view.startAnimation(sa);
    }

    public static void AnimScaleBig(Context mContext, View view){
        ScaleAnimation sa = new ScaleAnimation(1f, 1.3f, 1f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(500);
        view.startAnimation(sa);
    }

    //控件由完全透明到完全不透明变化，持续时间为0.2s；
    public static void toVisibleAnim(Context mContext, View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        view.startAnimation(alphaAnimation);
    }
    //
    public static void toHideAnim(Context mContext, View view){
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        view.startAnimation(scaleAnimation);
    }
    //控件以自身中心为圆心旋转90度，持续时间为0.2s；
    public static void rotateAnim(Context mContext, View view) {
        view.setVisibility(View.VISIBLE);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);
        view.startAnimation(rotateAnimation);
    }
    //控件从自身位置的最右端开始向左水平滑动了自身的宽度，持续时间为0.2s；
    public static void showScrollAnim(Context mContext, View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(200);
        view.startAnimation(mShowAction);
    }
    //image控件从自身位置的最左端开始水平向右滑动隐藏动画，持续时间0.2s
    public static void hiddenScrollAnim(Context mContext, LinearLayout view) {
        view.setVisibility(View.GONE);
        TranslateAnimation mHiddenAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(200);
        view.startAnimation(mHiddenAction);
    }
}
