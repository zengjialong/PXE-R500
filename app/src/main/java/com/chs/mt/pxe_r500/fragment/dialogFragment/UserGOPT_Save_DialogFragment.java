package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;

import java.io.UnsupportedEncodingException;

import static android.content.Context.INPUT_METHOD_SERVICE;


@SuppressLint({"NewApi", "ClickableViewAccessibility"})
public class UserGOPT_Save_DialogFragment extends DialogFragment {
    private Toast mToast;
    private static Context mContext;

    private final int maxLen = 13;

    private Boolean userGrouptrue = true;


    private Button BtnCancel, BtnSave;
    private ImageView img_exit;
    private EditText ET_UGNane;

    private TextView txt_Title;

    private int UserGroup = 0;
    public static final String ST_UserGroup = "UserGroup";

    private OnUserGroupDialogFragmentClickListener mUserGroupListener;

    public void OnSetUserGroupDialogFragmentChangeListener(OnUserGroupDialogFragmentClickListener listener) {
        this.mUserGroupListener = listener;
    }

    public interface OnUserGroupDialogFragmentClickListener {
        void onUserGroupSaveListener(int Index, boolean UserGroupflag);
//        void onUserGroupRecallListener(int Index, boolean UserGroupflag);
//        void onUserGroupDeleteListener(int Index, boolean UserGroupflag);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (MacCfg.BOOL_DialogHideBG) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContext = getActivity().getApplicationContext();
        UserGroup = getArguments().getInt(ST_UserGroup);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().setTitle(R.string.SoundOpt);

        View view = inflater.inflate(R.layout.chs_user_gopt_save_dialog, container, false);
        initView(view);
        initData();
        initClick();
        return view;
    }

    private void initView(View V_UserGSel_Dialog) {
        BtnCancel = (Button) V_UserGSel_Dialog.findViewById(R.id.id_b_cancle);
        BtnSave = (Button) V_UserGSel_Dialog.findViewById(R.id.id_b_save);
        ET_UGNane = (EditText) V_UserGSel_Dialog.findViewById(R.id.id_et_username_edit);

        txt_Title = V_UserGSel_Dialog.findViewById(R.id.id_text_title);

        img_exit=(ImageView) V_UserGSel_Dialog.findViewById(R.id.id_btn_exit);
        ET_UGNane.setFilters(new InputFilter[]{filter});

        if (checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup])) {
            ET_UGNane.setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup]));
        } else {
            ET_UGNane.setText("");
        }
    }


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;

            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }

            int sindex = 0;
            while (count <= maxLen && sindex < source.length()) {
                char c = source.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                sindex--;
            }

            return source.subSequence(0, sindex);
        }


    };


    private void initData() {

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

    private void initClick() {
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckGroupName(0)) {
                    SetUserGroupName(UserGroup);

                    if (userGrouptrue == true) {
                        if (mUserGroupListener != null) {
                            mUserGroupListener.onUserGroupSaveListener(0, true);
                        }
                        getDialog().cancel();
                    }else{
                        txt_Title.setText((getResources().getString(R.string.sameName_Tis)));

                    }
                } else {
                    ToastMsg(getResources().getString(R.string.SetGroupName));
                }
            }
        });
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

    }
    //////////////////////////////////////////////

    /**
     * 消息提示
     */
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 检测用户输入的名字，check 0:主界面，1：保存对话框
     */
    boolean CheckGroupName(int check) {
        boolean b_ret = false;
        String gname = "";
        if (check == 0) {
            gname = String.valueOf(ET_UGNane.getText());
        } else if (check == 1) {
            //gname=String.valueOf(ET_StoreSel.getText());
        }

        if (gname.length() >= 1) {
            for (int i = 0; i < gname.length(); i++) {
                if (gname.charAt(i) >= 0x21) {
                    b_ret = true;
                }
            }
        } else {
            b_ret = false;
        }
        return b_ret;
    }

    private String getGBKString(int[] nameC) {
        byte[] GBK = new byte[16];
        for (int j = 0; j < 16; j++) {
            GBK[j] = 0x00;
        }
        int n = 0;
        String uNameString = null;
        for (int j = 0; j < 13; j++) {
            if (nameC[j] != 0x00) {
                GBK[j] = (byte) nameC[j];
                ++n;
            }
        }
        try {
            byte[] GBKN = new byte[n];
            for (int j = 0; j < n; j++) {
                GBKN[j] = GBK[j];
            }
            uNameString = new String(GBKN, "GBK");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return uNameString;
    }

    private boolean checkUserGroupByteNull(int[] ug) {

        for (int i = 0; i < 15; i++) {
            if (ug[i] != 0x00) {
                return true;
            }
        }
        return false;
    }

    void ShowEditGroupName() {
        if (checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup])) {
            ET_UGNane.setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup]));
        } else {
            ET_UGNane.setText("");
        }
    }

    void SetUserGroupName(int UserID) {
        String gname = "";
        for (int j = 1; j <=Define.MAX_UI_GROUP; j++) {
            if (getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[j]).equals(String.valueOf(ET_UGNane.getText()))
                    &&!getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup]).equals(String.valueOf(ET_UGNane.getText()))) {
                userGrouptrue = false;
                return;
            }else{
                userGrouptrue = true;
            }
        }

        gname = String.valueOf(ET_UGNane.getText());
        byte[] strGBK = new byte[16];
        try {
            strGBK = gname.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("BUG gname="+gname);


        byte [] minestrGBK = gname.getBytes();
        char M='\0';
        for (int i = 0; i < DataStruct.RcvDeviceData.SYS.UserGroup[UserID].length; i++) {
            DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] = M;
            DataStruct.RcvDeviceData.SYS.UserGroup[0][i] =M;
        }
        int bn = 0;

        System.out.println("\n打印2："+strGBK.length);

//        if (strGBK.length > DataStruct.MAX_Name_Size) {
//            for (int i = 0; i < DataStruct.MAX_Name_Size; i++) {
//                DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] = (char) strGBK[i];
//                if ((strGBK[i] & 0xFF) >= (0xa0)) {
//                    ++bn;
//                }
//            }
//            if (((bn % 2) == 1) && ((DataStruct.RcvDeviceData.SYS.UserGroup[UserID][12]) >= (0xa0))) {
//                DataStruct.RcvDeviceData.SYS.UserGroup[UserID][12] = 0x00;
//                //DataStruct.RcvDeviceData.SYS.UserGroup[0][12] = 0x00;
//            }
//        } else {



            String str2=new String(minestrGBK);


            for (int i = 0; i < strGBK.length; i++) {


                DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] =  (char)strGBK[i];


                DataStruct.RcvDeviceData.SYS.UserGroup[0][i] =   (char)strGBK[i];
                System.out.println("BUG 打印2："+ String.valueOf(DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] ));
            }
        //}
    }


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        hideInput();
//		InputMethodManager imm = (InputMethodManager)
//				getActivity().getSystemService(INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        if(mSetOnClickDialogListener != null){
//            mSetOnClickDialogListener.onClickDialogListener(0, false);
//        }
    }
}
