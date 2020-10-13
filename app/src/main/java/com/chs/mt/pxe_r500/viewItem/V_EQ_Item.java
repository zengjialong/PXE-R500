package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.tools.EQ_SeekBar;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_EQ_Item extends RelativeLayout {

    public Button B_Gain;
    public Button B_Freq;
    public Button B_BW;
    public Button B_ID;
    public Button B_ResetEQ;
    public LinearLayout LY_ResetEQ;
    public LinearLayout Lyout_EQItem;
    public EQ_SeekBar MVS_SeekBar;

    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_EQ_Item(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_EQ_Item(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_EQ_Item(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_eqitem, this);

        Lyout_EQItem = (LinearLayout) findViewById(R.id.id_llyout_equalizer_one_1);
        B_ID = (Button) findViewById(R.id.id_tv_id_equalizer_one_1);
        MVS_SeekBar = (EQ_SeekBar) findViewById(R.id.id_mvs_equalizer_one_1);
        B_Gain = (Button) findViewById(R.id.id_tv_gain_equalizer_one_1);
        B_BW = (Button) findViewById(R.id.id_tv_q_equalizer_one_1);
        B_Freq = (Button) findViewById(R.id.id_tv_freq_equalizer_one_1);
        B_ResetEQ = (Button) findViewById(R.id.id_tv_reset_eqg_one_1);
        LY_ResetEQ = (LinearLayout) findViewById(R.id.id_ly_reset_eqg_one_1);

        clickEvent();

    }

    /*
    响应事件
     */
    private void clickEvent(){

    }

    public void setCh(int ch){

    }

}
