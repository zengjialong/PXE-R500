package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.LongCickButton.LongTouchListener;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;


@SuppressLint("NewApi")
public class SetoffDelayTimeFragment extends DialogFragment {
	private static volatile SetoffDelayTimeFragment dialog = null;
	private ImageView ValDialogButton;
	private LongCickButton ValDialogB_SUB,ValDialogB_INC;
	private MHS_SeekBar ValDialogSeekBar;
	private TextView Msg;
	/*长按按键的操作：0-减操作，1-加操作*/
	private int SYNC_INCSUB=0;
	/*按键长按：true-长按，false-非长按*/
	private boolean B_LongPress=false;
	
	private int DataVal = 0;
	private int DataMax = 255;
	public static final String ST_DataVal = "DataVal";
	public static final String ST_DataMax = "DataMax";


	private TextView btn_off_delay_time;
	private Button btn_off_delay_time_sure;

	private SetoffDelayTimeFragment.SetOnClickDialogListener mSetOnClickDialogListener;
	public void OnSetOnClickDialogListener(SetoffDelayTimeFragment.SetOnClickDialogListener listener) {
		this.mSetOnClickDialogListener = listener;
	}
	public interface SetOnClickDialogListener{
		void onClickDialogListener(int type, boolean boolClick);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		DataVal = getArguments().getInt(ST_DataVal);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_off_delay_time, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_ValSeekBarDialog) {

    	ValDialogButton = (ImageView)V_ValSeekBarDialog.findViewById(R.id.id_btn_exit);
    	ValDialogSeekBar = (MHS_SeekBar)V_ValSeekBarDialog.findViewById(R.id.id_know_output_delay_seekbar);
		btn_off_delay_time=(TextView) V_ValSeekBarDialog.findViewById(R.id.id_txt_output_off_delay_time);
		ValDialogB_SUB=(LongCickButton) V_ValSeekBarDialog.findViewById(R.id.id_btn_output_delay_sub);
		ValDialogB_INC=(LongCickButton) V_ValSeekBarDialog.findViewById(R.id.id_btn_output_delay_inc);

		btn_off_delay_time_sure=V_ValSeekBarDialog.findViewById(R.id.id_btn_time_delay_sure);
    	ValDialogSeekBar.setProgress(DataVal);
		ValDialogSeekBar.setProgressMax(MacCfg.delay_off_time);
	}
	private void initData() {
		FlashUI();
	}

	private void setDelayNum(){
		btn_off_delay_time.setText(String.valueOf(DataVal)+getResources().getString(R.string.second));
	}

	private void FlashUI(){
		DataVal=DataStruct.RcvDeviceData.SYS.offTime;
//		if(DataVal==0){
//			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_background_color));
//		}else{
//			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_progress_color));
//		}

		ValDialogSeekBar.setProgress(DataVal);
		setDelayNum();
	}


	private void initClick() {
		ValDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(DataVal, false);
				}
				getDialog().cancel();
			}
		});

    	ValDialogSeekBar.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
			@Override
			public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
				DataVal = progress;
				setDelayNum();
				DataStruct.RcvDeviceData.SYS.offTime = DataVal;
			}
		});
		btn_off_delay_time_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(mSetOnClickDialogListener!=null){
					mSetOnClickDialogListener.onClickDialogListener(DataVal, true);
				}
				getDialog().cancel();
			}
		});
    	/*对话框-选择*/
    	ValDialogB_SUB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SYNC_INCSUB=0;
				Val_INC_SUB(false);
			}
		});
    	ValDialogB_SUB.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				ValDialogB_SUB.setStart();
				return false;
			}
		});
    	ValDialogB_SUB.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				SYNC_INCSUB=0;
				Val_INC_SUB(false);
			}
		}, MacCfg.LongClickEventTimeMax);
		/////
    	ValDialogB_INC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SYNC_INCSUB=1;
				Val_INC_SUB(true);	
			}
		});
    	ValDialogB_INC.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				ValDialogB_INC.setStart();
				return false;
			}
		});
    	ValDialogB_INC.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				SYNC_INCSUB=1;
				Val_INC_SUB(true);
			}
		}, MacCfg.LongClickEventTimeMax);
	}
	
	private void Val_INC_SUB(boolean inc){
		if(inc){//递增
			if(++DataVal > DataMax){
				DataVal = DataMax;
			}
		}else{
			if(--DataVal < 0){
				DataVal = 0;
			}
		}
		ValDialogSeekBar.setProgress(DataVal);
		DataStruct.RcvDeviceData.SYS.offTime = DataVal;
		if(mSetOnClickDialogListener != null){
			mSetOnClickDialogListener.onClickDialogListener(DataVal, false);
        }
		setDelayNum();
	}

}
