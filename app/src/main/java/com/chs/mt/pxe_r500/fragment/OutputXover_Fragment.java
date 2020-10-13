package com.chs.mt.pxe_r500.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertNoLinkDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertResetOutSPKDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.FilterDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LinkDataCoypLeftRight_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.OtcDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionCleanDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetFreqDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetOutputValFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.WarningDialogIFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.filter_selectDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.oct_selectDialogFragment;
import com.chs.mt.pxe_r500.main.Output_or_InputSpkTypeActivity;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.tools.EQ_SeekBar;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;
import com.chs.mt.pxe_r500.tools.wheel.WheelView;
import com.chs.mt.pxe_r500.util.CenterShowHorizontalScrollView;
import com.chs.mt.pxe_r500.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import static com.chs.mt.pxe_r500.datastruct.MacCfg.OutputChannelSel;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutputXover_Fragment extends Fragment {
    private Toast mToast;
    private static Context mContext;
    private int OUT_CH_MAX_CFG = 6;
    private Button encryption;
    //Output
    private LinearLayout[] LY_Output = new LinearLayout[OUT_CH_MAX_CFG];/*输出通道*/
    private Button[] BtnPolar = new Button[OUT_CH_MAX_CFG];/*输出极性*/
    private Button[] BtnMute = new Button[OUT_CH_MAX_CFG];/*输出静音*/
    private Button[] BtnValInc = new Button[OUT_CH_MAX_CFG];/*输出音量按键+*/
    private Button[] BtnValSub = new Button[OUT_CH_MAX_CFG];/*输出音量按键-*/
    private Button[] BtnVal = new Button[OUT_CH_MAX_CFG];/*输出音量显示*/
    private Button[] BtnIndex = new Button[8];


    private TextView[] TV_CHName = new TextView[OUT_CH_MAX_CFG];
    private EQ_SeekBar[] SB_Output_SeekBar = new EQ_SeekBar[OUT_CH_MAX_CFG];//延时滑动调节
    private Button B_CM1, B_MS1, B_Inch1;    //延时单位设置
    private ImageView high_oct_off, low_oct_off;
    //Xover
    private Button HP_Filter, HP_Level, HP_Freq;
    private Button LP_Filter, LP_Level, LP_Freq;
    private boolean BOOL_HLP = true;//true,HP
    private AlertResetOutSPKDialogFragment setResetOutSPKDialogFragment;
    private AlertDialogFragment alertDialogFragment;

    private WarningDialogIFragment warningDialogIFragment;

    private TextView txt_cancel;

    private final static int MAXNUM = 5;  //通道个数选择
    private PopupWindow popupWindow;
    private int popupHeight, popupWidth;
    private TextView[] textViews = new TextView[MAXNUM];

    //对话框
    private SetEncryptionDialogFragment setEncryptionDialogFragment = null;
    private SetEncryptionCleanDialogFragment setEncryptionCleanDialogFragment = null;
    private FilterDialogFragment filterDialog = null;
    private OtcDialogFragment otcDialog = null;
    private SetFreqDialogFragment setFreqDialogFragment = null;
    private SetOutputValFragment setValDialogFragment = null;

    private Output_or_InputSpkTypeActivity output_or_inputSpkTypeDialog = null;

    private LoadingDialogFragment mLoadingDialogFragment = null;

    private LinkDataCoypLeftRight_DialogFragment mLinkDataCoypLeftRightDialogFragment = null;
    //////////////////////// Start  ////////////////////////////
    private CenterShowHorizontalScrollView centerShowHorizontalScrollView;
    //通道选择
    private WheelView WV_OutVa;
    private List<String> Channellists;
    private ImageView img_mute;
    private final int middlePadding = 10;
    private Button B_OutVaSetName, B_Outputvolome, B_polar, B_reset, B_lock, B_link;
    private MHS_SeekBar mhs_seekBar;
    private LinearLayout ly_reset, ly_link, ly_lock;
    private ImageView img_link;
    private ImageView img_type, img_hp_filter, img_lp_filter, img_hp_oct, img_lp_oct;
    private LongCickButton btn_inc, btn_sub;
    private TextView txt_volume;

    public OutputXover_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chs_fragment_output_xover, container, false);
        mContext = getActivity().getApplicationContext();
        initView(view);
        initClick();
        setTopView();
        FlashPageUI();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        // centerShowHorizontalScrollView=view.findViewById(R.id.id_center_hor);
        encryption = view.findViewById(R.id.id_b_encryption);
        Channellists = new ArrayList<String>();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }

        WV_OutVa = (WheelView) view.findViewById(R.id.id_output_va_wheelview);
        WV_OutVa.select(OutputChannelSel);
        WV_OutVa.lists(Channellists).showCount(5).selectTip("").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index, boolean fromUser) {
                if (!fromUser) {
                    OutputChannelSel = index;
                    FlashPageUI();
                }
            }
        }).build();

        B_OutVaSetName = view.findViewById(R.id.id_b_output_name_ch);
        img_type = view.findViewById(R.id.id_lp_img_type);
        high_oct_off = view.findViewById(R.id.id_b_hp_freq_reset);
        low_oct_off = view.findViewById(R.id.id_b_lp_freq_reset);
        HP_Filter = view.findViewById(R.id.id_b_hp_filter_va);
        img_hp_filter = view.findViewById(R.id.id_img_xiala);
        img_lp_filter = view.findViewById(R.id.id_lp_img_xiala);
        LP_Filter = view.findViewById(R.id.id_b_lp_filter_va);
        HP_Freq = view.findViewById(R.id.id_b_hp_freq_va);
        LP_Freq = view.findViewById(R.id.id_b_lp_freq_va);
        HP_Level = view.findViewById(R.id.id_b_hp_otc_va);
        img_hp_oct = view.findViewById(R.id.id_hp_img_oct);
        img_lp_oct = view.findViewById(R.id.id_lp_img_oct);
        LP_Level = view.findViewById(R.id.id_b_lp_otc_va);

        mhs_seekBar = view.findViewById(R.id.id_sb_13);
        B_Outputvolome = view.findViewById(R.id.id_b_output_val_va);
        txt_volume = view.findViewById(R.id.id_txt_volume);
        img_mute = view.findViewById(R.id.id_ly_mute);

        B_polar = view.findViewById(R.id.id_b_output_polar);
        img_link = view.findViewById(R.id.id_ly_link);

        B_reset = view.findViewById(R.id.id_d_channel_reset_va);
        B_lock = view.findViewById(R.id.id_d_channel_lock_va);
        B_link = view.findViewById(R.id.id_d_channel_link_va);

        ly_reset = view.findViewById(R.id.ly_channel_reset_va);
        ly_lock = view.findViewById(R.id.ly_channel_lock_va);
        ly_link = view.findViewById(R.id.ly_channel_link_va);
        btn_inc = view.findViewById(R.id.id_button_val_inc);
        btn_sub = view.findViewById(R.id.id_button_val_sub);

