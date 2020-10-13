package com.chs.mt.pxe_r500.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionDialogFragment;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;
import com.chs.mt.pxe_r500.tools.wheel.WheelView;
import com.chs.mt.pxe_r500.viewItem.V_MixerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MixerFragment extends Fragment {
    private Toast mToast;
    private Context mContext;
    private Button encryption;


    private int MaxINPUT = 16;

    private LinearLayout ly_input;
    private V_MixerItem[] mMixerItem  = new V_MixerItem[MaxINPUT];


    private Button btn_passive,btn_active;
    //通道选择
    private WheelView WV_OutVa;
    private List<String> Channellists;

    public MixerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chs_fragment_mixer, container, false);
        mContext = getActivity().getApplicationContext();
        initChannelSel(view);
        initView(view);
        initClick();

        initConfigMac();
        AddMixerPageListener();
        initViewContext();
        FlashPageUI();


        return view;
    }

    //刷新页面UI
    public void FlashPageUI(){
                encryption.setVisibility(View.GONE);
                FlashChannelUI();

    }
    //刷新通道UI
    public void FlashChannelUI(){
        WV_OutVa.setIndex(MacCfg.OutputChannelSel);
        flashInputAction(DataStruct.RcvDeviceData.SYS.none4);
        FlashMixerInputChannelSel();
        FlashMixerChannelSel();
        FlashInputVal();
        FlashInputPolar();
        FlashMixerSeekbar();
        FlashMixerResetButtomState();


    }

    /**
     * 消息提示
     */
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    //加密
    private void initClick(){
        encryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEncryptionDialog();
            }
        });

        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataStruct.RcvDeviceData.SYS.none4==0){
                   // DataOptUtil.SetInputSourceMixerVol(MacCfg.OutputChannelSel);
                    DataStruct.RcvDeviceData.SYS.none4=1;
                    for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                        DataOptUtil.SetInputSourceMixerVol(i);
                    }

                }


                flashInputAction(DataStruct.RcvDeviceData.SYS.none4);
                FlashChannelUI();
            }
        });

        btn_passive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataStruct.RcvDeviceData.SYS.none4==1){

                    DataStruct.RcvDeviceData.SYS.none4=0;
                    for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                        DataOptUtil.SetInputSourceMixerVol(i);
                    }
                }


                flashInputAction(DataStruct.RcvDeviceData.SYS.none4);
                FlashChannelUI();
            }
        });

    }
    private void showEncryptionDialog(){
        if (DataStruct.isConnecting) {
            SetEncryptionDialogFragment setEncryptionDialogFragment = new SetEncryptionDialogFragment();
            setEncryptionDialogFragment.show(getActivity().getFragmentManager(), "setEncryptionDialogFragment");
            setEncryptionDialogFragment.OnSetEncryptionDialogFragmentChangeListener(new SetEncryptionDialogFragment.OnEncryptionDialogFragmentClickListener() {
                @Override
                public void onEncryptionClickListener(
                        boolean Encryptionflag, boolean recallFlag) {
                    if(!recallFlag){
                        if(Encryptionflag){//加密处理
                           // DataOptUtil.SaveGroupData(0);
                        }else{//解密处理
                          //  DataOptUtil.SaveGroupData(0);
                        }
//                        LoadingDialogFragment mvLoadingDialog = new LoadingDialogFragment();
//                        mvLoadingDialog.show(getActivity().getFragmentManager(), "mvLoadingDialog");
                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
                        intentw.putExtra("state", true);
                        getActivity().sendBroadcast(intentw);
                    }else{//清除数据
//                        SetEncryptionCleanDialogFragment setEncryptionCleanDialogFragment = new SetEncryptionCleanDialogFragment();
//                        setEncryptionCleanDialogFragment.show(getActivity().getFragmentManager(), "setEncryptionCleanDialogFragment");
//                        setEncryptionCleanDialogFragment.OnSetEncryptionDialogCleanFragmentChangeListener(new SetEncryptionCleanDialogFragment.OnEncryptionCleanDialogFragmentClickListener() {
//
//                            @Override
//                            public void onEncryptionCleanClickListener(boolean EncryptionCleanflag) {
//                                // TODO Auto-generated method stub
//                                if(EncryptionCleanflag){
                                    //恢复加默认数据
//                                    for(int i=0;i<DataStruct.CurMacMode.Out.OUT_CH_MAX;i++){
//                                        DataStruct.RcvDeviceData.OUT_CH[i] = DataStruct.DefaultDeviceData.OUT_CH[i];
//                                    }
                                   DataOptUtil.setCleanData();
                                    LoadingDialogFragment mvLoadingDialog = new LoadingDialogFragment();
                                    mvLoadingDialog.show(getActivity().getFragmentManager(), "mvLoadingDialog");
                                    FlashPageUI();
//                                }
//                            }
//                        });
                    }


                }
            });
        } else {
            ToastMsg(getResources().getString(R.string.off_line_mode));
        }
    }
    private void initView(View view){
        encryption = (Button) view.findViewById(R.id.id_b_encryption);
        btn_passive= (Button) view.findViewById(R.id.id_b_passive);
        btn_active= (Button) view.findViewById(R.id.id_b_active);

        mMixerItem[0] = (V_MixerItem) view.findViewById(R.id.id_mixer_0);
        mMixerItem[1] = (V_MixerItem) view.findViewById(R.id.id_mixer_1);
        mMixerItem[2] = (V_MixerItem) view.findViewById(R.id.id_mixer_2);
        mMixerItem[3] = (V_MixerItem) view.findViewById(R.id.id_mixer_3);
        mMixerItem[4] = (V_MixerItem) view.findViewById(R.id.id_mixer_4);
        mMixerItem[5] = (V_MixerItem) view.findViewById(R.id.id_mixer_5);
        mMixerItem[6] = (V_MixerItem) view.findViewById(R.id.id_mixer_6);
        mMixerItem[7] = (V_MixerItem) view.findViewById(R.id.id_mixer_7);
        mMixerItem[8] = (V_MixerItem) view.findViewById(R.id.id_mixer_8);
        mMixerItem[9] = (V_MixerItem) view.findViewById(R.id.id_mixer_9);
        mMixerItem[10] = (V_MixerItem) view.findViewById(R.id.id_mixer_10);
        mMixerItem[11] = (V_MixerItem) view.findViewById(R.id.id_mixer_11);
        mMixerItem[12] = (V_MixerItem) view.findViewById(R.id.id_mixer_12);
        mMixerItem[13] = (V_MixerItem) view.findViewById(R.id.id_mixer_13);
        mMixerItem[14] = (V_MixerItem) view.findViewById(R.id.id_mixer_14);
        mMixerItem[15] = (V_MixerItem) view.findViewById(R.id.id_mixer_15);

        ly_input=view.findViewById(R.id.ly_input);

        for(int i=0;i<MaxINPUT;i++){
            mMixerItem[i].TV_MixerChn.setTag(i);
            mMixerItem[i].LLY_Mixer.setTag(i);
            mMixerItem[i].LRY_Mixer.setTag(i);
            mMixerItem[i].B_MixerPolar.setTag(i);
            mMixerItem[i].B_MixerValInc.setTag(i);
            mMixerItem[i].B_MixerValSub.setTag(i);

            mMixerItem[i].B_MixerVal.setTag(i);
            mMixerItem[i].SB_Mixer_SeekBar.setTag(i);
            mMixerItem[i].B_MixerHide.setTag(i);
            mMixerItem[i].B_MixerResetVal.setTag(i);

            mMixerItem[i].LLY_Reset_Polar.setTag(i);
            mMixerItem[i].LLY_Reset.setTag(i);
            mMixerItem[i].LLY_INS.setTag(i);
            mMixerItem[i].LLY_SUB.setTag(i);
        }
        mMixerItem[1].TV_MixerChn.setTextColor(getResources().getColor(R.color.hi_level2));
        mMixerItem[2].TV_MixerChn.setTextColor(getResources().getColor(R.color.hi_level3));
        mMixerItem[3].TV_MixerChn.setTextColor(getResources().getColor(R.color.hi_level4));
//        mMixerItem[4].TV_MixerChn.setTextColor(getResources().getColor(R.color.hi_level5));
//        mMixerItem[5].TV_MixerChn.setTextColor(getResources().getColor(R.color.hi_level6));



    }
    /*********************************************************************/
    /***************************    initChannelSel     ****************************/
    /*********************************************************************/
    private void initChannelSel(View view){

        Channellists = new ArrayList<String>();
        for(int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++){
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }


        WV_OutVa = (WheelView) view.findViewById(R.id.id_output_va_wheelview);
        WV_OutVa.select(MacCfg.OutputChannelSel);
        WV_OutVa.lists(Channellists).showCount(5).selectTip("").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index,boolean fromUser) {
                if(!fromUser){
                    MacCfg.OutputChannelSel = index;
                    FlashChannelUI();
                }
            }
        }).build();
    }
    private void initViewContext(){

    }


    private void initConfigMac(){
        for(int i=0;i<16;i++){
            mMixerItem[i].setVisibility(View.GONE);
        }
        for(int i=0;i< DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE;i++){
            mMixerItem[i].setVisibility(View.VISIBLE);
            mMixerItem[i].TV_MixerChn.setText(DataStruct.CurMacMode.Mixer.Name[i]);
            mMixerItem[i].Img_inputsource.setImageDrawable(DataStruct.CurMacMode.Mixer.img_Name[i]);
        }
//
//        mMixerItem[4].setVisibility(View.GONE);
//        mMixerItem[5].setVisibility(View.GONE);
    }


    /*********************************************************************/
    /*****************************     混音  TODO        ****************************/
    /*********************************************************************/
    void FlashInputPolar(){
        int[] in= new int[DataStruct.CurMacMode.Mixer.MIXER_CH_MAX] ;

        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            //in[i]=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN_polar&(1<<i);
            if(i<=7){
                in[i]=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].lim_mode&(1<<i);
            }else{
                in[i]=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].linkgroup_num&(1<<(i-8));
            }
        }

        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            if(in[i]==0){
                mMixerItem[i].B_MixerPolar.setText(R.string.Polar_P);
                mMixerItem[i].B_MixerPolar.
                        setTextColor(getResources().getColor(R.color.weight_b_Polar_P_text_color));
            }else{
                mMixerItem[i].B_MixerPolar.setText(R.string.Polar_N);
                mMixerItem[i].B_MixerPolar.
                        setTextColor(getResources().getColor(R.color.weight_b_Polar_N_text_color));
            }
        }
    }

    private void setMixerVolumeByIndex(int index, int val){
        switch (index) {
            case 0: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol=val; break;
            case 1: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol=val; break;
            case 2: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol=val; break;
            case 3: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol=val; break;

            case 4: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol=val; break;
            case 5: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol=val; break;
            case 6: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol=val; break;
            case 7: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol=val; break;

            case 8: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol=val; break;
            case 9: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol=val; break;
            case 10: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol=val; break;
            case 11: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol=val; break;

            case 12: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol=val; break;
            case 13: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol=val; break;
            case 14: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol=val; break;
            case 15: DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol=val; break;

            default:
                break;
        }
    }

    private int getMixerVolumeByIndex(int index){
        switch (index) {
            case 0: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
            case 1: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
            case 2: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
            case 3: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

            case 4: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
            case 5: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
            case 6: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
            case 7: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

            case 8: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
            case 9: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
            case 10: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol;
            case 11: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol;

            case 12: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol;
            case 13: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol;
            case 14: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
            case 15: return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;

            default: return 0;
        }
    }

    private void setMixerBufVolumeByIndex(int index, int val){
        switch (index) {
            case 0: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol=val; break;
            case 1: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol=val; break;
            case 2: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol=val; break;
            case 3: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol=val; break;

            case 4: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol=val; break;
            case 5: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol=val; break;
            case 6: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol=val; break;
            case 7: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol=val; break;

            case 8: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol=val; break;
            case 9: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol=val; break;
            case 10: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol=val; break;
            case 11: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol=val; break;

            case 12: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol=val; break;
            case 13: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol=val; break;
            case 14: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol=val; break;
            case 15: DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol=val; break;

            default:
                break;
        }
    }

    private int getMixerBufVolumeByIndex(int index){
        switch (index) {
            case 0: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
            case 1: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
            case 2: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
            case 3: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

            case 4: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
            case 5: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
            case 6: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
            case 7: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

            case 8: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
            case 9: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
            case 10: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol;
            case 11: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol;

            case 12: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol;
            case 13: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol;
            case 14: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
            case 15: return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;

            default: return 0;
        }
    }

    private void setMixerVolumeState(boolean off){

        if(off){
            setMixerVolumeByIndex(MacCfg.inputChannelSel,0);
        }else{
            setMixerVolumeByIndex(MacCfg.inputChannelSel,100);
        }

    }

    void FlashInputVal(){
        //DataStruct.CurMacMode.Mixer.MIXER_CH_MAX
        for(int i=0;i<16;i++){
            if((getMixerVolumeByIndex(i) > DataStruct.CurMacMode.Mixer.Max_Mixer_Vol)
                    ||(getMixerVolumeByIndex(i) < 0)){
                setMixerVolumeByIndex(i, 100);
            }
        }

        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            mMixerItem[i].B_MixerVal.setText(String.valueOf(getMixerVolumeByIndex(i)));
//            if(i == 8){
//                mMixerItem[i].B_MixerVal.setText(String.valueOf(getMixerVolumeByIndex(15)));
//            }
        }

        FlashMixerResetButtomState();
        //
        if((DataStruct.CurMacMode.BOOL_ENCRYPTION)&&(MacCfg.bool_Encryption==true)){
            for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
                mMixerItem[i].B_MixerVal.setText("0");
            }
        }

    }
    void FlashMixerSeekbar(){
        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            mMixerItem[i].SB_Mixer_SeekBar.setProgress(getMixerVolumeByIndex(i));
//            if(i == 8){
//                mMixerItem[i].SB_Mixer_SeekBar.setProgress(getMixerVolumeByIndex(15));
//            }
        }

        if((DataStruct.CurMacMode.BOOL_ENCRYPTION)&&(MacCfg.bool_Encryption==true)){
            for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
                mMixerItem[i].SB_Mixer_SeekBar.setProgress(0);
            }
        }

        FlashMixerResetButtomState();
    }
    void FlashMixerChannelSel(){}
    void FlashMixerInputChannelSel(){
        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            mMixerItem[i].LLY_Mixer.setBackgroundResource(R.drawable.btn_input_name);
        }
        mMixerItem[MacCfg.inputChannelSel].LLY_Mixer.setBackgroundResource(R.drawable.chs_layoutc_press);
    }
    private void flashInputAction(int state){
        if(state==0){
            ly_input.setBackgroundResource(R.drawable.mixerselectbgicon_pass);
        }else{
            ly_input.setBackgroundResource(R.drawable.mixerselectbgicon_active);
        }
    }
    void syncMixerVolume(){
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol;

        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;
    }
    void FlashMixerResetButtomState(){
        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            if(getMixerVolumeByIndex(i) == 0){
                mMixerItem[i].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
            }else{
                mMixerItem[i].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
            }
        }
    }

    private void FlashMixerVolumeData(){

        if((getMixerVolumeByIndex(MacCfg.inputChannelSel) == 0)&&
                (getMixerBufVolumeByIndex(MacCfg.inputChannelSel) != 0)){
            mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
            setMixerVolumeByIndex(MacCfg.inputChannelSel, getMixerBufVolumeByIndex(MacCfg.inputChannelSel));
        }else if(getMixerVolumeByIndex(MacCfg.inputChannelSel) != 0){
            setMixerBufVolumeByIndex(MacCfg.inputChannelSel, getMixerVolumeByIndex(MacCfg.inputChannelSel));
            setMixerVolumeByIndex(MacCfg.inputChannelSel, 0);
            mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
        }


//        if(MacCfg.inputChannelSel != 8){
//            if((getMixerVolumeByIndex(MacCfg.inputChannelSel) == 0)&&
//                    (getMixerBufVolumeByIndex(MacCfg.inputChannelSel) != 0)){
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
//                setMixerVolumeByIndex(MacCfg.inputChannelSel, getMixerBufVolumeByIndex(MacCfg.inputChannelSel));
//            }else if(getMixerVolumeByIndex(MacCfg.inputChannelSel) != 0){
//                setMixerBufVolumeByIndex(MacCfg.inputChannelSel, getMixerVolumeByIndex(MacCfg.inputChannelSel));
//                setMixerVolumeByIndex(MacCfg.inputChannelSel, 0);
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
//            }
//        }else{
//            if((getMixerVolumeByIndex(14) == 0)&&
//                    (getMixerBufVolumeByIndex(14) != 0)){
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
//                setMixerVolumeByIndex(14, getMixerBufVolumeByIndex(14));
//                setMixerVolumeByIndex(15, getMixerBufVolumeByIndex(14));
//            }else if(getMixerVolumeByIndex(14) != 0){
//                setMixerBufVolumeByIndex(14, getMixerVolumeByIndex(14));
//                setMixerVolumeByIndex(14, 0);
//                setMixerVolumeByIndex(15, 0);
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
//            }
//        }

    }


    void MixerIN_ValInc(){
        int val = 0;

        val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
        if(++val>DataStruct.CurMacMode.Mixer.Max_Mixer_Vol){
            val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
        }
        setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
        mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
        mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));


