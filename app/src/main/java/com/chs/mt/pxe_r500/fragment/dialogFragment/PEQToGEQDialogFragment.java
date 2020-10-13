package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.content.DialogInterface;
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

public class PEQToGEQDialogFragment extends BaseDialogFragment {

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
	private Button BtnOK,BtnCancel;
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

        DataOPT = getArguments().getInt(ST_DataOPT);
        SetMessage = getArguments().getString(ST_SetMessage,null);
        SetTitle = getArguments().getString(ST_SetTitle,null);
        SetCancelText = getArguments().getString(ST_SetCancelText,null);
        SetOKText = getArguments().getString(ST_SetOKText,null);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

	//	getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.chs_dialog_peq, container,false);
		initView(view);
		initData();
		initClick();

		return view;
	}

	private void initView(View V_AboutDialog) {

        BtnOK = (Button)V_AboutDialog.findViewById(R.id.id_b_save);
        BtnCancel = (Button)V_AboutDialog.findViewById(R.id.id_b_cancel);
		TVMsg = (TextView) V_AboutDialog.findViewById(R.id.id_tv_msg);
		imageView=V_AboutDialog.findViewById(R.id.id_setdelay_dialog_button);
		if(DataOPT==2){
			TVMsg.setText(getResources().getString(R.string.PEQ_change_GEQ));
		}else{
			TVMsg.setText(getResources().getString(R.string.GEQ_change_PEQ));
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
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
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
