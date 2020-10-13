package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.tools.MHS_SeekBar;

import java.text.DecimalFormat;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_DelayItem extends RelativeLayout {
    public LinearLayout LY;
    public Button BtnAdd,BtnSub,BtnVal;
    public MHS_SeekBar SB;

    private int Max = 259;
    private int val;
    private int DelayUnit = 2;
    private int tag = 0;

    private Context mContext;
    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int tag,int val, int type, boolean boolClick);
    }

    //结构函数
    public V_DelayItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_DelayItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_DelayItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        mContext = context;
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_delayitem, this);

        LY = (LinearLayout) findViewById(R.id.id_ly);
        BtnAdd = (Button)findViewById(R.id.id_b_right);
        BtnSub = (Button)findViewById(R.id.id_b_left);
        BtnVal = (Button)findViewById(R.id.id_b_val);
        SB = (MHS_SeekBar) findViewById(R.id.id_sb_l);

        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){
        BtnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((++val) > Max) {
                    val = Max;
                }
                flashDelayVal();
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(tag,val,val,true);
                }
            }
        });

        BtnSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((--val) < 0) {
                    val = 0;
                }
                flashDelayVal();
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(tag,val,val,true);
                }
            }
        });
        SB.setProgressMax(Max);
        SB.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                val = progress;
                flashDelayVal();
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(tag,val,val,true);
                }
            }
        });
    }
    public void setMax(int sMax){
        Max = sMax;
        SB.setProgressMax(Max);
    }
    public void set_Tag(int sMax){
        tag = sMax;
    }
    public void setPress(boolean press){
        if(press){
            LY.setBackground(mContext.getResources().
                    getDrawable(R.drawable.chs_layoutc_delay_press));
        }else {
            LY.setBackground(mContext.getResources().
                    getDrawable(R.drawable.chs_layoutc_delay_normal));
        }

    }

    public void setDelayUnit(int uint){
        DelayUnit = uint;
        ChannelShowDelay(val);
    }
    public void setDelayVal(int Vals){
        this.val = Vals;
        SB.setProgress(val);
        BtnVal.setText(ChannelShowDelay(val));
    }
    public void flashDelayVal(){
        BtnVal.setText(ChannelShowDelay(val));
    }
    /******* 延时时间转换  *******/
    private String ChannelShowDelay(int timedelay){
        String delaytimes=new String();
        switch(DelayUnit){
            case 1:delaytimes=CountDelayCM(timedelay);
                break;
            case 2:delaytimes=CountDelayMs(timedelay);
                break;
            case 3:delaytimes=CountDelayInch(timedelay);
                break;
            default: break;
        }
        return delaytimes;
    }

    private String  CountDelayCM(int num){
        DecimalFormat decimalFormat=new DecimalFormat("0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p="";
        int m_nTemp=75;
        float Time = (float) (num/48.0); //当Delay〈476时STEP是0.021MS；
        float LMT = (float) (((m_nTemp-50)*0.6+331.0)/1000.0*Time);
        LMT = LMT*100;
        //float LFT = (float) (LMT*3.2808*12.0);

        int fr=(int) (LMT*10);
        int ir = fr%10;
        int ri = 0;
        if(ir>=5){
            ri=fr/10+1;
        }else{
            ri=fr/10;
        }
        p=decimalFormat.format(ri);
        return p;
    }

    private String  CountDelayMs(int num){
        DecimalFormat decimalFormat=new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p="";
        int fr = num*10000/48;
        int ir = fr%10;
        int ri = 0;
        if(ir>=5){
            ri=fr/10+1;
        }else{
            ri=fr/10;
        }
        p=decimalFormat.format(ri/1000.0);
        return p;
    }

    private String  CountDelayInch(int num){
        DecimalFormat decimalFormat=new DecimalFormat("0");
        String p="";
        float base=(float) 331.0;
        if(num== DataStruct.CurMacMode.Delay.MAX){
            base=(float) 331.4;
        }
        int m_nTemp=75;
        float Time = (float) (num/48.0); //当Delay〈476时STEP是0.021MS；
        float LMT = (float) (((m_nTemp-50)*0.6+base)/1000.0*Time);

        float LFT = (float) (LMT*3.2808*12.0);

        int fr=(int) (LFT*10);
        int ir = fr%10;
        int ri = 0;
        if(ir>=5){
            ri=fr/10+1;
        }else{
            ri=fr/10;
        }
        p=decimalFormat.format(ri);
        return p;
    }
}
