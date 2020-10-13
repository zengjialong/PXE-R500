package com.chs.mt.pxe_r500.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bean.ImageText_Data;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ListImageTextViewAdapter extends BaseAdapter {
	
	private setListImageTextAdpterOnItemClick myAdpterOnclick;
	
    private final List<ImageText_Data> dataSource = new ArrayList<ImageText_Data>();
    private LayoutInflater mLayoutInflater = null;
    private int dimensionPixelSize = 0;
    private Context mContext;
    
    public ListImageTextViewAdapter(Context cxt, List<ImageText_Data> dataSource) {
        this.dataSource.addAll(dataSource);
        mLayoutInflater = LayoutInflater.from(cxt);
        dimensionPixelSize  = cxt.getResources().getDimensionPixelSize(R.dimen.seff_item_height2);
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
    public void setOnListImageTextAdpterOnItemClick(setListImageTextAdpterOnItemClick l) {  
    	myAdpterOnclick = l;  
    } 
    public interface setListImageTextAdpterOnItemClick{
    	public void onAdpterClick(int which,int postion,View v);
    }
    
    @SuppressLint("NewApi") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageTextViewHolder imageTextViewHolder = null;

        if (convertView == null
                || !ImageTextViewHolder.class.isInstance(convertView.getTag())) {
            convertView = mLayoutInflater.inflate(R.layout.chs_list_imagetextview,
                    null);
            imageTextViewHolder = new ImageTextViewHolder(convertView);
            convertView.setTag(imageTextViewHolder);
        } else {
            imageTextViewHolder = (ImageTextViewHolder) convertView.getTag();
        }
        ImageText_Data list_data = dataSource.get(position);

        if(list_data.getShowImage().equals("1")){
        	imageTextViewHolder.get_IV_Head().setVisibility(View.VISIBLE);
        	
        	try {
            	int dot=list_data.getImagePath().lastIndexOf("/");
       	    	String name=list_data.getImagePath().substring(dot+1);   
       	    	
       	    	InputStream is = mContext.getResources().getAssets().open("carlogo/"+name);
       	        Bitmap bitmap = BitmapFactory.decodeStream(is);
            	imageTextViewHolder.get_IV_Head().setImageBitmap(bitmap);
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }else{
        	imageTextViewHolder.get_IV_Head().setVisibility(View.GONE);
        }
        imageTextViewHolder.get_TV_Item().setText(list_data.get_tv_item());
        
        final int fpostion = position;
        imageTextViewHolder.get_LY_Item().setOnClickListener(new View.OnClickListener() {			
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
