package com.chs.mt.pxe_r500.bluetooth.spp_ble;
/**
 * 描述：此类为扫描和连接蓝牙设备弹出框
 * 作用：扫描和连接蓝牙设备
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bean.BTinfo;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.operation.AnimationUtil;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.util.ToolsUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeviceListActivity extends Activity {

    //该UUID表示串口服务
    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    static final UUID uuid = UUID.fromString(SPP_UUID);
    static String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    // Debugg
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_Name = "device_name";

    private BluetoothAdapter mBtAdapter;
    private ScanItem BtnScanBT;
    private BTAdapter mNewDevicesArrayAdapter;
    private List<BTinfo> ListNewDevData = new ArrayList<BTinfo>();
    private ListView newDevicesListView;
    /**
     * 蓝牙音频传输协议
     */
    private BluetoothA2dp a2dp;
    private BluetoothSocket socket;       //蓝牙连接socket
    private Handler mOthHandler;
    private boolean BoolAutoC = false;
    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice currentBluetoothDevice;
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver=null;

    private static Toast mToast;
    private static Context mContext;
    protected ProgressDialog progressDialog;
    private static BluetoothDevice btDevDevice;
    private static BluetoothDevice ListBTDevice;
    public static BluetoothSocket btSocket;
    private boolean BoolClick = false;
    private boolean BoolScanBlue = true;
    private boolean BoolFristShow = false;
    private LinearLayout LYAutoLink;
    private Button BtnAutoLink;
    private int time_Current=5;
    private String BT_CUR_ConnectedID;
    private String BT_CUR_ConnectedName;
    private final int MsgWhat_FlashTime = 110;
    private final int MsgWhat_AddDeviceToListview = 111;
    private final int MsgWhat_ScanBluetooth = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataStruct.BOOL_GetBluetoothList = true;
        DataStruct.ManualConnecting = true;

//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.chs_device_list);

        setResult(Activity.RESULT_CANCELED);
        mContext = this;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        LYAutoLink = (LinearLayout) findViewById(R.id.ly_Auto);
        BtnAutoLink = (Button) findViewById(R.id.button_Auto);
        BtnScanBT = (ScanItem) findViewById(R.id.button_scan);

        //获取之前连接的蓝牙
        SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
        BT_CUR_ConnectedID = sp.getString("BT_CUR_ConnectedID", "null");
        BT_CUR_ConnectedName = sp.getString("BT_CUR_ConnectedName", "null");
        Log.e("##BUG getSharedPf","BT_CUR_ConnectedID:"+
                BT_CUR_ConnectedID+",BT_CUR_ConnectedName:"+BT_CUR_ConnectedName + "\n");
        //获取已经连接上的蓝牙
//        CheckBTConnectNaneAnaAdderss();
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        mNewDevicesArrayAdapter = new BTAdapter(mContext, ListNewDevData);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);

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
        registerReceiver(mReceiver, filter);

        //动态注册CHS_Broad_BroadcastReceiver
