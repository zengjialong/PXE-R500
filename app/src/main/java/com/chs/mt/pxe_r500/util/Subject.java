package com.chs.mt.pxe_r500.util;
import java.util.ArrayList;
import java.util.HashMap;

public class Subject implements ISubject {
	private ArrayList<HashMap<IObserver, String>> observerList;
	
	public Subject(){
		observerList = new ArrayList<HashMap<IObserver, String>>();
	}
	
	private boolean isExist(IObserver observer, String event){
		for(int i = 0; i < observerList.size();i++){
			HashMap<IObserver, String> tmp = observerList.get(i);
			if(tmp.containsKey(observer)){
				if(tmp.get(observer)==null){
					if(event==null)
						return true;
				}
				else {
					if(event!=null&&tmp.get(observer).equals(event))
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void attach(IObserver observer, String event) {
		if(!isExist(observer, event)){
			if(event==null){
				for(int i = 0; i < observerList.size(); i++){
					if(observerList.get(i).containsKey(observer)){
						observerList.remove(i);
						i--;
					}
				}
				HashMap<IObserver, String> tmp = new HashMap<IObserver, String>();
				tmp.put(observer, event);
				observerList.add(tmp);
			}
			else{
				if(!isExist(observer, null)){
					HashMap<IObserver, String> tmp = new HashMap<IObserver, String>();
					tmp.put(observer, event);
					observerList.add(tmp);
				}
			}
		}
	}

	@Override
	public void detach(IObserver observer, String event) {
		for(int i = 0;i < observerList.size();i++){
			HashMap<IObserver, String> tmp = observerList.get(i);
			if(tmp.containsKey(observer)){
				if((event==null)||(tmp.get(observer)!=null&&event!=null&&tmp.get(observer).equals(event))){
					observerList.remove(i);
					i--;
				}
			}
		}
	}

	@Override
	public void notify(HashMap<String ,Object> notification) {
		String event = (String)notification.get("name");
		for(int i =0; i < observerList.size();i++){
			HashMap<IObserver, String> tmp = observerList.get(i);
			if(tmp.containsValue(event)||tmp.containsValue(null)){
				IObserver observer = (IObserver)tmp.keySet().toArray()[0];
//				if(observer instanceof BaseActivity){
//					Handler handler = ((BaseActivity)observer).getHandler();
//					handler.sendMessage(handler.obtainMessage(EventHandler.RECEIVE_NOTIFICATION, notification));
//				}
//				else
//					observer.update(notification);
			}
		}
	}
}
