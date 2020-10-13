package com.chs.mt.pxe_r500.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chs.mt.pxe_r500.R;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_filterItem extends RelativeLayout {
    private int Max = 3;
    private int index = 3;
    public Button[] ch = new Button[Max];
    private Context mContext;

    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_filterItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }
    public V_filterItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_filterItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_filter, this);

        ch[0] = (Button)findViewById(R.id.id_b_ch_0);
        ch[1] = (Button)findViewById(R.id.id_b_ch_1);
        ch[2] = (Button)findViewById(R.id.id_b_ch_2);

        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){
        ch[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                --index;
                if(index < 0){
                    index = 2;
                }
                setIndex(index);
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(index,0,true);
                }
            }
        });
        ch[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ++index;
                if(index > 2){
                    index = 0;
                }
                setIndex(index);
                if(mSetOnClickListener != null){
                    mSetOnClickListener.onClickListener(index,0,true);
                }
            }
        });

    }

    public void setIndex(int index){
        this.index = index;
        if(index == 0){
            ch[1].setText(mContext.getResources().getString(R.string.FilterLR));
        }else if(index == 1){
            ch[1].setText(mContext.getResources().getString(R.string.FilterB));
        }else if(index == 2){
            ch[1].setText(mContext.getResources().getString(R.string.FilterBW));
        }
    }

}
