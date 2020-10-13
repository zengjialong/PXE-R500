package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.operation.DataOptUtil;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SEFFShare_DialogFragment extends DialogFragment {
    //分享音效文件弹出框

    private LinearLayout LY_ShareSM_Single,LY_ShareSM_MAC;
    private TextView TV_ShareSM_Msg;
    private EditText ET_ShareSM_FileName,ET_ShareSM_Detials;
    private String ShareSM_Detials = "";
    private Button  B_ShareSM_Single,B_ShareSM_MAC,B_ShareSM_Save,B_ShareSM_Cancel;
    private boolean bool_ShareFileType;//false:single true:mac
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
        View view = inflater.inflate(R.layout.chs_dialog_share_singlemac_file, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_ShareSMacFile) {
        B_ShareSM_Save = (Button)V_ShareSMacFile.findViewById(R.id.id_b_ssm_save);
        B_ShareSM_Cancel = (Button)V_ShareSMacFile.findViewById(R.id.id_b_ssm_cancel);

        LY_ShareSM_Single = (LinearLayout)V_ShareSMacFile.findViewById(R.id.id_llyout_single_sel);
        LY_ShareSM_MAC = (LinearLayout)V_ShareSMacFile.findViewById(R.id.id_llyout_mac_sel);
        B_ShareSM_Single = (Button)V_ShareSMacFile.findViewById(R.id.id_b_sel_single);
        B_ShareSM_MAC = (Button)V_ShareSMacFile.findViewById(R.id.id_b_sel_mac);

        TV_ShareSM_Msg = (TextView)V_ShareSMacFile.findViewById(R.id.id_b_mac_msg);
        imageView=V_ShareSMacFile.findViewById(R.id.id_chs_dialog_exit);

	}

	private void initData() {
        bool_ShareFileType = false;
	}

	private void initClick() {
        LY_ShareSM_Single.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_ShareSM_Single.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_press));
                B_ShareSM_MAC.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_normal));
                bool_ShareFileType = false;
                //TV_ShareSM_Msg.setVisibility(View.INVISIBLE);
            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        LY_ShareSM_MAC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_ShareSM_Single.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_normal));
                B_ShareSM_MAC.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_press));
                bool_ShareFileType = true;
               // TV_ShareSM_Msg.setVisibility(View.VISIBLE);
            }
        });


        B_ShareSM_Save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }
                DataStruct.bool_ShareOrSaveMacSEFF = true;
                if(bool_ShareFileType){
                    DataStruct.fileNameString = Define.ShareDefaultGruopName;
                    DataOptUtil.ReadMacGroup(getActivity());
                }else{
                    DataStruct.fileNameString = Define.ShareDefaultName;
                    DataOptUtil.ShareEffData(getActivity());
                }
            }
        });

        B_ShareSM_Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }
            }
        });

	}

}
