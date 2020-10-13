package com.chs.mt.pxe_r500.bean;

public class DSP_DataMac {
	public int[] group_name = new int[16];
	//DSP_MusicData
	public DSP_MusicData music = new DSP_MusicData();
	//DSP_EFFData
	//public DSP_EFFData eff = new DSP_EFFData();
	//DSP_OutputData
	public DSP_OutputData output = new DSP_OutputData();
	
	
	///////////////////////////////////////////////////////////////////////
	//group_name
	public void Set_group_name(int[] group_name){
		this.group_name=group_name;
	}
	public int[] Get_group_name(){
		return this.group_name;
	}
	
	//DSP_MusicData
	public void Set_DSP_MusicData(DSP_MusicData st){
		this.music=st;
	}
	public DSP_MusicData Get_DSP_MusicData(){
		return music;
	}
	
	//DSP_OutputData
	public void Set_DSP_OutputData(DSP_OutputData st){
		this.output=st;
	}
	public DSP_OutputData Get_DSP_OutputData(){
		return output;
	}

	
	public DSP_DataMac() {
		super();
		this.output = new DSP_OutputData();
		this.music = new DSP_MusicData();
		//this.group_name=null;
		for(int i=0;i<16;i++){
			this.group_name[i] = 0;
		}
	}
	
	public DSP_DataMac(DSP_OutputData output,DSP_MusicData music,int[] group_name) {
		super();
		this.output=output;
		this.music=music;
		this.group_name=group_name;
	}

 
}
