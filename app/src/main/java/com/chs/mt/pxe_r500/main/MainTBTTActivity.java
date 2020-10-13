package com.chs.mt.pxe_r500.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.Permissions.PermissionsActivity;
import com.chs.mt.pxe_r500.Permissions.PermissionsChecker;
import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bluetooth.spp_ble.DeviceListActivity;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.fragment.DelayAuto_Fragment;
import com.chs.mt.pxe_r500.fragment.EQ_Fragment;
import com.chs.mt.pxe_r500.fragment.Home_Fragment;
import com.chs.mt.pxe_r500.fragment.MixerFragment;
import com.chs.mt.pxe_r500.fragment.OutputXover_Fragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.ADDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AboutDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertDialogIFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.AlertIOSDialog;
import com.chs.mt.pxe_r500.fragment.dialogFragment.EnterAdvanceDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LinkDataCoypLeftRight_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SEFFSave_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SEFFShare_DialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetEncryptionDialogFragment;
import com.chs.mt.pxe_r500.fragment.dialogFragment.SetoffDelayTimeFragment;
import com.chs.mt.pxe_r500.operation.AnimationUtil;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.service.ServiceOfCom;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;
import com.chs.mt.pxe_r500.updateApp.DownLoadCompleteReceiver;
import com.chs.mt.pxe_r500.updateApp.UpdateUtil;
import com.chs.mt.pxe_r500.util.FileUtil;
import com.chs.mt.pxe_r500.util.ToastUtil;
import com.chs.mt.pxe_r500.util.ToolsUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
//这是第一次进来显示的界面
public class MainTBTTActivity extends FragmentActivity {
    private static final String TAG = "MainTETActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //页面
//    private MusicFragment mMusicFragment = null;
    private DelayAuto_Fragment mDelayAuto = null;
    //    private DelayFRS_Draw_Fragment mDelayFRS_Draw = null;
    //  private DelayFRS_Fragment mDelayFRS = null;
    private EQ_Fragment mEQ = null;
    private AlertIOSDialog myDialog;

    private OutputXover_Fragment mOutputXover = null;
    private Home_Fragment mHome = null;

    private PopupMenuGuestActivity popuMenuGuest;

    private MixerFragment mMixer = null;
    //顶部状态栏
    private TextView TV_ViewPageName, TV_Connect, TV_Connect1;
    private ImageView B_ConMenu;//BtnTMsg
    private ImageView B_ConnectState;
    private Button LLyout_HighSettings;
    private LinearLayout LLyout_Back;
    private Button btn_back;
    private boolean bool_HighSettings = false;
    private PopupMenuActivity popuMenu;

    private AlertDialogFragment mAlertDialogFragment = null;

    private LinearLayout LYMainVal;
    private MHS_SeekBar SBMainVal;
    //导航按键
    private LinearLayout LY_Buttom;
    private int ButtomItemMax = 8;
    private int ButtomItemMaxUse = 5;
    private ImageView[] IV_ButtomSel = new ImageView[ButtomItemMax];
    private ImageView[] IV_ButtomItem = new ImageView[ButtomItemMax];
    private TextView[] TV_ButtomItemName = new TextView[ButtomItemMax];
    private LinearLayout[] RLyout_ButtomItem = new LinearLayout[ButtomItemMax];

    //Service端的Messenger对象
    private Messenger mServiceMessenger;
    //Activity端的Messenger对象
    private Messenger mActivityMessenger;
    private static Context mContext;
    private Toast mToast;
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver;
    private ScreenBroadcastReceiver mScreenReceiver;
    private DownLoadCompleteReceiver mDownLoadCompleteReceiver;
    private boolean EXITFLAG = false;
    private boolean isFront = false;
    /*********************************************************************/
    /*******************  蓝牙SPP-LE   通信     **********************/
    /*********************************************************************/
    private static final int REQUEST_FINE_LOCATION = 168;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static final int REQUEST_WirteSettings = 4;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_Name = "device_name";
    // 连接设备的名称
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    // 本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static BluetoothSocket btSocket;

    /**
     * 蓝牙音频传输协议
     */
    private BluetoothA2dp a2dp;
    private BluetoothSocket socket;       //蓝牙连接socket
    private Handler mOthHandler;
    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice currentBluetoothDevice;
    private static final int REQUEST_CODE_PERMISSIONS = 133; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.DISABLE_KEYGUARD,
            Manifest.permission.WAKE_LOCK
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    /*********************************************************************/
    /*******************           车机音量          **********************/
    /*********************************************************************/
//    private TWUtil mTWUtil= null;

    /*********************************************************************/
    /*******************  DialogFragment    **********************/
    /*********************************************************************/
    private ADDialogFragment mADDialogFragment = null;
    private LoadingDialogFragment mLoadingDialogFragment = null;

    private AlertDialogFragment alertDialogFragment = null;
    private AlertDialogIFragment alertDialogIFragment = null;

    private EnterAdvanceDialogFragment EnterAdvanceDialog = null;
    private String uriPath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            checkUriFile();
            finish();
            return;
        }
        mContext = this;

        MacCfg.bool_HaveSEFFUpdate = checkHaveSEFFileLoad();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_activity_main);

        ToolsUtil.setWindowStatusBarColor((Activity) mContext, R.color.color_Top_Bar);
//        StatusBarCompat.setStatusBarColor(this, color, lightStatusBar);
//        initCarVol();
        initData();
        initView();

        // 获取本地蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mPermissionsChecker = new PermissionsChecker(this);
        isFront = true;


        if (isGrantExternalRW(this)) {
            initLoadDialog();
        }


        //    MacCfg.BOOL_SoundStatues = DataOptUtil.getSoundStatus(mContext);
        flashBtnTMsg();
        flashFreqType();
    }

    private void initLoadDialog() {
        checkUriFile();
        if (isHaveAd()) {
            showAdDialog();
        } else {
            CheckAndOpenBluetooth();
        }
    }

    private void initData() {

        DataOptUtil.InitApp(mContext);
        //绑定Service
        Intent intent = new Intent(mContext, ServiceOfCom.class);
        mContext.bindService(intent, connection, Service.BIND_AUTO_CREATE);
        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter = new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        //注册receiver
        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
       // startScreenBroadcastReceiver();
        //initUpdata();//版本升级
    }

    private void initView() {
        initFragment();
        initTopbar();
        initBottomBar();
        backToMain();
//        LLyout_HighSettings.setVisibility(View.VISIBLE);
        BottomItemClick(2);
    }

    private void closePhoneSound(boolean isChecked) {
        Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, isChecked ? 1 : 0);
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (isChecked) {
            mAudioManager.loadSoundEffects();
        } else {
            mAudioManager.unloadSoundEffects();
        }
    }

    private void initFragment() {
        //页面
        mDelayAuto = new DelayAuto_Fragment();

        mEQ = new EQ_Fragment();

        mOutputXover = new OutputXover_Fragment();
        mHome = new Home_Fragment();

        mMixer = new MixerFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragmentTransaction.remove(fragment);
            }
        }

        fragmentTransaction
                .add(R.id.id_framelayout, mHome)
                .add(R.id.id_framelayout, mEQ)
                .add(R.id.id_framelayout, mDelayAuto)
                .add(R.id.id_framelayout, mOutputXover)
                .add(R.id.id_framelayout, mMixer)
                .hide(mDelayAuto)
                .hide(mMixer)
                .hide(mEQ)
                .hide(mOutputXover)
                .commit()
        ;
    }

    private void initTopbar() {

//        BtnTMsg = (Button) findViewById(R.id.id_b_msg);
        LYMainVal = (LinearLayout) findViewById(R.id.id_ly_sb_main_val);
        SBMainVal = (MHS_SeekBar) findViewById(R.id.id_sb_main_val);
        SBMainVal.setProgressMax(30);//DataStruct.CurMacMode.Master.MAX
        SBMainVal.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
                    DataStruct.RcvDeviceData.IN_CH[0].Valume = progress;
                } else {
                    DataStruct.RcvDeviceData.SYS.main_vol = progress;
                }
