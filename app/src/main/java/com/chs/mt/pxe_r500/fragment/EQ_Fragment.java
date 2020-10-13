package com.chs.mt.pxe_r500.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.PEQToGEQDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEQFreqBWGainDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionCleanDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionDialogFragment;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.tools.EQ;
import com.chs.mt.pxe_r500.tools.EQ_SeekBar;
import com.chs.mt.pxe_r500.tools.wheel.WheelView;
import com.chs.mt.pxe_r500.viewItem.V_EQ_Item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class EQ_Fragment extends Fragment {
    private Toast mToast;
    private static Context mContext;
    //EQ
    private final static int EQMax = 31;
    private V_EQ_Item[] EQItem = new V_EQ_Item[EQMax];
    private EQ toneHomeEq;
    private TextView TV_EQGainMax, TV_EQGainMin;//TV_EQ_Num


    private boolean bool_EQ_ModeFlag = false;
    private Button B_EQSetDefault, B_EQSetRecover;
    private boolean bool_ByPass = false;

    private Button encryption;
    ////////////通道选择
    private static final int UI_OUT_CH_MAX = 6;
    //通道选择
    private WheelView WV_OutVa;
    private List<String> Channellists;
//    private Button[] B_Channel = new Button[UI_OUT_CH_MAX];
//    private View[] B_ChannelView = new View[UI_OUT_CH_MAX];

    private Button btn_output_eq_mode, btn_output_Geq_mode;
    //对话框
    private SetEncryptionDialogFragment setEncryptionDialogFragment = null;
    private LoadingDialogFragment mLoadingDialogFragment = null;
    private AlertDialogFragment alertDialogFragment = null;
    private SetEQFreqBWGainDialogFragment setEQFreqBWGainDialogFragment = null;
    private SetEncryptionCleanDialogFragment setEncryptionCleanDialogFragment = null;
    private PEQToGEQDialogFragment peqToGEQDialogFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_eq, container, false);

        initData();
        initView(view);
        initClick();
        FlashPageUI();
        return view;
    }

    private void initView(View view) {

        Channellists = new ArrayList<String>();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }

        WV_OutVa = (WheelView) view.findViewById(R.id.id_output_va_wheelview);
        WV_OutVa.select(MacCfg.OutputChannelSel);
        WV_OutVa.lists(Channellists).showCount(5).selectTip("").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index, boolean fromUser) {
                if (!fromUser) {
                    MacCfg.OutputChannelSel = index;
                    FlashChannelUI();
                }
            }
        }).build();

        initChannelSel(view);
        AddViewEqualizer_Pager(view);
    }

    private void initData() {

    }

    private void initClick() {
        FlashPageUI();
        encryption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEncryptionDialog();
            }
        });
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

    private void flashLinkDataUI(int Tpe) {
        MacCfg.UI_Type = Tpe;
        DataOptUtil.syncLinkData(Tpe);

    }

    private void showEncryptionDialog() {
        if (DataStruct.isConnecting) {
            if (setEncryptionDialogFragment == null) {
                setEncryptionDialogFragment = new SetEncryptionDialogFragment();
            }
            if (!setEncryptionDialogFragment.isAdded()) {
                setEncryptionDialogFragment.show(getActivity().getFragmentManager(), "setEncryptionDialogFragment");
            }

            setEncryptionDialogFragment.OnSetEncryptionDialogFragmentChangeListener(new SetEncryptionDialogFragment.OnEncryptionDialogFragmentClickListener() {
                @Override
                public void onEncryptionClickListener(
                        boolean Encryptionflag, boolean recallFlag) {
                    if (!recallFlag) {
                        if (Encryptionflag) {//加密处理
                            // DataOptUtil.SaveGroupData(0);
                        } else {//解密处理
                            //DataOptUtil.SaveGroupData(0);
                        }
                        //showLoadingDialog();
                        /*刷新界面*/
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
                        intentw.putExtra("state", true);
                        getActivity().sendBroadcast(intentw);
                    } else {//清除数据
//                        if (setEncryptionCleanDialogFragment == null) {
//                            setEncryptionCleanDialogFragment = new SetEncryptionCleanDialogFragment();
//                        }
//                        if (!setEncryptionCleanDialogFragment.isAdded()) {
//                            setEncryptionCleanDialogFragment.show(getActivity().getFragmentManager(), "setEncryptionCleanDialogFragment");
//                        }
//
//                        setEncryptionCleanDialogFragment.OnSetEncryptionDialogCleanFragmentChangeListener(new SetEncryptionCleanDialogFragment.OnEncryptionCleanDialogFragmentClickListener() {
//
//                            @Override
//                            public void onEncryptionCleanClickListener(boolean EncryptionCleanflag) {
//                                // TODO Auto-generated method stub
//                                if (EncryptionCleanflag) {
                        //恢复加默认数据
//                                    for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
//                                        DataStruct.RcvDeviceData.OUT_CH[i] = DataStruct.DefaultDeviceData.OUT_CH[i];
//                                    }
                        DataOptUtil.setCleanData();
                        showLoadingDialog();
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


    private void showLoadingDialog() {
        if (mLoadingDialogFragment == null) {
            mLoadingDialogFragment = new LoadingDialogFragment();
        }
        if (!mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.show(getActivity().getFragmentManager(), "mLoadingDialogFragment");
        }

    }
    /*********************************************************************/
    /***************************    initChannelSel     ****************************/
    /*********************************************************************/
    private void initChannelSel(View view) {
//        //TODO
//        B_Channel[0] = (Button)view.findViewById(R.id.id_b_chh0);
//        B_Channel[1] = (Button)view.findViewById(R.id.id_b_chh1);
//        B_Channel[2] = (Button)view.findViewById(R.id.id_b_chh2);
//        B_Channel[3] = (Button)view.findViewById(R.id.id_b_chh3);
//        B_Channel[4] = (Button)view.findViewById(R.id.id_b_chh4);
//        B_Channel[5] = (Button)view.findViewById(R.id.id_b_chh5);
//
//        B_ChannelView[0] = (View)view.findViewById(R.id.id_v0);
//        B_ChannelView[1] = (View)view.findViewById(R.id.id_v1);
//        B_ChannelView[2] = (View)view.findViewById(R.id.id_v2);
//        B_ChannelView[3] = (View)view.findViewById(R.id.id_v3);
//        B_ChannelView[4] = (View)view.findViewById(R.id.id_v4);
//        B_ChannelView[5] = (View)view.findViewById(R.id.id_v5);
//
//
//        for(int i=0;i<UI_OUT_CH_MAX;i++){
//            B_Channel[i].setTag(i);
//            B_Channel[i].setText(DataStruct.CurMacMode.Out.Name[i]);
//            B_Channel[i].setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MacCfg.OutputChannelSel = (int)v.getTag();
//                    FlashChannelUI();
//                }
//            });
//        }


    }

    private void flashChannelSel() {
//        for(int i=0;i<UI_OUT_CH_MAX;i++){
//            B_Channel[i].setTextColor(getResources().getColor(R.color.outputCH_normal_text_color));
//            B_ChannelView[i].setVisibility(View.GONE);
//        }
//        B_Channel[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.outputCH_press_text_color));
//        B_ChannelView[MacCfg.OutputChannelSel].setVisibility(View.VISIBLE);
    }


    public void FlashPageUI() {

        WV_OutVa.setIndex(MacCfg.OutputChannelSel);
        //  if(DataStruct.CurMacMode.BOOL_ENCRYPTION){
        if (MacCfg.bool_Encryption) {
            encryption.setVisibility(View.VISIBLE);
            for (int i = 0; i < EQMax; i++) {
                EQItem[i].MVS_SeekBar.setProgress(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                EQItem[i].B_Gain.setText(ChangeGainValume(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
                EQItem[i].B_BW.setText(ChangeBWValume(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw));
                EQItem[i].B_Freq.setText(String.valueOf(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq) + "Hz");
            }
            toneHomeEq.SetEQData(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
        } else {
            encryption.setVisibility(View.GONE);
            FlashChannelUI();
        }
//        }else {
//            encryption.setVisibility(View.GONE);
//            FlashChannelUI();
//        }
    }
    /*********************************************************************/
    /***************************    EQ     ****************************/
    /*********************************************************************/
    private void AddViewEqualizer_Pager(View viewEQPage) {
        encryption = (Button) viewEQPage.findViewById(R.id.id_b_encryption);
        toneHomeEq = (EQ) viewEQPage.findViewById(R.id.id_eq_eqfilter_page);
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
        B_EQSetRecover = (Button) viewEQPage.findViewById(R.id.id_b_eq_recover);
        B_EQSetDefault = (Button) viewEQPage.findViewById(R.id.id_b_eq_reset);

        btn_output_eq_mode = viewEQPage.findViewById(R.id.id_b_eq_mode);
        btn_output_Geq_mode = viewEQPage.findViewById(R.id.id_b_eq_Geq_mode);

        TV_EQGainMax = (TextView) viewEQPage.findViewById(R.id.id_tv_equalizer_eq_gainmax);
        TV_EQGainMin = (TextView) viewEQPage.findViewById(R.id.id_tv_equalizer_eq_gainmin);
        TV_EQGainMax.setText("+" + String.valueOf(DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 20) + "dB");
        TV_EQGainMin.setText("-" + String.valueOf(DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 20) + "dB");

        EQItem[0] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_1);
        EQItem[1] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_2);
        EQItem[2] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_3);
        EQItem[3] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_4);
        EQItem[4] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_5);
        EQItem[5] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_6);
        EQItem[6] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_7);
        EQItem[7] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_8);
        EQItem[8] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_9);
        EQItem[9] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_10);
        EQItem[10] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_11);
        EQItem[11] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_12);
        EQItem[12] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_13);
        EQItem[13] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_14);
        EQItem[14] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_15);
        EQItem[15] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_16);
        EQItem[16] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_17);
        EQItem[17] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_18);
        EQItem[18] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_19);
        EQItem[19] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_20);
        EQItem[20] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_21);
        EQItem[21] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_22);
        EQItem[22] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_23);
        EQItem[23] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_24);
        EQItem[24] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_25);
        EQItem[25] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_26);
        EQItem[26] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_27);
        EQItem[27] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_28);

        EQItem[28] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_29);
        EQItem[29] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_30);
        EQItem[30] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_31);


        AddViewEqualizerListen_Pager();
    }

    private void AddViewEqualizerListen_Pager() {
        //初始化
        for (int i = 0; i < EQMax; i++) {
            EQItem[i].B_ID.setText(String.valueOf(i + 1));
            EQItem[i].MVS_SeekBar.setProgressMax(Define.EQ_BW_MAX);//DataStruct.CurMacMode.EQ.EQ_Gain_MAX
        }

        /**/
        B_EQSetRecover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ByEQPassStore() && !ByEQPass()) {
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
                bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.dialog_title_Prompt));
                bundle.putString(AlertDialogFragment.ST_SetOKText, getResources().getString(R.string.Sure));
                if (bool_ByPass) {//设置直通
                    bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ByPassEQ));
                } else if (!bool_ByPass) {////设置恢复
                    bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.RecoverEQ));
                }

                if (alertDialogFragment == null) {
                    alertDialogFragment = new AlertDialogFragment();
                }
                if (!alertDialogFragment.isAdded()) {
                    alertDialogFragment.setArguments(bundle);
                    alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
                }
                alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        if (boolClick) {
                            Set_Recover();
                        }
                    }
                });
            }
        });
