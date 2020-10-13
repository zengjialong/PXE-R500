package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.tools.KnobView_PicTwo;

import java.text.DecimalFormat;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_SeekBarIndexGainValItem extends RelativeLayout {
    private KnobView_PicTwo Sb;
    private TextView Index;
    private TextView Val;


    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_SeekBarIndexGainValItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_SeekBarIndexGainValItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_SeekBarIndexGainValItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_seekbarindexgainvalitem, this);

        Sb = (KnobView_PicTwo)findViewById(R.id.id_kv_sb);
        Val = (TextView)findViewById(R.id.id_tv_val);
        Index = (TextView)findViewById(R.id.id_tv_index);
        clickEvent();

    }

    public void setMax(int max){
        Sb.setMax(max);
    }

    public void setProgress(int progress){
        Sb.setProgress(progress);
        Val.setText(ChangeGainValume(progress));
    }

    public void setIndexText(String st){
        Index.setText(st);
    }

    private String ChangeGainValume(int num){
        //System.out.println("ChangeValume:"+num);
        String show = null;
        DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show=decimalFormat.format(0.0-(DataStruct.CurMacMode.EQ.EQ_Gain_MAX/2-num)/10.0);//format 返回的是字符串
        show=show+"dB";
        return show;
    }

    /*
    响应事件
     */

    private void clickEvent(){
        Sb.setProgressChangeListener(new KnobView_PicTwo.OnKnobView_PicTwo_ProgressChangeListener() {
            @Override
            public void onProgressChanged(KnobView_PicTwo view, boolean fromUser, int progress) {
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(progress,progress,true);
                    Val.setText(ChangeGainValume(progress));
                }

            }

            @Override
            public void onStartTrackingTouch(KnobView_PicTwo view, int progress) {

            }

            @Override
            public void onStopTrackingTouch(KnobView_PicTwo view, int progress) {

            }
        });
    }
}
