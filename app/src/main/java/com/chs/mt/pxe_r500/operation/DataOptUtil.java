package com.chs.mt.pxe_r500.operation;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.bean.Company;
import com.chs.mt.pxe_r500.bean.DSP_Data;
import com.chs.mt.pxe_r500.bean.DSP_DataInfo;
import com.chs.mt.pxe_r500.bean.DSP_DataMac;
import com.chs.mt.pxe_r500.bean.DSP_MACData;
import com.chs.mt.pxe_r500.bean.DSP_MusicData;
import com.chs.mt.pxe_r500.bean.DSP_OutputData;
import com.chs.mt.pxe_r500.bean.DSP_SingleData;
import com.chs.mt.pxe_r500.bean.DSP_SystemData;
import com.chs.mt.pxe_r500.bean.ImageUrlList;
import com.chs.mt.pxe_r500.bean.SEFF_File;
import com.chs.mt.pxe_r500.bean.User;
import com.chs.mt.pxe_r500.bean.VenderOption;
import com.chs.mt.pxe_r500.datastruct.Data;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.DataStruct_System;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.db.CarBrands_Table;
import com.chs.mt.pxe_r500.db.CarTypes_Table;
import com.chs.mt.pxe_r500.db.DB_LoginSM_Table;
import com.chs.mt.pxe_r500.db.DB_MFileList_Table;
import com.chs.mt.pxe_r500.db.DB_MMusicList_Table;
import com.chs.mt.pxe_r500.db.DB_SEffData_Table;
import com.chs.mt.pxe_r500.db.DB_SEffFile_Recently_Table;
import com.chs.mt.pxe_r500.db.DB_SEffFile_Table;
import com.chs.mt.pxe_r500.db.DataBaseCCMHelper;
import com.chs.mt.pxe_r500.db.DataBaseMusicHelper;
import com.chs.mt.pxe_r500.db.DataBaseOpenHelper;
import com.chs.mt.pxe_r500.db.MacTypes_Table;
import com.chs.mt.pxe_r500.db.MacsAgentName_Table;
import com.chs.mt.pxe_r500.mac.MacModeInit;
import com.chs.mt.pxe_r500.mac.MacModeInitData.Mac_H812;
import com.chs.mt.pxe_r500.mac.bean.MacMode;
import com.chs.mt.pxe_r500.mac.bean.MacModeArt;
import com.chs.mt.pxe_r500.service.ServiceOfCom;

import java.io.File;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.chs.mt.pxe_r500.datastruct.MacCfg.OutputChannelSel;

/**
 * Created by Administrator on 2017/6/5.
 */

public class DataOptUtil {
    public static final int NO = 0;  // 定义静态变量
    public static final int YES = 1; // 定义静态变量
    public static boolean DEBUG = false;

    private static Toast mToast;

    public static void InitApp(Context mContext) {
        MacCfg.BOOL_CanLinkUART = false;
        MacCfg.BOOL_FirstStart = false;
        DataStruct.RcvDeviceData = new Data();
        DataStruct.SendDeviceData = new Data();
        DataStruct.DefaultDeviceData = new Data();
        DataStruct.BufDeviceData = new Data();

        DataStruct.MacModeAll = new MacMode();
        DataStruct.CurMacMode = new MacModeArt();

        DataStruct.user = new User();
        DataStruct.userSeffTemp = new User();
        DataStruct.venderOption = new VenderOption();
        DataStruct.HomeImageUrlList = new ImageUrlList();

        DataStruct.mDSP_MusicData = new DSP_MusicData();
        DataStruct.mDSP_OutputData = new DSP_OutputData();
        DataStruct.mDSP_DataMac = new DSP_DataMac();
        DataStruct.MAC_DataBuf = new DSP_MACData();

        /*获取屏幕大小，并设置相关窗口大小*/
        GetScreenSizeAndSetPopWindow(mContext);
        initDatabases(mContext);
        //scanDirAsync(mContext);
        MacModeInit.initMacModeAllArt(mContext);

        DataStruct.CurMusic.resetData();

        DataStruct.FileList.clear();
        DataStruct.MusicList.clear();
        DataStruct.FileList.removeAll(DataStruct.FileList);
        DataStruct.MusicList.removeAll(DataStruct.MusicList);
    }

    public static void ToastMsg(String Msg, Context mContext) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    private static void setDefault(int i) {
        DataStruct.DefaultDeviceData.OUT_CH[i].mute = DataStruct.RcvDeviceData.OUT_CH[i].mute;
        DataStruct.DefaultDeviceData.OUT_CH[i].polar = DataStruct.RcvDeviceData.OUT_CH[i].polar;
        DataStruct.DefaultDeviceData.OUT_CH[i].gain = DataStruct.RcvDeviceData.OUT_CH[i].gain;
        DataStruct.DefaultDeviceData.OUT_CH[i].delay = DataStruct.RcvDeviceData.OUT_CH[i].delay;
        DataStruct.DefaultDeviceData.OUT_CH[i].eq_mode = DataStruct.RcvDeviceData.OUT_CH[i].eq_mode;
        DataStruct.DefaultDeviceData.OUT_CH[i].LinkFlag = DataStruct.RcvDeviceData.OUT_CH[i].LinkFlag;

        DataStruct.DefaultDeviceData.OUT_CH[i].h_freq = DataStruct.RcvDeviceData.OUT_CH[i].h_freq;
        DataStruct.DefaultDeviceData.OUT_CH[i].h_filter = DataStruct.RcvDeviceData.OUT_CH[i].h_filter;

        DataStruct.DefaultDeviceData.OUT_CH[i].h_level = DataStruct.RcvDeviceData.OUT_CH[i].h_level;
        // System.out.println("BUG 这里的高通的只为"+DataStruct.RcvDeviceData.OUT_CH[i].h_level);
        DataStruct.DefaultDeviceData.OUT_CH[i].l_freq = DataStruct.RcvDeviceData.OUT_CH[i].l_freq;
        DataStruct.DefaultDeviceData.OUT_CH[i].l_filter = DataStruct.RcvDeviceData.OUT_CH[i].l_filter;
        DataStruct.DefaultDeviceData.OUT_CH[i].l_level = DataStruct.RcvDeviceData.OUT_CH[i].l_level;
    }

    /**
     * 设置通道类型之后其他通道数据跟着改变
     */
    public static void setOutputType(int outPut, int Type) {
        // int otherType=0;
        if (Type != 0) {
            if (Type <= 6) {
                Type += 6;
            } else if (Type >= 13 && Type <= 15) {
                Type += 3;
            } else if (Type == 22 || Type == 25 || Type == 27 || Type == 30) {
                Type += 1;
            }else{
                return;
            }
            // System.out.println("BUG 通道类型为"+otherType);

            if ((outPut + 1) < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE) {
                for (int j = 0; j < Define.MAX_CHEQ; j++) {
                    DataStruct.RcvDeviceData.OUT_CH[outPut + 1].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[outPut].EQ[j].freq;
                    DataStruct.RcvDeviceData.OUT_CH[outPut + 1].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[outPut].EQ[j].level;
                    DataStruct.RcvDeviceData.OUT_CH[outPut + 1].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[outPut].EQ[j].bw;
                    DataStruct.RcvDeviceData.OUT_CH[outPut + 1].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[outPut].EQ[j].shf_db;
                    DataStruct.RcvDeviceData.OUT_CH[outPut + 1].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[outPut].EQ[j].type;
                }

                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].mute = DataStruct.RcvDeviceData.OUT_CH[outPut].mute;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].polar = DataStruct.RcvDeviceData.OUT_CH[outPut].polar;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].gain = DataStruct.RcvDeviceData.OUT_CH[outPut].gain;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].eq_mode = DataStruct.RcvDeviceData.OUT_CH[outPut].eq_mode;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].h_freq = DataStruct.RcvDeviceData.OUT_CH[outPut].h_freq;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].h_filter = DataStruct.RcvDeviceData.OUT_CH[outPut].h_filter;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].h_level = DataStruct.RcvDeviceData.OUT_CH[outPut].h_level;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].l_freq = DataStruct.RcvDeviceData.OUT_CH[outPut].l_freq;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].l_filter = DataStruct.RcvDeviceData.OUT_CH[outPut].l_filter;
                DataStruct.RcvDeviceData.OUT_CH[outPut + 1].l_level = DataStruct.RcvDeviceData.OUT_CH[outPut].l_level;

                setOutputSpk(Type, outPut + 1);
                SetInputSourceMixerVol(outPut + 1);

            }

        }


        // return otherType;

    }


    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }


    /**
     * 初始结构体
     *
     * @param mContext
     */
    public static void InitDataStruct(Context mContext) {

        for (int i = 0; i < Define.MAX_CH; i++) {
            for (int j = 0; j < Define.MAX_CHEQ; j++) {
                DataStruct.BufDeviceData.OUT_CH[i].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].freq;
                DataStruct.BufDeviceData.OUT_CH[i].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].level;
                DataStruct.BufDeviceData.OUT_CH[i].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].bw;
                DataStruct.BufDeviceData.OUT_CH[i].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].shf_db;
                DataStruct.BufDeviceData.OUT_CH[i].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].type;

                DataStruct.DefaultDeviceData.OUT_CH[i].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].freq;
                DataStruct.DefaultDeviceData.OUT_CH[i].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].level;
                DataStruct.DefaultDeviceData.OUT_CH[i].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].bw;
                DataStruct.DefaultDeviceData.OUT_CH[i].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].shf_db;
                DataStruct.DefaultDeviceData.OUT_CH[i].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].type;
            }

            //   setDefault(i);

            DataStruct.BufDeviceData.OUT_CH[i].mute = DataStruct.RcvDeviceData.OUT_CH[i].mute;
            DataStruct.BufDeviceData.OUT_CH[i].polar = DataStruct.RcvDeviceData.OUT_CH[i].polar;
            DataStruct.BufDeviceData.OUT_CH[i].gain = DataStruct.RcvDeviceData.OUT_CH[i].gain;
            DataStruct.BufDeviceData.OUT_CH[i].delay = DataStruct.RcvDeviceData.OUT_CH[i].delay;
            DataStruct.BufDeviceData.OUT_CH[i].eq_mode = DataStruct.RcvDeviceData.OUT_CH[i].eq_mode;
            DataStruct.BufDeviceData.OUT_CH[i].LinkFlag = DataStruct.RcvDeviceData.OUT_CH[i].LinkFlag;

            DataStruct.BufDeviceData.OUT_CH[i].h_freq = DataStruct.RcvDeviceData.OUT_CH[i].h_freq;
            DataStruct.BufDeviceData.OUT_CH[i].h_filter = DataStruct.RcvDeviceData.OUT_CH[i].h_filter;
            DataStruct.BufDeviceData.OUT_CH[i].h_level = DataStruct.RcvDeviceData.OUT_CH[i].h_level;
            DataStruct.BufDeviceData.OUT_CH[i].l_freq = DataStruct.RcvDeviceData.OUT_CH[i].l_freq;
            DataStruct.BufDeviceData.OUT_CH[i].l_filter = DataStruct.RcvDeviceData.OUT_CH[i].l_filter;
            DataStruct.BufDeviceData.OUT_CH[i].l_level = DataStruct.RcvDeviceData.OUT_CH[i].l_level;

            DataStruct.BufDeviceData.OUT_CH[i].IN1_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN2_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN3_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN4_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN5_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN6_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN7_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN8_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol;

            DataStruct.BufDeviceData.OUT_CH[i].IN9_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN10_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN11_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN12_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN13_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN14_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN15_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol;
            DataStruct.BufDeviceData.OUT_CH[i].IN16_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol;

            DataStruct.BufDeviceData.OUT_CH[i].lim_t = DataStruct.RcvDeviceData.OUT_CH[i].lim_t;
            DataStruct.BufDeviceData.OUT_CH[i].lim_a = DataStruct.RcvDeviceData.OUT_CH[i].lim_a;
            DataStruct.BufDeviceData.OUT_CH[i].lim_r = DataStruct.RcvDeviceData.OUT_CH[i].lim_r;
            DataStruct.BufDeviceData.OUT_CH[i].cliplim = DataStruct.RcvDeviceData.OUT_CH[i].cliplim;
            DataStruct.BufDeviceData.OUT_CH[i].lim_rate = DataStruct.RcvDeviceData.OUT_CH[i].lim_rate;
            DataStruct.BufDeviceData.OUT_CH[i].lim_mode = DataStruct.RcvDeviceData.OUT_CH[i].lim_mode;
            DataStruct.BufDeviceData.OUT_CH[i].linkgroup_num = DataStruct.RcvDeviceData.OUT_CH[i].linkgroup_num;

            DataStruct.BufDeviceData.OUT_CH[i].EncryptFlg = DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg;
            DataStruct.BufDeviceData.OUT_CH[i].name[0] = DataStruct.RcvDeviceData.OUT_CH[i].name[0];
            DataStruct.BufDeviceData.OUT_CH[i].name[1] = DataStruct.RcvDeviceData.OUT_CH[i].name[1];
            DataStruct.BufDeviceData.OUT_CH[i].name[2] = DataStruct.RcvDeviceData.OUT_CH[i].name[2];
            DataStruct.BufDeviceData.OUT_CH[i].name[3] = DataStruct.RcvDeviceData.OUT_CH[i].name[3];
            DataStruct.BufDeviceData.OUT_CH[i].name[4] = DataStruct.RcvDeviceData.OUT_CH[i].name[4];
            DataStruct.BufDeviceData.OUT_CH[i].name[5] = DataStruct.RcvDeviceData.OUT_CH[i].name[5];
            DataStruct.BufDeviceData.OUT_CH[i].name[6] = DataStruct.RcvDeviceData.OUT_CH[i].name[6];
            //   DataStruct.BufDeviceData.OUT_CH[i].name[7]  = DataStruct.RcvDeviceData.OUT_CH[i].name[7];

            ////////////////////////////////////


            DataStruct.DefaultDeviceData.OUT_CH[i].IN1_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN2_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN3_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN4_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN5_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN6_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN7_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN8_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol;

            DataStruct.DefaultDeviceData.OUT_CH[i].IN9_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN10_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN11_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN12_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN13_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN14_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN15_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol;
            DataStruct.DefaultDeviceData.OUT_CH[i].IN16_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol;