//        B_EQSetRecover.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
////
////                        if((!ByEQPass())&&(ByEQPassStore())){
////
////                        }else{
//
////                        }
//
//                        break;
//                    default:
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        break;
//                }
//                return false;
//            }
//        });
        B_EQSetDefault.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
                bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ResetEQ));
                bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.dialog_title_Prompt));
                bundle.putString(AlertDialogFragment.ST_SetOKText, getResources().getString(R.string.Sure));
                if (alertDialogFragment == null) {
                    alertDialogFragment = new AlertDialogFragment();
                }
                if (!alertDialogFragment.isAdded()) {
                    alertDialogFragment.setArguments(bundle);
                    alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
                }

                alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        if (boolClick) {
                            Set_Default();
                        }
                    }
                });
            }
        });

//        B_EQSetDefault.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
//                        bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ResetEQ));
//
//                        if(alertDialogFragment == null){
//                            alertDialogFragment= new AlertDialogFragment();
//                        }
//                        if (!alertDialogFragment.isAdded()) {
//                            alertDialogFragment.setArguments(bundle);
//                            alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
//                        }
//
//                        alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {
//
//                            @Override
//                            public void onClickDialogListener(int type, boolean boolClick) {
//                                if(boolClick){
//                                    Set_Default();
//                                }
//                            }
//                        });
//                        break;
//                    default:
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        break;
//                }
//                return false;
//            }
//        });

        btn_output_eq_mode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PEQToGEQDialogFragment.ST_DataOPT, 1);
                    bundle.putString(PEQToGEQDialogFragment.ST_SetMessage, getResources().getString(R.string.ResetEQ));

                    if (peqToGEQDialogFragment == null) {
                        peqToGEQDialogFragment = new PEQToGEQDialogFragment();
                    }
                    if (!peqToGEQDialogFragment.isAdded()) {
                        peqToGEQDialogFragment.setArguments(bundle);
                        peqToGEQDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
                    }
                    peqToGEQDialogFragment.OnSetOnClickDialogListener(new PEQToGEQDialogFragment.SetOnClickDialogListener() {

                        @Override
                        public void onClickDialogListener(int type, boolean boolClick) {
                            if (boolClick) {
                                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode = 0;
                                btn_output_eq_mode.setBackgroundResource(R.drawable.btn_output_link_press);
                                //  btn_output_eq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));

                                btn_output_Geq_mode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
                                //btn_output_Geq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
                                //LY_EQ_Mode.setVisibility(View.VISIBLE);

                                Set3BClick(false);  /*false 为可点击*/
//                                RestoreEQTo_EQ_Buf_Form_EQ_Default();
//                                FlashChannelUI();
                                Set_Default();
                                setZero();
                                //联调
                                DataOptUtil.syncLinkData(Define.UI_EQ_G_P_MODE_EQ);
                                //  ReturnNum.flashOutModeLinkDataUI(Define.UI_EQ_G_P_MODE_EQ);
                                setEQColor(MacCfg.EQ_Num);
                                ResetEQColor(MacCfg.EQ_Num);
                                bool_ByPass = false;
                            }
                        }
                    });

