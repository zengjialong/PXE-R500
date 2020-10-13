package com.chs.mt.pxe_r500.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
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
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.chs.mt.pxe_r500.MusicBox.MDef;
import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.SerialCom.ComBean;
import com.chs.mt.pxe_r500.SerialCom.SerialHelper;
import com.chs.mt.pxe_r500.bluetooth.ble.BluetoothLeService;
import com.chs.mt.pxe_r500.bluetooth.spp_ble.BluetoothChatService;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.operation.JsonRWUtil;
import com.chs.mt.pxe_r500.util.ToastUtil;
import com.chs.mt.pxe_r500.util.ToolsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.ACTION_PAIRING_REQUEST;
import static java.lang.Math.abs;

public class ServiceOfCom extends Service{
    public static boolean DEBUG = false;
    public static   final   int MAX_Name_Size=13;
    public  static final int EndFlag = 0xee;
    /*********************************************************************/
    /****************************   消息发送     ****************************/
    /*********************************************************************/
    public static final int NO = 0;  // 定义静态变量
    public static final int YES = 1; // 定义静态变量

    public static final int WHAT_IS_NULL = 0x00;            // 保留
    public static final int WHAT_IS_CONNECT_ERROR = 0x01;   // 连接错误
    public static final int WHAT_IS_CONNECT_RIGHT = 0x02;   // 连接网络正常
    public static final int WHAT_IS_SYNC_SUCESS = 0x03;     // 初始化同步数据
    public static final int WHAT_IS_NEW_DATA = 0x04;        // 收到新数据消息
    public static final int WHAT_IS_SEND_DATA = 0x05;       // 发送数据消息
    public static final int WHAT_IS_PROGRESSDIALOG = 0x06;  // 更新progressDialog
    public static final int WHAT_IS_LEDUP_SOURCE = 0x07;    // 信号灯实时更新音源
    public static final int WHAT_IS_OFF_LINE_INFO = 0x08;   // 未连接提示信息
    public static final int WHAT_IS_SYNC_GROUP_NAME=0x09;   // 同步用户组名字
    public static final int WHAT_IS_LED_FLASH=0x0A;         // 已连接设备
    public static final int WHAT_IS_RESET_EQ_CHNAME=0x0B;   // 刷新EQ通道名字
    public static final int WHAT_IS_LongPress_INC_SUB=0x0d; // 用于长按按键时的连续增减响应处理
    public static final int WHAT_IS_GroupClick=0x0e;        // 用于用户组的单双击响应处理
    public static final int WHAT_IS_Wait=0x0f;    			//用于发送队列延时
    public static final int WHAT_IS_LOADING=0x10;           //需要等待时显示加载图
    public static final int WHAT_IS_UPDATE_UI=0x11;         // 用于更新界面
    public static final int WHAT_IS_FLASH_BT_CONNECTED=0x12;// 刷新蓝牙的音频通道是否已经连接本机
    public static final int WHAT_IS_INIT_LOADING=0x13;      //当开机发送数据后，到一个正解的应答后开始显示加载进度
    public static final int WHAT_IS_CLOSE_BT=0x14;          //正在关闭蓝牙
    public static final int WHAT_IS_BT_TIME_OUT=0x15;       //蓝牙连接超时
    public static final int WHAT_IS_MENU_LOCKED=0x16;       //
    public static final int WHAT_IS_BLUETOOTH_SCAN=0x17;    //
    public static final int WHAT_IS_RETURN_EXIT = 0x18;     // 按2次返回键处理
    public static final int WHAT_IS_LOGOUT_SUCCESS = 0x19;
    public static final int WHAT_IS_LOGOUT_FAILED = 0x1a;
    public static final int WHAT_IS_FLASH_NETWORK=0x1b;     //网络是否在连接
    public static final int WHAT_IS_BT_SCAN=0x1c;           //蓝牙连接超时
    public static final int WHAT_IS_FLASH_NET_HOME_UI = 0x1d;// 刷界面
    public static final int WHAT_IS_FLASH_SYSTEM_DATA = 0x1e;// 刷界面
    public static final int WHAT_IS_TRYS_TO_CON_DSPPLAYMSG = 0x1f;// 刷界面
    public static final int WHAT_IS_ShowGetERRMasterVolMsg = 0x20;//
    public static final int WHAT_IS_Show_UnKnowMacType_Msg = 0x21;//
    public static final int WHAT_IS_Show_Timer = 0x22;//
    public static final int WHAT_IS_BOOT_Start = 0x23;//
    public static final int WHAT_IS_Max = 0x23;// 刷界面

    public static final int WHAT_IS_FlashUI_ConnectStateBtn = 0x60;
    public static final int WHAT_IS_FlashUI_AllPage       = 0x61;
    public static final int WHAT_IS_FlashUI_MasterPage    = 0x62;
    public static final int WHAT_IS_FlashUI_DelayPage     = 0x63;
    public static final int WHAT_IS_FlashUI_XOverPage     = 0x64;
    public static final int WHAT_IS_FlashUI_XOverOutputPage = 0x65;
    public static final int WHAT_IS_FlashUI_EQPage      = 0x66;
    public static final int WHAT_IS_FlashUI_MixerPage   = 0x67;
    public static final int WHAT_IS_FlashUI_DeviceVersionsErr = 0x68;
    public static final int WHAT_IS_FlashUI_InputSource  = 0x69;
    public static final int WHAT_IS_FlashUI_ConnectState = 0x6a;
    public static final int WHAT_IS_FlashUI_ShowLoadingDialog  = 0x6b;
    public static final int WHAT_IS_FlashUI_FlashLoadingDialog = 0x6c;

    public static final int WHAT_IS_Opt_ConnectDevice    = 0x70;
    public static final int WHAT_IS_Opt_DisconnectDevice = 0x71;

    public static final int Arg_ConnectStateBtn_OFF = 0x0;
    public static final int Arg_ConnectStateBtn_ON =  0x1;
    public static final int WHAT_IS_Opt_DisconnectDeviceSPP_LE = 0x72;


    /*********************************************************************/
    /*******************           车机音量          **********************/
    /*********************************************************************/
//    private TWUtil mTWUtil= null;
    private static long CarVolume = 0;
    private static boolean BOOL_FirstStart = false;
    private static long curVal = 0;
    private static long oldVal = 0;
    private static boolean BOOL_INC = true;
    /****************************   扫描本地，更新文件数据        ****************************/
    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    private Toast mToast;
    /*********************************************************************/
    /*******************  USB Host  蓝牙    WIFI通信     **********************/
    /*********************************************************************/

    /****************************************************************************/
    /****************************     蓝牙通信        ********************************/
    /****************************   蓝牙的连接状态        ****************************/
    public  static final int BT_SEND_DATA_PACK_SIZE = 20; //蓝牙发送数据包的最大字节数

    /*********************************************************************/
    /****************************    蓝牙SPP通信   ********************************/
    private static byte[] BTSendBuf20 = new byte[20];//较多常用24字节
    private byte[] BTRecBuf = new byte[1024];
    
    private static boolean BT_COther=false;      //true:用户手动连接其他设备
    private static boolean bool_OpBT=false;      //false:可操作蓝牙，避免快速开关断蓝牙 正在打开
    private static boolean bool_CloseBT=false;   //false:可操作蓝牙，避免快速开关断蓝牙 正在关闭
    /* true:从已经连接到DSP HP-XXXX蓝牙到8秒内，无法连接，测弹出提示：“蓝牙连接超时，请确定所连接的机器是
     * 否正确。若正确，请手动重启蓝牙，再进行连接。*/
    private static boolean bool_BT_CTO_Send=false;
    private static boolean bool_BT_ConTimeOut=false;
    private static boolean bool_FristStart=false;
    /*用于处理发送数失败后，收到错误应答后，再次重新发送数据*/
    private static  boolean  BTS_Again=false;/*true:要再次发送，1：其他*/
    /*************************************************************************/
    /****************************  蓝牙BLE通信   *******************************/
    public  static boolean BLE_DEVICE_STATUS = false;
    private static byte[] BLESendBuf = new byte[Define.BLE_MaxT];
    private static BluetoothLeService mBluetoothLeService = null;
    @SuppressWarnings("unused")
    private String mDeviceName = null;
    private String mDeviceAddress = null;
    /***************************************************************************/
    /****************************     WFIF通信       *******************************/
    private static Socket mSocketClient = null;
    @SuppressWarnings("unused")
    private static  BufferedReader mBufferedReaderClient = null;
    private String recvMessageClient = "";

    private static int WifiInfoTimerCnt = 0;


    private static boolean DeviceVerErrorFlg = false;// 设备版本信息错误标志
    private boolean WIFI_CanConnect = false;
    /***************************************************************************/
    /****************************  USB Host通信    *******************************/
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbManager USBManager; // USB管理器
    private static UsbDevice mUsbDevice;  // 找到的USB设备
    private UsbInterface mUsbInterface;
    private static UsbDeviceConnection mDeviceConnection;
    private static UsbEndpoint epOut;
    private UsbEndpoint epIn;
    private static byte[] USBSendBuf = new byte[Define.USB_MaxT];
    private static byte[] UARTSendBuf = new byte[Define.UART_MaxT];
    private byte[] Receiveytes=new byte[Define.USB_MaxT]; // 接收信息字节
    /***************************************************************************/
    /****************************     UART通信       *******************************/
    private static SerialControl ComA=null;

    /*********************************************************************/
    /*******************  蓝牙SPP-LE   通信     **********************/
    /*********************************************************************/
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // 连接设备的名称
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    // 本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    // 成员对象聊天服务
    private static BluetoothChatService mChatService = null;
    private static BluetoothDevice deviceSPPBLE;
    /***************************************************************************/
    /****************************     系统线程       *******************************/
    /***************************************************************************/
    private static int progressDialogStep = 0; // progressDialog分几步跑完

    public static final String TAG = "BUG ###ServiceOfCom";

    private static Context mContext;
    private Thread sThread = null;
    private Thread rThread = null;
    private Thread tThread = null;
    private CHS_Broad_BroadcastReceiver CHS_Broad_Receiver;

    //与Activity通信
    private Messenger mActivityMessenger;
    private Messenger mServiceMessenger;
    private Handler mhandlerService;
    private Messenger mReSendMessenger;