//                if(mHome != null){
//                    mHome.flashMasterVol();
//                }
                handlerMainVal.removeMessages(0);
                handlerMainVal.sendEmptyMessageDelayed(0, 3000);
            }
        });

        B_ConnectState = (ImageView) findViewById(R.id.id_b_Connect);
        TV_ViewPageName = (TextView) findViewById(R.id.di_tv_viewpage_name);
        LLyout_Back = (LinearLayout) findViewById(R.id.id_llyout_back);

        btn_back=findViewById(R.id.id_b_back);

        B_ConMenu = (ImageView) findViewById(R.id.id_b_menu);
        TV_Connect = (TextView) findViewById(R.id.id_tv_Connect);
        TV_Connect1 = (TextView) findViewById(R.id.id_tv_Connect1);

        LLyout_HighSettings = (Button) findViewById(R.id.id_b_advance);


        popuMenu = new PopupMenuActivity(this);
        popuMenuGuest = new PopupMenuGuestActivity(this);
        B_ConMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AnimationUtil.toVisibleAnim(mContext, view);

                if (!Define.bool_FunsPage) {
                    popuMenuGuest.showLocation(R.id.id_b_menu);// 设置弹出菜单弹出的位置
                    popuMenuGuest.setOnItemClickListener(new PopupMenuGuestActivity.OnItemClickListener() {

                        @Override
                        public void onClick(int item, String str) {
                            AboutDialogFragment aboutDialog = new AboutDialogFragment();
                            aboutDialog.show(getFragmentManager(), "AboutDialogFragment");
                            aboutDialog.OnSetOnClickDialogListener(
                                    new AboutDialogFragment.SetOnClickDialogListener() {
                                        @Override
                                        public void onClickDialogListener(int type, boolean boolClick) {
                                            //MenuActivity.this.finish();
                                        }
                                    }
                            );
                        }
                    });
                } else {
                    popuMenu.flashTurningMode();
                    popuMenu.showLocation(R.id.id_b_menu);// 设置弹出菜单弹出的位置
                    popuMenu.setOnItemClickListener(new PopupMenuActivity.OnItemClickListener() {

                        @Override
                        public void onClick(int item, String str) {
                            switch (item) {
                                case 0://分享音效
                                    SEFFShare_DialogFragment seffshare = new SEFFShare_DialogFragment();
                                    seffshare.show(getFragmentManager(), "seffshare");
                                    seffshare.OnSetOnClickDialogListener(
                                            new SEFFShare_DialogFragment.SetOnClickDialogListener() {
                                                @Override
                                                public void onClickDialogListener(int type, boolean boolClick) {
                                                    //MenuActivity.this.finish();
                                                }
                                            }
                                    );
                                    break;
                                case 1:
                                    SEFFSave_DialogFragment seffSave = new SEFFSave_DialogFragment();
                                    seffSave.show(getFragmentManager(), "seffSave");
                                    seffSave.OnSetOnClickDialogListener(new SEFFSave_DialogFragment.SetOnClickDialogListener() {
                                        @Override
                                        public void onClickDialogListener(int type, boolean boolClick) {

                                        }
                                    });
                                    break;
                                case 2:
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, SEff_DownloadActivity.class);
                                    mContext.startActivity(intent);
                                    break;
                                case 3:
                                    AboutDialogFragment aboutDialog = new AboutDialogFragment();
                                    aboutDialog.show(getFragmentManager(), "AboutDialogFragment");
                                    aboutDialog.OnSetOnClickDialogListener(
                                            new AboutDialogFragment.SetOnClickDialogListener() {
                                                @Override
                                                public void onClickDialogListener(int type, boolean boolClick) {
                                                    //MenuActivity.this.finish();
                                                }
                                            }
                                    );
                                    break;
                                case 4:
                                    MenuExit();
//                                Timer timer = new Timer();
//                                timer.schedule(new TimerTask(){
//                                    @Override
//                                    public void run(){
//
//                                    }
//                                }, 388);
                                    break;
                                case 5:
                                    break;
                                case 6:

                                    break;
                                case 7:
                                    break;
                                case 8:

                                    break;
                                case 9:
                                    break;
                                case 10:
                                    break;
                                case 11:
                                    showEncryption();
                                    break;
                                case 12:
//                                if (MacCfg.bool_Encryption) {
//                                    showEncryPTDelay();
//                                } else {
                                    showOFFDelay();
                                    //   }
                                    break;

                                default:
                                    break;
                            }


                        }
                    });

                }
            }

        });

        B_ConnectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return;
                //AnimationUtil.AnimScale(mContext,view);

                //if(!DataStruct.isConnecting){
                //ServiceOfCom.disconnectSet();
                ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(500);
                sa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //ServiceOfCom.connectToDevice();
                        ConnectClickEvent();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.startAnimation(sa);
                //}


            }
        });

        LLyout_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Define.bool_FunsPage){
                    Bundle bundle = new Bundle();
                    bundle.putInt(EnterAdvanceDialogFragment.ST_DataOPT, 0);
                    bundle.putString(EnterAdvanceDialogFragment.ST_SetMessage, "");
                    EnterAdvanceDialog = new EnterAdvanceDialogFragment();
                    if (!EnterAdvanceDialog.isAdded()) {
                        EnterAdvanceDialog.setArguments(bundle);
                        EnterAdvanceDialog.show(getFragmentManager(), "EnterAdvanceDialog");
                    }
                    EnterAdvanceDialog.OnSetOnClickDialogListener(
                            new EnterAdvanceDialogFragment.SetOnClickDialogListener() {
                                @Override
                                public void onClickDialogListener(int type, boolean boolClick) {
                                    if(boolClick){
                                        if(type == 1){
                                            Define.bool_FunsPage=true;
                                            initBottomItemClick(2);
                                            backToMain();
                                        }else {

                                            ToastMsg(getString(R.string.PasswordIncorrect));
                                        }
                                    }
                                }
                            }
                    );


                }else {
                    Define.bool_FunsPage=false;
                    initBottomItemClick(2);
                    backToMain();
                }
            }
        });
    }


    /**
     * 加密设置
     */
    private void showEncryption() {

        //if (DataStruct.isConnecting) {
        SetEncryptionDialogFragment setEncryptionDialogFragment = new SetEncryptionDialogFragment();
        setEncryptionDialogFragment.show(getFragmentManager(), "setEncryptionDialogFragment");
        setEncryptionDialogFragment.OnSetEncryptionDialogFragmentChangeListener(new SetEncryptionDialogFragment.OnEncryptionDialogFragmentClickListener() {
            @Override
            public void onEncryptionClickListener(
                    boolean Encryptionflag, boolean recallFlag) {
                if (!recallFlag) {
                    if (Encryptionflag) {//加密处理
                        // DataOptUtil.SaveGroupData(0);
                        //  DataStruct.SEFF_USER_GROUP_OPT = 3;
                        FlashFunsViewPage();
                    } else {//解密处理
                        // DataOptUtil.SaveGroupData(0);
                        //    DataStruct.SEFF_USER_GROUP_OPT = 4;
                        FlashFunsViewPage();
                    }
//                    LoadingDialogFragment mvLoadingDialog = new LoadingDialogFragment();
//                    mvLoadingDialog.show(getFragmentManager(), "mvLoadingDialog");
                    /*刷新界面*/
                } else {  //清除数据


//                    SetEncryptionCleanDialogFragment setEncryptionCleanDialogFragment = new SetEncryptionCleanDialogFragment();
//                    setEncryptionCleanDialogFragment.show(getFragmentManager(), "setEncryptionCleanDialogFragment");
//                    setEncryptionCleanDialogFragment.OnSetEncryptionDialogCleanFragmentChangeListener(new SetEncryptionCleanDialogFragment.OnEncryptionCleanDialogFragmentClickListener() {
//
//                        @Override
//                        public void onEncryptionCleanClickListener(boolean EncryptionCleanflag) {
//                            System.out.println("BUG 当前的额只为" + EncryptionCleanflag);
//                            // TODO Auto-generated method stub
//                            if (EncryptionCleanflag) {


                    DataStruct.SEFF_USER_GROUP_OPT = 1;
                    DataOptUtil.setCleanData();
                    showLoadingDialog();
                    FlashFunsViewPage();
                }
            }
        });

    }


    private void ConnectClickEvent() {
        if (DataStruct.isConnecting) {//关闭蓝牙
            MacCfg.BOOL_CanLinkUART = false;
            DataStruct.isConnecting = false;
            DataStruct.ManualConnecting = true;
            ServiceOfCom.disconnectSet();
            setConnectState(false);
            ToastMsg(getString(R.string.offline_link_bt));
        } else {
            if (DataOptUtil.isComBluetoothType()) {
                if (mBluetoothAdapter != null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                        return;
                    }
                }
                setupChat();
            } else {
                ServiceOfCom.connectToDevice();
            }

        }
    }

    /**
     * 关机延时
     */
    private void showOFFDelay() {
        SetoffDelayTimeFragment setoffDelayTimeFragment = new SetoffDelayTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(setoffDelayTimeFragment.ST_DataVal, DataStruct.RcvDeviceData.SYS.offTime);
        setoffDelayTimeFragment.setArguments(bundle);
        setoffDelayTimeFragment.show(getFragmentManager(), "AboutDialogFragment");
        setoffDelayTimeFragment.OnSetOnClickDialogListener(new SetoffDelayTimeFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int Val, boolean boolClick) {
                if (boolClick) {
                    DataStruct.RcvDeviceData.SYS.offTime = Val;
                }
            }
        });
    }


    /**
     * 加密提示
     */
    private void showEncryPTDelay() {
//        encryptDialogFragment encryptDialogFragment = new encryptDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(encryptDialogFragment.ST_DataOPT, DataStruct.RcvDeviceData.SYS.OffTime);
//        encryptDialogFragment.setArguments(bundle);
//        encryptDialogFragment.show(getFragmentManager(), "AboutDialogFragment");
//        encryptDialogFragment.OnSetOnClickDialogListener(new encryptDialogFragment.SetOnClickDialogListener() {
//            @Override
//            public void onClickDialogListener(int Val, boolean boolClick) {
//
//            }
//        });
//        myDialog = new AlertIOSDialog(this).builder();
//        myDialog.setGone().setTitle(getResources().getString(R.string.warning)).setMsg(getResources().getString(R.string.Encrying_data)).setPositiveButton(getResources().getString(R.string.Sure), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }).show();

        Bundle bundle = new Bundle();
        bundle.putInt(AlertDialogFragment.ST_DataOPT, 1);
        bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.warning));
        bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.Encrying_data));

        if (alertDialogFragment == null) {
            alertDialogFragment = new AlertDialogFragment();
        }
        if (!alertDialogFragment.isAdded()) {
            alertDialogFragment.setArguments(bundle);
            alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
        }
        alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

            @Override
            public void onClickDialogListener(int type, boolean boolClick) {

            }
        });


    }





    private void backToMain() {

        MacCfg.CurPage_bottom = Define.UI_PAGE_HOME;

        getSupportFragmentManager()
                .beginTransaction()
                .show(mHome)
                .hide(mMixer)
                .hide(mOutputXover)
                .hide(mDelayAuto)
                .hide(mEQ)
                .commit()
        ;
        if(Define.bool_FunsPage){
            btn_back.setText(R.string.common_user);
            LLyout_Back.setBackgroundResource(R.drawable.chs_gernal_normal);
            LY_Buttom.setVisibility(View.VISIBLE);
        }else{
            btn_back.setText(R.string.Technicians);
            LLyout_Back.setBackgroundResource(R.drawable.chs_gernal_press);
            LY_Buttom.setVisibility(View.GONE);
        }

       // LLyout_Back.setVisibility(View.GONE);
       // LY_Buttom.setVisibility(View.VISIBLE);
    }

    private void MenuExit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
    }

    private void initBottomBar() {
        LY_Buttom = (LinearLayout) findViewById(R.id.id_rlyout_bottom);
        //导航按键
        RLyout_ButtomItem[0] = (LinearLayout) findViewById(R.id.id_rlyout_0);
        RLyout_ButtomItem[1] = (LinearLayout) findViewById(R.id.id_rlyout_1);
        RLyout_ButtomItem[2] = (LinearLayout) findViewById(R.id.id_rlyout_2);
        RLyout_ButtomItem[3] = (LinearLayout) findViewById(R.id.id_rlyout_3);
        RLyout_ButtomItem[4] = (LinearLayout) findViewById(R.id.id_rlyout_4);

        //选中背景
        IV_ButtomSel[0] = (ImageView) findViewById(R.id.id_iv_item_bg_0);
        IV_ButtomSel[1] = (ImageView) findViewById(R.id.id_iv_item_bg_1);
        IV_ButtomSel[2] = (ImageView) findViewById(R.id.id_iv_item_bg_2);
        IV_ButtomSel[3] = (ImageView) findViewById(R.id.id_iv_item_bg_3);
        IV_ButtomSel[4] = (ImageView) findViewById(R.id.id_iv_item_bg_4);
        IV_ButtomSel[5] = (ImageView) findViewById(R.id.id_iv_item_bg_5);
        IV_ButtomSel[6] = (ImageView) findViewById(R.id.id_iv_item_bg_6);
        IV_ButtomSel[7] = (ImageView) findViewById(R.id.id_iv_item_bg_7);
        //项目图案
        IV_ButtomItem[0] = (ImageView) findViewById(R.id.id_iv_item_img_0);
        IV_ButtomItem[1] = (ImageView) findViewById(R.id.id_iv_item_img_1);
        IV_ButtomItem[2] = (ImageView) findViewById(R.id.id_iv_item_img_2);
        IV_ButtomItem[3] = (ImageView) findViewById(R.id.id_iv_item_img_3);
        IV_ButtomItem[4] = (ImageView) findViewById(R.id.id_iv_item_img_4);
        IV_ButtomItem[5] = (ImageView) findViewById(R.id.id_iv_item_img_5);
        IV_ButtomItem[6] = (ImageView) findViewById(R.id.id_iv_item_img_6);
        IV_ButtomItem[7] = (ImageView) findViewById(R.id.id_iv_item_img_7);
        //项目文字
        TV_ButtomItemName[0] = (TextView) findViewById(R.id.id_tv_item_name_0);
        TV_ButtomItemName[1] = (TextView) findViewById(R.id.id_tv_item_name_1);
        TV_ButtomItemName[2] = (TextView) findViewById(R.id.id_tv_item_name_2);
        TV_ButtomItemName[3] = (TextView) findViewById(R.id.id_tv_item_name_3);
        TV_ButtomItemName[4] = (TextView) findViewById(R.id.id_tv_item_name_4);
        TV_ButtomItemName[5] = (TextView) findViewById(R.id.id_tv_item_name_5);
        TV_ButtomItemName[6] = (TextView) findViewById(R.id.id_tv_item_name_6);
        TV_ButtomItemName[7] = (TextView) findViewById(R.id.id_tv_item_name_7);

        initBottomItemClick(2);

        for (int i = 0; i < ButtomItemMaxUse; i++) {
//            TV_ButtomItemName[i].setVisibility(View.GONE);
            RLyout_ButtomItem[i].setVisibility(View.VISIBLE);
            RLyout_ButtomItem[i].setTag(i);
            RLyout_ButtomItem[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int sel = (int) v.getTag();


                    BottomItemClick(sel);

                }
            });
        }

    }

    void initBottomItemClick(int sel) {
        IV_ButtomItem[0].setImageDrawable(getResources().getDrawable(R.drawable.setdelay_sel));
        IV_ButtomItem[1].setImageDrawable(getResources().getDrawable((R.drawable.output_sel)));
        IV_ButtomItem[2].setImageDrawable(getResources().getDrawable((R.drawable.home_sel)));
        IV_ButtomItem[3].setImageDrawable(getResources().getDrawable((R.drawable.eq_sel)));
        IV_ButtomItem[4].setImageDrawable(getResources().getDrawable((R.drawable.weight_sel)));

        TV_ButtomItemName[0].setText(getResources().getString(R.string.SetDelay));
        TV_ButtomItemName[1].setText(getResources().getString(R.string.Output));
        TV_ButtomItemName[2].setText(getResources().getString(R.string.Home));
        TV_ButtomItemName[3].setText(getResources().getString(R.string.EQ));
        TV_ButtomItemName[4].setText(getResources().getString(R.string.Weight));
        //选择背景
        for (int i = 0; i < ButtomItemMaxUse; i++) {
            IV_ButtomSel[i].setVisibility(View.GONE);
            TV_ButtomItemName[i].setTextColor(getResources().getColor(R.color.text_color_Bottom_Bar_normal));
        }
        //IV_ButtomSel[sel].setVisibility(View.VISIBLE);
        TV_ButtomItemName[sel].setTextColor(getResources().getColor(R.color.text_color_Bottom_Bar_press));
        switch (sel) {
            case 0:
                TV_ViewPageName.setText(getResources().getString(R.string.SetDelay));
                IV_ButtomItem[sel].setImageDrawable(getResources().getDrawable((R.drawable.setdelay)));
                break;
            case 1:
                TV_ViewPageName.setText(getResources().getString(R.string.Output));
                IV_ButtomItem[sel].setImageDrawable(getResources().getDrawable((R.drawable.output)));
                break;
            case 2:
                TV_ViewPageName.setText(getResources().getString(R.string.Home));
                IV_ButtomItem[sel].setImageDrawable(getResources().getDrawable((R.drawable.home)));
                break;
            case 3:
                TV_ViewPageName.setText(getResources().getString(R.string.EQ));
                IV_ButtomItem[sel].setImageDrawable(getResources().getDrawable((R.drawable.eq)));
                break;
            case 4:
                TV_ViewPageName.setText(getResources().getString(R.string.Weight));
                IV_ButtomItem[sel].setImageDrawable(getResources().getDrawable((R.drawable.weight)));
                break;
            default:
                break;

        }
    }

    private void BottomItemClick(int sel) {

        initBottomItemClick(sel);
        switch (sel) {
            case 0:

                MacCfg.CurPage_bottom = Define.UI_PAGE_DELAY;
                if (mDelayAuto == null) {
                    mDelayAuto = new DelayAuto_Fragment();
                }
                mDelayAuto.FlashPageUI();
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mDelayAuto)
                        .hide(mEQ)
                        .hide(mHome)
                        .hide(mMixer)
                        .hide(mOutputXover)
                        .commit()
                ;
                break;


            case 1:

                MacCfg.CurPage_bottom = Define.UI_PAGE_OUTPUT;
                if (mOutputXover == null) {
                    mOutputXover = new OutputXover_Fragment();
                }
                mOutputXover.FlashPageUI();
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mOutputXover)
                        .hide(mDelayAuto)
                        .hide(mHome)
                        .hide(mMixer)
                        .hide(mEQ)
                        .commit()
                ;
                break;

            case 2:
                MacCfg.CurPage_bottom = Define.UI_PAGE_HOME;
                if (mHome == null) {
                    mHome = new Home_Fragment();
                }
                mHome.FlashPageUI();
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mHome)
                        .hide(mOutputXover)
                        .hide(mDelayAuto)
                        .hide(mMixer)
                        .hide(mEQ)
                        .commit()
                ;
                break;
            case 3:
                MacCfg.CurPage_bottom = Define.UI_PAGE_EQ;
                if (mEQ == null) {
                    mEQ = new EQ_Fragment();
                }
                mEQ.FlashPageUI();
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mEQ)
                        .hide(mDelayAuto)
                        .hide(mOutputXover)
                        .hide(mHome)
                        .hide(mMixer)
                        .commit()
                ;
                break;
            case 4:
                MacCfg.CurPage = Define.UI_PAGE_MIXER;
                if (mMixer == null) {
                    mMixer = new MixerFragment();
                }
                mMixer.FlashPageUI();
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMixer)
                        .hide(mDelayAuto)
                        .hide(mOutputXover)
                        .hide(mEQ)
                        .hide(mHome)
                        .commit()
                ;
                break;

            default:
                break;
        }
    }

    private boolean checkHaveSEFFileLoad() {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri != null) {
            String path = uri.getPath();
            if (uriPath != null && uriPath == path) {
                return false;
            }
            uriPath = path;
            System.out.println("BUG onCreate checkHaveSEFFileLoad path:" + path);
            MacCfg.LOAD_SEFF_FROM_OTHER_PathName = path;
            return true;
            //DataOptUtil.loadSEFFileDownload(mContext,path);
            /*
            Intent intenl=new Intent();
            intenl.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intenl.putExtra("msg", Define.BoardCast_Load_LocalJson);
            intenl.putExtra("filePath", path);
            sendBroadcast(intent);
            return true;
            */
        }
        return false;
    }

    private void FlashFunsViewPage() {


        if (mDelayAuto != null) {
            mDelayAuto.FlashPageUI();
        }
        if (mEQ != null) {
            mEQ.FlashPageUI();
        }

        if (mOutputXover != null) {
            mOutputXover.FlashPageUI();
        }
        if (mMixer != null) {
            mMixer.FlashPageUI();
        }
        if (mHome != null) {
            mHome.FlashPageUI();
        }
        flashFreqType();
    }

    private void FlashInputsource() {
        if (mHome != null) {
            mHome.flashInputsource();
        }
    }

    private void flashFreqType() {

    }

    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            System.out.println("BUG CHS_Broad_FLASHUI_BroadcastReceiver msg:" + msg);
            if (msg.equals(Define.BoardCast_FlashUI_ConnectState)) {

                boolean res = intent.getBooleanExtra("state", false);
                System.out.println("BUG BoardCast_FlashUI_ConnectState state:" + res);
                setConnectState(res);
                if (!res) {

                    for (int i = 0; i <= Define.MAX_GROUP; i++) {
                        for (int j = 0; j < 16; j++) {
                            DataStruct.RcvDeviceData.SYS.UserGroup[i][j] = 0; // 用户名初始值
                        }
                    }
                }
            } else if (msg.equals(Define.BoardCast_FlashUI_AllPage)) {
                ToastMsg(getResources().getString(R.string.SynDataSucess));
                FlashFunsViewPage();
                FlashLinkState();
                if (MacCfg.bool_HaveSEFFUpdate && DataStruct.isConnecting) {
                    MacCfg.bool_HaveSEFFUpdate = false;
                    System.out.println("BUG MacCfg.bool_HaveSEFFUpdate" + MacCfg.bool_HaveSEFFUpdate);
                    showUpdateSEFFDialog();
                    ToastMsg(getResources().getString(R.string.SynDataSucess));
                }
                ;
//                if (DataStruct.RcvDeviceData.SYS.Safety != 0) {
//                    DataStruct.RcvDeviceData.EFF.Mic_mute = 0;
//                    if (mHome != null) {
//                        mHome.flashMKFUI();
//                    }
//                    showSaftMsgDialog();
//                }
            } else if (msg.equals(Define.BoardCast_FlashUI_Page)) {
                // ToastMsg(getResources().getString(R.string.SynDataSucess));
                //  ToastMsg(getResources().getString(R.string.SynDataSucess));

                //   if(MacCfg.bool_HaveSEFFUpdate && DataStruct.isConnecting){
                //  ToastMsg(getResources().getString(R.string.SynDataSucess));
                // }
                FlashFunsViewPage();
                FlashLinkState();
                if (DataStruct.RcvDeviceData.SYS.Safety != 0) {
                    DataStruct.RcvDeviceData.EFF.Mic_mute = 0;
                    if (mHome != null) {
                        mHome.flashMKFUI();
                    }
                }

//                if(mEFF != null){
//                    mEFF.FlashPageUI();
//                }

                //FlashLinkState();
//                if(MacCfg.bool_HaveSEFFUpdate && DataStruct.isConnecting){
//                    MacCfg.bool_HaveSEFFUpdate = false;
//                    showUpdateSEFFDialog();
//                    ToastMsg(getResources().getString(R.string.SynDataSucess));
//                }
                // ;
//                if(DataStruct.RcvDeviceData.SYS.Safety != 0){
//                    DataStruct.RcvDeviceData.EFF.Mic_mute = 0;
//                    if(mHome != null){
//                        mHome.flashMKFUI();
//                    }
//                  //  showSaftMsgDialog();
//                }
            } else if (msg.equals(Define.BoardCast_FlashUI_DeviceVersionsErr)) {
                ToastMsg(getResources().getString(
                        R.string.device)
                        + ":"
                        + DataStruct.DeviceVerString
                        + "\n"
                        + getResources().getString(R.string.apps)
                        + ":"
                        + MacCfg.App_versions/*getResources().getString(R.string.app_versions)*/
                        + "\n"
                        + getResources().getString(
                        R.string.version_error));


            } else if (msg.equals(Define.BoardCast_FlashUI_ShowLoading)) {
                showLoadingDialog();
            } else if (msg.equals(Define.BoardCast_FlashUI_IninLoadUI)) {
                //InitLoadFunsViewPage();
            } else if (msg.equals(Define.BoardCast_Load_LocalJson)) {
//                if(!isConnecting){
//                    ToastMsg(getResources().getString(R.string.off_line_mode));
//                    return;
//                }
                final String filePath = intent.getExtras().get("filePath").toString();
                DataOptUtil.loadSEFFileDownload(mContext, filePath);
            } else if (msg.equals(Define.BoardCast_FlashUI_InputSource)) {
                FlashInputsource();
            } else if (msg.equals(Define.BoardCast_FlashUI_EFFPageVol)) {
                FlashInputsource();

            } else if (msg.equals(Define.BoardCast_FlashUI_ShowSucessMsg)) {
                ToastMsg(getResources().getString(R.string.SynDataSucess));
            } else if (msg.equals(Define.BoardCast_FlashUI_SYSTEM_DATA)) {
                if (mHome != null) {
                    mHome.flashMasterVol();
                    mHome.flashInputsource();
//                    mHome.flashMixersource();
//                    mHome.flashTextColor();
                }
            } else if (msg.equals(Define.BoardCast_FlashUI_ConnectStateOFMsg)) {
                ToastMsg(getResources().getString(R.string.LinkOfMsg));
                setConnectState(false);
                if (DataStruct.isConnecting) {
                    ServiceOfCom.disconnectSet();
                }

            } else if (msg.equals(Define.BoardCast_FlashUI_FlashSoundStatus)) {
                flashBtnTMsg();
            } else if (msg.equals(Define.BoardCast_FlashUI_ShowMsg)) {
                String res = intent.getStringExtra("String");

                ToastMsg(res);
            } else if (msg.equals(Define.BoardCast_FlashUI_FUNSPageChange)) {
                flashFreqType();
            }
        }
    }

    private void flashBtnTMsg() {
//        if(MacCfg.BOOL_SoundStatues){
//            BtnTMsg.setVisibility(View.VISIBLE);
//        }else {
//            BtnTMsg.setVisibility(View.GONE);
//        }
    }

    private boolean isHaveAd() {
//        return true;
        SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
        String AdID = sp.getString("AdID", "0");
        if ((DataStruct.mDSPAi.Ad_Status.equals("1"))
                && (!(DataStruct.mDSPAi.AdID.equals(AdID)))
                && (DataStruct.mDSPAi.Ad_Image_Path.length() > 0)) {

            if (UpdateUtil.isNetworkAvalible(mContext)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void showAdDialog() {
        if (mADDialogFragment == null) {
            mADDialogFragment = new ADDialogFragment();
        }
        if (!mADDialogFragment.isAdded()) {
            mADDialogFragment.show(getFragmentManager(), "mADDialogFragment");
        }
        mADDialogFragment.OnSetOnClickDialogListener(new ADDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                CheckAndOpenBluetooth();
            }
        });
    }

    /**
     * 接收文件
     */
    private void checkUriFile() {
        //Intent intent = getIntent();

        Uri uri = null;
        if (getIntent().getData() != null) {
            uri = getIntent().getData();
        }
        System.out.println("BUG onCreate---------------- path:" + uri);
        try {
            if (uri != null) {
                String path = uri.getPath();
                String url = uri.toString();
                System.out.println("BUG 地址tt为" + url + "其他的值为" + isFront + "地址" + uriPath + "正确与否" + (uriPath == url));


                if ((uriPath == url)) {
//
                } else {
                    uriPath = url;
                    if (url.contains("content://")) { //文件管理和微信路径都是content://开头,QQ是file:
                        if (!url.contains(".fileprovider/")) {//微信路径都是用fileprovider提供的,所以如果有fileprovider就是微信,其他是文件管理
                            path = FileUtil.getFilePathFromContentUri(uri, (Activity) mContext);
                            System.out.println("BUG 未进的这里 地址为"+path);
                        } else {
                            path = FileUtil.getFPUriToPath(mContext, uri);
                            System.out.println("BUG 进的这里 地址为"+path);
                        }
                    }


                    MacCfg.LOAD_SEFF_FROM_OTHER_PathName = path;
                    MacCfg.bool_HaveSEFFUpdate = true;
                    ToastMsg(getString(R.string.SSM_REC));

                    if (DataOptUtil.addSEFFileList(this, path)) {
                        System.out.println("BUG  加入集合中");
                        if (MacCfg.bool_HaveSEFFUpdate && DataStruct.isConnecting) {
                            MacCfg.bool_HaveSEFFUpdate = false;
                            System.out.println("BUG MacCfg.bool_HaveSEFFUpdate" + MacCfg.bool_HaveSEFFUpdate);
                            showUpdateSEFFDialog();
                            setIntent(null);
                            ToastMsg(getResources().getString(R.string.SynDataSucess));
                        }

                        ToastMsg(getString(R.string.SSM_REC));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("BUG 错误" + e);
            e.printStackTrace();
        }
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

//            activity.requestPermissions(new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, REQUEST_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            return false;
        }

        return true;
    }

    /**
     * 同步文件
     */
    public void scanDirAsync() {
        String dir = Environment.getExternalStorageDirectory().toString();
        System.out.println("BUG scanDirAsync dir:" + dir);
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
        if (dir != null) {
            if (Build.VERSION.SDK_INT <= 20) {
                scanIntent.setData(Uri.fromFile(new File(dir)));
            } else {
                scanIntent.setData(FileProvider.getUriForFile(mContext,
                        mContext.getApplicationContext().getPackageName() + ".provider", new File(dir)));
            }
            sendBroadcast(scanIntent);
        }

    }

    //提示有音效文件更新对话框
    private void showUpdateSEFFDialog() {

        Bundle bundle = new Bundle();
        bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
        bundle.putString(AlertDialogFragment.ST_SetTitle, getResources().getString(R.string.SSM_Title));
        bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.SSM_MSG));

        if (alertDialogFragment == null) {
            alertDialogFragment = new AlertDialogFragment();
        }
        if (!alertDialogFragment.isAdded()) {
            alertDialogFragment.setArguments(bundle);
            alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
        }
        alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (MacCfg.LOAD_SEFF_FROM_OTHER_PathName != null) {
                    DataOptUtil.loadSEFFileDownload(mContext, MacCfg.LOAD_SEFF_FROM_OTHER_PathName);
                }
            }
        });

    }


    private void sentMstToService(int what, int arg1, int arg2) {
        if (mActivityMessenger == null) {
            mActivityMessenger = new Messenger(handler);
        }


        //创建消息
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;

        //设定消息要回应的Messenger
        message.replyTo = mActivityMessenger;
        handler.removeMessages(what);
        try {
            //通过ServiceMessenger将消息发送到Service中的Handler
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setConnectState(boolean connect) {
        if (connect) {
//            IV_ButtomItem[0].setBackgroundResource(R.drawable.chs_tabbar_connect_press);
//            TV_ButtomItemName[0].setText(getResources().getString(R.string.ST_BT_CONNECTED));
//            TV_ButtomItemName[0].setTextColor(getResources().getColor(R.color.text_color_connected));
//            B_ConnectState.setBackgroundResource(R.drawable.chs_constatus_all_selector);
            B_ConnectState.setImageDrawable(getResources().getDrawable(R.drawable.chs_btn_connect_press_selector));
//            TV_Connect.setVisibility(View.INVISIBLE);
//            TV_Connect1.setVisibility(View.VISIBLE);
//            B_ConnectState.setText(getResources().getString(R.string.ST_BT_CONNECTED));
            //TV_Connect.setTextColor(getResources().getColor(R.color.text_color_connected));
        } else {
//            IV_ButtomItem[0].setBackgroundResource(R.drawable.chs_tabbar_connect_normal);
//            TV_ButtomItemName[0].setText(getResources().getString(R.string.ST_BT_DISCONNECT));
//            TV_ButtomItemName[0].setTextColor(getResources().getColor(R.color.text_color_disconnected));
//            B_ConnectState.setBackgroundResource(R.drawable.chs_constatus_n_selector);
            B_ConnectState.setImageDrawable(getResources().getDrawable(R.drawable.chs_btn_connect_normal_selector));
            if (mHome != null) {
                mHome.SetUserGroupNameDefault();
            }
//            TV_Connect.setVisibility(View.VISIBLE);
//            TV_Connect1.setVisibility(View.INVISIBLE);
//            B_ConnectState.setText(getResources().getString(R.string.ST_BT_DISCONNECT));
//            TV_Connect.setTextColor(getResources().getColor(R.color.text_color_disconnected));
        }
    }

    private void showLoadingDialog() {
        if (mContext == null) {
            return;
        }
        if (!isFront) {
            return;
        }
        if (mLoadingDialogFragment == null) {
            mLoadingDialogFragment = new LoadingDialogFragment();
        }
        if (!mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.show(getFragmentManager(), "mLoadingDialogFragment");
        }
    }



    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void ToastMsg(String Msg, boolean longs) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            if (longs) {
                mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_LONG);
            } else {
                mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
            }
        }
        mToast.show();
    }

    /**
     * Activity端的Handler处理Service中的消息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("BUG handleMessage From Service msg.what:" + msg.what
                    + ",msg.arg1=" + msg.arg1 + ",msg.arg2=" + msg.arg2);
            if (msg.what == ServiceOfCom.WHAT_IS_FlashUI_ConnectStateBtn) {

            }
        }
    };
    /**
     * Service绑定状态的监听
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取Service端的Messenger
            mServiceMessenger = new Messenger(service);
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /*********************************************************************/
    /*******************  蓝牙SPP-LE   通信     **********************/
    /*********************************************************************/
    private void CheckAndOpenBluetooth() {
        if (DataOptUtil.isComBluetoothType()) {
            // 判断蓝牙是否可用
            if (mBluetoothAdapter == null) {
                ToastMsg("蓝牙是不可用的");
                //Exit();
                return;
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                setupChat();
//            getCurConnectBluetoothList();
//            currentBluetoothDevice = DataOptUtil.getBluetoothConnectInfo(mContext);
//            if(currentBluetoothDevice!=null){
//                if(DataOptUtil.getAutoConnect(mContext)){
//                    contectBuleDevices(currentBluetoothDevice);
//                }else {
//                    setupChat();
//                }
//            }else {
//                setupChat();
//            }
            }

        } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART) {
            handlerMainVal.sendEmptyMessageDelayed(3, 3000);
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("BUG onActivityResult requestCode=" + requestCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // 当DeviceListActivity返回与设备连接的消息
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }
                break;
            case REQUEST_ENABLE_BT:
                // 判断蓝牙是否启用
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                }
                break;
            case Define.ActivityResult_MusicPage_Back:

                break;

            case REQUEST_WirteSettings:
                // 当DeviceListActivity返回与设备连接的消息
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }
                if (isGrantExternalRW((Activity) mContext)) {
                        initLoadDialog();
                }
                break;
        }
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            System.out.println("BUG onActivityResult REQUEST_CODE_PERMISSIONS resultCode=PERMISSIONS_DENIED");
            //Exit();
        }

    }


    private void setupChat() {
        if (!DataOptUtil.isComBluetoothType()) {
            return;
        }
        mayRequestLocation();
    }

    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(mContext, "Android 6.0 Open Location", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOCATION);
            } else {
                if (mBluetoothAdapter != null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    } else {
                        Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    }
                }

            }
        } else {
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                    Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            } else {
                Toast.makeText(mContext, mContext.getText(R.string.RefusedUseBluetooth), Toast.LENGTH_LONG).show();
               // MenuExit();
                // The user disallowed the requested permission.
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initLoadDialog();
            } else {
                ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.quitLackPer));
               // MenuExit();
            }
        }

    }

    /**
     * 开始连接蓝牙设备
     */
    private void contectBuleDevices(BluetoothDevice device) {
        currentBluetoothDevice = device;
        if (currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HD_)) {
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HD_, currentBluetoothDevice);
        } else if (currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_CCS)) {
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_CCS, currentBluetoothDevice);
        } else if (currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_Play)) {
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_Play, currentBluetoothDevice);
        } else if (currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HDS)) {
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HDS, currentBluetoothDevice);
        }
    }

    private void sendMsgToConnectBluetooth(String type, BluetoothDevice device) {
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_ConnectToSomeoneDevice);
        intentw.putExtra("type", type);
        intentw.putExtra("address", device.getAddress());
        intentw.putExtra("device", device.getName());
        mContext.sendBroadcast(intentw);
        //exit();
    }

    private void getCurConnectBluetoothList() {
        MacCfg.LCBT.clear();

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Log.i(TAG, "connectedBT BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                Log.i(TAG, "connectedBT devices.size():" + devices.size());

                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) {
                        //保存已经连接的蓝牙
                        if (isCHSBluetooth(device.getName())) {
                            MacCfg.LCBT.add(device);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCHSBluetooth(String deviceName) {
        if ((deviceName.contains(Define.BT_Paired_Name_DSP_HD_))
                || (deviceName.contains(Define.BT_Paired_Name_DSP_CCS))
                || (deviceName.contains(Define.BT_Paired_Name_DSP_HDS))
                || (deviceName.contains(Define.BT_Paired_Name_DSP_Play))) {
            if (deviceName.contains("DSP Play ble")) {
                return false;
            }
            return true;
        }
        return false;
    }
    /*********************************************************************/
    /*******************           锁屏界面          **********************/
    /*********************************************************************/

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;


        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 锁屏
                System.out.println("BUG ScreenBroadcastReceiver 锁屏");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // 解锁
                System.out.println("BUG ScreenBroadcastReceiver 解锁");
            }
        }
    }

    private void startScreenBroadcastReceiver() {
        mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);

    }
    /*********************************************************************/
    /*******************          权限配置页面         **********************/
    /*********************************************************************/
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE_PERMISSIONS, PERMISSIONS);
    }

    /*********************************************************************/
    /*******************           APP升级版本          **********************/
    /*********************************************************************/
    /**
     * 初始化app更新广播
     */
    private void initUpdata() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        filter.addCategory("android.intent.category.DEFAULT");
        mDownLoadCompleteReceiver = new DownLoadCompleteReceiver();
        registerReceiver(mDownLoadCompleteReceiver, filter);
    }

    //showUpdateConfirmDialog(mContext, DataStruct.mDSPAi.Upgrade_Instructions);
    private boolean checkUpdateApp() {
        boolean boolUpdate = false;
        if (DataStruct.mDSPAi.Upgrade_Status.equals("1")) {
            int curV = MacCfg.App_versions.charAt(7);
            int getV = DataStruct.mDSPAi.Upgrade_Latest_Version.charAt(7);
            String cst = MacCfg.App_versions.substring(9, 11);
            String gst = DataStruct.mDSPAi.Upgrade_Latest_Version.substring(9, 11);
            int curCV = Integer.parseInt(cst);
            int getCV = Integer.parseInt(gst);

            if ((getV > curV) || (getCV > curCV)) {
                boolUpdate = true;
            }
        }
        return boolUpdate;
    }










