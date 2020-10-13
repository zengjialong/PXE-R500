package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bean.SEFF_File;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.operation.DataOptUtil;

import java.io.File;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SEFFSave_DialogFragment extends DialogFragment {
    //存储音效文件弹出框
    private Toast mToast;
    private LinearLayout LY_SSM_Single,LY_SSM_MAC;
    private TextView TV_SSM_Msg;
    private EditText ET_SSM_FileName,ET_SSM_Detials;
    private String SSM_Detials = "";
    private Button  B_SSM_Single,B_B_SSM_MAC,B_SSM_Save,B_SSM_Cancel;
    private boolean bool_FileType;//false:single true:mac
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
        View view = inflater.inflate(R.layout.chs_dialog_save_singlemac_file, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_SaveSMacFile) {
        B_SSM_Save = (Button)V_SaveSMacFile.findViewById(R.id.id_b_ssm_save);
        B_SSM_Cancel = (Button)V_SaveSMacFile.findViewById(R.id.id_b_ssm_cancel);

        imageView= V_SaveSMacFile.findViewById(R.id.id_btn_exit);


        LY_SSM_Single = (LinearLayout)V_SaveSMacFile.findViewById(R.id.id_llyout_single_sel);
        LY_SSM_MAC = (LinearLayout)V_SaveSMacFile.findViewById(R.id.id_llyout_mac_sel);
        B_SSM_Single = (Button)V_SaveSMacFile.findViewById(R.id.id_b_sel_single);
        B_B_SSM_MAC = (Button)V_SaveSMacFile.findViewById(R.id.id_b_sel_mac);
        ET_SSM_FileName = (EditText)V_SaveSMacFile.findViewById(R.id.id_et_filename);
        ET_SSM_Detials = (EditText)V_SaveSMacFile.findViewById(R.id.id_et_filedetials);
        TV_SSM_Msg = (TextView)V_SaveSMacFile.findViewById(R.id.id_b_mac_msg);

        B_SSM_Single.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_press));
        B_B_SSM_MAC.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_normal));
        bool_FileType = false;
       // TV_SSM_Msg.setVisibility(View.INVISIBLE);
	}

	private void initData(){

	}

	private void initClick() {
        LY_SSM_Single.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_SSM_Single.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_press));
                B_B_SSM_MAC.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_normal));
                bool_FileType = false;
               // TV_SSM_Msg.setVisibility(View.INVISIBLE);
            }
        });

        LY_SSM_MAC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_SSM_Single.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_normal));
                B_B_SSM_MAC.setBackground(getResources().getDrawable(R.drawable.chs_ssm_btncheck_press));
                bool_FileType = true;
               // TV_SSM_Msg.setVisibility(View.VISIBLE);
            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        B_SSM_Save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = ET_SSM_FileName.getText().toString();
                DataStruct.SSM_Detials = ET_SSM_Detials.getText().toString();
                DataStruct.bool_ShareOrSaveMacSEFF = false;
               // hideInput();


                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }
                if(checkSEFFileIsExit( Name)){
                    if(Name.length() > 0){
                        DataStruct.fileNameString = ET_SSM_FileName.getText().toString();
                        if(bool_FileType){
                            DataOptUtil.ReadMacGroup(getActivity());
                        }else{
                            DataOptUtil.SaveSingleSEFFData(DataStruct.fileNameString,getActivity());
                        }
                        getDialog().cancel();
                    }else{
                        ToastMsg(getResources().getString(R.string.SSM_FileNameMsg));
                    }
                }
            }
        });

        B_SSM_Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideInput();
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }
            }
        });

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


    /**
     * 消息提示
     */
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(getActivity(), Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private boolean checkSEFFileIsExit(String Name){
        File cache = new File(Environment.getExternalStorageDirectory(),  MacCfg.AgentNAME);
        File destDir = new File(cache.toString()+
                "/"+ MacCfg.Mac+
                "/"+"SoundEff"
        );
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String filePath = "";
        if(!bool_FileType){
            filePath = destDir.toString()+
                    "/"+Name+ Define.CHS_SEff_TYPE;//json
        }else{
            filePath = destDir.toString()+
                    "/"+Name+Define.CHS_SEff_MAC_TYPE;//json
        }

        SEFF_File SEFF_file = DataStruct.dbSEfFile_Table.find("file_path",filePath);
        if(SEFF_file != null){
            ToastMsg(getResources().getString(R.string.ULSE_SAMENAME));
            return false;
        }
        return true;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        hideInput();
//        InputMethodManager imm = (InputMethodManager)
//                getActivity().getSystemService(INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        if(mSetOnClickDialogListener != null){
//            mSetOnClickDialogListener.onClickDialogListener(0, false);
//        }
    }

}