    private static boolean BOOT = false;
    public ServiceOfCom() {
        Log.d(TAG, TAG);
    }


    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initService();
    }



    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");

        BOOT = intent.getBooleanExtra("BOOT",false);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG","onBind()");

        return mServiceMessenger.getBinder();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceOnDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    private void sendMsgToActivity(int what,int arg1,int arg2){
//        System.out.println("## BUG sendMsgToActivity  msg.what:"+what+",arg1"+arg1+",arg2"+arg2);
//
//        Message message = Message.obtain();
//        message.what = what;
//        message.arg1 = arg1;
//        message.arg2 = 0xEE;
//        try {
//            mActivityMessenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    /*********************************************************************/
    /*****************************   数据通信       ****************************/
    /*********************************************************************/
    /*********************************************************************/
    private void initService() {
        mContext = getApplicationContext();

		//rThread = new Thread(rRunnable);
		//rThread.start(); //WIFI,USB要打开
		sThread = new Thread(sRunnable);
		sThread.start();
		//tThread = new Thread(tRunnable);
//		tThread.start(); // 打开定时器线程 用不到
		int IdNum = (int) Thread.currentThread().getId();
		System.out.println(TAG+" initService currentThread Id:" + IdNum);
        initHandlerService();
		//动态注册CHS_Broad_BroadcastReceiver
    	CHS_Broad_Receiver = new CHS_Broad_BroadcastReceiver();
    	IntentFilter CHS_Broad_filter=new IntentFilter();
    	CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_BroadcastReceiver");
    	//注册receiver
    	registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
    	DataStruct.jsonRWOpt = new JsonRWUtil();

        DataOptUtil.InitApp(mContext);
    	//初始化通信方式
    	initCommunicationMode();
        initBluetoothReceiver();

    }

    private void initHandlerService(){
        /**
         * HandlerThread是Android系统专门为Handler封装的一个线程类，
         * 通过HandlerThread创建的Hanlder便可以进行耗时操作了
         * HandlerThread是一个子线程,在调用handlerThread.getLooper()之前必须先执行
         * HandlerThread的start方法。
         */
        HandlerThread handlerThread = new HandlerThread("serviceCalculate");
        handlerThread.start();
        mhandlerService = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(mActivityMessenger == null) {
                    mActivityMessenger = msg.replyTo;
                }
                if(msg.what == WHAT_IS_Opt_ConnectDevice){
                    if(msg.arg1 == Arg_ConnectStateBtn_ON){

                    }else{

                    }
                }

            }
        };
        mServiceMessenger = new Messenger(mhandlerService);
    }

    /**
     * 是否已经连接本机，true:已经连接
     */
    private static boolean isConnected(){
        switch (MacCfg.COMMUNICATION_MODE) {
            case Define.COMMUNICATION_WITH_WIFI:
                if(DataStruct.isConnecting && mSocketClient != null){
                    return true;
                }
                break;
            case Define.COMMUNICATION_WITH_BLUETOOTH_LE:
                return BLE_DEVICE_STATUS;
            case Define.COMMUNICATION_WITH_BLUETOOTH_SPP:
//                if((mSocketClient == null)&&(CheckBTStata())&& (btManager.getStatus()== Common.BTConnectStatusConnect)){
//                    return true;
//                }
                break;
            case Define.COMMUNICATION_WITH_USB_HOST:
                return MacCfg.USBConnected;
            case Define.COMMUNICATION_WITH_UART:

                break;
            default:
                break;
        }

        return false;
    }

    public void printSendPack(byte[] btdata, int packsize) {
        String packString="Send[";
        for(int i=0;i<packsize;i++){
            packString+=(btdata[i]+",");
        }
        System.out.println("BUG  printSendPack="+packString);
    }

    /*****************************  WIFI TODO ****************************/
    /*********************************************************************/

    private boolean NewSocketClient() {
        String msgText = Define.WIFI_IP_PORT;
        if (msgText.length() <= 0) {
            recvMessageClient = "IP不能为空!";// 消息换行
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = "+"IP不能为空!");
            return false;
        }
        int start = msgText.indexOf(":");
        if ((start == -1) || (start + 1 >= msgText.length())) {
            recvMessageClient = "IP地址不合法!";// 消息换行
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = "+"IP地址不合法!!");
            return false;
        }
        String sIP = msgText.substring(0, start);
        String sPort = msgText.substring(start + 1);
        int port = Integer.parseInt(sPort);

        System.out.println("BUG WIFI  "+"IP:" + sIP + ",port:" + port);
        try {
            if(mSocketClient==null){
                // 连接服务器
                mSocketClient = new Socket(sIP, port); // portnum
                if(mSocketClient!=null){
                    // 取得输入、输出流
                    mBufferedReaderClient = new BufferedReader(
                            new InputStreamReader(mSocketClient.getInputStream()));
                    recvMessageClient = "已经连接到server!";// 消息换行
                    // 发送连接命令
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_RIGHT;
                    mHandler.sendMessage(msg);

                    System.out.println("BUG mSocketClient new Socket 已经连接到server!");
                    return true;
                }else{
                    System.out.println("BUG mSocketClient new Socket ERROR!!!");
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            recvMessageClient = "连接IP异常:" + e.toString() + e.getMessage();// 消息换行
            System.out.println("BUG WIFI error-leon");
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);
            System.out.println("BUG WIFI  :"+recvMessageClient);
            return false;
        }
    }

    /**
     * 判断wifi连接状态
     *
     * @param ctx
     * @return
     */
    private boolean isWifiAvailable(Context ctx) {
        ConnectivityManager conMan = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (NetworkInfo.State.CONNECTED == wifi) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isWifiIpOfCHS() {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();
        String ipString = "";
        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
        System.out.println("BUG WIFI  isWifiIpOfCHS A ipString:" +ipString);

        // 判断IP地址是不是10.10.100.xxx
        if ((ipAddress & 0xffffff) == 0x640a0a) {

            if (ipAddress != 0) {
                ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
            }

            System.out.println("BUG WIFI  isWifiIpOfCHS B ipString:" +ipString);
            System.out.println("BUG WIFI  isWifiAvailable 4");
            Define.WIFI_IP=ipString;
            //Define.WIFI_IP_PORT=Define.WIFI_IP+DataStruct.WIFI_PORT;
            return true;
        } else {
            return false;
        }
    }

    public boolean CheckWifiStata() {
        if (DataStruct.isConnecting) {
            return false;
        }
        System.out.println("BUG WIFI  isWifiAvailable 0");
        if (DataStruct.ManualConnecting == true) {
            return false;
        }

        if(!isWifiAvailable(mContext)){
            return false;
        }
        System.out.println("BUG WIFI  isWifiAvailable 3");
        return isWifiIpOfCHS();
    }

    //打开返回true
    public static boolean CheckBTStata() {
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            if(!btAdapter.isEnabled()){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    /*********************************************************************/
    /*************************  蓝牙SPP通信方式 TODO  *********************/
    /*********************************************************************/
    private void initCommunicationMode() {
        switch (MacCfg.COMMUNICATION_MODE) {
            case Define.COMMUNICATION_WITH_WIFI:

                break;
            case Define.COMMUNICATION_WITH_BLUETOOTH_LE:
                //提示用户打开蓝牙
                if(CheckBTStata()==false){
                   // ShowOpenBTDialog();
                }
                break;
            case Define.COMMUNICATION_WITH_USB_HOST:
                RegUSBBroadcastReceiver();
                break;
            case Define.COMMUNICATION_WITH_UART:

                break;

            default:
                break;
        }

    }
    /* Check the audio channel of the Bluetooth connection device,
	 * if none, close bluetooth connection.
	*/


    private static void startScanBluetooth(){
        mContext.startActivity(new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
    }



    /*********************************************************************/
    /********************  蓝牙BLE GATT通信方式  TODO   ********************/
    /*********************************************************************/
    private void initBluetoothLe(){
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        System.out.println("BUG Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                mBluetoothLeService.connect(mDeviceAddress);
            }
        }, 988);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {                        //注册接收的事件
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                System.out.println("BUG Unable to initialize Bluetooth");
                //finish();
            }

            System.out.println("BUG mBluetoothLeService is okay");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //连接成功
                System.out.println("BUG BLE Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                System.out.println("BUG BLE ACTION_GATT_DISCONNECTED");
                BLE_DEVICE_STATUS = false;
                Message msg = Message.obtain();
                msg.what = WHAT_IS_CONNECT_ERROR;
                mHandler.sendMessage(msg);

            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){ //可以开始干活了
                System.out.println("BUG BLE ACTION_GATT_SERVICES_DISCOVERED");

                Message msg = Message.obtain();
                msg.what = WHAT_IS_CONNECT_RIGHT;
                mHandler.sendMessage(msg);

            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
                //System.out.println("BUG BLE RECV DATA");
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                //String string="[";
                if (data != null) {
                    for(int i=0;i<data.length;i++){
                        ReceiveDataFromDevice((0xff & data[i]),Define.COMMUNICATION_WITH_BLUETOOTH_LE);
                        //string+=((data[i] & 0xff)+",");
                    }
                    //System.out.println("BUG FUCK BLE broadcastUpdate data byte:"+string);
                }
            }
        }
    };

    //蓝牙BLE发大包发送数据，分每20字节一个小包
    public static void BLESendPack(byte[] btdata, int packsize) {
        //DataStruct.U0HeadFlg = NO;//设置包头无效
        int temp1=0,temp2=0;

        temp1 = packsize/Define.BLE_MaxT;
        if(temp1 > 0){
            for (int i=0;i<temp1;i++){
                for(int j=0;j<Define.BLE_MaxT;j++){
                    BLESendBuf[j] = btdata[i*Define.BLE_MaxT + j];
                }
                BLESend(BLESendBuf);
            }
        }

        temp2 = packsize%Define.BLE_MaxT;
        //清0
        for(int i=0;i<Define.BLE_MaxT;i++){
            BLESendBuf[i] = (byte)0x00;
        }

        if(temp2 > 0){
            for (int i=0;i<temp2;i++){
                BLESendBuf[i] = btdata[temp1*Define.BLE_MaxT + i];
            }
            BLESend(BLESendBuf);
        }
    }

    private static void BLESend(byte[] Sendbytes) {
        if(mBluetoothLeService != null){
            mBluetoothLeService.WriteByteValue(Sendbytes);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("BUG sThread BLESend");
            }
        }
    }
    /*********************************************************************/
    /**********************  Bluetooth SPP BLE通信方式   TODO  **********************/
    /*********************************************************************/
    private class ServerThread extends Thread {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        //private BluetoothServerSocket serverSocket;
        String tag="BUG FUCK";
        @Override
        public void run() {

            try {
                BluetoothSocket socket = deviceSPPBLE.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                System.out.println("####- BUG socket.connect()");

            } catch (Exception e) {
                System.out.println("BUG ServerThread run Exception!");
            }


//            try {
//                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(tag, UUID.fromString(SPP_UUID));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Log.d(tag, "等待客户连接...");
//            while (true) {
//                try {
//                    BluetoothSocket socket = serverSocket.accept();
//
//                    BluetoothDevice device = socket.getRemoteDevice();
//                    Log.d(tag, "接受客户连接 , 远端设备名字:" + device.getName() + " , 远端设备地址:" + device.getAddress());
//
//                    if (socket.isConnected()) {
//                        Log.d(tag, "已建立与客户连接.");
//                    }
//
//                } catch (Exception e) {
//                    Log.d(tag, "等待客户连接.Exception..");
//                    e.printStackTrace();
//                }
//            }
        }
    }

    private void connect(BluetoothDevice device) throws IOException {
        deviceSPPBLE = device;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null)
            new Thread(new ServerThread()).start();

//        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
//        UUID uuid = UUID.fromString(SPP_UUID);

//        BluetoothSocket socket = deviceSPPBLE.createRfcommSocketToServiceRecord(uuid);
//        socket.connect();
//        System.out.println("####- BUG socket.connect()");


//        String SS="BUG FUCK";
//        BluetoothServerSocket serverSocket;
//        serverSocket=mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SS, uuid);
//
//        while (true) {
//            BluetoothSocket socket = serverSocket.accept();
//            if (socket.isConnected()) {
//                System.out.println("####- BUG socket.connect()");
//            }
//        }
    }
    // 此Handler处理BluetoothChatService传来的消息
    @SuppressLint("HandlerLeak")
    private final Handler mHandlerOfSPP_LE = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Define.MESSAGE_STATE_CHANGE:
                    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Message msgg = Message.obtain();
                            msgg.what = WHAT_IS_CONNECT_RIGHT;
                            mHandler.sendMessage(msgg);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //ToastUtil.showShortToast(mContext,mContext.getResources().getString(R.string.LinkOfMsg));
                            System.out.println("BUG====- mHandlerOfSPP_LE WHAT_IS_CONNECT_ERROR");
                            //断开蓝牙
                            Message msg_err = Message.obtain();
                            msg_err.what = WHAT_IS_CONNECT_ERROR;
                            mHandler.sendMessage(msg_err);
                            break;
                    }
                    break;
                case Define.MESSAGE_Lost:
                    //断开蓝牙
                    Message msg_err = Message.obtain();
                    msg_err.what = WHAT_IS_CONNECT_ERROR;
                    mHandler.sendMessage(msg_err);
                    break;
                case Define.MESSAGE_WRITE:
                    break;
                case Define.MESSAGE_READ:
//            	System.out.println("BUG MESSAGE_READ");
                    byte[] readBuf = (byte[]) msg.obj;
                    int cnt = msg.arg1;

//                    String st=",Data<";
//                    String ss="";
//                    for(int i=0;i<cnt;i++){
//                        ss=Integer.toHexString(readBuf[i]&0xff).toLowerCase();
//                        if(ss.length()==1){
//                            ss="0"+ss;
//                        }
//                        if((((i+1)%4)==0)&&((i+1)!=cnt)){
//                            st=st+ss+" ";
//                        }else {
//                            st+=ss;
//                        }
//
//                    }
//                    System.out.println("BUG-COM-Service-接收N:"+cnt+st+">");


                    for (int i=0;i<cnt;i++) {
                         ReceiveDataFromDevice((readBuf[i] & 0xff), Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO);
                    }
                    break;
                case Define.MESSAGE_DEVICE_NAME:
                    break;
                case Define.MESSAGE_TOAST:

                    break;
            }
        }
    };
    public static void SPP_LESendPack(byte[] btdata, int packsize) {

        int temp1=0,temp2=0;
        temp1 = packsize/Define.BLE_MaxT;
        if(temp1 > 0){
            for (int i=0;i<temp1;i++){
                for(int j=0;j<Define.BLE_MaxT;j++){
                    BLESendBuf[j] = btdata[i*Define.BLE_MaxT + j];
                }
                SPP_LESend(BLESendBuf);
            }
        }

        temp2 = packsize%Define.BLE_MaxT;
        //清0
        for(int i=0;i<Define.BLE_MaxT;i++){
            BLESendBuf[i] = (byte)0x00;
        }

        if(temp2 > 0){
            for (int i=0;i<temp2;i++){
                BLESendBuf[i] = btdata[temp1*Define.BLE_MaxT + i];


            }
            SPP_LESend(BLESendBuf);

        }
    }

    private static void SPP_LESend(byte[] Sendbytes) {
        if(mChatService != null){
            mChatService.write(Sendbytes);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //   System.out.println("BUG 下发的值为\t"+ ByteUtil.ByteArrToHex(Sendbytes));
        }
    }
    /*********************************************************************/
    /**********************  USB Host通信方式   TODO  **********************/
    /*********************************************************************/
    private void RegUSBBroadcastReceiver() {

        //USBsend = (Button) findViewById(R.id.btsend);
        //USBsend.setOnClickListener(btsendListener);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);//PERMISSION
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);  //ATTACHED
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);  //DETACHED
        registerReceiver(mUsbReceiver, filter);

        searchUsbDevice();
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("BUG USB BroadcastReceiver="+ action);
            UsbDevice UsbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            //USBsend.setText("UsbDevice#VID:"+UsbDevice.getVendorId()+",PID:"+UsbDevice.getProductId());
            //ToastMsg("mUsbReceiver USB.getVendorId()="+UsbDevice.getVendorId());

