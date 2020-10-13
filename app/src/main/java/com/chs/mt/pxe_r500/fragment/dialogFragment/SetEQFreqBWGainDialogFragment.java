package com.chs.mt.pxe_r500.fragment.dialogFragment;
import com.chs.mt.pxe_r500.R;


import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.LongCickButton.LongTouchListener;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.text.DecimalFormat;
@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SetEQFreqBWGainDialogFragment extends DialogFragment {
	/* 调出设置频率的SeekBarDialog  */	
	private ImageView EQPageDialogButton;
	private LongCickButton EQPageDialogB_SUB,EQPageDialogB_INC;
	//private MHS_Num_SeekBar EQPageDialogSeekBar;
	private Button B_EQNum,EQPageDialogSure;

	private MHS_SeekBar sb_val;
	private TextView TV_Val;

	private int data = 0;
	public static final String ST_Data = "Data";
	private int DataOPT = 1;
	public static final String ST_DataOPT = "ST_DataOPT";
	private int DataNUM = 1;
	public static final String ST_DataNUM = "ST_DataNUM";
	public static final int DataOPT_BW   = 0;
	public static final int DataOPT_Freq = 1;
	public static final int DataOPT_Gain = 2;
	
	private OnEQFreqBWGainDialogFragmentChangeListener mEQFreqBWGainListener;
    public void OnSetEQFreqBWGainDialogFragmentChangeListener(OnEQFreqBWGainDialogFragmentChangeListener listener) {
        this.mEQFreqBWGainListener = listener;
    }

    public interface OnEQFreqBWGainDialogFragmentChangeListener{
        void onFreqSeekBarListener(int Freq, int type, boolean flag);
        void onBWSeekBarListener(int BW, int type, boolean flag);
        void onGainSeekBarListener(int Gain, int type, boolean flag);
    }

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        data = getArguments().getInt(ST_Data);
		DataOPT = getArguments().getInt(ST_DataOPT);
		DataNUM = getArguments().getInt(ST_DataNUM);
		
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);            
        View view = inflater.inflate(R.layout.chs_seekbar_dialog_eq_page, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_EQPageSeekBarDialog) {  	
		B_EQNum = (Button)V_EQPageSeekBarDialog.findViewById(R.id.id_tv_eq_num);
		EQPageDialogB_INC = (LongCickButton)V_EQPageSeekBarDialog.findViewById(R.id.id_b_setdelay_dialog_inc);
    	EQPageDialogB_SUB = (LongCickButton)V_EQPageSeekBarDialog.findViewById(R.id.id_b_setdelay_dialog_sub);
    	EQPageDialogButton = (ImageView)V_EQPageSeekBarDialog.findViewById(R.id.id_setdelay_dialog_button);
		EQPageDialogSure = (Button)V_EQPageSeekBarDialog.findViewById(R.id.id_setdelay_dialog_sure);
        sb_val = (MHS_SeekBar) V_EQPageSeekBarDialog.findViewById(R.id.id_mcl_seekbar_valume);
        TV_Val = (TextView) V_EQPageSeekBarDialog.findViewById(R.id.id_text);

//    	EQPageDialogSeekBar = (MHS_Num_SeekBar)V_EQPageSeekBarDialog.findViewById(R.id.id_freq_dialog_seekbar);
 
    	
    	switch (DataOPT) {
			case DataOPT_BW:
//				EQPageDialogSeekBar.setTextUnit("BW");
//		    	EQPageDialogSeekBar.setTextColor(Color.WHITE);
//		    	EQPageDialogSeekBar.setMax(Define.EQ_BW_MAX);
//		    	EQPageDialogSeekBar.SetSeekbarMin(0);
//		    	EQPageDialogSeekBar.setProgress(Define.EQ_BW_MAX-data);
//				EQPageDialogSeekBar.SetSeekbarBW(Define.EQ_BW_MAX-data);
				B_EQNum.setText(getResources().getString(R.string.SetEQ)+"(EQ"+String.valueOf(DataNUM+1)+")");

                sb_val.setProgressMax(Define.EQ_BW_MAX);
                sb_val.setProgress(Define.EQ_BW_MAX-data);
				break;
			case DataOPT_Freq:
//				EQPageDialogSeekBar.setTextUnit("Hz");
//		    	EQPageDialogSeekBar.setTextColor(Color.WHITE);
//		    	EQPageDialogSeekBar.setMax(240);
//		    	EQPageDialogSeekBar.SetSeekbarMin(0);
//				FlashEQPageDialogSeekBarProgress(data);
//				EQPageDialogSeekBar.SetSeekbarFreq(data);
//				EQPageDialogSeekBar.setProgress(GetFreqDialogSeekBarIndex(data));
				B_EQNum.setText(getResources().getString(R.string.SetFreq)+"(EQ"+String.valueOf(DataNUM+1)+")");

                sb_val.setProgressMax(240);
                sb_val.setProgress(GetFreqDialogSeekBarIndex(data));
				break;
			case DataOPT_Gain:
//				EQPageDialogSeekBar.setTextUnit("GainEQ");
//		    	EQPageDialogSeekBar.setTextColor(Color.WHITE);
//		    	EQPageDialogSeekBar.setMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);
//		    	EQPageDialogSeekBar.SetSeekbarMin(0);
//		    	//EQPageDialogSeekBar.setProgress(data-MacCfg.EQ_LEVEL_MIN);
//		    	EQPageDialogSeekBar.setProgress(data);
//				EQPageDialogSeekBar.SetSeekbarGain(data+MacCfg.EQ_LEVEL_MIN);
				B_EQNum.setText(getResources().getString(R.string.SetGain)+"(EQ"+String.valueOf(DataNUM+1)+")");

                sb_val.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);
                sb_val.setProgress(data);
				break;
			default:
				break;
		}
	}
	
    void FlashEQPageDialogSeekBarProgress(int fP){
    	int i;
    	
    	for(i=0;i<240;i++){
    		if((fP>=Define.FREQ241[i])&&(fP<=Define.FREQ241[i+1])){
//    			EQPageDialogSeekBar.setProgress(i+1);
                sb_val.setProgress(i+1);
    			return;
    		}
    	}
    }

	/*
	 * 根据频率值更新
	 */
	int GetFreqDialogSeekBarIndex(int fP){
    	int i;
    	int index=0;
    	for(i=0;i<240;i++){
    		if((fP>=Define.FREQ241[i])&&(fP<=Define.FREQ241[i+1])){
    			index=i+1;
    		}
    	}
    	return index;
    }
	private void initData() {
		FlashVal(data);
	}

	private void initClick() {
		EQPageDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		EQPageDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				System.out.println("BUG 点击了呀");
				getDialog().cancel();
			}
		});

		EQPageDialogSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});

		sb_val.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
			@Override
			public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
				switch (DataOPT) {
					case DataOPT_BW:
						data = Define.EQ_BW_MAX- progress;
						if(mEQFreqBWGainListener != null){
							mEQFreqBWGainListener.onBWSeekBarListener(data, 0, fromUser);
						}
						break;
					case DataOPT_Freq:
						data = (int) Define.FREQ241[progress];
						if(mEQFreqBWGainListener != null){
							mEQFreqBWGainListener.onFreqSeekBarListener(data, 0, fromUser);
						}
						break;
					case DataOPT_Gain:
						data = progress;
						if(mEQFreqBWGainListener != null){
							//System.out.println("FUCK onProgressChanged data:"+data);
							mEQFreqBWGainListener.onGainSeekBarListener(data, 0, fromUser);
						}
						break;
					default:
						break;
				}
				FlashVal(data);
			}
		});

        /*
		EQPageDialogSeekBar.setOnMHS_NumSeekBarChangeListener(new OnMHS_NumSeekBarChangeListener() {			
			@Override
			public void onProgressChanged(MHS_Num_SeekBar mhs_SeekBar, int progress,
					boolean fromUser) {
				//System.out.println("FUCK onProgressChanged chs_progress:"+chs_progress);
				switch (DataOPT) {
					case DataOPT_BW:
						data = Define.EQ_BW_MAX- progress;
						if(mEQFreqBWGainListener != null){
							mEQFreqBWGainListener.onBWSeekBarListener(data, 0, fromUser);
				        }
						break;
					case DataOPT_Freq:
						data = (int) Define.FREQ241[progress];
						if(mEQFreqBWGainListener != null){
							mEQFreqBWGainListener.onFreqSeekBarListener(data, 0, fromUser);
				        }
						break;
					case DataOPT_Gain:
						data = progress;
						if(mEQFreqBWGainListener != null){
							//System.out.println("FUCK onProgressChanged data:"+data);
							mEQFreqBWGainListener.onGainSeekBarListener(data, 0, fromUser);
				        }	
						break;
					default:
						break;
				}
			}
		});
		*/
		/*对话框-选择*/
		
		EQPageDialogB_SUB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EQ_INC_SUB(false);
			}
		});
		EQPageDialogB_SUB.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				EQPageDialogB_SUB.setStart();
				return false;
			}
		});
		EQPageDialogB_SUB.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				EQ_INC_SUB(false);
			}
		}, MacCfg.LongClickEventTimeMax);
		/////
		EQPageDialogB_INC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EQ_INC_SUB(true);
			}
		});
		EQPageDialogB_INC.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				EQPageDialogB_INC.setStart();
				return false;
			}
		});
		EQPageDialogB_INC.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				EQ_INC_SUB(true);
			}
		}, MacCfg.LongClickEventTimeMax);
		
	}
	
	private void EQ_INC_SUB(boolean inc){
		if(inc){//递增
			switch (DataOPT) {
				case DataOPT_BW:
					if(--data < 0){
						data = 0;
					}
//					EQPageDialogSeekBar.setProgress(Define.EQ_BW_MAX-data);
                    sb_val.setProgress(Define.EQ_BW_MAX-data);
					if(mEQFreqBWGainListener != null){
						mEQFreqBWGainListener.onBWSeekBarListener(data, 0, false);
			        }
					break;
				case DataOPT_Freq:
					if(++data > 20000){
						data = 20000;
					}
//					EQPageDialogSeekBar.SetSeekbarFreq(data);
//	    			EQPageDialogSeekBar.setProgress(GetFreqDialogSeekBarIndex(data));
                    sb_val.setProgress(GetFreqDialogSeekBarIndex(data));
					if(mEQFreqBWGainListener != null){
						mEQFreqBWGainListener.onFreqSeekBarListener(data, 0, false);
			        }
					break;
				case DataOPT_Gain:
					if(++data >  DataStruct.CurMacMode.EQ.EQ_Gain_MAX){
						data = DataStruct.CurMacMode.EQ.EQ_Gain_MAX;
					}
//					EQPageDialogSeekBar.setProgress(data);
                    sb_val.setProgress(data);
					if(mEQFreqBWGainListener != null){
						mEQFreqBWGainListener.onGainSeekBarListener(data, 0, false);
			        }	
					break;
				default:
					break;
			}
		}else{
			switch (DataOPT) {
				case DataOPT_BW:
					if(++data > Define.EQ_BW_MAX){
						data = Define.EQ_BW_MAX;
					}
//					EQPageDialogSeekBar.setProgress(Define.EQ_BW_MAX-data);
                    sb_val.setProgress(Define.EQ_BW_MAX-data);
					if(mEQFreqBWGainListener != null){
						mEQFreqBWGainListener.onBWSeekBarListener(data, 0, false);
			        }
					break;
				case DataOPT_Freq:
					if(--data < 20){
						data = 20;
					}
//					EQPageDialogSeekBar.SetSeekbarFreq(data);
//	    			EQPageDialogSeekBar.setProgress(GetFreqDialogSeekBarIndex(data));
                    sb_val.setProgress(GetFreqDialogSeekBarIndex(data));
					if(mEQFreqBWGainListener != null){
						mEQFreqBWGainListener.onFreqSeekBarListener(data, 0, false);
			        }
					break;
				case DataOPT_Gain:
					if(--data < 0){
						data = 0;
					}
//					EQPageDialogSeekBar.setProgress(data);
                    sb_val.setProgress(data);
					if(mEQFreqBWGainListener != null){
						mEQFreqBWGainListener.onGainSeekBarListener(data, 0, false);
			        }	
					break;
				default:
					break;
			}
		}
		FlashVal(data);
	}

    private void FlashVal(int data){
        switch (DataOPT) {
            case DataOPT_BW:
                TV_Val.setText(ChangeBWValume(data));
                 break;
            case DataOPT_Freq:
                TV_Val.setText(String.valueOf(data)+"Hz");
                break;
            case DataOPT_Gain:
                TV_Val.setText(ChangeGainValume(data));
                break;
            default:
                break;
        }
    }

    //------------------------------------
    //获取Equalizer 的EQ的增益数据显示
    private String ChangeGainValume(int num){
        //System.out.println("ChangeValume:"+num);
        String show = null;
        DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show=decimalFormat.format(0.0-(DataStruct.CurMacMode.EQ.EQ_Gain_MAX/2-num)/10.0);//format 返回的是字符串
        show=show+"dB";
        return show;
    }
    //获取Equalizer 的EQ的Q值数据显示
    private String ChangeBWValume(int num){
        if(num>Define.EQ_BW_MAX){
            num=Define.EQ_BW_MAX;
        }
        String show = null;
        DecimalFormat decimalFormat=new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show=decimalFormat.format(Define.EQ_BW[num]);//format 返回的是字符串
        return show;
    }

}