//            DataStruct.DefaultDeviceData.OUT_CH[i].lim_t = DataStruct.RcvDeviceData.OUT_CH[i].lim_t;
//            DataStruct.DefaultDeviceData.OUT_CH[i].lim_a = DataStruct.RcvDeviceData.OUT_CH[i].lim_a;
//            DataStruct.DefaultDeviceData.OUT_CH[i].lim_r = DataStruct.RcvDeviceData.OUT_CH[i].lim_r;
//            DataStruct.DefaultDeviceData.OUT_CH[i].cliplim = DataStruct.RcvDeviceData.OUT_CH[i].cliplim;
//            DataStruct.DefaultDeviceData.OUT_CH[i].lim_rate = DataStruct.RcvDeviceData.OUT_CH[i].lim_rate;
//            DataStruct.DefaultDeviceData.OUT_CH[i].lim_mode = DataStruct.RcvDeviceData.OUT_CH[i].lim_mode;
//            DataStruct.DefaultDeviceData.OUT_CH[i].linkgroup_num = DataStruct.RcvDeviceData.OUT_CH[i].linkgroup_num;

            DataStruct.DefaultDeviceData.OUT_CH[i].EncryptFlg = DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg;
            DataStruct.DefaultDeviceData.OUT_CH[i].name[0] = DataStruct.RcvDeviceData.OUT_CH[i].name[0];
            DataStruct.DefaultDeviceData.OUT_CH[i].name[1] = DataStruct.RcvDeviceData.OUT_CH[i].name[1];
            DataStruct.DefaultDeviceData.OUT_CH[i].name[2] = DataStruct.RcvDeviceData.OUT_CH[i].name[2];
            DataStruct.DefaultDeviceData.OUT_CH[i].name[3] = DataStruct.RcvDeviceData.OUT_CH[i].name[3];
            DataStruct.DefaultDeviceData.OUT_CH[i].name[4] = DataStruct.RcvDeviceData.OUT_CH[i].name[4];
            DataStruct.DefaultDeviceData.OUT_CH[i].name[5] = DataStruct.RcvDeviceData.OUT_CH[i].name[5];
            DataStruct.DefaultDeviceData.OUT_CH[i].name[6] = DataStruct.RcvDeviceData.OUT_CH[i].name[6];
            // DataStruct.DefaultDeviceData.OUT_CH[i].name[7]  = DataStruct.RcvDeviceData.OUT_CH[i].name[7];

        }

        for (int i = 0; i <= Define.MAX_GROUP; i++) {
            for (int j = 0; j < 16; j++) {
                DataStruct.RcvDeviceData.SYS.UserGroup[i][j] = 0; // 用户名初始值
            }
        }

        for (int j = 0; j < Define.MAX_CH; j++) {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                DataStruct.GainBuf[j][i] = 600;
            }
        }

        //初始化单组音效文件
        DataStruct.RcvDeviceData.SingleData.Set_chs(getCHS(mContext));
        DataStruct.RcvDeviceData.SingleData.Set_client(getClient());
        DataStruct.RcvDeviceData.SingleData.Set_data_info(getData_info());
        DataStruct.RcvDeviceData.SingleData.Set_data(getSingle_Data());

        DataStruct.SendDeviceData.SingleData = DataStruct.RcvDeviceData.SingleData;
        //初始化整机音效文件
        DataStruct.RcvDeviceData.MAC_Data.Set_chs(getCHS(mContext));
        DataStruct.RcvDeviceData.MAC_Data.Set_client(getClient());
        DataStruct.RcvDeviceData.MAC_Data.Set_data_info(getData_info());
        DataStruct.RcvDeviceData.MAC_Data.Set_SystemData(getSystemData());
        DataStruct.RcvDeviceData.MAC_Data.Set_data(getDefaultDSP_DataMac());
        DataStruct.SendDeviceData.MAC_Data = DataStruct.RcvDeviceData.MAC_Data;

        //SYSTEM
        initSystemDataStruct();

        int le = Define.CH_Mutex0.length;
        for (int i = 0; i < 25; i++) {
            Define.CH_Mutex[i] = new int[12];
        }
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[0][i] = Define.CH_Mutex0[i];
            } else {
                Define.CH_Mutex[0][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex1.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[1][i] = Define.CH_Mutex1[i];
            } else {
                Define.CH_Mutex[1][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex2.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[2][i] = Define.CH_Mutex2[i];
            } else {
                Define.CH_Mutex[2][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex3.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[3][i] = Define.CH_Mutex3[i];
            } else {
                Define.CH_Mutex[3][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex4.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[4][i] = Define.CH_Mutex4[i];
            } else {
                Define.CH_Mutex[4][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex5.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[5][i] = Define.CH_Mutex5[i];
            } else {
                Define.CH_Mutex[5][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex6.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[6][i] = Define.CH_Mutex6[i];
            } else {
                Define.CH_Mutex[6][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex7.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[7][i] = Define.CH_Mutex7[i];
            } else {
                Define.CH_Mutex[7][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex8.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[8][i] = Define.CH_Mutex8[i];
            } else {
                Define.CH_Mutex[8][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex9.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[9][i] = Define.CH_Mutex9[i];
            } else {
                Define.CH_Mutex[9][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex10.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[10][i] = Define.CH_Mutex10[i];
            } else {
                Define.CH_Mutex[10][i] = Define.EndFlag;
            }

        }

        le = Define.CH_Mutex11.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[11][i] = Define.CH_Mutex11[i];
            } else {
                Define.CH_Mutex[11][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex12.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[12][i] = Define.CH_Mutex12[i];
            } else {
                Define.CH_Mutex[12][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex13.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[13][i] = Define.CH_Mutex13[i];
            } else {
                Define.CH_Mutex[13][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex14.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[14][i] = Define.CH_Mutex14[i];
            } else {
                Define.CH_Mutex[14][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex15.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[15][i] = Define.CH_Mutex15[i];
            } else {
                Define.CH_Mutex[15][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex16.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[16][i] = Define.CH_Mutex16[i];
            } else {
                Define.CH_Mutex[16][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex17.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[17][i] = Define.CH_Mutex17[i];
            } else {
                Define.CH_Mutex[17][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex18.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[18][i] = Define.CH_Mutex18[i];
            } else {
                Define.CH_Mutex[18][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex19.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[19][i] = Define.CH_Mutex19[i];
            } else {
                Define.CH_Mutex[19][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex20.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[20][i] = Define.CH_Mutex20[i];
            } else {
                Define.CH_Mutex[20][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex21.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[21][i] = Define.CH_Mutex21[i];
            } else {
                Define.CH_Mutex[21][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex22.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[22][i] = Define.CH_Mutex22[i];
            } else {
                Define.CH_Mutex[22][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex23.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[23][i] = Define.CH_Mutex23[i];
            } else {
                Define.CH_Mutex[23][i] = Define.EndFlag;
            }

        }
        le = Define.CH_Mutex24.length;
        for (int i = 0; i < 12; i++) {
            if (i < le) {
                Define.CH_Mutex[24][i] = Define.CH_Mutex24[i];
            } else {
                Define.CH_Mutex[24][i] = Define.EndFlag;
            }
        }

    }

    public static void initSystemDataStruct() {
        DataStruct.SendDeviceData.SYS.input_source = DataStruct.RcvDeviceData.SYS.input_source = 1;
        DataStruct.SendDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.SYS.aux_mode = 255;
        DataStruct.SendDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.SYS.device_mode = 0;
        DataStruct.SendDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.SYS.hi_mode = 0;
        DataStruct.SendDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.SYS.blue_gain = 2;
        DataStruct.SendDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.SYS.aux_gain = 0;
        DataStruct.SendDeviceData.SYS.Safety = DataStruct.RcvDeviceData.SYS.Safety = 1;
        DataStruct.SendDeviceData.SYS.sound_effect = DataStruct.RcvDeviceData.SYS.sound_effect = 0;

        DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol = 27;
        DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay = 0;
        DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t = 0;
        DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource = 100;
        DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB = 100;
        DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
        DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime = 0;

//        DataStruct.RcvDeviceData.SYS.input_MusicVol = 20;
//        DataStruct.RcvDeviceData.SYS.input_EffVol = 20;
//        DataStruct.RcvDeviceData.SYS.input_MicVol = 20;

        setEncryption();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            setDefault(i);
            SetOutputVolume(i);
        }
    }


    /**设置清楚数据*/
    public static  void setCleanData(){
        MacCfg.bool_Encryption = false;

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg = 0x20;
        }
        DataOptUtil.setInitData();
        DataOptUtil.setEncryption();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.SetOutputVolume(i);
        }
        //   DataOptUtil.SaveGroupData(0);
        DataOptUtil.ComparedToSendData(true);
        DataStruct.SEFF_USER_GROUP_OPT = 1;
    }

    public static void setInitData() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.FillRecDataStruct(Define.OUTPUT, i, Mac_H812.H812_Output1_init_data, false);
        }
    }

    public static void setEncryption() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            setOutputSpk(DataStruct.CurMacMode.Out.SPK_TYPE[i], i);

        }
        DataOptUtil.GetChannelNum(0);
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            SetInputSourceMixerVol(i);
            setXOverWithOutputSPKType(i);
        }
    }


    public static void setOutputChannelSKP(int channel, int spk) {
        switch (channel) {
            case 0:
                DataStruct.RcvDeviceData.SYS.out1_spk_type = spk;
                break;
            case 1:
                DataStruct.RcvDeviceData.SYS.out2_spk_type = spk;
                break;
            case 2:
                DataStruct.RcvDeviceData.SYS.out3_spk_type = spk;
                break;
            case 3:
                DataStruct.RcvDeviceData.SYS.out4_spk_type = spk;
                break;
            case 4:
                DataStruct.RcvDeviceData.SYS.out5_spk_type = spk;
                break;
            case 5:
                DataStruct.RcvDeviceData.SYS.out6_spk_type = spk;
                break;
            case 6:
                DataStruct.RcvDeviceData.SYS.out7_spk_type = spk;
                break;
            case 7:
                DataStruct.RcvDeviceData.SYS.out8_spk_type = spk;
                break;
            case 8:
                DataStruct.RcvDeviceData.SYS.CurGroupID = spk;
                break;
            case 9:
                DataStruct.RcvDeviceData.SYS.out10_spk_type = spk;
                break;
            case 10:
                DataStruct.RcvDeviceData.SYS.out11_spk_type = spk;
                break;
            case 11:
                DataStruct.RcvDeviceData.SYS.out12_spk_type = spk;
                break;
            case 12:
                DataStruct.RcvDeviceData.SYS.out13_spk_type = spk;
                break;
            case 13:
                DataStruct.RcvDeviceData.SYS.out14_spk_type = spk;
                break;
            case 14:
                DataStruct.RcvDeviceData.SYS.out15_spk_type = spk;
                break;
            case 15:
                DataStruct.RcvDeviceData.SYS.out16_spk_type = spk;
                break;
            default:
                break;
        }
    }

    /**
     * 获取屏幕参数
     *
     * @param mContext
     */
    public static void GetScreenSizeAndSetPopWindow(Context mContext) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        DataStruct.Screen_Dip = dm.densityDpi;
        DataStruct.ScreenWidth = dm.widthPixels;
        DataStruct.ScreenHeight = dm.heightPixels;
        System.out.println("### Screen Dip:" + DataStruct.Screen_Dip);
        System.out.println("### Screen Width:" + DataStruct.ScreenWidth);
        System.out.println("### Screen Height:" + DataStruct.ScreenHeight);
        DataStruct.OctPopWindowHeight = DataStruct.ScreenHeight / 2 - DataStruct.ScreenHeight / 20;
        DataStruct.ScreenItemHeight = DataStruct.OctPopWindowHeight / 8;
        DataStruct.OctPopWindowHeight = (DataStruct.OctPopWindowHeight / 8) * 4;
        DataStruct.FifterPopWindowHeight = DataStruct.ScreenHeight / 6 + DataStruct.ScreenHeight / 360;
        //OutCha_PopWindowHeight=DataStruct.ScreenHeight/2-DataStruct.ScreenHeight/50;
        if ((DataStruct.ScreenWidth == 800) && (DataStruct.ScreenHeight == 1232)) {
            DataStruct.FifterPopWindowHeight = DataStruct.FifterPopWindowHeight / 2 + DataStruct.FifterPopWindowHeight / 20;
            DataStruct.OctPopWindowHeight = DataStruct.OctPopWindowHeight / 2 + DataStruct.OctPopWindowHeight / 20;

        }
    }

    /**
     * 初始化数据库
     */
    public static void initDatabases(Context mContext) {
        if (DataStruct.db != null && DataStruct.db.isOpen()) {
            DataStruct.db.close();
            Log.i("info", "db is lll");
        }
        // --------获取数据库对象----------//
        DataStruct.DataBaseHelper = new DataBaseOpenHelper(mContext);
        DataStruct.db = DataStruct.DataBaseHelper.getReadableDatabase();
        DataStruct.dbSEffData_Table = new DB_SEffData_Table(mContext, DataStruct.db);
        DataStruct.dbSEfFile_Table = new DB_SEffFile_Table(mContext, DataStruct.db);
        DataStruct.dbSEfFile_Recently_Table = new DB_SEffFile_Recently_Table(mContext, DataStruct.db);
        DataStruct.dbLoginSM_Table = new DB_LoginSM_Table(mContext, DataStruct.db);

        DataStruct.CCM_DataBaseHelper = new DataBaseCCMHelper(mContext);
        DataStruct.db_CCM = DataStruct.CCM_DataBaseHelper.getReadableDatabase();
        DataStruct.dbCarBrands_Table = new CarBrands_Table(mContext, DataStruct.db_CCM);
        DataStruct.dbCarTypes_Table = new CarTypes_Table(mContext, DataStruct.db_CCM);
        DataStruct.dbMacTypes_Table = new MacTypes_Table(mContext, DataStruct.db_CCM);
        DataStruct.dbMacsAgentName_Table = new MacsAgentName_Table(mContext, DataStruct.db_CCM);

        DataStruct.DBMusicHelper = new DataBaseMusicHelper(mContext);
        DataStruct.db_Music = DataStruct.DBMusicHelper.getReadableDatabase();
        DataStruct.dbMFileList_Table = new DB_MFileList_Table(mContext, DataStruct.db_Music);
        DataStruct.dbMMusicList_Table = new DB_MMusicList_Table(mContext, DataStruct.db_Music);

    }

    public static void ExitDatabases() {
        //mImageLoader = null;
        DataStruct.RcvDeviceData = null;
        DataStruct.SendDeviceData = null;

        DataStruct.db = null;
        DataStruct.db_CCM = null;
        DataStruct.DataBaseHelper = null;
        DataStruct.dbSEffData_Table = null;
        DataStruct.dbSEfFile_Table = null;
        DataStruct.dbSEfFile_Recently_Table = null;
        DataStruct.dbLoginSM_Table = null;

        DataStruct.DataBaseHelper = null;
        DataStruct.dbCarBrands_Table = null;
        DataStruct.dbCarTypes_Table = null;
        DataStruct.dbMacTypes_Table = null;
        DataStruct.dbMacsAgentName_Table = null;


        DataStruct.DBMusicHelper = null;
        DataStruct.db_Music = null;
        DataStruct.dbMFileList_Table = null;
        DataStruct.dbMMusicList_Table = null;

        //DataStruct.DataBaseMusicHelper = DataBaseOpenHelper.getInstance(mContext);
        // 调用getReadableDatabase方法如果数据库不存在 则创建 如果存在则打开
        //db = DataStruct.DataBaseMusicHelper.getReadableDatabase();
        // 关闭数据库

        if (DataStruct.DataBaseHelper != null) {
            DataStruct.DataBaseHelper.close();
        }
        if (DataStruct.CCM_DataBaseHelper != null) {
            DataStruct.CCM_DataBaseHelper.close();
        }

        // 删除数据库
        //DataStruct.DataBaseMusicHelper.deleteDatabase(mContext);
    }

    public static void scanDirAsync(Context mContext) {
	/*
        String dir= Environment.getExternalStorageDirectory().toString();
        System.out.println("BUG scanDirAsync dir:"+dir);
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
        if(dir != null){
            scanIntent.setData(Uri.fromFile(new File(dir)));
            mContext.sendBroadcast(scanIntent);
        }
*/
    }


    public static boolean VerderOptionFileIsExists() {
        String filePahtString = Environment.getExternalStorageDirectory() + "/" +
                MacCfg.AgentNAME + "/" + MacCfg.Mac + "/" +
                DataStruct.venderOption.Get_lastversion() + ".json";
        //System.out.println("BUG_NET filePahtString?:"+filePahtString);
        try {
            File f = new File(filePahtString);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static String getAppInfo(Context context) {
        String appnameString = null;
        try {
            appnameString = context.getPackageName();
        } catch (Exception e) {
            appnameString = null;
        }
        return appnameString;
    }

    /*********************************************************************/
    /******************   数据通信 ，格式转换，信息处理       **********************/
    /*********************************************************************/

    public static void InitLoad() {


        if (DataStruct.SendDeviceData == null) {
            return;
        }

        DataStruct.SEFF_USER_GROUP_OPT = 0;
        System.out.println("BUG:  InitLoad()");
        // U0BusyFlg = NO;
        DataStruct.DeviceVerErrorFlg = false;
        DataStruct.U0SynDataSucessFlg = false;

        DataStruct.SendbufferList.clear();
        DataStruct.SendbufferList.removeAll(DataStruct.SendbufferList);

        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SOFTWARE_VERSION;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;
        SendDataToDevice(false);
        InitLoadMac();//TODO
        System.out.println("BUG InitLoad DataStruct.SendbufferList.size()=" + DataStruct.SendbufferList.size());
        DataStruct.B_InitLoad = true;
    }

    public static void InitLoadMac() {
        DataStruct.SEFF_USER_GROUP_OPT = 1;
        System.out.println("BUG:  InitLoadMac()");
        MacCfg.bool_OutChLock = false;

        MacCfg.bool_OutChLink = false;


        for (int i = 0; i < Define.MAX_CH; i++) {
            for (int j = 0; j < DataStruct.CurMacMode.EQ.Max_EQ; j++) {
                DataStruct.GainBuf[i][j] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }

        }
        //主音量
//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;
        SendDataToDevice(false);


        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_Group;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;
        SendDataToDevice(false);

//        }else {
//            /*增加读取Input数据，获取音量 0x77*/
//            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.MUSIC;
//            DataStruct.SendDeviceData.ChannelID = 0x02;//MUSIC 固定2
//            DataStruct.SendDeviceData.DataID = 0x0;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//            DataStruct.SendDeviceData.DataLen = 0x00;
//            SendDataToDevice(false);
//        }

//        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
//        DataStruct.SendDeviceData.DeviceID = 0x01;
//        DataStruct.SendDeviceData.UserID = 0x00;
//        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//        DataStruct.SendDeviceData.ChannelID = Define.CUR_PROGRAM_INFO;
//        DataStruct.SendDeviceData.DataID = 0x00;
//        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//        DataStruct.SendDeviceData.PcCustom = 0x00;
//        DataStruct.SendDeviceData.DataLen = 0x00;
//        SendDataToDevice(false);

        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.PC_SOURCE_SET;// 输入源选择
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;
        SendDataToDevice(false);



        /*获取用户名字*/
        for (int i = 1; i <= DataStruct.CurMacMode.MAX_USE_GROUP; i++) {
            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = (i & 0xff);
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.GROUP_NAME;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;
            SendDataToDevice(false);
        }


        //读取通道输出类型配置
        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_SPK_TYPE;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;
        SendDataToDevice(false);

        //读取通道输出类型配置
//		if(DataStruct.CurMacMode.BOOL_SYSTEM_SPK_TYPEB){
//        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
//        DataStruct.SendDeviceData.DeviceID = 0x01;
//        DataStruct.SendDeviceData.UserID = 0x00;
//        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_SPK_TYPEB;
//        DataStruct.SendDeviceData.DataID = 0x00;
//        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//        DataStruct.SendDeviceData.PcCustom = 0x00;
//        DataStruct.SendDeviceData.DataLen = 0x00;
//        SendDataToDevice(false);
//        }

        /*增加读取全部输入通道数据*/

        /*增加读取全部通道的输出数据 DataStruct.CurMacMode.Out.OUT_CH_MAX */
        for (int i = 0; i < MacCfg.MaxOutputCH; i++) {
            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.OUTPUT;
            DataStruct.SendDeviceData.ChannelID = i;
            DataStruct.SendDeviceData.DataID = 0x77;//读当前组的数据
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
            DataStruct.SendDeviceData.DataLen = 0x00;
            SendDataToDevice(false);
        }
        /*增加读取通道延时,要放到最后（读取全部通道的输出数据）读取，若条件成立，会覆盖OUTPUT通道里的数据*/
//        if(DataStruct.CurMacMode.Delay.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
//            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//            DataStruct.SendDeviceData.ChannelID = Define.SOUND_FIELD_INFO;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//            DataStruct.SendDeviceData.DataLen = 0x00;
//            SendDataToDevice(false);
//        }
    }

    public static void send_get_SYSTEM_DATA_CMD() {
        DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x00;

        SendDataToDevice(false);
    }

    public static void syncMainVol() {
        DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;
        DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
        DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
        DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
        DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
        DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
        DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

        DataStruct.SendDeviceData.SYS.Safety = DataStruct.RcvDeviceData.SYS.Safety;
        DataStruct.SendDeviceData.SYS.sound_effect = DataStruct.RcvDeviceData.SYS.sound_effect;


        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;

        DataStruct.U0SendFrameFlg = YES;
        SendDataToDevice(false);
    }

    public static void ComparedToSendData(boolean uptodevice) {
        if (DataStruct.SendDeviceData == null) {
            DataStruct.SendDeviceData = new Data();
        }
        if (DataStruct.RcvDeviceData == null) {
            DataStruct.RcvDeviceData = new Data();
        }
        //System.out.println("FUCK ComparedToSendData");
        // 比较音源数据
        if (DataStruct.SendDeviceData.SYS.input_source != DataStruct.RcvDeviceData.SYS.input_source
                || DataStruct.SendDeviceData.SYS.aux_mode != DataStruct.RcvDeviceData.SYS.aux_mode
                || DataStruct.SendDeviceData.SYS.device_mode != DataStruct.RcvDeviceData.SYS.device_mode
                || DataStruct.SendDeviceData.SYS.hi_mode != DataStruct.RcvDeviceData.SYS.hi_mode
                || DataStruct.SendDeviceData.SYS.blue_gain != DataStruct.RcvDeviceData.SYS.blue_gain
                || DataStruct.SendDeviceData.SYS.aux_gain != DataStruct.RcvDeviceData.SYS.aux_gain
                || DataStruct.SendDeviceData.SYS.Safety != DataStruct.RcvDeviceData.SYS.Safety
                || DataStruct.SendDeviceData.SYS.sound_effect != DataStruct.RcvDeviceData.SYS.sound_effect) {
            DataStruct.SendDeviceData.SYS.input_source = DataStruct.RcvDeviceData.SYS.input_source;
            DataStruct.SendDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.SYS.aux_mode;
            DataStruct.SendDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.SYS.device_mode;
            DataStruct.SendDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.SYS.hi_mode;
            DataStruct.SendDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.SYS.blue_gain;
            DataStruct.SendDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.SYS.aux_gain;
            DataStruct.SendDeviceData.SYS.Safety = DataStruct.RcvDeviceData.SYS.Safety;
            DataStruct.SendDeviceData.SYS.sound_effect = DataStruct.RcvDeviceData.SYS.sound_effect;

            if (uptodevice == true) {
                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = 0x00;
                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                DataStruct.SendDeviceData.ChannelID = Define.PC_SOURCE_SET;
                DataStruct.SendDeviceData.DataID = 0x00;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                DataStruct.SendDeviceData.PcCustom = 0x00;
                DataStruct.SendDeviceData.DataLen = 8;

                DataStruct.U0SendFrameFlg = YES;
                SendDataToDevice(false);
            }

        }


        if (DataStruct.SendDeviceData.SYS.Play_vol != DataStruct.RcvDeviceData.SYS.Play_vol
                || DataStruct.SendDeviceData.SYS.Start_Def_source != DataStruct.RcvDeviceData.SYS.Start_Def_source
                || DataStruct.SendDeviceData.SYS.Source_type != DataStruct.RcvDeviceData.SYS.Source_type
                || DataStruct.SendDeviceData.SYS.none4 != DataStruct.RcvDeviceData.SYS.none4
                || DataStruct.SendDeviceData.SYS.SubVol != DataStruct.RcvDeviceData.SYS.SubVol
                || DataStruct.SendDeviceData.SYS.none5 != DataStruct.RcvDeviceData.SYS.none5
        ) {
            DataStruct.SendDeviceData.SYS.Play_vol = DataStruct.RcvDeviceData.SYS.Play_vol;
            DataStruct.SendDeviceData.SYS.Start_Def_source = DataStruct.RcvDeviceData.SYS.Start_Def_source;
            DataStruct.SendDeviceData.SYS.Source_type = DataStruct.RcvDeviceData.SYS.Source_type;
            DataStruct.SendDeviceData.SYS.none4 = DataStruct.RcvDeviceData.SYS.none4;
            DataStruct.SendDeviceData.SYS.SubVol = DataStruct.RcvDeviceData.SYS.SubVol;
            DataStruct.SendDeviceData.SYS.none5 = DataStruct.RcvDeviceData.SYS.none5;

            if (uptodevice == true) {
                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = 0x00;
                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_Group;
                DataStruct.SendDeviceData.DataID = 0x00;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                DataStruct.SendDeviceData.PcCustom = 0x00;
                DataStruct.SendDeviceData.DataLen = 8;

                DataStruct.U0SendFrameFlg = YES;
                SendDataToDevice(false);
            }
        }


        // 比较系统配置数据
        if (DataStruct.SendDeviceData.SYS.main_vol != DataStruct.RcvDeviceData.SYS.main_vol ||
                //680通过系统设置控制总音量，461通过Input.2.MUSIC.控制总音量
                DataStruct.SendDeviceData.SYS.alldelay != DataStruct.RcvDeviceData.SYS.alldelay
                || DataStruct.SendDeviceData.SYS.noisegate_t != DataStruct.RcvDeviceData.SYS.noisegate_t
                || DataStruct.SendDeviceData.SYS.AutoSource != DataStruct.RcvDeviceData.SYS.AutoSource
                || DataStruct.SendDeviceData.SYS.AutoSourcedB != DataStruct.RcvDeviceData.SYS.AutoSourcedB
                || DataStruct.SendDeviceData.SYS.MainvolMuteFlg != DataStruct.RcvDeviceData.SYS.MainvolMuteFlg
                || DataStruct.SendDeviceData.SYS.offTime != DataStruct.RcvDeviceData.SYS.offTime) {
            DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;
            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
            DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

            if (uptodevice == true) {
                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = 0x00;
                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
                DataStruct.SendDeviceData.DataID = 0x00;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                DataStruct.SendDeviceData.PcCustom = 0x00;
                DataStruct.SendDeviceData.DataLen = 8;

                DataStruct.U0SendFrameFlg = YES;
                SendDataToDevice(false);
            }
        }
        // 比较系统通道输出类型配置数据
        if (DataStruct.SendDeviceData.SYS.out1_spk_type != DataStruct.RcvDeviceData.SYS.out1_spk_type
                || DataStruct.SendDeviceData.SYS.out2_spk_type != DataStruct.RcvDeviceData.SYS.out2_spk_type
                || DataStruct.SendDeviceData.SYS.out3_spk_type != DataStruct.RcvDeviceData.SYS.out3_spk_type
                || DataStruct.SendDeviceData.SYS.out4_spk_type != DataStruct.RcvDeviceData.SYS.out4_spk_type
                || DataStruct.SendDeviceData.SYS.out5_spk_type != DataStruct.RcvDeviceData.SYS.out5_spk_type
                || DataStruct.SendDeviceData.SYS.out6_spk_type != DataStruct.RcvDeviceData.SYS.out6_spk_type
                || DataStruct.SendDeviceData.SYS.out7_spk_type != DataStruct.RcvDeviceData.SYS.out7_spk_type
                || DataStruct.SendDeviceData.SYS.out8_spk_type != DataStruct.RcvDeviceData.SYS.out8_spk_type) {

            DataStruct.SendDeviceData.SYS.out1_spk_type = DataStruct.RcvDeviceData.SYS.out1_spk_type;
            DataStruct.SendDeviceData.SYS.out2_spk_type = DataStruct.RcvDeviceData.SYS.out2_spk_type;
            DataStruct.SendDeviceData.SYS.out3_spk_type = DataStruct.RcvDeviceData.SYS.out3_spk_type;
            DataStruct.SendDeviceData.SYS.out4_spk_type = DataStruct.RcvDeviceData.SYS.out4_spk_type;
            DataStruct.SendDeviceData.SYS.out5_spk_type = DataStruct.RcvDeviceData.SYS.out5_spk_type;
            DataStruct.SendDeviceData.SYS.out6_spk_type = DataStruct.RcvDeviceData.SYS.out6_spk_type;
            DataStruct.SendDeviceData.SYS.out7_spk_type = DataStruct.RcvDeviceData.SYS.out7_spk_type;
            DataStruct.SendDeviceData.SYS.out8_spk_type = DataStruct.RcvDeviceData.SYS.out8_spk_type;

            if (uptodevice == true) {
                setCurID();

                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = 0x00;
                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_SPK_TYPE;
                DataStruct.SendDeviceData.DataID = 0x00;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                DataStruct.SendDeviceData.PcCustom = 0x00;
                DataStruct.SendDeviceData.DataLen = 8;

                DataStruct.U0SendFrameFlg = YES;
                SendDataToDevice(false);
            }
        }

        // 比较系统通道输出类型配置数据
//        if (DataStruct.SendDeviceData.SYS.out9_spk_type != DataStruct.RcvDeviceData.SYS.out9_spk_type
//                || DataStruct.SendDeviceData.SYS.out10_spk_type != DataStruct.RcvDeviceData.SYS.out10_spk_type
//                || DataStruct.SendDeviceData.SYS.out11_spk_type != DataStruct.RcvDeviceData.SYS.out11_spk_type
//                || DataStruct.SendDeviceData.SYS.out12_spk_type != DataStruct.RcvDeviceData.SYS.out12_spk_type
//                || DataStruct.SendDeviceData.SYS.out13_spk_type != DataStruct.RcvDeviceData.SYS.out13_spk_type
//                || DataStruct.SendDeviceData.SYS.out14_spk_type != DataStruct.RcvDeviceData.SYS.out14_spk_type
//                || DataStruct.SendDeviceData.SYS.out15_spk_type != DataStruct.RcvDeviceData.SYS.out15_spk_type
//                || DataStruct.SendDeviceData.SYS.out16_spk_type != DataStruct.RcvDeviceData.SYS.out16_spk_type
//
//
//                ) {
//            DataStruct.SendDeviceData.SYS.out9_spk_type = DataStruct.RcvDeviceData.SYS.out9_spk_type;
//            DataStruct.SendDeviceData.SYS.out10_spk_type = DataStruct.RcvDeviceData.SYS.out10_spk_type;
//            DataStruct.SendDeviceData.SYS.out11_spk_type = DataStruct.RcvDeviceData.SYS.out11_spk_type;
//            DataStruct.SendDeviceData.SYS.out12_spk_type = DataStruct.RcvDeviceData.SYS.out12_spk_type;
//            DataStruct.SendDeviceData.SYS.out13_spk_type = DataStruct.RcvDeviceData.SYS.out13_spk_type;
//            DataStruct.SendDeviceData.SYS.out14_spk_type = DataStruct.RcvDeviceData.SYS.out14_spk_type;
//            DataStruct.SendDeviceData.SYS.out15_spk_type = DataStruct.RcvDeviceData.SYS.out15_spk_type;
//            DataStruct.SendDeviceData.SYS.out16_spk_type = DataStruct.RcvDeviceData.SYS.out16_spk_type;
//
//            if (uptodevice == true) {
//                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                DataStruct.SendDeviceData.DeviceID = 0x01;
//                DataStruct.SendDeviceData.UserID = 0x00;
//                DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//                DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_SPK_TYPEB;
//                DataStruct.SendDeviceData.DataID = 0x00;
//                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//                DataStruct.SendDeviceData.PcCustom = 0x00;
//                DataStruct.SendDeviceData.DataLen = 8;
//
//                DataStruct.U0SendFrameFlg = YES;
//                SendDataToDevice(false);
//            }
//        }
        /////////////输入结构
//        if(DataStruct.CurMacMode.BOOL_USE_INS){
//			/* 比较MUSIC InputData数据  主要是总音量,静音,这里只有1通道 */
//            for(int i=0;i<DataStruct.CurMacMode.INS.INS_CH_MAX;i++){
//                //---id = 0		杂项
//                if (DataStruct.SendDeviceData.INS_CH[i].feedback != DataStruct.RcvDeviceData.INS_CH[i].feedback
//                        || DataStruct.SendDeviceData.INS_CH[i].polar    != DataStruct.RcvDeviceData.INS_CH[i].polar
//                        || DataStruct.SendDeviceData.INS_CH[i].eq_mode  != DataStruct.RcvDeviceData.INS_CH[i].eq_mode
//                        || DataStruct.SendDeviceData.INS_CH[i].mute     != DataStruct.RcvDeviceData.INS_CH[i].mute
//                        || DataStruct.SendDeviceData.INS_CH[i].delay    != DataStruct.RcvDeviceData.INS_CH[i].delay
//                        || DataStruct.SendDeviceData.INS_CH[i].Valume   != DataStruct.RcvDeviceData.INS_CH[i].Valume) {
//
//                    DataStruct.SendDeviceData.INS_CH[i].feedback  = DataStruct.RcvDeviceData.INS_CH[i].feedback;
//                    DataStruct.SendDeviceData.INS_CH[i].polar     = DataStruct.RcvDeviceData.INS_CH[i].polar;
//                    DataStruct.SendDeviceData.INS_CH[i].eq_mode   = DataStruct.RcvDeviceData.INS_CH[i].eq_mode;
//                    DataStruct.SendDeviceData.INS_CH[i].mute      = DataStruct.RcvDeviceData.INS_CH[i].mute;
//                    DataStruct.SendDeviceData.INS_CH[i].delay     = DataStruct.RcvDeviceData.INS_CH[i].delay;
//                    DataStruct.SendDeviceData.INS_CH[i].Valume    = DataStruct.RcvDeviceData.INS_CH[i].Valume;
//
//                    if (uptodevice == true) {
//                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                        DataStruct.SendDeviceData.DeviceID = 0x01;
//                        DataStruct.SendDeviceData.UserID = 0x00;
//                        DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                        DataStruct.SendDeviceData.ChannelID = i;
//                        DataStruct.SendDeviceData.DataID = Define.INS_MISC_ID;
//                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//                        DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                        DataStruct.SendDeviceData.DataLen = 8;
//
//                        DataStruct.U0SendFrameFlg = YES;
//                        SendDataToDevice(false);
//                    }
//                }
//            }
//            for(int i=0;i<DataStruct.CurMacMode.INS.INS_CH_MAX;i++){
//                if((DataStruct.SendDeviceData.INS_CH[i].h_freq  != DataStruct.RcvDeviceData.INS_CH[i].h_freq)  ||
//                        (DataStruct.SendDeviceData.INS_CH[i].h_filter!= DataStruct.RcvDeviceData.INS_CH[i].h_filter)||
//                        (DataStruct.SendDeviceData.INS_CH[i].h_level != DataStruct.RcvDeviceData.INS_CH[i].h_level) ||
//                        (DataStruct.SendDeviceData.INS_CH[i].l_freq  != DataStruct.RcvDeviceData.INS_CH[i].l_freq)  ||
//                        (DataStruct.SendDeviceData.INS_CH[i].l_filter!= DataStruct.RcvDeviceData.INS_CH[i].l_filter)||
//                        (DataStruct.SendDeviceData.INS_CH[i].l_level != DataStruct.RcvDeviceData.INS_CH[i].l_level)){
//
//                    DataStruct.SendDeviceData.INS_CH[i].h_freq   = DataStruct.RcvDeviceData.INS_CH[i].h_freq;
//                    DataStruct.SendDeviceData.INS_CH[i].h_filter = DataStruct.RcvDeviceData.INS_CH[i].h_filter;
//                    DataStruct.SendDeviceData.INS_CH[i].h_level  = DataStruct.RcvDeviceData.INS_CH[i].h_level;
//                    DataStruct.SendDeviceData.INS_CH[i].l_freq   = DataStruct.RcvDeviceData.INS_CH[i].l_freq;
//                    DataStruct.SendDeviceData.INS_CH[i].l_filter = DataStruct.RcvDeviceData.INS_CH[i].l_filter;
//                    DataStruct.SendDeviceData.INS_CH[i].l_level  = DataStruct.RcvDeviceData.INS_CH[i].l_level;
//                    if (uptodevice == true) {
//                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                        DataStruct.SendDeviceData.DeviceID = 0x01;
//                        DataStruct.SendDeviceData.UserID = 0x00;
//                        DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                        DataStruct.SendDeviceData.ChannelID = (byte)i;
//                        DataStruct.SendDeviceData.DataID = Define.INS_XOVER_ID;
//                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//                        DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                        DataStruct.SendDeviceData.DataLen = 8;
//
//                        DataStruct.U0SendFrameFlg = YES;
//                        SendDataToDevice(false);
//                    }
//                }
//            }
//            for(int i=0;i<DataStruct.CurMacMode.INS.INS_CH_MAX;i++){
//                for(int j=(Define.INS_ID_MAX+1);j<=(DataStruct.CurMacMode.INS.EQ.Max_EQ+Define.INS_ID_MAX);j++){
//                    if((DataStruct.SendDeviceData.INS_CH[i].EQ[j].freq != DataStruct.RcvDeviceData.INS_CH[i].EQ[j].freq) ||
//                            (DataStruct.SendDeviceData.INS_CH[i].EQ[j].level   != DataStruct.RcvDeviceData.INS_CH[i].EQ[j].level) ||
//                            (DataStruct.SendDeviceData.INS_CH[i].EQ[j].bw      != DataStruct.RcvDeviceData.INS_CH[i].EQ[j].bw) ||
//                            (DataStruct.SendDeviceData.INS_CH[i].EQ[j].shf_db  != DataStruct.RcvDeviceData.INS_CH[i].EQ[j].shf_db) ||
//                            (DataStruct.SendDeviceData.INS_CH[i].EQ[j].type    != DataStruct.RcvDeviceData.INS_CH[i].EQ[j].type)){
//
//                        DataStruct.SendDeviceData.INS_CH[i].EQ[j].freq  = DataStruct.RcvDeviceData.INS_CH[i].EQ[j].freq;
//                        DataStruct.SendDeviceData.INS_CH[i].EQ[j].level = DataStruct.RcvDeviceData.INS_CH[i].EQ[j].level;
//                        DataStruct.SendDeviceData.INS_CH[i].EQ[j].bw    = DataStruct.RcvDeviceData.INS_CH[i].EQ[j].bw;
//                        DataStruct.SendDeviceData.INS_CH[i].EQ[j].shf_db= DataStruct.RcvDeviceData.INS_CH[i].EQ[j].shf_db;
//                        DataStruct.SendDeviceData.INS_CH[i].EQ[j].type  = DataStruct.RcvDeviceData.INS_CH[i].EQ[j].type;
//                        if (uptodevice == true) {
//                            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                            DataStruct.SendDeviceData.DeviceID = 0x01;
//                            DataStruct.SendDeviceData.UserID = 0x00;
//                            DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                            DataStruct.SendDeviceData.ChannelID = (byte)i;
//                            DataStruct.SendDeviceData.DataID = (byte)j;
//                            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//                            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                            DataStruct.SendDeviceData.DataLen = 8;
//
//                            DataStruct.U0SendFrameFlg = YES;
//                            SendDataToDevice(false);
//                        }
//                    }
//                }
//            }
//        }else{
        //System.out.println("FUCK IN_MISC_ID ");
        //for(int i=0;i<MacCfg.IN_CH_MAX;i++){
        //---id = 9		杂项
        //System.out.println("FUCK -DataStruct.RcvDeviceData.IN_CH["+0+"].Valume"+DataStruct.RcvDeviceData.IN_CH[0].Valume);
        //System.out.println("FUCK -DataStruct.SendDeviceData.IN_CH["+0+"].Valume"+DataStruct.SendDeviceData.IN_CH[0].Valume);
//                if (DataStruct.SendDeviceData.IN_CH[0].feedback != DataStruct.RcvDeviceData.IN_CH[0].feedback
//                        || DataStruct.SendDeviceData.IN_CH[0].polar    != DataStruct.RcvDeviceData.IN_CH[0].polar
//                        || DataStruct.SendDeviceData.IN_CH[0].mode     != DataStruct.RcvDeviceData.IN_CH[0].mode
//                        || DataStruct.SendDeviceData.IN_CH[0].mute     != DataStruct.RcvDeviceData.IN_CH[0].mute
//                        || DataStruct.SendDeviceData.IN_CH[0].delay    != DataStruct.RcvDeviceData.IN_CH[0].delay
//                        || DataStruct.SendDeviceData.IN_CH[0].Valume   != DataStruct.RcvDeviceData.IN_CH[0].Valume) {
//
//                    DataStruct.SendDeviceData.IN_CH[0].feedback  = DataStruct.RcvDeviceData.IN_CH[0].feedback;
//                    DataStruct.SendDeviceData.IN_CH[0].polar     = DataStruct.RcvDeviceData.IN_CH[0].polar;
//                    DataStruct.SendDeviceData.IN_CH[0].mode      = DataStruct.RcvDeviceData.IN_CH[0].mode;
//                    DataStruct.SendDeviceData.IN_CH[0].mute      = DataStruct.RcvDeviceData.IN_CH[0].mute;
//                    DataStruct.SendDeviceData.IN_CH[0].delay     = DataStruct.RcvDeviceData.IN_CH[0].delay;
//                    DataStruct.SendDeviceData.IN_CH[0].Valume    = DataStruct.RcvDeviceData.IN_CH[0].Valume;
//                    //System.out.println("FUCK --IN_MISC_ID 0");
//
//
//                    if (uptodevice == true) {
//                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                        DataStruct.SendDeviceData.DeviceID = 0x01;
//                        DataStruct.SendDeviceData.UserID = 0x00;
//                        DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                        DataStruct.SendDeviceData.ChannelID = 0x02;//MUSIC 固定2
//                        DataStruct.SendDeviceData.DataID = Define.IN_MISC_ID;
//                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//                        DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                        DataStruct.SendDeviceData.DataLen = 8;
//
//						/*if(DEBUG) System.out.println("##feedback:"+DataStruct.SendDeviceData.IN_CH[0].feedback);
//						if(DEBUG) System.out.println("##polar:"+DataStruct.SendDeviceData.IN_CH[0].polar);
//						if(DEBUG) System.out.println("##mode:"+DataStruct.SendDeviceData.IN_CH[0].mode);
//						if(DEBUG) System.out.println("##mute:"+DataStruct.SendDeviceData.IN_CH[0].mute);
//						if(DEBUG) System.out.println("##delay:"+DataStruct.SendDeviceData.IN_CH[0].delay);*/
//						//System.out.println("FUCK ##Valume:"+DataStruct.SendDeviceData.IN_CH[0].Valume);
//
//                        DataStruct.U0SendFrameFlg = YES;
//                        SendDataToDevice(false);
//                    }
//                }
//
//                //噪声门 ,ID = 11
//                if (DataStruct.SendDeviceData.IN_CH[0].noisegate_t != DataStruct.RcvDeviceData.IN_CH[0].noisegate_t
//                        || DataStruct.SendDeviceData.IN_CH[0].noisegate_a != DataStruct.RcvDeviceData.IN_CH[0].noisegate_a
//                        || DataStruct.SendDeviceData.IN_CH[0].noisegate_k != DataStruct.RcvDeviceData.IN_CH[0].noisegate_k
//                        || DataStruct.SendDeviceData.IN_CH[0].noisegate_r != DataStruct.RcvDeviceData.IN_CH[0].noisegate_r
//                        || DataStruct.SendDeviceData.IN_CH[0].noise_config!= DataStruct.RcvDeviceData.IN_CH[0].noise_config) {
//
//                    DataStruct.SendDeviceData.IN_CH[0].noisegate_t = DataStruct.RcvDeviceData.IN_CH[0].noisegate_t;
//                    DataStruct.SendDeviceData.IN_CH[0].noisegate_a = DataStruct.RcvDeviceData.IN_CH[0].noisegate_a;
//                    DataStruct.SendDeviceData.IN_CH[0].noisegate_k = DataStruct.RcvDeviceData.IN_CH[0].noisegate_k;
//                    DataStruct.SendDeviceData.IN_CH[0].noisegate_r = DataStruct.RcvDeviceData.IN_CH[0].noisegate_r;
//                    DataStruct.SendDeviceData.IN_CH[0].noise_config= DataStruct.RcvDeviceData.IN_CH[0].noise_config;
//
//                    if (uptodevice == true) {
//                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                        DataStruct.SendDeviceData.DeviceID = 0x01;
//                        DataStruct.SendDeviceData.UserID = 0x00;
//                        DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                        DataStruct.SendDeviceData.ChannelID = 0x02;//MUSIC 固定2
//                        DataStruct.SendDeviceData.DataID = Define.IN_NOISEGATE_ID;
//                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//                        DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                        DataStruct.SendDeviceData.DataLen = 8;
//
//                        DataStruct.U0SendFrameFlg = YES;
//                        SendDataToDevice(false);
//                    }
//                }


        //}
//        }

        /**************************   比较输出延时     通过System通道   *******************************/
//        if(DataStruct.CurMacMode.Delay.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
//            if((DataStruct.RcvDeviceData.SYS.SoundDelayField[0] !=  (DataStruct.RcvDeviceData.OUT_CH[0].delay & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[1] != ((DataStruct.RcvDeviceData.OUT_CH[0].delay >> 8) & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[2] !=  (DataStruct.RcvDeviceData.OUT_CH[1].delay & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[3] != ((DataStruct.RcvDeviceData.OUT_CH[1].delay >> 8) & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[4] !=  (DataStruct.RcvDeviceData.OUT_CH[2].delay & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[5] != ((DataStruct.RcvDeviceData.OUT_CH[2].delay >> 8) & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[6] !=  (DataStruct.RcvDeviceData.OUT_CH[3].delay & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[7] != ((DataStruct.RcvDeviceData.OUT_CH[3].delay >> 8) & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[8] !=  (DataStruct.RcvDeviceData.OUT_CH[4].delay & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[9] != ((DataStruct.RcvDeviceData.OUT_CH[4].delay >> 8) & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[10]!=  (DataStruct.RcvDeviceData.OUT_CH[5].delay & 0xff))||
//                    (DataStruct.RcvDeviceData.SYS.SoundDelayField[11]!= ((DataStruct.RcvDeviceData.OUT_CH[5].delay >> 8) & 0xff))){
//
//                SendDelayDatabySystemChannel();
//                if (uptodevice == true) {
//                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                    DataStruct.SendDeviceData.DeviceID = 0x01;
//                    DataStruct.SendDeviceData.UserID = 0x00;
//                    DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//                    DataStruct.SendDeviceData.ChannelID = Define.SOUND_FIELD_INFO;
//                    DataStruct.SendDeviceData.DataID = 0x00;
//                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                    DataStruct.SendDeviceData.DataLen = 50;
//
//                    DataStruct.U0SendFrameFlg = YES;
//                    SendDataToDevice(false);
//                }
//            }
//        }
        //------------------输出
        //ReadFrome_EQ_Buf();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            for (int j = 0; j < Define.MAX_CHEQ; j++) {
                if (!DataStruct.isConnecting) {
                    return;
                }
                if ((DataStruct.SendDeviceData.OUT_CH[i].EQ[j].freq != DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].freq) ||
                        (DataStruct.SendDeviceData.OUT_CH[i].EQ[j].level != DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].level) ||
                        (DataStruct.SendDeviceData.OUT_CH[i].EQ[j].bw != DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].bw) ||
                        (DataStruct.SendDeviceData.OUT_CH[i].EQ[j].shf_db != DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].shf_db) ||
                        (DataStruct.SendDeviceData.OUT_CH[i].EQ[j].type != DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].type)) {


                    DataStruct.SendDeviceData.OUT_CH[i].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].freq;
                    DataStruct.SendDeviceData.OUT_CH[i].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].level;
                    DataStruct.SendDeviceData.OUT_CH[i].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].bw;
                    DataStruct.SendDeviceData.OUT_CH[i].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].shf_db;
                    DataStruct.SendDeviceData.OUT_CH[i].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].type;
                    if (uptodevice == true) {
                        setCurID();

                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                        DataStruct.SendDeviceData.DeviceID = 0x01;
                        DataStruct.SendDeviceData.UserID = 0x00;
                        DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                        DataStruct.SendDeviceData.ChannelID = (byte) i;
                        DataStruct.SendDeviceData.DataID = (byte) j;
                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                        DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                        DataStruct.SendDeviceData.DataLen = 8;

                        DataStruct.U0SendFrameFlg = YES;
                        SendDataToDevice(false);
                    }
                }
            }
        }
        //输出通道的延时
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            //id = 31		杂项
            if ((DataStruct.SendDeviceData.OUT_CH[i].mute != DataStruct.RcvDeviceData.OUT_CH[i].mute) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].polar != DataStruct.RcvDeviceData.OUT_CH[i].polar) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].gain != DataStruct.RcvDeviceData.OUT_CH[i].gain) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].delay != DataStruct.RcvDeviceData.OUT_CH[i].delay) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].eq_mode != DataStruct.RcvDeviceData.OUT_CH[i].eq_mode) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].LinkFlag != DataStruct.RcvDeviceData.OUT_CH[i].LinkFlag)) {
                //setCurID();

                DataStruct.SendDeviceData.OUT_CH[i].mute = DataStruct.RcvDeviceData.OUT_CH[i].mute;
                DataStruct.SendDeviceData.OUT_CH[i].polar = DataStruct.RcvDeviceData.OUT_CH[i].polar;
                DataStruct.SendDeviceData.OUT_CH[i].gain = DataStruct.RcvDeviceData.OUT_CH[i].gain;
                DataStruct.SendDeviceData.OUT_CH[i].delay = DataStruct.RcvDeviceData.OUT_CH[i].delay;
                DataStruct.SendDeviceData.OUT_CH[i].eq_mode = DataStruct.RcvDeviceData.OUT_CH[i].eq_mode;
                DataStruct.SendDeviceData.OUT_CH[i].LinkFlag = DataStruct.RcvDeviceData.OUT_CH[i].LinkFlag;

                if (uptodevice == true) {
                    setCurID();

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                    DataStruct.SendDeviceData.ChannelID = (byte) i;
                    DataStruct.SendDeviceData.DataID = Define.OUT_MISC_ID;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = YES;
                    SendDataToDevice(false);
                }
            }
        }

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            //高低通 ,ID = 32	(xover限MIC)
            if ((DataStruct.SendDeviceData.OUT_CH[i].h_freq != DataStruct.RcvDeviceData.OUT_CH[i].h_freq) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].h_filter != DataStruct.RcvDeviceData.OUT_CH[i].h_filter) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].h_level != DataStruct.RcvDeviceData.OUT_CH[i].h_level) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].l_freq != DataStruct.RcvDeviceData.OUT_CH[i].l_freq) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].l_filter != DataStruct.RcvDeviceData.OUT_CH[i].l_filter) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].l_level != DataStruct.RcvDeviceData.OUT_CH[i].l_level)) {

                // setCurID();

                DataStruct.SendDeviceData.OUT_CH[i].h_freq = DataStruct.RcvDeviceData.OUT_CH[i].h_freq;
                DataStruct.SendDeviceData.OUT_CH[i].h_filter = DataStruct.RcvDeviceData.OUT_CH[i].h_filter;
                DataStruct.SendDeviceData.OUT_CH[i].h_level = DataStruct.RcvDeviceData.OUT_CH[i].h_level;
                DataStruct.SendDeviceData.OUT_CH[i].l_freq = DataStruct.RcvDeviceData.OUT_CH[i].l_freq;
                DataStruct.SendDeviceData.OUT_CH[i].l_filter = DataStruct.RcvDeviceData.OUT_CH[i].l_filter;
                DataStruct.SendDeviceData.OUT_CH[i].l_level = DataStruct.RcvDeviceData.OUT_CH[i].l_level;
                if (uptodevice == true) {
                    setCurID();

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                    DataStruct.SendDeviceData.ChannelID = (byte) i;
                    DataStruct.SendDeviceData.DataID = Define.OUT_XOVER_ID;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = YES;
                    SendDataToDevice(false);
                }
            }
        }

        //id = 33		混合比例
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            if ((DataStruct.SendDeviceData.OUT_CH[i].IN1_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN2_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN3_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN4_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN5_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN6_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN7_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN8_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol)) {


                DataStruct.SendDeviceData.OUT_CH[i].IN1_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN2_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN3_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN4_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN5_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN6_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN7_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN8_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol;
                if (uptodevice == true) {

                    setCurID();

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                    DataStruct.SendDeviceData.ChannelID = (byte) i;
                    DataStruct.SendDeviceData.DataID = Define.OUT_Valume_ID;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = YES;
                    SendDataToDevice(false);
                }
            }
        }

        //id = 34		混合比例
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            if ((DataStruct.SendDeviceData.OUT_CH[i].IN9_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN10_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN11_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN12_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN13_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN14_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN15_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].IN16_Vol != DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol)
                //(DataStruct.SendDeviceData.OUT_CH[i].IN_polar != DataStruct.RcvDeviceData.OUT_CH[i].IN_polar)

            ) {

                DataStruct.SendDeviceData.OUT_CH[i].IN9_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN10_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN11_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN12_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN13_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN14_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN15_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol;
                DataStruct.SendDeviceData.OUT_CH[i].IN16_Vol = DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol;
                //DataStruct.SendDeviceData.OUT_CH[i].IN_polar  = DataStruct.RcvDeviceData.OUT_CH[i].IN_polar;
                if (uptodevice == true) {
                    setCurID();

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                    DataStruct.SendDeviceData.ChannelID = (byte) i;
                    DataStruct.SendDeviceData.DataID = Define.OUT_MIX_ID;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = YES;
                    SendDataToDevice(false);
                }
            }
        }

        //id = id = 35		压限
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            if ((DataStruct.SendDeviceData.OUT_CH[i].lim_t != DataStruct.RcvDeviceData.OUT_CH[i].lim_t) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].lim_a != DataStruct.RcvDeviceData.OUT_CH[i].lim_a) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].lim_r != DataStruct.RcvDeviceData.OUT_CH[i].lim_r) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].cliplim != DataStruct.RcvDeviceData.OUT_CH[i].cliplim) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].lim_rate != DataStruct.RcvDeviceData.OUT_CH[i].lim_rate) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].lim_mode != DataStruct.RcvDeviceData.OUT_CH[i].lim_mode) ||
                    (DataStruct.SendDeviceData.OUT_CH[i].linkgroup_num != DataStruct.RcvDeviceData.OUT_CH[i].linkgroup_num)

            ) {

                DataStruct.SendDeviceData.OUT_CH[i].lim_t = DataStruct.RcvDeviceData.OUT_CH[i].lim_t;
                DataStruct.SendDeviceData.OUT_CH[i].lim_a = DataStruct.RcvDeviceData.OUT_CH[i].lim_a;
                DataStruct.SendDeviceData.OUT_CH[i].lim_r = DataStruct.RcvDeviceData.OUT_CH[i].lim_r;
                DataStruct.SendDeviceData.OUT_CH[i].cliplim = DataStruct.RcvDeviceData.OUT_CH[i].cliplim;
                DataStruct.SendDeviceData.OUT_CH[i].lim_rate = DataStruct.RcvDeviceData.OUT_CH[i].lim_rate;
                DataStruct.SendDeviceData.OUT_CH[i].lim_mode = DataStruct.RcvDeviceData.OUT_CH[i].lim_mode;
                DataStruct.SendDeviceData.OUT_CH[i].linkgroup_num = DataStruct.RcvDeviceData.OUT_CH[i].linkgroup_num;
                if (uptodevice == true) {

                    setCurID();

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                    DataStruct.SendDeviceData.ChannelID = (byte) i;
                    DataStruct.SendDeviceData.DataID = Define.OUT_LIMIT_ID;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = YES;
                    SendDataToDevice(false);
                }
            }
        }

        //id = id = 36
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            if (
                    (DataStruct.SendDeviceData.OUT_CH[i].EncryptFlg != DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[0] != DataStruct.RcvDeviceData.OUT_CH[i].name[0]) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[1] != DataStruct.RcvDeviceData.OUT_CH[i].name[1]) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[2] != DataStruct.RcvDeviceData.OUT_CH[i].name[2]) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[3] != DataStruct.RcvDeviceData.OUT_CH[i].name[3]) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[4] != DataStruct.RcvDeviceData.OUT_CH[i].name[4]) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[5] != DataStruct.RcvDeviceData.OUT_CH[i].name[5]) ||
                            (DataStruct.SendDeviceData.OUT_CH[i].name[6] != DataStruct.RcvDeviceData.OUT_CH[i].name[6])
                //  (DataStruct.SendDeviceData.OUT_CH[i].name[7] != DataStruct.RcvDeviceData.OUT_CH[i].name[7])

            ) {

                DataStruct.SendDeviceData.OUT_CH[i].EncryptFlg = DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg;
                DataStruct.SendDeviceData.OUT_CH[i].name[0] = DataStruct.RcvDeviceData.OUT_CH[i].name[0];
                DataStruct.SendDeviceData.OUT_CH[i].name[1] = DataStruct.RcvDeviceData.OUT_CH[i].name[1];
                DataStruct.SendDeviceData.OUT_CH[i].name[2] = DataStruct.RcvDeviceData.OUT_CH[i].name[2];
                DataStruct.SendDeviceData.OUT_CH[i].name[3] = DataStruct.RcvDeviceData.OUT_CH[i].name[3];
                DataStruct.SendDeviceData.OUT_CH[i].name[4] = DataStruct.RcvDeviceData.OUT_CH[i].name[4];
                DataStruct.SendDeviceData.OUT_CH[i].name[5] = DataStruct.RcvDeviceData.OUT_CH[i].name[5];
                DataStruct.SendDeviceData.OUT_CH[i].name[6] = DataStruct.RcvDeviceData.OUT_CH[i].name[6];
                //   DataStruct.SendDeviceData.OUT_CH[i].name[7]  = DataStruct.RcvDeviceData.OUT_CH[i].name[7];
                if (uptodevice == true) {
                    setCurID();

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                    DataStruct.SendDeviceData.ChannelID = (byte) i;
                    DataStruct.SendDeviceData.DataID = Define.OUT_NAME_ID;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = YES;
                    SendDataToDevice(false);
                }
            }
        }
    }


    // 发送一个数据包到设备
    // sendorinsert = true 直接发送出去，否则插入发送队列
    public static void SendDataToDevice(boolean sendorinsert) {
        byte FrameDataSUM = 0x00;
        byte[] FrameDataBuf = new byte[DataStruct.U0DataLen + DataStruct.CMD_LENGHT];
        Arrays.fill(FrameDataBuf, (byte) 0x00);

        if (DataStruct.SendDeviceData == null) {
            return;
        }

        if (DataStruct.SendDeviceData.FrameType == 0xa2) { // 从设备读取数据，无需填充内容
            DataStruct.SendDeviceData.DataLen = 0;
        }

        FrameDataBuf[0] = (byte) (MacCfg.HEAD_DATA & 0xff); // 包头
        FrameDataBuf[1] = (byte) (MacCfg.HEAD_DATA & 0xff); // 包头
        FrameDataBuf[2] = (byte) (MacCfg.HEAD_DATA & 0xff); // 包头
        FrameDataBuf[3] = (byte) (DataStruct.FRAME_STA & 0xff); // 起始字符

        FrameDataBuf[4] = (byte) (DataStruct.SendDeviceData.FrameType & 0xff); // 帧类型 读/写 写:0xa1
        // 读:0xa2
        FrameDataBuf[5] = (byte) (DataStruct.SendDeviceData.DeviceID & 0xff);  // 设备ID
        FrameDataBuf[6] = (byte) (DataStruct.SendDeviceData.UserID & 0xff);    // 用户ID
        FrameDataBuf[7] = (byte) (DataStruct.SendDeviceData.DataType & 0xff);  // 数据类型 输入 输出 系统等
        FrameDataBuf[8] = (byte) (DataStruct.SendDeviceData.ChannelID & 0xff); // 通道ID
        FrameDataBuf[9] = (byte) (DataStruct.SendDeviceData.DataID & 0xff);    // 数据ID(MacCfg.BluetoothDeviceID & 0xff);//
        FrameDataBuf[10] = (byte) (MacCfg.BluetoothDeviceID & 0xff); // 淡入淡出标志.对OUTPUT，System.SOUND_FIELD_INFO有效
        FrameDataBuf[11] = (byte) (DataStruct.SendDeviceData.PcCustom & 0xff); // 自定义字符，发什么下去，返回什么
        FrameDataBuf[12] = (byte) (DataStruct.SendDeviceData.DataLen & 0xff);  // 数据长度低字节
        FrameDataBuf[13] = (byte) ((DataStruct.SendDeviceData.DataLen >> 8) & 0xff); // 数据长度高字节

        if (DataStruct.SendDeviceData.FrameType == 0xa1) { // 写数据到设备

            if (DataStruct.SendDeviceData.DataType == Define.EFF) {
                if (DataStruct.SendDeviceData.DataLen == Define.EFF_LEN) {
                    for (int i = 0; i < Define.EFF_LEN; i++) {
                        FrameDataBuf[14 + i] = (byte) DataStruct.ChannelBuf[i];
                    }
                } else {
                    if (DataStruct.SendDeviceData.DataID <= Define.EFF_ID_MAX) {
                        if (DataStruct.SendDeviceData.DataID < Define.MAX_EFF_CH_EQ) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].freq & 0xff);
                            FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].freq >> 8) & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].level & 0xff);
                            FrameDataBuf[17] = (byte) ((DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].level >> 8) & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].bw & 0xff);
                            FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].bw >> 8) & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].shf_db & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.EFF.EQ[DataStruct.SendDeviceData.DataID].type & 0xff);
                        } else if (DataStruct.SendDeviceData.DataID == Define.EFF_XOVER_ID) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.EFF.h_freq & 0xff);
                            FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.EFF.h_freq >> 8) & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.EFF.h_filter & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.EFF.h_level & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.EFF.l_freq & 0xff);
                            FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.EFF.l_freq >> 8) & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.EFF.l_filter & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.EFF.l_level & 0xff);
                        } else if (DataStruct.SendDeviceData.DataID == Define.EFF_Echo_ID) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.EFF.Echo_Space & 0xff);
                            FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.EFF.Echo_Delay & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.EFF.Echo_level & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.EFF.Echo_Repeat & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.EFF.Mic_Phase & 0xff);
                            FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.EFF.R_preDelay & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.EFF.Echo_LoRatio & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.EFF.feedback & 0xff);
                        } else if (DataStruct.SendDeviceData.DataID == Define.EFF_REV_ID) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.EFF.Rev_time & 0xff);
                            FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.EFF.Rev_level & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.EFF.Rev_LoRatio & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.EFF.gain & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.EFF.Mic_mute & 0xff);
                            FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.EFF.Mic_vol & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.EFF.Music_vol & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.EFF.Rev_preDelay & 0xff);
                        } else if (DataStruct.SendDeviceData.DataID == Define.EFF_NAME_ID) {
                            for (int i = 0; i < 8; i++) {
                                FrameDataBuf[14 + i] = (byte) (DataStruct.SendDeviceData.EFF.name[i] & 0xff);
                            }
                        }
                    }
                }

            } else if (DataStruct.SendDeviceData.DataType == Define.MUSIC) {
                /*写当前组*/
                if (DataStruct.SendDeviceData.UserID == 0) {
                    if (DataStruct.CurMacMode.BOOL_USE_INS) {
                        if (DataStruct.SendDeviceData.DataLen == Define.INS_LEN) {
                            for (int i = 0; i < Define.INS_LEN; i++) {
                                FrameDataBuf[14 + i] = (byte) DataStruct.ChannelBuf[i];
                            }

                        } else {
                            if (DataStruct.SendDeviceData.DataID <= Define.INS_ID_MAX) {
                                switch (DataStruct.SendDeviceData.DataID) {
                                    case Define.INS_MISC_ID:
                                        if (DEBUG)
                                            System.out.println("Write MUSIC Channel :IN_MISC_ID");
                                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].feedback & 0xff);
                                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].polar & 0xff);
                                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].eq_mode & 0xff);
                                        FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].mute & 0xff);
                                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].delay & 0xff);
                                        FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].delay >> 8) & 0xff);
                                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].Valume & 0xff);
                                        FrameDataBuf[21] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].Valume >> 8) & 0xff);

                                        break;
                                    case Define.INS_XOVER_ID:
                                        if (DEBUG)
                                            System.out.println("Write MUSIC Channel :IN_NOISEGATE_ID");
                                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].h_freq & 0xff);
                                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].h_freq & 0xff);
                                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].h_filter & 0xff);
                                        FrameDataBuf[17] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].h_level >> 8) & 0xff);
                                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].l_freq & 0xff);
                                        FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].l_freq >> 8) & 0xff);
                                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].l_filter & 0xff);
                                        FrameDataBuf[21] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].l_level >> 8) & 0xff);
                                        break;
                                    default:
                                        break;
                                }
                            } else if (DataStruct.SendDeviceData.DataID > Define.INS_ID_MAX) {
                                if (DataStruct.SendDeviceData.DataID <= (DataStruct.CurMacMode.INS.EQ.Max_EQ + Define.INS_ID_MAX)) {
                                    FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].freq & 0xff);
                                    FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].freq >> 8) & 0xff);
                                    FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].level & 0xff);
                                    FrameDataBuf[17] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].level >> 8) & 0xff);
                                    FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].bw & 0xff);
                                    FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].bw >> 8) & 0xff);
                                    FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].shf_db & 0xff);
                                    FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.INS_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].type & 0xff);

                                }
                            }
                        }

                    } else {
                        if (DataStruct.SendDeviceData.DataLen == Define.IN_LEN) {
                            for (int i = 0; i < Define.IN_LEN; i++) {
                                FrameDataBuf[14 + i] = (byte) DataStruct.ChannelBuf[i];
                            }
                        } else {
                            switch (DataStruct.SendDeviceData.DataID) {
                                case Define.IN_MISC_ID:
                                    if (DEBUG)
                                        System.out.println("Write MUSIC Channel :IN_MISC_ID");
                                    FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.IN_CH[0].feedback & 0xff);
                                    FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.IN_CH[0].polar & 0xff);
                                    FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.IN_CH[0].mode & 0xff);
                                    FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.IN_CH[0].mute & 0xff);
                                    FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.IN_CH[0].delay & 0xff);
                                    FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.IN_CH[0].delay >> 8) & 0xff);
                                    FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.IN_CH[0].Valume & 0xff);
                                    FrameDataBuf[21] = (byte) ((DataStruct.SendDeviceData.IN_CH[0].Valume >> 8) & 0xff);
