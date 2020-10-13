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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.operation.DataOptUtil;

@SuppressLint({"NewApi", "ClickableViewAccessibility"})
public class LinkDataCoypLeftRight_DialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;
    private Button[] B_Item = new Button[4];

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    private Context mContext;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_HP = 0;
    public   boolean DataOPT_LP = true;
    public ImageView imageView_left, imageView_right; //点击延时的左右问题

    private SetOnClickDialogListener mSetOnClickDialogListener;

    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    public interface SetOnClickDialogListener {
        void onClickDialogListener(int type, boolean boolClick);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContext=getActivity().getApplicationContext();
//        data = getArguments().getInt(ST_Data);
//        DataOPT = getArguments().getInt(ST_DataOPT);


        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_link_copy_leftright, container, false);
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



        B_Item[0] = (Button) view.findViewById(R.id.id_chs_dialog_btn0);
        B_Item[1] = (Button) view.findViewById(R.id.id_chs_dialog_btn1);
        B_Item[2] = (Button) view.findViewById(R.id.id_btn_Jump);
        B_Item[3] = (Button) view.findViewById(R.id.id_btn_Sure);


        if(!DataOptUtil.isZh(mContext)){
            for (int i = 0; i <B_Item.length ; i++) {
                B_Item[i].setTextSize(12);
            }
        }else{
            for (int i = 0; i <B_Item.length ; i++) {
                B_Item[i].setTextSize(15);
            }
        }


        imageView_left = (ImageView) view.findViewById(R.id.id_rb_high);
        imageView_right = (ImageView) view.findViewById(R.id.id_rb_left_right);
//        if(data == 1){
//            Msg.setText(getString(R.string.FrontCoupling));
//            B_Item[0].setText(R.string.LinkBaseCh1);
//            B_Item[1].setText(R.string.LinkBaseCh2);
//        }else if(data == 2){
//            Msg.setText(getString(R.string.RearCoupling));
//            B_Item[0].setText(R.string.LinkBaseCh3);
//            B_Item[1].setText(R.string.LinkBaseCh4);
//        }else if(data == 3){
//            Msg.setText(getString(R.string.SubCoupling));
//            B_Item[0].setText(R.string.LinkBaseCh5D);
//            B_Item[1].setText(R.string.LinkBaseCh5D);
//        }

//        B_Item[0].setTextColor(getResources().getColor(R.color.color_dialogItemTextPress));
//        B_Item[2].setTextColor(getResources().getColor(R.color.color_dialogItemTextCancle));

        imageView_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DataOPT_LP = true;
                setLink(DataOPT_LP);
            }
        });
        imageView_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DataOPT_LP = false;
                setLink(DataOPT_LP);
            }
        });


        for (int i = 0; i < B_Item.length; i++) {
            B_Item[i].setTag(i);
            B_Item[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = (Integer) v.getTag();
                    switch (newVal) {
//                        case 0:
////                            if (mSetOnClickDialogListener != null) {
////                                mSetOnClickDialogListener.onClickDialogListener(newVal, true);
////                            }
//                            DataOPT_LP = true;
//                            setLink(DataOPT_LP);
//                            break;
//                        case 1:
////                            if (mSetOnClickDialogListener != null) {
////                                mSetOnClickDialogListener.onClickDialogListener(newVal, false);
////                            }
//                            DataOPT_LP = false;
//                            setLink(DataOPT_LP);
//                            break;
                        case 2:
                            getDialog().cancel();
                            break;

                        case 3:
                            if (mSetOnClickDialogListener != null) {
                                mSetOnClickDialogListener.onClickDialogListener(newVal, DataOPT_LP);
                            }
                            getDialog().cancel();
                            break;
                    }

                }
            });
        }
    }

    private void setLink(boolean flag){
        if(flag){
            imageView_left.setImageDrawable(getResources().getDrawable(R.drawable.chs_input_inputsource_press));
            imageView_right.setImageDrawable(getResources().getDrawable(R.drawable.chs_input_inputsource_normal));
        }else{
            imageView_left.setImageDrawable(getResources().getDrawable(R.drawable.chs_input_inputsource_normal));
            imageView_right.setImageDrawable(getResources().getDrawable(R.drawable.chs_input_inputsource_press));
        }
    }

    private void initData() {
        setLink(DataOPT_LP);
    }

    private void initClick() {

    }


}