//                    myDialog = new AlertIOSDialog(getActivity()).builder();
//                    myDialog.setGone();
//                    myDialog.setTitle(getResources().getString(R.string.warning));
//                    myDialog.setMsg(getResources().getString(R.string.Encrying_data));
//                    myDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
//                    myDialog.setPositiveButton(getResources().getString(R.string.Sure), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                    myDialog.show();

                }
            }
        });
        btn_output_Geq_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEQByPass();

                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PEQToGEQDialogFragment.ST_DataOPT, 2);
                    bundle.putString(PEQToGEQDialogFragment.ST_SetMessage, getResources().getString(R.string.ResetEQ));

                    if (peqToGEQDialogFragment == null) {
                        peqToGEQDialogFragment = new PEQToGEQDialogFragment();
                    }
                    if (!peqToGEQDialogFragment.isAdded()) {
                        peqToGEQDialogFragment.setArguments(bundle);
                        peqToGEQDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
                    }

                    peqToGEQDialogFragment.OnSetOnClickDialogListener(new PEQToGEQDialogFragment.SetOnClickDialogListener() {

                        @Override
                        public void onClickDialogListener(int type, boolean boolClick) {
                            if (boolClick) {
                                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode = 1;
                                btn_output_Geq_mode.setBackgroundResource(R.drawable.btn_output_link_press);
                                //btn_output_Geq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
                                btn_output_eq_mode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
                                //  btn_output_eq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
                                Set3BClick(true);  /*true 为不可点击*/
//                                RestoreEQTo_EQ_Buf_Form_EQ_Default();
//                                FlashChannelUI();
                                Set_Default();
                                //联调
                                setZero();
                                DataOptUtil.syncLinkData(Define.UI_EQ_G_P_MODE_EQ);
                                //  ReturnNum.flashOutModeLinkDataUI(Define.UI_EQ_G_P_MODE_EQ);
                                setEQColor(MacCfg.EQ_Num);
                                ResetEQColor(MacCfg.EQ_Num);
                                bool_ByPass = false;

                            }
                        }
                    });
                }
            }
        });

        for (int i = 0; i < EQMax; i++) {
            EQItem[i].MVS_SeekBar.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);//
            //EQItem[i].B_Gain.setClickable(false);
            //EQItem[i].B_Freq.setClickable(false);
            //EQItem[i].B_BW.setClickable(false);
            EQItem[i].MVS_SeekBar.setTag(i);
            EQItem[i].MVS_SeekBar.setOnSeekBarChangeListener(new EQ_SeekBar.OnMSBEQSeekBarChangeListener() {
                @Override
                public void onProgressChanged(EQ_SeekBar mvs_SeekBar, int progress, boolean fromUser) {
                    //根据fromUser解锁onTouchEvent(MotionEvent event)可以传到父控制，或者消费掉MotionEvent
                    //VP_CHS_Pager.setNoScrollOnIntercept(fromUser);
                    MacCfg.EQ_Num = (int) mvs_SeekBar.getTag();
                    FlashEQLevel(progress);
                    setEQColor(MacCfg.EQ_Num);
                }
            });
            //B_Gain
            EQItem[i].B_Gain.setTag(i);
            EQItem[i].B_Gain.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {


                    MacCfg.EQ_Num = (int) view.getTag();
                    setEQColor(MacCfg.EQ_Num);
                    Bundle bundle = new Bundle();
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataOPT, SetEQFreqBWGainDialogFragment.DataOPT_Gain);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_Data,
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]
                                    .EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataNUM, MacCfg.EQ_Num);

                    if (setEQFreqBWGainDialogFragment == null) {
                        setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    }
                    if (!setEQFreqBWGainDialogFragment.isAdded()) {
                        setEQFreqBWGainDialogFragment.setArguments(bundle);
                        setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");
                    }


                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                            //刷新图表
                            FlashEQLevel(Gain);
                            EQItem[MacCfg.EQ_Num].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                            //联调
                            flashLinkDataUI(Define.UI_EQ_Level);
                        }

                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                        }

                        @Override
                        public void onBWSeekBarListener(int BW, int type, boolean flag) {
                        }
                    });
                }
            });

            //BW
            EQItem[i].B_BW.setTag(i);
            EQItem[i].B_BW.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                        return;
                    }
                    MacCfg.EQ_Num = (int) view.getTag();
                    setEQColor(MacCfg.EQ_Num);


                    Bundle bundle = new Bundle();
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataOPT, SetEQFreqBWGainDialogFragment.DataOPT_BW);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataNUM, MacCfg.EQ_Num);

                    if (setEQFreqBWGainDialogFragment == null) {
                        setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    }
                    if (!setEQFreqBWGainDialogFragment.isAdded()) {
                        setEQFreqBWGainDialogFragment.setArguments(bundle);
                        setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");
                    }

                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                        }

                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                        }

                        @Override
                        public void onBWSeekBarListener(int BW, int type, boolean flag) {
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw = BW;
                            EQItem[MacCfg.EQ_Num].B_BW.setText(ChangeBWValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw));
                            FlashEQPageUI();
                            //联调
                            flashLinkDataUI(Define.UI_EQ_BW);
                        }
                    });
                }
            });

            //FREQ
            EQItem[i].B_Freq.setTag(i);
            EQItem[i].B_Freq.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                        return;
                    }
                    MacCfg.EQ_Num = (int) view.getTag();
                    setEQColor(MacCfg.EQ_Num);


                    Bundle bundle = new Bundle();
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataOPT, SetEQFreqBWGainDialogFragment.DataOPT_Freq);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataNUM, MacCfg.EQ_Num);

                    if (setEQFreqBWGainDialogFragment == null) {
                        setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    }
                    if (!setEQFreqBWGainDialogFragment.isAdded()) {
                        setEQFreqBWGainDialogFragment.setArguments(bundle);
                        setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");
                    }

                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                        }

                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq = Freq;
                            EQItem[MacCfg.EQ_Num].B_Freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq) + "Hz");
                            //刷新图表
                            FlashEQPageUI();
                            flashLinkDataUI(Define.UI_EQ_Freq);
                        }

                        @Override
                        public void onBWSeekBarListener(int BW, int type, boolean flag) {
                        }
                    });
                }
            });
            EQItem[i].LY_ResetEQ.setTag(i);
            EQItem[i].LY_ResetEQ.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    MacCfg.EQ_Num = (int) view.getTag();

                    if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level ==
                            DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO
                            && (DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)

                    ) {
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num];
                        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;


                        // DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
                        flashLinkDataUI(Define.UI_EQ_Restore);

                    } else if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level
                            != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)
                    ) {
                        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;

                        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level;
//
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;

                        flashLinkDataUI(Define.UI_EQ_RESET);
                    }
