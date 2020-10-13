package com.chs.mt.pxe_r500.util;

import java.util.*;

public interface ISubject {
	public void attach(IObserver observer, String event);
	public void detach(IObserver observer, String event);
	public void notify(HashMap<String ,Object> notification);
}
