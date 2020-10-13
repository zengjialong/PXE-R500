package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_OutputFrs extends RelativeLayout {
    public RelativeLayout LY;
    public LinearLayout LY_Mute;
    public Button CH,Val;
    public Button Mute,Polar;
    public MHS_SeekBar SB;

    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_OutputFrs(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_OutputFrs(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_OutputFrs(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_outputfrsitem, this);

        CH = (Button)findViewById(R.id.id_b_ch);
        Val = (Button)findViewById(R.id.id_b_val);
        Mute = (Button)findViewById(R.id.id_b_mute);
        Polar = (Button)findViewById(R.id.id_b_polar);
        LY = (RelativeLayout) findViewById(R.id.id_ly);
        LY_Mute = (LinearLayout)findViewById(R.id.id_ly_mute);
        SB = (MHS_SeekBar) findViewById(R.id.id_mcl_seekbar_valume);

        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){

    }
}
