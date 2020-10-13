package com.chs.mt.pxe_r500.operation;

import android.content.Context;

import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/8.
 */

public class mOKhttpUtil {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    //Http Get
    public static void registerMobilePhoneInformation(Context mContext){
        if(!PhoneInfoUtil.getHoneInfo()){
            //return;
        }

        String URL="http://crm.chschs.com/index.php?m=Index&a=GetPC_User_Message" +
                "&AgentID="+ MacCfg.AgentID+
                "&softtype="+"2"+//软件类型：1为苹果、2为安卓、3为PC、0为WEB端
                "&macName="+MacCfg.Mac_type+
                "&macAddr="+MacCfg.PhoneMAC+
                "&clientModel="+MacCfg.PhoneName+
                "&osType="+"Andorid-"+MacCfg.PhoneOS+"-"+MacCfg.PhoneOS_Mode+
                "&SoftVersion="+MacCfg.App_versions+
                "&PhoneNumber="+PhoneInfoUtil.getPhoneNumber(mContext);
        //创建一个Request
        System.out.println("BUG  RegisterMobilePhoneInformation URL="+URL);
        final Request request = new Request.Builder()
                .url(URL)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String htmlStr =  response.body().string();
                System.out.println("BUG  RegisterMobilePhoneInformation htmlStr="+htmlStr);
                parseJson2_DSP_AI(htmlStr);
            }
        });
    }

    private static void parseJson2_DSP_AI(String rootSt){
        try {
            JSONObject root = new JSONObject(rootSt);
            DataStruct.mDSPAi.code = root.getString("code");
            DataStruct.mDSPAi.message = root.getString("message");

            JSONObject objdata = root.getJSONObject("data");

            DataStruct.mDSPAi.AgentID = objdata.getString("AgentID");
            DataStruct.mDSPAi.softtype = objdata.getString("softtype");
            DataStruct.mDSPAi.macName = objdata.getString("macName");
//            DataStruct.mDSPAi.macID = objdata.getString("macID");
            DataStruct.mDSPAi.ip = objdata.getString("ip");
            DataStruct.mDSPAi.ctime = objdata.getString("ctime");

            DataStruct.mDSPAi.Ad_Status = objdata.getString("Ad_Status");
            if(DataStruct.mDSPAi.Ad_Status.equals("1")){
                DataStruct.mDSPAi.Ad_Title = objdata.getString("Ad_Title");
                DataStruct.mDSPAi.Ad_Image_Path = objdata.getString("Ad_Image_Path");
                DataStruct.mDSPAi.Ad_URL = objdata.getString("Ad_URL");
                DataStruct.mDSPAi.Ad_Close_URL = objdata.getString("Ad_Close_URL");
                DataStruct.mDSPAi.AdID = objdata.getString("AdID");
            }

            DataStruct.mDSPAi.Upgrade_Status = objdata.getString("Upgrade_Status");
            if(DataStruct.mDSPAi.Upgrade_Status.equals("1")){
                DataStruct.mDSPAi.Upgrade_Instructions = objdata.getString("Upgrade_Instructions");
                DataStruct.mDSPAi.Upgrade_Latest_Version = objdata.getString("Upgrade_Latest_Version");
                DataStruct.mDSPAi.Upgrade_URL = objdata.getString("Upgrade_URL");
            }
            //System.out.println("BUG parseJson2_DSP_AI -OK!");
            printMsg_DSPAI();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("BUG parseJson2_DSP_AI -JSONException");
            e.printStackTrace();
        }
    }

    private static void printMsg_DSPAI(){
        System.out.println("BUG DataStruct.mDSPAi.code="+DataStruct.mDSPAi.code);
        System.out.println("BUG DataStruct.mDSPAi.message="+DataStruct.mDSPAi.message);

        System.out.println("BUG DataStruct.mDSPAi.AgentID="+DataStruct.mDSPAi.AgentID);
        System.out.println("BUG DataStruct.mDSPAi.softtype="+DataStruct.mDSPAi.softtype);
        System.out.println("BUG DataStruct.mDSPAi.macName="+DataStruct.mDSPAi.macName);
        System.out.println("BUG DataStruct.mDSPAi.ip="+DataStruct.mDSPAi.ip);
        System.out.println("BUG DataStruct.mDSPAi.ctime="+DataStruct.mDSPAi.ctime);

        System.out.println("BUG DataStruct.mDSPAi.Ad_Status="+DataStruct.mDSPAi.Ad_Status);
        System.out.println("BUG DataStruct.mDSPAi.AdID="+DataStruct.mDSPAi.AdID);
        System.out.println("BUG DataStruct.mDSPAi.Ad_Title="+DataStruct.mDSPAi.Ad_Title);
        System.out.println("BUG DataStruct.mDSPAi.Ad_Image_Path="+DataStruct.mDSPAi.Ad_Image_Path);
        System.out.println("BUG DataStruct.mDSPAi.Ad_URL="+DataStruct.mDSPAi.Ad_URL);
        System.out.println("BUG DataStruct.mDSPAi.Ad_Close_URL="+DataStruct.mDSPAi.Ad_Close_URL);

        System.out.println("BUG DataStruct.mDSPAi.Upgrade_Status="+DataStruct.mDSPAi.Upgrade_Status);
        System.out.println("BUG DataStruct.mDSPAi.Upgrade_Instructions="+DataStruct.mDSPAi.Upgrade_Instructions);
        System.out.println("BUG DataStruct.mDSPAi.Upgrade_Latest_Version="+DataStruct.mDSPAi.Upgrade_Latest_Version);
        System.out.println("BUG DataStruct.mDSPAi.Upgrade_URL="+DataStruct.mDSPAi.Upgrade_URL);

    }

    public static void getURL(String URL){
        //创建一个Request
        System.out.println("BUG  getURL URL="+URL);
        final Request request = new Request.Builder()
                .url(URL)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                //String htmlStr =  response.body().string();
                //System.out.println("BUG  getURL htmlStr="+htmlStr);
            }
        });

    }

}
