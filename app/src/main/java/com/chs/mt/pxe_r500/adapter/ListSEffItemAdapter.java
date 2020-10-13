package com.chs.mt.pxe_r500.adapter;
import com.chs.mt.pxe_r500.R;
import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.bean.JsonDataSt;
import com.chs.mt.pxe_r500.bean.SEff_ListDat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;


public class ListSEffItemAdapter extends BaseAdapter {
	private setSeffFileAdpterOnItemClick myAdpterOnclick;
	private final List<SEff_ListDat> dataSource = new ArrayList<SEff_ListDat>();
	private LayoutInflater mLayoutInflater = null;
	private int dimensionPixelSize = 0;
	private Context mContext;
	public ListSEffItemAdapter(Context cxt, List<SEff_ListDat> dataSource) {
		this.dataSource.addAll(dataSource);
		mLayoutInflater = LayoutInflater.from(cxt);
		dimensionPixelSize  = cxt.getResources().getDimensionPixelSize(R.dimen.seff_item_ly_height);
		mContext = cxt;
	}
	@Override
	public int getCount() {

		return dataSource.size();
	}

	@Override
	public Object getItem(int position) {
		return dataSource.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	public void setOnSeffFileAdpterOnItemClick(setSeffFileAdpterOnItemClick l) {
		myAdpterOnclick = l;
	}
	public interface setSeffFileAdpterOnItemClick{
		public void onAdpterClick(int which,int postion,View v);
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SEffViewHolder seffViewHolder = null;

		if (convertView == null
				|| !SEffViewHolder.class.isInstance(convertView.getTag())) {
			convertView = mLayoutInflater.inflate(R.layout.chs_listview_seff,
					null);
			seffViewHolder = new SEffViewHolder(convertView);
			convertView.setTag(seffViewHolder);
		} else {
			seffViewHolder = (SEffViewHolder) convertView.getTag();
		}
		SEff_ListDat seff = dataSource.get(position);
		LayoutParams lp = (LayoutParams) seffViewHolder.get_targetPanel().getLayoutParams();
		if(seff.isOpen.equals("1")){
			convertView.setRotation(0f);
			lp.bottomMargin = 0;
			lp.height =  dimensionPixelSize;

		}else{
			convertView.setRotation( 0f);
			lp.bottomMargin = - dimensionPixelSize;
			//�߶���0,lp.bottomMarginֵ��Ч������֮�������ø߶�Ϊ0����ΪΪ�˷�ֹ���ֺ�ʱ���»��Ʋ���ʱ���Ӷ����ִ�����ʾ
			lp.height = 0;
		}
		seffViewHolder.get_targetPanel().setLayoutParams(lp);
		seffViewHolder.get_titleB().setText(seff.name);
		seffViewHolder.get_TV_User().setText(seff.user);
		seffViewHolder.get_TV_UploadTime().setText("Upload_time"
				+ seff.upload_time);
		if(seff.getFravorite().equals("0")){
			seffViewHolder.get_B_FavoriteBar().setVisibility(android.view.View.GONE);
		}else if(seff.getFravorite().equals("1")){
			seffViewHolder.get_B_FavoriteBar().setVisibility(android.view.View.VISIBLE);
			seffViewHolder.get_B_FavoriteBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
		}

		if(seff.getLove().equals("0")){
			seffViewHolder.get_B_LoveBar().setVisibility(android.view.View.GONE);
		}else if(seff.getLove().equals("1")){
			seffViewHolder.get_B_LoveBar().setVisibility(android.view.View.VISIBLE);
			seffViewHolder.get_B_LoveBar().setBackground(mContext.getResources().getDrawable(R.drawable.chs_seff_love_press));
		}
		//0:
		if(seff.sel.equals("0")){
			seffViewHolder.get_ItemSel().setVisibility(View.INVISIBLE) ;
		}else if(seff.sel.equals("1")){
			seffViewHolder.get_ItemSel().setVisibility(View.VISIBLE);
			seffViewHolder.get_ItemSel().setBackgroundResource(R.drawable.chs_seff_sel_normal) ;
		}else if(seff.sel.equals("2")){
			seffViewHolder.get_ItemSel().setVisibility(View.VISIBLE);
			seffViewHolder.get_ItemSel().setBackgroundResource(R.drawable.chs_seff_sel_press) ;
		}
		//filetype
		if(seff.fileType.equals(JsonDataSt.DSP_Single)){
			seffViewHolder.get_TV_FileType().setText("SF");
		}else if(seff.fileType.equals(JsonDataSt.DSP_Complete)){
			seffViewHolder.get_TV_FileType().setText("AF");
		}
		final int fpostion = position;
		seffViewHolder.get_ItemSel().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Item().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_switcherIv().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
//        seffViewHolder.get_titleB().setOnClickListener(new View.OnClickListener() {			
//			@Override
//			public void onClick(View v) {
//				if (myAdpterOnclick != null) {
//					
//					int which = v.getId();
//					myAdpterOnclick.onAdpterClick(which, fpostion,v);
//				}
//			}
//		});
		seffViewHolder.get_B_FavoriteBar().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_B_LoveBar().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Buttom(0).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Buttom(1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Buttom(2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Buttom(3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Buttom(4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {

					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
		seffViewHolder.get_LY_Buttom(5).setOnClickListener(new View.OnClickListener() {
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
