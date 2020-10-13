package com.chs.mt.pxe_r500.bean;

public class MacCfg_DataInfo {

	private String data_user_name = "";
	private String data_user_tel = "";
	private String data_user_mailbox = "";
	private String data_user_info = "";
	private String data_machine_type = "";
	private String data_car_type = "";
	private String data_car_brand = "";
	private String data_json_version = "";
	private String data_briefing = "";
	private String data_upload_time = "";
	///////////////////////////////////////////////////////////////////////
	//data_user_name = "";
	public void Set_data_user_name(String st){
		this.data_user_name=st;
	}
	public String Get_data_user_name(){
		return data_user_name;
	}
	//data_user_tel = "";
	public void Set_data_user_tel(String st){
		this.data_user_tel=st;
	}
	public String Get_data_user_tel(){
		return data_user_tel;
	}
	//data_user_mailbox = "";
	public void Set_data_user_mailbox(String st){
		this.data_user_mailbox=st;
	}
	public String Get_data_user_mailbox(){
		return data_user_mailbox;
	}
	//data_user_info
	public void Set_data_user_info(String st){
		this.data_user_info=st;
	}
	public String Get_data_user_info(){
		return data_user_info;
	}
	//data_machine_type
	public void Set_data_machine_type(String st){
		this.data_machine_type=st;
	}
	public String Get_data_machine_type(){
		return data_machine_type;
	}
	//data_car_type
	public void Set_data_car_type(String st){
		this.data_car_type=st;
	}
	public String Get_data_car_type(){
		return data_car_type;
	}
	//data_car_brand
	public void Set_data_car_brand(String st){
		this.data_car_brand=st;
	}
	public String Get_data_car_brand(){
		return data_car_brand;
	}
	//data_json_version
	public void Set_data_json_version(String st){
		this.data_json_version=st;
	}
	public String Get_data_json_version(){
		return data_json_version;
	}
	//data_eff_briefing
	public void Set_data_eff_briefing(String st){
		this.data_briefing=st;
	}
	public String Get_data_eff_briefing(){
		return data_briefing;
	}
	//data_upload_time
	public void Set_data_upload_time(String st){
		this.data_upload_time=st;
	}
	public String Get_data_upload_time(){
		return data_upload_time;
	}
	//////////////////////////////////////////////////////////////////////////////
	public MacCfg_DataInfo() {
		super();
		this.data_user_name="";
		this.data_user_tel="";
		this.data_user_mailbox="";
		this.data_user_info="";
		this.data_machine_type="";
		this.data_car_type="";
		this.data_car_brand="";
		this.data_json_version="";
		this.data_briefing="";
		this.data_upload_time="";
	}
	public MacCfg_DataInfo(
			String data_user_name,
			String data_user_tel,
			String data_user_mailbox,
			String data_user_info,
			String data_machine_type,
			String data_car_type,
			String data_car_brand,
			String data_json_version,
			String data_briefing,
			String data_upload_time
			) {
		super();
		this.data_user_name=data_user_name;
		this.data_user_tel=data_user_tel;
		this.data_user_mailbox=data_user_mailbox;
		this.data_user_info=data_user_info;
		this.data_machine_type=data_machine_type;
		this.data_car_type=data_car_type;
		this.data_car_brand=data_car_brand;
		this.data_json_version=data_json_version;
		this.data_briefing=data_briefing;
		this.data_upload_time=data_upload_time;
	}
}
