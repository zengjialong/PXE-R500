package com.chs.mt.pxe_r500.datastruct;

public class DataStruct_EQ {	
	public  int   freq;   //频率  20Hz~20KHz 步进1Hz   发送数据20~20KHz
	public  int   level;  //增益值 -30.0dB~+15.0dB 步进0.1dB    发送数据300~750
	public  int   bw;     //Oct值  0.05~3.00/Oct 步进0.01       发送数据0~295		
	public  int   shf_db; //6dB,12dB(0:6dB; 1:12dB)	
	public  int   type;   //类型(0:PEQ;)	不可以调
}
