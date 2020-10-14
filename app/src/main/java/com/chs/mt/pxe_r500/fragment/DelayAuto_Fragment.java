package com.chs.mt.pxe_r500.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bean.Temp;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.dialogFragment.Delay_groupDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetDelayDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionCleanDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionDialogFragment;
import com.chs.mt.pxe_r500.operation.DataOptUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//
public class DelayAuto_Fragment extends Fragment {
    private static final int SPK_MAX = 62;
    private Button delay_car[] = new Button[SPK_MAX];
    private TextView txt_car[] = new TextView[SPK_MAX];
    private Button btn_delay_unit;
    private Toast mToast;
    private LinearLayout ly_delay_sub;
    private LinearLayout ly_delay_group;
    //左全频
    private int Left_All[] = new int[]{1, 2, 3, 4, 5, 13, 14, 24, 26,29,
            32, 33, 34,35,36, 57,55,60,45};

    //左高频
    private int Left_high[] = new int[]{0, 12,31, 41};
    //右全频
    private int Right_All[] = new int[]{7, 8,  10, 11, 16, 17, 25, 27,30,
            38, 39, 41,42, 45, 58, 56, 47,61,48};
    //右高频
    private int Right_high[] = new int[]{6, 15, 37, 46};

    //前中置高频
    private int center_high[] = new int[]{18,49};

    //后中置和超低
    private int Low_All[] = new int[]{28,23,59,54};
    //前中置全频
    private int center_All[] = new int[]{19,50};

    //后中置高频
    private int R_center_high[] = new int[]{20,51};

    //左超低
    private int L_Low_All[] = new int[]{21,52};
    //右超低
    private int R_Low_All[] = new int[]{22,53};

    private SetEncryptionDialogFragment setEncryptionDialogFragment = null;
    private SetEncryptionCleanDialogFragment setEncryptionCleanDialogFragment = null;

    private Delay_groupDialogFragment delay_groupDialogFragment=null;
    private LoadingDialogFragment mLoadingDialogFragment=null;
    private List<Integer> aList = new ArrayList<>();
    private SetDelayDialogFragment setDelayDialogFragment;
    private List<Integer> newAlist = new ArrayList<>();   //存储当前的通道做为key
    private List<Integer> countAlist = new ArrayList<>();  //存储当前通道类型为value
    private List<Integer> keyList = new ArrayList<>();
    private Button encryption;
    private Button btn_sound;

    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    private int OutChannenlTemp = 0;
    private ImageView it;
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();

        View view = inflater.inflate(R.layout.chs_dialog_car_input_delay, container, false);
        initView(view);
        initClick();

