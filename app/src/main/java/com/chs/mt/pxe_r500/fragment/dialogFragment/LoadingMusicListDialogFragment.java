package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class LoadingMusicListDialogFragment extends DialogFragment {
    private static int MSG_What_Show_Val = 1;
    private static int MSG_What_Cancel   = 2;
    /*加载对话框f*/
    private Dialog LoadingDialog,Loading;
    private TextView TV_LoadShow,TV_loadMessage;
    private View V_LoadingDialog;
    private static boolean go = true;
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


        TV_loadMessage.setText(getString(R.string.Loading));
        toal = DataStruct.SendbufferList.size();
        System.out.println("BUG toal:" + toal);
        step=toal;
        checkFlag();
        new Thread(new Runnable() {

            @Override
            public void run() {

                while (go){
                    checkFlag();

                    if((!DataStruct.isConnecting)&&(!DataStruct.U0SynDataSucessFlg)){
                        Message msgc =new Message();
                        msgc.what = MSG_What_Cancel;
                        mHandler.sendMessage(msgc);
                    }

                    Message msg1 =new Message();
                    msg1.what = MSG_What_Show_Val;
                    msg1.obj = "100%";//可以是基本类型，可以是对象，可以是List、map等；
                    mHandler.sendMessage(msg1);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }
                    step = DataStruct.SendbufferList.size();
                }

                Message msgc =new Message();
                msgc.what = MSG_What_Cancel;
                mHandler.sendMessage(msgc);

            }


        }).start();

    }

    private void checkFlag(){
        if(DataStruct.BOOL_MFList_OPT == 2){//文件加载中
            if(DataStruct.CurMusic.BoolHaveUPdateFileList){
                go = false;
            }else {
                go = true;
            }

        }else{//音乐加载中
            if(DataStruct.CurMusic.BoolHaveUPdateList){
                go = false;
            }else {
                go = true;
            }
        }
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
                if(DataStruct.BOOL_MFList_OPT == 2){//文件加载中
                    TV_LoadShow.setText(String.valueOf(DataStruct.CurMusic.UpdateFileListStart-1)
                    +"/"+String.valueOf(DataStruct.CurMusic.FileTotal)
                    );
                }else{//音乐加载中
                    TV_LoadShow.setText(String.valueOf(DataStruct.CurMusic.UpdateListStart-1)
                            +"/"+String.valueOf(DataStruct.CurMusic.Total)
                    );
                }

            }else if(msg.what == MSG_What_Cancel){
                if(getDialog() != null){
                    getDialog().cancel();

                }
            }


        }

    };

}