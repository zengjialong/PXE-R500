package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;

@SuppressLint({"NewApi", "ClickableViewAccessibility"})
public class InputSourceDialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;
    private Button[] B_Item = new Button[6];
    private LinearLayout LY_BG;

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;

    private int DataType = 1;//判断 传入进来的音源是混音还是主音源

    private int txt_decrement = 0;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final String ST_DataOPType = "ST_DataOPType";

    public static final int DataOPT_INS = 0;
    public static final int DataOPT_MIXER = 1;
    public static final String decrement_number = "decrement_number";
    private Button btn_exit;
    private Button rabtn_Optical, rabtn_Coaxial, rabtn_bluetooth, rabtn_high, rabtn_aux, rabtn_off;
    private Button btn_jump, btn_sure;
    private RadioGroup radioGroup;
    private Button btn_decrement;
    private Button[] rb_inputsource = new Button[4];
    private LinearLayout[] ly_inputsource=new LinearLayout[5];
    private LinearLayout ly_attenuation;
    private View layoutLeft;
    // 左中右三个弹出窗口
    private PopupWindow popLeft;
    private TextView txt_title;

    private TextView[] textViews = new TextView[5];
    private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    public interface SetOnClickDialogListener {
        void onClickDialogListener(int type, boolean boolClick,int DataType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DataType = getArguments().getInt(ST_DataOPT);

        DataOPT = getArguments().getInt(ST_DataOPType);
        setCurrentData();
        getDialog().setCancelable(false);
        //txt_decrement = getArguments().getInt(decrement_number);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_inputsource, container, false);
        initView(view);
        flashPageUi();
        return view;
    }

    private void initView(View view) {
        btn_exit = view.findViewById(R.id.id_inputsource_exit);

        txt_title=view.findViewById(R.id.id_txt_title);
        rb_inputsource[0] = view.findViewById(R.id.id_rb_high);
        rb_inputsource[1] = view.findViewById(R.id.id_rb_aux);
        rb_inputsource[2] = view.findViewById(R.id.id_rb_bluetooth);
        rb_inputsource[3] = view.findViewById(R.id.id_rb_Optical);


        ly_attenuation=view.findViewById(R.id. id_ly_attenuation);
        btn_decrement = view.findViewById(R.id.id_btn_decrement);
        ly_inputsource[0]= view.findViewById(R.id.id_ly_iputsource_high);
        ly_inputsource[1]= view.findViewById(R.id.id_ly_iputsource_aux);
        ly_inputsource[2]= view.findViewById(R.id.id_ly_iputsource_bluetooth);
        ly_inputsource[3]= view.findViewById(R.id.id_ly_iputsource_optical);
        View view1= view.findViewById(R.id.id_line_3);

        if(DataType==DataOPT_INS){
            ly_inputsource[3].setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            ly_attenuation.setVisibility(View.GONE);
            txt_title.setText(getResources().getString(R.string.Main_inputsource_select));
        }else{
            ly_inputsource[3].setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            ly_attenuation.setVisibility(View.VISIBLE);
            txt_title.setText(getResources().getString(R.string.source_superposition_title));
        }


        btn_jump = view.findViewById(R.id.id_btn_Jump);
        btn_sure = view.findViewById(R.id.id_btn_Sure);
        initViewClicklisten();
    }

    private void flashPageUi() {
        for (int i = 0; i < rb_inputsource.length; i++) {
            rb_inputsource[i].setBackgroundResource(R.drawable.chs_input_inputsource_normal);
        }
       // setCurrentData();

        rb_inputsource[DataOPT].setBackgroundResource(R.drawable.chs_input_inputsource_press);


        setshowVal();

    }


    /**
     * 设置按钮显示的值
     */
    private void setshowVal() {
        if (DataStruct.RcvDeviceData.SYS.AutoSourcedB == 70) {
            btn_decrement.setText(String.valueOf("30%"));
        } else if (DataStruct.RcvDeviceData.SYS.AutoSourcedB == 50) {
            btn_decrement.setText(String.valueOf("50%"));
        } else if (DataStruct.RcvDeviceData.SYS.AutoSourcedB == 20) {
            btn_decrement.setText(String.valueOf("80%"));
        } else if (DataStruct.RcvDeviceData.SYS.AutoSourcedB == 0) {
            btn_decrement.setText(String.valueOf("100%"));
        } else {
            btn_decrement.setText(String.valueOf("0%"));
        }
    }



    /***设置当前需要显示的值为*/
    private void setCurrentData(){
        int index=0;
        if(DataType==DataOPT_INS){
            index=DataStruct.RcvDeviceData.SYS.input_source;
        }else{
            index=DataStruct.RcvDeviceData.SYS.aux_mode;
        }

        switch (index){
            case 1:
                DataOPT=0;
                break;
            case 3:
                DataOPT=1;
                break;
            case 2:
                DataOPT=2;
                break;
            case 255:
                DataOPT=3;
                break;
        }
    }


    private void initViewClicklisten() {
        btn_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                if (mSetOnClickDialogListener != null) {
                    mSetOnClickDialogListener.onClickDialogListener(DataOPT, false,DataType);
                    getDialog().cancel();
                }
            }
        });

        btn_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                if (mSetOnClickDialogListener != null) {
                    mSetOnClickDialogListener.onClickDialogListener(DataOPT, true,DataType);
                    getDialog().cancel();
                }
            }
        });

        btn_jump.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                if (mSetOnClickDialogListener != null) {
                    mSetOnClickDialogListener.onClickDialogListener(DataOPT, false,DataType);
                    getDialog().cancel();
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


        for (int i = 0; i < rb_inputsource.length; i++) {
            rb_inputsource[i].setTag(i);
            rb_inputsource[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataOPT= (int) v.getTag();

//                    if(DataType==DataOPT_INS){
//                        DataStruct.RcvDeviceData.SYS.input_source = DataStruct.CurMacMode.inputsource.inputsource[num];
//                    }else{
//                        DataStruct.RcvDeviceData.SYS.aux_mode = DataStruct.CurMacMode.Mixersource.inputsource[num];
//                    }
                    if (mSetOnClickDialogListener != null) {
                        mSetOnClickDialogListener.onClickDialogListener(DataOPT, false,DataType);
                    }

//                    DataStruct.SendDeviceData.SYS.input_source = DataStruct.RcvDeviceData.SYS.input_source;
//                    DataStruct.SendDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.SYS.aux_mode;
//                    DataStruct.SendDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.SYS.device_mode;
//                    DataStruct.SendDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.SYS.hi_mode;
//                    DataStruct.SendDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.SYS.blue_gain;
//                    DataStruct.SendDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.SYS.aux_gain;
//                    DataStruct.SendDeviceData.SYS.Safety = DataStruct.RcvDeviceData.SYS.Safety;
//                    DataStruct.SendDeviceData.SYS.sound_effect = DataStruct.RcvDeviceData.SYS.sound_effect;
//
//                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                    DataStruct.SendDeviceData.DeviceID = 0x01;
//                    DataStruct.SendDeviceData.UserID = 0x00;
//                    DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//                    DataStruct.SendDeviceData.ChannelID = Define.PC_SOURCE_SET;
//                    DataStruct.SendDeviceData.DataID = 0x00;
//                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//                    DataStruct.SendDeviceData.PcCustom = 0x00;
//                    DataStruct.SendDeviceData.DataLen = 8;
//                    DataStruct.U0SendFrameFlg = 1;
//                    DataOptUtil.SendDataToDevice(false);

                    flashPageUi();
                }
            });
        }

        btn_decrement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popLeft != null && popLeft.isShowing()) {
                    popLeft.dismiss();
                } else {
                    layoutLeft = LayoutInflater.from(getActivity()).inflate(
                            R.layout.pop_attenuated_menu, null);
                    textViews[0] = layoutLeft.findViewById(R.id.tv1);
                    textViews[1] = layoutLeft.findViewById(R.id.tv2);
                    textViews[2] = layoutLeft.findViewById(R.id.tv3);
                    textViews[3] = layoutLeft.findViewById(R.id.tv4);
                    textViews[4] = layoutLeft.findViewById(R.id.tv5);

                    for (int i = 0; i < textViews.length; i++) {
                        textViews[i].setTag(i);
                    }
                    // 窗口宽度跟tvLeft一样
                    popLeft = new PopupWindow(layoutLeft, btn_decrement.getWidth()  ,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    Display display = getActivity().getWindowManager().getDefaultDisplay();//得到当前屏幕的显示器对象
                    Point size = new Point();//创建一个Point点对象用来接收屏幕尺寸信息
                    display.getSize(size);//Point点对象接收当前设备屏幕尺寸信息
                    int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
                    int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
                    popLeft = new PopupWindow(layoutLeft, btn_decrement.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    popLeft.setClippingEnabled(false);
                    ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.white));
                    popLeft.setBackgroundDrawable(cd);
                    // popLeft.setAnimationStyle(R.style.PopupAnimation);
                    popLeft.update();
                    popLeft.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    popLeft.setTouchable(true); // 设置popupwindow可点击
                    popLeft.setOutsideTouchable(true); // 设置popupwindow外部可点击
                    popLeft.setFocusable(true); // 获取焦点
                    // 设置popupwindow的位置（相对tvLeft的位置）
                    int topBarHeight = btn_decrement.getBottom();
                    popLeft.showAsDropDown(btn_decrement);
                    popLeft.setTouchInterceptor(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                popLeft.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                    for (int i = 0; i < textViews.length; i++) {
                        textViews[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int num= (int)v.getTag();
                                if(num==0){
                                    DataStruct.RcvDeviceData.SYS.AutoSourcedB=100;
                                }else if(num==1){
                                    DataStruct.RcvDeviceData.SYS.AutoSourcedB=70;
                                }else if(num==2){
                                    DataStruct.RcvDeviceData.SYS.AutoSourcedB=50;
                                }else if(num==3){
                                    DataStruct.RcvDeviceData.SYS.AutoSourcedB=20;
                                }else if(num==4){
                                    DataStruct.RcvDeviceData.SYS.AutoSourcedB=0;
                                }
                                setshowVal();
                                popLeft.dismiss();
                            }
                        });
                    }
                }
            }
        });
    }


}
