package com.chs.mt.pxe_r500.bean;

import com.chs.mt.pxe_r500.datastruct.Define;

public class DSP_Data {
	private int[] group_name = new int[16];
	//DSP_MusicData
	private DSP_MusicData music=new DSP_MusicData();
	//DSP_EFFData
	//private DSP_EFFData eff=new DSP_EFFData();
	//DSP_OutputData
	private DSP_OutputData output=new DSP_OutputData();
	//system
	private int[] system=new int[Define.MAX_SYSTEM];
	
	public void Set_group_name(int[] group_name){
		this.group_name=group_name;
	}
	public int[] Get_group_name(){
		return this.group_name;
	}
	///////////////////////////////////////////////////////////////////////
	//DSP_MusicData
	public void Set_DSP_MusicData(DSP_MusicData st){
		this.music=st;
	}
	public DSP_MusicData Get_DSP_MusicData(){
		return music;
	}
	//DSP_EFFData
//	public void Set_DSP_EFFData(DSP_EFFData st){
//		this.eff=st;
//	}
//	public DSP_EFFData Get_DSP_EFFData(){
//		return eff;
//	}
	
	//DSP_OutputData
	public void Set_DSP_OutputData(DSP_OutputData st){
		this.output=st;
	}
	public DSP_OutputData Get_DSP_OutputData(){
		return output;
	}
	public void Set_SystemData(int[] initData) {
		for (int i = 0; i < Define.MAX_SYSTEM; i++) {
			this.system[i] = initData[i];
		}
	}
	public int[] Set_SystemData() {
		return system;
	}

	
	public DSP_Data() {
		super();
		this.system=null;
		this.output=null;
		this.music=null;
		this.group_name=null;
	}
	
	public DSP_Data(
			DSP_OutputData output,
			DSP_MusicData music,

			int[] system,
			int[] group_name) {
		super();
		this.system=system;
		this.output=output;
		this.music=music;
		this.group_name=group_name;
	}

	public DSP_Data(
			DSP_OutputData output,
			DSP_MusicData music,
			int[] group_name) {
		super();
		this.output=output;
		this.music=music;
		this.group_name=group_name;
	}

 
}
