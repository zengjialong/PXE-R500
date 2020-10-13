package com.chs.mt.pxe_r500.updateApp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.util.ToastUtil;

import java.io.File;

/**
 * Created by Administrator on 2018/1/15.
 */

public class DownLoadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, id);
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
        }
    }

    /**
     * 下载完后安装apk
     *
     * @param
     */

    // 安装Apk
    private void installApk(Context context, long downloadApkId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloadapp", Activity.MODE_PRIVATE);
        long id = sharedPreferences.getLong("downloadid", 0);

        // 获取存储ID
        if (downloadApkId == id) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                File apkFile = context.getExternalFilesDir("Download/downapp");
                //对Android 版本判断
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // context.getPackageName() + ".fileprovider"  是配置中的authorities
                    Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
                    install.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                }
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
               // ToastUtil.showShortToast(context,context.getResources().getString(R.string.DownloadErr));
            }
        }
    }
}
