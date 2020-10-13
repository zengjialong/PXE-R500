package com.chs.mt.pxe_r500.bean;

public class DSP_DataInfo {

	//�û��û�JSON��Ϣ
	private String data_user_name = "";
	private String data_user_tel = "";
	private String data_user_mailbox = "";
	private String data_user_info = "";
	private String data_machine_type = "";
	private String data_car_type = "";
	private String data_car_brand = "";
	private String data_json_version = "";
	private String data_mcu_version = "";
	private String data_android_version = "";
	private String data_ios_version = "";
	private String data_pc_version = "";
	private String data_group_num = "";
	private String data_group_name = "";
	private String data_eff_briefing = "";
	private String data_upload_time = "";
	private int data_encryption_byte = 0;
	private String data_encryption_bool = "0";
	private int data_head_data = 0;
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
	//data_mcu_version
	public void Set_data_mcu_version(String st){
		this.data_mcu_version=st;
	}
	public String Get_data_mcu_version(){
		return data_mcu_version;
	}
	//data_android_version
	public void Set_data_android_version(String st){
		this.data_android_version=st;
	}
	public String Get_data_android_version(){
		return data_android_version;
	}
	//data_ios_version
	public void Set_data_ios_version(String st){
		this.data_ios_version=st;
	}
	public String Get_data_ios_version(){
		return data_ios_version;
	}
	//data_pc_version
	public void Set_data_pc_version(String st){
		this.data_pc_version=st;
	}
	public String Get_data_pc_version(){
		return data_pc_version;
	}
	//data_group_num
	public void Set_data_group_num(String st){
		this.data_group_num=st;
	}
	public String Get_data_group_num(){
		return data_group_num;
	}
	//data_group_name
	public void Set_data_group_name(String st){
		this.data_group_name=st;
	}
	public String Get_data_group_name(){
		return data_group_name;
	}
	//data_eff_briefing
	public void Set_data_eff_briefing(String st){
		this.data_eff_briefing=st;
	}
	public String Get_data_eff_briefing(){
		return data_eff_briefing;
	}
	//data_upload_time
	public void Set_data_upload_time(String st){
		this.data_upload_time=st;
	}
	public String Get_data_upload_time(){
		return data_upload_time;
	}
	//encryption_byte = 0;
	//encryption_bool = false;

	//encryption_byte
	public void Set_data_encryption_byte(int st){
		this.data_encryption_byte=st;
	}
	public int Get_data_encryption_byte(){
		return data_encryption_byte;
	}
	//encryption_bool
	public void Set_data_encryption_bool(String st){
		this.data_encryption_bool=st;
	}
	public String Get_data_encryption_bool(){
		return data_encryption_bool;
	}
	//data_head_data = 0;
	public void Set_data_head_data(int st){
		this.data_head_data=st;
	}
	public int Get_data_head_data(){
		return data_head_data;
	}
	//////////////////////////////////////////////////////////////////////////////
	public DSP_DataInfo() {
		super();
		this.data_user_name="";
		this.data_user_tel="";
		this.data_user_mailbox="";
		this.data_user_info="";
		this.data_machine_type="";
		this.data_car_type="";
		this.data_car_brand="";
		this.data_json_version="";
		this.data_mcu_version="";
		this.data_android_version="";
		this.data_ios_version="";
		this.data_pc_version="";
		this.data_group_num="";
		this.data_group_name="";
		this.data_eff_briefing="";
		this.data_upload_time="";
		this.data_encryption_byte=0;
		this.data_encryption_bool="0";
		this.data_head_data=0;
	}
	public DSP_DataInfo(
			String data_user_name,
			String data_user_tel,
			String data_user_mailbox,
			String data_user_info,
			String data_machine_type,
			String data_car_type,
			String data_car_brand,
			String data_json_version,
			String data_mcu_version,
			String data_android_version,
			String data_ios_version,
			String data_pc_version,
			String data_group_num,
			String data_group_name,
			String data_eff_briefing,
			String data_upload_time,
			int data_encryption_byte,
			String data_encryption_bool,
			int data_head_data
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
		this.data_mcu_version=data_mcu_version;
		this.data_android_version=data_android_version;
		this.data_ios_version=data_ios_version;
		this.data_pc_version=data_pc_version;
		this.data_group_num=data_group_num;
		this.data_group_name=data_group_name;
		this.data_eff_briefing=data_eff_briefing;
		this.data_upload_time=data_upload_time;
		this.data_encryption_byte=data_encryption_byte;
		this.data_encryption_bool=data_encryption_bool;
		this.data_head_data=data_head_data;
	}


}