//		    System.out.println("BUG USB DSP-UsbDevice getVendorId:"+UsbDevice.getVendorId());
//            System.out.println("BUG USB DSP-UsbDevice getProductId:"+UsbDevice.getProductId());
//            System.out.println("BUG USB DSP-UsbDevice getDeviceId:"+UsbDevice.getDeviceId());
//            System.out.println("BUG USB DSP-UsbDevice getDeviceName:"+UsbDevice.getDeviceName());
//            System.out.println("BUG USB DSP-UsbDevice getDeviceProtocol:"+UsbDevice.getDeviceProtocol());
//            System.out.println("BUG USB DSP-UsbDevice getDeviceSubclass:"+UsbDevice.getDeviceSubclass());
//            System.out.println("BUG USB DSP-UsbDevice getInterfaceCount:"+UsbDevice.getInterfaceCount());
            //只有本公司的产品才作处理
            if(UsbDevice!=null){
                if (UsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                        UsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                    mUsbDevice = UsbDevice;

                    if (ACTION_USB_PERMISSION.equals(action)) {
                        synchronized (this) {
                            //UsbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                            //允许权限申请
                            if(mUsbDevice!=null){
                                if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                                        mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                        findIntfAndEpt();
                                    } else {
                                        System.out.println("BUG USB permission denied for device " + mUsbDevice);
                                    }
                                }
                            }

                        }
                    } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                        //ToastMsg(getResources().getString(R.string.USB_ATTACHED));
                        if(mUsbDevice!=null){
                            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                                System.out.println("BUG USB DSPHD #ACTION_USB_DEVICE_ATTACHED");

                                findIntfAndEpt();
//        	        			//
//        	        			MacCfg.USBConnected=true;
//        	        			Message msg = Message.obtain();
//        						msg.what = WHAT_IS_CONNECT_RIGHT;
//        						mHandler.sendMessage(msg);
                            }
                        }

                        //searchUsbDevice();
                    } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                        System.out.println("BUG USB #ACTION_USB_DEVICE_DETACHED !");
                        //ToastMsg(getResources().getString(R.string.USB_DETACHED));
                        if(mUsbDevice!=null){
                            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {

                                mUsbDevice=null;
                                MacCfg.USBConnected=false;
                                Message msg = Message.obtain();
                                msg.what = WHAT_IS_CONNECT_ERROR;//
                                mHandler.sendMessage(msg);

                                System.out.println("BUG USB DSPHD #ACTION_USB_DEVICE_DETACHED WHAT_IS_CONNECT_ERROR");
                                //USBsend.setText("Did not found DSPHD");

                            }
                        }
                    }
                }
            }
        }
    };

    private void searchUsbDevice() {
        USBManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if(USBManager!=null){
            HashMap<String, UsbDevice> deviceList = USBManager.getDeviceList();

            System.out.println("BUG USB USBManager.toString：" + String.valueOf(USBManager.toString()));
            System.out.println("BUG USB deviceList.size：" + String.valueOf(deviceList.size()));
            String size=String.valueOf(deviceList.size());
            String vidString="VID=";
            ////ToastMsg("deviceList.size()="+deviceList.size());

            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            ArrayList<String> USBDeviceList = new ArrayList<String>(); // 存放USB设备的数量
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();

                USBDeviceList.add(String.valueOf(device.getVendorId()));
                USBDeviceList.add(String.valueOf(device.getProductId()));

                vidString+=device.getVendorId()+",";
                ////ToastMsg("device.getVendorId()="+device.getVendorId());
                // 在这里添加处理设备的代码
                if (device.getVendorId() == Define.USB_DSPHD_VID &&
                        device.getProductId() ==Define.USB_DSPHD_PID) {
                    mUsbDevice = device;

                    // 寻找接口和分配结点
                    findIntfAndEpt();

                    System.out.println("BUG USB DSP-HD getVendorId:"+device.getVendorId());
                    System.out.println("BUG USB DSP-HD getProductId:"+device.getProductId());
                    System.out.println("BUG USB DSP-HD getDeviceId:"+device.getDeviceId());
                    System.out.println("BUG USB DSP-HD getDeviceName:"+device.getDeviceName());
                    System.out.println("BUG USB DSP-HD getDeviceProtocol:"+device.getDeviceProtocol());
                    System.out.println("BUG USB DSP-HD getDeviceSubclass:"+device.getDeviceSubclass());
                    System.out.println("BUG USB DSP-HD getInterfaceCount:"+device.getInterfaceCount());

                    //USBsend.setText("USB DSP-HD#VID:"+device.getVendorId()+",PID:"+device.getProductId());

                    break;
                }
            }
            //ToastMsg(size+"==="+vidString);
        }
    }

    // 寻找接口和分配结点
    private void findIntfAndEpt() {
        if (mUsbDevice == null) {
            System.out.println("BUG USB 没有 USB 设备!");
            //ToastMsg(getResources().getString(R.string.DidNotFindUSB));
            return;
        }
        for (int i = 0; i < mUsbDevice.getInterfaceCount();) {
            // 获取设备接口，一般都是一个接口，你可以打印getInterfaceCount()方法查看接
            // 口的个数，在这个接口上有两个端点，OUT 和 IN
            UsbInterface intf = mUsbDevice.getInterface(i);
            System.out.println("BUG usb "+ i + " " + intf);
            mUsbInterface = intf;

            break;
        }

        if (mUsbInterface != null) {
            UsbDeviceConnection connection = null;
            // 判断是否有权限
            if (USBManager.hasPermission(mUsbDevice)) {
                // 打开设备，获取 UsbDeviceConnection 对象，连接设备，用于后面的通讯
                connection = USBManager.openDevice(mUsbDevice);
                if (connection == null) {
                    return;
                }
                if (connection.claimInterface(mUsbInterface, true)) {
                    System.out.println("BUG usb 找到接口");
                    //DisplayToast("找到接口");
                    mDeviceConnection = connection;
                    getEndpoint(mDeviceConnection, mUsbInterface);


                    MacCfg.USBConnected=true;
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_RIGHT;
                    mHandler.sendMessage(msg);

                } else {
                    connection.close();
                }
            } else {
                PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION),0);
                USBManager.requestPermission(mUsbDevice, pi); //该代码执行后，系统弹出一个对话框，
                System.out.println("BUG USB 没有权限");
                //ToastMsg(getResources().getString(R.string.HaveNotPermission));
            }
        } else {
            System.out.println("BUG usb 没有找到接口");
            //ToastMsg(getResources().getString(R.string.HaveNotInterface));
        }
    }

    // 用UsbDeviceConnection 与 UsbInterface 进行端点设置和通讯
    private void getEndpoint(UsbDeviceConnection connection, UsbInterface intf) {
        if (intf.getEndpoint(1) != null) {
            epOut = intf.getEndpoint(1);
        }
        if (intf.getEndpoint(0) != null) {
            epIn = intf.getEndpoint(0);
        }
    }

    private void USBHost_LinkButtonCmd() {
        if(mUsbDevice!=null){
            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                if(!MacCfg.USBConnected){
                    MacCfg.USBConnected=true;

                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_RIGHT;
                    mHandler.sendMessage(msg);
                }else {
                    MacCfg.USBConnected=false;
                    DataStruct.ManualConnecting=true;

                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_ERROR;
                    mHandler.sendMessage(msg);

                    System.out.println("BUG WHAT_IS_CONNECT_ERROR = "+"USBConnected false");
                }
            }
        }
    }

    private void USBHost_Connect() {
        if(mUsbDevice!=null){
            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                if(!MacCfg.USBConnected){
                    MacCfg.USBConnected=true;

                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_RIGHT;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    //USB大包发送数据，分每USB_MaxT字节一个小包
    public static void USB_SendPack(byte[] btdata, int packsize) {
        //DataStruct.U0HeadFlg = NO;//设置包头无效
        int temp1=0,temp2=0;

        temp1 = packsize/Define.USB_MaxT;
        if(temp1 > 0){
            for (int i=0;i<temp1;i++){
                for(int j=0;j<Define.USB_MaxT;j++){
                    USBSendBuf[j] = btdata[i*Define.USB_MaxT + j];
                }
                USBSend(USBSendBuf);
            }
        }

        temp2 = packsize%Define.USB_MaxT;
        //清0
        for(int i=0;i<Define.USB_MaxT;i++){
            USBSendBuf[i] = (byte)0x00;
        }

        if(temp2 > 0){
            for (int i=0;i<temp2;i++){
                USBSendBuf[i] = btdata[temp1*Define.USB_MaxT + i];
            }
            USBSend(USBSendBuf);
        }
    }

    private static int USBSend(byte[] Sendbytes) {
        int ret = -100;
        if(epOut!=null){

//            String st="BUG-COM---发送命令:<";
//            String ss="";
//            int packsize = Sendbytes.length;
//            for(int i=0;i<packsize;i++){
//                ss=Integer.toHexString(Sendbytes[i]&0xff).toLowerCase();
//                if(ss.length()==1){
//                    ss="0"+ss;
//                }
//                if((((i+1)%4)==0)&&((i+1)!=packsize)){
//                    st=st+ss+" ";
//                }else {
//                    st+=ss;
//                }
//            }
//            System.out.println(st+">");


            ret = mDeviceConnection.bulkTransfer(epOut, Sendbytes,
                    Sendbytes.length, 5000);
        }else{
            Log.e("USB","BUG USB epOut==null" );
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            System.out.println("BUG sThread BLESend");
        }
        return ret;
    }

    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte)(_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte)(_b0 ^ _b1);
        return ret;
    }

    public static byte[] HexString2Bytes(String src){
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for(int i=0; i<8; i++){
            ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
        }
        return ret;
    }

    /*********************************************************************/
    /*************************    UART通信方式 TODO  **********************/
    /*********************************************************************/
    private static void showMsgWithBroadcast(String st){
        //刷新界面连接进度
        Intent intentw=new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowMsg);
        intentw.putExtra("String", st);
        mContext.sendBroadcast(intentw);
    }

    private static void initSerialPort(){
        showMsgWithBroadcast("初始化串口");

        ComA = new SerialControl();
        ComA.setPort("/dev/ttyS6");
        ComA.setBaudRate("57600");
    }

    private static void closeSerialPort(){
        if (ComA!=null){
            ComA.stopSend();
            ComA.close();
        }
    }

    public static void serialPortSendData(byte[] buf){
        if (ComA!=null){
            ComA.send(buf);
        }
    }

    //USB大包发送数据，分每USB_MaxT字节一个小包
    public static void UART_SendPack(byte[] btdata, int packsize) {
        //DataStruct.U0HeadFlg = NO;//设置包头无效
        int temp1=0,temp2=0;

        temp1 = packsize/Define.UART_MaxT;
        if(temp1 > 0){
            for (int i=0;i<temp1;i++){
                for(int j=0;j<Define.UART_MaxT;j++){
                    UARTSendBuf[j] = btdata[i*Define.UART_MaxT + j];
                }
                serialPortSendData(UARTSendBuf);
            }
        }

        temp2 = packsize%Define.UART_MaxT;
        //清0
        for(int i=0;i<Define.UART_MaxT;i++){
            UARTSendBuf[i] = (byte)0x00;
        }

        if(temp2 > 0){
            for (int i=0;i<temp2;i++){
                UARTSendBuf[i] = btdata[temp1*Define.UART_MaxT + i];
            }
            serialPortSendData(UARTSendBuf);
        }
    }

    private static void OpenSerialPort(){
        if(ComA == null){
            initSerialPort();
        }

        try {
            ComA.open();
        } catch (SecurityException e) {
            Log.e(TAG,"打开串口失败:没有串口读/写权限!");
            showMsgWithBroadcast("打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            Log.e(TAG,"打开串口失败:未知错误!");
            showMsgWithBroadcast("打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            Log.e(TAG,"打开串口失败:参数错误!");
            showMsgWithBroadcast("打开串口失败:参数错误!");
        }

        if(ComA.isOpen()){
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_RIGHT;
            mHandler.sendMessage(msg);
        }
    }
    private static class SerialControl extends SerialHelper {
        public SerialControl(){
        }

        @Override
        protected void onDataReceived(final ComBean ComRecData){
            //Log.v(TAG, MyFunc.ByteArrToHex(ComRecData.bRec));


            int cnt = ComRecData.bRec.length;
//            if(cnt <= 0){
//                return;
//            }
//            String st=",Data<";
//            String ss="";
//            for(int i=0;i<cnt;i++){
//                ss=Integer.toHexString(ComRecData.bRec[i]&0xff).toLowerCase();
//                if(ss.length()==1){
//                    ss="0"+ss;
//                }
//                if((((i+1)%4)==0)&&((i+1)!=cnt)){
//                    st=st+ss+" ";
//                }else {
//                    st+=ss;
//                }
//
//            }
//            String sss = "BUG-COM-Service-接收N:"+cnt+st+">";
//            System.out.println(sss);
//            showMsgWithBroadcast(sss);

            for (int i=0;i<cnt;i++) {
                ReceiveDataFromDevice((ComRecData.bRec[i] & 0xff), Define.COMMUNICATION_WITH_UART);
            }
        }
    }



    /*********************************************************************/
    /*************************    测试包头 TODO  **********************/
    /*********************************************************************/

    private static int getBluetoothDeviceIDCom(String dev){
        if(dev == null){
            return Define.BT_Android_Type;
        }
        if(dev.contains(Define.BT_Paired_Name_DSP_HD_)){
            return Define.BT_Android_Type;
        }else if(dev.contains(Define.BT_Paired_Name_DSP_Play)){
            return Define.BT_ATS2825_Type;
        }else if(dev.contains(Define.BT_Paired_Name_DSP_HDS)){
            return Define.BT_ATS2825_Type;
        }else if(dev.contains(Define.BT_Paired_Name_DSP_CCS)){
            return Define.BT_Conrol_Type;
        }else{
            return Define.BT_Android_Type;
        }

    }
    //TODO
    public static void getCheckHeadFromBuf(){
        if(MacCfg.BOOL_ConnectAny){
            SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
            MacCfg.HEAD_DATA = sp.getInt("HEAD_DATA", 0x00);
//        String BT_CUR_ConnectedID = sp.getString("BT_CUR_ConnectedID", "null");
//        String BT_CUR_ConnectedName = sp.getString("BT_CUR_ConnectedName", "null");
//        Log.e("##BUG getSharedPf ","getCheckHeadFromBuf BT_CUR_ConnectedID:"+
//                BT_CUR_ConnectedID+",BT_CUR_ConnectedName:"+BT_CUR_ConnectedName + "\n");
        }

        System.out.println("BUG LOADINIT getCheckHeadFromBuf MacCfg.HEAD_DATA++++++++="+Integer.toHexString(MacCfg.HEAD_DATA)+"\n"
                +",BT_CUR_ConnectedName="+MacCfg.BT_CUR_ConnectedName+"\n"
                +",BT_CUR_ConnectedID="+MacCfg.BT_CUR_ConnectedID+"\n"
        );
        MacCfg.BluetoothDeviceID = getBluetoothDeviceIDCom(MacCfg.BT_CUR_ConnectedName);
        //MacCfg.BluetoothDeviceID = 0;
        MacCfg.HEAD_DATA_Index = 0;
        MacCfg.cntDSP = 0;
        //切换了蓝牙
        if(MacCfg.BT_CUR_ConnectedID == null){
            return;
        }

        if(!MacCfg.BT_CUR_ConnectedID.equals(MacCfg.BT_OLD_ConnectedID)){
            DataStruct.BOOL_CheckHEADOK = false;
            DataStruct.BOOL_CheckHEAD_ST = false;
            MacCfg.BT_OLD_ConnectedID = MacCfg.BT_CUR_ConnectedID;

            DataStruct.comType = Define.COMT_OFF;
            DataStruct.comDSP  = Define.COMT_OFF;
            DataStruct.comPlay = Define.COMT_OFF;
        }
    }

    /*********************************************************************/
    /*************************   通信方式 TODO  **********************/
    /*********************************************************************/
    public static void APP_SendPack(byte[] btdata, int packsize) {
        if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_WIFI){
            try {
                if (mSocketClient != null){
                    OutputStream os = mSocketClient.getOutputStream();
                    os.write(btdata, 0,packsize); // 发送到数据给设备
                    os.flush();
                }
            } catch (IOException e) {
                System.out.println("BUG sThread buffer send error-leon");
            }
        }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_SPP){
        }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO){
            SPP_LESendPack(btdata, packsize);
        }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_LE){
            BLESendPack(btdata, packsize);
        }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_USB_HOST){
            USB_SendPack(btdata, packsize);
        }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_UART){
            UART_SendPack(btdata, packsize);
        }
    }

    private void getMasterVolSendCmd(){
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;

        if(DataStruct.BOOLDSPHeadFlg == Define.Statues_YES){
            if(!DataStruct.BOOLHaveMasterVol){
                Log.d(TAG, "BUG ##Get BOOLHaveMasterVol");
                // 请求SYSTEM主音量
                DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = 0x00;
                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                DataStruct.SendDeviceData.ChannelID = Define.PC_SOURCE_SET;//PC_SOURCE_SET SYSTEM_DATA
                DataStruct.SendDeviceData.DataID = 0x00;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                DataStruct.SendDeviceData.PcCustom = 0x00;
                DataStruct.SendDeviceData.DataLen = 0x00;
                OutTimeCnt = 0;
                OK = true;
                int tryc = 0;
                clearRecFlag();
                DataStruct.U0RcvFrameFlg = NO;
                DataOptUtil.SendDataToDevice(true);
                while(DataStruct.U0RcvFrameFlg == NO){
                    if(!DataStruct.isConnecting){
                        OK = false;
                        break;
                    }
                    //Delay
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    // 发送数据无回应时，单次重试的时间间隔
                    if (++OutTimeCnt > 100){
                        OutTimeCnt = 0;
                        DataOptUtil.SendDataToDevice(true);
                        if(++tryc > 3){
                            OK = false;
                            break;
                        }
                    }
                }

                if(OK){//获取成功
                    DataStruct.BOOLHaveMasterVol = true;
                    DataStruct.BOOL_CheckHEADOK = true;

                    if(DataStruct.MasterConType){
                        System.out.println("BUG LOADINIT checkMasterVol System.Val="+DataStruct.RcvDeviceData.SYS.main_vol);
                        DataStruct.CurMacMode.Master.DATA_TRANSFER = Define.COM_TYPE_SYSTEM;
                        DataStruct.CurMacMode.Master.MAX = 66;
                    }else {
                        DataStruct.CurMacMode.Master.DATA_TRANSFER = Define.COM_TYPE_INPUT;
                        DataStruct.CurMacMode.Master.MAX = 60;
                        System.out.println("BUG LOADINIT checkMasterVolOfInputSt OK Input.Val="+DataStruct.RcvDeviceData.IN_CH[0].Valume);

                    }
                    Log.d(TAG, "BUG ##Get BOOLPlayHeadFlg OK");
                    //刷新界面连接进度
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_CancelDSPDataLoading);
                    mContext.sendBroadcast(intentw);

                    DataOptUtil.InitLoad();// 插入初始化申请包
                }else {
                    Message msgp = Message.obtain();
                    msgp.what = WHAT_IS_ShowGetERRMasterVolMsg;
                    mHandler.sendMessage(msgp);

                    Log.d(TAG, "BUG ##Get BOOLHaveMasterVol ERROR!!!");
                }
            }
        }
    }
    private void checkHeadDataSendCmd(){
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;
        if(DataStruct.BOOLDSPHeadFlg == Define.Statues_Null){
            Log.d(TAG, "BUG ##Get BOOLDSPHeadFlg");
            clearRecFlag();
            DataStruct.SendDeviceData.FrameType = 0xa2;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;

            DataStruct.U0SendFrameFlg = NO;//有数据更新需要发送，清除标志
            DataStruct.U0RcvFrameFlg = NO; //有新接收到数据的标志，清除标志
            OutTimeCnt = 0;
            OK = true;
            DataOptUtil.SendDataToDevice(true);//发送数据到设备
            while (DataStruct.U0RcvFrameFlg == NO) {
                if (!DataStruct.isConnecting) {
                    OK = false;
                    break;
                }
                // 连接超时处理
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
                if (++OutTimeCnt > 100) {
                    OutTimeCnt = 0;
                    OK = false;
                    break;
                }
            }
            if(OK){
                DataStruct.BOOLDSPHeadFlg = Define.Statues_YES;
                DataStruct.comDSP = Define.COMT_DSP;
                DataStruct.BOOL_CheckHEADOK = true;
                Log.d(TAG, "BUG ##Get BOOLDSPHeadFlg OK");

                //刷新界面连接进度
                Intent intentw=new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_CancelDSPDataLoading);
                mContext.sendBroadcast(intentw);

            }else {//
                DataStruct.BOOLDSPHeadFlg = Define.Statues_NO;
                Log.e(TAG, "BUG ##Get BOOLDSPHeadFlg ERROR!!!");
                DataStruct.BOOL_CheckHEADOK = false;
                if(MacCfg.BT_CUR_ConnectedName.contains(Define.BT_Paired_Name_DSP_Play)){
                    DataStruct.BOOL_CheckHEAD_ST = false;
                    if(DataStruct.BOOL_ShowCheckHeadDialog){
                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_CancelDSPDataLoading);
                        mContext.sendBroadcast(intentw);
                    }
                }else {
                    DataStruct.BOOL_CheckHEAD_ST = true;
                    //刷新界面连接进度
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_Show_HeadCheadLoading);
                    mContext.sendBroadcast(intentw);
                }
            }
        }
    }

    private void checkClientHeadDataSendCmd(){
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;
        if(DataStruct.BOOLDSPHeadFlg == Define.Statues_Null){
            Log.d(TAG, "BUG ##Get BOOLDSPHeadFlg");
            clearRecFlag();
            DataStruct.SendDeviceData.FrameType = 0xa2;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;

            DataStruct.U0SendFrameFlg = NO;//有数据更新需要发送，清除标志
            DataStruct.U0RcvFrameFlg = NO; //有新接收到数据的标志，清除标志
            OutTimeCnt = 0;
            OK = true;
            DataOptUtil.SendDataToDevice(true);//发送数据到设备
            while (DataStruct.U0RcvFrameFlg == NO) {
                if (!DataStruct.isConnecting) {
                    OK = false;
                    break;
                }
                // 连接超时处理
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
                if (++OutTimeCnt > 100) {
                    OutTimeCnt = 0;
                    OK = false;
                    break;
                }
            }
            if(OK){
                DataStruct.BOOLDSPHeadFlg = Define.Statues_YES;
                DataStruct.comDSP = Define.COMT_DSP;
                DataStruct.BOOL_CheckHEADOK = true;
                DataStruct.BOOL_CheckHEAD_ST = false;
                Log.d(TAG, "BUG ##Get BOOLDSPHeadFlg OK");

                DataStruct.BOOLHaveMasterVol = false;
                DataStruct.BOOLDSPHeadFlg = Define.Statues_YES;

                //刷新界面连接进度
                Intent intentw=new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_CancelDSPDataLoading);
                mContext.sendBroadcast(intentw);

            }else {//
                DataStruct.BOOLDSPHeadFlg = Define.Statues_NO;
                Log.e(TAG, "BUG ##Get BOOLDSPHeadFlg ERROR!!!");
                DataStruct.BOOL_CheckHEADOK = false;

            }
        }
    }



    private void sendLedPackage(){
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;
        //DSP发送LED包
        DataStruct.SendDeviceData.FrameType = 0xa2;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
        // 请求信号灯
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;

        DataStruct.U0SendFrameFlg = NO;//有数据更新需要发送，清除标志
        DataStruct.U0RcvFrameFlg = NO; //有新接收到数据的标志，清除标志
        OutTimeCnt = 0;
        RetryCnt = 0;
        // 清空标志，避免数据乱串
        clearRecFlag();
        DataOptUtil.SendDataToDevice(true);//发送数据到设备
        while (DataStruct.U0RcvFrameFlg == NO) {
            if (!DataStruct.isConnecting) {
                break;
            }
            // 连接超时处理
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("BUG sThread bt pack LED_DATA  InterruptedException");
            }
            if (BTS_Again == true) {
                BTS_Again = false;
                System.out.println("BUG ## Channel OutTimeCnt BTS_Again,LED_DATA Send again2");
                // 清空标志，避免数据乱串
                clearRecFlag();
                DataOptUtil.SendDataToDevice(true);
            }
            //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
            if (++OutTimeCnt > 1000) {
                OutTimeCnt = 0;
                // 清空标志，避免数据乱串
                clearRecFlag();
                DataOptUtil.SendDataToDevice(true);
                // 4次发送数据无回应时，断开连接
                if (++RetryCnt >= 2) {
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_ERROR;//
                    mHandler.sendMessage(msg);

                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                    intentw.putExtra("state", false);
                    mContext.sendBroadcast(intentw);
                    System.out.println("BUG sThread spp WHAT_IS_CONNECT_ERROR LED_DATA");
                    break;
                }
            }
        }
    }
    private void checkLedPackage(){
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;
        if(DataStruct.BOOL_CheckHEADOK){//已经知道包头
            ++MacCfg.LedPackageCnt;
            if(MacCfg.LedPackageCnt > 2){
                MacCfg.LedPackageCnt = 0;

                //DSP发送LED包
                DataStruct.SendDeviceData.FrameType = 0xa2;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = 0x00;
                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
                // 请求信号灯
                DataStruct.SendDeviceData.DataID = 0x00;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                DataStruct.SendDeviceData.PcCustom = 0x00;
                DataStruct.SendDeviceData.DataLen = 0x00;

                DataStruct.U0SendFrameFlg = NO;//有数据更新需要发送，清除标志
                DataStruct.U0RcvFrameFlg = NO; //有新接收到数据的标志，清除标志
                OutTimeCnt = 0;
                RetryCnt = 0;
                // 清空标志，避免数据乱串
                clearRecFlag();
                DataOptUtil.SendDataToDevice(true);//发送数据到设备
                while (DataStruct.U0RcvFrameFlg == NO) {
                    if (!DataStruct.isConnecting) {
                        break;
                    }
                    // 连接超时处理
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("BUG sThread bt pack LED_DATA  InterruptedException");
                    }
                    if (BTS_Again == true) {
                        BTS_Again = false;
                        System.out.println("BUG ## Channel OutTimeCnt BTS_Again,LED_DATA Send again2");
                        // 清空标志，避免数据乱串
                        clearRecFlag();
                        DataOptUtil.SendDataToDevice(true);
                    }
                    //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
                    if (++OutTimeCnt > 1000) {
                        OutTimeCnt = 0;
                        // 清空标志，避免数据乱串
                        clearRecFlag();
                        DataOptUtil.SendDataToDevice(true);
                        // 4次发送数据无回应时，断开连接
                        if (++RetryCnt >= 2) {
                            Message msg = Message.obtain();
                            msg.what = WHAT_IS_CONNECT_ERROR;//
                            mHandler.sendMessage(msg);

                            Intent intentw = new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                            intentw.putExtra("state", false);
                            mContext.sendBroadcast(intentw);
                            System.out.println("BUG sThread spp WHAT_IS_CONNECT_ERROR LED_DATA");
                            break;
                        }
                    }
                }
            }
        }
    }
    private void sendListPackage(){
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        clearRecFlag();
        MDef.BOOL_BluetoothBusy = true;
        DataStruct.U0SendFrameFlg = NO;
        DataStruct.U0RcvFrameFlg = NO;//有新接收到数据的标志，清除标志
        OutTimeCnt = 0;
        RetryCnt = 0;
        if (!DataOptUtil.isListNull()){//Dug:会引发空指针错误
            try{
                APP_SendPack(DataStruct.SendbufferList.get(0),
                        (DataStruct.SendbufferList.get(0).length));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        while(DataStruct.U0RcvFrameFlg == NO){
            if(!DataStruct.isConnecting){
                break;
            }
            //Delay
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("BUG sThread Delay Thread.sleep InterruptedException");
            }
            if(BTS_Again){
                BTS_Again=false;
                // 清空标志，避免数据乱串
                clearRecFlag();
                if (!DataOptUtil.isListNull()){//Dug:会引发空指针错误
                    APP_SendPack(DataStruct.SendbufferList.get(0),
                            (DataStruct.SendbufferList.get(0).length));
                }
            }
            // 发送数据无回应时，单次重试的时间间隔
            if (++OutTimeCnt > 800){
                OutTimeCnt = 0;
                System.out.println("BUG BT Send OutTimeCnt !!! ="+OutTimeCnt);
                if (!DataOptUtil.isListNull()){//Dug:会引发空指针错误
                    // 清空标志，避免数据乱串
                    clearRecFlag();
                    APP_SendPack(DataStruct.SendbufferList.get(0),
                            (DataStruct.SendbufferList.get(0).length));
                }
                if (++RetryCnt >= 2){

                                    /**/
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_ERROR;
                    mHandler.sendMessage(msg);
                    System.out.println("BUG sThread spp WHAT_IS_CONNECT_ERROR OutTimeCnt");

                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                    intentw.putExtra("state", false);
                    mContext.sendBroadcast(intentw);

                    break;

                }
            }
        }
    }
    /*********************************************************************/
    /**************************  系统主线程   TODO *************************/
    /*********************************************************************/
    // 线程:发送数据到设备 TODO
    private Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
        int IdNum = (int) Thread.currentThread().getId();

        while (true) {
            if(DataStruct.isConnecting){
                if(DataStruct.U0SendFrameFlg == NO&&DataOptUtil.isListNull()){
                    sendLedPackage();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(DataOptUtil.isListNull()&&DataStruct.U0SynDataSucessFlg){
                    DataOptUtil.ComparedToSendData(true);//经比较之前的数据（初始化数等），如有更新则发送新数据
                }
                if (!DataOptUtil.isListNull()){
                    sendListPackage();

                    if (!DataOptUtil.isListNull()){//Dug:会引发空指针错误
                        DataStruct.SendbufferList.remove(0);//成功发送，清除已经发送的列表
                    }

                    if (DataOptUtil.isListNull()){
                        if (DataStruct.SEFF_USER_GROUP_OPT > 0) {//Bug:连接上蓝牙设备，但版本号不对
                            DataStruct.SEFF_USER_GROUP_OPT = 0;
                            Message msgss = Message.obtain();
                            msgss.what = WHAT_IS_SYNC_SUCESS;
                            mHandler.sendMessage(msgss);
                        }
                    }
                    // 发送进度条消息给主线程
                    if (progressDialogStep > 0) {
                        Message msg = Message.obtain();
                        msg.what = WHAT_IS_PROGRESSDIALOG;
                        msg.arg1 = DataStruct.SendbufferList.size();
                        mHandler.sendMessage(msg);
                    }
                }else{


                    MDef.BOOL_BluetoothBusy = false;
//                    if(DataStruct.SendDeviceData == null){
//                        break;
//                    }
                    if(DataStruct.U0SendFrameFlg == NO){
                        sendLedPackage();

                    }
                }


            }

            //空闲时
            if(!DataStruct.isConnecting){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                //检测通信以便再次连接
                if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_WIFI){
                    if((mSocketClient == null)&&(!DataStruct.isConnecting)){
                        boolean wifiuse=CheckWifiStata();// 检测网络是否可用，并创建socket
                        System.out.println("BUG WIFI tRunnable CheckWifiStata():"+wifiuse);
                        if (wifiuse == true) {
                            NewSocketClient(); // 新建socket，注意在android4.0以后要在线程中创建socket，否则会抛出异常
                        }
                    }
                }else if((MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_SPP)||
                        (MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO)){
//                    if(CheckBTStata()&&(DataStruct.CanConnecting)){
//
////                        if((!DataStruct.ManualConnecting) && (!DataStruct.TryConnecting)){
////                            DataStruct.TryConnecting = true;
////                            mHandler.sendEmptyMessageDelayed(WHAT_IS_TryToConnect,5000);
////                            if(!DataStruct.BOOL_GetBluetoothList){
////                                if((++tyrConCnt) < 3){
////                                    if(mChatService != null){
////                                        mChatService.stop();
////                                        mChatService = null;
////                                    }
////
////                                    mChatService = new BluetoothChatService(mContext, mHandlerOfSPP_LE);
////                                    mOutStringBuffer = new StringBuffer("");
////                                    MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO;
////                                    // 获取本地蓝牙适配器
////                                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
////                                    BluetoothDevice Dev = mBluetoothAdapter.getRemoteDevice(MacCfg.BT_CUR_ConnectedID);
////                                    // 试图连接到设备
////                                    mChatService.connect(Dev);
////                                    //ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo));
////                                }else {
////                                    tyrConCnt = 0xff;
////                                }
////                            }
////                        }
//                    }
                }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_LE){
                    if(BLE_DEVICE_STATUS){
//                        Message msg = Message.obtain();
//                        msg.what = WHAT_IS_CONNECT_RIGHT;//
//                        mHandler.sendMessage(msg);
                    }
                }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_USB_HOST){
                    if((!DataStruct.ManualConnecting)&&(!DataStruct.isConnecting)){
                        DataStruct.ManualConnecting=false;
                        if(MacCfg.USBConnected){
                            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                                if((epOut!=null)&&(epOut!=null)){
                                    MacCfg.USBConnected=true;

                                    Message msg = Message.obtain();
                                    msg.what = WHAT_IS_CONNECT_RIGHT;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }
                        //USBHost_Connect
                    }
                }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_UART){
                    if(MacCfg.BOOL_CanLinkUART){
                        connectToDevice();
                        System.out.println("## BUG COMMUNICATION_WITH_UART Try to Link");
                    }
                }
            }

            Message msg = Message.obtain();
            msg.what = WHAT_IS_Show_Timer;
            msg.arg1 = 0;
            mHandler.sendMessage(msg);// 发送定时消息
        }
        }
    };

    private static void clearRecFlag(){
        DataStruct.U0HeadFlg = NO; // start rcv new head flag
        DataStruct.U0DataCnt = 0;  // rcv counter set 0
        DataStruct.U0HeadCnt = 0;
        BTS_Again = false;
        DataStruct.U0RcvFrameFlg = NO;
    }

    //用接收广播刷新UI TODO
    public class CHS_Broad_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg=intent.getExtras().get("msg").toString();
            System.out.println("BUG msg:"+msg);

            if(msg.equals(Define.BoardCast_Load_LocalJson)){
                if(!DataStruct.isConnecting){
                    //ToastMsg(getResources().getString(R.string.off_line_mode));
                    return;
                }

                final String filePath=intent.getExtras().get("filePath").toString();

                int fileType=DataStruct.jsonRWOpt.getSEFFFileType(mContext, filePath);
                ////ToastMsg("GetFileType="+String.valueOf(fileType));
                if(fileType == 1){
                    DataOptUtil.UpdateForJsonSingleData(filePath,mContext);
                }else if(fileType == 2){
                    DataOptUtil.loadMacEffJsonData(filePath,mContext);
                }else{
                    //ToastMsg(getResources().getString(R.string.LoadSEff_Fail));
                }
            }else if(msg.equals(Define.BoardCast_SHARE_MAC_SEFF)){
                DataOptUtil.ReadMacGroup(mContext);
            }else if(msg.equals(Define.BoardCast_SHARE_SEFF)){
                DataOptUtil.ShareEffData(mContext);
            }else if(msg.equals(Define.BoardCast_SAVE_LOCAL_SEFF)){
                Intent intentsave=new Intent();
                intentsave.setAction("android.intent.action.CHS_SEffUploadPage_BroadcastReceiver");
                intentsave.putExtra("msg", Define.BoardCast_EXIT_SEFFUploadPage);
                context.sendBroadcast(intentsave);

                boolean res=DataOptUtil.SaveSingleSEFF_JSON2Local(DataStruct.user.Get_fileName(),DataStruct.user.Get_fileName(),mContext);
                if(res){
                    //ToastMsg(getResources().getString(R.string.Save_success));
                }else{
                    //ToastMsg(getResources().getString(R.string.Save_error));
                }
            }else if(msg.equals(Define.BoardCast_LOAD_SEFF_FROM_OTHER)){
                if(!DataStruct.isConnecting){
                    //ToastMsg(getResources().getString(R.string.off_line_mode));
                    return;
                }

                Intent intentsave=new Intent();
                intentsave.setAction("android.intent.action.CHS_SEffUploadPage_BroadcastReceiver");
                intentsave.putExtra("msg", Define.BoardCast_EXIT_SEFFUploadPage);
                context.sendBroadcast(intentsave);

                String path=intent.getExtras().get("URL").toString();
                System.out.println("BUG BUG msg.equals(Define.BoardCast_LOAD_SEFF_FROM_OTHER) URL:" + path);
                int fileType=DataStruct.jsonRWOpt.getSEFFFileType(mContext, path);
                ////ToastMsg("GetFileType="+String.valueOf(fileType));
                if(fileType == 1){
                    DataOptUtil.UpdateForJsonSingleData(path,mContext);
                }else if(fileType == 2){
                    DataOptUtil.loadMacEffJsonData(path,mContext);
                }else{
                    //ToastMsg(getResources().getString(R.string.LoadSEff_Fail));
                }
            }else if(msg.equals(Define.BoardCast_INIT_BLUETOOTH_LE)){
                mDeviceName = intent.getStringExtra(BluetoothLeService.EXTRAS_DEVICE_NAME);
                mDeviceAddress = intent.getStringExtra(BluetoothLeService.EXTRAS_DEVICE_ADDRESS);
                initBluetoothLe();
            }else if(msg.equals(Define.BoardCast_OPT_DisonnectDeviceBT)){
                if(mChatService != null){
                    mChatService.stop();
                    mChatService = null;
                }
                ToastUtil.showShortToast(mContext,mContext.getResources().getString(R.string.LinkOfMsg));
            }else if(msg.equals(Define.BoardCast_ConnectToSomeoneDevice)){
                String address=intent.getStringExtra("address");
                String device=intent.getStringExtra("device");
                String type=intent.getStringExtra("type");

                MacCfg.BT_CUR_ConnectedName = device;
                MacCfg.BT_CUR_ConnectedID = address;

                System.out.println("####- BUG BLUETOOTH address->"+address
                +",device->"+device+",type->"+type);
                if(mChatService != null){
                    mChatService.stop();
                    mChatService = null;
                }
                mChatService = new BluetoothChatService(mContext, mHandlerOfSPP_LE);
                mOutStringBuffer = new StringBuffer("");
                MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO;
                // 获取本地蓝牙适配器
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice Dev = mBluetoothAdapter.getRemoteDevice(address);
                // 试图连接到设备
                mChatService.connect(Dev);
                ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo));
            }else if (msg.equals(Define.BoardCast_FlashUI_MusicPage)) {
                /*
                final String type=intent.getExtras().get(MDef.MUSICMSG_MSGTYPE).toString();
                if(type.equals(MDef.MUSICPAGE_Status)){
                }else if(type.equals(MDef.MUSICPAGE_ID3)){
                }else if(type.equals(MDef.MUSICPAGE_List)){
                }else if(type.equals(MDef.MUSICPAGE_UpdateSongInList)){
                }else if(type.equals(MDef.MUSICPAGE_FileList)){
                }else if(type.equals(MDef.MUSICPAGE_FListShowLoading)){
                }else if(type.equals(MDef.MUSICPAGE_MListShowLoading)){
                }else if(type.equals(MDef.MUSICPAGE_ShowConnectAgainMsg)){
                }
                */
            }
