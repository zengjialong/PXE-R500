package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.tools.KnobView_KTV;
import com.chs.mt.pxe_r500.tools.LongCickButton;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_SeekBarIndexValItem extends RelativeLayout {
    private KnobView_KTV Sb;
    private TextView Index;
    private TextView Val;
    private LongCickButton BtnAdd,BtnSub;
    private int curProgress = 0;
    private int Max = 0;
    private float mSeekBarMax = 0;

    private AttributeSet mAttrs = null;

    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_SeekBarIndexValItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_SeekBarIndexValItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_SeekBarIndexValItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_seekbarindexvalitem, this);

        Sb = (KnobView_KTV)findViewById(R.id.id_kv_sb);
        Val = (TextView)findViewById(R.id.id_tv_val);
        Index = (TextView)findViewById(R.id.id_tv_index);
        BtnAdd = (LongCickButton)findViewById(R.id.id_b_right);
        BtnSub = (LongCickButton)findViewById(R.id.id_b_left);
        TypedArray localTypedArray = context.obtainStyledAttributes(mAttrs, R.styleable.KNOB);
        clickEvent();

    }

    public void setMax(int max){
        Sb.setMax(max);
        Max = max;
    }
    public void setProgressMax(int max){

        mSeekBarMax = max;
    }

    public void setProgress(int progress){
        if(curProgress != progress){
            curProgress = progress;
            Sb.setProgress(progress);
            Val.setText(String.valueOf(progress));
        }
    }

    public void setIndexText(String st){
        Index.setText(st);
    }

    /*
    响应事件
     */
    private void clickEvent(){
        BtnSub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SUB();
            }
        });
        BtnSub.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                BtnSub.setStart();
                return false;
            }
        });
        BtnSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SUB();
            }
        }, MacCfg.LongClickEventTimeMax);
        /////
        BtnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                INC();
            }
        });
        BtnAdd.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                BtnAdd.setStart();
                return false;
            }
        });
        BtnAdd.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                INC();
            }
        }, MacCfg.LongClickEventTimeMax);
//        BtnAdd.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
//            @Override
//            public void onLongTouch() {
//                INC();
//            }
//        }, MacCfg.LongClickEventTimeMax);
//        BtnAdd.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                INC();
//            }
//        });

        BtnSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SUB();
            }
        });
        Sb.setProgressChangeListener(new KnobView_KTV.OnKnobView_PicTwo_ProgressChangeListener() {
            @Override
            public void onProgressChanged(KnobView_KTV view, boolean fromUser, int progress) {
                curProgress = progress;
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(progress,progress,true);
                    Val.setText(String.valueOf(progress));
                }

            }

            @Override
            public void onStartTrackingTouch(KnobView_KTV view, int progress) {

            }

            @Override
            public void onStopTrackingTouch(KnobView_KTV view, int progress) {

            }
        });
    }

    private void INC(){
        if ((++curProgress) > Max) {
            curProgress = Max;
        }
        Sb.setProgress(curProgress);
        Val.setText(String.valueOf(curProgress));
        if(mSetOnClickListener != null){
            mSetOnClickListener.onClickListener(curProgress,curProgress,true);
        }
    }
    private void SUB(){
        if ((--curProgress) < 0) {
            curProgress = 0;
        }
        Sb.setProgress(curProgress);
        Val.setText(String.valueOf(curProgress));
        if(mSetOnClickListener != null){
            mSetOnClickListener.onClickListener(curProgress,curProgress,true);
        }
    }
}
