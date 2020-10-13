package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.chs.mt.pxe_r500.R;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class EnterAdvanceDialogFragment extends DialogFragment {
    //关于
    private Button B_IMMSure,B_IMMCancle;
    private EditText ET_IMM;

    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    private String SetMessage = "";
    public static final String ST_SetMessage = "ST_SetMessage";

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
        SetMessage = getArguments().getString(ST_SetMessage);

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_enteradvance, container,false);
        initView(view);
		initData();
		initClick();


        return view;  
    }

    private void initView(View view) {

        B_IMMSure = (Button) view.findViewById(R.id.id_b_ok);
        B_IMMCancle = (Button) view.findViewById(R.id.id_b_cancle);
        ET_IMM = (EditText) view.findViewById(R.id.id_et_mm_edit);
        ET_IMM.setText("");

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
                       {
                           @Override
                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager)ET_IMM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(ET_IMM, 0);
                           }
                       },
                998);

        B_IMMSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(ET_IMM.getText().toString().equals("8888")){
                    if(mSetOnClickDialogListener != null){
                        mSetOnClickDialogListener.onClickDialogListener(1, true);
                    }
                    getDialog().cancel();
                }else{

                    if(mSetOnClickDialogListener != null){
                        mSetOnClickDialogListener.onClickDialogListener(0, true);
                    }
                    getDialog().cancel();
                }
            }
        });


        B_IMMCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, false);
                }
                getDialog().cancel();
            }
        });


	}

	private void initData() {

	}

	private void initClick() {

	}

}
