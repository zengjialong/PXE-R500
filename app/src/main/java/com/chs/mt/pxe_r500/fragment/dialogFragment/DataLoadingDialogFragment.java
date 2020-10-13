package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class DataLoadingDialogFragment extends DialogFragment {
    private static int MSG_What_Show_Val = 1;
    private static int MSG_What_Cancel   = 2;
    /*加载对话框f*/
    private Dialog LoadingDialog,Loading;
    private TextView TV_LoadShow,TV_loadMessage;
    private View V_LoadingDialog;

    int toal=0;
    int step=0;


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
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.chs_layout_loading_dialog, container,false);
        initView(view);
        initData();
        initClick();
        return view;
    }
    private void initView(View V_LoadingDialog) {
        ImageView spaceshipImage = (ImageView) V_LoadingDialog.findViewById(R.id.id_loading_dialog_img);
        TV_LoadShow = (TextView) V_LoadingDialog.findViewById(R.id.id_tv_progress);
        TV_loadMessage = (TextView) V_LoadingDialog.findViewById(R.id.id_tv_loading_message);
        Animation animation = AnimationUtils.loadAnimation(
                getActivity(), R.anim.chs_cirle); // 加载动画
        animation.setInterpolator(new LinearInterpolator());
        spaceshipImage.startAnimation(animation); // 使用ImageView显示动画
        TV_LoadShow.setVisibility(View.GONE);
        TV_loadMessage.setText(getString(R.string.Loading));

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                int cnt = 0;
                while (cnt < 10){//BOOL_GroupREC_ACK
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }
                    cnt++;
                    if(!DataStruct.isConnecting){
                        Message msgc =new Message();
                        msgc.what = MSG_What_Cancel;
                        mHandler.sendMessage(msgc);
                    }
                }

                Message msgc =new Message();
                msgc.what = MSG_What_Cancel;
                mHandler.sendMessage(msgc);

            }


        }).start();

    }

    private void initData() {

    }

    private void initClick() {

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_What_Show_Val){
                TV_LoadShow.setText(msg.obj.toString());
            }else if(msg.what == MSG_What_Cancel){
                if(getDialog() != null){
                    getDialog().cancel();

                }
            }


        }

    };

}