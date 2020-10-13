package com.chs.mt.pxe_r500.datastruct;

public class DataStruct_EFFect {	
	public  DataStruct_EQ[]  EQ = new DataStruct_EQ[Define.MAX_EFF_CH_EQ];//左回声EQ
	//高低通 ,ID = 8	只有频率可调 2011-10-20
	public  int   h_freq;	   //高通频率，20~20K，stp:1hz,发送实际频率值，如201HZ就发201
	public  int	  h_filter;	   //保留	高通类型值，0－－LR,1－－BESSEL,2－－BUTTERWORTH
	public  int   h_level;	   //保留	高通斜率值，0－－6db,1－－18db,2－－24db
	public  int   l_freq;	   //低通频率	
	public  int	  l_filter;	   //保留	低通类型
	public  int	  l_level;	   //保留	低通斜率值
	//----id = 9	        回声
	public  int	  Echo_Space;  //回声空间 改改成了 乒乓方式了
	public  int	  Echo_Delay;  //右延时	 Echo delay    0~250 (0~250 stp:1ms )  发送数据范围 0~250
	public  int	  Echo_level;  //回声电平
	public  int	  Echo_Repeat; //重复次数 0~100%
	public  int   Mic_Phase;     //麦克风相位
	public  int   R_preDelay;  //保留
	public  int   Echo_LoRatio;//回声湿度	0.01~0.50 步进1 发送范围 0~8
	public  int	  feedback;	   //反馈抑制等级 0 1 2 3 0：关闭
	//Effect ,ID= 10        混响
	public  int	 Rev_time;	   //混响时间 Reverb time  0.0~6.0  步进:0.1s	  发送数据0~60
	public  int	 Rev_level;	   //混响电平 Echo level1   80~100%  步进:1%     发送数据0~99
	public  int  Rev_LoRatio;  //混响湿度 比列	0.00~0.50 步进0.01		发送数据0~50
	public  int  gain;		   //效果音量，数值范围0-140，120为0dB, 注意设定值不得小于-60dB
	public  int  Mic_mute;     //麦克风静音，0：静音，1：正常输出
	public  int  Mic_vol;      //麦克风音量，数值范围0-140，120为0dB, 注意设定值不得小于-60dB
	public  int  Music_vol;    //音乐音量，数值范围0-140，120为0dB, 注意设定值不得小于-60dB
	public  int	 Rev_preDelay; //混响预延时	 0~100 (0~30 stp:1ms )  发送数据范围 0~30
	//----id =11
	public  byte[] name = new byte[8];
	
	public DataStruct_EFFect(){
		for(int j=0;j<Define.MAX_EFF_CH_EQ;j++){
			EQ[j]= new DataStruct_EQ();
		}
	}
}
