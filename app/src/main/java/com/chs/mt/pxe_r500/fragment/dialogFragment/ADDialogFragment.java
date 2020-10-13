package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.operation.mOKhttpUtil;
import com.chs.mt.pxe_r500.updateApp.UpdateUtil;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class ADDialogFragment extends DialogFragment {
    //关于
    private Context mContext;
    private Button BtnOK;
    private SimpleDraweeView AdView;
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
        View view = inflater.inflate(R.layout.chs_dialog_ad, container,false);
        mContext = getActivity();
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View view) {
        AdView = (SimpleDraweeView)view.findViewById(R.id.my_image);
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius((mContext.getResources().getDimensionPixelOffset(R.dimen.ad_img_radius)));
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
        GenericDraweeHierarchy hierarchy = builder.build();
        hierarchy.setRoundingParams(roundingParams);
        AdView.setHierarchy(hierarchy);

        Uri uri = Uri.parse(DataStruct.mDSPAi.Ad_Image_Path);
        AdView.setImageURI(uri);
        AdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UpdateUtil.isNetworkAvalible(mContext)){
                    if(!DataStruct.mDSPAi.Ad_URL.equals("")){

//                    String stC="http://crm.chschs.com/index.php?m=Index&a=Ad_Show_Page";
                        Intent intent = new Intent();
                        Bundle bundle=new Bundle();
                        bundle.putInt("NET_PAGE_TYPE", 0);
//                    if(DataStruct.mDSPAi.Ad_URL.contains(stC)){
                        bundle.putString("URL", DataStruct.mDSPAi.Ad_URL+"&macAddr="+MacCfg.PhoneMAC);//加载文件
//                    }else {
//                        bundle.putString("URL", DataStruct.mDSPAi.Ad_URL);//加载文件
//                    }

                    }
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

        BtnOK = (Button)view.findViewById(R.id.id_b_about_ok);
        BtnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
//                if(DataStruct.mDSPAi.Ad_Close_URL.length()>0){
//                    mOKhttpUtil.getURL(DataStruct.mDSPAi.Ad_Close_URL+"&macAddr="+MacCfg.PhoneMAC);
//                }
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }
            }
        });
	}

	private void initData() {
        SharedPreferences sp = getActivity().getSharedPreferences("SP", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString("AdID", DataStruct.mDSPAi.AdID);
        editor.commit();

        if(DataStruct.mDSPAi.Ad_Close_URL.length()>0){
            mOKhttpUtil.getURL(DataStruct.mDSPAi.Ad_Close_URL+"&macAddr="+MacCfg.PhoneMAC);
        }
	}

	private void initClick() {

	}

}
