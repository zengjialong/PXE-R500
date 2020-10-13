package com.chs.mt.pxe_r500.datastruct;
//麦克风音乐输入  共160字节
public class DataStruct_Inputs {	
	//---id = 0		杂项
	public  int	feedback;	//反馈抑制 0~4 1 发送数据范围0~4
	public  int	polar;		//极极，0－－同相，1－－反相
	public  int	eq_mode;		//模式选择位，0标准模式  1唱歌模式
	public  int	mute;		//静音2011-6-2  改 zhihui 									
	public  int	delay;		//(delay限music)延时, 0~30Ms (0~475  stp:0.021ms   476~496 str:1ms) 发送数据范围 0~496 （只有麦克风有）
	public  int	Valume;		//音量  范围0~100% dB  步进1  发送数据（0~100）当为0时，静音灯亮2010-11-8改
	//高低通 ,ID = 1	
	public  int	h_freq;		//高通频率，20~20K，stp:1hz,发送实际频率值，如201HZ就发201
	public  int	h_filter;	//保留	高通类型值，0－－LR,1－－BESSEL,2－－BUTTERWORTH
	public  int	h_level;	//保留	高通斜率值，0－－6db,1－－18db,2－－24db
	public  int	l_freq;		//低通频率	
	public  int	l_filter;	//保留	低通类型
	public  int	l_level;	//保留	低通斜率值
	
	public  DataStruct_EQ[] EQ = new DataStruct_EQ[Define.MAX_CHEQ];//音乐7个EQ 话筒15个EQ  固定参量均衡
	
	public DataStruct_Inputs(){
		for(int j=0;j<Define.MAX_CHEQ;j++){
			EQ[j]= new DataStruct_EQ();
		}
	}
}
