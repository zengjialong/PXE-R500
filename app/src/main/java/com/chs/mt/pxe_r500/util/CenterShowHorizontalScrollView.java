package com.chs.mt.pxe_r500.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.chs.mt.pxe_r500.R;

/**
 * Created by chenglin on 2017-6-23.
 */

public class CenterShowHorizontalScrollView extends HorizontalScrollView {
    private LinearLayout mShowLinear;

    public CenterShowHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShowLinear = new LinearLayout(context);
        mShowLinear.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mShowLinear.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(mShowLinear, params);
    }

    public void onClicked(int index) {

        System.out.println("BUG 进来的值为");
       // if (v.getTag(R.id.item_position) != null) {
            //int position = (Integer) v.getTag(R.id.item_position);
            int position=index;
            View itemView = mShowLinear.getChildAt(position);
            int itemWidth = itemView.getWidth();
            int scrollViewWidth = this.getWidth();
            for (int i = 0; i < mShowLinear.getChildCount(); i++) {
                mShowLinear.getChildAt(i).findViewById(R.id.text).setBackgroundResource(R.color.nullc);
            }
            //System.out.println("BUG 获取到的值为"+mShowLinear.getChildAt(position).getId());
            mShowLinear.getChildAt(position).findViewById(R.id.text).setBackgroundResource(R.drawable.btn_output_press);
            //先说x,和y，这两个传入的参数就是你想要移动到的位置
            smoothScrollTo(itemView.getLeft() - (scrollViewWidth / 2 - itemWidth / 2), 0);
      //  }
    }
    public void setSelect(int index){

    }

    public LinearLayout getLinear() {
        return mShowLinear;
    }

    public void addItemView(View itemView, int position) {
        itemView.setTag(R.id.item_position, position);
        mShowLinear.addView(itemView);
    }


}