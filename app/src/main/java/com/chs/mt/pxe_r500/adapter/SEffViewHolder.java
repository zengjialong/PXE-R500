package com.chs.mt.pxe_r500.adapter;
import com.chs.mt.pxe_r500.R;

import java.io.Serializable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




@SuppressWarnings("serial")
public class SEffViewHolder implements Serializable {
	
	private View contentView;
		
	private Button ItemSel;//item delete sel
	
	private LinearLayout LY_Item;//item
	private Button titleB;//item content
	private TextView TV_User,TV_UploadTime,TV_FileType;//
	private Button B_FavoriteBar;
	private Button B_LoveBar;
	
	
	private ImageView switcherIv;//item show pull
	//����
	private View targetPanel;
	private Button B_Favorite;//item favorite
	private Button B_Love;//item love
	private int MAX_LY_Buttom=6;
	private LinearLayout LY_Buttom[] = new LinearLayout[MAX_LY_Buttom];
       
    public SEffViewHolder(View contentView) {
        this.contentView = contentView;
        ItemSel = (Button) contentView.findViewById(R.id.seff_id_item_sel);        
        
        titleB = (Button) contentView.findViewById(R.id.seff_id_title_tv);
        LY_Item = (LinearLayout) contentView.findViewById(R.id.id_ly_item_seff);
        switcherIv = (ImageView) contentView.findViewById(R.id.seff_id_switcher_btn);
        targetPanel = contentView.findViewById(R.id.seff_id_panel_li);
        TV_User = (TextView) contentView.findViewById(R.id.seff_id_title_tv_user);
        TV_UploadTime = (TextView) contentView.findViewById(R.id.seff_id_title_tv_upload_time);
        TV_FileType = (TextView) contentView.findViewById(R.id.seff_id_filetype);
        B_FavoriteBar = (Button) contentView.findViewById(R.id.id_iv_favorite);
        B_LoveBar = (Button) contentView.findViewById(R.id.id_iv_love);
        
        B_Favorite = (Button) contentView.findViewById(R.id.id_b_seff_list_3);
        B_Love = (Button) contentView.findViewById(R.id.id_b_seff_list_4);

        
        LY_Buttom[0] = (LinearLayout) contentView.findViewById(R.id.id_ly_seff_list_1);
        LY_Buttom[1] = (LinearLayout) contentView.findViewById(R.id.id_ly_seff_list_2);
        LY_Buttom[2] = (LinearLayout) contentView.findViewById(R.id.id_ly_seff_list_3);
        LY_Buttom[3] = (LinearLayout) contentView.findViewById(R.id.id_ly_seff_list_4);
        LY_Buttom[4] = (LinearLayout) contentView.findViewById(R.id.id_ly_seff_list_5);
        LY_Buttom[5] = (LinearLayout) contentView.findViewById(R.id.id_ly_seff_list_6);
    }
    
    public View get_contentView(){
    	return this.contentView;
    }
    public Button get_ItemSel(){
    	return this.ItemSel;
    }
    public LinearLayout get_LY_Item(){
    	return this.LY_Item;
    }
    public Button get_titleB(){
    	return this.titleB;
    }
    public TextView get_TV_User(){
    	return this.TV_User;
    }
    public TextView get_TV_UploadTime(){
    	return this.TV_UploadTime;
    }
    public TextView get_TV_FileType(){
    	return this.TV_FileType;
    }
    public Button get_B_FavoriteBar(){
    	return this.B_FavoriteBar;
    }
    public Button get_B_LoveBar(){
    	return this.B_LoveBar;
    }
    public ImageView get_switcherIv(){
    	return this.switcherIv;
    }

    public View get_targetPanel(){
    	return this.targetPanel;
    }
    public Button get_B_Favorite(){
    	return this.B_Favorite;
    }
    public Button get_B_Love(){
    	return this.B_Love;
    }
    public LinearLayout get_LY_Buttom(int dex){
    	if(dex>=MAX_LY_Buttom){
    		return null;
    	}
    	return this.LY_Buttom[dex];
    }
     
}
