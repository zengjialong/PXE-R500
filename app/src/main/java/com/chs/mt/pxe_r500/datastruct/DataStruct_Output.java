package com.chs.mt.pxe_r500.datastruct;
//每个ID里包含有8个字节
public class DataStruct_Output {	
	//public	int	  EQ[][] = new int[31][5]; 
	public  DataStruct_EQ[] EQ = new DataStruct_EQ[Define.MAX_CHEQ];
	//id = 31		杂项
	public  int  mute;  //静音，0－－静音，1－－非静音
	public  int  polar; //极极，0－－同相，1－－反相
	public  int	 gain;  //输出总音量，连调，调试一个其他的通道都改变，只发送一个通道的数据//	增益植0~100%,stp:0.1,实际发送值：0~100
	public  int	 delay; //延时, 0~60Ms (0~475  stp:0.021ms   476~526 str:1ms) 发送数据范围 0~526
	public  int  eq_mode;		//EQ 模式 PEQ/GEQ
	public  int  LinkFlag;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)  注意：此值用系统结构中的喇叭类型代替，不随用户组数据改变
	//public  int  reduce_gain;	   //保留 低音通道的增益自动衰减或关闭此功能(只有OUTPUT6有此功能)，0~80dB，步进1dB
	//public  int  reduce_threshold; //保留 低音通道的增益自动衰减的启动门限，-20~-80dB，步进1dB  

	//高低通 ,ID = 32	(xover限MIC)
	public  int  h_freq;   //高通频率，20~20K，stp:1hz,发送实际频率值，如201HZ就发201
	public  int  h_filter; //高通类型值，0－－LR,1－－BESSEL,2－－BUTTERWORTH
	public  int  h_level;  //高通斜率值，0－－6db,1－－18db,2－－24db
	public  int  l_freq;   //低通频率	
	public  int  l_filter; //低通类型
	public  int  l_level;  //低通斜率值
	 
	// id = 33		混合比例
	public  int  IN1_Vol;  //音量  0%~100%
	public  int  IN2_Vol;  //音量
	public  int  IN3_Vol;  //音量
	public  int  IN4_Vol;  //音量
	public  int  IN5_Vol;   //保留
	public  int  IN6_Vol;    //保留
	public  int  IN7_Vol;    //保留
	public  int  IN8_Vol;    //保留
	
	// id = 34	        保留  
	public  int  IN9_Vol;	   //
	public  int	 IN10_Vol;	   //
	public  int	 IN11_Vol;	   //
	public  int  IN12_Vol;	   //
	public  int  IN13_Vol;	   //
	public  int  IN14_Vol;	   //	
	public  int  IN15_Vol;	   //
	public  int  IN16_Vol;	   //	
	//public  int  IN_polar; //极极，0－－同相，1－－反相，16位，15.14..3.2.1.0 对应IN4_Vol，IN3_Vol，IN2_Vol，IN1_Vol

	// id = 35		压限
	public  int  lim_t;     //压限器阀值，-30dbu~+20dbu，stp:0.1,实际发送值0~500
	public  int  lim_a;		//起动时间,0.3ms~100ms，0.3~1ms,stp:0.1;1~100ms,stp:1,实际发送值为0~106
	public  int  lim_r;		//释放时间值,2x,4x,8x,16x,32x,64x ,实际发送0 ~ 5
	public  int  cliplim;	//保留	clip_limit; +2 ~ +12db,stp:0.1, 实际发送0~100
	public  int  lim_rate;	//保留	压缩系数，1：1~1：无穷，    分1：2 ：4 ：8：16：32：64：无穷，共分7级，实际发送0~7
	public  int  lim_mode;	//保留	0--limit; 1---compressor
	public  int  linkgroup_num;  //保留comp_swi:	0--manual, 1--auto,linkgroup_num:0无加入联调组，>=1已经加入联调组

	//, ID = 36
	public  int	EncryptFlg;//加密标志 0*20=未加密   非0x21=已加密
	public  byte[] name = new byte[7];
	
	public DataStruct_Output(){
		for(int j=0;j<Define.MAX_CHEQ;j++){
			EQ[j]= new DataStruct_EQ();
		}
	}
}
