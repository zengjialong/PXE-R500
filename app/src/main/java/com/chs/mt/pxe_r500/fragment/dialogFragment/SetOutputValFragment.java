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
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.LongCickButton.LongTouchListener;
import com.chs.mt.pxe_r500.tools.MHS_Num_SeekBar;
import com.chs.mt.pxe_r500.tools.MHS_Num_SeekBar.OnMHS_NumSeekBarChangeListener;


@SuppressLint("NewApi")
public class SetOutputValFragment extends DialogFragment {
	private static volatile SetOutputValFragment dialog = null;
	private Button ValDialogButton;
	private LongCickButton ValDialogB_SUB,ValDialogB_INC;
	private MHS_Num_SeekBar ValDialogSeekBar;
	private TextView Msg;
	/*长按按键的操作：0-减操作，1-加操作*/
	private int SYNC_INCSUB=0;
	/*按键长按：true-长按，false-非长按*/
	private boolean B_LongPress=false;
	
	private int DataVal = 0;
	private int DataMax = 0;
	public static final String ST_DataVal = "DataVal";
	public static final String ST_DataMax = "DataMax";

	private OnValDialogFragmentChangeListener mValListener;
    public void OnSetValDialogFragmentChangeListener(OnValDialogFragmentChangeListener listener) {
        this.mValListener = listener;
    }

    public interface OnValDialogFragmentChangeListener{
        void onValSeekBarListener(int Val, int type, boolean flag);
    }

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {
		DataVal = getArguments().getInt(ST_DataVal);
		DataMax = getArguments().getInt(ST_DataMax);


        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);            
        View view = inflater.inflate(R.layout.chs_set_outputval_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_ValSeekBarDialog) {  	
		
		
    	ValDialogB_INC = (LongCickButton)V_ValSeekBarDialog.findViewById(R.id.id_b_freq_dialog_inc);
    	ValDialogB_SUB = (LongCickButton)V_ValSeekBarDialog.findViewById(R.id.id_b_freq_dialog_sub);
    	ValDialogButton = (Button)V_ValSeekBarDialog.findViewById(R.id.id_chs_dialog_exit);
    	ValDialogSeekBar = (MHS_Num_SeekBar)V_ValSeekBarDialog.findViewById(R.id.id_freq_dialog_seekbar);
    	
    	//ValDialogSeekBar.setTextColor(Color.WHITE);
    	ValDialogSeekBar.setMax(DataMax);
    	ValDialogSeekBar.SetSeekbarMin(0);
    	ValDialogSeekBar.setProgress(DataVal);

    	if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].mute==0){
			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_background_color));
		}


        Msg = (TextView) V_ValSeekBarDialog.findViewById(R.id.id_text_msg);
		Msg.setText(DataStruct.CurMacMode.Out.Name[MacCfg.OutputChannelSel]);
	}

	private void initData() {
		
	}


	private void FlashUI(){
		if(DataVal==0){
			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_background_color));
		}else{
			ValDialogSeekBar.setProgressFrontColor(getResources().getColor(R.color.delay_seekbar_progress_color));
		}
	}

	private void initClick() {
		ValDialogButton.setOnClickListener(new OnClickListener() {   		
			@Override
			public void onClick(View view) {
				getDialog().cancel();
			}
		});

    	ValDialogSeekBar.setOnMHS_NumSeekBarChangeListener(new OnMHS_NumSeekBarChangeListener() {			
			@Override
			public void onProgressChanged(MHS_Num_SeekBar mhs_SeekBar, int progress,
					boolean fromUser) {
				DataVal = progress;
				if(mValListener != null){
					mValListener.onValSeekBarListener(progress, 0, fromUser);
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
			if(++DataVal > DataMax){
				DataVal = DataMax;
			}
		}else{
			if(--DataVal < 0){
				DataVal = 0;
			}
		}
		ValDialogSeekBar.setProgress(DataVal);
		
		if(mValListener != null){
			mValListener.onValSeekBarListener(DataVal, 0, false);
        }

		FlashUI();
	}

}
