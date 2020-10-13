package com.chs.mt.pxe_r500.datastruct;

import android.database.sqlite.SQLiteDatabase;

import com.chs.mt.pxe_r500.MusicBox.MData;
import com.chs.mt.pxe_r500.MusicBox.MFListInfo;
import com.chs.mt.pxe_r500.MusicBox.MInfo;
import com.chs.mt.pxe_r500.MusicBox.MMListInfo;
import com.chs.mt.pxe_r500.bean.DSP_AI;
import com.chs.mt.pxe_r500.bean.DSP_DataMac;
import com.chs.mt.pxe_r500.bean.DSP_EFFData;
import com.chs.mt.pxe_r500.bean.DSP_MACData;
import com.chs.mt.pxe_r500.bean.DSP_MusicData;
import com.chs.mt.pxe_r500.bean.DSP_OutputData;
import com.chs.mt.pxe_r500.bean.SEFF_File;
import com.chs.mt.pxe_r500.bean.User;
import com.chs.mt.pxe_r500.bean.VenderOption;
import com.chs.mt.pxe_r500.bean.ImageUrlList;
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
import com.chs.mt.pxe_r500.mac.bean.MacMode;
import com.chs.mt.pxe_r500.mac.bean.MacModeArt;
import com.chs.mt.pxe_r500.operation.JsonRWUtil;

import java.util.ArrayList;
import java.util.List;

public class DataStruct {
    public   static   final   int  	U0DataLen = 800;	//数据包长度
    public   static   final   int   CMD_LENGHT = 16;

    public   static final int WRITE_CMD = 0xa1;
    public   static final int READ_CMD  = 0xa2;

    public   static final int FRAME_STA = 0xee;
    public   static final int FRAME_END = 0xaa;

    public   static final int RIGHT_ACK = 0x51;
    public   static final int ERROR_ACK = 0x52;
    public   static final int DATA_ACK  = 0x53;

    public static final int NO = 0;  // 定义静态变量
    public static final int YES = 1; // 定义静态变量
	//------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------系统类型中数据变量-------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------	
	public static Data RcvDeviceData     = new Data();
	public static Data SendDeviceData    = new Data();
	public static Data DefaultDeviceData = new Data();
	public static Data BufDeviceData     = new Data();
    public static int  SysMainValBuf = 0;
    public static MacMode MacModeAll = new MacMode();//所有支持的机型
    public static MacModeArt CurMacMode = new MacModeArt();//当前机型
    public static DSP_AI mDSPAi = new DSP_AI();
	public static User user = new User ();
	public static User userSeffTemp = new User ();
	public static VenderOption venderOption = new VenderOption ();
	public static ImageUrlList HomeImageUrlList = new ImageUrlList ();

    public static DSP_MusicData mDSP_MusicData = new DSP_MusicData();
    public static DSP_EFFData mDSP_EFFData = new DSP_EFFData();
    public static DSP_OutputData mDSP_OutputData = new DSP_OutputData();
    public static DSP_DataMac mDSP_DataMac = new DSP_DataMac();
    public static DSP_MACData MAC_DataBuf = new DSP_MACData();

    public static int U0RcvFrameFlg = NO; // 有新接收到数据的标志
    public static boolean BOOL_CheckHEADOK = false;
    public static boolean BOOL_CheckHEAD_ST = false;
    public static boolean BOOL_GroupREC_ACK = false;
    public static boolean BOOL_ShowCheckHeadDialog = false;
    //public int U0BusyFlg = NO; // 有数据要发送的标志
    public static int U0SendFrameFlg = NO; // 有数据要发送的标志
    public static boolean U0SynDataSucessFlg = false; // 同步初始化数据完成标志
    public static boolean U0SynDataError = false; // 同步初始化数据是否出错
    public static int PcConnectFlg = NO;
    public static int PcConnectCnt = 0;
    public static int ComType = Define.COMMUNICATION_WITH_BLUETOOTH_SPP;

    public static int U0HeadFlg = NO;
    public static int U0HeadCnt = 0;
    public static int U0DataCnt = 0;

    public static int comType   = 0;
    public static int comDSP    = 0;
    public static int comPlay   = 0;

