package com.chs.mt.pxe_r500.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;

import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.tools.MyScrollView;

import java.util.ArrayList;
import java.util.List;

import static com.chs.mt.pxe_r500.datastruct.MacCfg.OutputChannelSel;


public class Output_or_InputSpkTypeActivity extends DialogFragment implements MyScrollView.ScrollViewListener {
    private TextView title0, title1, title2, title3, title4, title, title5;
    private long topDistance0, topDistance1, topDistance2, topDistance3, topDistance4, topDistance5, height;
    private Button btn_exit;
    private MyScrollView scrollView;
    private int distance;
    private int DataOP = 2;

    //

    private TextView text_exit;

    private int MAXINPUT = 16;

    private TextView[] txt_output_spk = new TextView[32];

    private Output_or_InputSpkTypeActivity.SetOnClickDialogListener mSetOnClickDialogListener;

    public void OnSetOnClickDialogListener(Output_or_InputSpkTypeActivity.SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    public interface SetOnClickDialogListener {
        void onClickDialogListener(int type, boolean boolClick);
    }


    private List<Integer> aList = new ArrayList<>();

    private Context mcontet;
    private Boolean boolSetTw = true;


    @Override
    public void onStart() {
        super.onStart();
//        if(MacCfg.BOOL_DialogHideBG){
//            Window window = getDialog().getWindow();
//            WindowManager.LayoutParams windowParams = window.getAttributes();
//            windowParams.dimAmount = 0.0f;
//            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            window.setAttributes(windowParams);
//        }

//        Window window = getDialog().getWindow();
//      //  window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;
//        window.setAttributes(windowParams);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.72), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.chs_dialog_output_input_spk_type);
        View view = inflater.inflate(R.layout.chs_dialog_output_input_spk_type, container,false);
        mcontet = getActivity();

        initView(view);
        initClick();
        FlashPageUI();
        return view;

    }

    /*刷新界面*/
    private void FlashPageUI() {

        setOutput();
        setOutputNumberOverTwo();
        showOuputCanClick();
        text_exit.setText("CH-"+ String.valueOf(MacCfg.OutputChannelSel + 1)  +" "+ getResources().getString(R.string.output_title_type));

        //getResources().getString(R.string.settings)  +

    }


    /**
     * 设置输出不可点击
     */
    private void showOuputCanClick() {
        for (int i = 0; i < txt_output_spk.length; i++) {
            for (int j = 0; j < aList.size(); j++) {
                if (DataOptUtil.GetChannelName(aList.get(j), mcontet).equals(String.valueOf(txt_output_spk[i].getText()))

                ) {
                    txt_output_spk[i].setEnabled(false);
                    txt_output_spk[i].setTextColor(getResources().getColor(R.color.output_type_color));
                    //  txt_output_spk[i].setBackground(getResources().getDrawable(R.drawable.out_spk_text_press));
                    break;
                } else {
                    txt_output_spk[i].setEnabled(true);
                    //txt_output_spk[i].setBackground(getResources().getDrawable(R.color.nullc));
                    txt_output_spk[i].setTextColor(getResources().getColor(R.color.white));

                }
            }
        }
    }

    private void setOutput() {

    }

    private void setInput() {

    }


    /**
     * 设置当前通道数大于二的时候不可以选该通道(输出)
     */
    private void setOutputNumberOverTwo() {
        aList.clear();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            int count = 0;
            for (int j = DataStruct.CurMacMode.Out.OUT_CH_MAX_USE - 1; j >= 0; j--) { //循环后续所有元素
                //如果相等，则重复
                if (DataOptUtil.GetChannelNum(i) == DataOptUtil.GetChannelNum(j)
                        && DataOptUtil.GetChannelNum(i) != 0) {
                    count++;
                    if (count >= 1) {
                        if(j!= OutputChannelSel) {
                            aList.add(DataOptUtil.GetChannelNum(j));
                            break;
                        }
                    }
                }
            }
        }
    }


