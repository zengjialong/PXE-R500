package com.chs.mt.pxe_r500.bluetooth.spp_ble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bean.BTinfo;

import java.util.ArrayList;
import java.util.List;


public class BTAdapter extends BaseAdapter {

	private setAdpterOnItemClick myAdpterOnclick;
    private List<BTinfo> dataSource = new ArrayList<BTinfo>();
    private LayoutInflater mLayoutInflater = null;
    private Context mContext;
    public BTAdapter(Context cxt, List<BTinfo> dataSource) {
        this.dataSource.addAll(dataSource);
        mLayoutInflater = LayoutInflater.from(cxt);
        mContext = cxt;
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    public void setData(List<BTinfo> newPeopleList){
        this.dataSource = newPeopleList;
    }
    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public void setOnAdpterOnItemClick(setAdpterOnItemClick l) {
    	myAdpterOnclick = l;
    }
    public interface setAdpterOnItemClick{
    	public void onAdpterClick(int which, int postion, View v);
    }

    @SuppressLint("NewApi") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BTViewHolder mBTViewHolder = null;

        if (convertView == null
                || !BTViewHolder.class.isInstance(convertView.getTag())) {
            convertView = mLayoutInflater.inflate(R.layout.chs_listview_bt_item,
                    null);
            mBTViewHolder = new BTViewHolder(convertView);
            convertView.setTag(mBTViewHolder);
        } else {
            mBTViewHolder = (BTViewHolder) convertView.getTag();
        }
        BTinfo lisdata = dataSource.get(position);
        mBTViewHolder.ID.setText(lisdata.device.getAddress());
        mBTViewHolder.Name.setText(lisdata.device.getName());

        if(lisdata.sel){
            mBTViewHolder.LY.setBackgroundColor(mContext.getResources().getColor(R.color.bluetoothlistselect_color_press));
            mBTViewHolder.ID.setTextColor(mContext.getResources().getColor(R.color.bluetoothlistID_color_press));
            mBTViewHolder.Name.setTextColor(mContext.getResources().getColor(R.color.bluetoothlistName_color_press));
        }else {
            mBTViewHolder.LY.setBackgroundColor(mContext.getResources().getColor(R.color.bluetoothlistselect_color_normal));
            mBTViewHolder.ID.setTextColor(mContext.getResources().getColor(R.color.bluetoothlistID_color_normal));
            mBTViewHolder.Name.setTextColor(mContext.getResources().getColor(R.color.bluetoothlistName_color_normal));
        }

        if(lisdata.BoolStartCnt){
            mBTViewHolder.LYAutoC.setVisibility(View.VISIBLE);
            mBTViewHolder.TVT.setText("("+String.valueOf(lisdata.time)+")");
        }else {
            mBTViewHolder.LYAutoC.setVisibility(View.GONE);
        }

        final int fpostion = position;
        mBTViewHolder.LY.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {
					
					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});

        return convertView;
    }

}
