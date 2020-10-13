package com.chs.mt.pxe_r500.datastruct;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class MacCfg {
	/**
	 */
	public static  int HEAD_DATA = 0x7C;	            // 包头  0x7C
	public static  String MCU_Versions="MPAP-FV1.0";//下位机版本号 MPAP-FV1.0
	public static  String BRAND = "AP";
	public static  final String App_versions="APAP-FV1.00 (beta)";//定义本软件版本
	public static  final String Copyright="Copyright ©  ";
	public static  final  String CopyrightAll=" ALPS ALPINE CO., LTD.";//定义本版权";
	public static  final String welcome_logo=App_versions;
	public static  final String Mac="PXE-R500";

	public static  final String Mac_type="PXE-R500";
	public static int delay_off_time=255;
	public static  final String AgentNAME = BRAND;  //译宝
	public static  final String Json_versions="CHS-JSON_V1.00";//定义本软件版本
	public static  final String Json_versions_V0_00="CHS-JSON_V0.00";//定义本软件版本 
	public static  final String Json_MacCfgVersions="CHS-JSONMacCfg_V0.00";//定义本软件版本 
	
	public static  final String AgentID        = Define.AgentID_AP;
	public static  final int    Agent_ID       = 2;


	public static int main_vol=0;

	public static int Enter_Advance=0;
	public static  int Mic_Mute=5;
	public static int Out_Mute=2;
	public static        String PhoneMAC = "";
    public static        String PhoneOS = "";
	public static        String PhoneOS_Mode = "";
    public static        String PhoneName = "";
	public static  final   int  Define_MAC_MAX = 10;
	public static          int  Define_MAC = Define.MAC_TYPE_H812;
	//联机通信方式
	public static       boolean BOOL_ANYCON = false;
	public static         int COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO;
	public static         Boolean BOOL_USE_MusicBox=false;
	//USB Host
	public static boolean USBConnected=false;
	//变量
	public static boolean BOOL_ConnectAny=false;//是否可以连接多机型

    public static  int HEAD_DATA_Index = 0;
    public static boolean bool_Encryption=false;//加密标志
	public static byte Encryption_PasswordBuf[]={0,0,0,0,0,0};//密码保存
	public static boolean bool_HaveSEFFUpdate = false;//false:没有数据更新，ture:有数据更新。
	public static String LOAD_SEFF_FROM_OTHER_PathName = null;//false:没有数据更新，ture:有数据更新。

	public static int  Delay_Type = 0;//用作判断是延时声场联调

	/**
	 * 前声场联调组
	 * */
	public static int[]  F_Left_Sound = new int[]{1,2,3,5,6};//前左动态联调组
	public static int[]  F_Right_Sound = new int[]{7,8,9,11,12};//前右动态联调组
	public static int[]  F_Center_Sound = new int[]{19,20};//前中置动态联调组

	/**
	 * 后声场联调组
	 * */
	public static int[]  B_Left_Sound = new int[]{13,14,15,25,30};//后左动态联调组
	public static int[]  B_Right_Sound = new int[]{16,17,18,26,31};//后右动态联调组
	public static int[]  B_Center_Sound = new int[]{21,29};//后中置动态联调组
	public static int[]  S_Center_Sound = new int[]{22,23,};//超低动态联调组(左、右超低)
	public static int[]  Sub_Center_Sound = new int[]{24};//超低动态联调组(超低)



	/**
	 * 全声场联调组   前声场和后声场的对应就是全声场联调组
	 * */
//	public static int[]  A_Left_Sound_ = new int[]{13,14,15,25,30};//前左对应后左动态联调组
//	public static int[]  A_Right_Sound_ = new int[]{16,17,18,26,31};//后右动态联调组
//	public static int[]  A_Center_Sound_ = new int[]{21,29};//后中置动态联调组


	public static final float[] SCar = {(float) 1.5, (float) 1.35};
	public static final float[] MCar = {(float) 1.6, (float) 1.58};
	public static final float[] LCar = {(float) 1.75,(float) 1.75};
	public static       float[] CCar = SCar;

	public  static  boolean bool_OutChLink = false;
	public  static  boolean bool_AllTwoLinkCopyType=false;//false:left to right,true:right to left
	public  static  boolean bool_AllTwoLinkStatus = false;
    //动态联调
    public  static int[] DelayVal = new int[16];
    public  static int[] OutputVal = new int[16];

    /* 按键长按时间  */
	public static  int LongClickEventTimeMax = 100;
	/* 设置输出通道联调标志  */
    public  static int ChannelConOPT=0;
	public  static int ChannelConFLR=0;
	public  static int ChannelConRLR=0;
	public  static int ChannelConSLR=0;
	public  static int ChannelConCS=0;
	public  static int LinkChannleBase=0;
	public  static int ChannelConClick=0;
	public  static int MaxOupputNameLinkGroup=16;
	public  static  int ChannelLinkCnt = 0;
	public  static  int ChannelLinkBuf[][]=new int [16][2];
    public  static  int[] ChannelNumList = new int[26];
	public  static  int ChannelNumBuf[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public  static  boolean bool_OutChLeftRight=true;//true:从左复制到右，false:从右复制到左
	public  static  boolean bool_OutChLock = false;
	public  static  boolean bool_LinkFlag = false;//联调标志

	//-------------------------音源输入通道定义------------------------
	public  static  int Output_gain[]=new int[8];
	public  static  int Output_Lfreq[]=new int[8];
	public  static  int Output_Hfreq[]=new int[8];
	public static int[][] Output_level=new int[8][Define.MAX_CHEQ];
	//TOING SUB
	public  static final int ToningBW =295-72;

	public 	static     int  input_sourcetemp = 3; //信号灯包接收到的音源
    public 	static     int  main_Vol_Change= 3; //主音量
	public 	static     int  input_sourceAuxliary= 3; //信号灯包接收到的音源
	public 	static     int  mute_Vol_Change= 0; //主音量

	public 	static     int  CurProID = 0; //当前程序ID

	public  static  String BT_CUR_ConnectedName="DSP";//匹配的蓝牙名称
	public  static  String BT_CUR_ConnectedID = "NULL";//匹配的蓝牙名称
    public  static  String BT_OLD_ConnectedName="DSP HD";//匹配的蓝牙名称
	public  static  String BT_OLD_ConnectedID="DSP HD";//匹配的蓝牙名称
	public  static  String BT_ConnectedID = "NULL";//匹配的蓝牙名称
	public  static  String BT_ConnectedName = "NULL";//匹配的蓝牙名称
	public  static  String BT_GetName = "NULL";//匹配的蓝牙名称
	public  static  String BT_GetID = "NULL";//匹配的蓝牙名称

	public 	static  int  BluetoothDeviceID = Define.BT_Android_Type; //当前蓝牙ID
    //已经连接的蓝牙列表
	public  static  ArrayList<BluetoothDevice> LCBT = new ArrayList<BluetoothDevice>();
	public  static  boolean CHS_BT_CONNECTED = false; //已经连接上DSH HD的蓝牙设备
    public  static  boolean BTManualConnect = false;//是否手动关闭
	public  static     int  UI_Type			    = 0;

	public  static  boolean bool_ReadMacGroup = false; 
	public  static  int OutputChannelSel = 0;
	public  static  int inputChannelSel = 0;
	public  static  int EQ_Num = 0;
	public  static  int EQ_EFF_Num = 0;
	public  static  final int SPK_MAX = 28;

	public  static  final String DEVICE_NAME = "device_name";
	public  static  final String TOAST = "toast";
	public  static String DeviceVerString = "";     // 设备版本信息
	public  static  int CurPage = 0;
	public  static  int CurPage_bottom = 0;
	public  static  int CurFragmentPage = 0;

    public  static  int LedPackageCnt = 0;
	public  static  boolean UpdataAduanceData = false;
	public  static  int delayms = 200;
	public  static boolean BOOL_DialogHideBG=true;
	public  static  boolean BOOL_LoadSeffMute = true;
	public  static  boolean BOOL_OPTClose = false;
	public  static  boolean BOOL_OPTOpen = false;

	public  static  boolean BOOL_SoundStatues = false;
	public  static int cntDSP=0;
	public  static boolean BOOL_FlashMusicList=true;
	public  static boolean BOOL_FirstStart=false;
	public  static boolean BOOL_CanLinkUART=false;// 意外断线可以自动连接
	public  static boolean BOOL_SyncSystemVol=false;//是否同步系統音量

	public  static int MaxOutputCH = 8;

	public  static int OutputChannelCurrent = 8;


	public static int[] H_output_Freq_temp=new int[16];  //用做输出的频率开关
	public static int[] L_output_Freq_temp=new int[16];//用做输出的频率开关

}