    private void initView(View view) {
        btn_exit = view.findViewById(R.id.id_exit);
        text_exit = view.findViewById(R.id.id_txt_out_name_msg);
        title0 = (TextView) view.findViewById(R.id.title0);
        title1 = (TextView) view.findViewById(R.id.title1);
        title2 = (TextView) view.findViewById(R.id.title2);
        title3 = (TextView) view.findViewById(R.id.title3);
        title4 = (TextView) view.findViewById(R.id.title4);
        title5 = (TextView) view.findViewById(R.id.title5);
        title = (TextView) view.findViewById(R.id.id_title);
        scrollView = (MyScrollView) view.findViewById(R.id.scv);
        scrollView.setScrollViewListener(this);

        txt_output_spk[0] = view.findViewById(R.id.id_txt_0);
        txt_output_spk[1] = view.findViewById(R.id.id_txt_1);
        txt_output_spk[2] = view.findViewById(R.id.id_txt_2);
        txt_output_spk[3] = view.findViewById(R.id.id_txt_3);
        txt_output_spk[4] = view.findViewById(R.id.id_txt_4);
        txt_output_spk[5] = view.findViewById(R.id.id_txt_5);
        txt_output_spk[6] = view.findViewById(R.id.id_txt_6);
        txt_output_spk[7] = view.findViewById(R.id.id_txt_7);
        txt_output_spk[8] = view.findViewById(R.id.id_txt_8);
        txt_output_spk[9] = view.findViewById(R.id.id_txt_9);
        txt_output_spk[10] = view.findViewById(R.id.id_txt_10);
        txt_output_spk[11] = view.findViewById(R.id.id_txt_11);
        txt_output_spk[12] = view.findViewById(R.id.id_txt_12);
        txt_output_spk[13] = view.findViewById(R.id.id_txt_13);
        txt_output_spk[14] = view.findViewById(R.id.id_txt_14);
        txt_output_spk[15] = view.findViewById(R.id.id_txt_15);
        txt_output_spk[16] = view.findViewById(R.id.id_txt_16);
        txt_output_spk[17] = view.findViewById(R.id.id_txt_17);
        txt_output_spk[18] = view.findViewById(R.id.id_txt_18);
        txt_output_spk[19] = view.findViewById(R.id.id_txt_19);
        txt_output_spk[20] = view.findViewById(R.id.id_txt_20);
        txt_output_spk[21] = view.findViewById(R.id.id_txt_21);
        txt_output_spk[22] = view.findViewById(R.id.id_txt_22);
        txt_output_spk[23] = view.findViewById(R.id.id_txt_23);
        txt_output_spk[24] = view.findViewById(R.id.id_txt_24);
        txt_output_spk[25] = view.findViewById(R.id.id_txt_25);
        txt_output_spk[26] = view.findViewById(R.id.id_txt_26);
        txt_output_spk[27] = view.findViewById(R.id.id_txt_27);
        txt_output_spk[28] = view.findViewById(R.id.id_txt_28);
        txt_output_spk[29] = view.findViewById(R.id.id_txt_29);
        txt_output_spk[30] = view.findViewById(R.id.id_txt_30);
        txt_output_spk[31] = view.findViewById(R.id.id_txt_31);
        for (int i = 0; i < txt_output_spk.length; i++) {
            txt_output_spk[i].setTag(i);
        }
    }

    private void initClick() {
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSetOnClickDialogListener != null) {
                    mSetOnClickDialogListener.onClickDialogListener(1, false);
                    getDialog().cancel();
                }
              getDialog().cancel();
            }
        });

        for (int i = 0; i < txt_output_spk.length; i++) {
            txt_output_spk[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int outputspk = (int) v.getTag();
                    DataOptUtil.setOutputSpk(outputspk,MacCfg.OutputChannelSel); //设置每个通道的类型
                    DataOptUtil.SetInputSourceMixerVol(MacCfg.OutputChannelSel);
                    DataOptUtil.GetChannelNum(OutputChannelSel);
                    DataOptUtil.SetOutputVolume(OutputChannelSel);                   //设置音量值
                    DataOptUtil.setXOverWithOutputSPKType(MacCfg.OutputChannelSel);  //设置选择通道类型后的斜率值
                    DataOptUtil.setOutputType(MacCfg.OutputChannelSel,DataOptUtil.GetChannelNum(MacCfg.OutputChannelSel));
                    if (mSetOnClickDialogListener != null) {
                        mSetOnClickDialogListener.onClickDialogListener(1, true);
                    }
                    DataOptUtil.syncLinkData(Define.UI_OutVal);

                    getDialog().cancel();

                }
            });
        }
    }



//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        // System.out.println("BUG BAcitivy中执行该方法" + hasFocus);
//        super.getActivity().onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//         ;
//
//        }
//    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {

        topDistance5 = title5.getTop();
        topDistance0 = title0.getTop();
        topDistance1 = title1.getTop();
        topDistance2 = title2.getTop();
        topDistance3 = title3.getTop();//按钮左上角相对于父view（LinerLayout）的y坐标
        topDistance4 = title4.getTop();//按钮左上角相对于父view（LinerLayout）的y坐标
        height = title.getMeasuredHeight();


        distance = y;
        distancePlace(topDistance5, topDistance0);
        distancePlaceLayout(topDistance0, topDistance5);

        distancePlace(topDistance0, topDistance1);
        distancePlaceLayout(topDistance1, topDistance0);


        distancePlace(topDistance1, topDistance2);
        distancePlaceLayout(topDistance2, topDistance1);

        distancePlace(topDistance2, topDistance3);
        distancePlaceLayout(topDistance3, topDistance2);

        distancePlace(topDistance3, topDistance4);
        distancePlaceLayout(topDistance4, topDistance3);

        if (distance > topDistance4) {
            title.layout(0, 0, 0, (int) height);
            showText(topDistance4);
        }
    }

    private void distancePlaceLayout(long topDistance1, long topDistance0) {

        //方法的四个参数 l ,t ,r ,b分别是当前View从左，上，右，下相对于其父容器的距离
        if (distance > topDistance1 - height && distance < topDistance1) {
            title.layout(0, (int) (topDistance1 - distance - height), 0, (int) (topDistance1 - distance));
            showText(topDistance0);
        }
    }

    private void distancePlace(long topDistance0, long topDistance1) {
        if (distance >= topDistance0 && distance < topDistance1 - height) {
            title.layout(0, 0, title.getRight(), (int) height);
            showText(topDistance0);
        }
    }

    private void showText(long topDistance) {

        if (topDistance == topDistance5) {
            title.setText(getResources().getString(R.string.NULL));
        }
        if (topDistance == topDistance0) {
            title.setText(getResources().getString(R.string.setpreposition));
        }
        if (topDistance == topDistance1) {
            title.setText(getResources().getString(R.string.setpostposition));
        }
        if (topDistance == topDistance2) {
            title.setText(getResources().getString(R.string.Center));
        }
        if (topDistance == topDistance3) {
            title.setText(getResources().getString(R.string.Sub));

        }
        if (topDistance == topDistance4) {
            title.setText(getResources().getString(R.string.setrearout));
        }
    }
}