//        BtnIndex[0]=view.findViewById(R.id.id_btn_ch1);
//        BtnIndex[1]=view.findViewById(R.id.id_btn_ch2);
//        BtnIndex[2]=view.findViewById(R.id.id_btn_ch3);
//        BtnIndex[3]=view.findViewById(R.id.id_btn_ch4);
//        BtnIndex[4]=view.findViewById(R.id.id_btn_ch5);
//        BtnIndex[5]=view.findViewById(R.id.id_btn_ch6);
//        BtnIndex[6]=view.findViewById(R.id.id_btn_ch7);
//        BtnIndex[7]=view.findViewById(R.id.id_btn_ch8);
        // BtnIndex[8]=view.findViewById(R.id.id_btn_ch1);
    }

    private void setTopView() {
        // centerShowHorizontalScrollView.getLinear().removeAllViews();

//        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//            final View titleItem = View.inflate(getActivity(), R.layout.work_time_title_item, null);
//            centerShowHorizontalScrollView.addItemView(titleItem, i);
//
//            LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) titleItem.getLayoutParams();
//            titleParams.leftMargin = middlePadding / 2;
//            titleParams.rightMargin = middlePadding / 2;
//
//           final TextView textView= (TextView) titleItem.findViewById(R.id.text);
//            textView.setText(DataStruct.CurMacMode.Out.Name[i]);
//            titleItem.setTag(i);
//            titleItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    OutputChannelSel=(int)v.getTag();
//                    FlashPageUI();
//
//
//                }
//            });
//        }
    }


    private void OutputChannelSelect() {
//
//        for (int i = 0; i < BtnIndex.length; i++) {
//            BtnIndex[i].setBackgroundResource(R.drawable.btn_output_name);
//        }
//        BtnIndex[MacCfg.OutputChannelCurrent].setBackgroundResource(R.drawable.btn_output_name_press);
    }

    private void initClick() {
//        for (int i = 0; i < BtnIndex.length; i++) {
//            BtnIndex[i].setTag(i);
//            BtnIndex[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //int
//                    MacCfg.OutputChannelCurrent=(int)view.getTag();
//                    OutputChannelSelect();;
//                }
//            });
//        }


        btn_inc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                OutIN_ValInc();
            }
        });
        btn_inc.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                btn_inc.setStart();
                return false;
            }
        });
        btn_inc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                OutIN_ValInc();
            }
        }, MacCfg.LongClickEventTimeMax);

        //音量减
        btn_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                OutIN_ValSub();
            }
        });
        btn_sub.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                btn_sub.setStart();
                return false;
            }
        });
        btn_sub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                OutIN_ValSub();
            }
        }, MacCfg.LongClickEventTimeMax);


        HP_Level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(oct_selectDialogFragment.ST_DataOPT, oct_selectDialogFragment.DataOPT_HP);
                bundle.putInt(oct_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_level);

                oct_selectDialogFragment otcDialog = new oct_selectDialogFragment();
                otcDialog.setArguments(bundle);
                otcDialog.show(getActivity().getFragmentManager(), "otcDialog");
                otcDialog.OnSetOnClickDialogListener(new oct_selectDialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_level =
                                DataStruct.CurMacMode.XOver.Level.member1[type];
                        System.out.println("BUG     加密的值为" + DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_level);
                        HP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[type]);
                        DataOptUtil.syncLinkData(Define.UI_HOct);
                        flashXover();
                        //  ReturnNum.flashOutModeLinkDataUI(Define.UI_HOct);
                    }
                });
            }
        });


        encryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEncryptionDialog();
            }
        });
        B_OutVaSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetOutSpkNameDialog();
                // showSpkdialog();
            }
        });


        B_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                    if (DataOptUtil.GetChannelNum(i) != 0) {
                        flag = true;
                        break;
                    }
                }
                System.out.println("BUG 值为=============" + flag);
                if (flag == false) {
                    showWaringDialog();
                } else {
                    showLockDialog();
                }
            }
        });

        ly_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                    if (DataOptUtil.GetChannelNum(i) != 0) {
                        flag = true;
                        break;
                    }
                }

                //System.out.println("BUG 值为" + flag);
                if (flag == false) {
                    showWaringDialog();
                } else {
                    showLockDialog();
                }


            }
        });
        B_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _LINKMODE_SPKTYPE_Dialog();
            }
        });
        ly_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _LINKMODE_SPKTYPE_Dialog();
            }
        });

        B_polar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].polar == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].polar = 1;
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].polar = 0;
                }
                DataOptUtil.syncLinkData(Define.UI_OutPolar);
                flashPolar();
            }
        });
        B_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog();
            }
        });
        ly_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog();
            }
        });

        img_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 1;
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 0;
                }
                flashMute();
                DataOptUtil.syncLinkData(Define.UI_OutMute);
            }
        });


        mhs_seekBar.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                //  if(useList(Define.SUP_Type,DataOptUtil.GetChannelNum(OutputChannelSel))){
                DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain = progress * (DataStruct.CurMacMode.Out.StepOutVol);
                //}
                if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain != 0) {
                    DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 1;
                }
                flashMute();
                DataOptUtil.syncLinkData(Define.UI_OutMute);

                if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
                    B_Outputvolome.setText(String.valueOf((progress / 4)));
                } else {

                    B_Outputvolome.setText(String.valueOf(progress - 60));
                }


                DataOptUtil.syncLinkData(Define.UI_OutVal);
            }
        });


        high_oct_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MacCfg.H_output_Freq_temp[OutputChannelSel] == 20 && DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq == 20) {
                } else {
                    if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq == 20) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq = MacCfg.H_output_Freq_temp[OutputChannelSel];
                    } else {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq = 20;
                    }
                }
                setOctOnOrOff();
                DataOptUtil.syncLinkData(Define.UI_HFreq);
            }
        });
        low_oct_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MacCfg.L_output_Freq_temp[OutputChannelSel] == 20000
                        && DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq == 20000) {

                } else {
                    if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq == 20000) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq = MacCfg.L_output_Freq_temp[OutputChannelSel];
                    } else {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq = 20000;
                    }
                }
                setOctOnOrOff();
                DataOptUtil.syncLinkData(Define.UI_LFreq);

            }
        });


        LP_Level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(oct_selectDialogFragment.ST_DataOPT, oct_selectDialogFragment.DataOPT_LP);
                bundle.putInt(oct_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_level);

                oct_selectDialogFragment otcDialog = new oct_selectDialogFragment();
                otcDialog.setArguments(bundle);
                otcDialog.show(getActivity().getFragmentManager(), "otcDialog");
                otcDialog.OnSetOnClickDialogListener(new oct_selectDialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_level =
                                DataStruct.CurMacMode.XOver.Level.member1[type];
                        LP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[type]);
                        DataOptUtil.syncLinkData(Define.UI_LOct);
                        flashXover();
                        //  ReturnNum.flashOutModeLinkDataUI(Define.UI_HOct);
                    }
                });
            }
        });


        HP_Freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle Freq = new Bundle();
                Freq.putInt(SetFreqDialogFragment.ST_Freq, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq);
                Freq.putBoolean(SetFreqDialogFragment.ST_BOOL_HP, true);
                Freq.putInt(SetFreqDialogFragment.ST_LP_Freq, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq);
                Freq.putInt(SetFreqDialogFragment.ST_HP_Freq, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq);

                SetFreqDialogFragment setFreqDialogFragment = new SetFreqDialogFragment();
                setFreqDialogFragment.setArguments(Freq);
                setFreqDialogFragment.show(getActivity().getFragmentManager(), "setFreqDialogFragment");
                setFreqDialogFragment.OnSetFreqDialogFragmentChangeListener(new SetFreqDialogFragment.OnFreqDialogFragmentChangeListener() {

                    @Override
                    public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                        // TODO Auto-generated method stub
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq = Freq;

                        MacCfg.H_output_Freq_temp[OutputChannelSel] = DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq;

                        HP_Freq.setText(Integer.toString(DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq) + "Hz");
                        setOctOnOrOff();
                        DataOptUtil.syncLinkData(Define.UI_HFreq);
                    }
                });
            }
        });

        LP_Freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle Freq = new Bundle();
                Freq.putInt(SetFreqDialogFragment.ST_Freq, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq);
                Freq.putBoolean(SetFreqDialogFragment.ST_BOOL_HP, false);
                Freq.putInt(SetFreqDialogFragment.ST_HP_Freq, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq);
                Freq.putInt(SetFreqDialogFragment.ST_LP_Freq, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq);

                SetFreqDialogFragment setFreqDialogFragment = new SetFreqDialogFragment();
                setFreqDialogFragment.setArguments(Freq);
                setFreqDialogFragment.show(getActivity().getFragmentManager(), "setFreqDialogFragment");
                setFreqDialogFragment.OnSetFreqDialogFragmentChangeListener(new SetFreqDialogFragment.OnFreqDialogFragmentChangeListener() {

                    @Override
                    public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                        // TODO Auto-generated method stub
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq = Freq;
                        MacCfg.L_output_Freq_temp[OutputChannelSel] = DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq;
                        LP_Freq.setText(Integer.toString(DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq) + "Hz");
                        setOctOnOrOff();
                        DataOptUtil.syncLinkData(Define.UI_LFreq);
                    }
                });
            }
        });


        LP_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(filter_selectDialogFragment.ST_DataOPT, filter_selectDialogFragment.DataOPT_LP);
                bundle.putInt(filter_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_filter);
                filter_selectDialogFragment filterDialog = new filter_selectDialogFragment();
                filterDialog.setArguments(bundle);
                filterDialog.show(getActivity().getFragmentManager(), "filterDialog");
                filterDialog.OnSetOnClickDialogListener(new filter_selectDialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_filter =
                                DataStruct.CurMacMode.XOver.Fiter.member1[type];

                        LP_Filter.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[type]);
                        // ReturnNum.flashOutModeLinkDataUI(Define.UI_LFilter);
                        DataOptUtil.syncLinkData(Define.UI_LFilter);

                    }
                });
            }
        });


        HP_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(filter_selectDialogFragment.ST_DataOPT, filter_selectDialogFragment.DataOPT_HP);
                bundle.putInt(filter_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_filter);
                filter_selectDialogFragment filterDialog = new filter_selectDialogFragment();
                filterDialog.setArguments(bundle);
                filterDialog.show(getActivity().getFragmentManager(), "filterDialog");
                filterDialog.OnSetOnClickDialogListener(new filter_selectDialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_filter =
                                DataStruct.CurMacMode.XOver.Fiter.member1[type];
                        HP_Filter.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[type]);
                        // ReturnNum.flashOutModeLinkDataUI(Define.UI_LFilter);
                        DataOptUtil.syncLinkData(Define.UI_HFilter);

                    }
                });
            }
        });

    }


    void OutIN_ValSub() {
        int val = 0;

        val = DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol;
        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            val = (val / 4);
            if (--val < 0) {
                val = 0;
            }
            DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain = (val * 4) * DataStruct.CurMacMode.Out.StepOutVol;

        } else {
            if (--val < 0) {
                val = 0;
            }
            DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain = (val) * DataStruct.CurMacMode.Out.StepOutVol;
        }


        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain != 0) {
            DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 1;
        }
        flashMute();
        DataOptUtil.syncLinkData(Define.UI_OutMute);

        mhs_seekBar.setProgress((DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / (DataStruct.CurMacMode.Out.StepOutVol)));
        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            B_Outputvolome.setText(String.valueOf(val));
        } else {
            B_Outputvolome.setText(String.valueOf(val - 60));
        }

        DataOptUtil.syncLinkData(Define.UI_OutVal);
    }


    void OutIN_ValInc() {
        int val = 0;

        val = DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol;
        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            val = (val / 4);
            if (++val > 15) {
                val = 15;
            }
            DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain = (val * 4) * DataStruct.CurMacMode.Out.StepOutVol;
        } else {
            if (++val > (DataStruct.CurMacMode.Out.MaxOutVol / DataStruct.CurMacMode.Out.StepOutVol)) {
                val = (DataStruct.CurMacMode.Out.MaxOutVol / DataStruct.CurMacMode.Out.StepOutVol);
            }
            DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain = val * DataStruct.CurMacMode.Out.StepOutVol;
        }


        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain != 0) {
            DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute = 1;
        }
        flashMute();
        DataOptUtil.syncLinkData(Define.UI_OutMute);

        mhs_seekBar.setProgress((DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / (DataStruct.CurMacMode.Out.StepOutVol)));
        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            B_Outputvolome.setText(String.valueOf(val));
        } else {
            B_Outputvolome.setText(String.valueOf(val - 60));
        }

        DataOptUtil.syncLinkData(Define.UI_OutVal);
    }

    private void showWaringDialog() {

        Bundle bundle = new Bundle();
        bundle.putInt(WarningDialogIFragment.ST_DataOPT, 1);
        bundle.putString(WarningDialogIFragment.ST_SetTitle, getResources().getString(R.string.warning));

        bundle.putString(WarningDialogIFragment.ST_SetMessage, getResources().getString(R.string.ISLock));

        bundle.putString(WarningDialogIFragment.ST_SetOKText, getResources().getString(R.string.Sure));
        if (warningDialogIFragment == null) {
            warningDialogIFragment = new WarningDialogIFragment();
        }
        if (!warningDialogIFragment.isAdded()) {
            warningDialogIFragment.setArguments(bundle);
            warningDialogIFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
        }
        warningDialogIFragment.OnSetOnClickDialogListener(new WarningDialogIFragment.SetOnClickDialogListener() {

            @Override
            public void onClickDialogListener(int type, boolean boolClick) {

            }
        });
    }


    private void showLockDialog() {

        Bundle bundle = new Bundle();
        bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
        bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.dialog_title_Prompt));
        bundle.putString(AlertDialogFragment.ST_SetOKText, getResources().getString(R.string.Sure));
        if (!MacCfg.bool_OutChLock) {//锁定
            bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.Opt_Channel_Locked));
        } else {//解锁
            bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.Opt_Channel_unlock));
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
                    if (MacCfg.bool_OutChLock == false) {
                        MacCfg.bool_OutChLock = true;

                    } else {
                        MacCfg.bool_OutChLock = false;
                        MacCfg.bool_OutChLink = false;
                        setLinkState_Unlink();
                    }
                    //
                    setNoClick();
                    FlashPageUI();
                }
            }
        });