//                                    System.out.println("BUG  Write MUSIC IN_MISC_ID");
                                    break;
                                case Define.IN_NOISEGATE_ID:
                                    if (DEBUG)
                                        System.out.println("Write MUSIC Channel :IN_NOISEGATE_ID");
                                    FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.IN_CH[0].noisegate_t & 0xff);
                                    FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.IN_CH[0].noisegate_a & 0xff);
                                    FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.IN_CH[0].noisegate_k & 0xff);
                                    FrameDataBuf[17] = (byte) ((DataStruct.SendDeviceData.IN_CH[0].noisegate_k >> 8) & 0xff);
                                    FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.IN_CH[0].noisegate_r & 0xff);
                                    FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.IN_CH[0].noisegate_r >> 8) & 0xff);
                                    FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.IN_CH[0].noise_config & 0xff);
                                    FrameDataBuf[21] = (byte) ((DataStruct.SendDeviceData.IN_CH[0].noise_config >> 8) & 0xff);

                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                /*写其他组的数据，1个组的数据通道*/
                else if (DataStruct.SendDeviceData.UserID >= 1) {
                    if (DataStruct.CurMacMode.BOOL_USE_INS) {
                        for (int i = 0; i < Define.INS_LEN; i++) {
                            FrameDataBuf[14 + i] = (byte) (DataStruct.ChannelBuf[i] & 0xff);
                        }
                    } else {
                        for (int i = 0; i < Define.IN_LEN; i++) {
                            FrameDataBuf[14 + i] = (byte) DataStruct.ChannelBuf[i];
                        }
                    }

                    if (DEBUG)
                        System.out.println("Write MUSIC Channel UserID:" + DataStruct.SendDeviceData.UserID);
                }
            } else if (DataStruct.SendDeviceData.DataType == Define.OUTPUT) {



                /*写当前组*/
                if (DataStruct.SendDeviceData.UserID == 0) {
                    if (DataStruct.SendDeviceData.DataLen == Define.OUT_LEN) {//写一通道的当前数据
                        for (int i = 0; i < Define.OUT_LEN; i++) {
                            FrameDataBuf[14 + i] = (byte) (DataStruct.ChannelBuf[i] & 0xff);
                        }
                    } else {
                        if (DataStruct.SendDeviceData.DataID < Define.MAX_CHEQ) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].freq & 0xff);
                            FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].freq >> 8) & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].level & 0xff);
                            FrameDataBuf[17] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].level >> 8) & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].bw & 0xff);
                            FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].bw >> 8) & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].shf_db & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EQ[DataStruct.SendDeviceData.DataID].type & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
//                                for(int i=0;i<8;i++){
//                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
//                                }
//                            }
                        } else if (DataStruct.SendDeviceData.DataID == Define.OUT_MISC_ID) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].mute & 0xff);
                            FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].polar & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].gain & 0xff);
                            FrameDataBuf[17] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].gain >> 8) & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].delay & 0xff);
                            FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].delay >> 8) & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].eq_mode & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].LinkFlag & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
//                                for(int i=0;i<8;i++){
//                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
//                                }
//                            }
                        } else if (DataStruct.SendDeviceData.DataID == Define.OUT_XOVER_ID) {
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].h_freq & 0xff);
                            FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].h_freq >> 8) & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].h_filter & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].h_level & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].l_freq & 0xff);
                            FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].l_freq >> 8) & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].l_filter & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].l_level & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
//                                for(int i=0;i<8;i++){
//                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
//                                }
//                            }
                            if (DEBUG)
                                System.out.println("Write OUTPUT Channel OUT_XOVER_ID ChannelID :" + DataStruct.SendDeviceData.ChannelID);
                        } else if (DataStruct.SendDeviceData.DataID == Define.OUT_Valume_ID) {// id = 33		混合比例
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN1_Vol & 0xff);
                            FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN2_Vol & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN3_Vol & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN4_Vol & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN5_Vol & 0xff);
                            FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN6_Vol & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN7_Vol & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN8_Vol & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
//                                for(int i=0;i<8;i++){
//                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
//                                }
//                            }
                        } else if (DataStruct.SendDeviceData.DataID == Define.OUT_MIX_ID) {// id = 34		保留
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN9_Vol & 0xff);
                            FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN10_Vol & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN11_Vol & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN12_Vol & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN13_Vol & 0xff);
                            FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN14_Vol & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN15_Vol & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN16_Vol & 0xff);
                            //FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN_polar & 0xff);
                            //FrameDataBuf[21] = (byte)((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].IN_polar >> 8) & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
////                                for(int i=0;i<8;i++){
////                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
////                                }
////                            }
                        } else if (DataStruct.SendDeviceData.DataID == Define.OUT_LIMIT_ID) {// id = 35		压限
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].lim_t & 0xff);
                            FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].lim_t >> 8) & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].lim_a & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].lim_r & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].cliplim & 0xff);
                            FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].lim_rate & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].lim_mode & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].linkgroup_num & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
//                                for(int i=0;i<8;i++){
//                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
//                                }
//                            }
                        } else if (DataStruct.SendDeviceData.DataID == Define.OUT_NAME_ID) {// id = 36
                            FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].EncryptFlg & 0xff);
                            FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[0] & 0xff);
                            FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[1] & 0xff);
                            FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[2] & 0xff);
                            FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[3] & 0xff);
                            FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[4] & 0xff);
                            FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[5] & 0xff);
                            FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.OUT_CH[DataStruct.SendDeviceData.ChannelID].name[6] & 0xff);
//                            if(MacCfg.bool_Encryption==true){//加密
//                                for(int i=2;i<8;i++){//加密标志,联调标志不加密
//                                    FrameDataBuf[14+i]=(byte) (FrameDataBuf[14+i]^Define.Encrypt_DATA);
//                                }
//                            }
                        }
                    }
                }
                /*写其他组的数据，1个组的数据通道*/
                else if ((DataStruct.SendDeviceData.UserID >= 1) &&
                        (DataStruct.SendDeviceData.DataLen == Define.OUT_LEN)) {

                    for (int i = 0; i < Define.OUT_LEN; i++) {
                        FrameDataBuf[14 + i] = (byte) (DataStruct.ChannelBuf[i] & 0xff);
                    }
                    if (DEBUG)
                        System.out.println("Write OUTPUT Channel UserID:" + DataStruct.SendDeviceData.UserID + ",Channel:" + DataStruct.SendDeviceData.ChannelID);
                }
            } else if (DataStruct.SendDeviceData.DataType == Define.SYSTEM) {
                switch (DataStruct.SendDeviceData.ChannelID) {
                    case Define.PC_SOURCE_SET:
                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.SYS.input_source & 0xff); // 输入源(之前系统中的输入源)
                        // 高
                        // 低
                        // AUX
                        // 蓝牙
                        // 光纤
                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.SYS.aux_mode & 0xff); // 低电平模式
                        // 有3种
                        // 0:4个AUX
                        // 1:..................
                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.SYS.device_mode & 0xff); // 本字节第二位0x02
                        // 代表有数字音源输入，字节第一位0x01代表有蓝牙输入，否则没有该模块，PC不能切换至此音源
                        FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.SYS.hi_mode & 0xff);
                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.SYS.blue_gain & 0xff);
                        FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.SYS.aux_gain & 0xff);
                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.SYS.Safety & 0xff);
                        FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.SYS.sound_effect & 0xff);


                        //System.out.println("##17:"+(int)FrameDataBuf[17]+",18:"+(int)FrameDataBuf[18]+",19:"+(int)FrameDataBuf[19]);

                        break;

                    case Define.SYSTEM_DATA:
                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.SYS.main_vol & 0xff); // 输出总音量(之前输入结构中的总音量)
                        // -60~0dB
                        // 低字节
                        FrameDataBuf[15] = (byte) ((DataStruct.SendDeviceData.SYS.main_vol >> 8) & 0xff); // 输出总音量(之前输入结构中的总音量)
                        // -60~0dB
                        // 高字节
                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.SYS.alldelay & 0xff);   // DSP纯延时
                        // 0~100
                        // 0.01s~1s
                        FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.SYS.noisegate_t & 0xff);// 噪声门
                        // -120dbu~+10dbu,stp:1dbu,实际发送0~130
                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.SYS.AutoSource & 0xff); // 自动音源开关
                        // 0关
                        // 1开
                        FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.SYS.AutoSourcedB & 0xff);  // 自动音源检测的信号阀值
                        // -120dB~0dB
                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.SYS.MainvolMuteFlg & 0xff);// 静音临时标志，这个标志关机不保存，注意特别处理
                        FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.SYS.offTime & 0xff);
                        //System.out.println("##14:"+(int)FrameDataBuf[14]);
                        break;
                    case Define.SYSTEM_SPK_TYPE:
                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.SYS.out1_spk_type & 0xff);
                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.SYS.out2_spk_type & 0xff);
                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.SYS.out3_spk_type & 0xff);
                        FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.SYS.out4_spk_type & 0xff);
                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.SYS.out5_spk_type & 0xff);
                        FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.SYS.out6_spk_type & 0xff);
                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.SYS.out7_spk_type & 0xff);
                        FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.SYS.out8_spk_type & 0xff);
                        break;
                    case Define.SYSTEM_SPK_TYPEB:
                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.SYS.CurGroupID & 0xff);
                        System.out.println("BUG Define.SYSTEM_SPK_TYPEB\t");

                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.SYS.out10_spk_type & 0xff);
                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.SYS.out10_spk_type & 0xff);
                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.SYS.out11_spk_type & 0xff);
                        FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.SYS.out12_spk_type & 0xff);
                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.SYS.out13_spk_type & 0xff);
                        FrameDataBuf[19] = (byte) (DataStruct.SendDeviceData.SYS.out14_spk_type & 0xff);
                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.SYS.out15_spk_type & 0xff);
                        FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.SYS.out16_spk_type & 0xff);
                        break;
                    case Define.SOUND_FIELD_INFO:
                        for (int i = 0; i < 50; i++) {
                            FrameDataBuf[14 + i] = (byte) (DataStruct.RcvDeviceData.SYS.SoundDelayField[i] & 0xff);
                        }
                        if (DEBUG) System.out.println("Write SYSTEM Channel SOUND_FIELD_INFO");
                        break;
                    case Define.GROUP_NAME:
                        byte[] numbyte = new byte[16];
                        for (int j = 0; j < 16; j++) {
                            FrameDataBuf[14 + j] = (byte) (DataStruct.RcvDeviceData.SYS.UserGroup[DataStruct.SendDeviceData.UserID][j] & 0xff);
                            numbyte[j] = FrameDataBuf[14 + j];
                            System.out.println("BUG 装换之后的值为" + String.valueOf(numbyte[j]));
                        }
                        break;
                    case Define.SYSTEM_TRANSMITTAL://数据传输标志
                        for (int i = 0; i < 8; i++) {
                            FrameDataBuf[14 + i] = DataStruct_System.TRANSMITTAL[i];
                        }
                        break;
                    case Define.SYSTEM_RESET_MCU://复位MUC
                        for (int i = 0; i < 8; i++) {
                            FrameDataBuf[14 + i] = DataStruct_System.RESET_MCU[i];
                        }
                        break;
                    case Define.SYSTEM_RESET_GROUP_DATA://复位MUC
                        for (int i = 0; i < 8; i++) {
                            FrameDataBuf[14 + i] = DataStruct_System.RESET_GROUP_DATA[i];
                        }
                        break;
                    case Define.SYSTEM_Group:
