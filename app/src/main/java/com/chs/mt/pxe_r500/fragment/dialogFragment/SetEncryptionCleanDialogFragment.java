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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint({"NewApi", "ClickableViewAccessibility"})
public class SetEncryptionCleanDialogFragment extends DialogFragment {
	private Toast mToast;
	private static Context mContext;
	private Button B_Encrypt_Clean, B_Encrypt_CleanCancle;

	/*长按按键的操作：0-减操作，1-加操作*/
	@SuppressWarnings("unused")
	private int SYNC_INCSUB = 0;
	/*按键长按：true-长按，false-非长按*/
	@SuppressWarnings("unused")
	private boolean B_LongPress = false;
	private boolean bool_PasswordCorrect = false;
	private ImageView imageView;


	private OnEncryptionCleanDialogFragmentClickListener mEncryptionCleanListener;

	public void OnSetEncryptionDialogCleanFragmentChangeListener(OnEncryptionCleanDialogFragmentClickListener listener) {
		this.mEncryptionCleanListener = listener;
	}

	public interface OnEncryptionCleanDialogFragmentClickListener {
		void onEncryptionCleanClickListener(boolean EncryptionCleanflag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getDialog().setTitle(R.string.SetEncryptionClean);

		View view = inflater.inflate(R.layout.chs_set_encryption_clean_dialog, container, false);
		initView(view);
		initData();
		initClick();
		return view;
	}

	private void initView(View V_Set_EncryptionClean_Dialog) {
		B_Encrypt_Clean = (Button) V_Set_EncryptionClean_Dialog.findViewById(R.id.id_b_encryption_clean);
		B_Encrypt_CleanCancle = (Button) V_Set_EncryptionClean_Dialog.findViewById(R.id.id_b_encryption_clean_cancle);
		imageView= (ImageView) V_Set_EncryptionClean_Dialog.findViewById(R.id.id_btn_exit);
	}

	private void initData() {

	}

	private void initClick() {
		//清除当前组加密数据，只有在数据已加密时才有作用
		B_Encrypt_Clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//确保通信正常
				//if (DataStruct.isConnecting) {
				MacCfg.bool_Encryption = false;

				for (int i = 0; i <DataStruct.CurMacMode.Out.OUT_CH_MAX_USE ; i++) {
					DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg = 0x20;
				}

				if (mEncryptionCleanListener != null) {
					//加密数据处理
					mEncryptionCleanListener.onEncryptionCleanClickListener(true);
				}

				getDialog().cancel();
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});
		B_Encrypt_CleanCancle.setOnClickListener(new OnClickListener() {
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

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		hideInput();
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideInput() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
		View v = getActivity().getWindow().peekDecorView();
		if (null != v) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

}
