package com.chs.mt.pxe_r500.util;

import java.util.HashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class EventHandler extends Handler {
	public static final int RECEIVE_NOTIFICATION = 0;
	private IObserver observer;
	public EventHandler(IObserver observer, Looper looper){
		super(looper);
		this.observer = observer;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg){
		if(msg.what==RECEIVE_NOTIFICATION)
			observer.update((HashMap<String, Object>)msg.obj);
		else
			super.handleMessage(msg);
	}
}