//                        for (int i = 0; i < 8; i++) {
//                            FrameDataBuf[14 + i] = DataStruct.SendDeviceData.SYS.GROUP[i];
//                        }

                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.SYS.Play_vol & 0xff);
                        FrameDataBuf[15] = (byte) (DataStruct.SendDeviceData.SYS.Start_Def_source & 0xff);
                        FrameDataBuf[16] = (byte) (DataStruct.SendDeviceData.SYS.Source_type & 0xff);
                        FrameDataBuf[17] = (byte) (DataStruct.SendDeviceData.SYS.none4 & 0xff);
                        FrameDataBuf[18] = (byte) (DataStruct.SendDeviceData.SYS.SubVol & 0xff);
                        FrameDataBuf[19] = (byte) ((DataStruct.SendDeviceData.SYS.SubVol >> 8) & 0xff);
                        FrameDataBuf[20] = (byte) (DataStruct.SendDeviceData.SYS.none5[0] & 0xff);
                        FrameDataBuf[21] = (byte) (DataStruct.SendDeviceData.SYS.none5[1] & 0xff);
                        break;
                    case Define.CUR_PROGRAM_INFO:
//                        FrameDataBuf[14] = (byte) (DataStruct.SendDeviceData.SYS.CurGroupID & 0xff);
//                        System.out.println("BUG 下发的值为\t" + FrameDataBuf[14]);
//                        FrameDataBuf[15] = (byte) (0 & 0xff);
//                        FrameDataBuf[16] = (byte) (0 & 0xff);
//                        FrameDataBuf[17] = (byte) (0 & 0xff);
//                        FrameDataBuf[18] = (byte) (0 & 0xff);
//                        FrameDataBuf[19] = (byte) (0 & 0xff);
//                        FrameDataBuf[20] = (byte) (0 & 0xff);
//                        FrameDataBuf[21] = (byte) (0 & 0xff);
                        break;
                    default:
                        break;
                }
            } else {
                ;
            }
        }

        FrameDataSUM = FrameDataBuf[4];
        for (int i = 0; i < DataStruct.SendDeviceData.DataLen + 9; i++) {
            FrameDataSUM ^= FrameDataBuf[i + 5];
        }
//        if (FrameDataSUM == 0) {
//            return;
//        }

        FrameDataBuf[DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT - 2] = FrameDataSUM; // 校验和
        FrameDataBuf[DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT - 1] = (byte) DataStruct.FRAME_END; // 包尾

        if (sendorinsert) {
            if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI) {
//                if(DataStruct.isConnecting && mSocketClient != null){
//                    try {
//                        OutputStream os = mSocketClient.getOutputStream();
//                        os.write(FrameDataBuf, 0, DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT); // 发送到数据给设备
//                        os.flush();
//                    } catch (IOException e) {
//                        System.out.println("sThread pack(true) send error-leon");
//                    }
//                }
            } else if ((MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO)
                    || (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP)) {
                ServiceOfCom.SPP_LESendPack(FrameDataBuf,
                        DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT);
            } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_LE) {
//                BLESendPack(FrameDataBuf, DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT);
            } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_USB_HOST) {
                if (MacCfg.USBConnected) {
                    ServiceOfCom.USB_SendPack(FrameDataBuf, DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT);
                }
            } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART) {
                ServiceOfCom.UART_SendPack(FrameDataBuf, DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT);
            }

        } else {
            try {
                byte[] tempbuffer = new byte[DataStruct.SendDeviceData.DataLen
                        + DataStruct.CMD_LENGHT];
                for (int i = 0; i < DataStruct.SendDeviceData.DataLen + DataStruct.CMD_LENGHT; i++) {
                    tempbuffer[i] = FrameDataBuf[i];
                }
                DataStruct.SendbufferList.add(tempbuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void SendToButoothArry(byte[] btdata) {
        ServiceOfCom.SPP_LESendPack(btdata, btdata.length);
    }


    public static int GetChannelNum(int channel) {
        MacCfg.ChannelNumBuf[0] = DataStruct.RcvDeviceData.SYS.out1_spk_type;
        MacCfg.ChannelNumBuf[1] = DataStruct.RcvDeviceData.SYS.out2_spk_type;
        MacCfg.ChannelNumBuf[2] = DataStruct.RcvDeviceData.SYS.out3_spk_type;
        MacCfg.ChannelNumBuf[3] = DataStruct.RcvDeviceData.SYS.out4_spk_type;
        MacCfg.ChannelNumBuf[4] = DataStruct.RcvDeviceData.SYS.out5_spk_type;
        MacCfg.ChannelNumBuf[5] = DataStruct.RcvDeviceData.SYS.out6_spk_type;
        MacCfg.ChannelNumBuf[6] = DataStruct.RcvDeviceData.SYS.out7_spk_type;
        MacCfg.ChannelNumBuf[7] = DataStruct.RcvDeviceData.SYS.out8_spk_type;

        MacCfg.ChannelNumBuf[8] = DataStruct.RcvDeviceData.SYS.CurGroupID;
        MacCfg.ChannelNumBuf[9] = DataStruct.RcvDeviceData.SYS.out10_spk_type;
        MacCfg.ChannelNumBuf[10] = DataStruct.RcvDeviceData.SYS.out11_spk_type;
        MacCfg.ChannelNumBuf[11] = DataStruct.RcvDeviceData.SYS.out12_spk_type;
        MacCfg.ChannelNumBuf[12] = DataStruct.RcvDeviceData.SYS.out13_spk_type;
        MacCfg.ChannelNumBuf[13] = DataStruct.RcvDeviceData.SYS.out14_spk_type;
        MacCfg.ChannelNumBuf[14] = DataStruct.RcvDeviceData.SYS.out15_spk_type;
        MacCfg.ChannelNumBuf[15] = DataStruct.RcvDeviceData.SYS.out16_spk_type;

        for (int i = 0; i < 16; i++) {
            if (MacCfg.ChannelNumBuf[i] < 0) {
                MacCfg.ChannelNumBuf[i] = 0;
            }
//            if( MacCfg.ChannelNumBuf[i]>24){
//                MacCfg.ChannelNumBuf[i]=0;
//            }
        }


        return MacCfg.ChannelNumBuf[channel];
    }

    /**
     * 设置延时动态联调
     */
    public static void setOtherDelay(int val) {

        int TagNum = GetChannelNum(OutputChannelSel);
        boolean isContain = false;
        if (MacCfg.Delay_Type != 3) {
           // SaveDelay();
        }
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay =
                val;
        int valDll = val - MacCfg.DelayVal[MacCfg.OutputChannelSel];


        switch (MacCfg.Delay_Type) {
            case 0:
               if(IsContainGroup(MacCfg.F_Left_Sound,TagNum)){
                   setDelayData(MacCfg.F_Left_Sound,TagNum,valDll);
               }else if(IsContainGroup(MacCfg.F_Right_Sound,TagNum)){
                   setDelayData(MacCfg.F_Right_Sound,TagNum,valDll);
               }else if(IsContainGroup(MacCfg.F_Center_Sound,TagNum)){
                   setDelayData(MacCfg.F_Center_Sound,TagNum,valDll);
               }
                break;
            case 1:
                if(IsContainGroup(MacCfg.B_Left_Sound,TagNum)){
                    setDelayData(MacCfg.B_Left_Sound,TagNum,valDll);
                }else if(IsContainGroup(MacCfg.B_Right_Sound,TagNum)){
                    setDelayData(MacCfg.B_Right_Sound,TagNum,valDll);
                }else if(IsContainGroup(MacCfg.B_Center_Sound,TagNum)){
                    setDelayData(MacCfg.B_Center_Sound,TagNum,valDll);
                }
                break;
            case 2:
                if(IsContainGroup(MacCfg.B_Left_Sound,TagNum)||IsContainGroup(MacCfg.F_Left_Sound,TagNum)){
                    setDelayData(MacCfg.B_Left_Sound,TagNum,valDll);
                    setDelayData(MacCfg.F_Left_Sound,TagNum,valDll);
                }else if(IsContainGroup(MacCfg.B_Right_Sound,TagNum)||IsContainGroup(MacCfg.F_Right_Sound,TagNum)){
                    setDelayData(MacCfg.B_Right_Sound,TagNum,valDll);
                    setDelayData(MacCfg.F_Right_Sound,TagNum,valDll);
                }else if(IsContainGroup(MacCfg.B_Center_Sound,TagNum)||IsContainGroup(MacCfg.F_Center_Sound,TagNum)){
                    setDelayData(MacCfg.B_Center_Sound,TagNum,valDll);
                    setDelayData(MacCfg.F_Center_Sound,TagNum,valDll);
                }else if(IsContainGroup(MacCfg.S_Center_Sound,TagNum)||IsContainGroup(MacCfg.Sub_Center_Sound,TagNum)){
                    setDelayData(MacCfg.S_Center_Sound,TagNum,valDll);
                    setDelayData(MacCfg.Sub_Center_Sound,TagNum,valDll);
                }
                break;
            case 4:
                if(IsContainGroup(MacCfg.S_Center_Sound,TagNum)){
                    setDelayData(MacCfg.S_Center_Sound,TagNum,valDll);
                }
                break;
        }


    }


    private static void setDelayData(int[] arr,int TagNum,int valDll){
        for (int i = 0; i <arr.length ; i++) {
            if(arr[i]!=TagNum){
                if(GetChannel(arr[i])!=9){

                    DataStruct.RcvDeviceData.OUT_CH[GetChannel(arr[i])].delay = MacCfg.DelayVal[GetChannel(arr[i])]+valDll;
                    if(DataStruct.RcvDeviceData.OUT_CH[GetChannel(arr[i])].delay >
                            DataStruct.CurMacMode.Delay.MAX){
                        DataStruct.RcvDeviceData.OUT_CH[GetChannel(arr[i])].delay =
                                DataStruct.CurMacMode.Delay.MAX;
                    }
                    if(DataStruct.RcvDeviceData.OUT_CH[GetChannel(arr[i])].delay < 0){
                        DataStruct.RcvDeviceData.OUT_CH[GetChannel(arr[i])].delay = 0;
                    }
                }

            }
        }
    }

    /**
     * 判断通道类型是否存在超低中
     * */
    public static boolean useList(int[] arr, int targetValue) {
        for (int i = 0; i < Define.SUP_Type.length; i++) {

            if (Define.SUP_Type[i] == (targetValue)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 保存所有通道的延时用作动态联调
     */
    public static void SaveDelay() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            MacCfg.DelayVal[i] = DataStruct.RcvDeviceData.OUT_CH[i].delay;
        }
    }

    /**
     * 根据通道类型获取当前通道
     * */
    public static int GetChannel(int spk) {
        int Num=9;   //随便定义一个值  用作判断有没有这个类型
        for (int i = 0; i <DataStruct.CurMacMode.Out.OUT_CH_MAX_USE ; i++) {
            if(GetChannelNum(i)==spk){
                Num=i;
                break;
            }
        }
        return Num;
    }

    /**
     * 判断数组中是否包含这个值(判断当前的通道类型在那个延时联调组中)
     * */
    public static boolean IsContainGroup(int[] arr, int value) {
        boolean isContain = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                isContain = true;
                break;
            }
        }

        return isContain;

    }

    public static String GetChannelName(int channel, Context mcontext) {
        String rs = "NULL";
        switch (channel) {
            case 0:
                rs = mcontext.getResources().getString(R.string.NULL);
                break;
            case 1:
                rs = mcontext.getResources().getString(R.string.FL_Tweeter);
                break;
            case 2:
                rs = mcontext.getResources().getString(R.string.FL_Midrange);
                break;
            case 3:
                rs = mcontext.getResources().getString(R.string.FL_Woofer);
                break;
            case 4:
                //rs = mcontext.getResources().getString(R.string.FL_M_T);
                break;
            case 5:
                rs = mcontext.getResources().getString(R.string.FL_M_WF);
                break;
            case 6:
                rs = mcontext.getResources().getString(R.string.FL_Full);
                break;
            case 7:
                rs = mcontext.getResources().getString(R.string.FR_Tweeter);
                break;
            case 8:
                rs = mcontext.getResources().getString(R.string.FR_Midrange);
                break;
            case 9:
                rs = mcontext.getResources().getString(R.string.FR_Woofer);
                break;
            case 10:
                //rs = mcontext.getResources().getString(R.string.FR_M_T);
                break;
            case 11:
                rs = mcontext.getResources().getString(R.string.FR_M_WF);
                break;
            case 12:
                rs = mcontext.getResources().getString(R.string.FR_Full);
                break;
            case 13:
                rs = mcontext.getResources().getString(R.string.RL_Tweeter);
                break;
            case 14:
                rs = mcontext.getResources().getString(R.string.RL_Woofer);
                break;
            case 15:
                rs = mcontext.getResources().getString(R.string.RL_Full);
                break;
            case 16:
                rs = mcontext.getResources().getString(R.string.RR_Tweeter);
                break;
            case 17:
                rs = mcontext.getResources().getString(R.string.RR_Woofer);
                break;
            case 18:
                rs = mcontext.getResources().getString(R.string.RR_Full);
                break;
            case 19:
                rs = mcontext.getResources().getString(R.string.Front_Full_higt);
                break;
            case 20:
                rs = mcontext.getResources().getString(R.string.Front_Full);
                break;
            case 21:
                rs = mcontext.getResources().getString(R.string.C_Rear_Woofer);
                break;
            case 22:
                rs = mcontext.getResources().getString(R.string.L_Subweeter);
                break;
            case 23:
                rs = mcontext.getResources().getString(R.string.R_Subweeter);
                break;
            case 24:
                rs = mcontext.getResources().getString(R.string.Subweeter);
                break;
            case 25:
                rs = mcontext.getResources().getString(R.string.Input_type_26);
                break;
            case 26:
                rs = mcontext.getResources().getString(R.string.Input_type_27);
                break;
            case 27:
                rs = mcontext.getResources().getString(R.string.Input_type_28);
                break;
            case 28:
                rs = mcontext.getResources().getString(R.string.Input_type_29);
                break;
            case 29:
                rs = mcontext.getResources().getString(R.string.RR_center);
                break;
            case 30:
                rs = mcontext.getResources().getString(R.string.RL_Center_Woofer);
                break;
            case 31:
                rs = mcontext.getResources().getString(R.string.RR_Center_Woofer);
                break;

            default:
                rs = mcontext.getResources().getString(R.string.NULL);
                break;
        }
        return rs;
    }


    /**
     * 设置每个通道的类型
     */
    public static void setOutputSpk(int type, int channel) {  //type为类型  channel为通道
        switch (channel) {
            case 0:
                DataStruct.RcvDeviceData.SYS.out1_spk_type = type;
                break;
            case 1:
                DataStruct.RcvDeviceData.SYS.out2_spk_type = type;
                break;
            case 2:
                DataStruct.RcvDeviceData.SYS.out3_spk_type = type;
                break;
            case 3:
                DataStruct.RcvDeviceData.SYS.out4_spk_type = type;
                break;
            case 4:
                DataStruct.RcvDeviceData.SYS.out5_spk_type = type;
                break;
            case 5:
                DataStruct.RcvDeviceData.SYS.out6_spk_type = type;
                break;
            case 6:
                DataStruct.RcvDeviceData.SYS.out7_spk_type = type;
                break;
            case 7:
                DataStruct.RcvDeviceData.SYS.out8_spk_type = type;
                break;
            case 8:
                DataStruct.RcvDeviceData.SYS.CurGroupID = type;
                break;
            case 9:
                DataStruct.RcvDeviceData.SYS.out10_spk_type = type;
                break;
            case 10:
                DataStruct.RcvDeviceData.SYS.out11_spk_type = type;
                break;
            case 11:
                DataStruct.RcvDeviceData.SYS.out12_spk_type = type;
                break;
            case 12:
                DataStruct.RcvDeviceData.SYS.out13_spk_type = type;
                break;
            case 13:
                DataStruct.RcvDeviceData.SYS.out14_spk_type = type;
                break;
            case 14:
                DataStruct.RcvDeviceData.SYS.out15_spk_type = type;
                break;
            case 15:
                DataStruct.RcvDeviceData.SYS.out16_spk_type = type;
                break;
            default:
                break;
        }

    }


    /**
     * 设置选择通道类型后的斜率值
     */
    public static void setXOverWithOutputSPKType(int i) {



        /**
         * 前/后 高频
         * */
        for (int j = 0; j < Define.HighFreq.length; j++) {
            if (Define.HighFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.HighFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.HighFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.HighFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.HighFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.HighFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.HighFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.HighFreq_LPLEVEL;
                    ;
                }
            }
        }


        /**
         * 中频
         * */
        for (int j = 0; j < Define.MidFreq.length; j++) {
            if (Define.MidFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.MidFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.MidFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.MidFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.MidFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.MidFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.MidFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.MidFreq_LPLEVEL;
                    ;
                }
            }
        }
       /**
        * 低频
        * */
        for (int j = 0; j < Define.LowFreq.length; j++) {
            if (Define.LowFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.LowFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.LowFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.LowFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.LowFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.LowFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.LowFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.LowFreq_LPLEVEL;
                }
            }
        }
        /**
         * 中低
         * */
        for (int j = 0; j < Define.MidLowFreq.length; j++) {
            if (Define.MidLowFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.MidLowFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.MidLowFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.MidLowFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.MidLowFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.MidLowFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.MidLowFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.MidLowFreq_LPLEVEL;
                    ;
                }
            }
        }

        /**
         * 全频
         * */
        for (int j = 0; j < Define.AllFreq.length; j++) {
            if (Define.AllFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.AllFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.AllFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.AllFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.AllFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.AllFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.AllFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.AllFreq_LPLEVEL;
                    ;
                }
            }
        }


        /**
         * 中置
         * */
        for (int j = 0; j < Define.CenterFreq.length; j++) {
            if (Define.CenterFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.CenterFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.CenterFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.CenterFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.CenterFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.CenterFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.CenterFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.CenterFreq_LPLEVEL;
                    ;
                }
            }
        }

        /**
         * 中置高
         * */
        for (int j = 0; j < Define.MidHighFreq.length; j++) {
            if (Define.MidHighFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.MidHighFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.MidHighFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.MidHighFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.MidHighFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.MidHighFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.MidHighFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.MidHighFreq_LPLEVEL;
                    ;
                }
            }
        }

        /**
         * 超低
         * */
        for (int j = 0; j < Define.SupperLowFreq.length; j++) {
            if (Define.SupperLowFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.SupperLowFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.SupperLowFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.SupperLowFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.SupperLowFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.SupperLowFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.SupperLowFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.SupperLowFreq_LPLEVEL;
                    ;
                }
            }
        }

        /**
         * 环绕
         * */
        for (int j = 0; j < Define.SurroundFreq.length; j++) {
            if (Define.SurroundFreq[j] != Define.EndFlag) {
                if (MacCfg.ChannelNumBuf[i] == Define.SurroundFreq[j]) {
                    DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.SurroundFreq_HPFreq;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.SurroundFreq_LPFreq;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_filter = Define.SurroundFreq_HPFilter;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_filter = Define.SurroundFreq_LPFilter;

                    DataStruct.RcvDeviceData.OUT_CH[i].h_level = Define.SurroundFreq_HPLEVEL;
                    DataStruct.RcvDeviceData.OUT_CH[i].l_level = Define.SurroundFreq_LPLEVEL;
                    ;
                }
            }
        }




    }
    /***
     *根据通道类型设置通道音量值
     * */
    public static void SetOutputVolume(int chsel) {
        if (DataOptUtil.useList(Define.SUP_Type, DataOptUtil.GetChannelNum(chsel))) {
            DataStruct.RcvDeviceData.OUT_CH[chsel].gain=360;
        }else{
            DataStruct.RcvDeviceData.OUT_CH[chsel].gain=600;
        }

    }


    //设置默认的混音值(被动)
    public static void SetInputSourceMixerVol(int chsel) {
        int spk_type = 0;
        switch (chsel) {
            case 0:
                spk_type = DataStruct.RcvDeviceData.SYS.out1_spk_type;
                break;
            case 1:
                spk_type = DataStruct.RcvDeviceData.SYS.out2_spk_type;
                break;
            case 2:
                spk_type = DataStruct.RcvDeviceData.SYS.out3_spk_type;
                break;
            case 3:
                spk_type = DataStruct.RcvDeviceData.SYS.out4_spk_type;
                break;
            case 4:
                spk_type = DataStruct.RcvDeviceData.SYS.out5_spk_type;
                break;
            case 5:
                spk_type = DataStruct.RcvDeviceData.SYS.out6_spk_type;
                break;
            case 6:
                spk_type = DataStruct.RcvDeviceData.SYS.out7_spk_type;
                break;
            case 7:
                spk_type = DataStruct.RcvDeviceData.SYS.out8_spk_type;
                break;

            case 8:
                spk_type = DataStruct.RcvDeviceData.SYS.CurGroupID;
                break;
            case 9:
                spk_type = DataStruct.RcvDeviceData.SYS.out10_spk_type;
                break;
            case 10:
                spk_type = DataStruct.RcvDeviceData.SYS.out11_spk_type;
                break;
            case 11:
                spk_type = DataStruct.RcvDeviceData.SYS.out12_spk_type;
                break;
            case 12:
                spk_type = DataStruct.RcvDeviceData.SYS.out13_spk_type;
                break;
            case 13:
                spk_type = DataStruct.RcvDeviceData.SYS.out14_spk_type;
                break;
            case 14:
                spk_type = DataStruct.RcvDeviceData.SYS.out15_spk_type;
                break;
            case 15:
                spk_type = DataStruct.RcvDeviceData.SYS.out16_spk_type;
                break;
            default:
                break;
        }

        switch (spk_type) {
            case 0://空
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;
            case 1://前置左
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActive(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;

            case 7://前置右
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActive(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 100;
                break;
            case 13://后置左
            case 14:
            case 15:
            case 30:
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActive(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;
            case 16://后置右
            case 17:
            case 18:
            case 31:
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 100;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActive(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;
            case 19://前中置
            case 20:
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActiveAllType(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;
            case 21:
            case 29:
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 100;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActiveAllType(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;


            case 22://左超低
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;

                } else {
                    isActiveAllType(chsel);
                }

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;
            case 23://右超低
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;

                } else {
                    isActiveAllType(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 100;
                break;
            case 24://超低
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActiveAllType(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;

            case 27://左环绕
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol =0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 100;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 0;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol =0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActive(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;

            case 28://右环绕
                if (DataStruct.RcvDeviceData.SYS.none4 == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN1_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN2_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN3_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN4_Vol = 100;

                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN5_Vol = 0;
                    DataStruct.RcvDeviceData.OUT_CH[chsel].IN6_Vol = 0;
                } else {
                    isActive(chsel);
                }
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN7_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN8_Vol = 100;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN9_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN10_Vol = 100;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN11_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN12_Vol = 0;

                DataStruct.RcvDeviceData.OUT_CH[chsel].IN13_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN14_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN15_Vol = 0;
                DataStruct.RcvDeviceData.OUT_CH[chsel].IN16_Vol = 0;
                break;


            default:
                break;
        }
    }


    /**
     * 判断该地方为主动输入
     */
    private static boolean isActive(int Output) {
        if (Output % 2 == 0) {
            DataStruct.RcvDeviceData.OUT_CH[Output].IN1_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN2_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN3_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN4_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[Output].IN5_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN6_Vol = 0;
        } else {
            DataStruct.RcvDeviceData.OUT_CH[Output].IN1_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN2_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN3_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN4_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN5_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN6_Vol = 100;
        }

        return false;

    }



    /**
     * 判断该地方为主动输入(超低、前中置、后中置)
     */
    private static boolean isActiveAllType(int Output) {
            DataStruct.RcvDeviceData.OUT_CH[Output].IN1_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN2_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN3_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN4_Vol = 100;

            DataStruct.RcvDeviceData.OUT_CH[Output].IN5_Vol = 100;
            DataStruct.RcvDeviceData.OUT_CH[Output].IN6_Vol = 100;


        return false;

    }


    /*
     * @param DataStruchID:要初始化的数据ID
     * @param ChannelID:要初始化的数据通道ID
     * @param initData：赋值的数据
     * @param ReadFromeBT:是否从机器读取上来的
     */
    public static void FillRecDataStruct(int DataStruchID, int ChannelID, int initData[], boolean ReadFromeBT) {
        int initDataCnt = 0;
        /*从下面机器读取的要去通信字*/
        if (ReadFromeBT) {
            initDataCnt = 10;
        }
//        if((DataStruchID==Define.OUTPUT)&&(ReadFromeBT)){
//            if(ChannelID==0){
//                System.out.println("FUCK Encrypt_DATA=" + initData[298]);
//                System.out.println("FUCK LINK=" + initData[299]);
//            }
//
//            //只能放这里，多出10个
//            if(DataStruct.CurMacMode.BOOL_ENCRYPTION){
//                if((ChannelID==0)&&(DataStruchID==Define.OUTPUT)){
//                    System.out.println("Encryption Flag=" + initData[298]);
//                }
//                //第一通判断数据是否加密，298=296+10-8
//                if((ChannelID==0)&&(DataStruchID==Define.OUTPUT)){
//                    System.out.println("Encryption 1Flag=" + initData[298]);
//
//                    if(initData[298] != Define.EncryptionFlag){//非加密
//                        MacCfg.bool_Encryption=false;
//                    }else if(initData[298] == Define.EncryptionFlag){//加密
//                        MacCfg.bool_Encryption=true;
//                        for(int i=10;i<(Define.OUT_LEN+10);i++){
//                            if(i!=298){
//                                if(i!=299){
//                                    initData[i]=initData[i]^Define.Encrypt_DATA;
//                                }
//                            }
//                        }
//                    }
//                }
//                //若第一通判断数据加密，其他通道也加密
//                if((ChannelID > 0)&&(MacCfg.bool_Encryption == true)&&(DataStruchID == Define.OUTPUT)){
//                    System.out.println("Encryption ChannelID=" + ChannelID);
//                    for(int i=10;i<(Define.OUT_LEN+10);i++){
//                        if(i!=298){
//                            initData[i]=initData[i]^Define.Encrypt_DATA;
//                        }
//                    }
//                }
//            }
//        }else{
//            if(DataStruct.CurMacMode.BOOL_ENCRYPTION){
//                if((ChannelID==0)&&(DataStruchID==Define.OUTPUT)){
//                    System.out.println("Encryption Flag=" + initData[288]);
//                }
//                //第一通判断数据是否加密，298=296+10-8
//                if((ChannelID==0)&&(DataStruchID==Define.OUTPUT)){
//                    System.out.println("Encryption 1Flag=" + initData[288]);
//
//                    if(initData[288] != Define.EncryptionFlag){//非加密
//                        MacCfg.bool_Encryption=false;
//                    }else if(initData[288] == Define.EncryptionFlag){//加密
//                        MacCfg.bool_Encryption=true;
//                        for(int i=0;i<(Define.OUT_LEN);i++){
//                            if(i!=288){
//                                if(i!=289){
//                                    initData[i]=initData[i]^Define.Encrypt_DATA;
//                                }
//                            }
//                        }
//                    }
//                }
//                //若第一通判断数据加密，其他通道也加密
//                if((ChannelID > 0)&&(MacCfg.bool_Encryption == true)&&(DataStruchID == Define.OUTPUT)){
//                    System.out.println("Encryption ChannelID=" + ChannelID);
//                    for(int i=0;i<(Define.OUT_LEN);i++){
//                        if(i!=288){
//                            initData[i]=initData[i]^Define.Encrypt_DATA;
//                        }
//                    }
//                }
//            }

        //    }
        /*初始化数据结构的ID*/
        if (DataStruchID == Define.EFF) {
            for (int i = 0; i < Define.MAX_EFF_CH_EQ; i++) {//Define.MAX_CHEQ
                DataStruct.RcvDeviceData.EFF.EQ[i].freq = initData[initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.EFF.EQ[i].level = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.EFF.EQ[i].bw = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.EFF.EQ[i].shf_db = initData[++initDataCnt];
                DataStruct.RcvDeviceData.EFF.EQ[i].type = initData[++initDataCnt];
                ++initDataCnt;
            }
            //高低通 ,ID = 8	(xover限MIC)
            DataStruct.RcvDeviceData.EFF.h_freq = initData[initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.EFF.h_filter = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.h_level = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.l_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.EFF.l_filter = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.l_level = initData[++initDataCnt];
            //回声 ,ID  = 9
            DataStruct.RcvDeviceData.EFF.Echo_Space = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Echo_Delay = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Echo_level = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Echo_Repeat = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Mic_Phase = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.R_preDelay = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Echo_LoRatio = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.feedback = initData[++initDataCnt];
            //Effect ,ID= 10        混响
            DataStruct.RcvDeviceData.EFF.Rev_time = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Rev_level = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Rev_LoRatio = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.gain = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Mic_mute = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Mic_vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Music_vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.EFF.Rev_preDelay = initData[++initDataCnt];

            for (int i = 0; i < 8; i++) {
                DataStruct.RcvDeviceData.EFF.name[i] = (byte) initData[++initDataCnt];
            }

        } else if (DataStruchID == Define.MUSIC) {
            if (DataStruct.CurMacMode.BOOL_USE_INS) {
                for (int ch = 0; ch < DataStruct.CurMacMode.INS.INS_CH_MAX; ch++) {
                    //---id = 9		杂项
                    DataStruct.RcvDeviceData.INS_CH[ch].feedback = initData[initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].polar = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].eq_mode = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].mute = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].delay = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                    DataStruct.RcvDeviceData.INS_CH[ch].Valume = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                    //高低通 ,ID = 10
                    DataStruct.RcvDeviceData.INS_CH[ch].h_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                    DataStruct.RcvDeviceData.INS_CH[ch].h_filter = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].h_level = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].l_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                    DataStruct.RcvDeviceData.INS_CH[ch].l_filter = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.INS_CH[ch].l_level = initData[++initDataCnt];

                    //EQ
                    for (int i = 0; i < DataStruct.CurMacMode.INS.EQ.Max_EQ; i++) {
                        DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                        DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].level = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                        DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].bw = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                        DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].shf_db = initData[++initDataCnt];
                        DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].type = initData[++initDataCnt];
                    }
                    ++initDataCnt;
                }
            } else {
                //EQ
                for (int i = 0; i < Define.MAX_IN_EQ; i++) {
                    DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].freq = initData[initDataCnt] + initData[++initDataCnt] * 256;
                    DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].level = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                    DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].bw = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                    DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].shf_db = initData[++initDataCnt];
                    DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].type = initData[++initDataCnt];
                    ++initDataCnt;
                }
                //---id = 9		杂项
                DataStruct.RcvDeviceData.IN_CH[ChannelID].feedback = initData[initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].polar = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].mode = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].mute = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].delay = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.IN_CH[ChannelID].Valume = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                //高低通 ,ID = 10
                DataStruct.RcvDeviceData.IN_CH[ChannelID].h_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.IN_CH[ChannelID].h_filter = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].h_level = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].l_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.IN_CH[ChannelID].l_filter = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].l_level = initData[++initDataCnt];
                //噪声门 ,ID = 11
                DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_t = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_a = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_k = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_r = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.IN_CH[ChannelID].noise_config = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                //压限 ,ID = 12
                DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_t = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_a = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_r = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].cliplim = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_rate = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_mode = initData[++initDataCnt];
                DataStruct.RcvDeviceData.IN_CH[ChannelID].comp_swi = initData[++initDataCnt];
                //name[8] ID = 13
                for (int i = 0; i < 8; i++) {
                    DataStruct.RcvDeviceData.IN_CH[ChannelID].name[i] = (byte) initData[++initDataCnt];
                }

                //System.out.println("BUG　-DataStruct.RcvDeviceData.IN_CH["+ChannelID+"].Valume="+DataStruct.RcvDeviceData.IN_CH[ChannelID].Valume);
            }


        } else if (DataStruchID == Define.OUTPUT) {
            //EQ
            for (int i = 0; i < Define.MAX_CHEQ; i++) {//Define.MAX_CHEQ
                DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].freq = initData[initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].level = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].bw = initData[++initDataCnt] + initData[++initDataCnt] * 256;
                DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].shf_db = initData[++initDataCnt];
                DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].type = initData[++initDataCnt];
                ++initDataCnt;
                //System.out.println("BUG　DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].EQ["+i+"].level=" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].level);
                //检测数据是否异常
                if (ReadFromeBT) {
                    if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].level < DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN)
                            || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].level > DataStruct.CurMacMode.EQ.EQ_LEVEL_MAX)) {
                        DataStruct.U0SynDataError = true;
                    }

                    if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].bw < 0)
                            || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].bw > Define.EQ_BW_MAX)) {
                        DataStruct.U0SynDataError = true;
                    }

                    if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].freq < 20)
                            || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].freq > 20000)) {
                        DataStruct.U0SynDataError = true;
                    }
                }
            }
            //id = 31		杂项
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].mute = initData[initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].polar = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].gain = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].delay = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].eq_mode = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].LinkFlag = initData[++initDataCnt];
            //检测数据是否异常
            if (ReadFromeBT) {
                if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].gain < 0)
                        || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].gain > DataStruct.CurMacMode.Out.MaxOutVol)) {
                    //System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].gain" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].gain);
                    DataStruct.U0SynDataError = true;
                }

                if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].delay < 0)
                        || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].delay > DataStruct.CurMacMode.Delay.MAX)) {
                    //System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].delay" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].delay);
                    DataStruct.U0SynDataError = true;
                }
            }
            //高低通 ,ID = 32	(xover限MIC)
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_filter = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_level = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_filter = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_level = initData[++initDataCnt];