/*********************************************************************/
    /***************************    Link UI    ****************************/
    /*********************************************************************/
    /**
     * 联调内容
     */
    private void flashLinkDataUI(int Tpe) {
        MacCfg.UI_Type = Tpe;
        DataOptUtil.syncLinkData(Tpe);
        if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS) {//前声场，后声场，超低的联调，单独分开
            flashLinkUI_LINKMODE_FRS(Tpe);
        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS_A) {//前声场，后声场，超低的联调，一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR) {//前声场，后声场，单独分开

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR_A) {//前声场，后声场，中置超低的联调，全部一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE) {//设置通道输出类型后的联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE_S) {//设置通道输出类型后的联调，可联机保存

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_AUTO) {//任意联调，每个通道可以单独联调，可联机保存

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_LEFTRIGHT) {//固定两两通道联调
            flashLinkUI_LINKMODE_LEFTRIGHT(Tpe);
        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR2A) {//前声场，后声场，一起两两联调

        }

    }

    private void flashLinkUI_LINKMODE_LEFTRIGHT(int Tpe) {
        int LinkChannel = 0;
        if (!MacCfg.bool_OutChLink) {
            return;//没有联调
        }

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX / 2; i++) {
            if (MacCfg.OutputChannelSel == i * 2) {
                LinkChannel = i * 2 + 1;
                break;
            } else if (MacCfg.OutputChannelSel == i * 2 + 1) {
                LinkChannel = i * 2;
                break;
            }
        }

        if (MacCfg.UI_Type == Define.UI_OutVal) {

        } else if (MacCfg.UI_Type == Define.UI_OutMute) {
        } else if (MacCfg.UI_Type == Define.UI_OutPolar) {

        }
    }

    private void flashLinkUI_LINKMODE_FRS(int Tpe) {
        int LinkChannel = 0;
        if ((MacCfg.ChannelConFLR == 0) && (MacCfg.ChannelConRLR == 0) && (MacCfg.ChannelConSLR == 0)) {
            return;//没有联调
        }
        if ((MacCfg.ChannelConFLR == 1) && ((MacCfg.OutputChannelSel == 0) || (MacCfg.OutputChannelSel == 1))) {
            if (MacCfg.OutputChannelSel == 1) {
                LinkChannel = 0;
            } else {
                LinkChannel = 1;
            }
        }

        if ((MacCfg.ChannelConRLR == 1) && ((MacCfg.OutputChannelSel == 2) || (MacCfg.OutputChannelSel == 3))) {
            if (MacCfg.OutputChannelSel == 2) {
                LinkChannel = 3;
            } else {
                LinkChannel = 2;
            }
        }

        if ((MacCfg.ChannelConSLR == 1) && ((MacCfg.OutputChannelSel == 4) || (MacCfg.OutputChannelSel == 5))) {
            if (MacCfg.OutputChannelSel == 4) {
                LinkChannel = 5;
            } else {
                LinkChannel = 4;
            }
        }
        if (MacCfg.UI_Type == Define.UI_OutVal) {

        } else if (MacCfg.UI_Type == Define.UI_OutMute) {
        } else if (MacCfg.UI_Type == Define.UI_OutPolar) {

        }


    }

    private void setLinkBtnState(boolean Link) {
        if (Link) {
//            B_Link.setText(getString(R.string.UnLink));
//            B_Link.setTextColor(getResources().getColor(R.color.output_Link_press_text_color));


            //BtnName.setTextColor(getResources().getColor(R.color.output_Link_disable_text_color));
        } else {
//            B_Link.setText(getString(R.string.Link));
//            B_Link.setTextColor(getResources().getColor(R.color.output_Link_normal_text_color));
//
            //BtnName.setTextColor(getResources().getColor(R.color.output_Link_normal_text_color));
        }
    }


    private void checkLinkBtn() {
        if ((MacCfg.ChannelConFLR == 0)
                && (MacCfg.ChannelConRLR == 0)
                && (MacCfg.ChannelConSLR == 0)
        ) {
            setLinkBtnState(false);
        } else {
            setLinkBtnState(true);
        }


    }
    /*********************************************************************/
    /***************************    Link     ****************************/
    /*********************************************************************/




    //设置通道输出类型后的联调
    void _LINKMODE_SPKTYPE_Dialog() {
        if (!MacCfg.bool_OutChLink) {
            if (CheckChannelCanLink() > 0) {
                LinkDataCoypLeftRight_DialogFragment mLinkDataCoypLeftRightDialogFragment = new LinkDataCoypLeftRight_DialogFragment();
                mLinkDataCoypLeftRightDialogFragment.show(getFragmentManager(), "mLinkDataCoypLeftRightDialogFragment");
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
                        FlashFunsViewPage();

                        //刷新界面
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
                        intentw.putExtra("state", true);


                    }
                });
            } else {
                ToastMsg(getString(R.string.NoLinkLR));
            }
        } else {
            //不联调
            setLinkState_Unlink();
        }
    }

    //设置通道输出类型后的联调，可联机保存
    void _LINKMODE_SPKTYPE_S_Dialog() {
        _LINKMODE_SPKTYPE_Dialog();
    }

    //任意联调，每个通道可以单独联调，可联机保存
    void _LINKMODE_AUTO_Dialog() {

    }

    //固定两两通道联调
    void _LINKMODE_LEFTRIGHT_Dialog() {
        if (!MacCfg.bool_OutChLink) {
            LinkDataCoypLeftRight_DialogFragment mLinkDataCoypLeftRightDialogFragment = new LinkDataCoypLeftRight_DialogFragment();
            mLinkDataCoypLeftRightDialogFragment.show(getFragmentManager(), "mLinkDataCoypLeftRightDialogFragment");
            mLinkDataCoypLeftRightDialogFragment.OnSetOnClickDialogListener(new LinkDataCoypLeftRight_DialogFragment.SetOnClickDialogListener() {
                @Override
                public void onClickDialogListener(int type, boolean boolClick) {
                    MacCfg.bool_OutChLeftRight = boolClick;
                    //通道数据的复制
                    int Dfrom = 0, Dto = 0;

                    if (MacCfg.bool_OutChLeftRight) {
                        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX / 2; i++) {
                            DataOptUtil.channelDataCopy(0 + i * 2, 1 + i * 2);
                        }
                    } else {
                        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX / 2; i++) {
                            DataOptUtil.channelDataCopy(1 + i * 2, 0 + i * 2);
                        }
                    }

                    //刷新界面
//                    Intent intentw=new Intent();
//                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                    intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
//                    intentw.putExtra("state", true);

                    FlashFunsViewPage();

                    setLinkState_Link();
                }
            });
        } else {
            setLinkState_Unlink();
        }

    }

    //前声场，后声场，一起两两联调
    void _LINKMODE_FR2A_Dialog() {

    }


    //////////////////////

    //检查可设置联调的通道
    private int CheckChannelCanLink() {
        int channelNameNum = 0;
        int channelNameNumEls = 0;
        int res = 0;
        int inc = 0;
        int i = 0, j = 0;
        MacCfg.ChannelLinkCnt = 0;
        for (i = 0; i < MacCfg.MaxOupputNameLinkGroup; i++) {
            for (j = 0; j < 2; j++) {
                MacCfg.ChannelLinkBuf[i][j] = Define.EndFlag;
            }
        }
        for (i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            channelNameNum = GetChannelNum(i);
            if (((channelNameNum >= 0) && (channelNameNum <= 18)) || (channelNameNum == 22) || (channelNameNum == 23)) {
                for (j = i + 1; j < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; j++) {
                    channelNameNumEls = GetChannelNum(j);
//	    			channelNameNumEls=GetChannelNum(B_OutName[j].getText().toString());
                    if ((channelNameNum >= 1) && (channelNameNum <= 6) &&
                            (channelNameNumEls >= 7) && (channelNameNumEls <= 12)) {
                        res = channelNameNumEls - channelNameNum;
                        if (res == 6) {
                            MacCfg.ChannelLinkBuf[inc][0] = i;
                            MacCfg.ChannelLinkBuf[inc][1] = j;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 7) && (channelNameNum <= 12) &&
                            (channelNameNumEls >= 1) && (channelNameNumEls <= 6)) {
                        res = channelNameNum - channelNameNumEls;
                        if (res == 6) {
                            MacCfg.ChannelLinkBuf[inc][0] = j;
                            MacCfg.ChannelLinkBuf[inc][1] = i;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 13) && (channelNameNum <= 15) &&
                            (channelNameNumEls >= 16) && (channelNameNumEls <= 18)) {
                        res = channelNameNumEls - channelNameNum;
                        if (res == 3) {
                            MacCfg.ChannelLinkBuf[inc][0] = i;
                            MacCfg.ChannelLinkBuf[inc][1] = j;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 16) && (channelNameNum <= 18) &&
                            (channelNameNumEls >= 13) && (channelNameNumEls <= 15)) {
                        res = channelNameNum - channelNameNumEls;
                        if (res == 3) {
                            MacCfg.ChannelLinkBuf[inc][0] = j;
                            MacCfg.ChannelLinkBuf[inc][1] = i;
                            ++inc;
                        }
                    } else if ((channelNameNum == 22) && (channelNameNumEls == 23)) {
                        MacCfg.ChannelLinkBuf[inc][0] = i;
                        MacCfg.ChannelLinkBuf[inc][1] = j;
                        ++inc;
                    } else if ((channelNameNum == 23) && (channelNameNumEls == 22)) {
                        MacCfg.ChannelLinkBuf[inc][0] = j;
                        MacCfg.ChannelLinkBuf[inc][1] = i;
                        ++inc;
                    }
                    //System.out.println("CH_NAME res:"+res);
                }
            }

        }
        MacCfg.ChannelLinkCnt = inc;

        return inc;
    }

    private int GetChannelNum(int channel) {
//        MacCfg.ChannelNumBuf[0]= DataStruct.RcvDeviceData.SYS.out1_spk_type;
//        MacCfg.ChannelNumBuf[1]= DataStruct.RcvDeviceData.SYS.out2_spk_type;
//        MacCfg.ChannelNumBuf[2]= DataStruct.RcvDeviceData.SYS.out3_spk_type;
//        MacCfg.ChannelNumBuf[3]= DataStruct.RcvDeviceData.SYS.out4_spk_type;
//        MacCfg.ChannelNumBuf[4]= DataStruct.RcvDeviceData.SYS.out5_spk_type;
//        MacCfg.ChannelNumBuf[5]= DataStruct.RcvDeviceData.SYS.out6_spk_type;
//        MacCfg.ChannelNumBuf[6]= DataStruct.RcvDeviceData.SYS.out7_spk_type;
//        MacCfg.ChannelNumBuf[7]= DataStruct.RcvDeviceData.SYS.out8_spk_type;
//
//        MacCfg.ChannelNumBuf[8]  = DataStruct.RcvDeviceData.SYS.m_nCur_PG_ID;
//        MacCfg.ChannelNumBuf[9]  = DataStruct.RcvDeviceData.SYS.out10_spk_type;
//        MacCfg.ChannelNumBuf[10] = DataStruct.RcvDeviceData.SYS.out11_spk_type;
//        MacCfg.ChannelNumBuf[11] = DataStruct.RcvDeviceData.SYS.out12_spk_type;
//        MacCfg.ChannelNumBuf[12] = DataStruct.RcvDeviceData.SYS.out13_spk_type;
//        MacCfg.ChannelNumBuf[13] = DataStruct.RcvDeviceData.SYS.out14_spk_type;
//        MacCfg.ChannelNumBuf[14] = DataStruct.RcvDeviceData.SYS.out15_spk_type;
//        MacCfg.ChannelNumBuf[15] = DataStruct.RcvDeviceData.SYS.out16_spk_type;

        for (int i = 0; i < 16; i++) {
            if (MacCfg.ChannelNumBuf[i] < 0) {
                MacCfg.ChannelNumBuf[i] = 0;
            }
        }

        return MacCfg.ChannelNumBuf[channel];
    }

    private void setLinkState_Unlink() {

        if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS) {//前声场，后声场，超低的联调，单独分开

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS_A) {//前声场，后声场，超低的联调，一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR) {//前声场，后声场，单独分开

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR_A) {//前声场，后声场，中置超低的联调，全部一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE) {//设置通道输出类型后的联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE_S) {//设置通道输出类型后的联调，可联机保存
            DataStruct.RcvDeviceData.OUT_CH[0].name[1] = 0;
        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_AUTO) {//任意联调，每个通道可以单独联调，可联机保存

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_LEFTRIGHT) {//固定两两通道联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR2A) {//前声场，后声场，一起两两联调

        }
        MacCfg.bool_OutChLink = false;
        setLinkBtnState(false);
        //flashLinkLyView();
    }

    private void setLinkState_Link() {
//        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//            MacCfg.DelayVal[i] = DataStruct.RcvDeviceData.OUT_CH[i].delay;
//        }
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            // MacCfg.OutputVal[i] = DataStruct.RcvDeviceData.OUT_CH[i].gain;
        }

        if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS) {//前声场，后声场，超低的联调，单独分开

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS_A) {//前声场，后声场，超低的联调，一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR) {//前声场，后声场，单独分开

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR_A) {//前声场，后声场，中置超低的联调，全部一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE) {//设置通道输出类型后的联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE_S) {//设置通道输出类型后的联调，可联机保存
            //保存在MCU的联调标志
            DataStruct.RcvDeviceData.OUT_CH[0].name[1] = 1;

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_AUTO) {//任意联调，每个通道可以单独联调，可联机保存

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_LEFTRIGHT) {//固定两两通道联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR2A) {//前声场，后声场，一起两两联调

        }
        MacCfg.bool_OutChLink = true;
        setLinkBtnState(true);
    }

    private void FlashLinkState() {
        if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS) {//前声场，后声场，超低的联调，单独分开
            checkLinkBtn();
        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS_A) {//前声场，后声场，超低的联调，一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR) {//前声场，后声场，单独分开

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR_A) {//前声场，后声场，中置超低的联调，全部一起联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE) {//设置通道输出类型后的联调

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE_S) {//设置通道输出类型后的联调，可联机保存
            System.out.println("BUG LinkMOde DataStruct.RcvDeviceData.OUT_CH[0].name[1]:" + DataStruct.RcvDeviceData.OUT_CH[0].name[1]);
            //保存在MCU的联调标志
            if (DataStruct.RcvDeviceData.OUT_CH[0].name[1] != 0) {
                if (CheckChannelCanLink() > 0) {
                    setLinkState_Link();
                }
            }
        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_AUTO) {//任意联调，每个通道可以单独联调，可联机保存

        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_LEFTRIGHT) {//固定两两通道联调
            setLinkState_Unlink();
        } else if (DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR2A) {//前声场，后声场，一起两两联调

        }
    }


    /*********************************************************************/
    /*******************           车机音量          **********************/
    /*********************************************************************/
