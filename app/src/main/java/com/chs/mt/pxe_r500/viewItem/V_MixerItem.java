package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_MixerItem extends RelativeLayout {

    public TextView TV_MixerChn;/*输入通道名字*/
    public LinearLayout LLY_Mixer;/*输入通道*/
    public RelativeLayout LRY_Mixer;/*输入通道*/
    public Button B_MixerPolar;/*输入极性*/
    public LongCickButton B_MixerValInc;/*输入音量按键+*/
    public LongCickButton B_MixerValSub;/*输入音量按键-*/
    public Button B_MixerVal;/*输出音量显示*/
    public MHS_SeekBar SB_Mixer_SeekBar;
    public Button B_MixerHide;/*禁止点击层*/
    public Button B_MixerResetVal;/*数值复位*/

    public LinearLayout LLY_Reset_Polar;/*复位开关和相位*/
    public LinearLayout LLY_Reset;/*复位开关*/
    public LinearLayout LLY_INS;/*按键+*/
    public LinearLayout LLY_SUB;/*按键-*/
    public ImageView Img_inputsource;



    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_MixerItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_MixerItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_MixerItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_mixeritem, this);

        TV_MixerChn = (TextView) findViewById(R.id.id_tv_in_ch);/*输入通道名字*/
        LLY_Mixer = (LinearLayout)findViewById(R.id.id_llyout_ch);/*输入通道*/
        LRY_Mixer = (RelativeLayout) findViewById(R.id.id_llyout_nott_ch);/*输入通道*/
        B_MixerPolar = (Button)findViewById(R.id.id_b_weight_polar);/*输入极性*/
        B_MixerValInc = (LongCickButton) findViewById(R.id.id_button_weight_inc);/*输入音量按键+*/
        B_MixerValSub = (LongCickButton)findViewById(R.id.id_button_weight_sub);/*输入音量按键-*/
        B_MixerVal = (Button)findViewById(R.id.id_b_weight_show_ch);/*输出音量显示*/
        SB_Mixer_SeekBar = (MHS_SeekBar) findViewById(R.id.id_sb_weight);
        B_MixerHide = (Button)findViewById(R.id.id_button_donott);/*禁止点击层*/
        B_MixerResetVal = (Button)findViewById(R.id.id_b_weight_switch_ch);/*数值复位*/

        LLY_Reset_Polar = (LinearLayout)findViewById(R.id.id_ly_weight_switch_polar);/*复位开关和相位*/
        LLY_Reset = (LinearLayout)findViewById(R.id.id_ly_weight_switch_ch);/*复位开关*/
        LLY_INS = (LinearLayout)findViewById(R.id.id_ly_weight_inc);/*按键+*/
        LLY_SUB = (LinearLayout)findViewById(R.id.id_ly_weight_sub);/*按键-*/
        Img_inputsource=findViewById(R.id.id_mixer_img);
        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){

    }
}
