package com.chs.mt.pxe_r500.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.fragment.dialogFragment.Dialog.CommomDialog;

public class StatementActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_state);
        CommomDialog commomDialog = new CommomDialog(this, R.style.dialog, getResources().getString(R.string.StatementCON), new CommomDialog.OnAllCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {

                if(confirm){
                    Intent intent = new Intent();
                    intent.setClass(StatementActivity.this, MainTBTTActivity.class);
                    StatementActivity.this.startActivity(intent);
                }else{
                    finish();
                }
            }
        });
        commomDialog.setTitle(getResources().getString(R.string.Statement));
        commomDialog.setPositiveButton(getResources().getString(R.string.agree));
        commomDialog.setNegativeButton(getResources().getString(R.string.disagree));
        commomDialog.show();
    }
}
