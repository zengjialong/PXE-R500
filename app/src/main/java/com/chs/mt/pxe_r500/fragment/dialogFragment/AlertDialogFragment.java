package com.chs.mt.pxe_r500.fragment.dialogFragment;

import com.chs.mt.pxe_r500.R;

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

public class AlertDialogFragment extends BaseDialogFragment {

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        DataOPT = getArguments().getInt(ST_DataOPT);
        SetMessage = getArguments().getString(ST_SetMessage,null);
        SetTitle = getArguments().getString(ST_SetTitle,null);
        SetCancelText = getArguments().getString(ST_SetCancelText,null);
        SetOKText = getArguments().getString(ST_SetOKText,null);

		System.out.println("这");

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.chs_dialog_alter, container,false);
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


		if(DataOPT==1||DataOPT==2){
			BtnCancel.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}else{
			imageView.setVisibility(View.GONE);
			BtnCancel.setVisibility(View.VISIBLE);
		}

		//BtnOK.setBackground(R.drawable.chs_dialog_btn_normal);
//		if(SetTitle != null){
			TVTitle.setText(SetTitle);
//		}else {
//            TVTitle.setVisibility(View.GONE);
//        }
		if(SetMessage != null){
			TVMsg.setText(SetMessage);
		}
		if(SetOKText != null){
			BtnOK.setText(SetOKText);
		}
		if(SetCancelText != null){
			BtnCancel.setText(SetCancelText);
		}

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
