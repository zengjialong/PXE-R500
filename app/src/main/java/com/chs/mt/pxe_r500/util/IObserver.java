package com.chs.mt.pxe_r500.util;

import java.util.HashMap;

public interface IObserver {
	public void update(HashMap<String, Object> notification);
	public void observe(ISubject subject, String event);
	public void unobserve(ISubject subject, String event);
}
