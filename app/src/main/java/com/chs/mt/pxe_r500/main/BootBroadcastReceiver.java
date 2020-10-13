package com.chs.mt.pxe_r500.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chs.mt.pxe_r500.service.ServiceOfCom;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        //if (intent.getAction().equals(ACTION)) {
//            Intent mainActivityIntent = new Intent(context, BootActivity.class);  // 要启动的Activity
//            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mainActivityIntent);
        //}
        //后边的XXX.class就是要启动的服务
        Intent service = new Intent(context, ServiceOfCom.class);
        service.putExtra("BOOT", true);
        context.startService(service);
        //ToastUtil.showShortToast(context,"启动程序");
        //启动应用，参数为需要自动启动的应用的包名
        //Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        //context.startActivity(intent );
    }

}