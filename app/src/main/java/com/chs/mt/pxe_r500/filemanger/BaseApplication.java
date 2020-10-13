package com.chs.mt.pxe_r500.filemanger;
import com.chs.mt.pxe_r500.R;


import java.io.File;

import com.chs.mt.pxe_r500.filemanger.common.StringUtil;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * 全局应用类：用于保存和调用全局应用配置
 * 
 */
public class BaseApplication extends Application {

	// wifi状态 联通状态 移动状态
	private static int NETTYPE_WIFI = 0x01;
	private static int NETTYPE_CMWAP = 0x02;
	private static int NETTYPE_CMNET = 0x03;

	// 登陆状态
	private boolean login = false;

	// 登陆的ID
	private int loginUnid;

	@Override
	public void onCreate() {
		super.onCreate();
	};

	/**
	 * 现在翻转屏是否是横向的
	 * 
	 * @return
	 */
	public boolean isLandscape() {
		Configuration config = getResources().getConfiguration();

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return false;
		} else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是平板登陆
	 * 
	 * @return
	 */
	public boolean isTablet() {

		return getResources().getBoolean(R.bool.isTablet);
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0:没有网络 1:wifi网络 2:wap网络 3:net网络
	 */
	public int getNetworkType() {
		int netType = 0;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (ni == null) {
			return netType;
		}

		int nType = ni.getType();

		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = ni.getExtraInfo();
			if (!StringUtil.isEmpty(extraInfo)) {
				// 如果小写等于cmnet
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}

		return netType;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param versionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int versionCode) {
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= versionCode;
	}

	/**
	 * 获取app安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;

		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}

		if (info == null)
			info = new PackageInfo();

		return info;
	}

	/**
	 * 清除缓存目录
	 * 
	 * @param dir
	 *            目录
	 * @param curTime
	 *            当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;

		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					// 如果文件的最后修改时间小于当前系统时间
					if (child.lastModified() < curTime) {
						// 删除
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}
}
