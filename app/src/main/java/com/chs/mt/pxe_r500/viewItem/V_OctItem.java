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

public class V_OctItem extends RelativeLayout {
    private int Max = 4;
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
    public V_OctItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }
    public V_OctItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_OctItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_oct, this);

        ch[0] = (Button)findViewById(R.id.id_b_ch_0);
        ch[1] = (Button)findViewById(R.id.id_b_ch_1);
        ch[2] = (Button)findViewById(R.id.id_b_ch_2);
        ch[3] = (Button)findViewById(R.id.id_b_ch_3);
//        ch[4] = (Button)findViewById(R.id.id_b_ch_4);
//        ch[5] = (Button)findViewById(R.id.id_b_ch_5);

        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){
        for(int i=0;i<Max;i++){
            ch[i].setTag(i);
            ch[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int)view.getTag();
                    if(mSetOnClickListener != null){
                        mSetOnClickListener.onClickListener(index,0,true);
                    }
                    setCh(index);
                }
            });
        }
    }

    public void setCh(int index){
        if(index >= Max){
            return;
        }
        for(int i=0;i<Max;i++){
            ch[i].setBackground(getResources().getDrawable(R.drawable.chs_btn_oct_normal));
            ch[i].setTextColor(mContext.
                    getResources().getColor(R.color.xover_text_color_normal));
        }
        ch[index].setBackground(getResources()
                .getDrawable(R.drawable.chs_btn_oct_press));
        ch[index].setTextColor(mContext.
                getResources().getColor(R.color.xover_text_color_press));
    }
}
