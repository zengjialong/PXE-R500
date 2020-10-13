package com.chs.mt.pxe_r500.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.cache.util.LogUtil;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.InputSourceDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SEFFShare_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.UserGOPT_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.UserGOPT_Save_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.UserGroupDeleteDialogFragment;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.tools.EQ_SeekBar;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.Main_SeekBar;

import java.io.UnsupportedEncodingException;

import static android.app.Activity.RESULT_OK;


public class Home_Fragment extends Fragment {
    private Toast mToast;
    private static Context mContext;

    /*按键长按：true-长按，false-非长按*/
    /****************************   调音界面首页     ****************************/
    private Button[] MACUSEREFF = new Button[Define.MAX_UI_GROUP + 1];//Define.MAX_UI_GROUP
    private int UserGroup = 1;
    private int SYNC_INCSUB = 0;
    //true: select Master
    private Boolean Bool_MasterSub = true;


    private String Group_Name[] = new String[11];
    private String Group_Name_1[] = new String[11];
    private String Group_Name_2[] = new String[11];
    private int Click_Save_del = 0;

    private TextView BtnMaster;

    private LinearLayout LY_Mute;
    private ImageView ImgMute;
    private int masterValBuf = 0;

    private LongCickButton BtnValInc, BtnValSub;

    private Button BtnInputsource, BtnMixersource;
    //    private Button BtnMusic;
    private LinearLayout LYFeedback, LYInputsource;
    private TextView TVMKF;
    private Main_SeekBar VSB_Master;
    //private V_SeekBarIndexGainValItem[] EQItem=new V_SeekBarIndexGainValItem[3];
    //对话框
    private InputSourceDialogFragment mInputSourceDialog = null;
    private LoadingDialogFragment mLoadingDialogFragment = null;
    private UserGOPT_DialogFragment mUserGOPT_DialogFragment = null;
    private SEFFShare_DialogFragment seffshare = null;
    private UserGOPT_Save_DialogFragment userGroupDialogFragment = null;
    private UserGroupDeleteDialogFragment userGroupDelayDialogFragment = null;
    private AlertDialogFragment mAlertDialogFragment = null;

    //////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_home, container, false);
        initView(view);
        initData();
        initClick();
        FlashPageUI();

        return view;
    }

    private void initView(View view) {
        addToningHomeView(view);
        addMasterVal(view);
    }
    private void initData() {

    }

    private void initClick() {
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

    public void InitLoadPageUI() {
        if (MACUSEREFF[1] == null) {
            return;
        }
        addMasterValListener();
        FlashPageUI();
    }

    public void FlashPageUI() {
        if (MACUSEREFF[1] == null) {
            return;
        }

        if(DataStruct.isConnecting){
            SetToningHomeUserGroupName();
        }else{
            SetUserGroupNameDefault();
        }

        flashMasterVol();
        flashMute();
        flashInputsource();
        flashMixersource();
        flashEncrypt();
        flashTextColor();
        //flashLYMKF();

    }

    /*********************************************************************/
    /***************************    主音量     ****************************/
    /*********************************************************************/
    private void addMasterVal(View view) {
//        BtnMusic =  view.findViewById(R.id.id_b_musicbox);
        BtnInputsource = view.findViewById(R.id.id_b_inputsource);
        BtnMixersource = view.findViewById(R.id.id_b_mixersource);


        BtnMaster = view.findViewById(R.id.id_b_master);
        LY_Mute = view.findViewById(R.id.id_ly_mute);
        ImgMute = view.findViewById(R.id.id_b_mute);
        BtnValInc = view.findViewById(R.id.id_main_val_inc);
        BtnValSub = view.findViewById(R.id.id_main_val_sub);

        /////////////
        VSB_Master = (Main_SeekBar) view.findViewById(R.id.id_main_sb);
        VSB_Master.setProgressMax(35);//DataStruct.CurMacMode.Master.MAX

        addMasterValListener();
    }


    /**
     * 刷新麦克风
     */
//    private void flashLYMKF(){
//        if (DataStruct.RcvDeviceData.EFF.Mic_mute == 1) {
//            BtnMKF.setBackground(mContext.
//                    getResources().getDrawable(R.drawable.mkf_normal));
//            TVMKF.setText(mContext.getResources().getString(R.string.VolumeMicON));
//        }else{
//            BtnMKF.setBackground(mContext.
//                    getResources().getDrawable(R.drawable.mkf_press));
//            TVMKF.setText(mContext.getResources().getString(R.string.VolumeMicOFF));
//        }
//    }
    private void addMasterValListener() {


//        BtnMusic.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, MusicActivity.class);
//                startActivity(intent);
//            }
//        });

        VSB_Master.setOnSeekBarChangeListener(new Main_SeekBar.OnMSBEQSeekBarChangeListener() {
            @Override
            public void onProgressChanged(Main_SeekBar mvs_SeekBar, int progress, boolean fromUser) {
                DataStruct.RcvDeviceData.SYS.main_vol  = progress;
            //    DataStruct.RcvDeviceData.SYS.main_vol = Define.Array_Main_Vol[MacCfg.main_vol] + 60;
                if (DataStruct.RcvDeviceData.SYS.main_vol == 0) {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
                    setMute(true);
                } else {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                    setMute(false);
                }
                BtnMaster.setText(String.valueOf(DataStruct.RcvDeviceData.SYS.main_vol ));
            }
        });

        ///////
        //主音源
//        if (DataStruct.CurMacMode.BOOL_INPUT_SOURCE) {
        BtnInputsource.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputsourceEventDialog();
            }
        });