// else if(msg.equals(Define.BoardCast_TryConnectDSP)){
//                //只有数据的
//                //CheckHeadFromBuf();
//            }
        }
    }
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext,Msg, Toast.LENGTH_LONG);
        }
        mToast.show();
    }


    // 线程:监听服务器发来的消息
    private Runnable rRunnable = new Runnable() {
        @Override
        public void run() {

            byte[] buffer = new byte[DataStruct.U0DataLen
                    + DataStruct.CMD_LENGHT];
            int count = 0;

            int IdNum = (int) Thread.currentThread().getId();
            System.out.println("BUG rRunnable thread:" + IdNum);
            Thread.currentThread().setName("rThread");

            while (true) {
                if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_USB_HOST){
                    if((MacCfg.USBConnected)&&(mUsbDevice!=null)){
                        if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
                                mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
                            int ret = -100;
                            if(epIn!=null){
                                ret = mDeviceConnection.bulkTransfer(epIn, Receiveytes,
                                        Receiveytes.length, 10000);
                            }
                            if(ret > 0){
//                                String st=",Data<";
//                                String ss="";
//                                int len = Receiveytes.length;
//                                for(int i=0;i<len;i++){
//                                    ss=Integer.toHexString(Receiveytes[i]&0xff).toLowerCase();
//                                    if(ss.length()==1){
//                                        ss="0"+ss;
//                                    }
//                                    if((((i+1)%4)==0)&&((i+1)!=len)){
//                                        st=st+ss+" ";
//                                    }else {
//                                        st+=ss;
//                                    }
//                                }
//                                System.out.println("BUG-USB-run-接收N:"+len+st+">");

                                for (int i = 0; i < Receiveytes.length; i++) {
                                    ReceiveDataFromDevice(0xff & Receiveytes[i],
                                            Define.COMMUNICATION_WITH_USB_HOST);
                                }
                            }
                        }
                    }
                }else if(MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_WIFI){
                    if (DataStruct.isConnecting && mSocketClient != null) {
                        try {
                            InputStream in = mSocketClient.getInputStream();
                            if ((count = in.read(buffer)) > 0) {
                                for (int i = 0; i < count; i++) {
                                    ReceiveDataFromDevice(0xff & buffer[i],
                                            Define.COMMUNICATION_WITH_WIFI);
                                }
                            }
                        } catch (Exception e) {
//							recvMessageClient = "接收异常:" + e.getMessage();// 消息换行
//							Message msg = Message.obtain();
//							msg.what = WHAT_IS_CONNECT_ERROR;
//							mHandler.sendMessage(msg);
//
//							System.out.println("sThread spp WHAT_IS_CONNECT_ERROR"+recvMessageClient);
                        }
                    }
                }else {
                    break;
                }

            }
        }
    };

    Runnable tRunnable = new Runnable() {
        @Override
        public void run() {
            int IdNum = (int) Thread.currentThread().getId();
            System.out.println("BUG tRunnable thread:" + IdNum);
            Thread.currentThread().setName("tThread");
            while (true) {
//                try {
//                    Thread.sleep(100);// 线程暂停，单位毫秒
//                    Message msg = Message.obtain();
//                    msg.what = WHAT_IS_SEND_DATA;
//                    msg.arg1 = 0;//
//                    mHandler.sendMessage(msg);// 发送定时消息
//                } catch (InterruptedException e) {
//                    System.out.println("tThread timer error-leon");
//                }
            }
        }
    };

    // TODO -------Client模式下，监听服务器消息线程的消息处理
     @SuppressLint("HandlerLeak")
     static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // int IdNum = (int)Thread.currentThread().getId();
            // System.out.println("mHandler thread:"+IdNum);
            super.handleMessage(msg);

            if (msg.what == WHAT_IS_NULL) {
                ;
            } else if (msg.what == WHAT_IS_CONNECT_ERROR){ // 连接失败、错误或者用户断开连接

                disconnectSet();

            } else if (msg.what == WHAT_IS_CONNECT_RIGHT){ // 连接网络正常
                DataStruct.isConnecting = true;
                //getCheckHeadFromBuf();
                DataOptUtil.InitLoad();// 插入初始化申请包
                ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo));
            } else if (msg.what == WHAT_IS_TRYS_TO_CON_DSPPLAYMSG){ // 连接网络正常
                //ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo)+" DSP Play");
            } else if (msg.what == WHAT_IS_ShowGetERRMasterVolMsg){ // 连接网络正常

            } else if (msg.what == WHAT_IS_Show_UnKnowMacType_Msg){ // 连接网络正常
            } else if (msg.what == WHAT_IS_LongPress_INC_SUB){  // 刷新长按按键的连续增减界面
                //FlashButtonLongPressUI();
            } else if (msg.what == WHAT_IS_Show_Timer){  // 刷新长按按键的连续增减界面
                //FlashButtonLongPressUI();
//                boolean status = DataOptUtil.getSoundStatus(mContext);
//                if(status != MacCfg.BOOL_SoundStatues){
//                    MacCfg.BOOL_SoundStatues = status;
//                    //刷新界面连接进度
//                    Intent intentw=new Intent();
//                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                    intentw.putExtra("msg", Define.BoardCast_FlashUI_FlashSoundStatus);
//                    intentw.putExtra("state", status);
//                    mContext.sendBroadcast(intentw);
//                }
            } else if (msg.what == WHAT_IS_FLASH_BT_CONNECTED){  // 刷新蓝牙的音频通道是否已经连接本机
                //                if((MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_SPP)||
//                        (MacCfg.COMMUNICATION_MODE==Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO)){
//                    Thread thread=new Thread(new Runnable(){
//                        @Override
//                        public void run(){
//                            FlashBTConnectedMusicChannel();
//                        }
//                    });
//                    thread.start();
//
//                }
            } else if (msg.what == WHAT_IS_INIT_LOADING){//当开机发送数据后，到一个正解的应答后开始显示加载进度
                //刷新界面连接进度
                Intent intentw=new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_SYNC_SUCESS) {
                Log.e(TAG,"WHAT_IS_SYNC_SUCESS");
                connectSet();
                DataOptUtil.saveBluetoothInfo(mContext);
                DataOptUtil.saveHEAD_DATA(mContext);
            } else if (msg.what == WHAT_IS_NEW_DATA){ // 有新数据到来
            } else if (msg.what == WHAT_IS_SEND_DATA){ // 定时器消息,发送数据给设备
                if (++WifiInfoTimerCnt > 10) {
                    WifiInfoTimerCnt = 0;
                }
            } else if (msg.what == WHAT_IS_PROGRESSDIALOG){ // 更新progressDialog

            }else if (msg.what == WHAT_IS_FLASH_SYSTEM_DATA){
                Intent intentw=new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_SYSTEM_DATA);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_LEDUP_SOURCE){ // 信号灯实时更新音源
           //     if(DataStruct.CurMacMode.BOOL_INPUT_SOURCE){
                    DataStruct.RcvDeviceData.SYS.input_source = msg.arg1;
                    DataStruct.SendDeviceData.SYS.input_source = msg.arg1;
                    //刷新界面连接
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_InputSource);
                    intentw.putExtra("state", false);
                    mContext.sendBroadcast(intentw);
             //   }

            } else if (msg.what == WHAT_IS_SYNC_GROUP_NAME) {
            }  else if (msg.what == WHAT_IS_RETURN_EXIT) {
               // isExit = false;
            }  else if (msg.what == WHAT_IS_CLOSE_BT) {
                bool_CloseBT = false;
            }  else if(msg.what == WHAT_IS_RESET_EQ_CHNAME) {

            }  else if(msg.what == WHAT_IS_GroupClick) {

            }  else if(msg.what == WHAT_IS_BT_TIME_OUT) {//蓝牙超时
                if((mSocketClient == null)&&(!DataStruct.isConnecting)){
                    bool_BT_ConTimeOut=true;
                    bool_BT_CTO_Send=false;

                }
            }  else if(msg.what == WHAT_IS_MENU_LOCKED) {
//				bool_MenuLocked=false;
            }   else if(msg.what == WHAT_IS_Wait) {
//				bool_Wait=false;
            }   else if(msg.what == WHAT_IS_LOADING) {
              //  LoadingCancle();
            }   else if(msg.what == WHAT_IS_BT_SCAN) {
                startScanBluetooth();
            }
