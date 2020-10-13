package com.chs.mt.pxe_r500.fragment.dialogFragment;
import com.chs.mt.pxe_r500.R;



import java.io.UnsupportedEncodingException;

import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class UserGroupDeleteDialogFragment extends DialogFragment {
	private Toast mToast;
	private static Context mContext;	
	
	/* 调用，保存,删除 用户组 数据       */	
	private View V_UserGSel_Dialog;	
	private Dialog UserGroupOptDialog;
	private Button B_Store,B_Recall,B_Delete;
	
	private View   V_UserGDel_Dialog;
	private Dialog UserGroupDelDialog;
	private Button B_GroupDEL,B_GroupDEL_Cancle;
	/* 编辑 用户组名字     */	
	private boolean b_EditGN=false;//false:取消编辑用户名字，true:正在编辑用户名字
	private boolean b_DelGN=false;
	private LinearLayout LLyout_UserGroupName,LLyout_UserGroupName_del;
	private EditText ET_UGNane;
	
	/*长按按键的操作：0-减操作，1-加操作*/
	@SuppressWarnings("unused")
	private int SYNC_INCSUB=0;
	/*按键长按：true-长按，false-非长按*/
	@SuppressWarnings("unused")
	private boolean B_LongPress=false;
	private boolean bool_PasswordCorrect=false;

	private int UserGroup = 0;
	public static final String ST_UserGroup = "UserGroup";
	private ImageView ValDialogButton;
 	private OnUserGroupDeleteDialogFragmentClickListener mUserGroupDeleteListener;
    public void OnSetUserGroupDeleteDialogFragmentChangeListener(OnUserGroupDeleteDialogFragmentClickListener listener) {
        this.mUserGroupDeleteListener = listener;
    }

    public interface OnUserGroupDeleteDialogFragmentClickListener{
        void onUserGroupDeleteListener(int Index, boolean UserGroupflag);
    }


	@Override
	public void onStart() {
		super.onStart();
		if(MacCfg.BOOL_DialogHideBG){
			Window window = getDialog().getWindow();
			WindowManager.LayoutParams windowParams = window.getAttributes();
			windowParams.dimAmount = 0.0f;
			windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			window.setAttributes(windowParams);
		}
	}

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		mContext = getActivity().getApplicationContext();
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);        
//		getDialog().setTitle(R.string.SoundOpt);
		
        View view = inflater.inflate(R.layout.chs_user_group_del_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_UserGDel_Dialog) {  	
    	B_GroupDEL = (Button) V_UserGDel_Dialog.findViewById(R.id.id_b_group_sel_delete);
    	B_GroupDEL_Cancle = (Button)V_UserGDel_Dialog.findViewById(R.id.id_b_group_delete_sel_cancle);
		ValDialogButton = (ImageView)V_UserGDel_Dialog.findViewById(R.id.id_btn_exit);
	}

	private void initData() {
		
	}

	private void initClick() {
		/*删除用户组数据选项*/
    	B_GroupDEL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mUserGroupDeleteListener != null){
					mUserGroupDeleteListener.onUserGroupDeleteListener(0, true);
				}
				getDialog().cancel();
			}
		});
    	B_GroupDEL_Cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getDialog().cancel();
			}
		});
		ValDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				getDialog().cancel();
			}
		});
	}
	//////////////////////////////////////////////
	/**
	* 消息提示
	*/
	private void ToastMsg(String Msg) {
		if (null != mToast) {
			mToast.setText(Msg);
		} else {
			mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	/**
     * 检测用户输入的名字，check 0:主界面，1：保存对话框
     */
    boolean CheckGroupName(int check){
    	boolean b_ret=false;
    	String gname="";
		if(check==0){
			gname=String.valueOf(ET_UGNane.getText());
		}else if(check==1){
			//gname=String.valueOf(ET_StoreSel.getText());
		} 
    	
		if(gname.length()>=1){
    		for(int i=0;i<gname.length();i++){
				if(gname.charAt(i)>=0x21){
					b_ret=true;
	    		}
	    	}
    	}else{
    		b_ret = false;
    	}
		return b_ret;
    }
    
    private String getGBKString(int[] nameC){
    	byte[] GBK=new byte[16];
    	for(int j=0;j<16;j++){
    		GBK[j]=0x00;				
		}
    	int n=0;
		String uNameString = null;
		for(int j=0;j<13;j++){
			if(nameC[j] != 0x00){
				GBK[j]=(byte) nameC[j];
				++n;
			}				
		}
		try {
			byte[] GBKN=new byte[n];
			for(int j=0;j<n;j++){
				GBKN[j]=GBK[j];
			}
			uNameString = new String(GBKN, "GBK");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
    	
		return uNameString;   	
    }
    
    private boolean checkUserGroupByteNull(int[] ug){
    	
    	for(int i=0;i<15;i++){
    		if(ug[i]!=0x00){
    			return true;
    		}
    	}
    	return false;
    }
    
    void ShowEditGroupName(){
    	if(checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup])){
    		ET_UGNane.setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup]));  
    	}else{
    		ET_UGNane.setText(""); 
    	}
    }
}
