package com.chs.mt.pxe_r500.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast;
    public static void showShortToast(Context context , String content) {
        if(content == null){
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.show();
    }


}
