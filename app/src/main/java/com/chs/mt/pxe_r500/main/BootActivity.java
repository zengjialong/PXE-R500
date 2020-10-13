package com.chs.mt.pxe_r500.main;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.service.ServiceOfCom;

public class BootActivity extends FragmentActivity {
    private static final String TAG = "BootActivity";
    private static Context mContext;
    private Toast mToast;

    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        //绑定Service
        Intent intent = new Intent(mContext, ServiceOfCom.class);
        mContext.bindService(intent, connection, Service.BIND_AUTO_CREATE);
        handlerMainVal.sendEmptyMessageDelayed(3,1000);

        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter=new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
    }

    private Handler handlerMainVal = new Handler() {
        public void handleMessage(Message msg) {
        switch (msg.what) {

            case 3:
                MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_UART;
                // 直接移除，定时器停止
                handlerMainVal.removeMessages(3);
                ToastMsg(mContext.getResources().getString(R.string.TriesToConnectTo)+"DSP");
                ServiceOfCom.connectToDevice();
                break;

            default:
                break;
        }
    };
};
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext,Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    /**
     * Service绑定状态的监听
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void Exit(){
        System.exit(0);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        handlerMainVal.removeCallbacksAndMessages(null);
        Intent stopIntent = new Intent(mContext, ServiceOfCom.class);
        mContext.stopService(stopIntent);
        mContext.unbindService(connection);

        if(CHS_Broad_Receiver!=null){
            unregisterReceiver(CHS_Broad_Receiver);
        }
    }



    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            if (msg.equals(Define.BoardCast_FlashUI_ShowMsg)) {
                String res = intent.getStringExtra("String");
                ToastMsg(res);
            } else if (msg.equals(Define.BoardCast_FlashUI_CloseActivity)) {
                ToastMsg("BoardCast_FlashUI_CloseActivity");

//                Intent stopIntent = new Intent(mContext, ServiceOfCom.class);
//                mContext.stopService(stopIntent);
//                mContext.unbindService(connection);

                finish();
            }
        }
    }
}