//            else if(msg.what == WHAT_IS_TryToConnect) {
////                DataStruct.TryConnecting = false;
//            }
        }
    };

    public static void disconnectSet(){

        DataStruct.U0SynDataSucessFlg = false;
        DataStruct.isConnecting = false;
        MacCfg.bool_ReadMacGroup = false;
        DataStruct.U0HeadFlg = NO; // start rcv new head flag
        DataStruct.U0DataCnt = 0;  // rcv counter set 0
        DataStruct.U0HeadCnt = 0;

        DataStruct.BOOLDSPHeadFlg = Define.Statues_Null;//0：未知是否可用，1：明确不可用，2:明确可用
        DataStruct.BOOLPlayHeadFlg = Define.Statues_Null;//0：未知是否可用，1：明确不可用，2:明确可用
        DataStruct.BOOLHaveMasterVol = false;//false：未知获取主音量，true:已获取主音量

        DataStruct.U0RcvFrameFlg = NO; // 有新接收到数据的标志
        DataStruct.U0SendFrameFlg = NO; // 有数据要发送的标志
        DataStruct.U0SynDataError = false; // 同步初始化数据是否出错
        DataStruct.PcConnectFlg = NO;
        DataStruct.PcConnectCnt = 0;
        MDef.BOOL_ShowConnectAgainMsg = false;
        MDef.BOOL_BluetoothBusy = false;
        //中间变量
        MDef.U0HeadCnt = 0;
        MDef.U0HeadFlag = false;
        MDef.U0DataCnt = 0;
        MDef.U0RecFrameFlg = false;
        if(DataStruct.CurMusic != null){
            DataStruct.CurMusic.resetData();
        }


        bool_OpBT=false; //打开蓝牙完成，连接不成功
        DataStruct.U0HeadFlg = NO;  //设置包头无效
        MacCfg.UpdataAduanceData = false;
        //刷新界面连接按键
        Intent intentw=new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectState);
        intentw.putExtra("state", false);
        mContext.sendBroadcast(intentw);
        if (DeviceVerErrorFlg) {
            //刷新界面连接
            Intent intentd=new Intent();
            intentd.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intentd.putExtra("msg", Define.BoardCast_FlashUI_DeviceVersionsErr);
            intentd.putExtra("state", false);
            mContext.sendBroadcast(intentd);
        }
        //延时后再执行关闭接口，保存正在发送的数据发送完成，否则会引起发送线程的崩溃
        //解放连接资源
        if(mChatService != null){
            mChatService.stop();
            mChatService = null;
        }

        try {
            closeSerialPort();

            if (mSocketClient != null) {
                mSocketClient.close();
                mSocketClient = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connectToDevice(){
        if(MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART){
            OpenSerialPort();
        }else if(MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_USB_HOST){

        }
    }

    private static void connectSet(){
        DataOptUtil.ComparedToSendData(false); //同步数据
        DataStruct.SendbufferList.clear();
		
        DataStruct.CanConnecting = true;
        DataStruct.ManualConnecting = false;
        DataStruct.U0SynDataSucessFlg = true; //同步数据完成标记
        DataStruct.isConnecting = true;  //蓝牙可正常连接
        bool_BT_ConTimeOut = false;
        BLE_DEVICE_STATUS = true;
        DataStruct.SEFF_USER_GROUP_OPT = 0;
//        tyrConCnt = 0;
        //刷新界面连接按键
        Intent intentwt=new Intent();
        intentwt.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentwt.putExtra("msg", Define.BoardCast_FlashUI_ConnectState);
        intentwt.putExtra("state", true);
        mContext.sendBroadcast(intentwt);

        Intent intentm=new Intent();
        intentm.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentm.putExtra("msg", Define.BoardCast_FlashUI_ShowSucessMsg);
        intentm.putExtra("state", true);
        mContext.sendBroadcast(intentm);
        //刷新界面连接
        Intent intentw=new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
        intentw.putExtra("state", true);
        mContext.sendBroadcast(intentw);

    }
    // 接收数据，检包
    public static void ReceiveDataFromDevice(int data, int type) {

        if(DataStruct.RcvDeviceData == null){
            return;
        }

        //if(MacCfg.BOOL_USE_MusicBox){
        //    MDOptUtil.RecMusicBoxDataFromDevice(mContext,data & 0xff,Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO);
        //}
        /**/

        //判断包头，起始符，贞头

        if (DataStruct.U0HeadFlg == NO) {// DataStruct.U0HeadFlg=NO,rcv head data
            if((data == MacCfg.HEAD_DATA)&&(DataStruct.U0HeadCnt == 0)) {
                DataStruct.U0HeadCnt++;
            }else if((data == MacCfg.HEAD_DATA)&&(DataStruct.U0HeadCnt == 1)) {
                DataStruct.U0HeadCnt++;
            }else if((data == MacCfg.HEAD_DATA)&&(DataStruct.U0HeadCnt == 2)) {
                DataStruct.U0HeadCnt++;
            }else if (data == DataStruct.FRAME_STA && DataStruct.U0HeadCnt == 3){// Have rcv // 0xff,0xff,0xff,0xb1
                DataStruct.U0HeadFlg = YES;
                DataStruct.U0HeadCnt = 0;
            } else {
                DataStruct.U0HeadCnt = 0;
            }
            DataStruct.U0DataCnt = 0; // Ready rcv data, rcv cnt set 0
         }
        //有效包
        else if (DataStruct.U0HeadFlg == YES) {
            DataStruct.U0HeadCnt = 0;

            //BUG
            if(DataStruct.U0DataCnt >= DataStruct.RcvDeviceData.DataBuf.length-1){
                DataStruct.U0HeadFlg = NO; // start rcv new head flag
                DataStruct.U0DataCnt = 0;  // rcv counter set 0
                BTS_Again=true;
                return;
            }

            DataStruct.RcvDeviceData.DataBuf[DataStruct.U0DataCnt] = data;
            DataStruct.U0DataCnt++;
            if (DataStruct.U0DataCnt >= (DataStruct.RcvDeviceData.DataBuf[8]
                    + DataStruct.RcvDeviceData.DataBuf[9] * 256 + DataStruct.CMD_LENGHT - 4)) // ²»°üº¬°üÍ·ºÍÆðÊ¼·û£¬¼õ4
            {
                DataStruct.RcvDeviceData.FrameType = DataStruct.RcvDeviceData.DataBuf[0];
                DataStruct.RcvDeviceData.DeviceID  = DataStruct.RcvDeviceData.DataBuf[1];
                DataStruct.RcvDeviceData.UserID    = DataStruct.RcvDeviceData.DataBuf[2];
                DataStruct.RcvDeviceData.DataType  = DataStruct.RcvDeviceData.DataBuf[3];
                DataStruct.RcvDeviceData.ChannelID = DataStruct.RcvDeviceData.DataBuf[4];
                DataStruct.RcvDeviceData.DataID    = DataStruct.RcvDeviceData.DataBuf[5];
                DataStruct.RcvDeviceData.PCFadeInFadeOutFlg = DataStruct.RcvDeviceData.DataBuf[6];
                DataStruct.RcvDeviceData.PcCustom  = DataStruct.RcvDeviceData.DataBuf[7];
                DataStruct.RcvDeviceData.DataLen   = DataStruct.RcvDeviceData.DataBuf[8]
                        + DataStruct.RcvDeviceData.DataBuf[9] * 256;
                DataStruct.RcvDeviceData.CheckSum  = DataStruct.RcvDeviceData.DataBuf[DataStruct.RcvDeviceData.DataLen
                        + DataStruct.CMD_LENGHT - 6];
                DataStruct.RcvDeviceData.FrameEnd  = DataStruct.RcvDeviceData.DataBuf[DataStruct.RcvDeviceData.DataLen
                        + DataStruct.CMD_LENGHT - 5];

                DataStruct.U0HeadFlg = NO; // start rcv new head flag
                DataStruct.U0DataCnt = 0;  // rcv counter set 0

                if (DataStruct.RcvDeviceData.FrameEnd == DataStruct.FRAME_END){ // 判断包尾是否正确
                    int sum = 0;
                    for (int i = 0; i < (DataStruct.RcvDeviceData.DataLen
                            + DataStruct.CMD_LENGHT - 6); i++) {
                        sum ^= DataStruct.RcvDeviceData.DataBuf[i];
                    }

                    if (sum == DataStruct.RcvDeviceData.CheckSum){ // 通过校验
                        DataStruct.PcConnectFlg = YES;
                        DataStruct.PcConnectCnt = 0;
                        DataStruct.ComType = type; // 通讯类型
                        DataStruct.comDSP = Define.COMT_DSP;
                        ProcessRcvData();
                    }else {
                        BTS_Again=true;
                        System.out.println("BUG XXXXXXXX CheckSum ERROR!!");
                    }
                }else {
                    BTS_Again=true;
                    System.out.println("BUG XXXXXXXX  FRAME_END ERROR!! ");
                }
            }
        }

    }

    // 处理检包后的数据，分类存储器起来 TODO
    public static void ProcessRcvData() {
//		System.out.println("BUG XXXXXXXXXXXXXXXXXXXXXX");
//
//		System.out.println("BUG ProcessRcvData FrameType:" + DataStruct.RcvDeviceData.FrameType);
//		System.out.println("BUG ProcessRcvData DataType:" + DataStruct.RcvDeviceData.DataType);
//		System.out.println("BUG ProcessRcvData ChannelID:" + DataStruct.RcvDeviceData.ChannelID);

        if (DataStruct.RcvDeviceData.FrameType == DataStruct.DATA_ACK){ // 数据回应帧
            if (DataStruct.RcvDeviceData.DataType == Define.EFF) {
//                if(MacCfg.bool_ReadMacGroup){
//                    for(int i=0;i<Define.EFF_LEN;i++){
//                        DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].eff.eff[0][i] =DataStruct.RcvDeviceData.DataBuf[10+i];
//                    }
//                }else {
//                    if(DataStruct.RcvDeviceData.DataLen == Define.EFF_LEN){
//                        DataOptUtil.FillRecDataStruct(Define.EFF, 0, DataStruct.RcvDeviceData.DataBuf, true);
//                    }
//                }

            }else if (DataStruct.RcvDeviceData.DataType == Define.MUSIC) {/*读取整MUSIC.2通道的整个数据*/
                if(DEBUG) System.out.println("BUG ## Channel MUSIC DataLen:" + DataStruct.RcvDeviceData.DataLen);
                if(DataStruct.CurMacMode.BOOL_USE_INS){
                    if(DataStruct.RcvDeviceData.DataLen == Define.INS_LEN){
                        DataOptUtil.FillRecDataStruct(Define.MUSIC, DataStruct.RcvDeviceData.ChannelID, DataStruct.RcvDeviceData.DataBuf, true);
                        //保存整机数据
                    }
                }else{
                    if(DataStruct.RcvDeviceData.DataLen == Define.IN_LEN){
                        //保存整机数据
                        if(MacCfg.bool_ReadMacGroup){
                            for(int i=0;i<Define.IN_LEN;i++){
                                 DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].music.music[0][i] =DataStruct.RcvDeviceData.DataBuf[10+i];
                            }
                        }else {
                            DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, DataStruct.RcvDeviceData.DataBuf, true);
                            //System.out.println("BUG ---Master Valume:" + DataStruct.RcvDeviceData.IN_CH[0].Valume);
                            Message msgd = Message.obtain();
                            msgd.what = WHAT_IS_FLASH_SYSTEM_DATA;
                            msgd.arg1 = MacCfg.input_sourcetemp;
                            mHandler.sendMessage(msgd);
                        }
                    }
                }

            } else if (DataStruct.RcvDeviceData.DataType == Define.OUTPUT) {

                Log.e(TAG,"BUG ## Channel OUTPUT ChannelID:" + DataStruct.RcvDeviceData.ChannelID);
                if(DataStruct.RcvDeviceData.DataLen==Define.OUT_LEN){

                    if(MacCfg.bool_ReadMacGroup){
                        System.out.println("BUG ## MAC UserID:" + DataStruct.RcvDeviceData.UserID
                                +"-ChannelID="+DataStruct.RcvDeviceData.ChannelID
                                +",DataStruct.OUT_LEN="+DataStruct.RcvDeviceData.DataLen);
                        for(int i=0;i<Define.OUT_LEN;i++){
                            DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].output.output[DataStruct.RcvDeviceData.ChannelID][i]=DataStruct.RcvDeviceData.DataBuf[10+i];
                        }

                        //保存够一组数据了
                        if(DataStruct.RcvDeviceData.ChannelID==(DataStruct.CurMacMode.Out.OUT_CH_MAX-1)){
                            //保存够整机数据了
                            if(DataStruct.RcvDeviceData.UserID == DataStruct.CurMacMode.MAX_USE_GROUP){
                                //填充整机数据
                                DataStruct.MAC_DataBuf.Set_chs(DataOptUtil.getCHS(mContext));
                                DataStruct.MAC_DataBuf.Set_client(DataOptUtil.getClient());
                                DataStruct.MAC_DataBuf.Set_data_info(DataOptUtil.getData_info());
                                DataStruct.MAC_DataBuf.Set_SystemData(DataOptUtil.getSystemData());

                                if(DataStruct.bool_ShareOrSaveMacSEFF){
                                    DataOptUtil.ShareMacEffData(mContext);
                                }else{
                                    DataOptUtil.SaveMACSEFF_JSON2Local(DataStruct.fileNameString,mContext);
                                 }
                                MacCfg.bool_ReadMacGroup=false;
                            }
                        }
                    }else{//读取整机时不刷新数据
                        DataOptUtil.FillRecDataStruct(Define.OUTPUT, DataStruct.RcvDeviceData.ChannelID, DataStruct.RcvDeviceData.DataBuf, true);
                        //DataOptUtil.SaveEQTo_EQ_Buf(RcvDeviceData.ChannelID);

                        //检测数据是否出错
                        if(DataStruct.RcvDeviceData.ChannelID==(DataStruct.CurMacMode.Out.OUT_CH_MAX-1)){
                            MacCfg.UpdataAduanceData = true;
                            if(DataStruct.U0SynDataError){
                                //showResetMucDialog();
                            }
                        }
                    }
                }
            } else if (DataStruct.RcvDeviceData.DataType == Define.SYSTEM) {
                switch (DataStruct.RcvDeviceData.ChannelID) {
                    case Define.GROUP_NAME:
                        for (int i = 0; i < 16; i++){
                            DataStruct.RcvDeviceData.SYS.UserGroup[DataStruct.RcvDeviceData.UserID][i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i]; // 接收用户名
                            DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].group_name[i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i];
                        }

                        break;

                    case Define.PC_SOURCE_SET:

                        if(DataStruct.RcvDeviceData.DataLen == 8){
                            DataStruct.MasterConType = true;
                            System.out.println("BUG ....---===-->:Master Volume-->System");
                        }else {
                            DataStruct.MasterConType = true;
                            System.out.println("BUG ....---===-->:Master Volume-->Input");
                        }

                        DataStruct.RcvDeviceData.SYS.input_source = DataStruct.RcvDeviceData.DataBuf[10]; // 输入源(之前系统中的输入源)
                        // 高 1
                        // 低
                        // AUX 3
                        // 蓝牙  2
                        // 光纤
                        DataStruct.RcvDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.DataBuf[11]; // 低电平模式
                        // 有3种
                        // 0:4个AUX
                        // 1:..................
                        DataStruct.RcvDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.DataBuf[12];  // 本字节第二位0x02
                        // 代表有数字音源输入，字节第一位0x01代表有蓝牙输入，否则没有该模块，PC不能切换至此音源
                        DataStruct.RcvDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.DataBuf[13];// 保留
                        DataStruct.RcvDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.DataBuf[14];// 保留
                        DataStruct.RcvDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.DataBuf[15];// 保留
                        DataStruct.RcvDeviceData.SYS.Safety = DataStruct.RcvDeviceData.DataBuf[16];// 保留
                        DataStruct.RcvDeviceData.SYS.sound_effect = DataStruct.RcvDeviceData.DataBuf[17];// 保留

                        break;

                    case Define.MCU_BUSY:
                        // U0BusyFlg = YES;
                        break;
                    case Define.SOFTWARE_VERSION:
                        char[] databuf = new char[12];
                        databuf[11] = 0;
                        for (int i = 0; i < 11; i++) {
                            databuf[i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i];
                        }
                        MacCfg.DeviceVerString = String.valueOf(databuf);
                        if(!MacCfg.DeviceVerString.contains(MacCfg.MCU_Versions)){
                            DeviceVerErrorFlg = true;
                            if(!MacCfg.BOOL_ANYCON){
                                //没有匹配到机型
                                DeviceVerErrorFlg = true;
                                return;
                            }
                        }

                        //当开机发送数据后，到一个正确的MCU_Versions后开始显示加载进度
                        if(DataStruct.B_InitLoad==true){
                            DataStruct.B_InitLoad=false;
                            if(BT_COther==true){
                                BT_COther = false;
                                MacCfg.BTManualConnect=false;
                            }
                        }

                        Message msgil = Message.obtain();
                        msgil.what = WHAT_IS_INIT_LOADING;
                        mHandler.sendMessage(msgil);

                        bool_OpBT=false;//打开蓝牙完成
                        break;

                    case Define.SYSTEM_DATA:
                        DataStruct.RcvDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.DataBuf[10]
                                + DataStruct.RcvDeviceData.DataBuf[11] * 256;  // 输出总音量(之前输入结构中的总音量)
                        // -60~0dB
                        DataStruct.RcvDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.DataBuf[12]; // DSP纯延时
                        // 0~100
                        // 0.01s~1s
                        DataStruct.RcvDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.DataBuf[13];// 噪声门
                        // -120dbu~+10dbu,stp:1dbu,实际发送0~130
                        DataStruct.RcvDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.DataBuf[14];    // 自动音源开关
                        // 0关
                        // 1开
                        DataStruct.RcvDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.DataBuf[15];  // 自动音源检测的信号阀值
                        // -120dB~0dB
                        DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.DataBuf[16];// 静音临时标志，这个标志关机不保存，注意特别处理
                        DataStruct.RcvDeviceData.SYS.offTime = DataStruct.RcvDeviceData.DataBuf[17];// 保留
                        DataStruct.SysMainValBuf = DataStruct.RcvDeviceData.SYS.main_vol;
                        Message msgd = Message.obtain();
                        msgd.what = WHAT_IS_FLASH_SYSTEM_DATA;
                        msgd.arg1 = MacCfg.input_sourcetemp;
                        mHandler.sendMessage(msgd);
                        break;
                    case Define.SYSTEM_SPK_TYPE:
//                        for(int i=0;i<8;i++){
//                            if(DataStruct.RcvDeviceData.DataBuf[10+i] > MacCfg.SPK_MAX){
//                                DataStruct.RcvDeviceData.DataBuf[10+i] = 0;
//                            }
//
//                            if(DataStruct.RcvDeviceData.DataBuf[10+i] < 0){
//                                DataStruct.RcvDeviceData.DataBuf[10+i]=0;
//                            }
//
//                        }
                        DataStruct.RcvDeviceData.SYS.out1_spk_type=DataStruct.RcvDeviceData.DataBuf[10];
                        DataStruct.RcvDeviceData.SYS.out2_spk_type=DataStruct.RcvDeviceData.DataBuf[11];
                        DataStruct.RcvDeviceData.SYS.out3_spk_type=DataStruct.RcvDeviceData.DataBuf[12];
                        DataStruct.RcvDeviceData.SYS.out4_spk_type=DataStruct.RcvDeviceData.DataBuf[13];
                        DataStruct.RcvDeviceData.SYS.out5_spk_type=DataStruct.RcvDeviceData.DataBuf[14];
                        DataStruct.RcvDeviceData.SYS.out6_spk_type=DataStruct.RcvDeviceData.DataBuf[15];
                        DataStruct.RcvDeviceData.SYS.out7_spk_type=DataStruct.RcvDeviceData.DataBuf[16];
                        DataStruct.RcvDeviceData.SYS.out8_spk_type=DataStruct.RcvDeviceData.DataBuf[17];
                        break;
                    case Define.SYSTEM_SPK_TYPEB:
//                        DataStruct.RcvDeviceData.SYS.CurGroupID=DataStruct.RcvDeviceData.DataBuf[10];
//
//
//
//                        DataStruct.RcvDeviceData.SYS.out10_spk_type=DataStruct.RcvDeviceData.DataBuf[11];
//                        DataStruct.RcvDeviceData.SYS.out11_spk_type=DataStruct.RcvDeviceData.DataBuf[12];
//                        DataStruct.RcvDeviceData.SYS.out12_spk_type=DataStruct.RcvDeviceData.DataBuf[13];
//                        DataStruct.RcvDeviceData.SYS.out13_spk_type=DataStruct.RcvDeviceData.DataBuf[14];
//                        DataStruct.RcvDeviceData.SYS.out14_spk_type=DataStruct.RcvDeviceData.DataBuf[15];
//                        DataStruct.RcvDeviceData.SYS.out15_spk_type=DataStruct.RcvDeviceData.DataBuf[16];
//                        DataStruct.RcvDeviceData.SYS.out16_spk_type=DataStruct.RcvDeviceData.DataBuf[17];
                        //System.out.println("BUG FUCK:  case Define.SYSTEM_SPK_TYPEB out16_spk_type:=" + DataStruct.RcvDeviceData.SYS.out16_spk_type);
                        break;


                    case Define.LED_DATA:
                        if (MacCfg.input_sourcetemp != DataStruct.RcvDeviceData.DataBuf[10 ]) {
                            MacCfg.input_sourcetemp = DataStruct.RcvDeviceData.DataBuf[10];
                            Message msg = Message.obtain();
                            msg.what = WHAT_IS_LEDUP_SOURCE;
                            msg.arg1 = MacCfg.input_sourcetemp;// 信号灯实时输入源
                            mHandler.sendMessage(msg);
                        }
                      //  System.out.println("BUG 收到的辅助音源"+ DataStruct.RcvDeviceData.DataBuf[11 ]);
//                        if( MacCfg.input_sourceAuxliary!= DataStruct.RcvDeviceData.DataBuf[11 ]){
//                            DataStruct.RcvDeviceData.SYS.aux_mode= MacCfg.input_sourceAuxliary;
//                            //DataStruct.RcvDeviceData.SYS.aux_mode = MacCfg.input_sourceAuxliary;
//                            Intent intentw=new Intent();
//                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                            intentw.putExtra("msg", Define.BoardCast_FlashUI_SYSTEM_DATA);
//                            intentw.putExtra("state", false);
//                            mContext.sendBroadcast(intentw);
//                        }

                        if(MacCfg.main_Vol_Change!= DataStruct.RcvDeviceData.DataBuf[18 ]+ DataStruct.RcvDeviceData.DataBuf[19] * 256){
                            MacCfg.main_Vol_Change= DataStruct.RcvDeviceData.DataBuf[18]+ DataStruct.RcvDeviceData.DataBuf[19] * 256;
                            DataStruct.RcvDeviceData.SYS.main_vol = MacCfg.main_Vol_Change;
                                                        Intent intentw=new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_SYSTEM_DATA);
                            intentw.putExtra("state", false);
                            mContext.sendBroadcast(intentw);
                        }




                        if(MacCfg.mute_Vol_Change!= DataStruct.RcvDeviceData.DataBuf[24 ]){

                            MacCfg.mute_Vol_Change= DataStruct.RcvDeviceData.DataBuf[24];

                            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = MacCfg.mute_Vol_Change;
                            Intent intentw=new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_SYSTEM_DATA);
                            intentw.putExtra("state", false);
                            mContext.sendBroadcast(intentw);
                        }


