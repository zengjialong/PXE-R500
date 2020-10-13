package com.chs.mt.pxe_r500.datastruct;

public class DataStruct_IOS {	
	public  DataStruct_Output[] OUT_CH = new DataStruct_Output[Define.MAX_CH];
	public  DataStruct_Input[]  IN_CH  = new DataStruct_Input[Define.MAX_CH];
	public  DataStruct_Inputs[]  INS_CH  = new DataStruct_Inputs[Define.MAX_CH];
	
	public DataStruct_IOS(){
		/*初始化输入，输入通道的数据*/
		for(int i=0;i<Define.MAX_CH;i++){
			IN_CH[i]= new DataStruct_Input();			
		}
		for(int i=0;i<Define.MAX_CH;i++){
			OUT_CH[i]= new DataStruct_Output();			
		}
		for(int i=0;i<Define.MAX_CH;i++){
			INS_CH[i]= new DataStruct_Inputs();			
		}
	}
}