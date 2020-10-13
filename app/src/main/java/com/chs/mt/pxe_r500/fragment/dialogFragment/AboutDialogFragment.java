package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.operation.DataOptUtil;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class AboutDialogFragment extends BaseDialogFragment {
    //关于
    private TextView TV_MCU_Version_Menu,TV_SoftVersion_Menu,TV_CopyRight_Menu,TV_DeviceMac,TV_DeviceType;//TV_DeviceVersion
    private Button AboutSure;
    private ImageView imageView;
    private Context mcontext;


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
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().getWindow().setLayout(DeviceUtil.getDeviceWidth(), HlyUtils.dp2px(380));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mcontext=getActivity();
        View view = inflater.inflate(R.layout.chs_about_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_AboutDialog) {

        AboutSure = (Button)V_AboutDialog.findViewById(R.id.id_b_about_ok);

        TV_MCU_Version_Menu = (TextView) V_AboutDialog.findViewById(R.id.id_tv_device_version);
        TV_SoftVersion_Menu = (TextView) V_AboutDialog.findViewById(R.id.id_tv_soft_version);
        TV_CopyRight_Menu = (TextView) V_AboutDialog.findViewById(R.id.id_tv_copyright);

        TV_DeviceType = (TextView) V_AboutDialog.findViewById(R.id.id_tv_type);

        //TV_DeviceMac = (TextView) V_AboutDialog.findViewById(R.id.id_tv_device_mac);
        if(DataOptUtil.isZh(mcontext)){
            TV_SoftVersion_Menu.setText(getResources().getString(R.string.Software_version)+MacCfg.App_versions);
            TV_MCU_Version_Menu.setText(getResources().getString(R.string.device_version)+MacCfg.DeviceVerString);
        }else{
            TV_MCU_Version_Menu.setText(getResources().getString(R.string.device_version)+" "+MacCfg.DeviceVerString);
            TV_SoftVersion_Menu.setText(getResources().getString(R.string.Software_version)+" "+MacCfg.App_versions);
        }
        Time time = new Time();
        time.setToNow();
        int year=time.year;
        TV_CopyRight_Menu.setText(MacCfg.Copyright+String.valueOf(year)+MacCfg.CopyrightAll);

        TV_DeviceType.setText(String.valueOf(MacCfg.Mac));
        imageView=V_AboutDialog.findViewById(R.id.id_setdelay_dialog_button);
       // TV_DeviceMac.setText(getResources().getString(R.string.MacDevice)+ MacCfg.Mac_type);
        AboutSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }

            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
//        V_AboutDialog.setFocusableInTouchMode(true);
//        V_AboutDialog.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                return false;
//            }
//        });
	}

	private void initData() {

	}

	private void initClick() {

	}

}
