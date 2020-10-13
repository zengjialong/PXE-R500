package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.tools.QNumberPicker;

import java.lang.reflect.Field;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class OtcDialogFragment extends DialogFragment {

    private QNumberPicker mPicker;
    private Button Exit;
    private TextView Msg;

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_HP   = 0;
    public static final int DataOPT_LP = 1;


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
        View view = inflater.inflate(R.layout.chs_dialog_filter, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View view) {
        mPicker = (QNumberPicker) view.findViewById(R.id.id_NumberPicker);

        if(DataStruct.CurMacMode.XOver.Level.group == 1){
            String[] dataFill = new String[DataStruct.CurMacMode.XOver.Level.max1];
            for(int i=0;i<DataStruct.CurMacMode.XOver.Level.max1;i++){
                dataFill[i]=
                        DataStruct.CurMacMode.XOver.Level.memberName1[i];
            }
            if(data > dataFill.length-1){
                data = 0;
            }

            mPicker.setDisplayedValues(dataFill);
            mPicker.setMinValue(0);
            mPicker.setMaxValue(dataFill.length - 1);
        }else{
            if(MacCfg.OutputChannelSel <= DataStruct.CurMacMode.XOver.Level.end1){
                String[] dataFill = new String[DataStruct.CurMacMode.XOver.Level.max1];
                for(int i=0;i<DataStruct.CurMacMode.XOver.Level.max1;i++){
                    dataFill[i]=
                            DataStruct.CurMacMode.XOver.Level.memberName1[i];
                }
                if(data > dataFill.length-1){
                    data = 0;
                }

                mPicker.setDisplayedValues(dataFill);
                mPicker.setMinValue(0);
                mPicker.setMaxValue(dataFill.length - 1);
            }else {
                String[] dataFill = new String[DataStruct.CurMacMode.XOver.Level.max2];
                for(int i=0;i<DataStruct.CurMacMode.XOver.Level.max2;i++){
                    dataFill[i]=
                            DataStruct.CurMacMode.XOver.Level.memberName2[i];
                }
                if(data > dataFill.length-1){
                    data = 0;
                }

                mPicker.setDisplayedValues(dataFill);
                mPicker.setMinValue(0);
                mPicker.setMaxValue(dataFill.length - 1);
            }
        }




        mPicker.setValue(data);
        setNumberPickerDividerColor(mPicker);

        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (mSetOnClickDialogListener != null) {
                    mSetOnClickDialogListener.onClickDialogListener(newVal, true);
                }
            }
        });


        Exit = (Button) view.findViewById(R.id.id_chs_dialog_exit);
        Exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        Msg = (TextView) view.findViewById(R.id.id_text_msg);

	}

	private void initData() {

	}

	private void initClick() {

    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.transparent)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
