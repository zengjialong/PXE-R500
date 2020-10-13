package com.chs.mt.pxe_r500.adapter;
import com.chs.mt.pxe_r500.R;

import java.io.Serializable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




@SuppressWarnings("serial")
public class ImageTextViewHolder implements Serializable {
	
	private View contentView;
	
	private LinearLayout LY_Item;
	private ImageView IV_Head;
	private TextView TV_Item;

       
    public ImageTextViewHolder(View contentView) {
        this.contentView = contentView;
        
        LY_Item = (LinearLayout) contentView.findViewById(R.id.id_ly_item);
        IV_Head = (ImageView) contentView.findViewById(R.id.id_iv_head);
        TV_Item = (TextView) contentView.findViewById(R.id.id_tv_title);
    }
    
    public View get_contentView(){
    	return this.contentView;
    }

    public LinearLayout get_LY_Item(){
    	return this.LY_Item;
    }
    
    public ImageView get_IV_Head(){
    	return this.IV_Head;
    }
    
    public TextView get_TV_Item(){
    	return this.TV_Item;
    }

}