//
//                    //刷新图表
//                    EQItem[MacCfg.EQ_Num].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level-DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
//                    EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level-DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
                    FlashEQPageUI();
                    FlashChannelUI();
//                    CheckEQByPass();
                    //联调


                }
            });
        }
    }


    /*
     * 根据频率值更新
     */
    int GetFreqDialogSeekBarIndex(int fP) {
        int i;
        int index = 0;
        for (i = 0; i < 240; i++) {
            if ((fP >= Define.FREQ241[i]) && (fP <= Define.FREQ241[i + 1])) {
                index = i + 1;
            }
        }
        return index;
    }

    private void setZero() {
        for (int j = 0; j < EQMax; j++) {
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = Define.EQ_LEVEL_ZERO;
        }
    }

    //------------------------------------
    //获取Equalizer 的EQ的增益数据显示
    private String ChangeGainValume(int num) {
        //System.out.println("ChangeValume:"+num);
        String show = null;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show = decimalFormat.format(0.0 - (DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 2 - num) / 10.0);//format 返回的是字符串
        show = show + "dB";
        return show;
    }

    //获取Equalizer 的EQ的Q值数据显示
    private String ChangeBWValume(int num) {
        if (num > Define.EQ_BW_MAX) {
            num = Define.EQ_BW_MAX;
        }
        String show = null;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show = decimalFormat.format(Define.EQ_BW[num]);//format 返回的是字符串
        return show;
    }

    private void FlashEQPageUI() {
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
    }

    private void FlashEQPageChannelSel() {
        /*
        if(MacCfg.OUT_CH_MAX==6){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH6_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH6_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH6_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH6_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }else if(MacCfg.OUT_CH_MAX==8){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH8_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH8_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH8_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH8_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }else if(MacCfg.OUT_CH_MAX==10){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH10_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH10_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH10_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH10_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }else if(MacCfg.OUT_CH_MAX==12){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH12_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH12_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH12_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH12_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }
        */
    }

    public void FlashChannelUI() {
        flashChannelSel();
        //初始化
        for (int i = 0; i < EQMax; i++) {
            EQItem[i].B_ID.setText(String.valueOf(i + 1));
            EQItem[i].MVS_SeekBar.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);//DataStruct.CurMacMode.EQ.EQ_Gain_MAX
            //DataStruct.GainBuf[i] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        }


        FlashEQPageChannelSel();

        for (int i = 0; i < EQMax; i++) {
            if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level < DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN)
                    || (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level > DataStruct.CurMacMode.EQ.EQ_LEVEL_MAX)) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }

            if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw < 0)
                    || (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw > Define.EQ_BW_MAX)) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw = 0;
            }

            if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq < 20)
                    || (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq > 20000)) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq = 20;
            }
        }
        CheckEQOneByOneLevel();//刷新E单点直通
        CheckEQByPass();//刷新直通和恢复
        setEQColor(MacCfg.EQ_Num);
