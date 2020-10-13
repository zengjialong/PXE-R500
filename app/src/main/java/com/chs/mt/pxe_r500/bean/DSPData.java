package com.chs.mt.pxe_r500.bean;

public class DSPData {

	//�û��û�JSON��Ϣ	
	//client
	private String client = "";
	//machine_type
	private String machine_type = "";
	//car_type
	private String car_type = "";
	//car_brand
	private String car_brand = "";
	//json_version
	private String json_version = "";
	//mcu_version
	private String mcu_version = "";
	//android_version
	private String android_version = "";
	//ios_version
	private String ios_version = "";
	//pc_version
	private String pc_version = "";
	//group_num
	private String group_num = "";
	//group_name
	private String group_name = "";
	//encryption_byte
	private int encryption_byte = 0;
	//encryption_bool
	private boolean encryption_bool = false;
	
//	//DSP_MusicData
//	private ArrayList<DSP_MusicData> music;
//	//DSP_OutputData
//	private ArrayList<DSP_OutputData> chs_output;
	//DSP_MusicData
	private DSP_MusicData music=new DSP_MusicData();
	//DSP_OutputData
	private DSP_OutputData output=new DSP_OutputData();
	//system
	private DSP_SystemData system=new DSP_SystemData();
	
	///////////////////////////////////////////////////////////////////////
	//client
	public void Set_client(String st){
		this.client=st;
	}
	public String Get_client(){
		return client;
	}
	//machine_type
	public void Set_machine_type(String st){
		this.machine_type=st;
	}
	public String Get_machine_type(){
		return machine_type;
	}
	//car_type
	public void Set_car_type(String st){
		this.car_type=st;
	}
	public String Get_car_type(){
		return car_type;
	}
	//car_brand
	public void Set_car_brand(String st){
		this.car_brand=st;
	}
	public String Get_car_brand(){
		return car_brand;
	}
	//json_version
	public void Set_json_version(String st){
		this.json_version=st;
	}
	public String Get_json_version(){
		return json_version;
	}
	//mcu_version
	public void Set_mcu_version(String st){
		this.mcu_version=st;
	}
	public String Get_mcu_version(){
		return mcu_version;
	}
	//android_version
	public void Set_android_version(String st){
		this.android_version=st;
	}
	public String Get_android_version(){
		return android_version;
	}
	//ios_version
	public void Set_ios_version(String st){
		this.ios_version=st;
	}
	public String Get_ios_version(){
		return ios_version;
	}
	//pc_version
	public void Set_pc_version(String st){
		this.pc_version=st;
	}
	public String Get_pc_version(){
		return pc_version;
	}
	//group_num
	public void Set_group_num(String st){
		this.group_num=st;
	}
	public String Get_group_num(){
		return group_num;
	}
	//group_name
	public void Set_group_name(String st){
		this.group_name=st;
	}
	public String Get_group_name(){
		return group_name;
	}
	//encryption_byte
	public void Set_encryption_byte(int st){
		this.encryption_byte=st;
	}
	public int Get_encryption_byte(){
		return encryption_byte;
	}
	//encryption_bool
	public void Set_encryption_bool(Boolean st){
		this.encryption_bool=st;
	}
	public Boolean Get_encryption_bool(){
		return encryption_bool;
	}
	
	//DSP_MusicData
	public void Set_DSP_MusicData(DSP_MusicData st){
		this.music=st;
	}
	public DSP_MusicData Get_DSP_MusicData(){
		return music;
	}
	
//	public ArrayList<DSP_MusicData> getDSP_MusicData() {
//		return music;
//	}
//
//	public void setDSP_MusicData(ArrayList<DSP_MusicData> data) {
//		this.music = data;
//	}
	//DSP_OutputData
	public void Set_DSP_OutputData(DSP_OutputData st){
		this.output=st;
	}
	public DSP_OutputData Get_DSP_OutputData(){
		return output;
	}
//	public ArrayList<DSP_OutputData> getDSP_OutputData() {
//		return chs_output;
//	}
//
//	public void setDSP_OutputData(ArrayList<DSP_OutputData> data) {
//		this.chs_output = data;
//	}
	//system
	public void Set_SystemData(DSP_SystemData st){
		this.system=st;
	}
	public DSP_SystemData Get_SystemData(){
		return system;
	}
	
	
	
	public DSPData() {
		super();
		// TODO Auto-generated constructor stub
	}

 
}
