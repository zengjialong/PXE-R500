package com.chs.mt.pxe_r500.main;

/**
 * Created by Administrator on 2017/6/14.
 */

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.drawable.ColorDrawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.LinearLayout;
        import android.widget.LinearLayout.LayoutParams;
        import android.widget.PopupWindow;
        import android.widget.TextView;

        import com.chs.mt.pxe_r500.R;
        import com.chs.mt.pxe_r500.datastruct.Define;
        import com.chs.mt.pxe_r500.datastruct.MacCfg;
        import com.chs.mt.pxe_r500.operation.AnimationUtil;

        import java.util.Locale;

public class PopupMenuActivity extends PopupWindow implements OnClickListener {

    private Activity activity;
    private View popView;
    private int MaxItem=13;
    private View[] VItem  = new View[MaxItem];

    private View[] view_line  = new View[5];
    private OnItemClickListener onItemClickListener;
    private TextView TVTMode;
    private Context context;

    private LinearLayout ly_A_sound;

    private TextView[] txt=new TextView[6];
    public PopupMenuActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        context=this.activity;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.chs_layout_popupmenu, null);// 加载菜单布局文件




        this.setContentView(popView);// 把布局文件添加到popupwindow中
      //  this.setWidth(dip2px(activity, LayoutParams.WRAP_CONTENT));// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);// 获取焦点
        this.setTouchable(true); // 设置PopupWindow可触摸
        this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);




        // 获取选项卡
        VItem[0] = popView.findViewById(R.id.ly_item1);
        VItem[1]  = popView.findViewById(R.id.ly_item2);
        VItem[2]  = popView.findViewById(R.id.ly_item3);
        VItem[3]  = popView.findViewById(R.id.ly_item4);
        VItem[4]  = popView.findViewById(R.id.ly_item5);
        VItem[5]  = popView.findViewById(R.id.ly_item6);
        VItem[6]  = popView.findViewById(R.id.ly_item7);
        VItem[7]  = popView.findViewById(R.id.ly_item8);
        VItem[8]  = popView.findViewById(R.id.ly_item9);
        VItem[9]  = popView.findViewById(R.id.ly_item10);
        VItem[10]  = popView.findViewById(R.id.ly_item11);
        VItem[11]  = popView.findViewById(R.id.ly_item12);
        VItem[12]  = popView.findViewById(R.id.ly_item13);
        //TVTMode = VItem[9].findViewById(R.id.id_tv_turningmode);
        TVTMode=popView.findViewById(R.id.id_tv_sound_effect);

        ly_A_sound=popView.findViewById(R.id.main_dialog_layout);



        view_line[0]=popView.findViewById(R.id.id_line1);
        view_line[1]=popView.findViewById(R.id.id_line2);
        view_line[2]=popView.findViewById(R.id.id_line3);
        view_line[3]=popView.findViewById(R.id.id_line4);
        view_line[4]=popView.findViewById(R.id.id_line5);



        // 获取选项卡
        txt[0] = popView.findViewById(R.id.id_tv_sound_effect);
        txt[1]  = popView.findViewById(R.id.id_shutdown);
        txt[2]  = popView.findViewById(R.id.id_share);
        txt[3]  = popView.findViewById(R.id.id_save);
        txt[4]  = popView.findViewById(R.id.id_sound);
        txt[5]  = popView.findViewById(R.id.id_about);




        if(isZh(context)){
            for (int i = 0; i <txt.length ; i++) {
                txt[i].setTextSize(12);
            }
        }else{
            for (int i = 0; i <txt.length ; i++) {
                txt[i].setTextSize(10);
            }
        }



        // 添加监听
        for(int i=0;i<MaxItem;i++){
            VItem[i].setTag(i);
            VItem[i].setOnClickListener(this);
        }
        flashTurningMode();


     //   measuring();
    }

    /**
     * 用于测量菜单框的线条大小
     * */
    private void measuring(){
        int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        ly_A_sound.measure(width,height);
        int height1=ly_A_sound.getMeasuredHeight();
        int width1=ly_A_sound.getMeasuredWidth();



//        LayoutParams lp;
//        lp= (LayoutParams) line1.getLayoutParams();
//        lp.width=width1;
//        lp.height=3;
//        line1.setLayoutParams(lp);
//        line2.setLayoutParams(lp);
//        line3.setLayoutParams(lp);
//        line4.setLayoutParams(lp);
//        line5.setLayoutParams(lp);
//
//        line1.setVisibility(View.VISIBLE);
//        line2.setVisibility(View.VISIBLE);
//        line3.setVisibility(View.VISIBLE);
//        line4.setVisibility(View.VISIBLE);
//        line5.setVisibility(View.VISIBLE);
    }


    public void flashTurningMode(){
        if(MacCfg.bool_Encryption){
            TVTMode.setText(R.string.Deciphering);
        }else{
            TVTMode.setText(R.string.Encryption);
        }
    }

    /**
     * 设置显示的位置
     *
     * @param resourId
     *            这里的x,y值自己调整可以
     */
    public void showLocation(int resourId) {
        showAsDropDown(activity.findViewById(resourId), dip2px(activity, 0),
                dip2px(activity, -3));
    }

    @Override
    public void onClick(View v) {
        AnimationUtil.AnimScaleI(this.activity,v);
        int item = (Integer) v.getTag();

        if (onItemClickListener != null) {
            onItemClickListener.onClick(item, "");
        }
        dismiss();
    }

    // dip转换为px
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.3f);
    }

    // 点击监听接口
    public interface OnItemClickListener {
        public void onClick(int item, String str);
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }


}