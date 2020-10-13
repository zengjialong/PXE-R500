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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.MacCfg;

public class WarningDialogIFragment extends DialogFragment{

	private int DataOPT = 1;
	public static final String ST_DataOPT = "ST_DataOPT";
	private String SetMessage = "";
	public static final String ST_SetMessage = "ST_SetMessage";
	private String SetTitle = "";
	public static final String ST_SetTitle = "ST_SetTitle";
    private String SetCancelText = "";
    public static final String ST_SetCancelText = "ST_SetCancelText";
    private String SetOKText = "";
    public static final String ST_SetOKText = "ST_SetOKText";

	private TextView TVMsg,TVTitle;
	private Button   BtnOK;
	private ImageView imageView_exit;
	
	private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }
	public interface SetOnClickDialogListener{
		void onClickDialogListener(int type, boolean boolClick);
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

        DataOPT = getArguments().getInt(ST_DataOPT);
        SetMessage = getArguments().getString(ST_SetMessage);
        SetTitle = getArguments().getString(ST_SetTitle);
        SetCancelText = getArguments().getString(ST_SetCancelText);
        SetOKText = getArguments().getString(ST_SetOKText);


		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getDialog().setCanceledOnTouchOutside(false);
		View view = inflater.inflate(R.layout.chs_dialog_warinng, container,false);
		initView(view);
		initData();
		initClick();
		return view;
	}

	private void initView(View V_AboutDialog) {

        BtnOK = (Button)V_AboutDialog.findViewById(R.id.id_b_save);
		TVMsg = (TextView) V_AboutDialog.findViewById(R.id.id_tv_msg);
		TVTitle = (TextView) V_AboutDialog.findViewById(R.id.id_tv_title);


				imageView_exit = (ImageView) V_AboutDialog.findViewById(R.id.id_btn_exit);

        TVTitle.setText(SetTitle);
        TVMsg.setText(SetMessage);
        BtnOK.setText(SetOKText);

        BtnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().cancel();
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(DataOPT, true);
				}

			}
		});
		imageView_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
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

	private void initData() {

	}

	private void initClick() {

	}

}