//                        if((DataStruct.RcvDeviceData.SYS.input_MicVol
//                                != DataStruct.RcvDeviceData.DataBuf[10 + 11])||
//                                (DataStruct.RcvDeviceData.SYS.input_EffVol
//                                        != DataStruct.RcvDeviceData.DataBuf[10 + 12])||
//                                (DataStruct.RcvDeviceData.SYS.input_MusicVol
//                                        != DataStruct.RcvDeviceData.DataBuf[10 + 13])){
//
//
//                            Log.e(TAG, "ProcessRcvData: LED_DATA");
//
//                            DataStruct.RcvDeviceData.SYS.input_MicVol
//                                    = DataStruct.RcvDeviceData.DataBuf[10 + 11];
//                            DataStruct.RcvDeviceData.SYS.input_EffVol
//                                    = DataStruct.RcvDeviceData.DataBuf[10 + 12];
//                            DataStruct.RcvDeviceData.SYS.input_MusicVol
//                                    = DataStruct.RcvDeviceData.DataBuf[10 + 13];
//
//                            //刷新界面连接
//                            Intent intentw=new Intent();
//                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                            intentw.putExtra("msg", Define.BoardCast_FlashUI_EFFPageVol);
//                            intentw.putExtra("state", false);
//                            mContext.sendBroadcast(intentw);
//
//                        }

                        break;
                    case Define.CUR_PROGRAM_INFO:
                        DataStruct.RcvDeviceData.SYS.CurGroupID = DataStruct.RcvDeviceData.DataBuf[10]&0xff;// 当前用户组ID

                        break;
                    case Define.SOUND_FIELD_INFO:
                        //if(DEBUG) System.out.println("## Channel System-SOUND_FIELD_INFO:" + DataStruct.RcvDeviceData.DataLen);
                        DataOptUtil.FillDelayDataBySystemChannel(DataStruct.RcvDeviceData.DataBuf,DataStruct.RcvDeviceData.DataLen,true,true);
                        break;
                    case Define.SYSTEM_Group:
                        DataStruct.RcvDeviceData.SYS.Play_vol=DataStruct.RcvDeviceData.DataBuf[10];
                        DataStruct.RcvDeviceData.SYS.Start_Def_source=DataStruct.RcvDeviceData.DataBuf[11];
                        DataStruct.RcvDeviceData.SYS.Source_type=DataStruct.RcvDeviceData.DataBuf[12];
                        DataStruct.RcvDeviceData.SYS.none4=DataStruct.RcvDeviceData.DataBuf[13];
                        DataStruct.RcvDeviceData.SYS.SubVol=DataStruct.RcvDeviceData.DataBuf[14]+DataStruct.RcvDeviceData.DataBuf[15]*256;
                        DataStruct.RcvDeviceData.SYS.none5[0]=DataStruct.RcvDeviceData.DataBuf[16];
                        DataStruct.RcvDeviceData.SYS.none5[1]=DataStruct.RcvDeviceData.DataBuf[17];
                        DataStruct.RcvDeviceData.SYS.out16_spk_type=DataStruct.RcvDeviceData.DataBuf[18];
                        break;
                    default:
                        break;
                }
            } else {
                ;
            }

            DataStruct.U0RcvFrameFlg = YES; // rcv one frame end
