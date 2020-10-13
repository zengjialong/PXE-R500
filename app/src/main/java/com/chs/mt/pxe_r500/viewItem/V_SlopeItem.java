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

public class V_SlopeItem extends RelativeLayout {

    private final int maxCH = 4;
    private Button[] ButtomItem = new Button[maxCH];
    private View[] VItem = new View[maxCH];
    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_SlopeItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_SlopeItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_SlopeItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_slopeitem, this);

        ButtomItem[0] = (Button)findViewById(R.id.id_b_0);
        ButtomItem[1] = (Button)findViewById(R.id.id_b_1);
        ButtomItem[2] = (Button)findViewById(R.id.id_b_2);
        ButtomItem[3] = (Button)findViewById(R.id.id_b_3);


        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){

        for(int i=0;i<maxCH;i++){
            ButtomItem[i].setTag(i);
            ButtomItem[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                int val = (int) v.getTag();
                setSelect(val);

                if (mSetOnClickListener != null) {
                    mSetOnClickListener.onClickListener(val, 0, true);
                }
                }
            });
        }
    }

    public void setSelect(int ch){
        for(int j=0;j<maxCH;j++){
            ButtomItem[j].setBackgroundResource(R.drawable.chs_btn_slope_sel_normal);
            ButtomItem[j].setTextColor(getResources().getColor(R.color.text_color_xovernotset));
        }
        ButtomItem[ch].setBackgroundResource(R.drawable.chs_btn_slope_sel_press);
        ButtomItem[ch].setTextColor(getResources().getColor(R.color.text_color_xoverset));
    }


    public void showMax(Boolean show){
        if(show){
            ButtomItem[3].setVisibility(VISIBLE);
            ButtomItem[2].setVisibility(VISIBLE);
        }else{
            ButtomItem[3].setVisibility(GONE);
            ButtomItem[2].setVisibility(GONE);
        }

    }

}