        FlashPageUI();
        return view;
    }

    private void init() {
//         it = view.findViewById(R.id.id_delay_car_7);
//        it.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                it.setHI
//            }
//        });
    }

    private void initView(View view) {
        encryption=view.findViewById(R.id.id_b_encryption);
        delay_car[0] =  view.findViewById(R.id.id_delay_car_1);
        delay_car[1] =  view.findViewById(R.id.id_delay_car_2);
        delay_car[2] =  view.findViewById(R.id.id_delay_car_3);
        delay_car[3] =  view.findViewById(R.id.id_delay_car_4);
        delay_car[4] =  view.findViewById(R.id.id_delay_car_5);
        delay_car[5] =  view.findViewById(R.id.id_delay_car_6);
        delay_car[6] =  view.findViewById(R.id.id_delay_car_7);
        delay_car[7] =  view.findViewById(R.id.id_delay_car_8);
        delay_car[8] =  view.findViewById(R.id.id_delay_car_9);
        delay_car[9] =  view.findViewById(R.id.id_delay_car_10);
        delay_car[10] =  view.findViewById(R.id.id_delay_car_11);
        delay_car[11] =  view.findViewById(R.id.id_delay_car_12);
        delay_car[12] =  view.findViewById(R.id.id_delay_car_13);
        delay_car[13] =  view.findViewById(R.id.id_delay_car_14);
        delay_car[14] =  view.findViewById(R.id.id_delay_car_15);
        delay_car[15] =  view.findViewById(R.id.id_delay_car_16);
        delay_car[16] =  view.findViewById(R.id.id_delay_car_17);
        delay_car[17] =  view.findViewById(R.id.id_delay_car_18);
        delay_car[18] =  view.findViewById(R.id.id_delay_car_19);
        delay_car[19] =  view.findViewById(R.id.id_delay_car_20);
        delay_car[20] =  view.findViewById(R.id.id_delay_car_21);
        delay_car[21] =  view.findViewById(R.id.id_delay_car_22);
        delay_car[22] =  view.findViewById(R.id.id_delay_car_23);
        delay_car[23] =  view.findViewById(R.id.id_delay_car_24);
        delay_car[24] =  view.findViewById(R.id.id_delay_car_25);
        delay_car[25] =  view.findViewById(R.id.id_delay_car_26);
        delay_car[26] =  view.findViewById(R.id.id_delay_car_27);
        delay_car[27] =  view.findViewById(R.id.id_delay_car_28);
        delay_car[28] =  view.findViewById(R.id.id_delay_car_29);
        delay_car[29] =  view.findViewById(R.id.id_delay_car_30);
        delay_car[30] =  view.findViewById(R.id.id_delay_car_31);


        delay_car[31] =  view.findViewById(R.id.id_delay_car_1_2);
        delay_car[32] =  view.findViewById(R.id.id_delay_car_2_2);
        delay_car[33] =  view.findViewById(R.id.id_delay_car_3_2);
        delay_car[34] =  view.findViewById(R.id.id_delay_car_4_2);
        delay_car[35] =  view.findViewById(R.id.id_delay_car_5_2);
        delay_car[36] =  view.findViewById(R.id.id_delay_car_6_2);
        delay_car[37] =  view.findViewById(R.id.id_delay_car_7_2);
        delay_car[38] =  view.findViewById(R.id.id_delay_car_8_2);
        delay_car[39] =  view.findViewById(R.id.id_delay_car_9_2);
        delay_car[40] =  view.findViewById(R.id.id_delay_car_10_2);
        delay_car[41] =  view.findViewById(R.id.id_delay_car_11_2);
        delay_car[42] =  view.findViewById(R.id.id_delay_car_12_2);
        delay_car[43] =  view.findViewById(R.id.id_delay_car_13_2);
        delay_car[44] =  view.findViewById(R.id.id_delay_car_14_2);
        delay_car[45] =  view.findViewById(R.id.id_delay_car_15_2);
        delay_car[46] =  view.findViewById(R.id.id_delay_car_16_2);
        delay_car[47] =  view.findViewById(R.id.id_delay_car_17_2);
        delay_car[48] =  view.findViewById(R.id.id_delay_car_18_2);
        delay_car[49] =  view.findViewById(R.id.id_delay_car_19_2);
        delay_car[50] =  view.findViewById(R.id.id_delay_car_20_2);
        delay_car[51] =  view.findViewById(R.id.id_delay_car_21_2);
        delay_car[52] =  view.findViewById(R.id.id_delay_car_22_2);
        delay_car[53] =  view.findViewById(R.id.id_delay_car_23_2);
        delay_car[54] =  view.findViewById(R.id.id_delay_car_24_2);
        delay_car[55] =  view.findViewById(R.id.id_delay_car_25_2);
        delay_car[56] =  view.findViewById(R.id.id_delay_car_26_2);
        delay_car[57] =  view.findViewById(R.id.id_delay_car_27_2);
        delay_car[58] =  view.findViewById(R.id.id_delay_car_28_2);
        delay_car[59] =  view.findViewById(R.id.id_delay_car_29_2);
        delay_car[60] =  view.findViewById(R.id.id_delay_car_30_2);
        delay_car[61] =  view.findViewById(R.id.id_delay_car_31_2);

        txt_car[0] =  view.findViewById(R.id.id_txt_output_delay_1);
        txt_car[1] =  view.findViewById(R.id.id_txt_output_delay_2);
        txt_car[2] =  view.findViewById(R.id.id_txt_output_delay_3);
        txt_car[3] =  view.findViewById(R.id.id_txt_output_delay_4);
        txt_car[4] =  view.findViewById(R.id.id_txt_output_delay_5);
        txt_car[5] =  view.findViewById(R.id.id_txt_output_delay_6);
        txt_car[6] =  view.findViewById(R.id.id_txt_output_delay_7);
        txt_car[7] =  view.findViewById(R.id.id_txt_output_delay_8);
        txt_car[8] =  view.findViewById(R.id.id_txt_output_delay_9);
        txt_car[9] =  view.findViewById(R.id.id_txt_output_delay_10);
        txt_car[10] =  view.findViewById(R.id.id_txt_output_delay_11);
        txt_car[11] =  view.findViewById(R.id.id_txt_output_delay_12);
        txt_car[12] =  view.findViewById(R.id.id_txt_output_delay_13);
        txt_car[13] =  view.findViewById(R.id.id_txt_output_delay_14);
        txt_car[14] =  view.findViewById(R.id.id_txt_output_delay_15);
        txt_car[15] =  view.findViewById(R.id.id_txt_output_delay_16);
        txt_car[16] =  view.findViewById(R.id.id_txt_output_delay_17);
        txt_car[17] =  view.findViewById(R.id.id_txt_output_delay_18);
        txt_car[18] =  view.findViewById(R.id.id_txt_output_delay_19);
        txt_car[19] =  view.findViewById(R.id.id_txt_output_delay_20);
        txt_car[20] =  view.findViewById(R.id.id_txt_output_delay_21);
        txt_car[21] =  view.findViewById(R.id.id_txt_output_delay_22);
        txt_car[22] =  view.findViewById(R.id.id_txt_output_delay_23);
        txt_car[23] =  view.findViewById(R.id.id_txt_output_delay_24);
        txt_car[24] =  view.findViewById(R.id.id_txt_output_delay_25);
        txt_car[25] =  view.findViewById(R.id.id_txt_output_delay_26);
        txt_car[26] = view.findViewById(R.id.id_txt_output_delay_27);
        txt_car[27] = view.findViewById(R.id.id_txt_output_delay_28);
        txt_car[28] = view.findViewById(R.id.id_txt_output_delay_29);
        txt_car[29] = view.findViewById(R.id.id_txt_output_delay_30);
        txt_car[30] = view.findViewById(R.id.id_txt_output_delay_31);


        txt_car[31] = view.findViewById(R.id.id_txt_output_delay_1_2);
        txt_car[32] = view.findViewById(R.id.id_txt_output_delay_2_2);
        txt_car[33] = view.findViewById(R.id.id_txt_output_delay_3_2);
        txt_car[34] = view.findViewById(R.id.id_txt_output_delay_4_2);
        txt_car[35] = view.findViewById(R.id.id_txt_output_delay_5_2);
        txt_car[36] = view.findViewById(R.id.id_txt_output_delay_6_2);
        txt_car[37] = view.findViewById(R.id.id_txt_output_delay_7_2);
        txt_car[38] = view.findViewById(R.id.id_txt_output_delay_8_2);
        txt_car[39] = view.findViewById(R.id.id_txt_output_delay_9_2);
        txt_car[40] = view.findViewById(R.id.id_txt_output_delay_10_2);
        txt_car[41] = view.findViewById(R.id.id_txt_output_delay_11_2);
        txt_car[42] = view.findViewById(R.id.id_txt_output_delay_12_2);
        txt_car[43] = view.findViewById(R.id.id_txt_output_delay_13_2);
        txt_car[44] = view.findViewById(R.id.id_txt_output_delay_14_2);
        txt_car[45] = view.findViewById(R.id.id_txt_output_delay_15_2);
        txt_car[46] = view.findViewById(R.id.id_txt_output_delay_16_2);
        txt_car[47] = view.findViewById(R.id.id_txt_output_delay_17_2);
        txt_car[48] = view.findViewById(R.id.id_txt_output_delay_18_2);
        txt_car[49] = view.findViewById(R.id.id_txt_output_delay_19_2);
        txt_car[50] = view.findViewById(R.id.id_txt_output_delay_20_2);
        txt_car[51] = view.findViewById(R.id.id_txt_output_delay_21_2);
        txt_car[52] = view.findViewById(R.id.id_txt_output_delay_22_2);
        txt_car[53] = view.findViewById(R.id.id_txt_output_delay_23_2);
        txt_car[54] = view.findViewById(R.id.id_txt_output_delay_24_2);
        txt_car[55] = view.findViewById(R.id.id_txt_output_delay_25_2);
        txt_car[56] = view.findViewById(R.id.id_txt_output_delay_26_2);
        txt_car[57] = view.findViewById(R.id.id_txt_output_delay_27_2);
        txt_car[58] = view.findViewById(R.id.id_txt_output_delay_28_2);

        txt_car[59] = view.findViewById(R.id.id_txt_output_delay_29_2);
        txt_car[60] = view.findViewById(R.id.id_txt_output_delay_30_2);
        txt_car[61] = view.findViewById(R.id.id_txt_output_delay_31_2);
        btn_delay_unit = view.findViewById(R.id.id_b_ms1);
        ly_delay_sub = view.findViewById(R.id.id_llyout_delay_speaker_superlow);

        ly_delay_group=view.findViewById(R.id.ly_delay_group);

       btn_sound= view.findViewById(R.id.id_btn_sound);

    }

    private void initClick() {


        btn_delay_unit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Temp.delay_unit == 2) {
                    Temp.delay_unit = 1;
                } else if (Temp.delay_unit == 1) {
                    Temp.delay_unit = 3;
                } else if (Temp.delay_unit == 3) {
                    Temp.delay_unit = 2;
                }
                setBtnDelayUnit(Temp.delay_unit);
                setDelayVal();
            }
        });
        encryption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEncryptionDialog();
            }
        });


        ly_delay_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelayGroup();
            }
        });
        btn_sound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelayGroup();
            }
        });


        for (int i = 0; i < delay_car.length; i++) {
            delay_car[i].setTag(i);
            delay_car[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    int OutputChannelSel = setCurrent(index);
                    if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute == 1) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 0;
                    } else {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 1;
                    }
                    DataOptUtil.syncLinkData(Define.UI_OutMute);
                    //DataOptUtil.flashLinkDataUI(Define.UI_OutMute, OutputChannelSel);
                    // ReturnNum.flashLinkDataUI(Define.UI_OutMute);
                    flashDelayCarLogo();
                    // System.out.println("BUG 当前的通道值为"+MacCfg.OutputChannelSel);
                }

            });
            txt_car[i].setTag(i);
            txt_car[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    setDelay_car(index);
                }
            });

        }
    }

    private void showDelayGroup(){
        Delay_groupDialogFragment otcDialog = new Delay_groupDialogFragment();
        otcDialog.show(getActivity().getFragmentManager(), "otcDialog");
        otcDialog.OnSetOnClickDialogListener(new Delay_groupDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
//                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_level =
//                                DataStruct.CurMacMode.XOver.Level.member1[type];
//                        System.out.println("BUG     加密的值为" + DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_level);
//                        DataOptUtil.syncLinkData(Define.UI_HOct);
                //  ReturnNum.flashOutModeLinkDataUI(Define.UI_HOct);
                setSoundText();
            }
        });
    }

    private void setSoundText(){

        switch ( MacCfg.Delay_Type){
            case 0:
                btn_sound.setText(String.valueOf(getResources().getString(R.string.F_Sound)));
                break;
            case 1:
                btn_sound.setText(String.valueOf(getResources().getString(R.string.R_Sound)));
                break;
            case 2:
                btn_sound.setText(String.valueOf(getResources().getString(R.string.A_Sound)));
                break;
            case 3:
                btn_sound.setText(String.valueOf(getResources().getString(R.string.None_Sound)));
                break;
            case 4:
                btn_sound.setText(String.valueOf(getResources().getString(R.string.S_Sound)));
                break;
        }
    }

    //加密
    private void showEncryptionDialog() {
        if (DataStruct.isConnecting) {
            if (setEncryptionDialogFragment == null) {
                setEncryptionDialogFragment = new SetEncryptionDialogFragment();
            }

            setEncryptionDialogFragment.show(getActivity().getFragmentManager(), "setEncryptionDialogFragment");
            setEncryptionDialogFragment.OnSetEncryptionDialogFragmentChangeListener(new SetEncryptionDialogFragment.OnEncryptionDialogFragmentClickListener() {
                @Override
                public void onEncryptionClickListener(
                        boolean Encryptionflag, boolean recallFlag) {
                    if (!recallFlag) {
                        if (Encryptionflag) {//加密处理
                            // DataOptUtil.SaveGroupData(0);
                        } else {//解密处理
                            // DataOptUtil.SaveGroupData(0);
                        }
//                        showLoadingDialog();
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
//
//                        setEncryptionCleanDialogFragment.show(getActivity().getFragmentManager(), "setEncryptionCleanDialogFragment");
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

    private void showLoadingDialog(){
        if(mLoadingDialogFragment == null){
            mLoadingDialogFragment = new LoadingDialogFragment();
        }
        if (!mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.show(getActivity().getFragmentManager(), "mLoadingDialogFragment");
        }

    }


    /**
     * 消息提示
     */
    @SuppressWarnings("unused")
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }



    /**
     * 通过该值获取到当前的Key(既通道值)
     */
    private int setCurrent(int index) {
        int num = (int) index + 1;
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            Integer value = map.get(key);
            System.out.println("BUG 点击之后的值为key="+key+"value="+value);
            if (value == num) {
                return key;
            } else if (num > 31) {
                if (value == index) {
                    return key;
                }
            }
        }
        return 0;
    }


    /**
     * 设置延时需要显示的图标
     */
    private void setDelayType(int index, int OutChannel) {
        //System.out.println("BUG 延时的值为"+index+"通道类型"+OutChannel);
        for (int j = 0; j < Left_All.length; j++) {
            if (Left_All[j] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_left_all_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_left_all_selector);
                }

            }
        }
        for (int j = 0; j < Right_All.length; j++) {
            if (Right_All[j] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_right_all_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_right_all_selector);
                }
            }
        }
        for (int i = 0; i < Left_high.length; i++) {
            if (Left_high[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_left_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_left_selector);
                }
            }
        }
        for (int i = 0; i < Right_high.length; i++) {
            if (Right_high[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_right_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_right_selector);
                }
            }
        }
        for (int i = 0; i < center_high.length; i++) {
            if (center_high[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_center_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_center_selector);
                }
            }
        }
        for (int i = 0; i < center_All.length; i++) {
            if (center_All[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_center_all_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_center_all_selector);
                }
            }
        }
        for (int i = 0; i < Low_All.length; i++) {
            if (Low_All[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_low_all_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_low_all_selector);
                }
            }
        }
        for (int i = 0; i < L_Low_All.length; i++) {
            if (L_Low_All[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_low_all_left_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_low_all_left_selector);
                }
            }
        }
        for (int i = 0; i < R_Low_All.length; i++) {
            if (R_Low_All[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_low_all_right_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_low_all_right_selector);
                }
            }
        }

        for (int i = 0; i < R_center_high.length; i++) {
            if (R_center_high[i] == index) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 0) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_right_center_all_mute_selector);
                } else if (DataStruct.RcvDeviceData.OUT_CH[OutChannel].mute == 1) {
                    delay_car[index].setBackgroundResource(R.drawable.chs_speaker_high_right_center_all_right_selector);
                }
            }
        }




        if (OutChannel == MacCfg.OutputChannelSel) {
            delay_car[index].setSelected(true);
        } else {
            delay_car[index].setSelected(false);
        }


    }

    /**
     * 设置当前的通道为静音
     */
    private void setDelay_press() {

    }

    /**
     * 设置点击事件 看那个按钮高亮显示
     */
    private void setDelay_car(int index) {
        int num = (int) index + 1;
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            Integer value = map.get(key);
          //  System.out.println("BUG 通道为"+key+"值为"+value);
            if (value == num) {
                MacCfg.OutputChannelSel = key;
                Temp.OutChannenlTemp = key;
                break;
            } else if (num > 28) {
                if (value == index) {
                    MacCfg.OutputChannelSel = key;
                    Temp.OutChannenlTemp = key;
                    break;
                }
            }
        }
        showOutputDelayDialog();
        boolean bSelected = true;
        for (int j = 0; j < delay_car.length; j++) {
            if (j == (int) index) {
                bSelected = true;
                delay_car[(int) index].setSelected(bSelected);
            } else {
                bSelected = false;
                delay_car[j].setSelected(bSelected);
            }
        }
    }

    /**
     * 输出延时的弹窗
     */
    private void showOutputDelayDialog() {
        DataOptUtil.SaveDelay();

        int max = DataStruct.CurMacMode.Delay.MAX;
        final Bundle val = new Bundle();
        val.putInt(SetDelayDialogFragment.ST_Data,
                DataStruct.RcvDeviceData.OUT_CH[Temp.OutChannenlTemp].delay);
        val.putInt(SetDelayDialogFragment.ST_DelayUnit, Temp.delay_unit);

        if (setDelayDialogFragment == null) {
            setDelayDialogFragment = new SetDelayDialogFragment();
        }
        if (!setDelayDialogFragment.isAdded()) {
            setDelayDialogFragment.setArguments(val);
            setDelayDialogFragment.show(getActivity().getFragmentManager(), "setDelayDialogFragment");
        }

        setDelayDialogFragment.OnSetDelayDialogFragmentChangeListener(new SetDelayDialogFragment.OnDelayDialogFragmentChangeListener() {
            @Override
            public void onDelaySeekBarListener(int Val, int type, boolean flag) {
                DataStruct.RcvDeviceData.OUT_CH[Temp.OutChannenlTemp].delay = Val;
                DataOptUtil.setOtherDelay(Val);

               // DataOptUtil.syncLinkData(Define.UI_Delay);
                setDelayVal();
                DataOptUtil.SaveDelay();
            }



        });
    }




    public void FlashPageUI() {
        if(MacCfg.bool_Encryption){
            encryption.setVisibility(View.VISIBLE);
            for(int i=0;i<txt_car.length;i++){
                txt_car[i].setText(DataOptUtil.CountDelayMs(0));
            }
        }else {
            encryption.setVisibility(View.GONE);
            setDelay_car_Gone();
            setBtnDelayUnit(Temp.delay_unit);
            int num =DataOptUtil.GetChannelNum(MacCfg.OutputChannelSel);
            Iterator<Integer> iter = map.keySet().iterator();
            flashDelayCarLogo();
        }
        setSoundText();


        //  }


    }


    /**
     * 刷新车界面的图标
     */
    private void flashDelayCarLogo() {
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            Integer value = map.get(key);
            if (value > 0) {
                value -= 1;
            }
            setDelayType(value, key);
        }
    }

    /**
     * 设置延时单位的值
     */
    private void setBtnDelayUnit(int delay_unit) {
        switch (delay_unit) {

            case 1:
                btn_delay_unit.setText(String.valueOf(getResources().getString(R.string.CM)));
                break;
            case 2:
                btn_delay_unit.setText(String.valueOf((getResources().getString(R.string.MS))));
                break;
            case 3:
                btn_delay_unit.setText(String.valueOf(getResources().getString(R.string.Inch)));
                break;
            default:
                btn_delay_unit.setText(String.valueOf(getResources().getString(R.string.MS)));
                break;
        }
    }

    /**
     * 设置需要显示哪一些通道
     */
    private void setDelay_car_Gone() {
        aList.clear();
        newAlist.clear();
        countAlist.clear();
        map.clear();

        for (int i = 0; i < delay_car.length; i++) {
          delay_car[i].setVisibility(View.INVISIBLE);
          txt_car[i].setVisibility(View.INVISIBLE);
        }
        ly_delay_sub.setVisibility(View.GONE);

        for (int i = 0; i <DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            if (MacCfg.ChannelNumBuf[i] != 0) {
                for (int j =DataStruct.CurMacMode.Out.OUT_CH_MAX_USE - 1; j > i; j--) {
                    if (MacCfg.ChannelNumBuf[i] == MacCfg.ChannelNumBuf[j]) {
                        System.out.println("BUG 当前的值为"+MacCfg.ChannelNumBuf[i] );
                        countAlist.add(MacCfg.ChannelNumBuf[i]);
                    }
                }
            }
        }


        for (int i = 0; i < countAlist.size(); i++) {

            delay_car[countAlist.get(i) +30].setVisibility(View.VISIBLE);
            txt_car[countAlist.get(i) + 30].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            if (MacCfg.ChannelNumBuf[i] != 0) {
                if (aList.contains(MacCfg.ChannelNumBuf[i])) {
                    aList.add(MacCfg.ChannelNumBuf[i] +31);
                } else {
                    aList.add(MacCfg.ChannelNumBuf[i]);
                }
                newAlist.add(i);
            }
        }

        for (int i = 0; i < newAlist.size(); i++) {

            map.put(newAlist.get(i), aList.get(i));
        }


        /**表示获取到的value值隐藏*/
        for (int i = 0; i < aList.size(); i++) {
            delay_car[aList.get(i) - 1].setVisibility(View.VISIBLE);
            txt_car[aList.get(i) - 1].setVisibility(View.VISIBLE);
        }
        setDelayVal();
    }

    /**
     * 设置需要显示的延时值
     */
    private void setDelayVal() {
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            Integer value = map.get(key);

            if (Temp.delay_unit == 2) {
                txt_car[value - 1].setText(DataOptUtil.CountDelayMs(DataStruct.RcvDeviceData.OUT_CH[key].delay));
            } else if (Temp.delay_unit == 1) {
                txt_car[value - 1].setText(DataOptUtil.CountDelayCM(DataStruct.RcvDeviceData.OUT_CH[key].delay));
            } else if (Temp.delay_unit == 3) {
                txt_car[value - 1].setText(DataOptUtil.CountDelayInch(DataStruct.RcvDeviceData.OUT_CH[key].delay));
            }
        }
    }


}