//            System.out.println("BUG DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].h_freq=" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq);
//            System.out.println("BUG DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].l_freq=" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq);
            //检测数据是否异常
            if (ReadFromeBT) {
                if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq < 0)
                        || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq > 20000)) {

                    System.out.println("BUG U0SynDataError DataStruct.RcvDeviceData.OUT_CH[" + ChannelID + "].h_freq" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq);
                    DataStruct.U0SynDataError = true;
                }

                if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_filter < 0)
                        || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_filter > 2)) {
                    System.out.println("BUG U0SynDataError DataStruct.RcvDeviceData.OUT_CH[" + ChannelID + "].h_filter" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_filter);
                    DataStruct.U0SynDataError = true;
                }

//                if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_level < 0)
//                        ||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_level > 7)){
//                    System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].h_level" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_level);
//                    DataStruct.U0SynDataError=true;
//                }

                if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq < 0)
                        || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq > 20000)) {
                    System.out.println("DataStruct.RcvDeviceData.OUT_CH[" + ChannelID + "].l_freq" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq);
                    DataStruct.U0SynDataError = true;
                }

                if ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_filter < 0)
                        || (DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_filter > 2)) {
                    System.out.println("DataStruct.RcvDeviceData.OUT_CH[" + ChannelID + "].l_filter" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_filter);
                    DataStruct.U0SynDataError = true;
                }

//                if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_level < 0)
//                        ||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_level > 7)){
//                    System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].l_level" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_level);
//                    DataStruct.U0SynDataError=true;
//                }
            }
            // id = 33		混合比例
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN1_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN2_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN3_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN4_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN5_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN6_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN7_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN8_Vol = initData[++initDataCnt];
            // id = 34		保留
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN9_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN10_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN11_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN12_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN13_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN14_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN15_Vol = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN16_Vol = initData[++initDataCnt];
            //DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN_polar   = initData[++initDataCnt]+initData[++initDataCnt]*256;
            //DataStruct.RcvDeviceData.OUT_CH[ChannelID].none11   = initData[++initDataCnt];
            //检测数据是否异常
			/*
			if(ReadFromeBT){
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN1_Vol < 0)
	    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN1_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN1_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN1_Vol);
	    			DataStruct.U0SynDataError=true;
	    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN2_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN2_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN2_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN2_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN3_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN3_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN3_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN3_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN4_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN4_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN4_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN4_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN5_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN5_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN5_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN5_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN6_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN6_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN6_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN6_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN7_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN7_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN7_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN7_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN8_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN8_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN8_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN8_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN9_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN9_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN9_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN9_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN10_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN10_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN10_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN10_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN11_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN11_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN11_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN11_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN12_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN12_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN12_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN12_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN13_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN13_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN13_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN13_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN14_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN14_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN14_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN14_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN15_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN15_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN15_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN15_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
				if((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN16_Vol < 0)
		    			||(DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN16_Vol > MacCfg.MAX_INPUT_VALUME)){
					System.out.println("DataStruct.RcvDeviceData.OUT_CH["+ChannelID+"].IN16_Vol" + DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN16_Vol);
		    			DataStruct.U0SynDataError=true;
		    		}
			}
			*/
            // id = 35		压限
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_t = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_a = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_r = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].cliplim = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_rate = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_mode = initData[++initDataCnt];
            DataStruct.RcvDeviceData.OUT_CH[ChannelID].linkgroup_num = initData[++initDataCnt];

            DataStruct.RcvDeviceData.OUT_CH[ChannelID].EncryptFlg = initData[++initDataCnt];
            //name[8]  ID = 36
            for (int i = 0; i < 7; i++) {
                DataStruct.RcvDeviceData.OUT_CH[ChannelID].name[i] = (byte) initData[++initDataCnt];
            }


            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                if (DataStruct.RcvDeviceData.OUT_CH[i].EncryptFlg == 0x21) {
                    MacCfg.bool_Encryption = true;
                    break;
                } else {
                    MacCfg.bool_Encryption = false;
                }
            }

//                if(ChannelID==7){
//                    MacCfg.bool_Encryption = false;
//                }
            //  }

            // System.out.println("BUG 通道"+ChannelID+"\ti==="+DataStruct.RcvDeviceData.OUT_CH[ChannelID].EncryptFlg+"是否加密"+MacCfg.bool_Encryption );

//            if (DataStruct.RcvDeviceData.OUT_CH[0].EncryptFlg == 0x21 || DataStruct.RcvDeviceData.OUT_CH[7].EncryptFlg == 0x21) {
//
//            } else {
//                MacCfg.bool_Encryption = false;
//            }

            //  if(DataStruct.CurMacMode.BOOL_ENCRYPTION){
//                if((ChannelID==0)&&(MacCfg.bool_Encryption==true)){
//                    for(int i=0;i<6;i++){
//                        MacCfg.Encryption_PasswordBuf[i]=DataStruct.RcvDeviceData.OUT_CH[ChannelID].name[2+i];
//                        System.out.println("Encryption Encryption_PasswordBuf-"+i+"=" + MacCfg.Encryption_PasswordBuf[i]);
//                    }
//                }
            //  }

        } else if (DataStruchID == Define.SYSTEM) {
            DataStruct.RcvDeviceData.SYS.input_source = initData[initDataCnt];
            DataStruct.RcvDeviceData.SYS.aux_mode = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.device_mode = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.hi_mode = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.blue_gain = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.aux_gain = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.Safety = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.sound_effect = initData[++initDataCnt];

            DataStruct.RcvDeviceData.SYS.main_vol = initData[++initDataCnt] + initData[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.SYS.alldelay = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.noisegate_t = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.AutoSource = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.AutoSourcedB = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.offTime = initData[++initDataCnt];

            DataStruct.RcvDeviceData.SYS.out1_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out2_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out3_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out4_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out5_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out6_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out7_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out8_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.CurGroupID = initData[++initDataCnt];

            System.out.println("BUG  DataStruct.RcvDeviceData.SYS.out9_spk_type\t"
                    + DataStruct.RcvDeviceData.SYS.CurGroupID + "\t"
                    + DataStruct.RcvDeviceData.SYS.input_source + "音源" + DataStruct.RcvDeviceData.SYS.aux_mode);
            DataStruct.RcvDeviceData.SYS.out10_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out11_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out12_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out13_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out14_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out15_spk_type = initData[++initDataCnt];
            DataStruct.RcvDeviceData.SYS.out16_spk_type = initData[++initDataCnt];
        }
    }

    /*
     * @param DataStruchID:要初始化的数据ID
     * @param ChannelID:要初始化的数据通道ID
     * @param initBuf：赋值的数据
     */
    public static void FillSedDataStruct(int DataStruchID, int[] initBuf) {
        if (DataStruchID == Define.EFF) {
            for (int i = 0; i < Define.EFF_LEN; i++) {
                DataStruct.ChannelBuf[i] = initBuf[i];
            }
        } else if (DataStruchID == Define.MUSIC) {
            if (DataStruct.CurMacMode.BOOL_USE_INS) {
                for (int i = 0; i < Define.INS_LEN; i++) {
                    DataStruct.ChannelBuf[i] = initBuf[i];
                }
            } else {
                for (int i = 0; i < Define.IN_LEN; i++) {
                    DataStruct.ChannelBuf[i] = initBuf[i];
                }
            }

        } else if (DataStruchID == Define.OUTPUT) {
            for (int i = 0; i < Define.OUT_LEN; i++) {
                DataStruct.ChannelBuf[i] = initBuf[i];
            }
        }
    }

    /*
     * @param DataStruchID:要初始化的数据ID
     * @param ChannelID:要初始化的数据通道ID
     * @param initData：赋值的数据
     * @param dataSize：赋值的数据的大小
     */
    public static void FillSedDataStruct(int DataStruchID, int ChannelID) {
        int ChCnt = 0;
        /*初始化数据结构的ID*/
        if (DataStruchID == Define.EFF) {
            for (int i = 0; i < Define.MAX_EFF_CH_EQ; i++) {
                DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.EFF.EQ[i].freq & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.EFF.EQ[i].freq >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.EQ[i].level & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.EFF.EQ[i].level >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.EQ[i].bw & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.EFF.EQ[i].bw >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.EQ[i].shf_db & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.EQ[i].type & 0xff);
                ++ChCnt;
            }
            //高低通 ,ID = 32	(xover限MIC)
            DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.EFF.h_freq & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.EFF.h_freq >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.h_filter & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.h_level & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.l_freq & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.EFF.l_freq >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.l_filter & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.l_level & 0xff);
            //----id = 9	        回声
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Echo_Space & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Echo_Delay & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Echo_level & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Echo_Repeat & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Mic_Phase & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.R_preDelay & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Echo_LoRatio & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.feedback & 0xff);
            //Effect ,ID= 10        混响
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Rev_time & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Rev_level & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Rev_LoRatio & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.gain & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Mic_mute & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Mic_vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Music_vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.EFF.Rev_preDelay & 0xff);

            //name[8] ID = 11
            for (int i = 0; i < 8; i++) {
                DataStruct.ChannelBuf[++ChCnt] = DataStruct.RcvDeviceData.EFF.name[i];
            }
        } else if (DataStruchID == Define.MUSIC) {
            if (DataStruct.CurMacMode.BOOL_USE_INS) {
                for (int ch = 0; ch < DataStruct.CurMacMode.INS.INS_CH_MAX; ch++) {
                    //---id = 9		杂项
                    DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].feedback & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].polar & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].eq_mode & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].mute & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].delay & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].delay >> 8) & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].Valume & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].Valume >> 8) & 0xff);
                    //高低通 ,ID = 10
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].h_freq & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].h_freq >> 8) & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].h_filter & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].h_level & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].l_freq & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].l_freq >> 8) & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].l_filter & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].l_level & 0xff);
                    //EQ
                    for (int i = 0; i < DataStruct.CurMacMode.INS.EQ.Max_EQ; i++) {
                        DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].freq & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].freq >> 8) & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].level & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].level >> 8) & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].bw & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].bw >> 8) & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].shf_db & 0xff);
                        DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.INS_CH[ch].EQ[i].type & 0xff);
                    }
                    ++ChCnt;
                }
            } else {
                for (int i = 0; i < Define.MAX_IN_EQ; i++) {
                    DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].freq & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].freq >> 8) & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].level & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].level >> 8) & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].bw & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].bw >> 8) & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].shf_db & 0xff);
                    DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].EQ[i].type & 0xff);
                    ++ChCnt;
                }
                //---id = 9		杂项
                DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].feedback & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].polar & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].mode & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].mute & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].delay & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].delay >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].Valume & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].Valume >> 8) & 0xff);
                //高低通 ,ID = 10
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].h_freq & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].h_freq >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].h_filter & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].h_level & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].l_freq & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].l_freq >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].l_filter & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].l_level & 0xff);
                //噪声门 ,ID = 11
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_t & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_a & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_k & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_k >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_r & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].noisegate_r >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].noise_config & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].noise_config >> 8) & 0xff);
                //压限 ,ID = 12
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_t & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_t >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_a & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_r & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].cliplim & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_rate & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].lim_mode & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.IN_CH[ChannelID].comp_swi & 0xff);
                //name[8] ID = 13
                for (int i = 0; i < 8; i++) {
                    DataStruct.ChannelBuf[++ChCnt] = DataStruct.RcvDeviceData.IN_CH[i].name[i];
                }
            }

        } else if (DataStruchID == Define.OUTPUT) {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].freq & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].freq >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].level & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].level >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].bw & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].bw >> 8) & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].shf_db & 0xff);
                DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].EQ[i].type & 0xff);
                ++ChCnt;
            }
            //id = 31		杂项
            DataStruct.ChannelBuf[ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].mute & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].polar & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].gain & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].gain >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].delay & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].delay >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].eq_mode & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].LinkFlag & 0xff);
            //高低通 ,ID = 32	(xover限MIC)
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_freq >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_filter & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].h_level & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_freq >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_filter & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].l_level & 0xff);
            // id = 33		混合比例
            //高低通 ,ID = 32	(xover限MIC)
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN1_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN2_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN3_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN4_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN5_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN6_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN7_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN8_Vol & 0xff);
            // id = 34		保留
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN9_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN10_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN11_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN12_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN13_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN14_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN15_Vol & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN16_Vol & 0xff);
            //DataStruct.ChannelBuf[++ChCnt]= (DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN_polar & 0xff);
            //DataStruct.ChannelBuf[++ChCnt]= ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].IN_polar >> 8) & 0xff);
            // id = 35		压限
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_t & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = ((DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_t >> 8) & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_a & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_r & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].cliplim & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_rate & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].lim_mode & 0xff);
            DataStruct.ChannelBuf[++ChCnt] = (DataStruct.RcvDeviceData.OUT_CH[ChannelID].linkgroup_num & 0xff);
            //name[8]  ID = 36
            DataStruct.ChannelBuf[++ChCnt] = DataStruct.RcvDeviceData.OUT_CH[ChannelID].EncryptFlg;
            for (int i = 0; i < 7; i++) {
                DataStruct.ChannelBuf[++ChCnt] = DataStruct.RcvDeviceData.OUT_CH[ChannelID].name[i];
            }

//            if(MacCfg.bool_Encryption==true){//加密
//                for(int i=0;i<Define.OUT_LEN;i++){
//                    DataStruct.ChannelBuf[i]=(byte) ((byte)DataStruct.ChannelBuf[i]^Define.Encrypt_DATA);
//                }
//                if(ChannelID == 0){
//                    //加密标志,联调标志不加密
//                    DataStruct.ChannelBuf[Define.OUT_LEN-8] = (byte) Define.EncryptionFlag;
//                    DataStruct.ChannelBuf[Define.OUT_LEN-7] = DataStruct.RcvDeviceData.OUT_CH[0].name[1];
//                    for(int i=0;i<6;i++){
//                        DataStruct.ChannelBuf[Define.OUT_LEN-6+i]=(byte) (MacCfg.Encryption_PasswordBuf[i]^Define.Encrypt_DATA);
//                    }
//                }
//            }else if(MacCfg.bool_Encryption==false){//非加密
//                DataStruct.ChannelBuf[Define.OUT_LEN-8]=(byte) Define.DecipheringFlag;
//            }
        }
    }

    /******* 读取延时数据   *******/
    public static void FillDelayDataBySystemChannel(int initData[], int dataSize, boolean ReadFromeBT, boolean flash) {
        int initDataCnt = 0;
        if (DataStruct.CurMacMode.Delay.DATA_TRANSFER == Define.COM_TYPE_SYSTEM) {
            for (int i = 0; i < 50; i++) {
                DataStruct.RcvDeviceData.SYS.SoundDelayField[i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i];
            }
            DataStruct.RcvDeviceData.OUT_CH[0].delay = DataStruct.RcvDeviceData.SYS.SoundDelayField[initDataCnt] + DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[1].delay = DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] + DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[2].delay = DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] + DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[3].delay = DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] + DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[4].delay = DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] + DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] * 256;
            DataStruct.RcvDeviceData.OUT_CH[5].delay = DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] + DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] * 256;
            /**/
        }
    }

    public static void SendDelayDatabySystemChannel() {
        int initDataCnt = 0;
        DataStruct.RcvDeviceData.SYS.SoundDelayField[initDataCnt] = (DataStruct.RcvDeviceData.OUT_CH[0].delay & 0xff);
        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = ((DataStruct.RcvDeviceData.OUT_CH[0].delay >> 8) & 0xff);

        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = (DataStruct.RcvDeviceData.OUT_CH[1].delay & 0xff);
        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = ((DataStruct.RcvDeviceData.OUT_CH[1].delay >> 8) & 0xff);

        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = (DataStruct.RcvDeviceData.OUT_CH[2].delay & 0xff);
        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = ((DataStruct.RcvDeviceData.OUT_CH[2].delay >> 8) & 0xff);

        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = (DataStruct.RcvDeviceData.OUT_CH[3].delay & 0xff);
        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = ((DataStruct.RcvDeviceData.OUT_CH[3].delay >> 8) & 0xff);

        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = (DataStruct.RcvDeviceData.OUT_CH[4].delay & 0xff);
        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = ((DataStruct.RcvDeviceData.OUT_CH[4].delay >> 8) & 0xff);

        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = (DataStruct.RcvDeviceData.OUT_CH[5].delay & 0xff);
        DataStruct.RcvDeviceData.SYS.SoundDelayField[++initDataCnt] = ((DataStruct.RcvDeviceData.OUT_CH[5].delay >> 8) & 0xff);

    }

    public static int[] GetOutputChannelSkpType() {
//        MacCfg.ChannelNumBuf[0]= DataStruct.RcvDeviceData.SYS.out1_spk_type;
//        MacCfg.ChannelNumBuf[1]= DataStruct.RcvDeviceData.SYS.out2_spk_type;
//        MacCfg.ChannelNumBuf[2]= DataStruct.RcvDeviceData.SYS.out3_spk_type;
//        MacCfg.ChannelNumBuf[3]= DataStruct.RcvDeviceData.SYS.out4_spk_type;
//        MacCfg.ChannelNumBuf[4]= DataStruct.RcvDeviceData.SYS.out5_spk_type;
//        MacCfg.ChannelNumBuf[5]= DataStruct.RcvDeviceData.SYS.out6_spk_type;
//        MacCfg.ChannelNumBuf[6]= DataStruct.RcvDeviceData.SYS.out7_spk_type;
//        MacCfg.ChannelNumBuf[7]= DataStruct.RcvDeviceData.SYS.out8_spk_type;
//
//        MacCfg.ChannelNumBuf[8] = DataStruct.RcvDeviceData.SYS.out9_spk_type;
//        MacCfg.ChannelNumBuf[9] = DataStruct.RcvDeviceData.SYS.out10_spk_type;
//        MacCfg.ChannelNumBuf[10] = DataStruct.RcvDeviceData.SYS.out11_spk_type;
//        MacCfg.ChannelNumBuf[11] = DataStruct.RcvDeviceData.SYS.out12_spk_type;
//        MacCfg.ChannelNumBuf[12] = DataStruct.RcvDeviceData.SYS.out13_spk_type;
//        MacCfg.ChannelNumBuf[13] = DataStruct.RcvDeviceData.SYS.out14_spk_type;
//        MacCfg.ChannelNumBuf[14] = DataStruct.RcvDeviceData.SYS.out15_spk_type;
//        MacCfg.ChannelNumBuf[15] = DataStruct.RcvDeviceData.SYS.out16_spk_type;
        return MacCfg.ChannelNumBuf;
    }

    public static void readGroupWithMcu(int group) {
        DataStruct.BOOL_GroupREC_ACK = true;

        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_Group;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;
        for (int i = 0; i < 8; i++) {
            DataStruct.SendDeviceData.SYS.GROUP[i] = 0;
        }
        DataStruct.SendDeviceData.SYS.GROUP[0] = (byte) group;
        SendDataToDevice(false);

        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.MUSIC;
            DataStruct.SendDeviceData.ChannelID = 0x02;//MUSIC 固定2
            DataStruct.SendDeviceData.DataID = 0x77;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
            DataStruct.SendDeviceData.DataLen = 0x00;
            SendDataToDevice(false);
        }
    }

    public static void sendResetMUCData() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        DataStruct_System.RESET_MCU[0] = (byte) t.second;
        DataStruct_System.RESET_MCU[1] = (byte) ((byte) t.year / 100);
        DataStruct_System.RESET_MCU[2] = (byte) ((byte) t.year % 100);
        DataStruct_System.RESET_MCU[3] = (byte) t.month;
        DataStruct_System.RESET_MCU[4] = (byte) t.monthDay;
        DataStruct_System.RESET_MCU[5] = (byte) t.hour;
        DataStruct_System.RESET_MCU[6] = (byte) t.minute;
        DataStruct_System.RESET_MCU[7] = (byte) t.second;


        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_RESET_MCU;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;

        SendDataToDevice(false);
    }

    public static void SaveSystemData() {
//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER != Define.COM_TYPE_SYSTEM){
//            return;
//        }
        // 比较音源数据
        DataStruct.SendDeviceData.SYS.input_source = DataStruct.RcvDeviceData.SYS.input_source;
        DataStruct.SendDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.SYS.aux_mode;
        DataStruct.SendDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.SYS.device_mode;
        DataStruct.SendDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.SYS.hi_mode;
        DataStruct.SendDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.SYS.blue_gain;
        DataStruct.SendDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.SYS.aux_gain;
        DataStruct.SendDeviceData.SYS.Safety = DataStruct.RcvDeviceData.SYS.Safety;
        DataStruct.SendDeviceData.SYS.sound_effect = DataStruct.RcvDeviceData.SYS.sound_effect;


        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.PC_SOURCE_SET;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;
        DataStruct.U0SendFrameFlg = YES;
        SendDataToDevice(false);

        // 比较系统配置数据
        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;

        DataStruct.U0SendFrameFlg = YES;
        SendDataToDevice(false);

        if (DataStruct.CurMacMode.BOOL_SET_SPK_TYPE) {
            // 比较系统通道输出类型配置数据
            DataStruct.SendDeviceData.SYS.out1_spk_type = DataStruct.RcvDeviceData.SYS.out1_spk_type;
            DataStruct.SendDeviceData.SYS.out2_spk_type = DataStruct.RcvDeviceData.SYS.out2_spk_type;
            DataStruct.SendDeviceData.SYS.out3_spk_type = DataStruct.RcvDeviceData.SYS.out3_spk_type;
            DataStruct.SendDeviceData.SYS.out4_spk_type = DataStruct.RcvDeviceData.SYS.out4_spk_type;
            DataStruct.SendDeviceData.SYS.out5_spk_type = DataStruct.RcvDeviceData.SYS.out5_spk_type;
            DataStruct.SendDeviceData.SYS.out6_spk_type = DataStruct.RcvDeviceData.SYS.out6_spk_type;
            DataStruct.SendDeviceData.SYS.out7_spk_type = DataStruct.RcvDeviceData.SYS.out7_spk_type;
            DataStruct.SendDeviceData.SYS.out8_spk_type = DataStruct.RcvDeviceData.SYS.out8_spk_type;

            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_SPK_TYPE;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 8;

            DataStruct.U0SendFrameFlg = YES;
            SendDataToDevice(false);
        }


//        if (DataStruct.CurMacMode.BOOL_SYSTEM_SPK_TYPEB) {
//            // 比较系统通道输出类型配置数据
//            DataStruct.SendDeviceData.SYS.CurGroupID = DataStruct.RcvDeviceData.SYS.CurGroupID;
//
//
//            DataStruct.SendDeviceData.SYS.out10_spk_type = DataStruct.RcvDeviceData.SYS.out10_spk_type;
//            DataStruct.SendDeviceData.SYS.out11_spk_type = DataStruct.RcvDeviceData.SYS.out11_spk_type;
//            DataStruct.SendDeviceData.SYS.out12_spk_type = DataStruct.RcvDeviceData.SYS.out12_spk_type;
//            DataStruct.SendDeviceData.SYS.out13_spk_type = DataStruct.RcvDeviceData.SYS.out13_spk_type;
//            DataStruct.SendDeviceData.SYS.out14_spk_type = DataStruct.RcvDeviceData.SYS.out14_spk_type;
//            DataStruct.SendDeviceData.SYS.out15_spk_type = DataStruct.RcvDeviceData.SYS.out15_spk_type;
//            DataStruct.SendDeviceData.SYS.out16_spk_type = DataStruct.RcvDeviceData.SYS.out16_spk_type;
//
//            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_SPK_TYPEB;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//            DataStruct.SendDeviceData.PcCustom = 0x00;
//            DataStruct.SendDeviceData.DataLen = 8;
//
//            DataStruct.U0SendFrameFlg = YES;
//            SendDataToDevice(false);
//        }
    }

    /**
     * 保存用户组
     *
     * @param UserID 用户组
     */
    public static void SaveGroupData(int UserID) {
        DataStruct.SEFF_USER_GROUP_OPT = 2;

        //保存音效名字
        if (UserID != 0) {
            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = UserID;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.GROUP_NAME;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x10;
            SendDataToDevice(false);
        }

        /*保存输出通道的输出数据 DataStruct.CurMacMode.Out.OUT_CH_MAX*/
        for (int i = 0; i < 8; i++) {
            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = (byte) (UserID & 0xff);
            DataStruct.SendDeviceData.DataType = Define.OUTPUT;
            DataStruct.SendDeviceData.ChannelID = (byte) (i & 0xff);
            DataStruct.SendDeviceData.DataID = 0x00;//DataStruct.IN_MISC_ID;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
            DataStruct.SendDeviceData.DataLen = Define.OUT_LEN;
            FillSedDataStruct(Define.OUTPUT, i);
            SendDataToDevice(false);
        }
        // DataStruct.RcvDeviceData.SYS.UserGroup[0]= DataStruct.RcvDeviceData.SYS.UserGroup[UserID];
        setCurID();

    }


    /**
     * 特殊处理  设置通道数据变动之后改变当前组的转态
     */
    public static void setCurID() {
        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.GROUP_NAME;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;
        SendDataToDevice(false);
    }

    public static void SaveGroupData(int UserID, Context mContext) {

        SaveGroupData(UserID);
        //刷新界面连接进度
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
        intentw.putExtra("state", false);
        mContext.sendBroadcast(intentw);
    }

    /*删除用户组名称*/
    public static void DeleteGroup(int group) {
        for (int i = 0; i < 16; i++) {
            DataStruct.RcvDeviceData.SYS.UserGroup[group][i] = 0;
        }
        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = group;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.GROUP_NAME;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 0x10;
        SendDataToDevice(false);
    }

    /*调用用户组*/
    public static void ReadGroupData(int UserGroup, Context mContext) {
        boolean netCall = false;
        DataStruct.SEFF_USER_GROUP_OPT = 1;

        /*清除列表*/
        DataStruct.SendbufferList.clear();
        DataStruct.SendbufferList.removeAll(DataStruct.SendbufferList);


        DataStruct.RcvDeviceData.SYS.UserGroup[0] = DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup];
        setCurID();

        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM) {
            //插入静音包
            DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;//DataStruct.RcvDeviceData.SYS.main_vol;
            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = 0;//DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
            DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 8;
            SendDataToDevice(false);
        }
        /*增加读取全部通道的输出数据 DataStruct.CurMacMode.Out.OUT_CH_MAX*/
        for (int i = 0; i < 8; i++) {
            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = UserGroup;
            DataStruct.SendDeviceData.DataType = Define.OUTPUT;
            DataStruct.SendDeviceData.ChannelID = i;
            DataStruct.SendDeviceData.DataID = 0x00;/*读某一组的数据，下位机并把此组数据复制到当前组中*/
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
            DataStruct.SendDeviceData.DataLen = 0x00;
            SendDataToDevice(false);

        }
