package com.chs.mt.pxe_r500.util;

import java.util.HashMap;

public class Observer implements IObserver {

	@Override
	public void update(HashMap<String, Object> notification) {
		// TODO Auto-generated method stub
	}

	@Override
	public void observe(ISubject subject, String event) {
		subject.attach(this, event);
	}

	@Override
	public void unobserve(ISubject subject, String event) {
		subject.detach(this, event);
	}
}