//    private void initCarVol(){
//        mTWUtil = new TWUtil();
//        if(mTWUtil.open(new short[]{0x0201,0x0203,0x0301})==0){
//            mTWUtil.start();
//            mTWUtil.addHandler(TAG,handlerMainVal);
//            mTWUtil.write(0x0203,0xff);
//        }
//
//    }

    /*********************************************************************/
    /*******************           其他          **********************/
    /*********************************************************************/
    private static long curVal = 0;
    private static long oldVal = 0;
    private boolean BOOL_INC = true;
    @SuppressLint("HandlerLeak")
    private Handler handlerMainVal = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handlerMainVal.removeMessages(1);
                    LYMainVal.setVisibility(View.GONE);
                    break;
                case 1:
                    // 直接移除，定时器停止
                    handlerMainVal.removeMessages(0);
                    break;
                case 3:
                    // 直接移除，定时器停止
                    handlerMainVal.removeMessages(3);
                    ServiceOfCom.connectToDevice();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private void Exittimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EXITFLAG = false;
            }
        }, 2000);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MenuExit();
    }


    @Override
    public void onResume() {
        super.onResume();
        isFront = true;

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
//            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//            PermissionChecker.checkPermission(this, PERMISSIONS,Process.myPid(),Process.myUid(), getPackageName());
        }
