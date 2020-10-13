package com.chs.mt.pxe_r500.bean;

import com.chs.mt.pxe_r500.datastruct.Define;

public class DSP_MACData {
	public String fileType=JsonDataSt.DSP_Complete;
	//CHS
	public Company chs=new Company();
	//client
	public Company client=new Company();
	//client
	public DSP_DataInfo data_info=new DSP_DataInfo();
	//data
	public DSP_DataMac[] data=new DSP_DataMac[Define.MAX_GROUP_USE+1];
	//system
	//public DSP_SystemData system=new DSP_SystemData();
	public int[] system = new int[Define.MAX_SYSTEM];
	///////////////////////////////////////////////////////////////////////
	//fileType
	public void Set_fileType(String st){
		this.fileType=st;
	}
	public String Get_fileType(){
		return this.fileType;
	}
	//CHS
	public void Set_chs(Company st){
		this.chs=st;
	}
	public Company Get_chs(){
		return this.chs;
	}
	//client
	public void Set_client(Company st){
		this.client=st;
	}
	public Company Get_client(){
		return this.client;
	}
	//data_info
	public void Set_data_info(DSP_DataInfo st){
		this.data_info=st;
	}
	public DSP_DataInfo Get_data_info(){
		return this.data_info;
	}

	//data
	public void Set_data(DSP_DataMac[] data){
		this.data=data;
	}
	public DSP_DataMac[] Get_data(){
		return this.data;
	}


	public DSP_DataMac Get_data( int index){
		if(index >= (Define.MAX_GROUP_USE+1)){
			index = Define.MAX_GROUP_USE;
		}

		return this.data[index];
	}
	public void Set_data(DSP_DataMac data,int index){
		if(index >= (Define.MAX_GROUP_USE+1)){
			index = Define.MAX_GROUP_USE;
		}
		this.data[index]=data;
	}
	/*system
	public void Set_SystemData(DSP_SystemData st){
		this.system=st;
	}
	public DSP_SystemData Get_SystemData(){
		return system;
	}
	*/
	public void Set_SystemData(int[] initData) {
		for (int i = 0; i < Define.MAX_SYSTEM; i++) {
			this.system[i] = initData[i];
		}
	}
	public int[] Set_SystemData() {
		return system;
	}

	public DSP_MACData() {
		super();
		for(int i=0;i<Define.MAX_GROUP_USE+1;i++){
			this.data[i]= new DSP_DataMac();
		}
	}
	public DSP_MACData(Company chs,Company client,DSP_DataInfo data_info,DSP_DataMac[] data,int[] system) {
		super();

		for(int i=0;i<Define.MAX_GROUP_USE+1;i++){
			this.data[i]= new DSP_DataMac();
		}

		this.chs=chs;
		this.client=client;
		this.data_info=data_info;
		this.data=data;
		this.system=system;
	}

}
