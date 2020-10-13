package com.chs.mt.pxe_r500.bean;

public class DSP_SingleData {
	private String fileType=JsonDataSt.DSP_Single;
	//CHS
	private Company chs=new Company();
	//client
	private Company client=new Company();
	//client
	private DSP_DataInfo data_info=new DSP_DataInfo();	
	//data
	private DSP_Data data=new DSP_Data();	
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
		return chs;
	}
	//client
	public void Set_client(Company st){
		this.client=st;
	}
	public Company Get_client(){
		return client;
	}
	//data_info
	public void Set_data_info(DSP_DataInfo st){
		this.data_info=st;
	}
	public DSP_DataInfo Get_data_info(){
		return data_info;
	}
	//data
	public void Set_data(DSP_Data st){
		this.data=st;
	}
	public DSP_Data Get_data(){
		return data;
	}
	
	
	public DSP_SingleData() {
		super();
		this.chs=null;
		this.client=null;
		this.data_info=null;
		this.data=null;
		
	}
	public DSP_SingleData(Company chs,Company client,DSP_DataInfo data_info,DSP_Data data) {
		super();
		this.chs=chs;
		this.client=client;
		this.data_info=data_info;
		this.data=data;
	}




}
