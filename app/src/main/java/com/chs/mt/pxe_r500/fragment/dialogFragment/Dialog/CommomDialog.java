package com.chs.mt.pxe_r500.fragment.dialogFragment.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.chs.mt.pxe_r500.R;

/**
 * 启动界面之后的弹窗
 * */
public class CommomDialog extends Dialog implements View.OnClickListener {

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private static final String WEBVIEW_CONTENT = "<html><head></head><body style=\"text-align:justify;margin:0;\">%s</body></html>";

    private Context mContext;
    private String content;
    private OnAllCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;


    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog( Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;

    }

    public CommomDialog( Context context, int  cancelable, String content, OnAllCloseListener cancelListener) {
        super(context, cancelable);
        this.mContext = context;
        this.content = content;
        this.listener = cancelListener;
    }
    protected CommomDialog(Context context, boolean cancelable, OnCancelListener  cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }
    public CommomDialog setTitle(String title){
        this.title = title;
        return this;
    }


    public CommomDialog setPositiveButton(String name){   //设置左边取消栏的按钮文字
        this.positiveName = name;
        return this;
    }


    public CommomDialog setNegativeButton(String name){ //设置右边确定栏的按钮文字
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common_layout);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);


        contentTxt.setText(content);
        TextPaint tp = contentTxt.getPaint();

        tp.setFakeBoldText(true);
        // contentTxt.setVerticalScrollBarEnabled(false);
        //contentTxt.loadDataWithBaseURL("", title, "text/html", "utf-8","");

        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }


        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }


        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }


    public interface OnAllCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }


}
