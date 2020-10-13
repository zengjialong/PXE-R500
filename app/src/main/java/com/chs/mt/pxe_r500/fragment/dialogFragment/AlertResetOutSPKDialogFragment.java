package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;

public class AlertResetOutSPKDialogFragment extends DialogFragment{

	private int DataOPT = 1;
	public static  String ST_DataOPT = "ST_DataOPT";   //文本上的文字
	private String SetMessage = "";
	public static  String ST_SetMessage = "ST_SetMessage";  //确定按钮
	public static  String BT_SetMessage = "BT_SetMessage";  //取消按钮
	private TextView txt_num;

	private Button   BtnOK,BtnCancel,BtnExit;
	private ImageView imageView;
	private SetOnClickDialogListener mSetOnClickDialogListener;
	public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
		this.mSetOnClickDialogListener = listener;
	}
	public interface SetOnClickDialogListener{
		void onClickDialogListener(int type, boolean boolClick);
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		ST_DataOPT = getArguments().getString(ST_DataOPT);
		ST_SetMessage = getArguments().getString(ST_SetMessage);
		BT_SetMessage = getArguments().getString(BT_SetMessage);


		View view = inflater.inflate(R.layout.chs_dialog_alter_reset, container,false);
		initView(view);
		flashPageUi();
		return view;
	}

	private void flashPageUi() {
		BtnOK.setText(ST_SetMessage);   //清空那里
		BtnCancel.setText(BT_SetMessage);    //默认
		txt_num.setText(ST_DataOPT);

	}

	private void initView(View V_AboutDialog) {

		BtnOK = (Button)V_AboutDialog.findViewById(R.id.id_b_save);
		BtnCancel = (Button)V_AboutDialog.findViewById(R.id.id_b_cancel);
		txt_num= (TextView) V_AboutDialog.findViewById(R.id.id_tv_msg);
		BtnExit=(Button)V_AboutDialog.findViewById(R.id.id_b_exit);

		imageView=V_AboutDialog.findViewById(R.id.id_setdelay_dialog_button);

//
//		BtnOK.setTextColor(getResources().getColor(R.color.dialog_btn_text_normal_color));
//		BtnCancel.setTextColor(getResources().getColor(R.color.dialog_btn_text_normal_color));

		BtnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().cancel();
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(0, true);
				}
			}
		});
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});

		BtnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(1, false);
				}
			}
		});

		BtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().cancel();
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(1, true);
				}

			}
		});

		getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}

}
