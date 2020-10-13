package com.chs.mt.pxe_r500.tools;
import com.chs.mt.pxe_r500.R;


import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ������
 * @author Administrator
 *
 */
public class PopListViewAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	
	private ArrayList<String> list; 
	
	private int typelist=0;

	public PopListViewAdapter(Context context, ArrayList<String> list, int listtype) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.typelist=listtype;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		//  Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if(typelist==1){
				//convertView = inflater.inflate(R.layout.chs_pop_outch_name_items, null);
				convertView = inflater.inflate(R.layout.chs_pop_lv_items, null);
			}if(typelist==2){
				convertView = inflater.inflate(R.layout.chs_pop_outch_name_items, null);
				//convertView = inflater.inflate(R.layout.chs_pop_lv_items, null);
			}else{
				convertView = inflater.inflate(R.layout.chs_pop_lv_items, null);
			}
			
		}
		TextView tv = (TextView)convertView.findViewById(R.id.text);
		tv.setText(list.get(position));
		
		return convertView;
	}

}
