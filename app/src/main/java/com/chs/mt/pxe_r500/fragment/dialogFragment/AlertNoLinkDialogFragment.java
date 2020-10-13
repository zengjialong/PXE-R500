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

public class AlertNoLinkDialogFragment extends DialogFragment{

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
private ImageView imageView;
	private TextView TVMsg,TVTitle;
	private Button   BtnOK,BtnCancel;
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

//        DataOPT = getArguments().getInt(ST_DataOPT);
//        SetMessage = getArguments().getString(ST_SetMessage,null);
//        SetTitle = getArguments().getString(ST_SetTitle,null);
//        SetCancelText = getArguments().getString(ST_SetCancelText,null);
//        SetOKText = getArguments().getString(ST_SetOKText,null);


		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.chs_dialog_nolink_alter, container,false);
		initView(view);
		initData();
		initClick();
		return view;
	}

	private void initView(View V_AboutDialog) {

        BtnOK = (Button)V_AboutDialog.findViewById(R.id.id_b_save);
        BtnCancel = (Button)V_AboutDialog.findViewById(R.id.id_b_cancel);
		TVTitle=(TextView) V_AboutDialog.findViewById(R.id.id_tab_title);




		TVMsg = (TextView) V_AboutDialog.findViewById(R.id.id_tv_msg);

		imageView=V_AboutDialog.findViewById(R.id.id_btn_exit);


		BtnOK.setTextColor(getResources().getColor(R.color.dialog_btn_text_normal_color));
		BtnCancel.setTextColor(getResources().getColor(R.color.dialog_btn_text_normal_color));

        BtnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().cancel();
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(DataOPT, true);
				}
			}
		});

        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(DataOPT, false);
                }

            }
        });
		imageView.setOnClickListener(new View.OnClickListener() {
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
