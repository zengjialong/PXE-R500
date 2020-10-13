package com.chs.mt.pxe_r500.datastruct;
//麦克风音乐输入  共160字节
public class DataStruct_Input {	
	public  DataStruct_EQ[] EQ = new DataStruct_EQ[Define.MAX_CHEQ];//音乐7个EQ 话筒15个EQ  固定参量均衡
	//public	int	  EQ[][] = new int[9][5]; 
	//---id = 9		杂项
	public  int	feedback;	//反馈抑制 0~4 1 发送数据范围0~4
	public  int	polar;		//极极，0－－同相，1－－反相
	public  int	mode;		//模式选择位，0标准模式  1唱歌模式
	public  int	mute;		//静音2011-6-2  改 zhihui 									
	public  int	delay;		//(delay限music)延时, 0~30Ms (0~475  stp:0.021ms   476~496 str:1ms) 发送数据范围 0~496 （只有麦克风有）
	public  int	Valume;		//音量  范围0~100% dB  步进1  发送数据（0~100）当为0时，静音灯亮2010-11-8改
	//高低通 ,ID = 10	
	public  int	h_freq;		//高通频率，20~20K，stp:1hz,发送实际频率值，如201HZ就发201
	public  int	h_filter;	//保留	高通类型值，0－－LR,1－－BESSEL,2－－BUTTERWORTH
	public  int	h_level;	//保留	高通斜率值，0－－6db,1－－18db,2－－24db
	public  int	l_freq;		//低通频率	
	public  int	l_filter;	//保留	低通类型
	public  int	l_level;	//保留	低通斜率值
	//噪声门 ,ID = 11	
	public  int	noisegate_t;//阀值，-120dbu~+10dbu,stp:1dbu,实际发送0~130
	public  int	noisegate_a;//保留  只有阀值可调，    起动时间，0.3ms ~ 100ms
	public  int	noisegate_k;//保留  只有阀值可调		保持时间,
	public  int	noisegate_r;//保留  只有阀值可调		释放时间,2X,4X,6X,8X,16X,32X
	public  int	noise_config;//保留 0--enable, 1--disable,当为disable时，阀值取 -120dbu(只由MCU及PC控制，DSP不用)
	//压限 ,ID = 12
	public  int	lim_t;		//压限器阀值，-30dbu~+20dbu，stp:0.1,实际发送值0~500
	public  int	lim_a;		//起动时间,0.3ms~100ms，0.3~1ms,stp:0.1;1~100ms,stp:1,实际发送值为0~106
	public  int	lim_r;		//释放时间值,2x,4x,8x,16x,32x,64x ,实际发送0 ~ 5
	public  int	cliplim;	//保留  clip_limit; +2 ~ +12db,stp:0.1, 实际发送0~100
	public  int	lim_rate;	//保留	压缩系数，1：1~1：无穷，    分1：2 ：4 ：8：16：32：64：无穷，共分7级，实际发送0~7
	public  int	lim_mode;	//保留	0--limit; 1---compressor
	public  int	comp_swi;   //保留	0--manual, 1--auto
	//, ID = 13
	public  byte[] name = new byte[8];
	
	public DataStruct_Input(){
		for(int j=0;j<Define.MAX_CHEQ;j++){
			EQ[j]= new DataStruct_EQ();
		}
	}
}