//        } else {
//            BtnInputsource.setVisibility(View.GONE);
//        }
        //混音音源

        BtnMixersource.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MixerSourceDialog();
            }
        });







        /*
         * 音量静音
         */
        ImgMute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                        DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                        setMute(false);
                    } else {
                        DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
                        setMute(true);
                    }
            }
        });
        /*
         * 音量增减
         */
        BtnValSub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SYNC_INCSUB = 0;
                MasterVol_INC_SUB(false);
            }
        });
        BtnValSub.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                BtnValSub.setStart();
                return false;
            }
        });
        BtnValSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SYNC_INCSUB = 0;
                MasterVol_INC_SUB(false);
            }
        }, MacCfg.LongClickEventTimeMax);
        /////
        BtnValInc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SYNC_INCSUB = 1;
                MasterVol_INC_SUB(true);
            }
        });
        BtnValInc.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                BtnValInc.setStart();
                return false;
            }
        });
        BtnValInc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SYNC_INCSUB = 1;
                MasterVol_INC_SUB(true);
            }
        }, MacCfg.LongClickEventTimeMax);
    }


    public void flashMKFUI() {
    }


    private int sendMasterVol(int vol) {
        int m_nPos = 0;
        if (vol == 0) {
            m_nPos = 0;
        } else if (vol == 1) {
            m_nPos = 10;
        } else if (vol == 2) {
            m_nPos = 14;
        } else if ((vol >= 3) && (vol <= 24)) {
            m_nPos = (vol - 2) * 2 + 14;
            if ((vol % 2) != 0) {

                m_nPos = m_nPos - 1;
            }
        } else if ((vol >= 25) && (vol <= 35)) {

            m_nPos = (vol - 25) + 58;

        }
        return m_nPos;
    }


    private void InputsourceEventDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(InputSourceDialogFragment.ST_DataOPT, InputSourceDialogFragment.DataOPT_INS);
        bundle.putInt(InputSourceDialogFragment.ST_DataOPType, DataStruct.RcvDeviceData.SYS.input_source);
        if (mInputSourceDialog == null) {
            mInputSourceDialog = new InputSourceDialogFragment();
        }
        if (!mInputSourceDialog.isAdded()) {
            mInputSourceDialog.setArguments(bundle);
            mInputSourceDialog.show(getActivity().getFragmentManager(), "mInputSourceDialog");
        }

        mInputSourceDialog.OnSetOnClickDialogListener(new InputSourceDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick,int DataType) {
//                DataStruct.RcvDeviceData.SYS.input_source = DataStruct.CurMacMode.inputsource.inputsource[type];
//                BtnInputsource.setText(DataStruct.CurMacMode.inputsource.Name[type]);
                if(boolClick){
                    DataStruct.RcvDeviceData.SYS.input_source = DataStruct.CurMacMode.inputsource.inputsource[type];
                }
                flashTextColor();
                flashInputsource();
            }
        });
    }

    private void MixerSourceDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(InputSourceDialogFragment.ST_DataOPT, InputSourceDialogFragment.DataOPT_MIXER);
        bundle.putInt(InputSourceDialogFragment.ST_DataOPType, DataStruct.RcvDeviceData.SYS.aux_mode);
        InputSourceDialogFragment mMInputSourceDialog = new InputSourceDialogFragment();
        mMInputSourceDialog.setArguments(bundle);
        mMInputSourceDialog.show(getActivity().getFragmentManager(), "mMInputSourceDialog");
        mMInputSourceDialog.OnSetOnClickDialogListener(new InputSourceDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick,int OutType) {
                if(boolClick){
                    DataStruct.RcvDeviceData.SYS.aux_mode = DataStruct.CurMacMode.Mixersource.inputsource[type];
                }
                flashTextColor();
                flashMixersource();
            }
        });
    }


    public void flashInputsource() {
        for (int i = 0; i < DataStruct.CurMacMode.inputsource.Max; i++) {
            if (DataStruct.RcvDeviceData.SYS.input_source ==
                    DataStruct.CurMacMode.inputsource.inputsource[i]) {
                BtnInputsource.setText(DataStruct.CurMacMode.inputsource.Name[i]);
                break;
            }
        }
    }

    public void flashMixersource() {

        boolean haveS = false;
        for (int i = 0; i < DataStruct.CurMacMode.Mixersource.Max; i++) {
            if (DataStruct.RcvDeviceData.SYS.aux_mode ==
                    DataStruct.CurMacMode.Mixersource.inputsource[i]) {
                BtnMixersource.setText(DataStruct.CurMacMode.Mixersource.Name[i]);
                haveS = true;
                break;
            }
        }
        if (!haveS) {
            BtnMixersource.setText(R.string.OFF);
        }
    }


    public void flashTextColor(){
        if(DataStruct.RcvDeviceData.SYS.input_source==DataStruct.RcvDeviceData.SYS.aux_mode){
            BtnMixersource.setTextColor(getResources().getColor(R.color.red));
        }else{
            BtnMixersource.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void flashEncrypt() {
        if (!DataStruct.CurMacMode.BOOL_ENCRYPTION) {
            return;
        }

    }


    private void MasterVol_INC_SUB(boolean inc) {
        int val;

        if (DataStruct.RcvDeviceData.SYS.main_vol > DataStruct.CurMacMode.Master.MAX) {
            DataStruct.RcvDeviceData.SYS.main_vol = DataStruct.CurMacMode.Master.MAX;
        }
        val = DataStruct.RcvDeviceData.SYS.main_vol;
        if (inc) {
            if (++val > DataStruct.CurMacMode.Master.MAX) {
                val = DataStruct.CurMacMode.Master.MAX;
            }
        } else {
            if (--val < 0) {
                val = 0;
            }
        }
        DataStruct.RcvDeviceData.SYS.main_vol =val;
        DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
        setMute(false);
        VSB_Master.setProgress(val);
        BtnMaster.setText(String.valueOf(val));


    }


    private void setMute(boolean mute) {
        if (mute) {
            // LY_Mute.setBackgroundResource(R.drawable.chs_btn_output_mute_press);
            ImgMute.setImageDrawable(getResources().getDrawable(R.drawable.chs_mute_press));
        } else {
            //  LY_Mute.setBackgroundResource(R.drawable.chs_btn_output_mute_normal);
            ImgMute.setImageDrawable(getResources().getDrawable(R.drawable.chs_mute_normal));
        }

    }

    private void setMasterVol() {
        Bool_MasterSub = true;


        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            if (DataStruct.RcvDeviceData.IN_CH[0].Valume > DataStruct.CurMacMode.Master.MAX) {
                DataStruct.RcvDeviceData.IN_CH[0].Valume = DataStruct.CurMacMode.Master.MAX;
            }
        } else {
            if (DataStruct.RcvDeviceData.SYS.main_vol > DataStruct.CurMacMode.Master.MAX) {
                DataStruct.RcvDeviceData.SYS.main_vol = DataStruct.CurMacMode.Master.MAX;
            }
        }
    }

    private void setSubVol() {
        Bool_MasterSub = false;


        if (DataStruct.RcvDeviceData.IN_CH[0].Valume > DataStruct.CurMacMode.Master.MAX) {
            DataStruct.RcvDeviceData.IN_CH[0].Valume = DataStruct.CurMacMode.Master.MAX;
        }

    }

    private void flashMute() {
//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
        if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
            setMute(true);
        } else {
            setMute(false);
        }
//        }else {
//            if(DataStruct.RcvDeviceData.IN_CH[0].Valume != 0){
//                setMute(false);
//            }else {
//                setMute(true);
//            }
//        }
    }


    public void flashMasterVol() {
        flashMute();

        int val;
        if (DataStruct.RcvDeviceData.SYS.main_vol > DataStruct.CurMacMode.Master.MAX) {
            DataStruct.RcvDeviceData.SYS.main_vol = DataStruct.CurMacMode.Master.MAX;
        }
        val = DataStruct.RcvDeviceData.SYS.main_vol;
//        for (int i = 0; i < Define.Array_Main_Vol.length; i++) {
//            if (Define.Array_Main_Vol[i] == (val - 60)) {
//                val = i;
//                break;
//            }
//        }


        VSB_Master.setProgressMax(35);
        VSB_Master.setProgress(val);
        BtnMaster.setText(String.valueOf(val));

        if (DataStruct.RcvDeviceData.SYS.main_vol == 0) {
            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;

            setMute(true);
        }
    }

    private void setSubVal(int val) {
        if (DataStruct.CurMacMode.BOOL_SET_SPK_TYPE) {
            // setSpkTypeBassVolume(val);
        } else {
            DataStruct.RcvDeviceData.OUT_CH[4].gain = val;
            DataStruct.RcvDeviceData.OUT_CH[5].gain = val;
        }

    }




    /***************************    音效调用     ****************************/
    private void addToningHomeView(View view) {
        MACUSEREFF[1] = view.findViewById(R.id.btn_useg_1);
        MACUSEREFF[2] = view.findViewById(R.id.btn_useg_2);
        MACUSEREFF[3] = view.findViewById(R.id.btn_useg_3);
        MACUSEREFF[4] = view.findViewById(R.id.btn_useg_4);
        MACUSEREFF[5] = view.findViewById(R.id.btn_useg_5);
        MACUSEREFF[6] = view.findViewById(R.id.btn_useg_6);

        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            MACUSEREFF[i].setTag(i);
            MACUSEREFF[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    UserGroup = (Integer) view.getTag();
                  if ((DataStruct.isConnecting) && (DataStruct.U0SynDataSucessFlg)) {
                        DealUserGroupSeff();
                        DealUserGroupSeff();
                    } else {
                        ToastMsg(mContext.getResources().getString(R.string.off_line_mode));
                    }

                }
            });
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

    private void DealUserGroupSeff() {
        if (mUserGOPT_DialogFragment == null) {
            mUserGOPT_DialogFragment = new UserGOPT_DialogFragment();
        }

        if (!mUserGOPT_DialogFragment.isAdded()) {
            mUserGOPT_DialogFragment.show(getActivity().getFragmentManager(), "mUserGOPT_DialogFragment");
        }

        mUserGOPT_DialogFragment.OnSetOnClickDialogListener(new UserGOPT_DialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                switch (type) {
                    case 0:
                        showSaveUserGroupDialog();
                        break;
                    case 1://调用
                        DataOptUtil.ReadGroupData(UserGroup, mContext);
                        MacCfg.bool_OutChLink  = false;
                        break;
                    case 2://删除
                        showSaveUserGroupPwdDialog();

                        break;
                    case 3:
                        if (seffshare == null) {
                            seffshare = new SEFFShare_DialogFragment();
                        }
                        if (!seffshare.isAdded()) {
                            seffshare.show(getActivity().getFragmentManager(), "seffshare");
                        }

                        seffshare.OnSetOnClickDialogListener(
                                new SEFFShare_DialogFragment.SetOnClickDialogListener() {
                                    @Override
                                    public void onClickDialogListener(int type, boolean boolClick) {
                                        //MenuActivity.this.finish();
                                    }
                                }
                        );
                        break;
                    case 4:
                        ComponentName componentName = new ComponentName(
                                "cn.madeapps.android.yibaomusic",   //要去启动的App的包名
                                "cn.madeapps.android.yibaomusic.activity.ChooseSoundEffectActivity_");//要去启动的App中的Activity的类名
                        Intent intent = new Intent();
                        intent.setComponent(componentName);
                        startActivityForResult(intent, 101);
                        break;

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String sound_url = extras.getString("sound_url");
            LogUtil.d("音效文件路径 ： " + sound_url);
            if (sound_url != null) {
                DataOptUtil.loadSEFFileDownload(mContext, sound_url);
            }

        }
    }

    private void showSaveUserGroupPwdDialog() {
        if (userGroupDelayDialogFragment == null) {
            userGroupDelayDialogFragment = new UserGroupDeleteDialogFragment();
        }
        if (!userGroupDelayDialogFragment.isAdded()) {
            userGroupDelayDialogFragment.show(getActivity().getFragmentManager(), "userGroupDelayDialogFragment");
        }
        userGroupDelayDialogFragment.OnSetUserGroupDeleteDialogFragmentChangeListener(new UserGroupDeleteDialogFragment.OnUserGroupDeleteDialogFragmentClickListener() {
            @Override
            public void onUserGroupDeleteListener(int Index, boolean UserGroupflag) {
                if (UserGroupflag) {
                    DataOptUtil.DeleteGroup(UserGroup);
                    SetToningHomeUserGroupName();
                }
            }
        });
    }




    private void showSaveUserGroupDialog() {

        Bundle bundle = new Bundle();
        bundle.putInt(UserGOPT_Save_DialogFragment.ST_UserGroup, UserGroup);

//        if(userGroupDialogFragment == null){
        userGroupDialogFragment = new UserGOPT_Save_DialogFragment();
//        }
        userGroupDialogFragment.setArguments(bundle);
        if (!userGroupDialogFragment.isAdded()) {
            userGroupDialogFragment.show(getActivity().getFragmentManager(), "userGroupDialogFragment");
        }

//        userGroupDialogFragment.show(getActivity().getFragmentManager(), "userGroupDialogFragment");
        userGroupDialogFragment.OnSetUserGroupDialogFragmentChangeListener(new UserGOPT_Save_DialogFragment.OnUserGroupDialogFragmentClickListener() {
            //保存
            @Override
            public void onUserGroupSaveListener(int Index, boolean UserGroupflag) {

                System.out.println("BUG 这个的这i为"+UserGroup);
                SetToningHomeUserGroupName();
                DataOptUtil.SaveGroupData(UserGroup, mContext);
                //showLoadingDialog();

            }

        });
    }

    private void showDelayUserGroupDialog() {
        if (userGroupDelayDialogFragment == null) {
            userGroupDelayDialogFragment = new UserGroupDeleteDialogFragment();
        }
        if (!userGroupDelayDialogFragment.isAdded()) {
            userGroupDelayDialogFragment.show(getActivity().getFragmentManager(), "userGroupDelayDialogFragment");
        }
        userGroupDelayDialogFragment.OnSetUserGroupDeleteDialogFragmentChangeListener(new UserGroupDeleteDialogFragment.OnUserGroupDeleteDialogFragmentClickListener() {
            @Override
            public void onUserGroupDeleteListener(int Index, boolean UserGroupflag) {
                if (UserGroupflag) {
                    DataOptUtil.DeleteGroup(UserGroup);
                    SetToningHomeUserGroupName();
                }
            }
        });
    }

    /*设置调音主页用户组音效名字*/
    private void SetToningHomeUserGroupName() {
        System.out.println("BUG 第一个的之为"+getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[0])+"=-="+getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[1]));
        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            if (checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[i])) {
                for (int j = 0; j < Group_Name_1.length; j++) {
                        MACUSEREFF[i].setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]));
                }
            } else {
                        MACUSEREFF[i].setText(getResources().getString(R.string.Sound_EFF) + String.valueOf(i));
            }
        }
    }

    private boolean checkUserGroupByteNull(int[] ug) {
        for (int i = 0; i < 15; i++) {
            if (ug[i] != 0x00) {
                return true;
            }
        }
        return false;
    }

    public  void SetUserGroupNameDefault() {
        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            MACUSEREFF[i].setText(getResources().getString(R.string.Sound_EFF)  + String.valueOf(i));
        }
    }

    private String getGBKString(int[] nameC) {
        byte[] GBK = new byte[16];
        for (int j = 0; j < 16; j++) {
            GBK[j] = 0x00;
        }
        int n = 0;
        String uNameString = null;
        for (int j = 0; j < 13; j++) {
            if (nameC[j] != 0x00) {
                GBK[j] = (byte) nameC[j];
                ++n;
            }
        }
        try {
            byte[] GBKN = new byte[n];
            for (int j = 0; j < n; j++) {
                GBKN[j] = GBK[j];
            }
            uNameString = new String(GBKN, "GBK");
        } catch (UnsupportedEncodingException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }

        return uNameString;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////

}