package com.chs.mt.pxe_r500.operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chs.mt.pxe_r500.R;
import com.google.gson.Gson;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.encrypt.SeffFileCipherUtil;
import com.chs.mt.pxe_r500.filemanger.common.FileUtil;
import com.chs.mt.pxe_r500.bean.JsonDataSt;
import com.chs.mt.pxe_r500.bean.CarBrands;
import com.chs.mt.pxe_r500.bean.CarTypes;
import com.chs.mt.pxe_r500.bean.Company;
import com.chs.mt.pxe_r500.bean.DSP_Data;
import com.chs.mt.pxe_r500.bean.DSP_DataInfo;
import com.chs.mt.pxe_r500.bean.DSP_MACData;
import com.chs.mt.pxe_r500.bean.DSP_MusicData;
import com.chs.mt.pxe_r500.bean.DSP_OutputData;
import com.chs.mt.pxe_r500.bean.DSP_SingleData;
import com.chs.mt.pxe_r500.bean.MacTypes;
import com.chs.mt.pxe_r500.bean.MacsAgentName;
import com.chs.mt.pxe_r500.bean.SEFF_File;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

public class JsonRWUtil {

	public  static String getAppInfo(Context context) {
 		try {
 			String pkName = context.getPackageName();
 			return pkName;
 		} catch (Exception e) {
 			
 		}
 		return null;
 	}

    public static void checkAndAddDB(Context mContext,String filePath){
    	//加到数据库存中
    	JsonRWUtil jsonRWOpt = null;
    	jsonRWOpt = new JsonRWUtil();
    	DSP_SingleData mDSP_SData=new DSP_SingleData();	    	
    	mDSP_SData = jsonRWOpt.LoadJsonLocal2DSP_DataInfo(mContext,filePath);
    	DSP_DataInfo dataInfo = mDSP_SData.Get_data_info();     
		SEFF_File seff_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
		int dot=filePath.lastIndexOf("/");
		String name=filePath.substring(dot+1);   
        //Log.e("test#",name);   
        name =name.substring(0, name.length()-Define.CHS_SEff_TYPE_L);  
		if(seff_file == null){
			DataStruct.dbSEfFile_Table.insert(new SEFF_File(
        		"file_id",//file_id
        		mDSP_SData.Get_fileType(),//file_type
        		name,//file_name
        		filePath,//file_path
				"0",//file_favorite
				"0",//file_love
				"200",//file_size
				mDSP_SData.Get_data_info().Get_data_upload_time(),//file_time
				"file_msg",//file_msg
				
				dataInfo.Get_data_user_name(),//data_user_name
				dataInfo.Get_data_machine_type(),//data_machine_type
				dataInfo.Get_data_car_type(),//data_car_type
				dataInfo.Get_data_car_brand(),//data_car_brand
				dataInfo.Get_data_group_name(),//data_group_name
				dataInfo.Get_data_upload_time(),//data_upload_time
				dataInfo.Get_data_eff_briefing(),//data_eff_briefing
				
				"0",//list_sel
				"0"//list_is_open
			));
		}
    }
    /**
     * 

     */
    public static boolean SaveSingleJson2Local(
    		Context context,
    		String fineName,
    		DSP_SingleData sdata
    	) {

		//isGrantExternalRW((Activity)context);
    	boolean res = false;
		File cache = new File(Environment.getExternalStorageDirectory(),  MacCfg.AgentNAME);
		File destDir = new File(cache.toString()+
				"/"+MacCfg.Mac+
				"/"+"SoundEff"
				);
		System.out.println("BUG  是否存在filePath write:" + destDir.exists()+"=-="+destDir.getAbsolutePath());

		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		System.out.println("BUG  存在了:" + destDir.exists());
		String filePath = destDir.toString()+        		
        		"/"+fineName+Define.CHS_SEff_TYPE;
        System.out.println("BUG  filePath write:" + filePath);
        
        Gson gson = new Gson();
		try {
			FileWriter fw = new FileWriter(filePath);
		    PrintWriter out = new PrintWriter(fw);
		    out.write(gson.toJson(sdata));
		    out.println();
		    fw.close();
		    out.close();
		    //加密
		    if(Define.SEFFFILE_Encrypt){
		    	res = SeffFileCipherUtil.encrypt(filePath);
		    	checkAndAddDB(context,filePath);
		    }else{	
		    	res = true;
		    }		    
		} catch (Exception e) {
			res = false;
			e.printStackTrace();
		}
		return res;      
    }


    
    public static boolean SaveMACJson2Local(
    		Context context,
    		String fineName,
    		DSP_MACData sdata
    	) {


    	boolean res = false;
		File cache = new File(Environment.getExternalStorageDirectory(),  MacCfg.AgentNAME);
		File destDir = new File(cache.toString()+
				"/"+MacCfg.Mac+
				"/"+"SoundEff"
				);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		String filePath = destDir.toString()+        		
        		"/"+fineName+Define.CHS_SEff_MAC_TYPE;
        System.out.println("BUG  filePath write:" + filePath);
        
        Gson gson = new Gson();
		try {
			FileWriter fw = new FileWriter(filePath);
		    PrintWriter out = new PrintWriter(fw);
		    out.write(gson.toJson(sdata));
		    out.println();
		    fw.close();
		    out.close();
		    //加密
		    if(Define.SEFFFILE_Encrypt){		    	
		    	res = SeffFileCipherUtil.encrypt(filePath);
		    	checkAndAddDB(context,filePath);
		    }else{	
		    	res = true;
		    }		    
		} catch (Exception e) {
			res = false;
			e.printStackTrace();
		}
		return res;      
    }
    

