package com.chs.mt.pxe_r500.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.dialogFragment.DelayUnitDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetDelayDialogFragment;
import com.chs.mt.pxe_r500.viewItem.V_DelayItem;
//import com.chs.mt.hh_dbs460_carplay.fragment.dialogFragment.SetDelayMVNSBDialogFragment;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DelayFRS_Fragment extends Fragment {
    private Button Btn_Unit;
    private static final int OUT_CH_MAX_CFG = 10;
    private int DelayUnit = 2;

//    private Button[] BtnVal = new Button[OUT_CH_MAX_CFG];
//    private Button[] BtnIndex = new Button[OUT_CH_MAX_CFG];
    private V_DelayItem[] v_delayItem = new V_DelayItem[OUT_CH_MAX_CFG];
    //对话框
    private SetDelayDialogFragment setDelayDialogFragment=null;
    private DelayUnitDialogFragment mDelayUnitDialog=null;
//    private SetDelayMVNSBDialogFragment setDelayMVNSBDialogFragment=null;

    private Context mContext;

    public DelayFRS_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_delay_frs, container, false);
        initData();
        initView(view);
        initClick();
        FlashPageUI();
        return view;
    }

    private void initData(){

    }
    private void initView(View view){
        Btn_Unit = (Button) view.findViewById(R.id.id_delayunit);

//        BtnIndex[0] = (Button) view.findViewById(R.id.id_delay_0);
//        BtnIndex[1] = (Button) view.findViewById(R.id.id_delay_1);
//        BtnIndex[2] = (Button) view.findViewById(R.id.id_delay_2);
//        BtnIndex[3] = (Button) view.findViewById(R.id.id_delay_3);

        v_delayItem[0] = (V_DelayItem) view.findViewById(R.id.id_ly_0);
        v_delayItem[1] = (V_DelayItem) view.findViewById(R.id.id_ly_1);
        v_delayItem[2] = (V_DelayItem) view.findViewById(R.id.id_ly_2);
        v_delayItem[3] = (V_DelayItem) view.findViewById(R.id.id_ly_3);
        v_delayItem[4] = (V_DelayItem) view.findViewById(R.id.id_ly_4);
        v_delayItem[5] = (V_DelayItem) view.findViewById(R.id.id_ly_5);
        v_delayItem[6] = (V_DelayItem) view.findViewById(R.id.id_ly_6);
        v_delayItem[7] = (V_DelayItem) view.findViewById(R.id.id_ly_7);
        v_delayItem[8] = (V_DelayItem) view.findViewById(R.id.id_ly_8);
        v_delayItem[9] = (V_DelayItem) view.findViewById(R.id.id_ly_9);

    }
    private void initClick(){
        Btn_Unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(DelayUnitDialogFragment.ST_DataOPT, DelayUnit-1);
                bundle.putInt(DelayUnitDialogFragment.ST_Data, DelayUnit-1);

                if(mDelayUnitDialog == null){
                    mDelayUnitDialog= new DelayUnitDialogFragment();
                }
                if (!mDelayUnitDialog.isAdded()) {
                    mDelayUnitDialog.setArguments(bundle);
                    mDelayUnitDialog.show(getActivity().getFragmentManager(), "mDelayUnitDialog");
                }

                mDelayUnitDialog.OnSetOnClickDialogListener(new DelayUnitDialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        DelayUnit = type;
                        for(int i=0;i<OUT_CH_MAX_CFG;i++){
                            v_delayItem[i].setDelayUnit(DelayUnit);
                        }
                        FlashPageUI();




                    }
                });
            }
        });

        for(int i=0;i<OUT_CH_MAX_CFG;i++){
//            BtnIndex[i].setTag(i);
//            BtnIndex[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    MacCfg.OutputChannelSel = (int)view.getTag();
//                    setIndex();
//                }
//            });

            v_delayItem[i].set_Tag(i);
            v_delayItem[i].OnSetOnClickListener(new V_DelayItem.SetOnClickListener() {
                @Override
                public void onClickListener(int tag, int val, int type, boolean boolClick) {

                    MacCfg.OutputChannelSel = tag;
                    setIndex();
                    if(boolClick){
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay =
                                val;
                        int valDll = val - MacCfg.DelayVal[MacCfg.OutputChannelSel];
                        if(MacCfg.bool_OutChLink){
                            int to=0;
                            if((MacCfg.OutputChannelSel%2)==0){
                                to = MacCfg.OutputChannelSel +1;
                            }else {
                                to = MacCfg.OutputChannelSel -1;
                            }

                            DataStruct.RcvDeviceData.OUT_CH[to].delay = MacCfg.DelayVal[to]+valDll;

                            if(DataStruct.RcvDeviceData.OUT_CH[to].delay >
                                    DataStruct.CurMacMode.Delay.MAX){
                                DataStruct.RcvDeviceData.OUT_CH[to].delay =
                                        DataStruct.CurMacMode.Delay.MAX;
                            }

                            if(DataStruct.RcvDeviceData.OUT_CH[to].delay < 0){
                                DataStruct.RcvDeviceData.OUT_CH[to].delay = 0;
                            }
                            v_delayItem[to].setDelayVal(DataStruct.RcvDeviceData.OUT_CH[to].delay);
                        }

                    }
                }
            });
        }


    }

    //刷新页面UI
    public void FlashPageUI(){

//        for(int i=0;i<DataStruct.CurMacMode.Out.OUT_CH_MAX_USE;i++){
//            MacCfg.DelayVal[i] = DataStruct.RcvDeviceData.OUT_CH[i].delay;
//        }
        FlashChannelUI();
    }
    //刷新通道UI
    public void FlashChannelUI(){
        switch (DelayUnit){
            case 1:
                Btn_Unit.setText(R.string.CM);
                break;
            case 2:
                Btn_Unit.setText(R.string.MS);
                break;
            case 3:
                Btn_Unit.setText(R.string.Inch);
                break;
        }
        setIndex();
        for(int i=0;i<OUT_CH_MAX_CFG;i++){
            v_delayItem[i].setDelayVal(DataStruct.RcvDeviceData.OUT_CH[i].delay);
        }

    }

    private void showDialog(){
        setIndex();



        /**/
        Bundle Freq = new Bundle();
        Freq.putInt(SetDelayDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay);
        Freq.putInt(SetDelayDialogFragment.ST_DelayUnit, DelayUnit);

        if(setDelayDialogFragment == null){
            setDelayDialogFragment = new SetDelayDialogFragment();
        }
        if (!setDelayDialogFragment.isAdded()) {
            setDelayDialogFragment.setArguments(Freq);
            setDelayDialogFragment.show(getActivity().getFragmentManager(), "SetDelayDialogFragment");
        }

//        setDelayDialogFragment.OnSetDelayDialogFragmentChangeListener(new SetDelayDialogFragment.OnDelayDialogFragmentChangeListener() {
//
//            @Override
//            public void onDelayVolChangeListener(int delay, int type, boolean flag) {
//                // TODO Auto-generated method stub
//                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay = delay;
//                //BtnVal[MacCfg.OutputChannelSel].setText(String.valueOf(ChannelShowDelay(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay)));
//                //B_SetDelay_Show[GetDelayId(MacCfg.ChannelNumBuf[MacCfg.OutputChannelSel])].setText(String.valueOf(ChannelShowDelay(delay)));
//            }
//        });

    }

    private void setIndex(){
//        BtnIndex[0].setBackground(mContext.getResources().getDrawable(R.drawable.tjjspeakl_l_normal));
//        BtnIndex[1].setBackground(mContext.getResources().getDrawable(R.drawable.tjjspeakl_r_normal));
//        BtnIndex[2].setBackground(mContext.getResources().getDrawable(R.drawable.tjjspeakl_l_normal));
//        BtnIndex[3].setBackground(mContext.getResources().getDrawable(R.drawable.tjjspeakl_r_normal));

        for(int i=0;i<OUT_CH_MAX_CFG;i++){
            v_delayItem[i].setPress(false);
        }

        v_delayItem[MacCfg.OutputChannelSel].setPress(true);

//        if((MacCfg.OutputChannelSel%2)==0){
//            BtnIndex[MacCfg.OutputChannelSel].setBackground(mContext.
//                    getResources().getDrawable(R.drawable.tjjspeakl_l_press));
//        }else {
//            BtnIndex[MacCfg.OutputChannelSel].setBackground(mContext.
//                    getResources().getDrawable(R.drawable.tjjspeakl_r_press));
//        }

    }


    /******* 延时时间转换  *******/
    private String ChannelShowDelay(int timedelay){
        String delaytimes=new String();
        switch(DelayUnit){
            case 1:delaytimes=CountDelayCM(timedelay);
                break;
            case 2:delaytimes=CountDelayMs(timedelay);
                break;
            case 3:delaytimes=CountDelayInch(timedelay);
                break;
            default: break;
        }
        return delaytimes;
    }

    private String  CountDelayCM(int num){
        DecimalFormat decimalFormat=new DecimalFormat("0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p="";
        int m_nTemp=75;
        float Time = (float) (num/48.0); //当Delay〈476时STEP是0.021MS；
        float LMT = (float) (((m_nTemp-50)*0.6+331.0)/1000.0*Time);
        LMT = LMT*100;
        //float LFT = (float) (LMT*3.2808*12.0);

        int fr=(int) (LMT*10);
        int ir = fr%10;
        int ri = 0;
        if(ir>=5){
            ri=fr/10+1;
        }else{
            ri=fr/10;
        }
        p=decimalFormat.format(ri);
        return p;
    }

    private String  CountDelayMs(int num){
        DecimalFormat decimalFormat=new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p="";
        int fr = num*10000/48;
        int ir = fr%10;
        int ri = 0;
        if(ir>=5){
            ri=fr/10+1;
        }else{
            ri=fr/10;
        }
        p=decimalFormat.format(ri/1000.0);
        return p;
    }

    private String  CountDelayInch(int num){
        DecimalFormat decimalFormat=new DecimalFormat("0");
        String p="";
        float base=(float) 331.0;
        if(num==DataStruct.CurMacMode.Delay.MAX){
            base=(float) 331.4;
        }
        int m_nTemp=75;
        float Time = (float) (num/48.0); //当Delay〈476时STEP是0.021MS；
        float LMT = (float) (((m_nTemp-50)*0.6+base)/1000.0*Time);

        float LFT = (float) (LMT*3.2808*12.0);

        int fr=(int) (LFT*10);
        int ir = fr%10;
        int ri = 0;
        if(ir>=5){
            ri=fr/10+1;
        }else{
            ri=fr/10;
        }
        p=decimalFormat.format(ri);
        return p;
    }


}