    public static int BOOLDSPHeadFlg = 0;//0：未知是否可用，1：明确不可用，2:明确可用
    public static int BOOLPlayHeadFlg = 0;//0：未知是否可用，1：明确不可用，2:明确可用
    public static boolean BOOLHaveMasterVol = false;//false：未知获取主音量，true:已获取主音量

    public static boolean MasterConType  = false;//false:Input，True:System

    public static int[][] GainBuf=new int[Define.MAX_CH][31] ;
    public static int[][] GainEFFBuf=new int[Define.MAX_CH][31] ;


    public static int[] initDataBuf = new int[U0DataLen];
    /*用于结构与数组之间这转换*/
    public static int[] ChannelBuf = new int[DataStruct.U0DataLen + DataStruct.CMD_LENGHT];
    /*消息发送队列*/
    public static ArrayList<byte[]> SendbufferList = new ArrayList<byte[]>();
    public static ArrayList<byte[]> MSendbufferList = new ArrayList<byte[]>();
    /*消息发送buf*/
//    public static byte[] FrameDataBuf = new byte[DataStruct.U0DataLen + DataStruct.CMD_LENGHT];
//    public static byte FrameDataSUM = 0;

    public static SQLiteDatabase db=null,db_CCM=null,db_Music;
    public static DataBaseOpenHelper DataBaseHelper=null;
    public static DB_SEffData_Table dbSEffData_Table=null;
    public static DB_SEffFile_Table dbSEfFile_Table=null;
    public static DB_SEffFile_Recently_Table dbSEfFile_Recently_Table=null;
    public static DB_LoginSM_Table dbLoginSM_Table=null;

    public static DataBaseCCMHelper CCM_DataBaseHelper=null;
    public static CarBrands_Table dbCarBrands_Table=null;
    public static CarTypes_Table dbCarTypes_Table=null;
    public static MacTypes_Table dbMacTypes_Table=null;
    public static MacsAgentName_Table dbMacsAgentName_Table=null;

    public static DataBaseMusicHelper DBMusicHelper = null;
    public static DB_MFileList_Table dbMFileList_Table = null;
    public static DB_MMusicList_Table dbMMusicList_Table = null;
    //public static List<String> LocalSEfflstFile =new ArrayList<String>(); //本机的音效文件列表
    public static List<SEFF_File> LocalSEffFile_List = new ArrayList<SEFF_File>();
    public static boolean boolUpdateLocalFile=false;
    public static JsonRWUtil jsonRWOpt = null;
    public static String fileNameString = "";
    public static boolean bool_HaveSEFFUpdate = false;//false:没有数据更新，ture:有数据更新。
    public static String LOAD_SEFF_FROM_OTHER_PathName = null;//false:没有数据更新，ture:有数据更新。

    //MusicBox
    public static MData RcvMBoxData      = new MData();
    public static MData SendMBoxData     = new MData();
    public static byte[] FDMBoxBuf = new byte[MData.MDataMax];
    public static List<MMListInfo> MusicList = new ArrayList<MMListInfo>();
    public static List<MFListInfo> FileList = new ArrayList<MFListInfo>();
    public static MInfo CurMusic = new MInfo();
    //屏窗口参数
    public	static 	int	 Screen_Dip = 0;
    public  static 	int	 ScreenWidth = 0;
    public  static 	int	 ScreenHeight = 0;
    public	static 	int	 FifterPopWindowHeight = 0;
    public	static 	int	 OctPopWindowHeight = 0;
    public  static 	int	 ScreenItemHeight = 0;

    public  static boolean isConnecting = false;
    public  static boolean isConnectingOld = false;
    public  static boolean bool_OldConnectStatus = false;
    public  static boolean ManualConnecting = false;
    public  static boolean CanConnecting = false;
//    public  static boolean TryConnecting = false;
    public  static boolean BOOL_GetBluetoothList = false;
    public  static int SEFF_USER_GROUP_OPT = 0;//1:调用，2:保存
    public  static int BOOL_MFList_OPT = 0;//1:Music，2:File
    public static   boolean B_InitLoad=false;


    public static String DeviceVerString = null;     // 设备版本信息
    public static boolean DeviceVerErrorFlg = false;

    public  static 	int	 MAX_Name_Size = 13;

    public static boolean bool_ShareOrSaveMacSEFF = false;
    public static String SSM_Detials="";

}
