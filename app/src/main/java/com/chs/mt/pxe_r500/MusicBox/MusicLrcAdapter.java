package com.chs.mt.pxe_r500.MusicBox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class MusicLrcAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;

	public MusicLrcAdapter(FragmentManager fm) {
		super(fm);
	}

	public MusicLrcAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int postion) {
		return list.get(postion);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
