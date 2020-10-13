package com.chs.mt.pxe_r500.main;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;

public class WelcomeActivity extends Activity {
	private String statementShowString="";
	private static Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.chs_welcome);
        mContext = this;

        if (!isTaskRoot()) {
            finish();
            return;
        }

        //关闭后台打开的服务进程
        //ServiceOfCom.disconnectSet();
//        Intent stopIntent = new Intent(mContext, ServiceOfCom.class);
//        mContext.stopService(stopIntent);

//        Intent intentw=new Intent();
//        intentw.setAction("android.intent.action.CHS_Broad_BroadcastReceiver");
//        intentw.putExtra("msg", Define.BoardCast_StopService);
//        mContext.sendBroadcast(intentw);

		if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
			finish();
			return;
		}
//        Intent intentw=new Intent();
//        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//        intentw.putExtra("msg", Define.BoardCast_FlashUI_CloseActivity);
//        mContext.sendBroadcast(intentw);
        //CheckBTConnectNaneAnaAdderss();最好不要用
        //注册手机信息
       // if(UpdateUtil.isNetworkAvalible(mContext)){
        //    mOKhttpUtil.registerMobilePhoneInformation(this);
//        }

		//获取SharedPreferences对象
		Context ctx = this;
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		statementShowString = sp.getString("Statement", "SHOW");
        MacCfg.BOOL_SyncSystemVol = sp.getBoolean("SYNC_SYSTEMVOL", false);
		handler.postDelayed(runnable, 4600);
//        ImageView imageView = (ImageView)findViewById(R.id.id_iv);
//
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
//        alphaAnimation.setDuration(3000);
//        imageView.startAnimation(alphaAnimation);
//        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
////			if(statementShowString.equals("NULL")){
//                Intent intent = new Intent();
//                intent.setClass(mContext, MainTETActivity.class);
//                startActivity(intent);
////			}else{
////				Intent intent = new Intent();
////				intent.setClass(WelcomeActivity.this, StatementActivity.class);
////				WelcomeActivity.this.startActivity(intent);
////			}
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask(){
//                    @Override
//                    public void run(){
//                        finish();
//                    }
//                }, 388);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
	}

	@Override
	protected void onStart() {

		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if(statementShowString.equals("NULL")){
				Intent intent = new Intent();
				intent.setClass(mContext, MainTBTTActivity.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, StatementActivity.class);
				WelcomeActivity.this.startActivity(intent);
			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run(){
					finish();
				}
			}, 388);
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != handler) {
            handler.removeCallbacks(runnable);
        }
        handler = null;
    }








    //打开返回true
    public static boolean CheckBTStata() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!btAdapter.isEnabled()){
            return false;
        }
        return true;
    }

    private static void CheckBTConnectNaneAnaAdderss(){
        if(CheckBTStata()==false){
            return;
        }
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        int a2dp = btAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = btAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = btAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);

        int flag = -1;
        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
            flag = a2dp;
        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
            flag = headset;
        } else if (health == BluetoothProfile.STATE_CONNECTED) {
            flag = health;
        }
        //System.out.println("BUG CheckBTConnect A flag:"+flag);
        if (flag != -1) {
            btAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {

                @Override
                public void onServiceDisconnected(int profile) {
                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices != null && mDevices.size() > 0) {
                        MacCfg.LCBT.clear();
                        for (BluetoothDevice device : mDevices) {
                            Log.e("##BUG BT Connect BT","BT name:"+
                                    device.getName()+",BT Address "+device.getAddress() + "\n");
                            MacCfg.BT_GetName = device.getName();
                            MacCfg.BT_GetID = device.getAddress();

                            if((MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_HD_))
                                    ||(MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_CCS))
                                    ||(MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_HDS))
                                    ||(MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_Play))
                                    ){
                                MacCfg.BT_CUR_ConnectedName = MacCfg.BT_GetName;
                                MacCfg.BT_CUR_ConnectedID = MacCfg.BT_GetID;
                                MacCfg.CHS_BT_CONNECTED=true;
                                if(MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_HD_)){
                                    MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP;
                                    MacCfg.BT_ConnectedID = MacCfg.BT_GetID;
                                    MacCfg.BT_ConnectedName = MacCfg.BT_GetName;
                                }
                                MacCfg.LCBT.add(device);
                            }
                        }
                    } else {
                        MacCfg.BT_ConnectedID = "NULL";
                        MacCfg.BT_ConnectedName = "NULL";
                        MacCfg.BT_CUR_ConnectedName = "NULL";
                        MacCfg.BT_CUR_ConnectedID = "NULL";
                        MacCfg.CHS_BT_CONNECTED=false;
                        //System.out.println("BUG CheckBTConnect mDevices is null0");
                    }
                }}, flag);
        }else if(flag == -1) {
            MacCfg.BT_ConnectedID = "NULL";
            MacCfg.BT_ConnectedName = "NULL";
            MacCfg.BT_CUR_ConnectedName = "NULL";
            MacCfg.BT_CUR_ConnectedID = "NULL";
            MacCfg.CHS_BT_CONNECTED=false;
            //System.out.println("BUG CheckBTConnect mDevices is null.1");
        }
    }

}
