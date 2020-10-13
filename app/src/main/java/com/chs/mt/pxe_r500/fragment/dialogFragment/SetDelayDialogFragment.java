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
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.operation.DataOptUtil;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.LongCickButton.LongTouchListener;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;


@SuppressLint("NewApi")
public class SetDelayDialogFragment extends DialogFragment {
	private Button ValDialogButton;
	private LongCickButton ValDialogB_SUB,ValDialogB_INC;
	private MHS_SeekBar ValDialogSeekBar;
	private TextView Msg;
	/*长按按键的操作：0-减操作，1-加操作*/
	private int SYNC_INCSUB=0;
	/*按键长按：true-长按，false-非长按*/
	private boolean B_LongPress=false;

	private int DataVal = 0;
	private int DataMax = 0;
	public static final String ST_Data = "DataVal";
	public static final String ST_DelayUnit = "DataMax";


	private TextView txt_output_delay_cm,txt_output_delay_ms,txt_output_delay_inch;

	private TextView txt_title_output_delay;

	private OnDelayDialogFragmentChangeListener mValListener;
	public void OnSetDelayDialogFragmentChangeListener(OnDelayDialogFragmentChangeListener listener) {
		this.mValListener = listener;
	}

	public interface OnDelayDialogFragmentChangeListener{
		void onDelaySeekBarListener(int Val, int type, boolean flag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		DataVal = getArguments().getInt(ST_Data);
		DataMax = getArguments().getInt(ST_DelayUnit);



		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.chs_seekbar_dialog_setdelay, container,false);
		initView(view);
		initData();
		initClick();
		return view;
	}
	private void initView(View V_ValSeekBarDialog) {
		txt_title_output_delay=V_ValSeekBarDialog.findViewById(R.id.id_txt_output_delay_val);

		ValDialogB_SUB= (LongCickButton)V_ValSeekBarDialog.findViewById(R.id.id_btn_output_delay_sub);
		ValDialogB_INC = (LongCickButton)V_ValSeekBarDialog.findViewById(R.id.id_btn_output_delay_inc);
		ValDialogButton = (Button)V_ValSeekBarDialog.findViewById(R.id.id_btn_exit);
		ValDialogSeekBar = (MHS_SeekBar)V_ValSeekBarDialog.findViewById(R.id.id_setdelay_dialog_seekbar);


		txt_output_delay_ms=(TextView)V_ValSeekBarDialog.findViewById(R.id.id_txt_output_delay_ms);
		txt_output_delay_cm=(TextView)V_ValSeekBarDialog.findViewById(R.id.id_txt_output_delay_cm);
		txt_output_delay_inch=(TextView)V_ValSeekBarDialog.findViewById(R.id.id_txt_output_delay_inch);
		ValDialogSeekBar.setProgress(DataVal);

		ValDialogSeekBar.setProgressMax(DataStruct.CurMacMode.Delay.MAX);




		Msg = (TextView) V_ValSeekBarDialog.findViewById(R.id.id_txt_output_val_name);
		//Msg.setText(DataStruct.CurMacMode.Out.Name[MacCfg.OutputChannelSel]);
	}

	private void initData() {
		FlashUI();
	}


	private void setDelayNum(){
		//if(Temp.delay_unit==1){
		txt_output_delay_ms.setText(DataOptUtil.CountDelayMs(DataVal)+" "+"ms");
		//}else if(Temp.delay_unit==2){
		txt_output_delay_cm.setText(DataOptUtil.CountDelayCM(DataVal)+" "+"cm");
		//}else{
		txt_output_delay_inch.setText(DataOptUtil.CountDelayInch(DataVal)+" "+"inch");
		//}
	}

	private void FlashUI(){
		DataVal=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay;
		if(DataVal==0){
			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_background_color));
		}else{
			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_progress_color));
		}
		ValDialogSeekBar.setProgress(DataVal);
		Msg.setText(("CH-" + String.valueOf(MacCfg.OutputChannelSel + 1)));
//				txt_title_output_delay.setText((getResources().getString(R.string.outlet))
//						+String.valueOf(MacCfg.OutputChannelSel + 1)+getResources().getString(R.string.SetDelay));
		txt_title_output_delay.setText(getResources().getString(R.string.SetDelaySetting));

		setDelayNum();
	}


	private void initClick() {
		ValDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().cancel();
			}
		});

		ValDialogSeekBar.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
			@Override
			public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
				DataVal = progress;
				if(mValListener != null){
					mValListener.onDelaySeekBarListener(progress, 0, fromUser);
				}
				FlashUI();
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
			if(++DataVal > DataStruct.CurMacMode.Delay.MAX){
				DataVal = DataStruct.CurMacMode.Delay.MAX;
			}
		}else{
			if(--DataVal < 0){
				DataVal = 0;
			}
		}
		ValDialogSeekBar.setProgress(DataVal);
		if(mValListener != null){
			mValListener.onDelaySeekBarListener(DataVal, 0, false);
		}
		FlashUI();
	}






}
