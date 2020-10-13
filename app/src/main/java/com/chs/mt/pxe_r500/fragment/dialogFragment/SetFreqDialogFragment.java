package com.chs.mt.pxe_r500.fragment.dialogFragment;
import com.chs.mt.pxe_r500.R;



import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.tools.LongCickButton;
import com.chs.mt.pxe_r500.tools.MHS_Num_SeekBar;
import com.chs.mt.pxe_r500.tools.LongCickButton.LongTouchListener;
import com.chs.mt.pxe_r500.tools.MHS_Num_SeekBar.OnMHS_NumSeekBarChangeListener;
import android.annotation.SuppressLint;
import android.app.Dialog;
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


@SuppressLint("NewApi")
public class SetFreqDialogFragment extends DialogFragment {
	private static volatile SetFreqDialogFragment dialog = null;
	private View V_FreqSeekBarDialog;
	private Dialog FreqSeekBarDialog;
	private Button FreqDialogButton;
	private LongCickButton FreqDialogB_SUB,FreqDialogB_INC;
	private MHS_Num_SeekBar FreqDialogSeekBar;
	private TextView Msg;
	/*长按按键的操作：0-减操作，1-加操作*/
	private int SYNC_INCSUB=0;
	/*按键长按：true-长按，false-非长按*/
	private boolean B_LongPress=false;
	
	private int Freq = 0;
	public static final String ST_Freq = "Freq";
	private int HP_Freq = 0;
	public static final String ST_HP_Freq = "ST_HP_Freq";
	private int LP_Freq = 0;
	public static final String ST_LP_Freq = "ST_LP_Freq";
	private boolean BOOL_HP = false;
	public static final String ST_BOOL_HP = "BOOL_HP";
	private ImageView imageView;
	private TextView txt_Freq;
	
	
	private OnFreqDialogFragmentChangeListener mFreqListener;
    public void OnSetFreqDialogFragmentChangeListener(OnFreqDialogFragmentChangeListener listener) {
        this.mFreqListener = listener;
    }