//        TV_EQ_Num.setText("EQ"+(MacCfg.EQ_Num+1));

        for (int i = 0; i < EQMax; i++) {

            EQItem[i].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
            EQItem[i].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
            EQItem[i].B_BW.setText(ChangeBWValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw));
            EQItem[i].B_Freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq) + "Hz");
        }
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
            bool_EQ_ModeFlag = true;
            btn_output_Geq_mode.setBackgroundResource(R.drawable.btn_output_link_press);
            // btn_output_Geq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
            btn_output_eq_mode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
            //btn_output_eq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
        } else if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            bool_EQ_ModeFlag = false;
            btn_output_eq_mode.setBackgroundResource(R.drawable.btn_output_link_press);
            // btn_output_eq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));

            btn_output_Geq_mode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
            //btn_output_Geq_mode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
        }
        // setGainBufValue();
//        B_Encryption.setVisibility(View.GONE);
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
    }

    private void setGainBufValue() {
        for (int i = 0; i < EQMax; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                DataStruct.GainBuf[MacCfg.OutputChannelSel][i] = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level;
                DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            } else {
                DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level;
            }

        }
    }

    private void Set3BClick(boolean canclick) {
        if (!canclick) {
            for (int i = 0; i < EQMax; i++) {
                EQItem[i].B_Freq.setClickable(true);
                EQItem[i].B_BW.setClickable(true);
                EQItem[i].B_Gain.setClickable(true);
            }
        } else {
            for (int i = 0; i < EQMax; i++) {
                EQItem[i].B_Freq.setClickable(false);
                EQItem[i].B_BW.setClickable(false);
                //EQItem[i].B_Gain.setClickable(false);
            }
        }
    }

    private void ResetEQColor() {
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            for (int i = 0; i < EQMax; i++) {
                EQItem[i].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            }
        } else {
            for (int i = 0; i < EQMax; i++) {
                EQItem[i].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
                EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
                EQItem[i].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            }
        }
    }

    private void ResetEQColor(int num) {
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            //for(int i=0;i<EQMax;i++){
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            //}
        } else {
            //for(int i=0;i<EQMax;i++){
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            //}
        }
    }

    private void setEQColor(int num) {
        ResetEQColor();
        EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
        } else {
//			EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
//			EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
//			EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));

            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
        }

    }

    //点击重置均衡(清空所有数据)
    private void RestoreEQTo_EQ_Buf_Form_EQ_Default() {
        //for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
        for (int j = 0; j < EQMax; j++) {
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;

            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            DataStruct.GainBuf[MacCfg.OutputChannelSel][j] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        }
        SetEQStoreToDefault();
        //}
    }

    private void SaveEQTo_EQ_Store() {
        for (int j = 0; j < EQMax; j++) {
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
            //     DataStruct.GainBuf[MacCfg.OutputChannelSel][j] = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
            //			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
    }

    private void SetEQStoreToDefault() {
        for (int j = 0; j < EQMax; j++) {
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
    }

    private void EQ_StoreTo_Cur() {
        for (int j = 0; j < EQMax; j++) {
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
    }

    //恢复出厂数据的界面
    private void Set_Default() {
        RestoreEQTo_EQ_Buf_Form_EQ_Default();
        //联调
        flashLinkDataUI(Define.UI_EQ_ALL);
        //刷新图表
        FlashChannelUI();
        B_EQSetRecover.setText(R.string.Restore_EQ);
    }

    private void CheckEQOneByOneLevel() {
        for (int i = 0; i < EQMax; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                EQItem[i].B_ResetEQ.setBackgroundResource(R.drawable.chs_eq_resetg_normal);
            } else {
                EQItem[i].B_ResetEQ.setBackgroundResource(R.drawable.chs_eq_resetg_press);
            }
        }
    }

    private void CheckEQByPass() {
        bool_ByPass = ByEQPass();
        if (bool_ByPass) {//可以设置直通
            B_EQSetRecover.setText(R.string.Bypass_EQ);
            // B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
            B_EQSetRecover.setBackgroundResource(R.drawable.btn_output_link_press);
        } else if (!bool_ByPass) {//可以设置恢复
            B_EQSetRecover.setText(R.string.Restore_EQ);
            // B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
        }
    }

    private boolean ByEQPass() {
        boolean res = false;
        for (int i = 0; i < EQMax; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                res = true;
            }
        }
        return res;
    }


    private boolean ByEQPassStore() {
        boolean res = false;
        for (int i = 0; i < EQMax; i++) {
            if (DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                res = true;
            }
        }
        return res;
    }

    private void Set_Recover() {
        System.out.println("FUCK Set_Recover");
        if (bool_ByPass) {//设置直通    --  恢复状态
            //保存数据用于恢复
            bool_ByPass = false;
            for (int j = 0; j < EQMax; j++) {
                DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;

                DataStruct.GainBuf[MacCfg.OutputChannelSel][j] = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }
//            for(int j=0;j<EQMax;j++){
//                System.out.println("FUCK --ff EQ["+j+"].level="+DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
//            SaveEQTo_EQ_Store();
//            for (int i = 0; i < EQMax; i++) {
//                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
//            }
            flashLinkDataUI(Define.UI_EQ_Zero);
            B_EQSetRecover.setText(R.string.Restore_EQ);
//            for(int j=0;j<EQMax;j++){
//                System.out.println("FUCK --buf EQ["+j+"].level="+DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
        } else if (!bool_ByPass) {//设置恢复
//            for(int j=0;j<EQMax;j++){
//                System.out.println("FUCK ##buf EQ["+j+"].level="+DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
            bool_ByPass = true;
            // EQ_StoreTo_Cur();
            for (int j = 0; j < EQMax; j++) {

                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;

                DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }
            B_EQSetRecover.setText(R.string.Bypass_EQ);
            flashLinkDataUI(Define.UI_EQ_Recover);
        }
        FlashChannelUI();
        //联调

    }

    private void FlashEQLevel(int progress) {
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(progress));
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
            EQItem[MacCfg.EQ_Num].B_ResetEQ.setBackgroundResource(R.drawable.chs_eq_resetg_normal);
        } else {
            EQItem[MacCfg.EQ_Num].B_ResetEQ.setBackgroundResource(R.drawable.chs_eq_resetg_press);
        }

        CheckEQByPass();//刷新直通和恢复
        FlashEQPageUI();
        //联调
        flashLinkDataUI(Define.UI_EQ_Level);
    }

}