//        System.out.println("BUG onResume XXXXXXXXXXXXXXXXXX");
//        if (!DataStruct.isConnecting) {
//            setConnectState(false);
//        } else {
//            if (mHome != null) {
//                mHome.FlashPageUI();
//            }
//        }
        checkUriFile();
        //setIntent(null);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CHS_Broad_Receiver != null) {
            unregisterReceiver(CHS_Broad_Receiver);
        }
//        if (mScreenReceiver != null) {
//            unregisterReceiver(mScreenReceiver);
//        }
//        if (mDownLoadCompleteReceiver != null) {
//            unregisterReceiver(mDownLoadCompleteReceiver);
//        }

        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    fragmentTransaction.remove(fragment);
                }
            }
        }

        handlerMainVal.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        Intent stopIntent = new Intent(mContext, ServiceOfCom.class);
        mContext.stopService(stopIntent);
        mContext.unbindService(connection);

        System.exit(0);
    }

    private void KeyInc() {
        if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
            return;
        }
        int val = 0;
        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
        } else {
            val = DataStruct.RcvDeviceData.SYS.main_vol;
        }

        if (++val > DataStruct.CurMacMode.Master.MAX) {
            val = DataStruct.CurMacMode.Master.MAX;
        }

        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            DataStruct.RcvDeviceData.IN_CH[0].Valume = val;
        } else {
            DataStruct.RcvDeviceData.SYS.main_vol = val;
        }

