package com.chs.mt.pxe_r500.bluetooth.spp_ble;
/**
 * 描述：蓝牙服务核心类
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothChatService {
    // 测试数据
    private static final String TAG = "BluetoothChatService";
    private static final boolean D = true;

    private static final String NAME = "BluetoothChat";

    // 声明一个唯一的UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");	//change by chongqing jinou	

    private BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private Context mContext;
    // 常量,显示当前的连接状态
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2; 
    public static final int STATE_CONNECTED = 3;
    
    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
        mContext = context;
    }

    /**
     * 设置当前的连接状态
     * @param state  连接状态
     */
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // 通知Activity更新UI
        mHandler.obtainMessage(Define.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * 返回当前连接状态 
     * 
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     *开始聊天服务
     *
     */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
            mAcceptThread = null;
        }
        setState(STATE_LISTEN);
    }

    /**
     * 连接远程设备
     * @param device  连接
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.d(TAG, "连接到: " + device);

        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * 启动ConnectedThread开始管理一个蓝牙连接
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "连接");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancelSocket();
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        Message msg = mHandler.obtainMessage(Define.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(MacCfg.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * 停止所有线程
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        setState(STATE_NONE);
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancelSocket();
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
    }

    /**
     * 以非同步方式写入ConnectedThread
     * @param out 
     */
    public void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
//        System.out.println("FUCK r.write(out)");
        r.write(out);
    }

    /**
     * 无法连接，通知Activity
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);
        
        Message msg = mHandler.obtainMessage(Define.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MacCfg.TOAST, "无法连接设备");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 设备断开连接，通知Activity
     */
    private void connectionLost() {
 
        Message msg = mHandler.obtainMessage(Define.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MacCfg.TOAST, "设备断开连接");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mHandler.obtainMessage(Define.MESSAGE_Lost, 0, 0, null)
                .sendToTarget();
    }

    /**
     * 监听传入的连接
     */
    private class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket = null;
        private BluetoothSocket socket = null;
        public AcceptThread() {
            //BluetoothServerSocket tmp = null;
            mAdapter = BluetoothAdapter.getDefaultAdapter();
            try {
                if(mAdapter != null){
                    mmServerSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                    Log.e(TAG, "mAdapter.listenUsingRfcommWithServiceRecord");
                }
            } catch (IOException e) {
                cancel();
                if(mAdapter != null){
                    mAdapter.cancelDiscovery();
                }
                Log.e(TAG, "listen() failed", e);
            }
            //mmServerSocket = tmp;
        }

        public void run() {
            if (D) Log.d(TAG, "BEGIN mAcceptThread" + this);
            setName("AcceptThread");


            while (mState != STATE_CONNECTED) {
                try {
                    if(mmServerSocket!=null){
                        socket = mmServerSocket.accept();
                    }
                } catch (IOException e) {

                    cancelSocket();
                    cancel();
                     //Motinlu
//                    connectionLost();
//                    cancel();
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_OPT_DisonnectDeviceBT);
                    if(mContext != null){
                        mContext.sendBroadcast(intentw);
                    }
                    break;
                }

                // 如果连接被接受
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // 开始连接线程
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // 没有准备好或已经连接
                            cancelSocket();
                            break;
                        }
                    }
                }
            }
            if (D) Log.i(TAG, "结束mAcceptThread");
        }

        public void cancel() {
            if (D) Log.d(TAG, "取消 " + this);
            try {
                if(mmServerSocket!=null){
                    mmServerSocket.close();
                    mmServerSocket = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "关闭失败", e);
            }
        }

        public void cancelSocket() {
            try {
                if(socket!=null){
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "socket 关闭失败", e);
            }
        }
    }


    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket = null;
        private BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            if(device != null){
                mmDevice = device;
                try {
                    mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    cancel();
                    Log.e(TAG, "create() 失败", e);
                }
            }

        }

        public void run() {
            Log.i(TAG, "开始mConnectThread");
            setName("ConnectThread");
            if(mAdapter != null){
                mAdapter.cancelDiscovery();
            }

            try {
                if(mmSocket != null){
                    mmSocket.connect();
                }
            } catch (IOException e) {
                connectionFailed();
                cancel();
//                BluetoothChatService.this.start();
                return;
            }

            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }
            if(mmSocket != null){
                connected(mmSocket, mmDevice);
            }

        }

        public void cancel() {
            try {
                if(mmSocket != null){
                    mmSocket.close();
                    mmSocket = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "关闭连接失败", e);
            }
        }
    }

    /**
     * 处理所有传入和传出的传输
     */
    private class ConnectedThread extends Thread {
        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "创建 ConnectedThread");
            if(socket != null){
                mmSocket = socket;
                // 得到BluetoothSocket输入和输出流
                try {
                    mmInStream = socket.getInputStream();
                    mmOutStream = socket.getOutputStream();
                } catch (IOException e) {
                    cancel();
                    Log.e(TAG, "temp sockets not created", e);
                }
            }
        }

		public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            int len = -1;
            // 循环监听消息
            while (true) {
                try {
                	byte[] buffer = new byte[1024];
                	if(mmInStream != null){
                        len = mmInStream.read(buffer);
                    }

                    if(len != -1) {
                    	byte[] tempbuffer = ByteUtil.range(buffer, 0, len);
                		mHandler.obtainMessage(Define.MESSAGE_READ, len, len, tempbuffer)
                        .sendToTarget();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    cancel();
//                    if(mState != STATE_NONE) {
//                    	// 在重新启动监听模式启动该服务
//                    	BluetoothChatService.this.start();
//                    }
                    break;
                }

            }
        }

        /**
         * 写入OutStream连接
         * @param buffer  要写的字节
         */
        public void write(byte[] buffer) {
            try {
                if(mmOutStream!=null){
                    mmOutStream.write(buffer);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                connectionLost();
                cancel();
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {

            try {
                if(mmInStream!=null){
                    mmInStream.close();
                    mmInStream = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "close() of connect mmInStream failed", e);
            }
            try {
                if(mmOutStream!=null){
                    mmOutStream.close();
                    mmOutStream = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "close() of connect mmOutStream failed", e);
            }
            try {
                if(mmSocket != null){
                    mmSocket.close();
                    mmSocket = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
    /**
     * 从字节数组到十六进制字符串转换 
     */
    public static String bytes2HexString(byte[] b) {
  	  String ret = "";
  	  for (int i = 0; i < b.length; i++) {
  	   String hex = Integer.toHexString(b[ i ] & 0xFF);
  	   if (hex.length() == 1) {
  	    hex = '0' + hex;
  	   }
  	   ret += hex.toUpperCase();
  	  }
  	  return ret;
  	}
}
