package com.chs.mt.pxe_r500.datastruct;

import com.chs.mt.pxe_r500.bean.DSP_MACData;
import com.chs.mt.pxe_r500.bean.DSP_SingleData;

public class Data {
    //------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------  通讯数据结构定义   一帧的数据定义 ----------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------




    public     int      INC1;					//
    public	   int      INC2;					//
    public	   int      INC3;					//
    public	   int   	  FrameStar;			//
    public	   int   	  FrameType;			//
    public	   int   	  DeviceID;			    //
    public	   int   	  UserID;				//
    public	   int   	  DataType;			    //
    public	   int   	  ChannelID;			//
    public	   int   	  DataID;				//
    public	   int   	  PCFadeInFadeOutFlg;	//
    public	   int      PcCustom;				//
    public	   int   	  DataLen;			    //注意设备发过来的数据长度是双字节的
    public	   int   	  Buf[] = new int[DataStruct.U0DataLen];	//
    public	   int   	  CheckSum;			    //
    public	   int      FrameEnd;			    //

    public	   int	  DataBuf[] = new int[DataStruct.U0DataLen+DataStruct.CMD_LENGHT];  //接受的临时网络数据12+100
	//------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------系统类型中数据变量-------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------
	public    DataStruct_Output[] OUT_CH = new DataStruct_Output[Define.MAX_CH];
	public    DataStruct_Input[]  IN_CH  = new DataStruct_Input[Define.MAX_CH];
	public    DataStruct_Inputs[] INS_CH = new DataStruct_Inputs[Define.MAX_CH];
	public    DataStruct_System   SYS    = new DataStruct_System();
    public    DataStruct_EFFect   EFF   = new DataStruct_EFFect();
	public Data(){
		/*初始化输入，输入通道的数据*/
		for(int i=0;i<Define.MAX_CH;i++){
			INS_CH[i]= new DataStruct_Inputs();
		}
		for(int i=0;i<Define.MAX_CH;i++){
			IN_CH[i]= new DataStruct_Input();
		}
		for(int i=0;i<Define.MAX_CH;i++){
			OUT_CH[i]= new DataStruct_Output();
		}
	}

    public static DSP_SingleData SingleData=new DSP_SingleData();//用于接收和解释网络的用户组数据，里面包含一组数据内容
    public static DSP_MACData MAC_Data=new DSP_MACData();//用于接收和解释网络的用户组数据，里面包含一整机组数据内容

}
