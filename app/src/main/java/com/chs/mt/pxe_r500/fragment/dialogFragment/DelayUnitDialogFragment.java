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
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class DelayUnitDialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;
    private Button[] B_Item = new Button[6];

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";



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

        data = getArguments().getInt(ST_Data);
        DataOPT = getArguments().getInt(ST_DataOPT);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_delayunit, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }


	private void initView(View view) {
        Exit = (Button) view.findViewById(R.id.id_chs_dialog_exit);
        Exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        Msg = (TextView) view.findViewById(R.id.id_text_msg);
//        Msg.setText(getString(R.string.Toning_DelayType));

        B_Item[0] = (Button) view.findViewById(R.id.id_chs_dialog_btn0);
        B_Item[1] = (Button) view.findViewById(R.id.id_chs_dialog_btn1);
        B_Item[2] = (Button) view.findViewById(R.id.id_chs_dialog_btn2);

        if(data > 2){
            data = 1;
        }
        setSel(data);
        B_Item[data].setTextColor(getResources().getColor(R.color.color_dialogItemTextPress));
        for(int i=0;i<3;i++){
            B_Item[i].setTag(i);
            B_Item[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = (Integer) v.getTag();
                    setSel(newVal);
                    if (mSetOnClickDialogListener != null) {
                        mSetOnClickDialogListener.onClickDialogListener(newVal+1, true);
                        getDialog().cancel();
                    }
                }
            });
        }

	}

	private void setSel(int sel){
        for(int i=0;i<3;i++) {
            B_Item[i].setTextColor(getResources().getColor(R.color.color_dialogItemTextNormal));
        }
        B_Item[sel].setTextColor(getResources().getColor(R.color.color_dialogItemTextPress));
    }

	private void initData() {

	}

	private void initClick() {

    }

}