//        //插入恢复静音包
        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM) {
            DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;
            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
            DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;
            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 8;
            SendDataToDevice(false);
        }
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
        intentw.putExtra("state", true);
        mContext.sendBroadcast(intentw);
    }


    //检查可设置联调的通道
    public static int CheckChannelCanLink() {
        int channelNameNum = 0;
        int channelNameNumEls = 0;
        int res = 0;
        int inc = 0;
        int i = 0, j = 0;
        MacCfg.ChannelLinkCnt = 0;
        for (i = 0; i < MacCfg.MaxOupputNameLinkGroup; i++) {
            for (j = 0; j < 2; j++) {
                MacCfg.ChannelLinkBuf[i][j] = Define.EndFlag;
            }
        }
        for (i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            channelNameNum = GetChannelNum(i);
            if (((channelNameNum >= 0) && (channelNameNum <= 18)) || (channelNameNum == 22) || (channelNameNum == 23)) {
                for (j = i + 1; j < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; j++) {
                    channelNameNumEls = GetChannelNum(j);
//	    			channelNameNumEls=GetChannelNum(B_OutName[j].getText().toString());
                    if ((channelNameNum >= 1) && (channelNameNum <= 6) &&
                            (channelNameNumEls >= 7) && (channelNameNumEls <= 12)) {
                        res = channelNameNumEls - channelNameNum;
                        if (res == 6) {
                            MacCfg.ChannelLinkBuf[inc][0] = i;
                            MacCfg.ChannelLinkBuf[inc][1] = j;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 7) && (channelNameNum <= 12) &&
                            (channelNameNumEls >= 1) && (channelNameNumEls <= 6)) {
                        res = channelNameNum - channelNameNumEls;
                        if (res == 6) {
                            MacCfg.ChannelLinkBuf[inc][0] = j;
                            MacCfg.ChannelLinkBuf[inc][1] = i;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 13) && (channelNameNum <= 15) &&
                            (channelNameNumEls >= 16) && (channelNameNumEls <= 18)) {
                        res = channelNameNumEls - channelNameNum;
                        if (res == 3) {
                            MacCfg.ChannelLinkBuf[inc][0] = i;
                            MacCfg.ChannelLinkBuf[inc][1] = j;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 16) && (channelNameNum <= 18) &&
                            (channelNameNumEls >= 13) && (channelNameNumEls <= 15)) {
                        res = channelNameNum - channelNameNumEls;
                        if (res == 3) {
                            MacCfg.ChannelLinkBuf[inc][0] = j;
                            MacCfg.ChannelLinkBuf[inc][1] = i;
                            ++inc;
                        }
                    } else if ((channelNameNum == 22) && (channelNameNumEls == 23)) {
                        MacCfg.ChannelLinkBuf[inc][0] = i;
                        MacCfg.ChannelLinkBuf[inc][1] = j;
                        ++inc;
                    } else if ((channelNameNum == 23) && (channelNameNumEls == 22)) {
                        MacCfg.ChannelLinkBuf[inc][0] = j;
                        MacCfg.ChannelLinkBuf[inc][1] = i;
                        ++inc;
                    }
                    //System.out.println("CH_NAME res:"+res);
                }
            }

        }
        MacCfg.ChannelLinkCnt = inc;

        return inc;
    }

    public static void SaveAllGain() {

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {

            MacCfg.Output_gain[i] = DataStruct.RcvDeviceData.OUT_CH[i].gain;
            MacCfg.Output_Hfreq[i] = DataStruct.RcvDeviceData.OUT_CH[i].h_freq;
            MacCfg.Output_Lfreq[i] = DataStruct.RcvDeviceData.OUT_CH[i].l_freq;
            for (int j = 0; j < Define.MAX_CHEQ; j++) {
                MacCfg.Output_level[i][j] = DataStruct.RcvDeviceData.OUT_CH[i].EQ[j].level;
            }
        }
    }


    public static void channelDataCopy(int from, int to) {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
            DataStruct.RcvDeviceData.OUT_CH[to].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[from].EQ[j].freq;
            DataStruct.RcvDeviceData.OUT_CH[to].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[from].EQ[j].level;
            DataStruct.RcvDeviceData.OUT_CH[to].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[from].EQ[j].bw;
            DataStruct.RcvDeviceData.OUT_CH[to].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[from].EQ[j].shf_db;
            DataStruct.RcvDeviceData.OUT_CH[to].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[from].EQ[j].type;
        }

        if (DataStruct.CurMacMode.Out.LinkMute) {
            // DataStruct.RcvDeviceData.OUT_CH[to].mute  = DataStruct.RcvDeviceData.OUT_CH[from].mute;
        }
        if (DataStruct.CurMacMode.Out.LinkPolor) {
            // DataStruct.RcvDeviceData.OUT_CH[to].polar = DataStruct.RcvDeviceData.OUT_CH[from].polar;
        }
        DataStruct.RcvDeviceData.OUT_CH[to].gain  = DataStruct.RcvDeviceData.OUT_CH[from].gain;
        //DataStruct.RcvDeviceData.OUT_CH[to].delay = DataStruct.RcvDeviceData.OUT_CH[from].delay;
        DataStruct.RcvDeviceData.OUT_CH[to].eq_mode = DataStruct.RcvDeviceData.OUT_CH[from].eq_mode;
        //DataStruct.RcvDeviceData.OUT_CH[to].spk_type  = DataStruct.RcvDeviceData.OUT_CH[from].spk_type;

        DataStruct.RcvDeviceData.OUT_CH[to].h_freq = DataStruct.RcvDeviceData.OUT_CH[from].h_freq;
        DataStruct.RcvDeviceData.OUT_CH[to].h_filter = DataStruct.RcvDeviceData.OUT_CH[from].h_filter;
        DataStruct.RcvDeviceData.OUT_CH[to].h_level = DataStruct.RcvDeviceData.OUT_CH[from].h_level;
        DataStruct.RcvDeviceData.OUT_CH[to].l_freq = DataStruct.RcvDeviceData.OUT_CH[from].l_freq;
        DataStruct.RcvDeviceData.OUT_CH[to].l_filter = DataStruct.RcvDeviceData.OUT_CH[from].l_filter;
        DataStruct.RcvDeviceData.OUT_CH[to].l_level = DataStruct.RcvDeviceData.OUT_CH[from].l_level;
    }

    public static void syncLinkData(int Type) {
//        if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS){//前声场，后声场，超低的联调，单独分开
//            _LINKMODE_FRS();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FRS_A){//前声场，后声场，超低的联调，一起联调
//            _LINKMODE_FRS_A();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR){//前声场，后声场，单独分开
//            _LINKMODE_FR();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR_A){//前声场，后声场，中置超低的联调，全部一起联调
//            _LINKMODE_FR_A();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE){//设置通道输出类型后的联调
        _LINKMODE_SPKTYPE(Type);
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_SPKTYPE_S){//设置通道输出类型后的联调，可联机保存
//            _LINKMODE_SPKTYPE_S();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_AUTO){//任意联调，每个通道可以单独联调，可联机保存
//            _LINKMODE_AUTO();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_LEFTRIGHT){//固定两两通道联调
//            _LINKMODE_LEFTRIGHT();
//        }else if(DataStruct.CurMacMode.LinkMOde == Define.LINKMODE_FR2A){//前声场，后声场，一起两两联调
//            _LINKMODE_FR2A();
//        }

    }

    //前声场，后声场，超低的联调，单独分开
    public static void _LINKMODE_FRS() {
        int Dto = 0xff, Dfrom;
        Dfrom = MacCfg.OutputChannelSel;
        int chLinkValume = 0;
        //同时刷新连接状态
        if ((MacCfg.ChannelConFLR == 0) && (MacCfg.ChannelConRLR == 0) && (MacCfg.ChannelConSLR == 0)) {
            return;//没有联调
        }

        if ((MacCfg.ChannelConFLR == 1) && (MacCfg.ChannelConRLR == 0) && (MacCfg.ChannelConSLR == 0)) {
            chLinkValume = 1;
        } else if ((MacCfg.ChannelConFLR == 0) && (MacCfg.ChannelConRLR == 1) && (MacCfg.ChannelConSLR == 0)) {
            chLinkValume = 2;
        } else if ((MacCfg.ChannelConFLR == 1) && (MacCfg.ChannelConRLR == 1) && (MacCfg.ChannelConSLR == 0)) {
            chLinkValume = 3;
        } else if ((MacCfg.ChannelConFLR == 0) && (MacCfg.ChannelConRLR == 0) && (MacCfg.ChannelConSLR == 1)) {
            chLinkValume = 4;
        } else if ((MacCfg.ChannelConFLR == 1) && (MacCfg.ChannelConRLR == 0) && (MacCfg.ChannelConSLR == 1)) {
            chLinkValume = 5;
        } else if ((MacCfg.ChannelConFLR == 0) && (MacCfg.ChannelConRLR == 1) && (MacCfg.ChannelConSLR == 1)) {
            chLinkValume = 6;
        } else if ((MacCfg.ChannelConFLR == 1) && (MacCfg.ChannelConRLR == 1) && (MacCfg.ChannelConSLR == 1)) {
            chLinkValume = 7;
        }

        switch (chLinkValume) {
            case 1://0-1
                if (Dfrom == 0) {
                    Dto = 1;
                } else if (Dfrom == 1) {
                    Dto = 0;
                }
                break;
            case 2://2-3
                if (Dfrom == 2) {
                    Dto = 3;
                } else if (Dfrom == 3) {
                    Dto = 2;
                }
                break;
            case 4://4-5
                if (Dfrom == 4) {
                    Dto = 5;
                } else if (Dfrom == 5) {
                    Dto = 4;
                }
                break;
            case 3://0-1 //2-3
                if (Dfrom == 0) {
                    Dto = 1;
                } else if (Dfrom == 1) {
                    Dto = 0;
                } else if (Dfrom == 2) {
                    Dto = 3;
                } else if (Dfrom == 3) {
                    Dto = 2;
                }
                break;
            case 5://0-1 //4-5
                if (Dfrom == 0) {
                    Dto = 1;
                } else if (Dfrom == 1) {
                    Dto = 0;
                } else if (Dfrom == 4) {
                    Dto = 5;
                } else if (Dfrom == 5) {
                    Dto = 4;
                }
                break;
            case 6:////2-3 //4-5
                if (Dfrom == 2) {
                    Dto = 3;
                } else if (Dfrom == 3) {
                    Dto = 2;
                } else if (Dfrom == 4) {
                    Dto = 5;
                } else if (Dfrom == 5) {
                    Dto = 4;
                }
                break;
            case 7://0-1 //2-3 //4-5
                if (Dfrom == 0) {
                    Dto = 1;
                } else if (Dfrom == 1) {
                    Dto = 0;
                } else if (Dfrom == 2) {
                    Dto = 3;
                } else if (Dfrom == 3) {
                    Dto = 2;
                } else if (Dfrom == 4) {
                    Dto = 5;
                } else if (Dfrom == 5) {
                    Dto = 4;
                }
                break;
            default:
                break;
        }

        if (Dto < DataStruct.CurMacMode.Out.OUT_CH_MAX) {

            if (MacCfg.UI_Type == Define.UI_HFilter) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_filter = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_filter;
            } else if (MacCfg.UI_Type == Define.UI_HOct) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_level;
            } else if (MacCfg.UI_Type == Define.UI_HFreq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_freq;
            } else if (MacCfg.UI_Type == Define.UI_LFilter) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_filter = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_filter;
            } else if (MacCfg.UI_Type == Define.UI_LOct) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_level;
            } else if (MacCfg.UI_Type == Define.UI_LFreq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_freq;
            } else if (MacCfg.UI_Type == Define.UI_OutVal) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].gain = DataStruct.RcvDeviceData.OUT_CH[Dfrom].gain;
            } else if (MacCfg.UI_Type == Define.UI_OutMute) {
                if (DataStruct.CurMacMode.Out.LinkMute) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].mute = DataStruct.RcvDeviceData.OUT_CH[Dfrom].mute;
                }
            } else if (MacCfg.UI_Type == Define.UI_OutPolar) {
                if (DataStruct.CurMacMode.Out.LinkPolor) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].polar = DataStruct.RcvDeviceData.OUT_CH[Dfrom].polar;

                }
            } else if (MacCfg.UI_Type == Define.UI_EQ_BW) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].bw
                        = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].bw;
            } else if (MacCfg.UI_Type == Define.UI_EQ_Freq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].freq
                        = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].freq;
            } else if (MacCfg.UI_Type == Define.UI_EQ_Level) {
//                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level
//                        = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].level;


            } else if (MacCfg.UI_Type == Define.UI_EQ_G_P_MODE_EQ) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].eq_mode
                        = DataStruct.RcvDeviceData.OUT_CH[Dfrom].eq_mode;
            } else if (MacCfg.UI_Type == Define.UI_EQ_ALL) {
                for (int j = 0; j < Define.MAX_CHEQ; j++) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].freq;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].level;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].bw;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].shf_db;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].type;
                }
            }

        }
    }

    //前声场，后声场，超低的联调，一起联调
    public static void _LINKMODE_FRS_A() {

    }

    //前声场，后声场，单独分开
    public static void _LINKMODE_FR() {

    }

    //前声场，后声场，中置超低的联调，全部一起联调
    public static void _LINKMODE_FR_A() {

    }

    //设置通道输出类型后的联调
    public static void _LINKMODE_SPKTYPE(int type) {
        int Dfrom = 0, Dto = 0xff;
        MacCfg.UI_Type = type;
        //同时刷新连接状态
        if ((!MacCfg.bool_OutChLink) || (DataOptUtil.CheckChannelCanLink() == 0)) {
            return;//没有联调
        }
        Dfrom = MacCfg.OutputChannelSel;
        for (int i = 0; i < MacCfg.ChannelLinkCnt; i++) {
            if (MacCfg.ChannelLinkBuf[i][0] == MacCfg.OutputChannelSel) {
                Dto = MacCfg.ChannelLinkBuf[i][1];
            } else if (MacCfg.ChannelLinkBuf[i][1] == MacCfg.OutputChannelSel) {
                Dto = MacCfg.ChannelLinkBuf[i][0];
            }
        }
//        if (DataStruct.RcvDeviceData.OUT_CH[Dfrom].LinkFlag == 0) {
//            return;
//        }

        if (Dto < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE) {
//        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
//
//            if (DataStruct.RcvDeviceData.OUT_CH[i].LinkFlag == DataStruct.RcvDeviceData.OUT_CH[Dfrom].LinkFlag) {
//                if (i != Dfrom) {
            if (MacCfg.UI_Type == Define.UI_HFilter) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_filter = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_filter;
            } else if (MacCfg.UI_Type == Define.UI_HOct) {

                DataStruct.RcvDeviceData.OUT_CH[Dto].h_level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_level;
            } else if (MacCfg.UI_Type == Define.UI_HFreq) {
                //   DataStruct.RcvDeviceData.OUT_CH[i].h_freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_freq;

                int valDll = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_freq
                        - MacCfg.Output_Hfreq[Dfrom];
                int val = DataStruct.RcvDeviceData.OUT_CH[Dto].h_freq + valDll;
                if (val > 20000) {
                    val = 20000;
                } else if (val < 20) {
                    val = 20;
                }
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_freq = val;
                SaveAllGain();

            } else if (MacCfg.UI_Type == Define.UI_LFilter) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_filter = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_filter;
            } else if (MacCfg.UI_Type == Define.UI_LOct) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_level;
            } else if (MacCfg.UI_Type == Define.UI_LFreq) {
                int valDll = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_freq
                        - MacCfg.Output_Lfreq[Dfrom];
                int val = DataStruct.RcvDeviceData.OUT_CH[Dto].l_freq + valDll;
                if (val > 20000) {
                    val = 20000;
                } else if (val < 20) {
                    val = 20;
                }
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_freq = val;
                SaveAllGain();

                //  DataStruct.RcvDeviceData.OUT_CH[i].l_freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_freq;
            } else if (MacCfg.UI_Type == Define.UI_OutVal) {
               DataStruct.RcvDeviceData.OUT_CH[Dto].gain = DataStruct.RcvDeviceData.OUT_CH[Dfrom].gain;

//                int valDll = DataStruct.RcvDeviceData.OUT_CH[Dfrom].gain
//                        - MacCfg.Output_gain[Dfrom];
//                // for (int i = 0; i < Temp.listOutputLink.size(); i++) {
//
//                // if (i != Dfrom) {
//
//                DataStruct.RcvDeviceData.OUT_CH[Dto].gain =
//                        MacCfg.Output_gain[Dto] + valDll;
//
//
//                if (DataStruct.RcvDeviceData.OUT_CH[Dto].gain > DataStruct.CurMacMode.Out.MaxOutVol) {
//                    DataStruct.RcvDeviceData.OUT_CH[Dto].gain = DataStruct.CurMacMode.Out.MaxOutVol;
//                } else if (DataStruct.RcvDeviceData.OUT_CH[Dto].gain < 0) {
//                    DataStruct.RcvDeviceData.OUT_CH[Dto].gain = 0;
//                }
//                SaveAllGain();
            } else if (MacCfg.UI_Type == Define.UI_OutMute) {
                if (DataStruct.CurMacMode.Out.LinkMute) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].mute = DataStruct.RcvDeviceData.OUT_CH[Dfrom].mute;
                }
            } else if (MacCfg.UI_Type == Define.UI_OutPolar) {
                if (DataStruct.CurMacMode.Out.LinkPolor) {
                    //    DataStruct.RcvDeviceData.OUT_CH[Dto].polar  = DataStruct.RcvDeviceData.OUT_CH[Dfrom].polar;

                }
            } else if (MacCfg.UI_Type == Define.UI_EQ_BW) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].bw = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].bw;
            } else if (MacCfg.UI_Type == Define.UI_EQ_Freq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].freq;
            } else if (MacCfg.UI_Type == Define.UI_EQ_Level) {
                // DataStruct.RcvDeviceData.OUT_CH[i].EQ[MacCfg.EQ_Num].level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].level;
                int valDll = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].level
                        - MacCfg.Output_level[Dfrom][MacCfg.EQ_Num];


                // for (int i = 0; i < Temp.listOutputLink.size(); i++) {
                //  if (i != Dfrom) {

                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = MacCfg.Output_level[Dto][MacCfg.EQ_Num] + valDll;
                if (DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level > DataStruct.CurMacMode.EQ.EQ_LEVEL_MAX) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_MAX;
                } else if (DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level < DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
                }
                SaveAllGain();

            } else if (MacCfg.UI_Type == Define.UI_EQ_G_P_MODE_EQ) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].eq_mode = DataStruct.RcvDeviceData.OUT_CH[Dfrom].eq_mode;
            } else if (MacCfg.UI_Type == Define.UI_EQ_ALL) {
                for (int j = 0; j < DataStruct.CurMacMode.EQ.Max_EQ; j++) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].freq;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].level;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].bw;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].shf_db;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].type;


                    DataStruct.BufDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[Dfrom].EQ[j].level;
                    DataStruct.GainBuf[Dto][j] = DataStruct.BufDeviceData.OUT_CH[Dfrom].EQ[j].level;
                }
            } else if (MacCfg.UI_Type == Define.UI_EQ_RESET) {
                System.out.println("BUG 进来了"
                        + Dfrom + "设置的额" + DataStruct.GainBuf[Dto][MacCfg.EQ_Num]);
                // for (int j = 0; j < DataStruct.CurMacMode.EQ.Max_EQ; j++) {


                //if (DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                DataStruct.GainBuf[Dto][MacCfg.EQ_Num] = DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level;

                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;

                DataStruct.BufDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
//                            }else {
//
//                                if(i!=Dfrom) {
//                                    DataStruct.RcvDeviceData.OUT_CH[i].EQ[MacCfg.EQ_Num].level = DataStruct.GainBuf[i][MacCfg.EQ_Num];
//                                }
//                            }


//                        for (int j = 0; j < DataStruct.CurMacMode.EQ.Max_EQ; j++) {
//                            DataStruct.RcvDeviceData.OUT_CH[i].EQ[MacCfg.EQ_Num].level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].level;
//                        }
                SaveAllGain();
                //}
            } else if (MacCfg.UI_Type == Define.UI_EQ_Restore) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = DataStruct.GainBuf[Dto][MacCfg.EQ_Num];
            } else if (MacCfg.UI_Type == Define.UI_EQ_Zero) {
                for (int j = 0; j < 31; j++) {
                    DataStruct.BufDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level;
                    DataStruct.GainBuf[Dto][j] = DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
                }

            } else if (MacCfg.UI_Type == Define.UI_EQ_Recover) {
                for (int j = 0; j < 31; j++) {

                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[Dto].EQ[j].level;
                }
            }
//                }
//            }

        }

        // }
    }

    //任意联调，每个通道可以单独联调，可联机保存
    public static void _LINKMODE_AUTO() {

    }

    //固定两两通道联调
    public static void _LINKMODE_LEFTRIGHT() {
        int Dfrom = 0, Dto = 0xff;
        //同时刷新连接状态
        if (!MacCfg.bool_OutChLink) {
            return;//没有联调
        }
        Dfrom = MacCfg.OutputChannelSel;
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX / 2; i++) {
            if (MacCfg.OutputChannelSel == i * 2) {
                Dto = i * 2 + 1;
                break;
            } else if (MacCfg.OutputChannelSel == i * 2 + 1) {
                Dto = i * 2;
                break;
            }

        }

        if (Dto < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE) {
            if (MacCfg.UI_Type == Define.UI_HFilter) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_filter = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_filter;
            } else if (MacCfg.UI_Type == Define.UI_HOct) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_level;
            } else if (MacCfg.UI_Type == Define.UI_HFreq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].h_freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].h_freq;
            } else if (MacCfg.UI_Type == Define.UI_LFilter) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_filter = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_filter;
            } else if (MacCfg.UI_Type == Define.UI_LOct) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_level;
            } else if (MacCfg.UI_Type == Define.UI_LFreq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].l_freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].l_freq;
            } else if (MacCfg.UI_Type == Define.UI_OutVal) {

                DataStruct.RcvDeviceData.OUT_CH[Dto].gain = DataStruct.RcvDeviceData.OUT_CH[Dfrom].gain;
            } else if (MacCfg.UI_Type == Define.UI_OutMute) {
                if (DataStruct.CurMacMode.Out.LinkMute) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].mute = DataStruct.RcvDeviceData.OUT_CH[Dfrom].mute;
                }


            } else if (MacCfg.UI_Type == Define.UI_OutPolar) {
                if (DataStruct.CurMacMode.Out.LinkPolor) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].polar = DataStruct.RcvDeviceData.OUT_CH[Dfrom].polar;

                }
            } else if (MacCfg.UI_Type == Define.UI_EQ_BW) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].bw = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].bw;
            } else if (MacCfg.UI_Type == Define.UI_EQ_Freq) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].freq;
            } else if (MacCfg.UI_Type == Define.UI_EQ_Level) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[MacCfg.EQ_Num].level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[MacCfg.EQ_Num].level;
            } else if (MacCfg.UI_Type == Define.UI_EQ_G_P_MODE_EQ) {
                DataStruct.RcvDeviceData.OUT_CH[Dto].eq_mode = DataStruct.RcvDeviceData.OUT_CH[Dfrom].eq_mode;
            } else if (MacCfg.UI_Type == Define.UI_EQ_ALL) {
                for (int j = 0; j < DataStruct.CurMacMode.EQ.Max_EQ; j++) {
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].freq = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].freq;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].level;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].bw = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].bw;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].shf_db = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].shf_db;
                    DataStruct.RcvDeviceData.OUT_CH[Dto].EQ[j].type = DataStruct.RcvDeviceData.OUT_CH[Dfrom].EQ[j].type;
                }
            }
        }
    }

    //前声场，后声场，一起两两联调
    public static void _LINKMODE_FR2A() {

    }


    public static String CountDelayCM(int num) {
        DecimalFormat decimalFormat = new DecimalFormat("0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = "";
        int m_nTemp = 75;
        float Time = (float) (num / 48.0); //当Delay〈476时STEP是0.021MS；
        float LMT = (float) (((m_nTemp - 50) * 0.6 + 331.0) / 1000.0 * Time);
        LMT = LMT * 100;
        //float LFT = (float) (LMT*3.2808*12.0);

        int fr = (int) (LMT * 10);
        int ir = fr % 10;
        int ri = 0;
        if (ir >= 5) {
            ri = fr / 10 + 1;
        } else {
            ri = fr / 10;
        }
        p = decimalFormat.format(ri);
        return p;
    }

    public static String CountDelayMs(int num) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = "";
        int fr = num * 10000 / 48;
        int ir = fr % 10;
        int ri = 0;
        if (ir >= 5) {
            ri = fr / 10 + 1;
        } else {
            ri = fr / 10;
        }
        p = decimalFormat.format(ri / 1000.0);
        return p;
    }

    public static String CountDelayInch(int num) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        String p = "";
        float base = (float) 331.0;
        if (num == DataStruct.CurMacMode.Delay.MAX) {
            base = (float) 331.4;
        }
        int m_nTemp = 75;
        float Time = (float) (num / 48.0); //当Delay〈476时STEP是0.021MS；
        float LMT = (float) (((m_nTemp - 50) * 0.6 + base) / 1000.0 * Time);

        float LFT = (float) (LMT * 3.2808 * 12.0);

        int fr = (int) (LFT * 10);
        int ir = fr % 10;
        int ri = 0;
        if (ir >= 5) {
            ri = fr / 10 + 1;
        } else {
            ri = fr / 10;
        }
        p = decimalFormat.format(ri);
        return p;
    }


    /*********************************************************************/
    /*****************************  JSON TODO  ***************************/
    /*********************************************************************/
    /*提示有音效文件更新对话框
    private void showUpdateSEFFDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.SSM_Title);
        builder.setMessage(R.string.SSM_MSG);

        builder.setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if(LOAD_SEFF_FROM_OTHER_PathName != null){
                    int fileType=jsonRWOpt.getSEFFFileType(mContext, LOAD_SEFF_FROM_OTHER_PathName);
                    //ToastMsg("GetFileType="+String.valueOf(fileType));
                    if(fileType == 1){
                        UpdateForJsonSingleData(LOAD_SEFF_FROM_OTHER_PathName);
                    }else if(fileType == 2){
                        loadMacEffJsonData(LOAD_SEFF_FROM_OTHER_PathName);
                    }else{
                        ToastMsg(getResources().getString(R.string.LoadSEff_Fail));
                    }
                }

            }
        });
        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.create().show();
    }
    //存储音效文件弹出框
    private void initSaveSingleMacSEFFileDialog(){
        SaveSMacFileDialog = new Dialog(this);
        LayoutInflater inflaterSSMDialog = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        V_SaveSMacFile = inflaterSSMDialog.inflate(R.layout.dialog_save_singlemac_file,
                (ViewGroup)findViewById(R.id.id_dialog_save_singlemac_file));
        SaveSMacFileDialog.setContentView(V_SaveSMacFile);
        SaveSMacFileDialog.setTitle(R.string.SSM_Save);

        B_SSM_Save = (Button)V_SaveSMacFile.findViewById(R.id.id_b_ssm_save);
        B_SSM_Cancel = (Button)V_SaveSMacFile.findViewById(R.id.id_b_ssm_cancel);

        LY_SSM_Single = (LinearLayout)V_SaveSMacFile.findViewById(R.id.id_llyout_single_sel);
        LY_SSM_MAC = (LinearLayout)V_SaveSMacFile.findViewById(R.id.id_llyout_mac_sel);
        B_SSM_Single = (Button)V_SaveSMacFile.findViewById(R.id.id_b_sel_single);
        B_B_SSM_MAC = (Button)V_SaveSMacFile.findViewById(R.id.id_b_sel_mac);
        ET_SSM_FileName = (EditText)V_SaveSMacFile.findViewById(R.id.id_et_filename);
        ET_SSM_Detials = (EditText)V_SaveSMacFile.findViewById(R.id.id_et_filedetials);
        TV_SSM_Msg = (TextView)V_SaveSMacFile.findViewById(R.id.id_b_mac_msg);

        LY_SSM_Single.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_SSM_Single.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_press));
                B_B_SSM_MAC.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_normal));
                bool_FileType = false;
                TV_SSM_Msg.setVisibility(View.GONE);
            }
        });

        LY_SSM_MAC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_SSM_Single.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_normal));
                B_B_SSM_MAC.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_press));
                bool_FileType = true;
                TV_SSM_Msg.setVisibility(View.VISIBLE);
            }
        });


        B_SSM_Save.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        B_SSM_Save.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_SSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
                        B_SSM_Save.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_SSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        B_SSM_Save.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_SSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        String Name = ET_SSM_FileName.getText().toString();
                        SSM_Detials = ET_SSM_Detials.getText().toString();
                        if(checkSEFFileIsExit( Name)){


                            if(Name.length() > 0){
                                fileNameString = ET_SSM_FileName.getText().toString();
                                SaveSMacFileDialog.cancel();
                                if(bool_FileType){
                                    ReadMacGroup();
                                    bool_ShareOrSaveMacSEFF = true;
                                }else{
                                    SaveSingleSEFFData(fileNameString);
                                }
                            }else{
                                ToastMsg(getResources().getString(R.string.SSM_FileNameMsg));
                            }
                        }
                        break;
                    default:
                        B_SSM_Save.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_SSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        break;
                }
                return false;
            }
        });

        B_SSM_Cancel.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        B_SSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_SSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
                        B_SSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_SSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        B_SSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_SSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        SaveSMacFileDialog.cancel();

                        break;
                    default:
                        B_SSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_SSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        break;
                }
                return false;
            }
        });

    }

    //分享音效文件弹出框
    private void initShareSingleMacSEFFileDialog(){
        ShareSMacFileDialog = new Dialog(this);
        LayoutInflater inflaterSSMDialog = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        V_ShareSMacFile = inflaterSSMDialog.inflate(R.layout.dialog_share_singlemac_file,
                (ViewGroup)findViewById(R.id.id_dialog_share_singlemac_file));
        ShareSMacFileDialog.setContentView(V_ShareSMacFile);
        ShareSMacFileDialog.setTitle(R.string.Menu_Share);

        B_ShareSM_Save = (Button)V_ShareSMacFile.findViewById(R.id.id_b_ssm_save);
        B_ShareSM_Cancel = (Button)V_ShareSMacFile.findViewById(R.id.id_b_ssm_cancel);

        LY_ShareSM_Single = (LinearLayout)V_ShareSMacFile.findViewById(R.id.id_llyout_single_sel);
        LY_ShareSM_MAC = (LinearLayout)V_ShareSMacFile.findViewById(R.id.id_llyout_mac_sel);
        B_ShareSM_Single = (Button)V_ShareSMacFile.findViewById(R.id.id_b_sel_single);
        B_ShareSM_MAC = (Button)V_ShareSMacFile.findViewById(R.id.id_b_sel_mac);

        TV_ShareSM_Msg = (TextView)V_ShareSMacFile.findViewById(R.id.id_b_mac_msg);

        LY_ShareSM_Single.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_ShareSM_Single.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_press));
                B_ShareSM_MAC.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_normal));
                bool_ShareFileType = false;
                TV_ShareSM_Msg.setVisibility(View.GONE);
            }
        });

        LY_ShareSM_MAC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                B_ShareSM_Single.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_normal));
                B_ShareSM_MAC.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_press));
                bool_ShareFileType = true;
                TV_ShareSM_Msg.setVisibility(View.VISIBLE);
            }
        });


        B_ShareSM_Save.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        B_ShareSM_Save.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_ShareSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
                        B_ShareSM_Save.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_ShareSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        B_ShareSM_Save.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_ShareSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        String Name = ET_SSM_FileName.getText().toString();
                        SSM_Detials = ET_SSM_Detials.getText().toString();
                        ShareSMacFileDialog.cancel();
                        if(bool_ShareFileType){
                            fileNameString = DataStruct.ShareDefaultGruopName;
                            bool_ShareOrSaveMacSEFF = false;
                            ReadMacGroup();
                        }else{
                            fileNameString = DataStruct.ShareDefaultName;
                            ShareEffData();
                        }
                        break;
                    default:
                        B_ShareSM_Save.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_ShareSM_Save.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        break;
                }
                return false;
            }
        });

        B_ShareSM_Cancel.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        B_ShareSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_ShareSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
                        B_ShareSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_press);
                        B_ShareSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_press_color));
                        break;
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        B_ShareSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_ShareSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        ShareSMacFileDialog.cancel();

                        break;
                    default:
                        B_ShareSM_Cancel.setBackgroundResource(R.drawable.ssm_btn_normal);
                        B_ShareSM_Cancel.setTextColor(getResources().getColor(R.color.ssm_b_optsc_normal_color));
                        break;
                }
                return false;
            }
        });

    }

    private void showShareSingleMacSEFFileDialog(){
        ShareSMacFileDialog.show();
        B_ShareSM_Single.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_press));
        B_ShareSM_MAC.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_normal));
        TV_ShareSM_Msg.setVisibility(View.GONE);
        bool_ShareFileType = false;
    }
    */

    /**
     * bool_FileType:false-单机
     */
    public static boolean checkSEFFileIsExit(String Name, boolean bool_FileType, Context mContext) {
        File cache = new File(Environment.getExternalStorageDirectory(), MacCfg.AgentNAME);
        File destDir = new File(cache.toString() +
                "/" + MacCfg.Mac +
                "/" + "SoundEff"
        );
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String filePath = "";
        if (!bool_FileType) {
            filePath = destDir.toString() +
                    "/" + Name + Define.CHS_SEff_TYPE;//json
        } else {
            filePath = destDir.toString() +
                    "/" + Name + Define.CHS_SEff_MAC_TYPE;//json
        }

        SEFF_File SEFF_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
        if (SEFF_file != null) {
            ToastMsg(mContext.getResources().getString(R.string.ULSE_SAMENAME), mContext);
            return false;
        }
        return true;
    }