//            DataStruct.isConnecting = true;  // 蓝牙可正常连接
//            DataStruct.isConnecting  = true;
//            if(DataStruct.isConnecting != DataStruct.isConnectingOld){
//                DataStruct.isConnectingOld = DataStruct.isConnecting;
//                //setConnectStatus(DataStruct.isConnecting);
//            }
        } else if (DataStruct.RcvDeviceData.FrameType == DataStruct.ERROR_ACK){ // 错误回应
            BTS_Again=true;
        } else if (DataStruct.RcvDeviceData.FrameType == DataStruct.RIGHT_ACK){ // 正确回应
//            DataStruct.isConnecting  = true;
//            if(DataStruct.isConnecting != DataStruct.isConnectingOld){
//                DataStruct.isConnectingOld = DataStruct.isConnecting;
//                //setConnectStatus(DataStruct.isConnecting);
//            }
            DataStruct.U0RcvFrameFlg = YES; // rcv one frame end
//            DataStruct.isConnecting = true;  // 蓝牙可正常连接
        } else {
            ;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////   蓝牙监听连接    ////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private void initBluetoothReceiver(){
        // 注册Receiver来获取蓝牙设备相关的结果
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);

        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(ACTION_PAIRING_REQUEST);
        this.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = null;

            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){

                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //System.out.println("BUG BOND_STATE:"+device.getName() +"ACTION_BOND_STATE_CHANGED");
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        break;
                    case BluetoothDevice.BOND_NONE:
                    default:
                        break;
                }
            } else if (intent.getAction().equals(ACTION_PAIRING_REQUEST)) {

            }//A2DP连接状态改变
            else if(action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)){
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                Log.i(TAG,"connect state===-->"+state);

//                if(isCHSBluetooth(device.getName())){
//                    if(state == BluetoothA2dp.STATE_CONNECTED){
//                        Log.e(TAG,"connect state===-->A2DP连接===GOOG!!!!");
//                        if((!DataStruct.isConnecting)&&(!DataStruct.ManualConnecting)){
//                            if(mChatService != null){
//                                mChatService.stop();
//                                mChatService = null;
//                            }
//
//                            mChatService = new BluetoothChatService(mContext, mHandlerOfSPP_LE);
//                            mOutStringBuffer = new StringBuffer("");
//                            MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO;
//                            // 获取本地蓝牙适配器
//                            //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                            //BluetoothDevice Dev = mBluetoothAdapter.getRemoteDevice(MacCfg.BT_CUR_ConnectedID);
//                            // 试图连接到设备
//                            mChatService.connect(device);
//                        }
//                    }
//                }
            }else if(action.equals(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED)){
                //A2DP播放状态改变
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_NOT_PLAYING);
                Log.i(TAG,"play state="+state);
            }
        }
    };
    private boolean isCHSBluetooth(String deviceName){
        if((deviceName.contains(Define.BT_Paired_Name_DSP_HD_))
                ||(deviceName.contains(Define.BT_Paired_Name_DSP_CCS))
                ||(deviceName.contains(Define.BT_Paired_Name_DSP_HDS))
                ||(deviceName.contains(Define.BT_Paired_Name_DSP_Play))){
            if(deviceName.contains("DSP Play ble")){
                return false;
            }
            return true;
        }
        return false;
    }
    public void serviceOnDestroy() {
        if(mReceiver != null){
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        if(rThread!=null){
            rThread.interrupt(); // 关闭接收线程
            rThread=null;
        }
        if(sThread!=null){
            sThread.interrupt(); // 关闭接收线程
            sThread=null;
        }
        if(tThread!=null){
            tThread.interrupt(); // 关闭接收线程
            tThread=null;
        }
        if(CHS_Broad_Receiver!=null){
            unregisterReceiver(CHS_Broad_Receiver);
            CHS_Broad_Receiver = null;
        }
        MacCfg.BOOL_CanLinkUART = false;

        DataStruct.U0SynDataSucessFlg = false;
        DataStruct.isConnecting = false;
        MacCfg.bool_ReadMacGroup = false;
        DataStruct.U0HeadFlg = NO; // start rcv new head flag
        DataStruct.U0DataCnt = 0;  // rcv counter set 0
        DataStruct.U0HeadCnt = 0;

        DataStruct.U0RcvFrameFlg = NO; // 有新接收到数据的标志
        DataStruct.U0SendFrameFlg = NO; // 有数据要发送的标志
        DataStruct.U0SynDataError = false; // 同步初始化数据是否出错
        DataStruct.PcConnectFlg = NO;
        DataStruct.PcConnectCnt = 0;


        //中间变量
        MDef.U0HeadCnt = 0;
        MDef.U0HeadFlag = false;
        MDef.U0DataCnt = 0;
        MDef.U0RecFrameFlg = false;

        bool_OpBT=false; //打开蓝牙完成，连接不成功
        DataStruct.U0HeadFlg = NO;  //设置包头无效
        MacCfg.UpdataAduanceData = false;

        for(int i=0;i<=WHAT_IS_Max;i++){
            mHandler.removeMessages(i);
        }
        mHandler.removeCallbacksAndMessages(null);

        if(mChatService != null){
            mChatService.stop();
            mChatService = null;
        }

        try {
            if (mSocketClient != null) {
                mSocketClient.close();
                mSocketClient = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mBluetoothLeService != null){
            mBluetoothLeService.close();
            mBluetoothLeService = null;
        }

        if(mUsbReceiver!=null){
            unregisterReceiver(mUsbReceiver);
        }
        
        if(mChatService != null){
            mChatService.stop();
            mChatService = null;
        }

        closeSerialPort();

        DataOptUtil.ExitDatabases();

        super.onDestroy();
    }





}