//        if(MacCfg.CurPage == Define.UI_PAGE_HOME){
//            LYMainVal.setVisibility(View.GONE);
//        }else {
//            LYMainVal.setVisibility(View.VISIBLE);
//        }
        SBMainVal.setProgress(val);

        if (DataStruct.CurMacMode.Master.MuteChangeWVol) {
            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
        }

        if (mHome != null) {
            mHome.flashMasterVol();
        }

        handlerMainVal.removeMessages(0);
        handlerMainVal.sendEmptyMessageDelayed(0, 3000);

    }

    private void KeySub() {
        if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
            return;
        }
        int val = 0;
        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
        } else {
            val = DataStruct.RcvDeviceData.SYS.main_vol;
        }
        if (--val < 0) {
            val = 0;
        }

        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            DataStruct.RcvDeviceData.IN_CH[0].Valume = val;
        } else {
            DataStruct.RcvDeviceData.SYS.main_vol = val;
        }

//        if(MacCfg.CurPage == Define.UI_PAGE_HOME){
//            LYMainVal.setVisibility(View.GONE);
//        }else {
//            LYMainVal.setVisibility(View.VISIBLE);
//        }

        SBMainVal.setProgress(val);
        if (DataStruct.CurMacMode.Master.MuteChangeWVol) {
            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
        }
        if (mHome != null) {
            mHome.flashMasterVol();
        }
        handlerMainVal.removeMessages(0);
        handlerMainVal.sendEmptyMessageDelayed(0, 3000);
    }

    //    // 4. 拦截系统热键
    @Override