//        if(MacCfg.inputChannelSel != 8){
//            val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
//            if(++val>DataStruct.CurMacMode.Mixer.Max_Mixer_Vol){
//                val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
//            }
//            setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
//            mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }else{
//            val = getMixerVolumeByIndex(14);
//            if(++val>DataStruct.CurMacMode.Mixer.Max_Mixer_Vol){
//                val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
//            }
//            setMixerVolumeByIndex(14, val);
//            setMixerVolumeByIndex(15, val);
//            mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }

        syncMixerVolume();
        FlashMixerResetButtomState();



    }

    void MixerIN_ValSub(){

        int val = 0;

        val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
        if(--val<0){
            val=0;
        }
        setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
        mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
        mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));

//        if(MacCfg.inputChannelSel != 8){
//            val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
//            if(--val<0){
//                val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
//            }
//            setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
//            mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }else{
//            val = getMixerVolumeByIndex(14);
//            if(--val<0){
//                val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
//            }
//            setMixerVolumeByIndex(14, val);
//            setMixerVolumeByIndex(15, val);
//            mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }

        syncMixerVolume();
        FlashMixerResetButtomState();



    }

    void MixerIN_ValShow(int val){
        setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
        mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));


//        if(MacCfg.inputChannelSel != 8){
//            setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }else{
//            setMixerVolumeByIndex(14, val);
//            setMixerVolumeByIndex(15, val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }
    }

    public void AddMixerPageListener(){
        //输入正反相
        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            mMixerItem[i].B_MixerPolar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    MacCfg.inputChannelSel = (int)view.getTag();

                    if(MacCfg.OutputChannelSel<=7){
                        if((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].lim_mode&(1<<MacCfg.inputChannelSel))==0){
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].lim_mode|=(1<<MacCfg.inputChannelSel);

                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setText(R.string.Polar_N);
                            //mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setBackgroundResource(R.drawable.btn_output_polar_p);
                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.
                                    setTextColor(getResources().getColor(R.color.weight_b_Polar_N_text_color));
                        }else if((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].lim_mode&(1<<MacCfg.inputChannelSel))>=1){
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].lim_mode&=~(1<<MacCfg.inputChannelSel);

                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setText(R.string.Polar_P);
                            //mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setBackgroundResource(R.drawable.btn_output_polar_n);
                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.
                                    setTextColor(getResources().getColor(R.color.weight_b_Polar_P_text_color));
                        }
                    }else{
                        if((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].linkgroup_num&(1<<(MacCfg.inputChannelSel-8)))==0){
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].linkgroup_num|=(1<<(MacCfg.inputChannelSel-8));

                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setText(R.string.Polar_N);
                            //mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setBackgroundResource(R.drawable.btn_output_polar_p);
                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.
                                    setTextColor(getResources().getColor(R.color.weight_b_Polar_N_text_color));
                        }else if((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].linkgroup_num&(1<<(MacCfg.inputChannelSel-8)))>=1){
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].linkgroup_num&=~(1<<(MacCfg.inputChannelSel-8));

                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setText(R.string.Polar_P);
                            //mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.setBackgroundResource(R.drawable.btn_output_polar_n);
                            mMixerItem[MacCfg.inputChannelSel].B_MixerPolar.
                                    setTextColor(getResources().getColor(R.color.weight_b_Polar_P_text_color));
                        }
                    }


                    FlashMixerInputChannelSel();
                }
            });
        }

        for(int i=0;i<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;i++){
            mMixerItem[i].B_MixerValInc.setTag(i);
            mMixerItem[i].B_MixerValSub.setTag(i);
            //音量加
            mMixerItem[i].B_MixerValInc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MacCfg.inputChannelSel = (int)arg0.getTag();
                    MixerIN_ValInc();
                }
            });
            mMixerItem[i].B_MixerValInc.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    MacCfg.inputChannelSel = (int)arg0.getTag();
                    mMixerItem[MacCfg.inputChannelSel].B_MixerValInc.setStart();
                    return false;
                }
            });
            mMixerItem[i].B_MixerValInc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    MixerIN_ValInc();
                }
            }, MacCfg.LongClickEventTimeMax);

            //音量减
            mMixerItem[i].B_MixerValSub.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MacCfg.inputChannelSel = (int)arg0.getTag();
                    MixerIN_ValSub();
                }
            });
            mMixerItem[i].B_MixerValSub.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    MacCfg.inputChannelSel = (int)arg0.getTag();
                    mMixerItem[MacCfg.inputChannelSel].B_MixerValSub.setStart();
                    return false;
                }
            });
            mMixerItem[i].B_MixerValSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    MixerIN_ValSub();
                }
            }, MacCfg.LongClickEventTimeMax);

        }

        for(int j=0;j<DataStruct.CurMacMode.Mixer.MIXER_CH_MAX;j++){
            mMixerItem[j].SB_Mixer_SeekBar.setProgressMax(DataStruct.CurMacMode.Mixer.Max_Mixer_Vol);
            mMixerItem[j].SB_Mixer_SeekBar.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {

                @Override
                public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress,
                                              boolean fromUser) {
                    //根据fromUser解锁onTouchEvent(MotionEvent event)可以传到父控制，或者消费掉MotionEvent
                    //VP_CHS_Pager.setNoScrollOnIntercept(fromUser);
                    MacCfg.inputChannelSel = (int)mhs_SeekBar.getTag();

                    MixerIN_ValShow(progress);
                    FlashMixerInputChannelSel();

                    syncMixerVolume();
                    FlashMixerResetButtomState();


                }
            });

            mMixerItem[j].B_MixerResetVal.setTag(j);
            mMixerItem[j].B_MixerResetVal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    MacCfg.inputChannelSel = (Integer) view.getTag();

                    FlashMixerInputChannelSel();
                    FlashMixerVolumeData();
                    FlashInputVal();
                    FlashMixerSeekbar();
                }
            });

        }




    }
}