//        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
//        IntentFilter CHS_Broad_filter=new IntentFilter();
//        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        //注册receiver
//        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
        //自动连接功能
        if(DataOptUtil.getAutoConnect(mContext)){
            BtnAutoLink.setBackgroundResource(R.drawable.check_press);
            BoolAutoC = true;
            time_Current = 5;

        }else {
            BoolAutoC = false;
            BtnAutoLink.setBackgroundResource(R.drawable.check_normal);
        }
        //getCurConnectBluetoothList();
        setonClickListener();
        doDiscovery();


        LYAutoLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataOptUtil.getAutoConnect(mContext)){
                    DataOptUtil.setAutoConnect(mContext,false);
                    BtnAutoLink.setBackgroundResource(R.drawable.check_normal);
                    AnimationUtil.toVisibleAnim(mContext,BtnAutoLink);
                    setAutoConnectDefault();
                }else {
                    DataOptUtil.setAutoConnect(mContext,true);
                    BtnAutoLink.setBackgroundResource(R.drawable.check_press);
                    AnimationUtil.toVisibleAnim(mContext,BtnAutoLink);
                    BoolAutoC = true;
                    time_Current = 5;
                    handler_timeCurrent.sendEmptyMessageDelayed(MsgWhat_FlashTime,100);
                    showAutoConnectItem();
                }
            }
        });
        BtnScanBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
            }
        });


    }

    private void showAutoConnectItem(){
        if(!BT_CUR_ConnectedID.equals("null")){
            if(BoolAutoC){
                for(int i=0;i<ListNewDevData.size();i++){
                    if(ListNewDevData.get(i).device.getAddress().equals(BT_CUR_ConnectedID)){
                        if(!BoolFristShow){
                            time_Current = 5;
                            BoolFristShow = true;
                        }
                        ListNewDevData.get(i).BoolStartCnt = true;
                        ListNewDevData.get(i).time = time_Current;
                        currentBluetoothDevice = ListNewDevData.get(i).device;
                        Log.e("##BUG get ","currentBluetoothDevice");

                        if(time_Current > 0){
                            handler_timeCurrent.sendEmptyMessageDelayed(MsgWhat_FlashTime,1000);
                        }

                        break;
                    }
                }
            }else {
                for(int i=0;i<ListNewDevData.size();i++){
                    ListNewDevData.get(i).BoolStartCnt = false;
                    ListNewDevData.get(i).time = time_Current;
                }
            }
            mNewDevicesArrayAdapter = new BTAdapter(mContext, ListNewDevData);
            newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
            setonClickListener();
//                mNewDevicesArrayAdapter.setData(ListNewDevData);
//                mNewDevicesArrayAdapter.notifyDataSetChanged();
        }
    }

    private void setAutoConnectDefault(){
        BoolAutoC = false;
        handler_timeCurrent.removeMessages(MsgWhat_FlashTime);
        //刷新列表
        for(int i=0;i<ListNewDevData.size();i++){
            ListNewDevData.get(i).BoolStartCnt = false;
        }
        mNewDevicesArrayAdapter = new BTAdapter(mContext, ListNewDevData);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        setonClickListener();
//                mNewDevicesArrayAdapter.setData(ListNewDevData);
//                mNewDevicesArrayAdapter.notifyDataSetChanged();
    }



    private void setCancelDiscoveryDefault(){
        BtnScanBT.Stop();
    }

    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");
        //setAutoConnectDefault();
        ListNewDevData.clear();
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        getCurConnectBluetoothList();
        //刷新计时
        time_Current = 5;
        handler_timeCurrent.sendEmptyMessageDelayed(MsgWhat_FlashTime,100);
        showAutoConnectItem();

        handler_timeCurrent.sendEmptyMessageDelayed(MsgWhat_ScanBluetooth,500);

    }

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

    private void setonClickListener(){
        //连接新蓝牙
        mNewDevicesArrayAdapter.setOnAdpterOnItemClick(new BTAdapter.setAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
                if(BoolClick){
                    ToolsUtil.Toast(mContext,getResources().getString(R.string.ble_device_connecting));
                    return;
                }
                BoolClick = true;
                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();
                }

                setAutoConnectDefault();

                BluetoothDevice device = ListNewDevData.get(postion).device;
                //刷新列表
                for(int i=0;i<ListNewDevData.size();i++){
                    ListNewDevData.get(i).sel = false;
                }
                ListNewDevData.get(postion).sel = true;
                mNewDevicesArrayAdapter = new BTAdapter(mContext, ListNewDevData);
                newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
                setonClickListener();
//                mNewDevicesArrayAdapter.setData(ListNewDevData);
//                mNewDevicesArrayAdapter.notifyDataSetChanged();

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    if((device.getName().contains(Define.BT_Paired_Name_DSP_HD_))
//                            ){
//                        doPair(device);
//                    }else if(
//                             (device.getName().contains(Define.BT_Paired_Name_DSP_Play))
//                            ||(device.getName().contains(Define.BT_Paired_Name_DSP_HDS))
//                            ||(device.getName().contains(Define.BT_Paired_Name_DSP_CCS))
//                            ){
                        //此蓝牙不用连接的
                        //sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_CCS, device);
//                    }
                    contectBuleDevices(device);
                }else {
                    if(device.getName().contains(Define.BT_Paired_Name_DSP_HD_)){
//                        if(isBluetoothConnected(device)){
                            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HD_, device);
//                        }else {
//                            Log.e(TAG,"BUG "+device.getName()+"contectBuleDevicesA2DP"+"\n");
//                            contectBuleDevicesA2DP(device);
//                        }
                    }else if(device.getName().contains(Define.BT_Paired_Name_DSP_CCS)){
                        sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_CCS, device);
                    }else if(device.getName().contains(Define.BT_Paired_Name_DSP_HDS)){
                        sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HDS, device);
                    }else if(device.getName().contains(Define.BT_Paired_Name_DSP_Play)){
                        sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_Play, device);
                    }
                }
            }
        });
    }

    private void sendMsgToConnectBluetooth(String type, BluetoothDevice device){
        Intent intentw=new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_ConnectToSomeoneDevice);
        intentw.putExtra("type", type);
        intentw.putExtra("address", device.getAddress());
        intentw.putExtra("device", device.getName());
        mContext.sendBroadcast(intentw);
        exit();
    }
    public void showProgressDailog(String msg){
        if(progressDialog==null)
            progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage(msg);
        progressDialog.show();

    }

    public void hideProgressDailog(){
        if(progressDialog!=null){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    private static void ToastMsg(String Msg) {
		if (null != mToast) {
			mToast.setText(Msg);
		} else {
			mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	private void addBluetoothDeviceToListView(BluetoothDevice devices,boolean flashList){
        if(devices != null){
            if((devices.getName() != null)&&isCHSBluetooth(devices.getName())){

                if(ListNewDevData.size() > 0){
                    for(int i=0;i<ListNewDevData.size();i++){
                        //System.out.println("BUG ListNewDevData 1>" + ListNewDevData.get(i).device.getAddress() + ",2->" + devices.getAddress());
                        if(ListNewDevData.get(i).device.getAddress().equals(devices.getAddress())){
                            return;
                        }
                    }
                }
                ListNewDevData.add(new BTinfo(false,devices));
                if(flashList){
                    mNewDevicesArrayAdapter = new BTAdapter(mContext, ListNewDevData);
                    newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
                    setonClickListener();
                }
            }
        }

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = null;
            System.out.println("BUG 搜索设备"+action);

            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device!=null) {
                System.out.println("BUG device mReceiver-getName>" + device.getName() + ",intent.getAction()->" +intent.getAction());
            }
            if(device!=null) {
                if(BT_CUR_ConnectedID.equals(device.getAddress())){
                    if(!MacCfg.BOOL_FirstStart){
                        MacCfg.BOOL_FirstStart = true;
                        if(BoolAutoC){
                            contectBuleDevices(device);
                        }
                    }
                }

                System.out.println("BUG device mReceiver-getName>" + device.getName() + ",getAddress->" + device.getAddress());
                addBluetoothDeviceToListView(device,true);
                if(ListNewDevData.size() > 0){
                    showAutoConnectItem();
                }
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//                if(device!=null) {
//                    if(BT_CUR_ConnectedID.equals(device.getAddress())){
//                        if(!MacCfg.BOOL_FirstStart){
//                            MacCfg.BOOL_FirstStart = true;
//                            if(BoolAutoC){
//                                contectBuleDevices(device);
//                            }
//                        }
//                    }
//
//                    System.out.println("BUG device ACTION_FOUND-getName>" + device.getName() + ",getAddress->" + device.getAddress());
//                    addBluetoothDeviceToListView(device,true);
//                    if(ListNewDevData.size() > 0){
//                        showAutoConnectItem();
//                    }
//                }
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                time_Current = 5;
                BtnScanBT.Start();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setCancelDiscoveryDefault();
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){

                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("BUG BOND_STATE:"+device.getName() +"ACTION_BOND_STATE_CHANGED");
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
//                        showProgressDailog(device.getName()+getResources().getString(R.string.spp_le_Pairing));
//                        ToastMsg(getString(R.string.spp_le_Pairing));
//                        System.out.println("BUG info:"+device.getName() +"-正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
//                        hideProgressDailog();
//                        ToastMsg(getString(R.string.spp_le_Paired));
//                        ToastMsg(getString(R.string.spp_le_Linking));
//                        System.out.println("BUG info:"+device.getName() +"-完成配对......");
//                        if(device.getName().contains(Define.BT_Paired_Name_DSP_HD_)){//此款蓝牙要音频连接后才能连接
//                            contectBuleDevicesA2DP(device);
//                        }else {
//                            contectBuleDevices(device);
//                        }

                        break;
                    case BluetoothDevice.BOND_NONE:
//                        hideProgressDailog();
//                        System.out.println("BUG info:"+device.getName() +"-取消配对......");
                    default:
                        break;
                }
            } else if (intent.getAction().equals(ACTION_PAIRING_REQUEST)) {
//                System.out.println("BUG info: 11111111111111111111111 ACTION_PAIRING_REQUEST");
//                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (btDevice.getName().equals(Define.BT_Paired_Name_DSP_CCS)) {
//                    try {
//                        abortBroadcast();
//                        ClsUtils.setPin(btDevice.getClass(), btDevice, "1234"); // 手机和蓝牙采集器配对
//                        ClsUtils.setPairingConfirmation(device.getClass(), device, true);
//                        ClsUtils.createBond(btDevice.getClass(), btDevice);
//                        System.out.println("BUG info:确认配对");
//                    } catch (Exception e) {
//                        System.out.println("BUG info: Exception=" + btDevice.getName());
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }else {
//
//                }

            }//A2DP连接状态改变
            else if(action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)){
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                Log.i(TAG,"connect state="+state);
            }else if(action.equals(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED)){
                //A2DP播放状态改变
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_NOT_PLAYING);
                Log.i(TAG,"play state="+state);
            }
        }
    };

    /**
     * 配对
     */
    public void doPair(BluetoothDevice device) {
        boolean result = false;
        try{
            Method m = device.getClass().getDeclaredMethod("createBond",new Class[]{});
            m.setAccessible(true);
            Boolean originalResult = (Boolean) m.invoke(device);
            result = originalResult.booleanValue();
        }catch(Exception ex){
        }
/*
        final BluetoothDevice Dev = device;
        if(null == mOthHandler){
            HandlerThread handlerThread = new HandlerThread("other_thread");
            handlerThread.start();
            mOthHandler = new Handler(handlerThread.getLooper());
        }
        mOthHandler.post(new Runnable() {
            @Override
            public void run() {
                initSocket(Dev);   //取得socket
                try {
                    socket.connect();   //请求配对
//						mAdapterManager.updateDeviceAdapter();
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showShortToast(mContext,mContext.getResources().getString(R.string.LinkOfMsg));
                    finish();
                }
            }
        });
        */
    }


    /**
     * 取消蓝牙配对
     * @param device
     */
    public static void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.d("BlueUtils", e.getMessage());
        }
    }


    /**
     * 取得BluetoothSocket
     */
    public void initSocket(BluetoothDevice device) {
        BluetoothSocket temp = null;
        try {
//            temp = blueDevice.getDevice().createRfcommSocketToServiceRecord(
//                    MY_UUID_SECURE);
//			temp = mTouchObject.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));
//            以上取得socket方法不能正常使用， 用以下方法代替
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            temp = (BluetoothSocket) m.invoke(device, 1);
            //怪异错误： 直接赋值给socket,对socket操作可能出现异常，  要通过中间变量temp赋值给socket
        }catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        socket = temp;
    }
    /**
     * 开始连接蓝牙设备
     */
    private void contectBuleDevices(BluetoothDevice device) {
        currentBluetoothDevice = device;
        if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HD_)){
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HD_, currentBluetoothDevice);
        }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_CCS)){
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_CCS, currentBluetoothDevice);
        }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_Play)){
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_Play, currentBluetoothDevice);
        }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HDS)){
            sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HDS, currentBluetoothDevice);
        }
