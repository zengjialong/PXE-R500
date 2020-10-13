package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.tools.LongCickButton;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_DelayItem_L extends RelativeLayout {

    private LongCickButton Btn_Add,Btn_Sub;
    private Button Btn_Val,Btn_Spk;

    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_DelayItem_L(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_DelayItem_L(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_DelayItem_L(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_view_delay_l, this);

        Btn_Add = (LongCickButton) findViewById(R.id.id_inc);
        Btn_Sub = (LongCickButton)findViewById(R.id.id_sub);
        Btn_Val = (Button)findViewById(R.id.id_b_val);
        Btn_Spk = (Button)findViewById(R.id.id_b_spk);

        clickEvent();

        setCh(0);
    }

    /*
    响应事件
     */
    private void clickEvent(){
//
//        for(int i=0;i<maxCH;i++){
//            ButtomItem[i].setTag(i);
//            ButtomItem[i].setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int val = (int) v.getTag();
//                    setCh(val);
//
//                    if (mSetOnClickListener != null) {
//                        mSetOnClickListener.onClickListener(val, 0, true);
//                    }
//                }
//            });
//        }
    }

    public void setCh(int ch){

    }

}