/*
    private void showSaveSingleMacSEFFileDialog(){
        SaveSMacFileDialog.show();
        B_SSM_Single.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_press));
        B_B_SSM_MAC.setBackground(getResources().getDrawable(R.drawable.ssm_btncheck_normal));
        TV_SSM_Msg.setVisibility(View.GONE);
        ET_SSM_FileName.setText("");
        bool_FileType = false;
    }
*/

    //更新单组音效数据
    public static boolean UpdateForJsonSingleData(String filePath, Context mContext) {
        String isJson = filePath.substring(filePath.length() - Define.CHS_SEff_TYPE_L, filePath.length());

        //System.out.println("data  load Seff File-："+filePath);
        System.out.println("BUG  if(msg.equals(DataStruct.BoardCast_Load_LocalJson)");

        if ((!filePath.equals(null)) && (isJson.equals(Define.CHS_SEff_TYPE))) {
            int res = UpdateJsonSingleData(filePath, mContext);
            if (res == 0) {
                // ToastMsg(mContext.getResources().getString(R.string.LoadSEff_Success),mContext);
                return true;
            } else if (res == 1) {
                //ToastMsg(mContext.getResources().getString(R.string.LoadSEff_Fail),mContext);
                return false;
            } else if (res == 2) {
                ToastMsg(mContext.getResources().getString(R.string.SEFF_WRONG_MAC), mContext);
                return false;
            }

        } else {
            ToastMsg(mContext.getResources().getString(R.string.FileError), mContext);
            return false;
        }
        return false;
    }

    //返回0：成功，1:文件解释出错，2：机型不匹配
    public static int UpdateJsonSingleData(String filePath, Context mContext) {
        int res = 0;
        boolean boolMac = false;
        boolean boolInitLoadUi = false;
        DSP_SingleData mDSP_SData = new DSP_SingleData();
        mDSP_SData = JsonRWUtil.LoadJsonLocal2DataStruce(mContext, filePath);
        System.out.println("BUG UpdateJsonSData  mDSP_SData=" + mDSP_SData);
        //printfDSP_SData(mDSP_SData);
        DataStruct.SEFF_USER_GROUP_OPT = 1;
        if (mDSP_SData != null) {

            System.out.println("BUG UpdateJsonSData  Get_client().Get_company_brand()=" + mDSP_SData.Get_client().Get_company_brand());
            System.out.println("BUG UpdateJsonSData  mDSP_SData.Get_data_info().Get_data_machine_type()=" + mDSP_SData.Get_data_info().Get_data_machine_type());

            if (!mDSP_SData.Get_client().Get_company_brand().equals(MacCfg.AgentID)) {
                return 2;
            }
            if (!mDSP_SData.Get_data_info().Get_data_machine_type().equals(MacCfg.Mac)) {
                return 2;
            }

            //SYSTEM
//            int[] pc_source_set =  new int[8];
//            pc_source_set =  mDSP_SData.Get_data().Get_SystemData().Get_pc_source_set();
//            int[] system_data =  new int[8];
//            system_data =  mDSP_SData.Get_data().Get_SystemData().Get_system_data();
//            int[] system_spk_type =  new int[8];
//            system_spk_type =  mDSP_SData.Get_data().Get_SystemData().Get_system_spk_type();
//            int[] system = new int[32];
//
//            for(int i=0;i<8;i++){
//                system[i]=pc_source_set[i];
//            }
//            for(int i=0;i<8;i++){
//                system[i+8]=system_data[i];
//            }
//            for(int i=0;i<16;i++){
//                system[i+16]=system_spk_type[i];
//            }

            //  FillRecDataStruct(Define.SYSTEM, 0, system, false);
            //MUSIC
//            int[] eff=new int[Define.EFF_LEN];
//            eff = mDSP_SData.Get_data().Get_DSP_EFFData().GetEFFData(0);
//            FillRecDataStruct(Define.EFF, 0, eff, false);

            DataStruct.SendDeviceData.SYS.main_vol = 0;//DataStruct.RcvDeviceData.SYS.main_vol;
            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = 0;//DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
            DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 8;

            SendDataToDevice(false);


            //MUSIC
            int[] music = new int[Define.IN_LEN];
            music = mDSP_SData.Get_data().Get_DSP_MusicData().GetMusicData(0);
            FillRecDataStruct(Define.MUSIC, 0, music, false);
            //OUTPUT
            int[][] output = new int[Define.MAX_CH][Define.OUT_LEN];
            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
                output[i] = mDSP_SData.Get_data().Get_DSP_OutputData().GetOutputData(i);
                FillRecDataStruct(Define.OUTPUT, i, output[i], false);
            }
            System.out.println("BUG TEXT.level=" + DataStruct.RcvDeviceData.OUT_CH[0].EQ[1].level);
            // MacCfg.BOOL_LoadSeffMute = true;
            //刷新界面

            //motinlu 20180612
            ComparedToSendData(false);
            //DataStruct.SendbufferList.clear();




            SaveGroupData(0);
            DataStruct.SEFF_USER_GROUP_OPT = 1;
            Intent intent3 = new Intent();
            intent3.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intent3.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
            intent3.putExtra("state", true);
            mContext.sendBroadcast(intent3);

            //SaveGroupData(0);


            DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;//DataStruct.RcvDeviceData.SYS.main_vol;
            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;//DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
            DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 8;

            SendDataToDevice(false);
            //刷新界面
            Intent intentw = new Intent();
            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intentw.putExtra("msg", Define.BoardCast_FlashUI_Page);
            intentw.putExtra("state", true);
            mContext.sendBroadcast(intentw);

            //加到数据库存中
            DSP_DataInfo dataInfo = mDSP_SData.Get_data_info();
            SEFF_File seff_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
            int dot = filePath.lastIndexOf("/");
            String name = filePath.substring(dot + 1);
            //Log.e("test#",name);
            name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
            if (seff_file == null) {
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
            res = 0;
        } else {
            res = 1;
        }
        return res;
    }

    //分享单组音效文件
    public static void ShareEffData(Context mContext) {
//		if(!isConnected()){
//			ToastMsg(getResources().getString(R.string.please_connect_msg));
//			return;
//		}

        boolean res = SaveSingleSEFF_JSON2Local(DataStruct.fileNameString,
                DataStruct.fileNameString, mContext);
        if (res) {
            ToastMsg(mContext.getResources().getString(R.string.Save_success), mContext);
            if (Define.DEFALIB == Define.DEF_APP) {
                JsonRWUtil.ShareSoundEFFData(
                        mContext, Define.ShareDefaultName, 0);
            }
//            UpdateLocalSEffList(mContext);
        } else {
            ToastMsg(mContext.getResources().getString(R.string.ShareError), mContext);
        }
    }



    public static void SaveSingleSEFFData(String FileName, Context mContext) {
//		if(!isConnected()){
//			ToastMsg(getResources().getString(R.string.please_connect_msg));
//			return;
//		}

        boolean res = SaveSingleSEFF_JSON2Local(FileName,
                FileName, mContext);
        if (!res) {
            ToastMsg(mContext.getResources().getString(R.string.ShareError), mContext);
        } else {
            ToastMsg(mContext.getResources().getString(R.string.Save_success), mContext);
        }
    }

    //分享整机音效文件
    public static void ReadMacGroup(Context mContext) {
        if (!DataStruct.isConnecting) {
            ToastMsg(mContext.getResources().getString(R.string.please_connect_msg), mContext);
            return;
        }
        DataStruct.SendbufferList.clear();
        DataStruct.SendbufferList.removeAll(DataStruct.SendbufferList);

        //增加当前组数据
        //EFF结构赋值
        // FillSedDataStruct(Define.EFF, 0);
//        for(int cn=0;cn<Define.EFF_LEN;cn++){
//            DataStruct.MAC_DataBuf.data[0].eff.eff[0][cn] = DataStruct.ChannelBuf[cn];
//        }

        //MUSIC结构赋值
        FillSedDataStruct(Define.MUSIC, 0);
        //RcvDeviceData.MAC_Data.data[0].music.music[0] = ChannelBuf;
        for (int cn = 0; cn < Define.IN_LEN; cn++) {
            DataStruct.MAC_DataBuf.data[0].music.music[0][cn] = DataStruct.ChannelBuf[cn];
        }
        //mDSP_MusicData.SetMusicData(0, ChannelBuf);
        //OUTPUT结构赋值
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            FillSedDataStruct(Define.OUTPUT, i);
            //mDSP_OutputData.SetOutputData(i, ChannelBuf);
            for (int cn = 0; cn < Define.OUT_LEN; cn++) {
                DataStruct.MAC_DataBuf.data[0].output.output[i][cn] = DataStruct.ChannelBuf[cn];
            }

        }
        DataStruct.MAC_DataBuf.data[0].group_name = DataStruct.RcvDeviceData.SYS.UserGroup[0];//给与用户组1，一样的名字

        MacCfg.bool_ReadMacGroup = true;
        DataStruct.SEFF_USER_GROUP_OPT = 2;

//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
//            //插入静音包
//            DataStruct.SendDeviceData.SYS.main_vol = 0;//DataStruct.RcvDeviceData.SYS.main_vol;
//            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
//            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
//            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
//            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
//            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = 0;//DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
//            DataStruct.SendDeviceData.SYS.offTime= DataStruct.RcvDeviceData.SYS.OffTime;
//
//            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//            DataStruct.SendDeviceData.PcCustom = 0x00;
//            DataStruct.SendDeviceData.DataLen = 8;
//
//            SendDataToDevice(false);
//        }
        /*获取用户名字*/
        for (int i = 1; i <= DataStruct.CurMacMode.MAX_USE_GROUP; i++) {
            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = i;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.GROUP_NAME;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;
            SendDataToDevice(false);
        }
        for (int i = 1; i <= DataStruct.CurMacMode.MAX_USE_GROUP; i++) {
//            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
//                /*增加读取Input数据，获取音量 0x77*/
//                DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
//                DataStruct.SendDeviceData.DeviceID = 0x01;
//                DataStruct.SendDeviceData.UserID = i;
//                DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                DataStruct.SendDeviceData.ChannelID = 0x02;//MUSIC 固定2
//                DataStruct.SendDeviceData.DataID = 0x77;//DataStruct.IN_MISC_ID;
//                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//                DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                DataStruct.SendDeviceData.DataLen = 0;
//                SendDataToDevice(false);
//            }

            //读取音效数据
//            DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = i&0xff;
//            DataStruct.SendDeviceData.DataType = Define.EFF;
//            DataStruct.SendDeviceData.ChannelID = 0x00;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//            DataStruct.SendDeviceData.PcCustom = 0x00;
//            DataStruct.SendDeviceData.DataLen = 0x00;
//            SendDataToDevice(false);

            /*增加读取全部通道的输出数据*/
            for (int j = 0; j < DataStruct.CurMacMode.Out.OUT_CH_MAX; j++) {
                DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = i;
                DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                DataStruct.SendDeviceData.ChannelID = j;
                DataStruct.SendDeviceData.DataID = 0x77;/*读当前组的数据*/
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
                DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                DataStruct.SendDeviceData.DataLen = 0;
                SendDataToDevice(false);
            }
        }

//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
//            //插入静音包
//            DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;
//            DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
//            DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
//            DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
//            DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
//            DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
//            DataStruct.SendDeviceData.SYS.offTime= DataStruct.RcvDeviceData.SYS.OffTime;
//
//            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//            DataStruct.SendDeviceData.PcCustom = 0x00;
//            DataStruct.SendDeviceData.DataLen = 8;
//
//            SendDataToDevice(false);
//        }

        //刷新界面连接进度
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
        intentw.putExtra("state", false);
        mContext.sendBroadcast(intentw);
    }

    //分享整机音效文件
    public static void ShareMacEffData(Context mContext) {
        boolean res = SaveMACSEFF_JSON2Local(Define.ShareDefaultGruopName, mContext);
        if (res) {
            ToastMsg(mContext.getResources().getString(R.string.Save_success), mContext);
            if (Define.DEFALIB == Define.DEF_APP) {
                JsonRWUtil.ShareSoundEFFData(
                        mContext, Define.ShareDefaultGruopName, 1);
            }
//            UpdateLocalSEffList(mContext);
        } else {
            ToastMsg(mContext.getResources().getString(R.string.ShareError), mContext);
        }
    }

    public static void saveMacGroupData2Mac(Context mContext) {
        int[] system = new int[Define.MAX_SYSTEM];
        //更新界面
        //String nameString = "sa{";
        for (int group = 0; group <= DataStruct.CurMacMode.MAX_USE_GROUP; group++) {
            DataStruct.RcvDeviceData.SYS.UserGroup[group] = DataStruct.MAC_DataBuf.data[group].group_name;
//			nameString = "sa{";
//			for(int i=0;i<16;i++){
//				nameString += (String.valueOf(RcvDeviceData.UserGroup[group][i])+",");
//			}
//			System.out.println("BUG nameString ="+nameString);
        }

        //MUSIC
        for (int i = 0; i < system.length; i++) {
            system[i] = DataStruct.MAC_DataBuf.system[i];
        }

        FillRecDataStruct(Define.SYSTEM, 0, system, false);
        //MUSIC
        FillRecDataStruct(Define.MUSIC, 0, DataStruct.MAC_DataBuf.data[0].music.music[0], false);
        //EFF
        //  FillRecDataStruct(Define.EFF, 0, DataStruct.MAC_DataBuf.data[0].eff.eff[0], false);
        //OUTPUT
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            FillRecDataStruct(Define.OUTPUT, i, DataStruct.MAC_DataBuf.data[0].output.output[i],
                    false);
        }
        ComparedToSendData(true);
        //刷新界面连接
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_Page);
        intentw.putExtra("state", true);
        mContext.sendBroadcast(intentw);

//        DataStruct.SendbufferList.clear();
//        DataStruct.SendbufferList.removeAll(DataStruct.SendbufferList);
        DataStruct.SEFF_USER_GROUP_OPT = 1;
        //DataStruct.SaveFlag = true;
        //DataStruct.MAX_USE_GROUP

//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
        //插入静音包
        DataStruct.SendDeviceData.SYS.main_vol = 0;//DataStruct.RcvDeviceData.SYS.main_vol;
        DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
        DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
        DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
        DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
        DataStruct.SendDeviceData.SYS.MainvolMuteFlg = 0;//DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
        DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;

        SendDataToDevice(false);
//        }

        for (int group = 0; group <= DataStruct.CurMacMode.MAX_USE_GROUP; group++) {
            //开始传输
            //增加保存数据开始传输标志TODO
//            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_TRANSMITTAL;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//            DataStruct.SendDeviceData.PcCustom = 0x00;
//            DataStruct.SendDeviceData.DataLen = 8;
//            DataStruct.SendDeviceData.SYS.TRANSMITTAL[0]=1;
//            DataStruct.SendDeviceData.SYS.TRANSMITTAL[1]=(byte)(group&0xff);
//            for(int i=2;i<8;i++){
//                DataStruct.SendDeviceData.SYS.TRANSMITTAL[i]=0;
//            }
//            SendDataToDevice(false);

            //用户组名字
            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = group;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.GROUP_NAME;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x10;
            SendDataToDevice(false);

            /*保存 Musec Input数据，保存音量*/
//            for (int i = 0; i < 1; i++) {
//            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
//                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//                DataStruct.SendDeviceData.DeviceID = 0x01;
//                DataStruct.SendDeviceData.UserID =  group;
//                DataStruct.SendDeviceData.DataType = Define.MUSIC;
//                DataStruct.SendDeviceData.ChannelID = 0x02;//MUSIC 固定2
//                DataStruct.SendDeviceData.DataID = 0x00;//DataStruct.IN_MISC_ID;
//                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//                DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//                DataStruct.SendDeviceData.DataLen = Define.IN_LEN;//DataStruct.IN_LEN;
//                FillSedDataStruct(Define.MUSIC, DataStruct.MAC_DataBuf.data[group].music.music[0]);
//                SendDataToDevice(false);
//            }

//            }
            // 保存音效组数据
//            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID =  (byte)(group&0xff);
//            DataStruct.SendDeviceData.DataType = Define.EFF;
//            DataStruct.SendDeviceData.ChannelID = 0x0;//MUSIC 固定2
//            DataStruct.SendDeviceData.DataID = 0x00;//DataStruct.IN_MISC_ID;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
//            DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
//            DataStruct.SendDeviceData.DataLen = Define.EFF_LEN;
//            FillSedDataStruct(Define.EFF, 0);
//            SendDataToDevice(false);
            /*保存输出通道的输出数据 DataStruct.CurMacMode.Out.OUT_CH_MAX */
            //System.out.println("BUG XXXXXXXXXXXXXXX  group="+group);
            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                DataStruct.SendDeviceData.DeviceID = 0x01;
                DataStruct.SendDeviceData.UserID = group;
                DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                DataStruct.SendDeviceData.ChannelID = i;
                DataStruct.SendDeviceData.DataID = 0x00;//DataStruct.IN_MISC_ID;
                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
                DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                DataStruct.SendDeviceData.DataLen = Define.OUT_LEN;//DataStruct.IN_LEN;
                FillSedDataStruct(Define.OUTPUT, DataStruct.MAC_DataBuf.data[group].output.output[i]);
                SendDataToDevice(false);
            }

//            //增加保存数据完成标志
//            DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
//            DataStruct.SendDeviceData.DeviceID = 0x01;
//            DataStruct.SendDeviceData.UserID = 0x00;
//            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
//            DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_TRANSMITTAL;
//            DataStruct.SendDeviceData.DataID = 0x00;
//            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
//            DataStruct.SendDeviceData.PcCustom = 0x00;
//            DataStruct.SendDeviceData.DataLen = 8;
//
//            DataStruct.SendDeviceData.SYS.TRANSMITTAL[0]=0;//传输结束
//            DataStruct.SendDeviceData.SYS.TRANSMITTAL[1]=(byte)(group&0xff);
//            for(int i=2;i<8;i++){
//                DataStruct.SendDeviceData.SYS.TRANSMITTAL[i]=0;
//            }
//            SendDataToDevice(false);
        }

//        if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_SYSTEM){
        //插入静音包
        DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;
        DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
        DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
        DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
        DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
        DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
        DataStruct.SendDeviceData.SYS.offTime = DataStruct.RcvDeviceData.SYS.offTime;

        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
        DataStruct.SendDeviceData.DeviceID = 0x01;
        DataStruct.SendDeviceData.UserID = 0x00;
        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
        DataStruct.SendDeviceData.DataID = 0x00;
        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
        DataStruct.SendDeviceData.PcCustom = 0x00;
        DataStruct.SendDeviceData.DataLen = 8;

        SendDataToDevice(false);
//        }

        SaveSystemData();


//        //刷新界面连接进度
        Intent intentL = new Intent();
        intentL.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentL.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
        intentL.putExtra("state", false);
        mContext.sendBroadcast(intentL);
    }

    //加载整机
    //TODO
    //返回0：成功，1:文件解释出错，2：机型不匹配
    public static boolean loadMacEffJsonData(String filePath, Context mContext) {
        boolean boolMac = false;
        boolean boolInitLoadUi = false;
        DSP_MACData MAC_Data = new DSP_MACData();
        MAC_Data = JsonRWUtil.loadMacDataJson2DataStruce(mContext, filePath);
        //System.out.println("BUG loadMacDataJson2DataStruce  boolRes="+boolRes);
        //ToastMsg("loadMacDataJson2DataStruce boolRes="+boolRes);
        if (MAC_Data != null) {
            System.out.println("BUG 获取到的ID为" + MAC_Data.Get_client().Get_company_brand() + "名字为" + MAC_Data.Get_data_info().Get_data_machine_type());
            if (!MAC_Data.Get_client().Get_company_brand().equals(MacCfg.AgentID) || !MAC_Data.Get_data_info().Get_data_machine_type().equals(MacCfg.Mac)) {
                ToastMsg(mContext.getResources().getString(R.string.FileError), mContext);
                return false;
            }

            //加到数据库存中
            DSP_DataInfo dataInfo = MAC_Data.Get_data_info();
            SEFF_File seff_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
            int dot = filePath.lastIndexOf("/");
            String name = filePath.substring(dot + 1);
            //Log.e("test#",name);
            name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
            if (seff_file == null) {
                DataStruct.dbSEfFile_Table.insert(new SEFF_File(
                        "file_id",//file_id
                        MAC_Data.Get_fileType(),//file_type
                        name,//file_name
                        filePath,//file_path
                        "0",//file_favorite
                        "0",//file_love
                        "200",//file_size
                        MAC_Data.Get_data_info().Get_data_upload_time(),//file_time
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

            DataStruct.MAC_DataBuf = MAC_Data;
            //DataStruct.RcvDeviceData.MAC_Data = MAC_Data;
            saveMacGroupData2Mac(mContext);
            return true;
        } else {
            ToastMsg(mContext.getResources().getString(R.string.FileError), mContext);
            return false;
        }
    }

    static Context mContextn;

    public static void UpdateLocalSEffList(Context mContext) {
        mContextn = mContext;
        DataStruct.LocalSEffFile_List.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {  //
                try {
                    //用此方法何总手机会死机
                    //GetFiles(Environment.getExternalStorageDirectory().toString(), DataStruct.CHS_SEff_TYPE,true);
                    queryFiles(mContextn.getApplicationContext());
                } catch (Exception e) {
                    System.out.println("SD Exception GetFiles");
                }
            }
        }).start();

    }

    @SuppressWarnings("unused")
    public static void UploadEffData(Context mContext) {
        boolean res = SaveSingleSEFF_JSON2Local(Define.ShareDefaultName, DataStruct.user.Get_fileName(), mContext);
        System.out.println("BUG UploadEffData.SaveSingleSEFF_JSON2Local.res=" + res);
//        if(res){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Operaton operaton=new Operaton();
//                    operaton.uploadFileSEFF(mContext);
//                }
//            }).start();
//
//        }else{
//            ToastMsg(getResources().getString(R.string.ShareError));
//        }


    }

    /**
     * 保存音效文件
     */
    //TODO
    public static boolean SaveSingleSEFF_JSON2Local(String fileName, String gruouName, Context mContext) {
        //CHS
        Company chs = new Company();
        chs = getCHS(mContext);

        //client
        Company client = new Company();
        client = getClient();

        //data_info
        DSP_DataInfo data_info = new DSP_DataInfo();
        data_info = getData_info();

        //data
        DSP_Data Data = new DSP_Data();
        Data = getSingle_Data();

        DSP_SingleData mDSP_SData = new DSP_SingleData(chs, client, data_info, Data);
        boolean res = false;
        res = JsonRWUtil.SaveSingleJson2Local(
                mContext,
                fileName,
                mDSP_SData
        );

        return res;
    }

    //保存整机文件
    public static boolean SaveMACSEFF_JSON2Local(String fileName, Context mContext) {
        boolean res = false;
        res = JsonRWUtil.SaveMACJson2Local(
                mContext,
                fileName,
                DataStruct.MAC_DataBuf
        );
        if (!res) {
            ToastMsg(mContext.getResources().getString(R.string.Save_error), mContext);
        } else {
            ToastMsg(mContext.getResources().getString(R.string.Save_success), mContext);
        }

        return res;
    }

    //单组Data的数据
    public static DSP_Data getSingle_Data() {

        //SYSTEM结构赋值
//        DSP_SystemData mDSP_SystemData=new DSP_SystemData();
//        mDSP_SystemData = getSystemData();

        //MUSIC结构赋值
        FillSedDataStruct(Define.MUSIC, 0);
        DataStruct.mDSP_MusicData.SetMusicData(0, DataStruct.ChannelBuf);


        //OUTPUT结构赋值
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            FillSedDataStruct(Define.OUTPUT, i);
            DataStruct.mDSP_OutputData.SetOutputData(i, DataStruct.ChannelBuf);
        }

        DSP_Data Data = new DSP_Data(DataStruct.mDSP_OutputData,
                DataStruct.mDSP_MusicData,
                getSystemData(),
                DataStruct.RcvDeviceData.SYS.UserGroup[0]);
        return Data;
    }

    //整机Data的数据
    public static DSP_DataMac[] getDefaultDSP_DataMac() {
        DSP_DataMac[] mDSP_DataMacArry = new DSP_DataMac[DataStruct.CurMacMode.MAX_USE_GROUP + 1];
        for (int i = 0; i < DataStruct.CurMacMode.MAX_USE_GROUP + 1; i++) {
            mDSP_DataMacArry[i] = getGroupSingle_Data(i);
        }
        return mDSP_DataMacArry;
    }

    //单组整机数据中的 Data的数据
    public static DSP_DataMac getGroupSingle_Data(int index) {
        //MUSIC结构赋值
        FillSedDataStruct(Define.MUSIC, 0);
        DataStruct.mDSP_MusicData.SetMusicData(0, DataStruct.ChannelBuf);
        //EFF结构赋值
//        FillSedDataStruct(Define.EFF, 0);
//        DataStruct.mDSP_EFFData.SetEFFData(0, DataStruct.ChannelBuf);
        //OUTPUT结构赋值
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
            FillSedDataStruct(Define.OUTPUT, i);
            DataStruct.mDSP_OutputData.SetOutputData(i, DataStruct.ChannelBuf);
        }

        DSP_DataMac Data = new DSP_DataMac(DataStruct.mDSP_OutputData,
                DataStruct.mDSP_MusicData,
                DataStruct.RcvDeviceData.SYS.UserGroup[index]);
        return Data;
    }

    //获取System数据
    public static int[] getSystemData() {
        //SYSTEM结构赋值
        DSP_SystemData mDSP_SystemData = new DSP_SystemData();
        //只改为要改变的，其他不要改变
        int[] SystemBuf = new int[Define.MAX_SYSTEM];
        //PC_SOURCE_SET
        SystemBuf[0] = (byte) (DataStruct.RcvDeviceData.SYS.input_source & 0xff);
        SystemBuf[1] = (byte) (DataStruct.RcvDeviceData.SYS.aux_mode & 0xff);
        SystemBuf[2] = (byte) (DataStruct.RcvDeviceData.SYS.device_mode & 0xff);
        SystemBuf[3] = (byte) (DataStruct.RcvDeviceData.SYS.hi_mode & 0xff);
        SystemBuf[4] = (byte) (DataStruct.RcvDeviceData.SYS.blue_gain & 0xff);
        SystemBuf[5] = (byte) (DataStruct.RcvDeviceData.SYS.aux_gain & 0xff);
        SystemBuf[6] = (byte) (DataStruct.RcvDeviceData.SYS.Safety & 0xff);
        SystemBuf[7] = (byte) (DataStruct.RcvDeviceData.SYS.sound_effect & 0xff);
        //SYSTEM_DATA
        SystemBuf[8] = (byte) (DataStruct.RcvDeviceData.SYS.main_vol & 0xff);
        SystemBuf[9] = (byte) ((DataStruct.RcvDeviceData.SYS.main_vol >> 8) & 0xff);
        SystemBuf[10] = (byte) (DataStruct.RcvDeviceData.SYS.alldelay & 0xff);
        SystemBuf[11] = (byte) (DataStruct.RcvDeviceData.SYS.noisegate_t & 0xff);
        SystemBuf[12] = (byte) (DataStruct.RcvDeviceData.SYS.AutoSource & 0xff);
        SystemBuf[13] = (byte) (DataStruct.RcvDeviceData.SYS.AutoSourcedB & 0xff);
        SystemBuf[14] = (byte) (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg & 0xff);
        SystemBuf[15] = (byte) (DataStruct.RcvDeviceData.SYS.offTime & 0xff);
        //SYSTEM_SPK_TYPE
        SystemBuf[16] = (byte) (DataStruct.RcvDeviceData.SYS.out1_spk_type & 0xff);
        SystemBuf[17] = (byte) (DataStruct.RcvDeviceData.SYS.out2_spk_type & 0xff);
        SystemBuf[18] = (byte) (DataStruct.RcvDeviceData.SYS.out3_spk_type & 0xff);
        SystemBuf[19] = (byte) (DataStruct.RcvDeviceData.SYS.out4_spk_type & 0xff);
        SystemBuf[20] = (byte) (DataStruct.RcvDeviceData.SYS.out5_spk_type & 0xff);
        SystemBuf[21] = (byte) (DataStruct.RcvDeviceData.SYS.out6_spk_type & 0xff);
        SystemBuf[22] = (byte) (DataStruct.RcvDeviceData.SYS.out7_spk_type & 0xff);
        SystemBuf[23] = (byte) (DataStruct.RcvDeviceData.SYS.out8_spk_type & 0xff);
        SystemBuf[24] = (byte) (DataStruct.RcvDeviceData.SYS.CurGroupID & 0xff);
        SystemBuf[25] = (byte) (DataStruct.RcvDeviceData.SYS.out10_spk_type & 0xff);
        SystemBuf[26] = (byte) (DataStruct.RcvDeviceData.SYS.out11_spk_type & 0xff);
        SystemBuf[27] = (byte) (DataStruct.RcvDeviceData.SYS.out12_spk_type & 0xff);
        SystemBuf[28] = (byte) (DataStruct.RcvDeviceData.SYS.out13_spk_type & 0xff);
        SystemBuf[29] = (byte) (DataStruct.RcvDeviceData.SYS.out14_spk_type & 0xff);
        SystemBuf[30] = (byte) (DataStruct.RcvDeviceData.SYS.out15_spk_type & 0xff);
        SystemBuf[31] = (byte) (DataStruct.RcvDeviceData.SYS.out16_spk_type & 0xff);


        for (int i = 0; i < DataStruct.RcvDeviceData.SYS.RESET_MCU.length; i++) {
            SystemBuf[32 + i] = (byte) (DataStruct.RcvDeviceData.SYS.RESET_MCU[i] & 0xff);
        }


        // mDSP_SystemData.Set_system(SystemBuf);
        //系统延时


        return SystemBuf;
    }

    //获取数据信息
    public static DSP_DataInfo getData_info() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());

        if (DataStruct.DeviceVerString == null) {
            DataStruct.DeviceVerString = "";
        }

        return new DSP_DataInfo(
                "xiao",//Set_data_user_name
                "13570379864",//Set_data_user_tel
                DataStruct.user.Get_email(),//Set_data_user_mailbox
                "MC",//Set_data_user_info
                MacCfg.Mac,//DataStruct.CurMacMode.MacType,//Set_data_machine_typeMacCfg.MAC_Type
                DataStruct.user.Get_carType_Cid(),//Set_data_car_type
                "2016-02-30",//Set_data_car_brand
                MacCfg.Json_versions,//Set_data_json_version
                DataStruct.DeviceVerString,//Set_data_mcu_version
                MacCfg.App_versions,//Set_data_android_version
                "IOS-?",//Set_data_ios_version
                "PC-?",//Set_data_pc_version
                "0",//Set_data_group_num
                DataStruct.user.Get_seff_name(),//Set_data_group_name
                DataStruct.SSM_Detials,//DataStruct.user.Get_seff_detials(),//Set_data_eff_briefing
                date,//Set_data_upload_time
                0,//Set_data_encryption_byte
                "0",

                MacCfg.HEAD_DATA//Set_data_head_data
        );
    }

    //获取客户信息
    public static Company getClient() {
        return new Company(
                GetAgentIDName(),//Set_company_name
                "020-XXXXXXXX",//Set_company_tel
                "020-XXXXXXXX",//Set_company_contacts
                "www.XX.com",//Set_company_web
                "WEIXIN",//Set_company_weixin
                "QQ",//Set_company_qq
                GetAgentIDName(),//Set_company_briefing_en
                GetAgentIDName(),//Set_company_briefing_zh
                MacCfg.AgentID//Set_company_brand
        );
    }

    //获取CHS信息
    public static Company getCHS(Context mContext) {
        return new Company(
                mContext.getResources().getString(R.string.chs_name),
                mContext.getResources().getString(R.string.chs_tel),
                mContext.getResources().getString(R.string.chs_contacts),
                mContext.getResources().getString(R.string.chs_web),
                mContext.getResources().getString(R.string.chs_weixin),
                mContext.getResources().getString(R.string.chs_qq),
                mContext.getResources().getString(R.string.chs_briefing_en),
                mContext.getResources().getString(R.string.chs_briefing_zh),
                mContext.getResources().getString(R.string.chs_brand)
        );
    }

    //获取客户名字
    public static String GetAgentIDName() {
        String name = "CHS";
        switch (MacCfg.Agent_ID) {
            case 1:
                name = "佰芙";
                break;
            case 2:
                name = "阿尔派";
                break;
            case 3:
                name = "合德";
                break;
            case 4:
                name = "惠州惠诺";
                break;
            case 5:
                name = "御音";
                break;
            case 6:
                name = "锐高";
                break;
            case 7:
                name = "迪声";
                break;
            case 8:
                name = "声鑫";
                break;
            case 9:
                name = "鹏辉";
                break;
            case 10:
                name = "芬朗";
                break;
            case 11:
                name = "汇隆";
                break;
            case 12:
                name = "卡莱";
                break;
            case 13:
                name = "云晶";
                break;
            case 14:
                name = "江波";
                break;
            case 15:
                name = "俊宏";
                break;
            case 16:
                name = "酷派";
                break;
            case 17:
                name = "盈必达";
                break;
            case 18:
                name = "车厘子";
                break;
            case 19:
                name = "荣鼎";
                break;
            case 20:
                name = "译宝";
                break;
            default:
                break;
        }
        return name;
    }

    public static void GetFiles(String Path, String Extension, boolean IsIterative, Context mContext) { //搜索目录，扩展名，是否进入子文件夹
        File[] files = new File(Path).listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) { //判断扩展名
                    String path = f.getPath();
                    String dec = path.substring(path.length() - Define.CHS_SEff_TYPE_L, path.length());
//              	    System.out.println("SD isJson:"+isJson);
                    if (dec.equals(Define.CHS_SEff_TYPE)) {
                        int dot = path.lastIndexOf("/");
                        String name = path.substring(dot + 1);

                        name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
                        SEFF_File seff_File = new SEFF_File();

                        DSP_SingleData mDSP_SData = new DSP_SingleData();
                        mDSP_SData = JsonRWUtil.LoadJsonLocal2DSP_DataInfo(mContext, path);
                        //System.out.println("DBTEST --GetFiles-----name:"+name+",mDSP_SData:"+mDSP_SData);
                        if (mDSP_SData != null) {
                            DSP_DataInfo dataInfo = mDSP_SData.Get_data_info();
                            seff_File.Set_file_id("id?");
                            seff_File.Set_file_type(mDSP_SData.Get_fileType());
                            seff_File.Set_file_name(name);
                            seff_File.Set_file_path(path);
                            seff_File.Set_file_favorite("0");
                            seff_File.Set_file_love("0");
                            seff_File.Set_file_size("size?");
                            seff_File.Set_file_time("time?");
                            seff_File.Set_file_msg("msg?");


                            seff_File.Set_data_user_name(dataInfo.Get_data_user_name());
                            seff_File.Set_data_machine_type(dataInfo.Get_data_machine_type());
                            seff_File.Set_data_car_type(dataInfo.Get_data_car_type());
                            seff_File.Set_data_car_brand(dataInfo.Get_data_car_brand());
                            seff_File.Set_data_group_name(dataInfo.Get_data_group_name());
                            seff_File.Set_data_upload_time(dataInfo.Get_data_upload_time());
                            seff_File.Set_data_eff_briefing(dataInfo.Get_data_eff_briefing());

                            DataStruct.LocalSEffFile_List.add(seff_File);
                        }


                    }

                    //System.out.println("SD  -----"+f.getPath());
                }
                if (!IsIterative) {
                    break;
                }
            } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) { //忽略点文件（隐藏文件/文件夹）
                GetFiles(f.getPath(), Extension, IsIterative, mContext);
            }
        }
    }

    /**
     * 初始化数据库
     */