//        if(!MacCfg.bool_OutChLock){
//            bundle.putString(AlertResetOutSPKDialogFragment.ST_DataOPT, getResources().getString(R.string.Opt_Channel_Locked));
//        }else{
//            bundle.putString(AlertResetOutSPKDialogFragment.ST_DataOPT, getResources().getString(R.string.Opt_Channel_unlock));
//        }
//
//        bundle.putString(AlertResetOutSPKDialogFragment.ST_SetMessage, getResources().getString(R.string.No));
//        bundle.putString(AlertResetOutSPKDialogFragment.BT_SetMessage, getResources().getString(R.string.Yes));
//
//        if (setResetOutSPKDialogFragment == null) {
//            setResetOutSPKDialogFragment = new AlertResetOutSPKDialogFragment();
//        }
//        if (!setResetOutSPKDialogFragment.isAdded()) {
//            setResetOutSPKDialogFragment.setArguments(bundle);
//            setResetOutSPKDialogFragment.show(getActivity().getFragmentManager(), "setResetOutSPKDialogFragment");
//        }
//        setResetOutSPKDialogFragment.OnSetOnClickDialogListener(new AlertResetOutSPKDialogFragment.SetOnClickDialogListener() {
//            @Override
//            public void onClickDialogListener(int type, boolean boolClick) {
//                if (boolClick) {
//
//
//                }
//            }
//        });
    }


    /***重置输出通道*/
    private void showResetDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(AlertResetOutSPKDialogFragment.ST_DataOPT, getResources().getString(R.string.Opt_Channel_Set));
        bundle.putString(AlertResetOutSPKDialogFragment.ST_SetMessage, getResources().getString(R.string.Emptied));
        bundle.putString(AlertResetOutSPKDialogFragment.BT_SetMessage, getResources().getString(R.string.Default));

        if (setResetOutSPKDialogFragment == null) {
            setResetOutSPKDialogFragment = new AlertResetOutSPKDialogFragment();
        }
        if (!setResetOutSPKDialogFragment.isAdded()) {
            setResetOutSPKDialogFragment.setArguments(bundle);
            setResetOutSPKDialogFragment.show(getActivity().getFragmentManager(), "AlertResetOutSPKDialogFragment");
        }
        setResetOutSPKDialogFragment.OnSetOnClickDialogListener(new AlertResetOutSPKDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (boolClick) {
//                    for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//                        DataStruct.RcvDeviceData.OUT_CH[i].LinkFlag = 0;
//                    }

                    if (type == 0) {
                        setOutputNameEmpty();
                    } else if (type == 1) {
                        setOutputNameDefault(true);
                    }
                    for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                        DataOptUtil.SetOutputVolume(i);
                    }

                    MacCfg.bool_OutChLock = false;
                    setLinkState_Unlink();
                    FlashPageUI();
                }
            }
        });
    }


    private void setNoClick() {
        //|| DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].LinkFlag != 0


        if (MacCfg.bool_OutChLock || MacCfg.bool_OutChLink) {

            B_OutVaSetName.setTextColor(getResources().getColor(R.color.gray));
            B_OutVaSetName.setEnabled(false);

        } else {

            B_OutVaSetName.setTextColor(getResources().getColor(R.color.white));
            B_OutVaSetName.setEnabled(true);


        }
        if (!MacCfg.bool_OutChLock) {
            B_lock.setText(getResources().getString(R.string.Locked));
            ly_lock.setBackgroundResource(R.drawable.btn_input_name);
        } else {
            B_lock.setText(getResources().getString(R.string.unlocked));
            ly_lock.setBackgroundResource(R.drawable.btn_output_link_press);
        }

    }




    public void showUp(View v) {


    }

    private void setLinkCanClick() {
        ly_link.setBackgroundResource(R.drawable.btn_canclick);
        ly_link.setClickable(false);
        B_link.setClickable(false);
        B_link.setTextColor(getResources().getColor(R.color.gray));
        B_link.setText(String.valueOf(getResources().getString(R.string.btn_Link)));
        img_link.setImageDrawable(getResources().getDrawable(R.drawable.chs_unlink));
    }

    private void setLink(int Output, int Group) {
        ly_link.setClickable(true);
        B_link.setClickable(true);
        B_link.setTextColor(getResources().getColor(R.color.white));
        //  switch (Group) {
        img_link.setImageDrawable(getResources().getDrawable(R.drawable.chs_link));
        // System.out.println("BUG 值为" + Group);
        if (Group != 0) {
            B_link.setText(String.valueOf(getResources().getString(R.string.btn_Link) + "\t" + Group));
            ly_link.setBackgroundResource(R.drawable.btn_output_link_press);
        } else {
            B_link.setText(String.valueOf(getResources().getString(R.string.btn_Link)));
            ly_link.setBackgroundResource(R.drawable.btn_input_name);
        }
        DataOptUtil.SaveAllGain();


    }


    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();

        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < (windowHeight));
        System.out.println("BUG 得到的值为" + screenWidth + "=-=-=-=-=" + windowWidth);
        if (isNeedShowUp) {  //不需要向上显示
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight + (int) getResources().getDimension(R.dimen.space_30);
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight - (int) getResources().getDimension(R.dimen.space_30);
        }
        return windowPos;
    }


    //设置通道输出类型后的联调 wang测试
    void _LINKMODE_SPKTYPE_Dialog() {

        // showPopMeun();
        if (!MacCfg.bool_OutChLink) {
            if (DataOptUtil.CheckChannelCanLink() > 0) {
                LinkDataCoypLeftRight_DialogFragment mLinkDataCoypLeftRightDialogFragment = new LinkDataCoypLeftRight_DialogFragment();
                mLinkDataCoypLeftRightDialogFragment.show(getActivity().getFragmentManager(), "mLinkDataCoypLeftRightDialogFragment");
                mLinkDataCoypLeftRightDialogFragment.OnSetOnClickDialogListener(new LinkDataCoypLeftRight_DialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        MacCfg.bool_OutChLeftRight = boolClick;
                        //通道数据的复制
                        int Dfrom = 0, Dto = 0;
                        for (int i = 0; i < MacCfg.ChannelLinkCnt; i++) {
                            //true:从左复制到右，false:从右复制到左   */
                            if (MacCfg.bool_OutChLeftRight) {
                                Dfrom = MacCfg.ChannelLinkBuf[i][0];
                                Dto = MacCfg.ChannelLinkBuf[i][1];
                            } else {
                                Dfrom = MacCfg.ChannelLinkBuf[i][1];
                                Dto = MacCfg.ChannelLinkBuf[i][0];
                            }
                            DataOptUtil.channelDataCopy(Dfrom, Dto);
                        }
                        setLinkState_Link();
                        FlashPageUI();
                        DataOptUtil.SaveAllGain();
                        MacCfg.bool_OutChLock = true;
                        setNoClick();

                        //刷新界面
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
                        intentw.putExtra("state", true);


                    }
                });
            } else {
                AlertNoLinkDialogFragment alertNoLinkDialogFragment = new AlertNoLinkDialogFragment();
                alertNoLinkDialogFragment.show(getActivity().getFragmentManager(), "alertNoLinkDialogFragment");
            }
        } else {
            MacCfg.bool_OutChLock = false;

            //不联调
            setLinkState_Unlink();
            setNoClick();
        }
    }

    private void setLinkState_Link() {
        MacCfg.bool_OutChLink = true;
        setLinkBtnState(true);
    }

    private void setLinkState_Unlink() {
        MacCfg.bool_OutChLink = false;
        setLinkBtnState(false);
    }

    private void setLinkBtnState(boolean Link) {
        //System.out.println("BUG 进来刷新了啊");
        if (Link) {
            // B_Link.setBackgroundResource(R.drawable.chs_btn_outputset_press);
            //  B_link.setText(getString(R.string.UnLink));
            //   btn_link.setTextColor(getResources().getColor(R.color.output_xover_reset_button_text_color_press));
            // btn_link.setBackgroundResource(R.drawable.chs_output_xover_reset_color_press);
            ly_link.setBackgroundResource(R.drawable.btn_output_link_press);
            B_OutVaSetName.setTextColor(getResources().getColor(R.color.gray));
        } else {
            //B_Link.setBackgroundResource(R.drawable.chs_btn_outputset_normal);
            // B_Link.setText(getString(R.string.Link));
            // B_link.setText(getString(R.string.Link));
            ly_link.setBackgroundResource(R.drawable.btn_input_name);
            //    btn_link.setTextColor(getResources().getColor(R.color.output_xover_reset_button_text_color_normal));
            //    btn_link.setBackgroundResource(R.drawable.chs_output_xover_reset_color_normal);
            //   btn_spk_type.setTextColor(getResources().getColor(R.color.output_Link_normal_text_color));
        }
    }


    /**
     * 注意该处的Activity为DialogFragment
     */
    private void showSetOutSpkNameDialog() {
        if (output_or_inputSpkTypeDialog == null) {
            output_or_inputSpkTypeDialog = new Output_or_InputSpkTypeActivity();
        }

        output_or_inputSpkTypeDialog.show(getActivity().getFragmentManager(), "setEncryptionDialogFragment");
        output_or_inputSpkTypeDialog.OnSetOnClickDialogListener(new Output_or_InputSpkTypeActivity.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int Val, boolean boolClick) {
                if (boolClick) {
                    FlashPageUI();
                }
            }
        });
    }

    private void setOutputNameEmpty() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.setOutputSpk(0, i);
            DataOptUtil.setXOverWithOutputSPKType(i);
            //TV_Context[i].setText(GetChannelName(GetChannelNum(i)));
            //设置默认输出滤波器
