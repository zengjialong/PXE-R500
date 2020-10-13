package com.chs.mt.pxe_r500.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;


public class FragmentAdapter extends FragmentPagerAdapter {


	private List<Fragment> mFragments;
	private FragmentManager fm;
	public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.mFragments = fragments;
		this.fm = fm;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}


//	private int mChildCount = 0;
//	@Override
//	public void notifyDataSetChanged() {
//		// 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
//		mChildCount = getCount();
//		super.notifyDataSetChanged();
//	}




	public List<Fragment> getmFragments() {
		return mFragments;
	}

	public void setmFragments(List<Fragment> mFragments) {
		if(this.mFragments != null){
			FragmentTransaction ft = fm.beginTransaction();
			for(Fragment f:this.mFragments){
				ft.remove(f);
			}
			ft.commit();
			ft=null;
			fm.executePendingTransactions();
		}
		this.mFragments = mFragments;
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}


}