//    public static void initDatabases(Context mContext) {
//        if (DataStruct.db != null && DataStruct.db.isOpen()) {
//            DataStruct.db.close();
//            Log.i("info", "db is lll");
//        }
//        // --------获取数据库对象----------//
//        DataStruct.DataBaseMusicHelper = new DataBaseOpenHelper(mContext);
//        DataStruct.db=DataStruct.DataBaseMusicHelper.getReadableDatabase();
//        DataStruct.dbSEffData_Table = new DB_SEffData_Table(mContext, DataStruct.db);
//        DataStruct.dbSEfFile_Table = new DB_SEffFile_Table(mContext, DataStruct.db);
//        DataStruct.dbSEfFile_Recently_Table = new DB_SEffFile_Recently_Table(mContext, DataStruct.db);
//        DataStruct.dbLoginSM_Table = new DB_LoginSM_Table(mContext, DataStruct.db);
//
//        DataStruct.CCM_DataBaseHelper = new DataBaseCCMHelper(mContext);
//        DataStruct.db_CCM=DataStruct.CCM_DataBaseHelper.getReadableDatabase();
//        DataStruct.dbCarBrands_Table = new CarBrands_Table(mContext, DataStruct.db_CCM);
//        DataStruct.dbCarTypes_Table = new CarTypes_Table(mContext, DataStruct.db_CCM);
//        DataStruct.dbMacTypes_Table = new MacTypes_Table(mContext, DataStruct.db_CCM);
//        DataStruct.dbMacsAgentName_Table = new MacsAgentName_Table(mContext, DataStruct.db_CCM);
//    }
//    public static  void ExitDatabases() {
//        //DataBaseMusicHelper = DataBaseOpenHelper.getInstance(mContext);
//        // 调用getReadableDatabase方法如果数据库不存在 则创建 如果存在则打开
//        //db = DataBaseMusicHelper.getReadableDatabase();
//        // 关闭数据库
//        DataStruct.DataBaseMusicHelper.close();
//        DataStruct.CCM_DataBaseHelper.close();
//        // 删除数据库
//        //DataBaseMusicHelper.deleteDatabase(mContext);
//    }
//    public static void scanDirAsync(Context mContext) {
//        String dir=Environment.getExternalStorageDirectory().toString();
//        System.out.println("BUG scanDirAsync dir:"+dir);
//        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
//        scanIntent.setData(Uri.fromFile(new File(dir)));
//        mContext.sendBroadcast(scanIntent);
//    }
    public static void queryFiles(Context mContext) {
        String name = "";
        String dec = "";

        DSP_DataInfo dataInfo = new DSP_DataInfo();
        DSP_SingleData mDSP_SData = new DSP_SingleData();
        String[] projection = new String[]{BaseColumns._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.SIZE
        };
        Cursor cursor = mContext.getContentResolver().query(
                Uri.parse("content://media/external/file"),//"content://media/external/file"
                // Uri.parse("content://storage/emulated/0/"),
                projection,
                MediaStore.MediaColumns.DATA + " like ?",
                //new String[]{"%.json"},//+DataStruct.CHS_SEff_TYPE
                new String[]{"%" + Define.CHS_SEff_TYPE},//+DataStruct.CHS_SEff_TYPE
                //new String[]{"%.data"},//+DataStruct.CHS_SEff_TYPE
                null);
        System.out.println("BUG cursor:" + cursor);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idindex = cursor
                        .getColumnIndex(BaseColumns._ID);
                int dataindex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATA);
                int sizeindex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.SIZE);
                int addedindex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATE_ADDED);
                do {
                    cursor.getString(idindex);
                    String path = cursor.getString(dataindex);
                    String size = cursor.getString(sizeindex);
                    String added = cursor.getString(addedindex);


                    dec = path.substring(path.length() - Define.CHS_SEff_TYPE_L, path.length());
                    System.out.println("BUG queryFiles path:" + path);
                    if (dec.equals(Define.CHS_SEff_TYPE)) {
                        int dot = path.lastIndexOf("/");
                        name = path.substring(dot + 1);
                        //Log.e("test#",name);
                        name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
                        Log.e("test@", name);
                        SEFF_File seff_File = new SEFF_File();
                        mDSP_SData = JsonRWUtil.LoadJsonLocal2DSP_DataInfo(mContext, path);
                        //System.out.println("DBTEST --GetFiles-----name:"+name+",mDSP_SData:"+mDSP_SData);
                        if (mDSP_SData != null) {
                            if (mDSP_SData != null) {
                                dataInfo = mDSP_SData.Get_data_info();
                                seff_File.Set_file_id("id?");
                                seff_File.Set_file_type(mDSP_SData.Get_fileType());
                                seff_File.Set_file_name(name);
                                seff_File.Set_file_path(path);
                                seff_File.Set_file_favorite("0");
                                seff_File.Set_file_love("0");
                                seff_File.Set_file_size(size);
                                seff_File.Set_file_time(added);
                                seff_File.Set_file_msg("msg?");


                                seff_File.Set_data_user_name(dataInfo.Get_data_user_name());
                                seff_File.Set_data_machine_type(dataInfo.Get_data_machine_type());
                                seff_File.Set_data_car_type(dataInfo.Get_data_car_type());
                                seff_File.Set_data_car_brand(dataInfo.Get_data_car_brand());
                                seff_File.Set_data_group_name(dataInfo.Get_data_group_name());
                                seff_File.Set_data_upload_time(dataInfo.Get_data_upload_time());
                                seff_File.Set_data_eff_briefing(dataInfo.Get_data_eff_briefing());

                                DataStruct.LocalSEffFile_List.add(seff_File);
                            }
                        	/*
                        	dbSEfFile_Table.insert(new SEFF_File(
                        		id,//file_id
                        		mDSP_SData.Get_fileType(),//file_name
                        		name,//file_name
                				path,//file_path
                				"0",//file_favorite
                				"0",//file_love
                				size,//file_size
                				added,//file_time
                				"msg?",//file_msg

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
                			*/
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        //整机
        cursor = mContext.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.MediaColumns.DATA + " like ?",
                new String[]{"%" + Define.CHS_SEff_MAC_TYPE},
                null);

        System.out.println("BUG cursor:" + cursor);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idindex = cursor
                        .getColumnIndex(BaseColumns._ID);
                int dataindex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATA);
                int sizeindex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.SIZE);
                int addedindex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATE_ADDED);
                do {
                    cursor.getString(idindex);
                    String path = cursor.getString(dataindex);
                    String size = cursor.getString(sizeindex);
                    String added = cursor.getString(addedindex);

                    dec = path.substring(path.length() - Define.CHS_SEff_TYPE_L, path.length());
                    System.out.println("BUG queryFiles path:" + path);
                    if (dec.equals(Define.CHS_SEff_MAC_TYPE)) {
                        int dot = path.lastIndexOf("/");
                        name = path.substring(dot + 1);
                        name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
                        Log.e("test@", name);
                        SEFF_File seff_File = new SEFF_File();
                        mDSP_SData = JsonRWUtil.LoadJsonLocal2DSP_DataInfo(mContext, path);
                        if (mDSP_SData != null) {
                            if (mDSP_SData != null) {
                                dataInfo = mDSP_SData.Get_data_info();
                                seff_File.Set_file_id("id?");
                                seff_File.Set_file_type(mDSP_SData.Get_fileType());
                                seff_File.Set_file_name(name);
                                seff_File.Set_file_path(path);
                                seff_File.Set_file_favorite("0");
                                seff_File.Set_file_love("0");
                                seff_File.Set_file_size(size);
                                seff_File.Set_file_time(added);
                                seff_File.Set_file_msg("msg?");

                                seff_File.Set_data_user_name(dataInfo.Get_data_user_name());
                                seff_File.Set_data_machine_type(dataInfo.Get_data_machine_type());
                                seff_File.Set_data_car_type(dataInfo.Get_data_car_type());
                                seff_File.Set_data_car_brand(dataInfo.Get_data_car_brand());
                                seff_File.Set_data_group_name(dataInfo.Get_data_group_name());
                                seff_File.Set_data_upload_time(dataInfo.Get_data_upload_time());
                                seff_File.Set_data_eff_briefing(dataInfo.Get_data_eff_briefing());

                                DataStruct.LocalSEffFile_List.add(seff_File);
                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    @SuppressWarnings("unused")
    public static void UpdateVerderOption(Context mContext) {
        //测试VerderOption版本
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                Operaton operaton=new Operaton();
                boolean result=operaton.UpdateVerderOptionVersion();
                System.out.println("BUG_NET onClick+operaton.UpdateVerderOptionVersion:"+result);
                //需要更新
                if(result){
                    System.out.println("BUG_NET VerderOptionFileIsExists exist?:"+VerderOptionFileIsExists());
                    if(!VerderOptionFileIsExists()){//最新版本文件不存在就开始下载
                        Operaton operatono=new Operaton();
                        boolean res=operatono.getNetWorkVenderOption(mContext,DataStruct.venderOption.Get_version_url());
                        if(res){//下载完成则更新数据库
                            DataStruct.dbCarBrands_Table.ResetTable();
                            DataStruct.dbCarTypes_Table.ResetTable();
                            DataStruct.dbMacTypes_Table.ResetTable();
                            DataStruct.dbMacsAgentName_Table.ResetTable();
                            //VerderOption解释
                            JsonRWUtil.parseLocalVerderOptionJson(mContext);
                        }
                    }else{//若文件存在，测试数据库是否空，空则重新更新
                        MacTypes macTypes = DataStruct.dbMacTypes_Table.find("id", "1");

                        if(macTypes==null){
                            System.out.println("BUG_NET Build CCM_DataBaseHelper！！！");
                            DataStruct.dbCarBrands_Table.ResetTable();
                            DataStruct.dbCarTypes_Table.ResetTable();
                            DataStruct.dbMacTypes_Table.ResetTable();
                            DataStruct.dbMacsAgentName_Table.ResetTable();
                            //VerderOption解释
                            JsonRWUtil.parseLocalVerderOptionJson(mContext);
                        }
                    }
                }
//				else{
//					MacTypes macTypes = ControlPCActivity.dbMacTypes_Table.find("id", "1");
//					//System.out.println("BUG_NET macTypes is exist?:"+macTypes);
//					if(macTypes==null){
////
//						ControlPCActivity.dbCarBrands_Table.ResetTable();
//						ControlPCActivity.dbCarTypes_Table.ResetTable();
//						ControlPCActivity.dbMacTypes_Table.ResetTable();
//						ControlPCActivity.dbMacsAgentName_Table.ResetTable();
//						//VerderOption解释
//		        		jsonRWOpt.parseVerderOptionJson(mContext);
//					}
//				}
            }
        }).start();
        */
    }

    public static boolean VerderOptionFileIsExists(Context mContext) {
        String appName = getAppInfo(mContext);
        String filePahtString = Environment.getExternalStorageDirectory() + "/" +
                MacCfg.AgentNAME + "/" + MacCfg.Mac + "/" +
                DataStruct.venderOption.Get_lastversion() + ".json";
        //System.out.println("BUG_NET filePahtString?:"+filePahtString);
        try {
            File f = new File(filePahtString);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {

            return false;
        }
        return true;
    }

    //加载音效文件
    public static boolean loadSEFFileDownload(Context mContext, String filePath) {
        System.out.println("BUG  loadSEFFileDownload fileType" + filePath);
        int fileType = JsonRWUtil.getSEFFFileType(mContext, filePath);
        //ToastMsg("GetFileType="+String.valueOf(fileType));
        if (fileType == 1) {
            return UpdateForJsonSingleData(filePath,
                    mContext);
        } else if (fileType == 2) {
            return loadMacEffJsonData(filePath, mContext);
        } else {
            ToastMsg(mContext.getResources().getString(R.string.LoadSEff_Fail), mContext);
            return false;
        }
    }

    //加载音效文件
    public static boolean addSEFFileList(Context mContext, String filePath) {
        System.out.println("BUG  loadSEFFileDownload fileType" + filePath);
        int fileType = JsonRWUtil.getSEFFFileType(mContext, filePath);

        //printfDSP_SData(mDSP_SData);
        if (fileType == 1) {
            DSP_SingleData mDSP_SData = new DSP_SingleData();
            mDSP_SData = JsonRWUtil.LoadJsonLocal2DataStruce(mContext, filePath);
            if (mDSP_SData == null) {
                return false;
            }
            //加到数据库存中
            DSP_DataInfo dataInfo = mDSP_SData.Get_data_info();
            SEFF_File seff_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
            int dot = filePath.lastIndexOf("/");
            String name = filePath.substring(dot + 1);
            //Log.e("test#",name);
            name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
            if (seff_file == null) {
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
            return true;
        } else if (fileType == 2) {
            DSP_MACData MAC_Data = new DSP_MACData();
            MAC_Data = JsonRWUtil.loadMacDataJson2DataStruce(mContext, filePath);
            if (MAC_Data == null) {
                return false;
            }
            //加到数据库存中
            DSP_DataInfo dataInfo = MAC_Data.Get_data_info();
            SEFF_File seff_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
            int dot = filePath.lastIndexOf("/");
            String name = filePath.substring(dot + 1);
            //Log.e("test#",name);
            name = name.substring(0, name.length() - Define.CHS_SEff_TYPE_L);
            if (seff_file == null) {
                DataStruct.dbSEfFile_Table.insert(new SEFF_File(
                        "file_id",//file_id
                        MAC_Data.Get_fileType(),//file_type
                        name,//file_name
                        filePath,//file_path
                        "0",//file_favorite
                        "0",//file_love
                        "200",//file_size
                        MAC_Data.Get_data_info().Get_data_upload_time(),//file_time
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
            return true;
        } else {
            ToastMsg(mContext.getResources().getString(R.string.LoadSEff_Fail), mContext);
            return false;
        }
    }

    //加载音效文件fileType 1：单机，2：整机
    public static boolean loadSEFFileCustom(Context mContext, int fileType) {
        System.out.println("BUG  loadSEFFileCustom fileType" + fileType);
        boolean haveFile;
        File cache = new File(Environment.getExternalStorageDirectory(), MacCfg.AgentNAME);
        File destDir = new File(cache.toString() +
                "/" + MacCfg.Mac +
                "/" + "SoundEff"
        );
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        String filePath;
        if (fileType == 1) {
            filePath = destDir.toString() +
                    "/" + Define.ShareDefaultName + Define.CHS_SEff_TYPE;
            haveFile = SEFFileDownloadExists(mContext, filePath);
        } else if (fileType == 2) {
            filePath = destDir.toString() +
                    "/" + Define.ShareDefaultGruopName + Define.CHS_SEff_MAC_TYPE;
            haveFile = SEFFileDownloadExists(mContext, filePath);
        } else {
            return false;
        }
        System.out.println("BUG  loadSEFFileCustom filePath:" + filePath);
        if (!haveFile) {
            System.out.println("BUG  loadSEFFileCustom is not exist!!  ");
            return false;
        }


        System.out.println("BUG  loadSEFFileDownload:" + filePath);
        return loadSEFFileDownload(mContext, filePath);
    }

    public static boolean SEFFileDownloadExists(Context mContext, String filePahtString) {
        try {
            File f = new File(filePahtString);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static int getOutVal(int ch, Boolean SmallToBig, int Dfrom) {
        if (ch <= 5) {
            if (SmallToBig) {
                return Dfrom > 25 ? (500 + (Dfrom - 25) * 10) : Dfrom * 20;
            } else {
                return Dfrom > 500 ? (25 + (Dfrom - 500) / 10) : Dfrom / 20;
            }
        } else {
            if (SmallToBig) {
                return Dfrom * 40;
            } else {
                return Dfrom / 40;
            }
        }
    }

    //连接状态
    public static boolean getConnectState() {
        System.out.println("BUG  getConnectState:" + DataStruct.isConnecting);
        return DataStruct.isConnecting;
    }

    public static boolean isConnected() {
        if ((DataStruct.isConnecting) && (DataStruct.U0SynDataSucessFlg)) {
            return true;
        }
        return false;
    }

    //列表状态
    public static boolean isListNull() {
        if (DataStruct.SendbufferList == null) {
            return true;
        }
        if (DataStruct.SendbufferList.size() <= 0) {
            return true;
        }
        if (DataStruct.SendbufferList.isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isDataNull() {
        if ((DataStruct.SendDeviceData == null) || (DataStruct.RcvDeviceData == null)) {
            return true;
        }
        return false;
    }

    public static boolean getSoundStatus(Context mContext) {
        int statues = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.SOUND_EFFECTS_ENABLED, 0);
        if (statues != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveBluetoothInfo(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (MacCfg.BT_CUR_ConnectedID != null) {
            editor.putString("BT_CUR_ConnectedID", MacCfg.BT_CUR_ConnectedID);
        }
        if (MacCfg.BT_CUR_ConnectedName != null) {
            editor.putString("BT_CUR_ConnectedName", MacCfg.BT_CUR_ConnectedName);
        }
        DataStruct.ComType = DataStruct.comDSP + DataStruct.comPlay;
        editor.putInt("ComType", DataStruct.ComType);
        editor.commit();

        System.out.println("BUG >>>saveBluetoothInfo "
                + ",BT_CUR_ConnectedName=" + MacCfg.BT_CUR_ConnectedName + "\n"
                + ",BT_CUR_ConnectedID=" + MacCfg.BT_CUR_ConnectedID + "\n"
        );
    }

    public static void saveHEAD_DATA(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("HEAD_DATA", MacCfg.HEAD_DATA);
        DataStruct.ComType = DataStruct.comDSP + DataStruct.comPlay;
        editor.putInt("ComType", DataStruct.ComType);
        editor.commit();

        System.out.println("BUG >>>saveBluetoothInfo MacCfg.HEAD_DATA++++++++=" + Integer.toHexString(MacCfg.HEAD_DATA) + "\n"
        );
    }

    public static BluetoothDevice getBluetoothConnectInfo(Context mContext) {

        if (MacCfg.LCBT.size() > 0) {
            SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
            String BT_CUR_ConnectedID = sp.getString("BT_CUR_ConnectedID", "null");
            String BT_CUR_ConnectedName = sp.getString("BT_CUR_ConnectedName", "null");
            Log.e("##BUG getSharedPf->", "BT_CUR_ConnectedID:" +
                    BT_CUR_ConnectedID + ",BT_CUR_ConnectedName:" + BT_CUR_ConnectedName + "\n");
            for (int i = 0; i < MacCfg.LCBT.size(); i++) {
                Log.e("##BUG getSharedPf->", "Name:" +
                        MacCfg.LCBT.get(i).getName() + ",ID:" + MacCfg.LCBT.get(i).getAddress() + "\n");

                if ((MacCfg.LCBT.get(i).getName().equals(BT_CUR_ConnectedName))
                        && (MacCfg.LCBT.get(i).getAddress().equals(BT_CUR_ConnectedID))) {

                    MacCfg.BT_CUR_ConnectedName = BT_CUR_ConnectedName;
                    MacCfg.BT_CUR_ConnectedID = BT_CUR_ConnectedID;
                    return MacCfg.LCBT.get(i);
                }
            }
        }
        MacCfg.BT_CUR_ConnectedName = null;
        MacCfg.BT_CUR_ConnectedID = null;
        return null;
    }

    public static void setAutoConnect(Context mContext, boolean statues) {
        SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("AutoConnect", statues);
        editor.commit();
    }

    public static boolean getAutoConnect(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("SP", MODE_PRIVATE);
        return sp.getBoolean("AutoConnect", true);
    }

    public static int getMacChannelTotal() {

        if (MacCfg.HEAD_DATA == Define.AgentHead_CHS) {
            return 12;
        }

        if ((MacCfg.DeviceVerString.contains(""))
                || (MacCfg.DeviceVerString.contains("MPYJ-GV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-FV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-JV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-KV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-LV1."))
                || (MacCfg.DeviceVerString.contains("MPAP-BV1."))
                || (MacCfg.DeviceVerString.contains("MPHL-GV1."))
                || (MacCfg.DeviceVerString.contains("MAJH-AV1."))
                || (MacCfg.DeviceVerString.contains("MCRS-EV1."))
                || (MacCfg.DeviceVerString.contains("MPKL-DV1."))
                || (MacCfg.DeviceVerString.contains("MPFL-CV1."))
                || (MacCfg.DeviceVerString.contains("MZFL-LV1."))
                || (MacCfg.DeviceVerString.contains("MPFL-EV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-PV1."))
                || (MacCfg.DeviceVerString.contains("MYYJ-CV1."))
                || (MacCfg.DeviceVerString.contains("MLYL-CV2."))
        ) {
            return 6;
        } else if ((MacCfg.DeviceVerString.contains("MDKP-NV1."))
                || (MacCfg.DeviceVerString.contains("MPKP-MV1."))
                || (MacCfg.DeviceVerString.contains("MYDW-AV1."))
                || (MacCfg.DeviceVerString.contains("MDHL-DV1."))
                || (MacCfg.DeviceVerString.contains("MDHL-FV1."))
                || (MacCfg.DeviceVerString.contains("MCHS-CV1."))
                || (MacCfg.DeviceVerString.contains("MCRS-DV1."))
                || (MacCfg.DeviceVerString.contains("MLYL-GV1."))
        ) {
            return 8;
        } else if ((MacCfg.DeviceVerString.contains(""))
                || (MacCfg.DeviceVerString.contains("MPKP-EV1."))
                || (MacCfg.DeviceVerString.contains("MPFL-DV1."))
                || (MacCfg.DeviceVerString.contains("MLYL-EV2."))
        ) {
            return 10;
        } else if ((MacCfg.DeviceVerString.contains(""))
                || (MacCfg.DeviceVerString.contains("MPKP-FV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-EV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-HV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-MV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-NV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-OV1."))
                || (MacCfg.DeviceVerString.contains("MPAP-AV1."))
                || (MacCfg.DeviceVerString.contains("HL812-V1."))
                || (MacCfg.DeviceVerString.contains("MDHL-EV1."))
                || (MacCfg.DeviceVerString.contains("MPYJ-EV1."))
                || (MacCfg.DeviceVerString.contains("MLYL-FV1."))
        ) {
            return 12;
        } else {
            return 6;
        }

    }


    public static boolean isComBluetoothType() {
        if ((MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_USB_HOST)
                || (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI)
                || (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART)
        ) {
            return false;
        }
        return true;
    }

}
