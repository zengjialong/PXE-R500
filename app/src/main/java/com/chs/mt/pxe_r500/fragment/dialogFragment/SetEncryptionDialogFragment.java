package com.chs.mt.pxe_r500.fragment.dialogFragment;
import com.chs.mt.pxe_r500.R;


import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SetEncryptionDialogFragment extends DialogFragment {
	private static final int MaxEnNum=5;
	
	private Toast mToast;
	private static Context mContext;	
	private Button B_Encrypt_OK,B_Encrypt_Cancle;//B_Encrypt_Clean,B_Encrypt_CleanCancle;
	private EditText ET_Encryption;

	/*长按按键的操作：0-减操作，1-加操作*/
	@SuppressWarnings("unused")
	private int SYNC_INCSUB=0;
	/*按键长按：true-长按，false-非长按*/
	@SuppressWarnings("unused")
	private boolean B_LongPress=false;
	private boolean bool_PasswordCorrect=false;
	private TextView txt_title;
	private ImageView img_exit;
	
 	private OnEncryptionDialogFragmentClickListener mEncryptionListener;
    public void OnSetEncryptionDialogFragmentChangeListener(OnEncryptionDialogFragmentClickListener listener) {
        this.mEncryptionListener = listener;
    }

    public interface OnEncryptionDialogFragmentClickListener{
        void onEncryptionClickListener(boolean Encryptionflag, boolean recallFlag);
    }

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		mContext = getActivity().getApplicationContext();	
		
      getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//		if(MacCfg.bool_Encryption){
//			getDialog().setTitle(R.string.Deciphering);
//		}else{
//			getDialog().setTitle(R.string.SetEncryption);
//		}
		
        View view = inflater.inflate(R.layout.chs_set_encryption_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_Set_Encryption_Dialog) {  	
		B_Encrypt_OK = (Button)V_Set_Encryption_Dialog.findViewById(R.id.id_b_encryption_ok);     	
    	B_Encrypt_Cancle = (Button)V_Set_Encryption_Dialog.findViewById(R.id.id_b_encryption_cancle);
    	ET_Encryption = (EditText)V_Set_Encryption_Dialog.findViewById(R.id.id_et_set_encryption);
		txt_title = (TextView) V_Set_Encryption_Dialog.findViewById(R.id.id_b_set_title);
		img_exit=V_Set_Encryption_Dialog.findViewById(R.id.id_btn_exit);
	}

	private void initData() {
		for (int i = 0; i < MacCfg.Encryption_PasswordBuf.length ; i++) {
			MacCfg.Encryption_PasswordBuf[i]= DataStruct.RcvDeviceData.OUT_CH[0].name[i+1];
		}
		if(MacCfg.bool_Encryption==true){
			// MacCfg.bool_Encryption=true;
			img_exit.setVisibility(View.VISIBLE);
			B_Encrypt_Cancle.setText(getResources().getString(R.string.EncryptionClean));
		}else{
			// MacCfg.bool_Encryption=false;
			img_exit.setVisibility(View.GONE);
			B_Encrypt_Cancle.setText(getResources().getString(R.string.cancel));
		}

		if (MacCfg.bool_Encryption) {
			txt_title.setText(getResources().getString(R.string.Deciphering));
		} else {
			txt_title.setText(getResources().getString(R.string.Encryption));
		}
	}

	private void initClick() {
		//确定
    	B_Encrypt_OK.setOnClickListener(new OnClickListener() {   		
			@Override
			public void onClick(View view) {	
				//确保通信正常
//				if (DataStruct.isConnecting) {
//				} else {
//					ToastMsg(getResources().getString(R.string.off_line_mode));
//					return;
//				}
				//判断密码是否符合规范
				byte[] pwd = new byte[10];
				if(ET_Encryption.getText().length()==6){
					String passwd=ET_Encryption.getText().toString();					
					pwd=passwd.getBytes();
				}else{
					ToastMsg(getResources().getString(R.string.PasswordIncorrect));
	  				return;
				}
				//System.out.println("pwd bool_Set_Encryption2:"+bool_Set_Encryption);
				if(MacCfg.bool_Encryption==false){  //加密
					for (int i = 0; i < MacCfg.Encryption_PasswordBuf.length; i++) {
						MacCfg.Encryption_PasswordBuf[i] = pwd[i];
						DataStruct.RcvDeviceData.OUT_CH[0].name[i+1] = MacCfg.Encryption_PasswordBuf[i];
					}
					MacCfg.bool_Encryption = true;

					for (int i = 0; i <DataStruct.CurMacMode.Out.OUT_CH_MAX_USE ; i++) {
						DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg=0x21;
					}

					if (mEncryptionListener != null) {
						//加密数据处理
						mEncryptionListener.onEncryptionClickListener(true, false);
					}
	  			}else if(MacCfg.bool_Encryption==true){//解密
	  				bool_PasswordCorrect=true;
	  				for(int i=0;i<pwd.length;i++){
						if(MacCfg.Encryption_PasswordBuf[i]!=pwd[i]){
							bool_PasswordCorrect=false;
						}
					}
	  				
	  				if(bool_PasswordCorrect==true){//密码正确
	  					MacCfg.bool_Encryption=false;

						for (int i = 0; i <DataStruct.CurMacMode.Out.OUT_CH_MAX_USE ; i++) {
							DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg=0x20;
						}
						if (mEncryptionListener != null) {
							//加密数据处理
							mEncryptionListener.onEncryptionClickListener(false, false);
						}
		  			}else{
		  				ToastMsg(getResources().getString(R.string.PasswordIncorrect));
		  				return;		  				
		  			}	  				
	  			}
				getDialog().cancel();
			}
		});
    	B_Encrypt_Cancle.setOnClickListener(new OnClickListener() {   		
			@Override
			public void onClick(View view) {
				getDialog().cancel();
				if(MacCfg.bool_Encryption==true){
					if(mEncryptionListener != null){
						//加密数据处理
						mEncryptionListener.onEncryptionClickListener(false,true);
					}
				}

			}
		});
//    	B_EncryptClean.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				getDialog().cancel();
//
//
//			}
//		});
		img_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideInput() {

//		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
//
//		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	hideInput();
	}
}