    public interface OnFreqDialogFragmentChangeListener{
        void onFreqSeekBarListener(int Freq, int type, boolean flag);
//        void onFreqClickINCListener(int Freq, int type);
//        void FreqClickSUBListener(int Freq, int type);
    }

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		Freq = getArguments().getInt(ST_Freq);
		BOOL_HP = getArguments().getBoolean(ST_BOOL_HP);
		HP_Freq = getArguments().getInt(ST_HP_Freq);
		LP_Freq = getArguments().getInt(ST_LP_Freq);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		System.out.println("onCreate SetFreqDialogFragment:" + Freq);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);            
        View view = inflater.inflate(R.layout.chs_set_freq_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_FreqSeekBarDialog) {  	
		
		
    	FreqDialogB_INC = (LongCickButton)V_FreqSeekBarDialog.findViewById(R.id.id_b_freq_dialog_inc);
    	FreqDialogB_SUB = (LongCickButton)V_FreqSeekBarDialog.findViewById(R.id.id_b_freq_dialog_sub);
    	FreqDialogButton = (Button)V_FreqSeekBarDialog.findViewById(R.id.id_btn_sure);
    	FreqDialogSeekBar = (MHS_Num_SeekBar)V_FreqSeekBarDialog.findViewById(R.id.id_freq_dialog_seekbar);
		imageView=V_FreqSeekBarDialog.findViewById(R.id.id_chs_dialog_exit);

		txt_Freq=V_FreqSeekBarDialog.findViewById(R.id.id_text_freq);

    	FreqDialogSeekBar.setTextUnit("Hz");
    	//FreqDialogSeekBar.setTextColor(Color.WHITE);
    	FreqDialogSeekBar.setMax(240);
    	FreqDialogSeekBar.SetSeekbarMin(0);
    	
    	FreqDialogSeekBar.SetSeekbarFreq(Freq);
    	FlashFreqDialogSeekBarProgress(Freq);
		txt_Freq.setText(String.valueOf(Freq)+"Hz");
    	if(BOOL_HP){
    		FreqDialogSeekBar.setHP_MaxP(GetFreqDialogSeekBarIndex(LP_Freq));
		}else{
			FreqDialogSeekBar.setLP_MinP(GetFreqDialogSeekBarIndex(HP_Freq));
		}

        Msg = (TextView) V_FreqSeekBarDialog.findViewById(R.id.id_text_msg);
        if(BOOL_HP){
            Msg.setText(getResources().getString(R.string.H_P_F_Freq));
        }else{
            Msg.setText(getResources().getString(R.string.L_P_F_Freq));
        }
	}
	
	void FlashFreqDialogSeekBarProgress(int fP){  	
    	for(int i=0;i<240;i++){
    		if((fP>=Define.FREQ241[i])&&(fP<=Define.FREQ241[i+1])){
    			FreqDialogSeekBar.setProgress(i+1);
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
		
	}

	private void initClick() {
		FreqDialogButton.setOnClickListener(new OnClickListener() {   		
			@Override
			public void onClick(View view) {
				getDialog().cancel();
			}
		});

		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});

    	FreqDialogSeekBar.setOnMHS_NumSeekBarChangeListener(new OnMHS_NumSeekBarChangeListener() {			
			@Override
			public void onProgressChanged(MHS_Num_SeekBar mhs_SeekBar, int progress,
					boolean fromUser) {
				int ps = (int) Define.FREQ241[progress];
				
				if(BOOL_HP){
		    		if(ps > LP_Freq){
		    			ps = LP_Freq;
		    		}
		    		
				}else{
					if(HP_Freq > ps){
		    			ps = HP_Freq;
		    		}
				}
				Freq = ps;
				FlashFreqDialogSeekBarProgress(ps);
	    		FreqDialogSeekBar.SetSeekbarFreq(ps);
				txt_Freq.setText(String.valueOf(ps)+"Hz");
				if(mFreqListener != null){
					mFreqListener.onFreqSeekBarListener(ps, 0, fromUser);
		        }		
			}
		});
    	/*对话框-选择*/
    	FreqDialogB_SUB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SYNC_INCSUB=0;
				Freq_INC_SUB(false);
			}
		});
    	FreqDialogB_SUB.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				FreqDialogB_SUB.setStart();
				return false;
			}
		});
    	FreqDialogB_SUB.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				SYNC_INCSUB=0;
				Freq_INC_SUB(false);
			}
		}, MacCfg.LongClickEventTimeMax);
		/////
    	FreqDialogB_INC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SYNC_INCSUB=1;
				Freq_INC_SUB(true);	
			}
		});
    	FreqDialogB_INC.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				FreqDialogB_INC.setStart();
				return false;
			}
		});
    	FreqDialogB_INC.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				SYNC_INCSUB=1;
				Freq_INC_SUB(true);		
			}
		}, MacCfg.LongClickEventTimeMax);
	}
	
	private void Freq_INC_SUB(boolean inc){
		if(inc){//递增
			if(BOOL_HP){//高通
				if(++Freq > LP_Freq){
					Freq = LP_Freq;
				}
			}else{
				if(++Freq > 20000){
					Freq = 20000;
				}
			}
		}else{
			if(BOOL_HP){//高通
				if(--Freq < 20){
					Freq = 20;
				}
			}else{
				if(--Freq < HP_Freq){
					Freq = HP_Freq;
				}
			}
		}
		FlashFreqDialogSeekBarProgress(Freq);
		FreqDialogSeekBar.SetSeekbarFreq(Freq);
		txt_Freq.setText(String.valueOf(Freq)+"Hz");
		if(mFreqListener != null){
			mFreqListener.onFreqSeekBarListener(Freq, 0, false);
        }		
	}

}
