package com.chs.mt.pxe_r500.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.chs.mt.pxe_r500.datastruct.MacCfg;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Environment;

public class ImageUtil {
	 private static final int STROKE_WIDTH = 4;
	//从assets资源中获取图片
	public static Bitmap getBitmap(Context context,String filename){
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			
			InputStream is = am.open(filename);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	
	
	
	/**
    * 加载本地图片
    * @param url
    * @return
    */
    public static Bitmap getLocalBitmap(String url) {
         try {
              FileInputStream fis = new FileInputStream(url);
              return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

         } catch (FileNotFoundException e) {
              e.printStackTrace();
              return null;
         }
    }
    private static String getAppInfo(Context context) {
    	String appnameString=null;
 		try {
 			appnameString = context.getPackageName();
// 			String versionName = context.getPackageManager().getPackageInfo(
// 					pkName, 0).versionName;
// 			int versionCode = context.getPackageManager()
// 					.getPackageInfo(pkName, 0).versionCode;
// + "   " + versionName + "  " + versionCode;
 		} catch (Exception e) {
 			appnameString=null;
 		}
 		return appnameString;
 	}
    
    public Uri getImageURI(Context context) throws Exception {
		File cache = new File(Environment.getExternalStorageDirectory(), MacCfg.AgentNAME);
		System.out.println("cache = " + cache.toString());
        String name = getAppInfo(context)+".png";
        File file = new File(cache, name);
        // 如果图片存在本地缓存目录，则不去服务器下载 
        if (file.exists()) {
            return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
        }
        return null;
	}
    public static  Bitmap SetUserDefaultHeadImg(Context context) {
    	Bitmap bitmap = getBitmap(context,"chs_head_pic.png");
        return SetRoundBitmap(bitmap) ; 
	}
	public static  Bitmap GetAndSetRoundBitmap(Context context, String imgName) {
		
		File cache = new File(Environment.getExternalStorageDirectory(), MacCfg.AgentNAME);
		System.out.println("cache = " + cache.toString());
        String filename = cache.toString()+"/"+MacCfg.Mac+"/"+imgName;//
        
        
		 Bitmap bitmap= getLocalBitmap(filename);
		 if(bitmap == null){
			 bitmap = getBitmap(context,"chs_head_pic.png");
		 }		 
        return SetRoundBitmap(bitmap) ; 
	}
	public static  Bitmap SetRoundBitmap(Bitmap bitmap) {
		 int width = bitmap.getWidth();
		 int height = bitmap.getHeight();
		 float roundPx;
	     float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
		 if (width <= height) {
		      roundPx = width / 2;
		      top = 0;
		      left = 0;
		      bottom = width;
	          right = width;
	          height = width;
		      dst_left = 0;
		      dst_top = 0;
		      dst_right = width;
		      dst_bottom = width;
		  } else {
		      roundPx = height / 2;
	          float clip = (width - height) / 2;
		      left = clip;
		      right = width - clip;
		      top = 0;
		      bottom = height;
			  width = height;
			  dst_left = 0;
			  dst_top = 0;
			  dst_right = height;
			  dst_bottom = height;
	     }

        Bitmap output = Bitmap.createBitmap(width,
		height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
	    final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
	    final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(Color.WHITE);
	    paint.setStrokeWidth(4); 
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, src, dst, paint);
	    
	    //画白色圆圈
	    paint.reset();
	    paint.setColor(Color.WHITE);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeWidth(STROKE_WIDTH);
	    paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2 - STROKE_WIDTH / 2, paint);
        return output ; 
	}
	//保存网络图片到本地 第一个参数是图片名称 第二个参数为需要保存的bitmap
	public boolean saveLocalImg(Context context,Bitmap bitmap, String ImgName) {
		boolean res=false;
		String appName=getAppInfo(context);
		
		//��������Ŀ¼��ϵͳһ���о͵ô�������Ŀ¼�ģ�
		File cache = new File(Environment.getExternalStorageDirectory(),MacCfg.AgentNAME + "/"+MacCfg.Mac);
         
        if(!cache.exists()){
                cache.mkdirs();
        }
        
		File file = new File(cache, ImgName);
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
//			System.out.println("BUG  "+"saveImg= " + chs_file.toURI());

			res=true;
		} catch (FileNotFoundException e) {
			res=false;
			e.printStackTrace();
		} catch (IOException e) {
			res=false;
			e.printStackTrace();
		}
		return res;
	}
//	//将剪切后的图片保存到本地图片上！ OK
//	public  void savaBitmap(Bitmap bitmap){
//		Date date = new Date(System.currentTimeMillis());  
//		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMddHHmmss");  
//		cutnameString= dateFormat.format(date);
//		filename=Environment.getExternalStorageDirectory().getPath()+"/"+cutnameString+".png";
//		File f = new File(filename);
//		FileOutputStream fOut = null;
//		try {
//			f.createNewFile();
//			fOut = new FileOutputStream(f);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);//把Bitmap对象解析成流
//		try {
//			fOut.flush();
//			fOut.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
		
	//读取本地图片 参数路径
	@SuppressWarnings("unused")
	private Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			// File chs_file = new File(pathString);
			// if (chs_file.exists()) {
			bitmap = BitmapFactory.decodeFile(pathString);
			// }
		} catch (Exception e) {
		}

		return bitmap;
	}
}