//    public boolean onkeydown(KeyEvent event) {
//
//        int key = event.getKeyCode();//获取物理按键的key类型：比如音量键，power键等
//        int key1 = event.getAction();//获取某一物理按键的对应的事件类型；比如音量键的按下（down）事件，音量键的松开（up）事件
//        //ToastMsg("dispatchKeyEvent====->"+String.valueOf(key));
//        if (key == KeyEvent.KEYCODE_DPAD_LEFT
//                || key == KeyEvent.KEYCODE_VOLUME_UP) {//按下的是安卓物理左键或者是音量键上键
//
//            if (key1 == KeyEvent.ACTION_UP) {//音量键上键的up事件
//                KeyInc();
//                return true;
//            }
//        } else if (key == KeyEvent.KEYCODE_DPAD_RIGHT
//                || key == KeyEvent.KEYCODE_VOLUME_DOWN) {//按下的是安卓物理右键或者是音量键下键
//
//            if (key1 == KeyEvent.ACTION_UP) {//音量键下键的up事件
//                KeySub();
//
//                return true;
//            }
//        }else if (key == KeyEvent.KEYCODE_BACK) {
//            if (key1 == KeyEvent.ACTION_UP) {//音量键下键的up事件
//                Log.e("##BUG  ","KEYCODE_BACK---------=======-->>");
//                if(MacCfg.CurPage == Define.UI_PAGE_HOME){
////                if(EXITFLAG){
////                    Exit();
////                }else {
////                    EXITFLAG = true;
////                    ToastMsg(getResources().getString(R.string.Again_exit));
////                    Exittimer();
////                }
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    startActivity(intent);
//                }else {
//                    backToMain();
//                }
//                return true;
//            }
//
//        }
//
//        return super.dispatchKeyEvent(event);
//    }

    //@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.e("##BUG  ","onKeyDown KEYCODE_BACK---------=======-->>");
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
        //if (keyCode == KeyEvent.KEYCODE_MENU) {
        //ToastMsg("onKeyDown====->"+String.valueOf(keyCode));
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//
////            if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
////                return false;
////            }
//            int val = 0;
////            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
////                val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
////            }else {
//                val = DataStruct.RcvDeviceData.SYS.main_vol-60;
////            }
//            val -= 2;
//            if(val < 0){
//                val = 0;
//            }
//
////            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
////                DataStruct.RcvDeviceData.IN_CH[0].Valume = val;
////            }else {
//                DataStruct.RcvDeviceData.SYS.main_vol = val+60;
////            }
//
////            if(MacCfg.CurPage == Define.UI_PAGE_HOME){
////                LYMainVal.setVisibility(View.GONE);
////            }else {
////                LYMainVal.setVisibility(View.VISIBLE);
////            }
//
//            SBMainVal.setProgress(val);
//            if(DataStruct.CurMacMode.Master.MuteChangeWVol){
//                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
//            }
//            if(mHome != null){
//                mHome.flashMasterVol();
//            }
//            handlerMainVal.removeMessages(0);
//            handlerMainVal.sendEmptyMessageDelayed(0, 3000);
//            return true;
//
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//                return false;
//            }
//            int val = 0;
//            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
//                val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
//            }else {
//                val = DataStruct.RcvDeviceData.SYS.main_vol-60;
//            }
//            val += 2;
//            if(val > DataStruct.CurMacMode.Master.MAX){
//                val = DataStruct.CurMacMode.Master.MAX;
//            }
//
//            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
//                DataStruct.RcvDeviceData.IN_CH[0].Valume = val;
//            }else {
//                DataStruct.RcvDeviceData.SYS.main_vol = val+60;
//            }
//
////            if(MacCfg.CurPage == Define.UI_PAGE_HOME){
////                LYMainVal.setVisibility(View.GONE);
////            }else {
////                LYMainVal.setVisibility(View.VISIBLE);
////            }
//            SBMainVal.setProgress(val);
//
//            if(DataStruct.CurMacMode.Master.MuteChangeWVol){
//                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
//            }
//
//            if(mHome != null){
//                mHome.flashMasterVol();
//            }
//
//            handlerMainVal.removeMessages(0);
//            handlerMainVal.sendEmptyMessageDelayed(0, 3000);
//            return true;
//
//        }else
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (EXITFLAG) {
                MenuExit();
            } else {
                EXITFLAG = true;
                ToastMsg(getResources().getString(R.string.Again_exit));
                Exittimer();
            }
//            if(MacCfg.CurPage == Define.UI_PAGE_HOME){
//
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//            }else {
//                backToMain();
//            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
