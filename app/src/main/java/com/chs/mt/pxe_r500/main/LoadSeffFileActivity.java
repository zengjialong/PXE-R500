package com.chs.mt.pxe_r500.main;

import com.chs.mt.pxe_r500.datastruct.Define;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class LoadSeffFileActivity extends Activity{
	public static final String BoardCast_LOAD_SEFF_FROM_OTHER="BoardCast_LOAD_SEFF_FROM_OTHER";
	private static final int WHAT_CALL_MAIN_ACTIVITY           = 0x03;    
	private Handler mHandler ;
	private CHS_LoadSeffFileActivity_BroadcastReceiver CHS_LoadSeffFileActivity_Receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("BUG LoadSeffFileActivity onCreate path");
		Intent intent=getIntent();
		Uri uri=intent.getData();
		if(uri!=null){
			String path=uri.getPath();
			System.out.println("BUG onCreate LoadSeffFileActivity path:" + path);
			Intent intenl=new Intent();
			intenl.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
			intenl.putExtra("msg", Define.BoardCast_Load_LocalJson);
			intenl.putExtra("filePath", path);
			sendBroadcast(intent);


			Message msg = Message.obtain();
			msg.what = WHAT_CALL_MAIN_ACTIVITY;
			mHandler.sendMessageDelayed(msg, 500);
		}


		initmHandlerView();
		//��̬ע��CHS_Broad_BroadcastReceiver
		CHS_LoadSeffFileActivity_Receiver = new CHS_LoadSeffFileActivity_BroadcastReceiver();
    	IntentFilter CHS_Broad_filter=new IntentFilter();
    	CHS_Broad_filter.addAction("android.intent.action.CHS_LoadSeffFileActivity_BroadcastReceiver");
    	//ע��receiver
    	registerReceiver(CHS_LoadSeffFileActivity_Receiver, CHS_Broad_filter);  
    	
    	

        
	}
	

    public class CHS_LoadSeffFileActivity_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
              String msg=intent.getExtras().get("msg").toString();
              System.out.println("BUG CHS_SEffUploadPage_BroadcastReceiver msg:"+msg);
              if(msg.equals(Define.BoardCast_EXIT_SEFFUploadPage)){            	  
            	  finish();
              }
        }
    }
	
	private void initmHandlerView() {
		mHandler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
     			super.handleMessage(msg);
     			System.out.println("BUG  LoadSeffFileActivity msg.what:" + msg.what);
    			if (msg.what == WHAT_CALL_MAIN_ACTIVITY) {
    				finish();
    			}
    		}
        };
        
		
		
	}
	
	@Override
	protected void onDestroy() {
		if(CHS_LoadSeffFileActivity_Receiver!=null){
			unregisterReceiver(CHS_LoadSeffFileActivity_Receiver);
		}
		if(mHandler!=null){
			mHandler=null;
		}
		
		
		super.onDestroy();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