//            DataStruct.RcvDeviceData.OUT_CH[i].h_filter = DataStruct.DefaultDeviceData.OUT_CH[i].h_filter;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_filter = DataStruct.DefaultDeviceData.OUT_CH[i].l_filter;
//            DataStruct.RcvDeviceData.OUT_CH[i].h_level = DataStruct.DefaultDeviceData.OUT_CH[i].h_level;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_level = DataStruct.DefaultDeviceData.OUT_CH[i].l_level;
//            DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.AllFreq_HPFreq;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.AllFreq_LPFreq;
        }
        flashOutputName();
        //TODO 混音
        SetInputSourceMixerEmpty();
        //  SetLinkState();
        //TODO 联调

    }


    //设置清空的混音值
    private void SetInputSourceMixerEmpty() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol = 0;

            //设置默认输出滤波器
//            DataStruct.RcvDeviceData.OUT_CH[i].h_filter = 2;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_filter = 2;
            DataStruct.RcvDeviceData.OUT_CH[i].h_level = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].l_level = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.AllFreq_HPFreq;
            DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.AllFreq_LPFreq;
        }
    }


    void setOutputNameDefault(boolean setdef) {

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.setOutputSpk(DataStruct.CurMacMode.Out.SPK_TYPE[i], i);
        }


        flashOutputName();

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.setXOverWithOutputSPKType(i);
        }

        //TODO
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.SetInputSourceMixerVol(i);
        }

    }


    private void flashMute() {
        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].mute == 0) {
            img_mute.setImageDrawable(getResources().getDrawable(R.drawable.chs_mute_press));
        } else {
            img_mute.setImageDrawable(getResources().getDrawable(R.drawable.chs_mute_normal));
        }
    }

    private void flashXoverDefault() {
        boolean have = false;
        have = false;
        for (int i = 0; i < DataStruct.CurMacMode.XOver.Level.max1; i++) {
            if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_level
                    == DataStruct.CurMacMode.XOver.Level.member1[i]) {
                have = true;
                HP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[i]);
                break;
            }
        }
        if (!have) {
            HP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[0]);
        }
        have = false;
        for (int i = 0; i < DataStruct.CurMacMode.XOver.Level.max1; i++) {
            if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].l_level
                    == DataStruct.CurMacMode.XOver.Level.member1[i]) {
                have = true;
                LP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[i]);
                break;
            }
        }
        if (!have) {
            LP_Level.setText(getString(R.string.NULL));
        }

        set6dbNullDefault();
        setOctOnOrOffDefault();
    }

    private void flashXover() {
        boolean have = false;

        //斜率
        have = false;
        for (int i = 0; i < DataStruct.CurMacMode.XOver.Level.max1; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_level
                    == DataStruct.CurMacMode.XOver.Level.member1[i]) {
                have = true;
                HP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[i]);
                break;
            }
        }
        if (!have) {
            HP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[0]);
        }
        have = false;
        for (int i = 0; i < DataStruct.CurMacMode.XOver.Level.max1; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_level
                    == DataStruct.CurMacMode.XOver.Level.member1[i]) {
                have = true;
                LP_Level.setText(DataStruct.CurMacMode.XOver.Level.memberName1[i]);
                break;
            }
        }
        if (!have) {
            LP_Level.setText(getString(R.string.NULL));
        }
        set6dbNull();
        setOctOnOrOff();
    }


    /**
     * 设置斜率为6dB的时候为null
     */
    private void set6dbNullDefault() {
        if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].l_level == 0) {
            LP_Filter.setText(getString(R.string.NULL));
            LP_Filter.setEnabled(false);
            img_lp_filter.setEnabled(false);
            LP_Filter.setTextColor(getResources().getColor(R.color.gray));
        } else {
            LP_Filter.setEnabled(true);
            img_lp_filter.setEnabled(true);
            LP_Filter.setTextColor(getResources().getColor(R.color.white));
            for (int i = 0; i < DataStruct.CurMacMode.XOver.Fiter.max1; i++) {
                if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].l_filter
                        == DataStruct.CurMacMode.XOver.Fiter.member1[i]) {
                    LP_Filter.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[i]);
                    break;
                }
            }
        }
        if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_level == 0) {
            HP_Filter.setText(getString(R.string.NULL));
            HP_Filter.setEnabled(false);
            HP_Filter.setTextColor(getResources().getColor(R.color.gray));
        } else {
            HP_Filter.setEnabled(true);
            HP_Filter.setTextColor(getResources().getColor(R.color.white));
            for (int i = 0; i < DataStruct.CurMacMode.XOver.Fiter.max1; i++) {
                if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_filter
                        == DataStruct.CurMacMode.XOver.Fiter.member1[i]) {
                    HP_Filter.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[i]);
                    break;
                }
            }
        }
    }


    /**
     * 设置斜率按钮的开关
     */
    private void setOctOnOrOffDefault() {


        if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_freq != 20) {
            sethighoctoff(true);
        } else {
            sethighoctoff(false);
        }
        if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].l_freq != 20000) {
            setlowoctoff(true);
        } else {
            setlowoctoff(false);
        }
        HP_Freq.setText(Integer.toString(DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].h_freq) + "Hz");

        LP_Freq.setText(Integer.toString(DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].l_freq) + "Hz");
    }


    /**
     * 设置斜率为6dB的时候为null
     */
    private void set6dbNull() {
        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_level == 0) {
            LP_Filter.setText(getString(R.string.NULL));
            LP_Filter.setEnabled(false);
            LP_Filter.setTextColor(getResources().getColor(R.color.gray));
        } else {
            LP_Filter.setEnabled(true);
            LP_Filter.setTextColor(getResources().getColor(R.color.white));
            for (int i = 0; i < DataStruct.CurMacMode.XOver.Fiter.max1; i++) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_filter
                        == DataStruct.CurMacMode.XOver.Fiter.member1[i]) {
                    LP_Filter.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[i]);
                    break;
                }
            }
        }
        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_level == 0) {
            HP_Filter.setText(getString(R.string.NULL));
            HP_Filter.setEnabled(false);
            HP_Filter.setTextColor(getResources().getColor(R.color.gray));
        } else {
            HP_Filter.setEnabled(true);
            HP_Filter.setTextColor(getResources().getColor(R.color.white));
            for (int i = 0; i < DataStruct.CurMacMode.XOver.Fiter.max1; i++) {
                if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_filter
                        == DataStruct.CurMacMode.XOver.Fiter.member1[i]) {
                    HP_Filter.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[i]);
                    break;
                }
            }
        }
    }


    /**
     * 设置斜率按钮的开关
     */
    private void setOctOnOrOff() {

        if (MacCfg.H_output_Freq_temp[OutputChannelSel] == 0) {
            MacCfg.H_output_Freq_temp[OutputChannelSel] = DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq;
        }

        if (MacCfg.L_output_Freq_temp[OutputChannelSel] == 0) {
            MacCfg.L_output_Freq_temp[OutputChannelSel] = DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq;
        }
        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq != 20) {
            sethighoctoff(true);
        } else {
            sethighoctoff(false);
        }
        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq != 20000) {
            setlowoctoff(true);
        } else {
            setlowoctoff(false);
        }
        HP_Freq.setText(Integer.toString(DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].h_freq) + "Hz");

        LP_Freq.setText(Integer.toString(DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].l_freq) + "Hz");
    }


    /**
     * 设置高通的开关
     */
    private void sethighoctoff(boolean b) {
        if (b) {
            high_oct_off.setImageDrawable(getResources().getDrawable(R.drawable.chs_eq_freq_off));
        } else {
            high_oct_off.setImageDrawable(getResources().getDrawable(R.drawable.chs_eq_freq_on));
        }
    }

    /**
     * 设置低通的开关
     */
    private void setlowoctoff(boolean b) {
        if (b) {
            low_oct_off.setImageDrawable(getResources().getDrawable(R.drawable.chs_eq_freq_off));
        } else {
            low_oct_off.setImageDrawable(getResources().getDrawable(R.drawable.chs_eq_freq_on));
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
                    } else {
                        //清除数据
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

                        //                }
                        //               }
                        //            });
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
    //////////////////////////////////////////////

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

//    private void SetLinkState() {
//        boolean flag = true;
//        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//
//            if (DataOptUtil.GetChannelNum(i) != 0) {
//                flag = false;
//                break;
//            }
//        }
//        if (flag == false) {
//            setLink(MacCfg.OutputChannelSel, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].LinkFlag);
//        } else {
//            setLinkCanClick();
//        }
//    }

    //刷新页面UI
    public void FlashPageUI() {
        WV_OutVa.setIndex(OutputChannelSel);
        //  centerShowHorizontalScrollView.onClicked(OutputChannelSel);
        // SetLinkState();

        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            mhs_seekBar.setProgressMax((DataStruct.CurMacMode.Out.MaxOutVol / DataStruct.CurMacMode.Out.StepOutVol) - 6);
        } else {
            mhs_seekBar.setProgressMax(DataStruct.CurMacMode.Out.MaxOutVol / DataStruct.CurMacMode.Out.StepOutVol);
        }

        if (MacCfg.bool_Encryption) {
            encryption.setVisibility(View.VISIBLE);
            flashXoverDefault();

        } else {
            encryption.setVisibility(View.GONE);

            //  }

            flashXover();
            setNoClick();
            flashOutputName();
            flashVolume();
            flashMute();
            flashPolar();
        }
        if (MacCfg.bool_OutChLink) {
            setLinkBtnState(true);
        } else {
            setLinkBtnState(false);
        }

        // if(useList(Define.SUP_Type,DataOptUtil.GetChannelNum(OutputChannelSel))){
        //     mhs_seekBar.setProgressMax(DataStruct.CurMacMode.Out.MaxOutVol/(DataStruct.CurMacMode.Out.StepOutVol*4));
        // }else{

    }


    private void flashPolar() {
//        if(MacCfg.bool_Encryption){
//            if (DataStruct.DefaultDeviceData.OUT_CH[OutputChannelSel].polar == 0) {
//                B_polar.setText(getResources().getString(R.string.Polar_P));
//                B_polar.setBackgroundResource(R.drawable.btn_output_name);
//            } else {
//                B_polar.setText(getResources().getString(R.string.Polar_N));
//                B_polar.setBackgroundResource(R.drawable.btn_output_link_press);
//            }
//        }else{
        if (DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].polar == 0) {
            B_polar.setText(getResources().getString(R.string.Polar_P));
            // B_polar.setTextColor(getResources().getColor(R.color.txt_normal));
            B_polar.setBackgroundResource(R.drawable.btn_output_name);
        } else {
            B_polar.setText(getResources().getString(R.string.Polar_N));
            //  B_polar.setTextColor(getResources().getColor(R.color.txt_press));
            B_polar.setBackgroundResource(R.drawable.btn_output_link_press);
        }
        //  }

    }

    private void flashVolume() {

        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            B_Outputvolome.setText(String.valueOf((DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / (DataStruct.CurMacMode.Out.StepOutVol * 4))));
        } else {
            B_Outputvolome.setText(String.valueOf((DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol) - 60));
        }
        mhs_seekBar.setProgress((DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol));
        //  }
//        }else{
//            B_Outputvolome.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol));
//            mhs_seekBar.setProgress((DataStruct.RcvDeviceData.OUT_CH[OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol));
//        }
    }

    private void flashOutputName() {

        if(DataOptUtil.isZh(mContext)){
            B_OutVaSetName.setTextSize(13);
        }else{
            B_OutVaSetName.setTextSize(10);
        }
        B_OutVaSetName.setText(DataOptUtil.GetChannelName(DataOptUtil.GetChannelNum(OutputChannelSel), mContext));
        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(OutputChannelSel))) {
            txt_volume.setText(getResources().getString(R.string.output_volume_1));
        } else {
            txt_volume.setText(getResources().getString(R.string.output_volume));
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        FlashPageUI();
    }


}
