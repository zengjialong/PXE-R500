package com.chs.mt.pxe_r500.util;

import java.io.*;

/**
 * @author Administrator
 *
 */

public class LogUtil {
	public static final int LOG_LEVEL_EMERGENCY = 0;
	public static final int LOG_LEVEL_ERROR = 1;
	public static final int LOG_LEVEL_WARNING = 2;
	public static final int LOG_LEVEL_INFO = 3;
	public static final int LOG_LEVEL_DEBUG = 4;
	
	public static final boolean LOG_CONSOLE_ENABLED = true;
	public static final boolean LOG_FS_ENABLED = false;
	
	public static final boolean LOG_ENABLED_EMERGENCY = true;
	public static final boolean LOG_ENABLED_ERROR = true;
	public static final boolean LOG_ENABLED_WARNING = true;
	public static final boolean LOG_ENABLED_INFO = true;
	public static final boolean LOG_ENABLED_DEBUG = true;
	
	@SuppressWarnings({ "unused", "deprecation" })
	public static void log(int level, String description) {
		if((!LOG_CONSOLE_ENABLED&&!LOG_FS_ENABLED)
				||(level==LOG_LEVEL_EMERGENCY&&!LOG_ENABLED_EMERGENCY)
				||(level==LOG_LEVEL_ERROR&&!LOG_ENABLED_ERROR)
				||(level==LOG_LEVEL_WARNING&&!LOG_ENABLED_WARNING)
				||(level==LOG_LEVEL_INFO&&!LOG_ENABLED_INFO)
				||(level==LOG_LEVEL_DEBUG&&!LOG_ENABLED_DEBUG))
			return;
		String type = null;
		switch(level) {
			case LOG_LEVEL_EMERGENCY:
				type = "Emergency";
				break;
			case LOG_LEVEL_ERROR:
				type = "Error";
				break;
			case LOG_LEVEL_WARNING:
				type = "Warning";
				break;
			case LOG_LEVEL_INFO:
				type = "Info";
				break;
			case LOG_LEVEL_DEBUG:
				type = "Debug";
				break;
			default:
				return;
		}
//		Mediator mediator = Mediator.getInstance();
//		try{
//			PackageInfo pm = mediator.getPackageManager().getPackageInfo(mediator.getApplicationContext().getPackageName(), PackageManager.GET_ACTIVITIES);
//			String versionInfo = String.format("%s_%s", pm.versionName, pm.versionCode);
//			String phoneInfo = String.format("%s_%s_%s_%s", Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION.SDK);
//			String curLog = String.format("%s[%s]%s[%s]%s", phoneInfo, versionInfo, (new Date()).toLocaleString(), type, description);
//			if(LOG_CONSOLE_ENABLED) {
//				System.out.println(curLog);
//			}
//			if(LOG_FS_ENABLED) {
//				String sdCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//				File file = new File(String.format("%s/AtuatLog.txt", sdCardPath));
//				if(!file.exists()){
//					try {
//						file.createNewFile();
//					}
//					catch(Exception e) {
//						System.out.println(printException(e));
//					}
//				}
//				if(file.exists()) {
//					try {
//						OutputStream os = new FileOutputStream(file);
//						os.write(curLog.getBytes());
//						os.close();
//					}
//					catch (Exception e) {
//						System.out.println(printException(e));
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			System.out.println(printException(e));
//		}
	}
	
	private static String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}
	
	public static void log(Exception e){
		log(LOG_LEVEL_DEBUG, printException(e));
	}
}
