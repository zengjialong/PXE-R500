package com.chs.mt.pxe_r500.mac;

import android.content.Context;

import com.chs.mt.pxe_r500.R;
import com.chs.mt.pxe_r500.datastruct.DataStruct;
import com.chs.mt.pxe_r500.datastruct.Define;
import com.chs.mt.pxe_r500.datastruct.MacCfg;
import com.chs.mt.pxe_r500.mac.MacModeInitData.Mac_H812;
import com.chs.mt.pxe_r500.operation.DataOptUtil;

/**
 * Created by Administrator on 2017/7/11.
 */

public class MacModeInit {


    static void initMacModeOf_H812(Context mContext) {
        DataStruct.MacModeAll.HEAD_DATA[Define.MAC_TYPE_H812] = 0x7C;           //包头
        DataStruct.MacModeAll.MacMode[Define.MAC_TYPE_H812] = Define.MAC_TYPE_H812;      //机型编号
        DataStruct.MacModeAll.MacType[Define.MAC_TYPE_H812] = MacCfg.Mac;           //机型，用于音效文件识别
        DataStruct.MacModeAll.MacTypeDisplay[Define.MAC_TYPE_H812] = MacCfg.Mac;    //机型，用于显示
        DataStruct.MacModeAll.MCU_Versions[Define.MAC_TYPE_H812] = MacCfg.MCU_Versions;//下位机版本号

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].CurMacMode = Define.MAC_TYPE_H812;   //机型编号
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].MacType = MacCfg.Mac;           //机型，用于音效文件识别
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].MacTypeDisplay = MacCfg.Mac;           //机型，用于显示
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].MCU_Versions = MacCfg.MCU_Versions;//下位机版本号
        /*****************************主功能***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_SYSTEM_SPK_TYPEB = false;//是否使用SYSTEM_SPK_TYPEB
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_RESET_GROUP_DATA = false;//是否使用数据复位标志，当数据出错时提示用户是否恢复出厂设置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_TRANSMITTAL = false;//是否使用數據傳輸標誌
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_USE_INS = false;/*输入功能*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_ENCRYPTION = false;/*加密功能*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_HIDEMODE = false;/*高频二分频*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_MIXER = false;/*混音设置  false:无*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_INPUT_SOURCE = true;/*音源输入*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_INPUT_SOURCE_VOL = false;/*音源音量*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_MIXER_SOURCE = false;/*混音音源输入*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_MIXER_SOURCE_VOL = false;/*混音音源音量*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_SUB_VOL = false;/*低音音量*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].BOOL_SET_SPK_TYPE = false;/*通道类型设置*/

    /*通道联调
     Define.LINKMODE_FRS           1.前声场，后声场，超低的联调，单独分开
     Define.LINKMODE_FRS_A         2.前声场，后声场，超低的联调，一起联调
     LINKMODE_FR            3.前声场，后声场，单独分开
     Define.LINKMODE_FR_A          4.前声场，后声场，中置超低的联调，一起联调
     Define.LINKMODE_SPKTYPE       5.设置通道输出类型后的联调
     Define.LINKMODE_SPKTYPE_S     6.设置通道输出类型后的联调，可联机保存
     Define.LINKMODE_AUTO          7.任意联调，每个通道可以单独联调，可联机保存
     Define.LINKMODE_LEFTRIGHT     8.固定两两通道联调
     */
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].LinkMOde = Define.LINKMODE_LEFTRIGHT;

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].MAX_USE_GROUP = 6;//用户组数据，一般固定为6
        /*****************************主音量***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Master.MAX = 35;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Master.MuteChangeWVol = true;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Master.DATA_TRANSFER = Define.COM_TYPE_SYSTEM;
        /*****************************延时***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Delay.MAX = 384;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Delay.DATA_TRANSFER = Define.COM_TYPE_OUTPUT;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Delay.Type = Define.DELAY_SPKTYPE;
        /*****************************Output***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX = 6;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX_USE = 6;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.MaxOutVol = 660;//最大值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.StepOutVol = 10;//步进
        //通道类型
//        for (int i = 0; i < 16; i++) {
//            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[i] = 0;
//        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[0] = 6;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[1] = 12;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[2] = 15;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[3] = 18;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[4] = 22;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[5] = 23;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[6] = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[7] = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[8] = 13;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[9] = 16;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[10] = 21;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.SPK_TYPE[11] = 24;


        //通道名字
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[i] = "CH"+"-" + String.valueOf(i + 1);
        }

//        //通道名字
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[0] =
//                mContext.getString(R.string.CH1);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[1] =
//                mContext.getString(R.string.CH2);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[2] =
//                mContext.getString(R.string.CH3);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[3] =
//                mContext.getString(R.string.CH4);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[4] =
//                mContext.getString(R.string.CH5);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[5] =
//                mContext.getString(R.string.CH6);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[6] =
//                mContext.getString(R.string.CH7);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[7] =
//                mContext.getString(R.string.CH8);

//        for (int i = 8; i < 16; i++) {
//            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.Name[i] =
//                    mContext.getString(R.string.NULL);
//        }


        /*****************************滤波器 一般固定不用修改***********************************/
        //滤波器类型
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.BOOL_Hide_6DB_Fiter = false;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.max1 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.memberName1[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.memberName1[1] = mContext.getString(R.string.FilterBW);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.memberName1[2] = mContext.getString(R.string.FilterB);
        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.max2 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.memberName2[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.memberName2[1] = mContext.getString(R.string.FilterBW);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Fiter.memberName2[2] = mContext.getString(R.string.FilterB);

        //滤波器斜率
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.max1 = 9;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.member1[i] = i;
        }
       DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[0] = mContext.getString(R.string.Otc6dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[1] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[2] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[3] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[4] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[5] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[6] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[7] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName1[8] = mContext.getString(R.string.OtcOFF);
        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.max2 = 9;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[0] = mContext.getString(R.string.Otc6dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[1] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[2] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[3] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[4] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[5] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[6] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[7] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].XOver.Level.memberName2[8] = mContext.getString(R.string.OtcOFF);

        /*****************************EQ***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.group = 1;//有多少组
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.Max_EQ = 31;//使用的EQ,31,10
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.GPEQ_Mode = true;//图示均衡模式
        //EQ增益范围
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.EQ_LEVEL_MAX = 720;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.EQ_LEVEL_MIN = 480;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.EQ_LEVEL_ZERO = 600;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.EQ_Gain_MAX = 240;

        //组一
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.EQ_Max1 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX;
        //组二
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.EQ_Max2 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].EQ.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Out.OUT_CH_MAX;
        /****************************混音************************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.MIXER_CH_MAX = 16;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.MIXER_CH_MAX_USE = 8;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Max_Mixer_Vol = 100;//最大值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.BOOL_Polar = false;//有无相反相
        //通道名字
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[0] =
                mContext.getString(R.string.IN1);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[1] =
                mContext.getString(R.string.IN2);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[2] =
                mContext.getString(R.string.IN3);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[3] =
                mContext.getString(R.string.IN4);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[4] =
//                mContext.getString(R.string.Mixer_HI_5);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[5] =
//                mContext.getString(R.string.Mixer_HI_6);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[6] =
//                mContext.getString(R.string.Mixer_HI_7);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[7] =
//                mContext.getString(R.string.Mixer_HI_8);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[4] =
                mContext.getString(R.string.Mixer_AUX_1);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[5] =
                mContext.getString(R.string.Mixer_AUX_2);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[6] =
                mContext.getString(R.string.Mixer_IN15);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[7] =
                mContext.getString(R.string.Mixer_IN16);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[12] =
//                mContext.getString(R.string.Mixer_IN13);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[13] =
//                mContext.getString(R.string.Mixer_IN14);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[14] =
//                mContext.getString(R.string.Mixer_IN15);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[15] =
//                mContext.getString(R.string.Mixer_IN16);




        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[0] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_high);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[1] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_high);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[2] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_high);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[3] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_high);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[4] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_aux);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[5] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_aux);

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[6] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_bluetooth);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[7] =
                mContext.getResources().getDrawable(R.drawable.chs_input_source_bluetooth);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[8] =
//                mContext.getResources().getDrawable(R.drawable.chs_input_source_bluetooth);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.img_Name[9] =
//                mContext.getResources().getDrawable(R.drawable.chs_input_source_bluetooth);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[6] =
//                mContext.getString(R.string.Mixer_HI_7);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixer.Name[7] =
//                mContext.getString(R.string.Mixer_HI_8);


        /****************************音源************************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.MaxVol = 40;//音源音量
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Max = 3;//音源个数

        //音源名字
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Name[0] = mContext.getString(R.string.Hi_Level);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Name[1] = mContext.getString(R.string.AUX);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Name[2] = mContext.getString(R.string.Bluetooth);
       // DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Name[3] = mContext.getString(R.string.OFF);
        //    DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Name[4] = mContext.getString(R.string.Optical);
        //    DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.Name[5] = mContext.getString(R.string.Coaxial);

        //与名字对应的音源值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.inputsource[0] = 1;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.inputsource[1] = 3;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.inputsource[2] = 2;
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.inputsource[3] = 2;
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.inputsource[4] = 5;
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].inputsource.inputsource[5] = 6;
        /****************************混音音源************************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.MaxVol = 60;//音源音量
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Max = 4;//音源个数

        //混音音源名字
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[0] = mContext.getString(R.string.Hi_Level);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[1] = mContext.getString(R.string.AUX);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[2] = mContext.getString(R.string.Bluetooth);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[3] = mContext.getString(R.string.OFF);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[4] = mContext.getString(R.string.Optical);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[5] = mContext.getString(R.string.Coaxial);
        //DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.Name[4] = mContext.getString(R.string.OFF);

        //与名字对应的混音音源值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[0] = 1;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[1] = 3;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[2] = 2;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[3] = 255;
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[4] = 6;
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[5] = 6;
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].Mixersource.inputsource[6] = 7;
        /*****************************输入***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX = 11;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX_USE = 11;//最大通道
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.Name[i] = String.valueOf(i);
        }

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX_EQ = 4;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX_EQ_USE = 4;//最大通道

        //滤波器类型
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.BOOL_Hide_6DB_Fiter = false;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.max1 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.memberName1[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.memberName1[1] = mContext.getString(R.string.FilterB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.memberName1[2] = mContext.getString(R.string.FilterBW);
        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.max2 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.memberName2[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.memberName2[1] = mContext.getString(R.string.FilterB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Fiter.memberName2[2] = mContext.getString(R.string.FilterBW);

        //滤波器斜率
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.max1 = 8;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[0] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[1] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[2] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[3] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[4] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[5] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[6] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName1[7] = mContext.getString(R.string.OtcOFF);

        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.max2 = 8;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[0] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[1] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[2] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[3] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[4] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[5] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[6] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.XOver.Level.memberName2[7] = mContext.getString(R.string.OtcOFF);

        //EQ配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.group = 1;//有多少组
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.Max_EQ = 31;//使用的EQ,31,10
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.GPEQ_Mode = true;//图示均衡模式
        //EQ增益范围
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.EQ_LEVEL_MAX = 800;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.EQ_LEVEL_MIN = 400;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.EQ_LEVEL_ZERO = 600;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.EQ_Gain_MAX = 400;

        //组一
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.EQ_Max1 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX;
        //组二
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.EQ_Max2 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.EQ.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_H812].INS.INS_CH_MAX;
    }


    public static void initCurMacModeArt(Context mContext) {
        //TODO
        MacCfg.bool_Encryption = false;//切换机型时把加密标志，联调标志清为默认


                DataStruct.CurMacMode = DataStruct.MacModeAll.MacModeArt[MacCfg.Define_MAC];

    }

    public static void initMacModeDefaultData(Context mContext) {
        initCurMacModeArt(mContext);

            DataOptUtil.FillRecDataStruct(Define.EFF, 0, Mac_H812.H812_EFF_init_data, false);
            DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, Mac_H812.H812_inputs_init_data, false);
            DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, Mac_H812.H812_Input_init_data, false);

            for (int i = 0; i <DataStruct.CurMacMode.Out.OUT_CH_MAX_USE ; i++) {
                DataOptUtil.FillRecDataStruct(Define.OUTPUT, i, Mac_H812.H812_Output1_init_data, false);
            }

        DataOptUtil.InitDataStruct(mContext);
    }


    public static void initMacModeAllArt(Context mContext) {
        initMacModeOf_H812(mContext);//混音
        DataStruct.MacModeAll.MacModeMax = MacCfg.Define_MAC_MAX;
        initMacModeDefaultData(mContext);
    }


}