//        showProgressDailog(device.getName()+getResources().getString(R.string.spp_le_Linking));
//        currentBluetoothDevice = device;
//        /**使用A2DP协议连接设备*/
//        mBtAdapter.getProfileProxy(this, mProfileServiceListener, BluetoothProfile.A2DP);
    }


    private boolean isBluetoothConnected(BluetoothDevice currentDevice){
        boolean haveDeviceCon = false;
        for(int i=0;i<MacCfg.LCBT.size();i++){
            if(MacCfg.LCBT.get(i).getAddress().contains(currentDevice.getAddress())){
                haveDeviceCon = true;
                break;
            }
        }
        return haveDeviceCon;
    }



    //获取正在连接的蓝牙
    private void getCurConnectBluetoothList(){
        MacCfg.LCBT.clear();

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if(state == BluetoothAdapter.STATE_CONNECTED){
                Log.i(TAG,"connectedBT BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                Log.i(TAG,"connectedBT devices.size():"+devices.size());

                for(BluetoothDevice device : devices){
                    addBluetoothDeviceToListView(device,true);
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if(isConnected){
                        Log.i(TAG,"connectedBT:"+device.getName());
                        addBluetoothDeviceToListView(device,true);
                        //保存已经连接的蓝牙
                        if(isCHSBluetooth(device.getName())){
                            MacCfg.LCBT.add(device);
                            Log.i(TAG,"BT_CUR_ConnectedID:"+BT_CUR_ConnectedID+
                                    ",device.getAddress():"+device.getAddress()+
                                    ",MacCfg.BOOL_FirstStart:"+MacCfg.BOOL_FirstStart+
                                    ",BoolAutoC:"+BoolAutoC);
                            if(BT_CUR_ConnectedID.equals(device.getAddress())){
                                if(!MacCfg.BOOL_FirstStart){
                                    MacCfg.BOOL_FirstStart = true;
                                    if(BoolAutoC){
                                        contectBuleDevices(device);
                                    }
                                }
                            }
                        }
                    }
                }
                mNewDevicesArrayAdapter = new BTAdapter(mContext, ListNewDevData);
                newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
                setonClickListener();
//                mNewDevicesArrayAdapter.setData(ListNewDevData);
//                mNewDevicesArrayAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*********************************************************************/
    /*******************         Bluetooth A2P      **********************/
    /*********************************************************************/


    private void contectBuleDevicesA2DP(BluetoothDevice device) {
        currentBluetoothDevice = device;

        if(isBluetoothConnected(device)){
            contectBuleDevices(currentBluetoothDevice);
        }else {
            showProgressDailog(device.getName()+getResources().getString(R.string.spp_le_Linking));
            currentBluetoothDevice = device;
            connectA2dp(currentBluetoothDevice);
        }

        if(mBtAdapter.getProfileConnectionState(BluetoothProfile.A2DP)!=BluetoothProfile.STATE_CONNECTED){
            //在listener中完成A2DP服务的调用
            //mBtAdapter.getProfileProxy(mContext, new connServListener(), BluetoothProfile.A2DP);
        }
    }
    /**
     * 连接蓝牙设备（通过监听蓝牙协议的服务，在连接服务的时候使用BluetoothA2dp协议）
     */
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceDisconnected(int profile) {
            Log.i(TAG, "onServiceDisconnected profile="+profile);
            if(profile == BluetoothProfile.A2DP){
                a2dp = null;
            }
        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            hideProgressDailog();
            try {
                if (profile == BluetoothProfile.A2DP) {
                    //Log.d(TAG, "BUG-------->>>>>>BluetoothProfile.A2DP");
                    /**使用A2DP的协议连接蓝牙设备（使用了反射技术调用连接的方法）*/
                    a2dp = (BluetoothA2dp) proxy;
                }
            } catch (Exception e) {
                System.out.println("BUG-----contectBuleDevices--->>>>>>Exception!!!");
                e.printStackTrace();
            }
        }
    };

    private void disConnectA2dp(BluetoothDevice device){
        setPriority(device, 0);
        try {
            //通过反射获取BluetoothA2dp中connect方法（hide的），断开连接。
            Method connectMethod =BluetoothA2dp.class.getMethod("disconnect",
                    BluetoothDevice.class);
            connectMethod.invoke(a2dp, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void connectA2dp(BluetoothDevice device){
        setPriority(device, 100); //设置priority
        try {
            //通过反射获取BluetoothA2dp中connect方法（hide的），进行连接。
            Method connectMethod =BluetoothA2dp.class.getMethod("connect",
                    BluetoothDevice.class);
            connectMethod.invoke(a2dp, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPriority(BluetoothDevice device, int priority) {
        if (a2dp == null) return;
        try {//通过反射获取BluetoothA2dp中setPriority方法（hide的），设置优先级
            Method connectMethod =BluetoothA2dp.class.getMethod("setPriority",
                    BluetoothDevice.class,int.class);
            connectMethod.invoke(a2dp, device, priority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getPriority(BluetoothDevice device) {
        int priority = 0;
        if (a2dp == null) return priority;
        try {//通过反射获取BluetoothA2dp中getPriority方法（hide的），获取优先级
            Method connectMethod =BluetoothA2dp.class.getMethod("getPriority",
                    BluetoothDevice.class);
            priority = (Integer) connectMethod.invoke(a2dp, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return priority;
    }
    public List<BluetoothDevice> getConnectedDevices(){
        if(a2dp == null)return null;
        return a2dp.getConnectedDevices();
    }
    public int getConnectionState(BluetoothDevice device) {
        if(a2dp == null)return BluetoothProfile.STATE_DISCONNECTED;
        return a2dp.getConnectionState(device);
    }
    public boolean isA2dpPlaying(BluetoothDevice device){
        if(a2dp == null)return false;
        return a2dp.isA2dpPlaying(device);
    }
    /*
    private static class MyHandler extends Handler {

        private WeakReference<MainTETActivity> activityWeakReference;

        public MyHandler(MainTETActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainTETActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == 1) {
                    // 做相应逻辑
                }
            }
        }
    }
    */
    //自动连接
    @SuppressLint("HandlerLeak")
    private Handler handler_timeCurrent = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == MsgWhat_FlashTime){
                handler_timeCurrent.removeMessages(MsgWhat_FlashTime);
                //刷新列表
                showAutoConnectItem();

                --time_Current;
                if(currentBluetoothDevice != null){
                    System.out.println("BUG-----AutocontectBuleDevices--->>>>>>time_Current="+time_Current+
                            "currentBluetoothDevicegetName="+currentBluetoothDevice.getName()+"\n"+
                            "currentBluetoothDevicegetAddress="+currentBluetoothDevice.getAddress()+"\n"+
                            "BoolAutoC="+BoolAutoC+"\n"
                    );
                }else {
                    System.out.println("BUG-----AutocontectBuleDevices--currentBluetoothDevice=Null->>>>");
                }

                if((time_Current <= 0)&&(currentBluetoothDevice != null)){
                    if(BoolAutoC){
                        BoolAutoC = false;
                        System.out.println("BUG device currentBluetoothDevice-getName>"
                                +currentBluetoothDevice.getName()+",getAddress->"
                                +currentBluetoothDevice.getAddress());

                        if(isBluetoothConnected(currentBluetoothDevice)){//所选择的蓝牙已经连接了手机
                            if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HD_)){
                                sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HD_, currentBluetoothDevice);
                            }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_CCS)){
                                sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_CCS, currentBluetoothDevice);
                            }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HDS)){
                                sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_HDS, currentBluetoothDevice);
                            }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_Play)){
                                sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_Play, currentBluetoothDevice);
                            }
                        }else {//所选择的蓝牙还未连接手机
                            if((currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HD_))
                                    ||(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_Play))
                                    ||(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_HDS))
                                    ){
                                contectBuleDevices(currentBluetoothDevice);
                            }else if(currentBluetoothDevice.getName().contains(Define.BT_Paired_Name_DSP_CCS)){
                                //此蓝牙不用连接的
                                sendMsgToConnectBluetooth(Define.BT_Paired_Name_DSP_CCS, currentBluetoothDevice);
                            }
                        }
                    }

                }
                if(time_Current <= 0){
                    time_Current = 0;
                }
            }else if(msg.what == MsgWhat_AddDeviceToListview){
                handler_timeCurrent.removeMessages(MsgWhat_AddDeviceToListview);
            }else if(msg.what == MsgWhat_ScanBluetooth){
                handler_timeCurrent.removeMessages(MsgWhat_ScanBluetooth);
                mBtAdapter.startDiscovery();
            }
        }
    };

    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
//            if (msg.equals(Define.BoardCast_AutoConnectToDevice)) {
//
//            }
        }
    }

    private void exit(){
        DataStruct.BOOL_GetBluetoothList = true;
        DataStruct.ManualConnecting = false;
        finish();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask(){
//            @Override
//            public void run(){
//                finish();
//            }
//        }, 108);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataStruct.BOOL_GetBluetoothList = true;
        DataStruct.ManualConnecting = true;
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
        }
        handler_timeCurrent.removeMessages(MsgWhat_FlashTime);
        handler_timeCurrent.removeMessages(MsgWhat_AddDeviceToListview);
        handler_timeCurrent.removeMessages(MsgWhat_ScanBluetooth);
        handler_timeCurrent.removeCallbacksAndMessages(null);
        handler_timeCurrent = null;
        //this.unregisterReceiver(CHS_Broad_Receiver);


        //unbindService(mProfileServiceListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mBtAdapter.isEnabled())
            return;

    }
}