    //把单组文件转化为数据结构
    public static DSP_SingleData LoadJsonLocal2DataStruce(Context context,String filePathLocalString) {
		DSP_SingleData sData = new DSP_SingleData();
		boolean res = false;
		int[] initsystem = new int[Define.IN_LEN];
		try {
			//解密
			if (Define.SEFFFILE_Encrypt) {
				res = SeffFileCipherUtil.decrypt(filePathLocalString);
				if (!res) {
					sData = null;
					return sData;
				}
				filePathLocalString += Define.CIPHER_TEXT_SUFFIX;

			}
			InputStream in = new FileInputStream(filePathLocalString);
			InputStreamReader is = new InputStreamReader(in, "UTF-8");


			BufferedReader br = new BufferedReader(is);
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}
			is.close();
			br.close();

			//删除临时文件
			if (Define.SEFFFILE_Encrypt) {
				File dir = new File(filePathLocalString);
				if (dir.isFile()) {
					dir.delete();
				} else {
					FileUtil.deleteDir(dir);
				}
			}
			// FileUtils.writeString2File(FileUtils.APP_LOG, "LoadPCSingle.txt", builder.toString());
			JSONObject root = new JSONObject(builder.toString());
			JSONObject objchs = root.getJSONObject("chs");
			sData.Set_chs(new Company(
					objchs.getString("company_name"),
					objchs.getString("company_tel"),
					objchs.getString("company_contacts"),
					objchs.getString("company_web"),
					objchs.getString("company_weixin"),
					objchs.getString("company_qq"),
					objchs.getString("company_briefing_en"),
					objchs.getString("company_briefing_zh"),
					objchs.getString("company_brand")
			));

			JSONObject objclient = root.getJSONObject("client");
			sData.Set_client(new Company(
					objclient.getString("company_name"),
					objclient.getString("company_tel"),
					objclient.getString("company_contacts"),
					objclient.getString("company_web"),
					objclient.getString("company_weixin"),
					objclient.getString("company_qq"),
					objclient.getString("company_briefing_en"),
					objclient.getString("company_briefing_zh"),
					objclient.getString("company_brand")
			));

			/**获取ID为data的数据*/
			JSONObject objdata = root.getJSONObject("data");
			DSP_Data dsp_Data = new DSP_Data();
			int[] initgroup_name = new int[16];
			JSONArray Arraygroup_name = objdata.getJSONArray("group_name");
			int len = Arraygroup_name.length();
			if (len > 16) {
				len = 16;
			}
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					initgroup_name[i] = Arraygroup_name.getInt(i) & 0xff;
				}
			}
			dsp_Data.Set_group_name(initgroup_name);


			/**获取音乐数据*/
			DSP_MusicData musicData = new DSP_MusicData();
			JSONObject objmusic = objdata.getJSONObject("music");
			JSONArray arraymusic = objmusic.getJSONArray("music");

			int[] initDatamusic = new int[Define.IN_LEN];
			int mch = arraymusic.length();
			if (mch > Define.MAX_CH) {
				mch = Define.MAX_CH;
			}

			if (mch > 0) {
				for (int i = 0; i < mch; i++) {
					JSONArray lan = arraymusic.getJSONArray(i);
					int cnt = lan.length();
					if (cnt > Define.IN_LEN) {
						cnt = Define.IN_LEN;
					}
					for (int j = 0; j < cnt; j++) {
						initDatamusic[j] = lan.getInt(j) & 0xff;
					}
					musicData.SetMusicData(i, initDatamusic);
				}
			}
			dsp_Data.Set_DSP_MusicData(musicData);
			DSP_OutputData outputData = new DSP_OutputData();
			JSONObject objoutput = objdata.getJSONObject("output");
			JSONArray arrayoutput = objoutput.getJSONArray("output");
			int[][] initDataoutput = new int[Define.MAX_CH][Define.OUT_LEN];
			System.out.println("BUG seff arrayoutput.length():" + arrayoutput.length());
			int och = arrayoutput.length();
			if (och > Define.MAX_CH) {
				och = Define.MAX_CH;
			}
			if (och > 0) {
				for (int i = 0; i < och; i++) {
					JSONArray lan = arrayoutput.getJSONArray(i);
					System.out.println("BUG seff arrayoutput[?].length():" + lan.length());
					int cnt = lan.length();
					if (cnt > Define.OUT_LEN) {
						cnt = Define.OUT_LEN;
					}
					for (int j = 0; j < cnt; j++) {
						initDataoutput[i][j] = lan.getInt(j) & 0xff;
					}
				}
			}
			for (int i = 0; i < och; i++) {
				outputData.SetOutputData(i, initDataoutput[i]);
			}
			dsp_Data.Set_DSP_OutputData(outputData);
			JSONObject objdata_info = root.getJSONObject("data_info");
			sData.Set_data_info(new DSP_DataInfo(
					objdata_info.getString("data_user_name"),
					objdata_info.getString("data_user_tel"),
					objdata_info.getString("data_user_mailbox"),
					objdata_info.getString("data_user_info"),
					objdata_info.getString("data_machine_type"),
					objdata_info.getString("data_car_type"),
					objdata_info.getString("data_car_brand"),
					objdata_info.getString("data_json_version"),
					objdata_info.getString("data_mcu_version"),
					objdata_info.getString("data_android_version"),
					objdata_info.getString("data_ios_version"),
					objdata_info.getString("data_pc_version"),
					objdata_info.getString("data_group_num"),
					objdata_info.getString("data_group_name"),
					objdata_info.getString("data_eff_briefing"),
					objdata_info.getString("data_upload_time"),
					objdata_info.getInt("data_encryption_byte"),
					objdata_info.getString("data_encryption_bool"),
					objdata_info.getInt("data_head_data")
			));
			sData.Set_data(dsp_Data);
 			
 			builder=null;
 			System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce OK !!!"+filePathLocalString);     
         } catch (UnsupportedEncodingException e) {  
        	 sData=null;
         	 System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce  UnsupportedEncodingException"+filePathLocalString);  
             e.printStackTrace();  
         } catch (IOException e) {  
        	 sData=null;
         	 System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce  IOException"+filePathLocalString);  
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 sData=null; 
         	 System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce  JSONException"+filePathLocalString);   
             e.printStackTrace();  
         }
    	return sData;
    }
    
    public static DSP_SingleData LoadJsonLocal2DSP_DataInfo(Context context,String filePathLocalString) {
    	DSP_SingleData sData=new DSP_SingleData();
    	 try {  
    		 if(Define.SEFFFILE_Encrypt){
    			 boolean res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 return null;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;
    			 
    		 }
    		 
    		 InputStream in = new FileInputStream(filePathLocalString);	        
             InputStreamReader is = new InputStreamReader(in, "UTF-8");  
             BufferedReader br = new BufferedReader(is);  
             String line;  
             StringBuilder builder = new StringBuilder();  
             while((line=br.readLine())!=null){  
                 builder.append(line);  
             }  
             is.close();
             br.close();
             
             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }
              
 			JSONObject root = new JSONObject(builder.toString());  
 			
 			sData.Set_fileType(root.getString("fileType"));
 			//System.out.println("VOT fileType:"+root.getString("fileType"));  
 			
 			//data_info
 			JSONObject objdata_info = root.getJSONObject("data_info");
 			sData.Set_data_info(new DSP_DataInfo(
				objdata_info.getString("data_user_name"),
				objdata_info.getString("data_user_tel"),
				objdata_info.getString("data_user_mailbox"),
				objdata_info.getString("data_user_info"),
				objdata_info.getString("data_machine_type"),
				objdata_info.getString("data_car_type"),
				objdata_info.getString("data_car_brand"),
				objdata_info.getString("data_json_version"),
				objdata_info.getString("data_mcu_version"),
				objdata_info.getString("data_android_version"),
				objdata_info.getString("data_ios_version"),
				objdata_info.getString("data_pc_version"),
				objdata_info.getString("data_group_num"),
				objdata_info.getString("data_group_name"),
				objdata_info.getString("data_eff_briefing"),
				objdata_info.getString("data_upload_time"),
				objdata_info.getInt("data_encryption_byte"),
				objdata_info.getString("data_encryption_bool"),
				objdata_info.getInt("data_head_data")
				));			 
 			builder=null;
			System.out.println("BUG DataInfo OK !!!"+filePathLocalString);    
         } catch (UnsupportedEncodingException e) {  
        	 sData=null;
         	 System.out.println("BUG DataInfo UnsupportedEncodingException"+filePathLocalString);    
             e.printStackTrace();  
         } catch (IOException e) {  
        	 sData=null;
         	 System.out.println("BUG DataInfo IOException"+filePathLocalString);   
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 sData=null; 
         	 System.out.println("BUG DataInfo JSONException"+filePathLocalString);    
             e.printStackTrace();  
         }
    	 
    	return sData;
    }
    
    /*
     * Get sound effects File type
     * return 0:error,1:single File, 2:MacType
     */
    public static int getSEFFFileType(Context context,String filePathLocalString) {
    	int ret = 0;
    	 try {  
    		 if(Define.SEFFFILE_Encrypt){
    			 boolean res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 return ret;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;    			 
    		 }
    		 
    		 InputStream in = new FileInputStream(filePathLocalString);	        
             InputStreamReader is = new InputStreamReader(in, "UTF-8");  
             BufferedReader br = new BufferedReader(is);  
             String line;  
             StringBuilder builder = new StringBuilder();  
             while((line=br.readLine())!=null){  
                 builder.append(line);  
             }  
             is.close();
             br.close();
             
             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }
              
 			JSONObject root = new JSONObject(builder.toString());  
 			
 			if(root.getString("fileType").equals(JsonDataSt.DSP_Single)){
 				return 1;
 			}else if(root.getString("fileType").equals(JsonDataSt.DSP_Complete)){
 				return 2;
 			}
 
 			builder=null;
			System.out.println("BUG DataInfo OK !!!"+filePathLocalString);    
			ret = 0;
         } catch (UnsupportedEncodingException e) {  
        	 ret = 0;
         	 System.out.println("BUG DataInfo UnsupportedEncodingException"+filePathLocalString);    
             e.printStackTrace();  
         } catch (IOException e) {  
        	 ret = 0;
         	 System.out.println("BUG DataInfo IOException"+filePathLocalString);   
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 ret = 0;
         	 System.out.println("BUG DataInfo JSONException"+filePathLocalString);    
             e.printStackTrace();  
         }
    	 
    	 return ret;
    }

    //把整机文件转化为结构体保存
    public static DSP_MACData loadMacDataJson2DataStruce(Context context,String filePathLocalString) {

//    	boolean res = false;
//    	int[] temp = new int[DataStruct.OUT_LEN];
    	DSP_MACData MAC_Data = new DSP_MACData();
    	System.out.println("BUG loadMacDataJson2DataStruce:"+filePathLocalString);
    	 try {
    		 //解密
    		 if(Define.SEFFFILE_Encrypt){
    			 boolean res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 return null;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;

    		 }

    		 InputStream in = new FileInputStream(filePathLocalString);
             InputStreamReader is = new InputStreamReader(in, "UTF-8");
             BufferedReader br = new BufferedReader(is);
             String line;
             StringBuilder builder = new StringBuilder();
             while((line=br.readLine())!=null){
                 builder.append(line);
             }
             is.close();
             br.close();

             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }

             JSONObject root = new JSONObject(builder.toString());
			 Iterator keys=root.keys();
			 while (keys.hasNext()){
				 System.out.println("BUG 所有的key"+String.valueOf(keys.next()));
			 }

 			 //CHS
 			 JSONObject objchs = root.getJSONObject("chs");
 			 MAC_Data.Set_chs(new Company(
				objchs.getString("company_name"),
				objchs.getString("company_tel"),
				objchs.getString("company_contacts"),
				objchs.getString("company_web"),
				objchs.getString("company_weixin"),
				objchs.getString("company_qq"),
				objchs.getString("company_briefing_en"),
				objchs.getString("company_briefing_zh"),
				objchs.getString("company_brand")
				));
 			 //client
 			 JSONObject objclient = root.getJSONObject("client");
 			 MAC_Data.Set_client(new Company(
				objclient.getString("company_name"),
				objclient.getString("company_tel"),
				objclient.getString("company_contacts"),
				objclient.getString("company_web"),
				objclient.getString("company_weixin"),
				objclient.getString("company_qq"),
				objclient.getString("company_briefing_en"),
				objclient.getString("company_briefing_zh"),
				objclient.getString("company_brand")
				));
 			 //client
 			 JSONObject objdata_info = root.getJSONObject("data_info");
 			 MAC_Data.Set_data_info(new DSP_DataInfo(
				objdata_info.getString("data_user_name"),
				objdata_info.getString("data_user_tel"),
				objdata_info.getString("data_user_mailbox"),
				objdata_info.getString("data_user_info"),
				objdata_info.getString("data_machine_type"),
				objdata_info.getString("data_car_type"),
				objdata_info.getString("data_car_brand"),
				objdata_info.getString("data_json_version"),
				objdata_info.getString("data_mcu_version"),
				objdata_info.getString("data_android_version"),
				objdata_info.getString("data_ios_version"),
				objdata_info.getString("data_pc_version"),
				objdata_info.getString("data_group_num"),
				objdata_info.getString("data_group_name"),
				objdata_info.getString("data_eff_briefing"),
				objdata_info.getString("data_upload_time"),
				objdata_info.getInt("data_encryption_byte"),
				objdata_info.getString("data_encryption_bool"),
				objdata_info.getInt("data_head_data")
				));

 			 System.out.println("BUG 加载系统数据");

 			 //所有的keychs
			 //12-03 10:31:54.342 7249-7249/com.chs.mt.pxe_r600 I/System.out: BUG 所有的keyclient
			 //12-03 10:31:54.342 7249-7249/com.chs.mt.pxe_r600 I/System.out: BUG 所有的keydata
			 //12-03 10:31:54.342 7249-7249/com.chs.mt.pxe_r600 I/System.out: BUG 所有的keydata_info
			 //12-03 10:31:54.342 7249-7249/com.chs.mt.pxe_r600 I/System.out: BUG 所有的keyfileType
			 //12-03 10:31:54.342 7249-7249/com.chs.mt.pxe_r600 I/System.out: BUG 所有的keysystem
			 //12-03 10:31:54.342 7249-7249/com.chs.mt.pxe_r600 I/System.out: BUG 加载系统数据

 			//system
			 JSONArray arraysystem_data = root.getJSONArray("system");
			 System.out.println("BUG 加载系统数据12");

			 if (arraysystem_data.length() > 0) {
				 for (int i = 0; i < arraysystem_data.length(); i++) {
					 MAC_Data.system[i]
							 = arraysystem_data.getInt(i) & 0xff;
				 }
			 }


 			//解释Data数据 暂时7组，0为当前组
 			JSONArray obj_data_Array = root.getJSONArray("data");
// 			DSP_DataMac mDSP_DataMac = new DSP_DataMac();
// 			DSP_MusicData musicData = new DSP_MusicData();
// 			DSP_OutputData outputData = new DSP_OutputData();
 			JSONObject data = null;
 			JSONArray Arraygroup_name = null;
 			JSONObject Object_music = null;
 			JSONObject Object_eff = null;
 			JSONObject Object_output = null;
 			JSONArray Array_music = null;
 			JSONArray Array_output = null;
 			JSONArray Array_eff = null;
 			int len = 0;
 			int lenD = 0;

 			int groupMax=obj_data_Array.length();
 			if(groupMax > (Define.MAX_UI_GROUP+1)){
 				groupMax = (Define.MAX_UI_GROUP+1);
 			}
 			if(groupMax > 0){
 				for(int i = 0; i < groupMax; i++){
 					data = obj_data_Array.getJSONObject(i);
 					if(data != null){
 						//获取用户组名字
 	 					Arraygroup_name = data.getJSONArray("group_name");
 	 					if(Arraygroup_name != null){
 	 						len = Arraygroup_name.length();
 	 						System.out.println("BUG MACSEFF Arraygroup_name.length()="+Arraygroup_name.length());
// 	 						System.out.println("BUG loadMacDataJson2DataStruce Arraygroup_name="+Arraygroup_name.toString());
 	 	 	 				if(len > 16){
 	 	 	 					len = 16;
 	 	 	 				}
 	 	 	 				if(len>0){
 	 	 	 	 				for(int j=0;j<len;j++){
 	 	 	 	 					MAC_Data.data[i].group_name[j]
 	 	 	 	 						= Arraygroup_name.getInt(j)&0xff;
 	 	 	 	 	 			}

 	 	 	 	 			}
 	 					}

 	 	 				//获取MUSIC
 	 					Object_music = data.getJSONObject("music");
 	 	 	 			if(Object_music != null){
 	 	 	 				Array_music = Object_music.getJSONArray("music");
	 	 	 	 			len = Array_music.length();
							System.out.println("BUG MACSEFF Array_music.length()="+Array_music.length());
							if(len > Define.MAX_CH){
	 	 	 					len = Define.MAX_CH;
	 	 	 				}
	 	 	 				for(int j=0;j<len;j++){
	 	 	 					JSONArray musicArray = Array_music.getJSONArray(j);
	 	 	 					lenD = musicArray.length();
								System.out.println("BUG MACSEFF Array_music.musicArray.length()="+musicArray.length());
	 	 	 	 				if(lenD > Define.IN_LEN){
	 	 	 	 					lenD = Define.IN_LEN;
	 	 	 	 				}
	 	 	 	 				for(int k=0;k<lenD;k++){
	 	 	 	 					MAC_Data.data[i].music.music[j][k]=musicArray.getInt(k)&0xff;
	 		 	 				}

	 	 	 				}
 	 	 	 			}


 	 	 				//获取Output
 	 	 	 			String st="ss{";
 	 	 	 			Object_output = data.getJSONObject("output");
 	 	 	 			if(Object_output != null){
 	 	 	 				Array_output = Object_output.getJSONArray("output");
	 	 	 	 			lenD = 0;
	 	 	 	 			len = Array_output.length();
							System.out.println("BUG MACSEFF Array_output.length()="+Array_output.length());
	 	 	 				if(len > Define.MAX_CH){
	 	 	 					len = Define.MAX_CH;
	 	 	 				}

	 	 	 				for(int j=0;j<len;j++){
	 	 	 					JSONArray outputArray = Array_output.getJSONArray(j);
	 	 	 					lenD = outputArray.length();
								System.out.println("BUG MACSEFF Array_output.outputArray.length()="+outputArray.length());
								if(lenD > Define.OUT_LEN){
	 	 	 	 					lenD = Define.OUT_LEN;
	 	 	 	 				}
//	 	 	 	 				st="ss{";
	 	 	 	 				for(int k=0;k<lenD;k++){
	 	 	 	 					MAC_Data.data[i].output.output[j][k] = outputArray.getInt(k)&0xff;
//	 	 	 	 					st+=(String.valueOf(MAC_Data.data[i].output.output[j][k])+",");
	 		 	 				}
	 	 	 	 				//System.out.println("BUG loadMacDataJson2DataStruce st="+st);
	 	 	 	 				//System.out.println("BUG loadMacDataJson2DataStruce output i="+i);
	 	 	 				}
 	 	 	 			}
 					}
 				}
 			}
 			System.out.println("BUG 完成");
 			//printDatad(MAC_Data);
 			//printData();
 			//res = true;
 			System.out.println("BUG loadMacDataJson2DataStruce OK !!!"+filePathLocalString);
    	 } catch (UnsupportedEncodingException e) {
        	 //res = false;
    		 MAC_Data = null;
         	 System.out.println("BUG loadMacDataJson2DataStruce  UnsupportedEncodingException"+filePathLocalString);
             e.printStackTrace();
         } catch (IOException e) {
        	 //res = false;
        	 MAC_Data = null;
         	 System.out.println("BUG loadMacDataJson2DataStruce  IOException"+filePathLocalString);
             e.printStackTrace();
         }catch (JSONException e) {
        	 //res = false;
        	 MAC_Data = null;
         	 System.out.println("BUG loadMacDataJson2DataStruce  JSONException"+filePathLocalString);
             e.printStackTrace();
         }
    	 return MAC_Data;
    }
    
    private void printData(){
    	String st="ss{";
    	for(int i=0;i<=Define.MAX_GROUP;i++){
    		System.out.println("BUG loadMacDataJson2DataStruce GGGGGG Group="+i);   
    		for(int j=0;j<Define.MAX_CH;j++){
    			System.out.println("BUG loadMacDataJson2DataStruce CCCCC channel="+j); 
    			st="ss{";
    			for(int k=0;k<Define.OUT_LEN;k++){
    				st+=(String.valueOf(DataStruct.RcvDeviceData.MAC_Data.data[i].output.output[j][k])+",");
        		}
    			System.out.println("BUG loadMacDataJson2DataStruce st="+st);
    		}
    	}
    }
    private void printDatad(DSP_MACData MAC_Data){
    	String st="ss{";
    	for(int i=0;i<=Define.MAX_GROUP;i++){
    		System.out.println("BUG loadMacDataJson2DataStruce GGGGGG Group="+i);   
    		for(int j=0;j<Define.MAX_CH;j++){
    			System.out.println("BUG loadMacDataJson2DataStruce CCCCC channel="+j); 
    			st="ss{";
    			for(int k=0;k<Define.OUT_LEN;k++){
    				st+=(String.valueOf(MAC_Data.data[i].output.output[j][k])+",");
        		}
    			System.out.println("BUG loadMacDataJson2DataStruce st="+st);
    		}
    	}
    }
    //打开目录的音效文件
    public void OpenSoundSEFFDir(Context context, String fileType) {
    	String filePath = Environment.getExternalStorageDirectory()+
			"/"+MacCfg.AgentNAME+
			"/"+MacCfg.Mac+
			"/"+"SoundEff"+
			"/"+"Single"+
			"/"+getAppInfo(context)+fileType+String.valueOf(1)+Define.CHS_SEff_TYPE;//
    	Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath)), "text/*");
		context.startActivity(intent);
	}
    
    //分享音效文件，固定目录，参数音效文件名字,fileType 0:单纯组文件，1：整机文件
    public static void ShareSoundEFFData(Context mcontext, String finename, int fileType) {
		String filePath = "";
		if (fileType == 0) {
			filePath = Environment.getExternalStorageDirectory() +
					"/" + MacCfg.AgentNAME +
					"/" + MacCfg.Mac +
					"/" + "SoundEff" +
					"/" + finename + Define.CHS_SEff_TYPE;//
		} else if (fileType == 1) {
			filePath = Environment.getExternalStorageDirectory() +
					"/" + MacCfg.AgentNAME +
					"/" + MacCfg.Mac +
					"/" + "SoundEff" +
					"/" + finename + Define.CHS_SEff_MAC_TYPE;//
		}
		try {
			Intent intent = new Intent();
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("*/");  //message audio video image application  java
			Uri fileUri = null;
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction("android.intent.action.SEND");
			intent.setType("text/plain");  //加上这句可以使文件分享到微信中去  //message audio video image application  java
//		intent.putExtra(Intent.EXTRA_TEXT,"TEXT");
			if (Build.VERSION.SDK_INT <= 23) {
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
			} else {
				intent.putExtra(Intent.EXTRA_STREAM,
						getImageContentUri(mcontext, new File(filePath)));
			}
			mcontext.startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
		}


//		try {
//			if (Build.VERSION.SDK_INT <= 20) {
//				fileUri=Uri.fromFile(new File(filePath));  //直接获取本地的地址
//			}else {
//				//fileUri = FileProvider.getUriForFile(mcontext, "com.chs.mt.pxe_r600.fileprovider", (new File(filePath)));
//				fileUri=getImageContentUri(mcontext, new File(filePath));
////				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
//
//
//
//				System.out.println("BUG ===="+fileUri);
//			}
//			System.out.println("BUG 最后地址的值为" + fileUri);
//			// intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
//
//			intent.putExtra(Intent.EXTRA_STREAM, fileUri);  //设置是分享文件 Intent.EXTRA_STREAM
//			intent.setType("text/plain");  //加上这句可以使文件分享到微信中去
//			mcontext.startActivity(intent);
//			//mcontext.startActivity(Intent.createChooser(intent,  mcontext.getResources().getString(R.string.share_seff)));
//		} catch (Exception e) {
//			System.out.println("BUG 最后的值为" + e);
//			e.printStackTrace();
//		}

//		String filePath="";
//
//		if(fileType == 0){
//			filePath = Environment.getExternalStorageDirectory()+
//					"/"+MacCfg.AgentNAME+
//					"/"+MacCfg.Mac+
//					"/"+"SoundEff"+
//					"/"+finename+Define.CHS_SEff_TYPE;//
//
//		}else if(fileType == 1){
//			filePath = Environment.getExternalStorageDirectory()+
//					"/"+MacCfg.AgentNAME+
//					"/"+MacCfg.Mac+
//					"/"+"SoundEff"+
//					"/"+finename+Define.CHS_SEff_MAC_TYPE;//
//		}
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction("android.intent.action.SEND");
//		intent.setType("application/*");  //message audio video image application  java
////		intent.putExtra(Intent.EXTRA_TEXT,"TEXT");
////intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
//		if (Build.VERSION.SDK_INT <= 20){
//			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
//		}else {
//			intent.putExtra(Intent.EXTRA_STREAM,
//					FileProvider.getUriForFile(context,
//							context.getApplicationContext().getPackageName() + ".provider", new File(filePath)));
//		}
//		context.startActivity(intent);

    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        Uri uri = null;

		System.out.println("BUG 游标 cursor"+cursor);
        if (cursor != null) {
			System.out.println("BUG 游标 111111111"+cursor.moveToFirst());
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
           }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }

	//strFile 为文件名称 返回true为存在
	public boolean fileIsExists(String strFile) {
		try {
			File f=new File(strFile);
			if(f.exists()) {
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
    
	//分享音效文件，参数音效文件路径(分享本地的文件)
    public void ShareLocalSoundEFFData(Context context, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("*/");  //message audio video image application  java
        Uri fileUri = null;
        try {
            if (Build.VERSION.SDK_INT < 24) {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));  //直接获取本地的地址
            }else {
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
						Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

				intent.putExtra(Intent.EXTRA_STREAM,
						getImageContentUri(context, new File(filePath)));
            // fileUri = FileProvider.getUriForFile(context, "com.chs.mt.pxe_r500.fileprovider", (new File(filePath)));
                //fileUri=getImageContentUri(context, new File(filePath));
             //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
            }
            System.out.println("BUG 最后地址的值为" + fileIsExists(String.valueOf(fileUri)));
            // intent.setDataAndType(fileUri, "application/vnd.android.package-archive");

            //intent.putExtra(Intent.EXTRA_STREAM, fileUri);  //设置是分享文件 Intent.EXTRA_STREAM
            intent.setType("text/plain");  //加上这句可以使文件分享到微信中去
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_seff)));
        } catch (Exception e) {
            System.out.println("BUG 最后的值为" + e);
            e.printStackTrace();
        }

	}

    public void parseVerderOptionJson(Context context){  
        try {  
            InputStreamReader is = new InputStreamReader(context.getAssets().open("VerderOptionV9.json"), "UTF-8");  
            BufferedReader br = new BufferedReader(is);  
            String line;  
            StringBuilder builder = new StringBuilder();  
            while((line=br.readLine())!=null){  
                builder.append(line);  
            }  
            is.close();
            br.close();
            
			JSONObject root = new JSONObject(builder.toString());  
			//System.out.println("VOT code:"+root.getString("code"));  
			//System.out.println("VOT message:"+root.getString("message"));  
			
			JSONObject objdata = root.getJSONObject("data");
			//System.out.println("VOT types:"+objdata.getString("types"));  
			
			JSONArray arraybrands = objdata.getJSONArray("brands");  
			JSONArray arraycartpyes = objdata.getJSONArray("cartpyes");  
			JSONArray arraymacs = objdata.getJSONArray("macs");  
			JSONArray arraymacsAgentName = objdata.getJSONArray("macsAgentName");  
			
			//System.out.println("VOT arraybrands.length():"+arraybrands.length());  
			//System.out.println("VOT arraycartpyes.length():"+arraycartpyes.length());  
			//System.out.println("VOT arraymacs.length():"+arraymacs.length()); 
			//List_CarBrands
			for(int i=0;i<arraybrands.length();i++){  
				JSONObject lan = arraybrands.getJSONObject(i);  
//				DataStruct.venderOption.List_CarBrands.add(new CarBrands(
//					lan.getString(CarBrands.T_CID),
//					lan.getString(CarBrands.T_NAME),
//					lan.getString(CarBrands.T_IMG_PATH)
//				));
				
				DataStruct.dbCarBrands_Table.insert(new CarBrands(
					lan.getString(CarBrands.T_CID),
					lan.getString(CarBrands.T_NAME),
					lan.getString(CarBrands.T_IMG_PATH)
    			));
			}  
			//arraycartpyes
			for(int i=0;i<arraycartpyes.length();i++){  
				JSONObject lan = arraycartpyes.getJSONObject(i);  
//				DataStruct.venderOption.List_CarTypes.add(new CarTypes(
//					lan.getString(CarTypes.T_CID),
//					lan.getString(CarTypes.T_BRAND_ID),
//					lan.getString(CarTypes.T_NAME),
//					lan.getString(CarTypes.T_IMG_PATH)
//				));
				
				DataStruct.dbCarTypes_Table.insert(new CarTypes(
					lan.getString(CarTypes.T_CID),
					lan.getString(CarTypes.T_BRAND_ID),
					lan.getString(CarTypes.T_NAME),
					lan.getString(CarTypes.T_IMG_PATH)
    			));
			} 
			
			//arraymacs
			for(int i=0;i<arraymacs.length();i++){  
				JSONObject lan = arraymacs.getJSONObject(i);  
//				DataStruct.venderOption.List_MacTypes.add(new MacTypes(
//					lan.getString(MacTypes.T_CID),
//					lan.getString(MacTypes.T_NAME)
//				));
				DataStruct.dbMacTypes_Table.insert(new MacTypes(
					lan.getString(MacTypes.T_CID),
					lan.getString(MacTypes.T_NAME)
    			));
			} 
			
			//macsAgentName
			for(int i=0;i<arraymacsAgentName.length();i++){  
				JSONObject lan = arraymacsAgentName.getJSONObject(i);  
				DataStruct.dbMacsAgentName_Table.insert(new MacsAgentName(
					lan.getString(MacsAgentName.T_CID),
					lan.getString(MacsAgentName.T_MID),
					lan.getString(MacsAgentName.T_AgentID),
					lan.getString(MacsAgentName.T_CNAME)
    			));
			} 
			
			System.out.println("VOT Update VerderOption OK !!!");  
//			System.out.println("VOT DataStruct.venderOption.List_CarBrands.size():"
//					+DataStruct.venderOption.List_CarBrands.size());  
//			System.out.println("VOT DataStruct.venderOption.List_CarTypes.size():"
//					+DataStruct.venderOption.List_CarTypes.size());  
//			System.out.println("VOT DataStruct.venderOption.List_MacTypes.size():"
//					+DataStruct.venderOption.List_MacTypes.size());  

        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT UnsupportedEncodingException");  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT IOException");  
            e.printStackTrace();  
        }catch (JSONException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT JSONException");  
            e.printStackTrace();  
        }  
    }
    public static void parseLocalVerderOptionJson(Context context){
    	String appName=getAppInfo(context);
		File file = new File(Environment.getExternalStorageDirectory(),
				MacCfg.AgentNAME+"/"+MacCfg.Mac+"/"+DataStruct.venderOption.Get_lastversion()+".json");
		System.out.println("BUG  chs_file.toString"+file.toString());

        try {  
        	InputStreamReader is = new InputStreamReader(   
                    new FileInputStream(file), "UTF-8");  

            BufferedReader br = new BufferedReader(is);  
            String line;  
            StringBuilder builder = new StringBuilder();  
            while((line=br.readLine())!=null){  
                builder.append(line);  
            }  
            is.close();
            br.close();
            
			JSONObject root = new JSONObject(builder.toString());  
			//System.out.println("VOT code:"+root.getString("code"));  
			//System.out.println("VOT message:"+root.getString("message"));  
			
			JSONObject objdata = root.getJSONObject("data");
			//System.out.println("VOT types:"+objdata.getString("types"));  
			
			JSONArray arraybrands = objdata.getJSONArray("brands");  
			JSONArray arraycartpyes = objdata.getJSONArray("cartpyes");  
			JSONArray arraymacs = objdata.getJSONArray("macs");  
			JSONArray arraymacsAgentName = objdata.getJSONArray("macsAgentName");  
			
			//System.out.println("VOT arraybrands.length():"+arraybrands.length());  
			//System.out.println("VOT arraycartpyes.length():"+arraycartpyes.length());  
			//System.out.println("VOT arraymacs.length():"+arraymacs.length()); 
			//List_CarBrands
			for(int i=0;i<arraybrands.length();i++){  
				JSONObject lan = arraybrands.getJSONObject(i);  
//				DataStruct.venderOption.List_CarBrands.add(new CarBrands(
//					lan.getString(CarBrands.T_CID),
//					lan.getString(CarBrands.T_NAME),
//					lan.getString(CarBrands.T_IMG_PATH)
//				));
				
				DataStruct.dbCarBrands_Table.insert(new CarBrands(
					lan.getString(CarBrands.T_CID),
					lan.getString(CarBrands.T_NAME),
					lan.getString(CarBrands.T_IMG_PATH)
    			));
			}  
			//arraycartpyes
			for(int i=0;i<arraycartpyes.length();i++){  
				JSONObject lan = arraycartpyes.getJSONObject(i);  
//				DataStruct.venderOption.List_CarTypes.add(new CarTypes(
//					lan.getString(CarTypes.T_CID),
//					lan.getString(CarTypes.T_BRAND_ID),
//					lan.getString(CarTypes.T_NAME),
//					lan.getString(CarTypes.T_IMG_PATH)
//				));
				
				DataStruct.dbCarTypes_Table.insert(new CarTypes(
					lan.getString(CarTypes.T_CID),
					lan.getString(CarTypes.T_BRAND_ID),
					lan.getString(CarTypes.T_NAME),
					lan.getString(CarTypes.T_IMG_PATH)
    			));
			} 
			
			//arraymacs
			for(int i=0;i<arraymacs.length();i++){  
				JSONObject lan = arraymacs.getJSONObject(i);  
//				DataStruct.venderOption.List_MacTypes.add(new MacTypes(
//					lan.getString(MacTypes.T_CID),
//					lan.getString(MacTypes.T_NAME)
//				));
				DataStruct.dbMacTypes_Table.insert(new MacTypes(
					lan.getString(MacTypes.T_CID),
					lan.getString(MacTypes.T_NAME)
    			));
			} 
			
			//macsAgentName
			System.out.println("BUG  arraymacsAgentName.length():"+arraymacsAgentName.length());
			for(int i=0;i<arraymacsAgentName.length();i++){  
				JSONObject lan = arraymacsAgentName.getJSONObject(i);  
				DataStruct.dbMacsAgentName_Table.insert(new MacsAgentName(
					lan.getString(MacsAgentName.T_CID),
					lan.getString(MacsAgentName.T_MID),
					lan.getString(MacsAgentName.T_AgentID),
					lan.getString(MacsAgentName.T_CNAME)
    			));
//				System.out.println("BUG  arraymacsAgentName.T_CID():"+lan.getString(MacsAgentName.T_CID));
//				System.out.println("BUG  arraymacsAgentName.T_MID():"+lan.getString(MacsAgentName.T_MID));
//				System.out.println("BUG  arraymacsAgentName.T_AgentID():"+lan.getString(MacsAgentName.T_AgentID));
//				System.out.println("BUG  arraymacsAgentName.T_CNAME():"+lan.getString(MacsAgentName.T_CNAME));
			} 
			
			
			System.out.println("BUG Update VerderOption OK !!!");  
//			System.out.println("VOT DataStruct.venderOption.List_CarBrands.size():"
//					+DataStruct.venderOption.List_CarBrands.size());  
//			System.out.println("VOT DataStruct.venderOption.List_CarTypes.size():"
//					+DataStruct.venderOption.List_CarTypes.size());  
//			System.out.println("VOT DataStruct.venderOption.List_MacTypes.size():"
//					+DataStruct.venderOption.List_MacTypes.size());  

        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT UnsupportedEncodingException");  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT IOException");  
            e.printStackTrace();  
        }catch (JSONException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT JSONException");  
            e.printStackTrace();  
        }  
    }

}
