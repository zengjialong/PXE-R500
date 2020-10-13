package com.chs.mt.pxe_r500.bean;



public class SEFF_File {
	
	public static final String T_ID = "t_id";
	
	public static final String F_ID       = "file_id";
	public static final String F_TYPE     = "file_type";
	public static final String F_NAME     = "file_name";
	public static final String F_PATH     = "file_path";
	public static final String F_FAVORITE = "file_favorite";
	public static final String F_LOVE     = "file_love";
	public static final String F_SIZE     = "file_size";//������Ϣjson�ṹ
	public static final String F_TIME     = "file_time";//������Ϣjson�ṹ
	public static final String F_MSG      = "file_msg";//������Ϣjson�ṹ
	
	public static final String D_USER_NAME      = "data_user_name";
	public static final String D_MAC_TYPE       = "data_machine_type";
	public static final String D_CAR_TYPE       = "data_car_type";
	public static final String D_CAR_BRAND      = "data_car_brand";
	public static final String D_GROUP_NAME     = "data_group_name";
	public static final String D_UPLOAD_TIME    = "data_upload_time";
	public static final String D_BRIEFING       = "data_eff_briefing";
	
	public static final String L_SEL           = "list_sel";//0:����ʾѡ���1����ʾѡ���2��ѡ���ѡ��
	public static final String L_IS_OPEN       = "list_is_open";

	private int id;	//���ݿ���
	private String file_id = "";
	private String file_type = "";
	private String file_name = "";
	private String file_path = "";
	private String file_favorite = "";
	private String file_love = "";
	private String file_size = "";
	private String file_time = "";
	private String file_msg = "";
	
	private String data_user_name = "";
	private String data_machine_type = "";
	private String data_car_type = "";
	private String data_car_brand = "";
	private String data_group_name = "";
	private String data_upload_time = "";
	private String data_eff_briefing = "";
	
	private String list_sel = "";
	private String list_is_open = "";
	///////////////////////////////////////////////////////////////////////
	//��ǰ��ϢID	 cid
	public void Set_id(int id){
		this.id=id;
	}
	public int Get_id(){
		return this.id;
	}
	
	//file_id
	public void Set_file_id(String st){
		this.file_id=st;
	}
	public String Get_file_id(){
		return this.file_id;
	}

	//file_type
	public void Set_file_type(String st){
		this.file_type=st;
	}
	public String Get_file_type(){
		return this.file_type;
	}
	
	//file_name
	public void Set_file_name(String st){
		this.file_name=st;
	}
	public String Get_file_name(){
		return this.file_name;
	}
	
	//file_path
	public void Set_file_path(String st){
		this.file_path=st;
	}
	public String Get_file_path(){
		return this.file_path;
	}


	//file_favorite
	public void Set_file_favorite(String st){
		this.file_favorite=st;
	}
	public String Get_file_favorite(){
		return this.file_favorite;
	}
	//collect
	public void Set_file_love(String st){
		this.file_love=st;
	}
	public String Get_file_love(){
		return this.file_love;
	}
	//file_size
	public void Set_file_size(String st){
		this.file_size=st;
	}
	public String Get_file_size(){
		return this.file_size;
	}
	//file_time
	public void Set_file_time(String st){
		this.file_time=st;
	}
	public String Get_file_time(){
		return this.file_time;
	}
	//file_msg
	public void Set_file_msg(String st){
		this.file_msg=st;
	}
	public String Get_file_msg(){
		return this.file_msg;
	}
	//data_user_name
	public void Set_data_user_name(String st){
		this.data_user_name=st;
	}
	public String Get_data_user_name(){
		return this.data_user_name;
	}
	//data_machine_type
	public void Set_data_machine_type(String st){
		this.data_machine_type=st;
	}
	public String Get_data_machine_type(){
		return this.data_machine_type;
	}
	//data_car_type
	public void Set_data_car_type(String st){
		this.data_car_type=st;
	}
	public String Get_data_car_type(){
		return this.data_car_type;
	}
	//data_car_brand
	public void Set_data_car_brand(String st){
		this.data_car_brand=st;
	}
	public String Get_data_car_brand(){
		return this.data_car_brand;
	}
	//data_group_name
	public void Set_data_group_name(String st){
		this.data_group_name=st;
	}
	public String Get_data_group_name(){
		return this.data_group_name;
	}
	//data_upload_time
	public void Set_data_upload_time(String st){
		this.data_upload_time=st;
	}
	public String Get_data_upload_time(){
		return this.data_upload_time;
	}
	//data_eff_briefing
	public void Set_data_eff_briefing(String st){
		this.data_eff_briefing=st;
	}
	public String Get_data_eff_briefing(){
		return this.data_eff_briefing;
	}
	//list_sel
	public void Set_list_sel(String st){
		this.list_sel=st;
	}
	public String Get_list_sel(){
		return this.list_sel;
	}
	//list_is_open
	public void Set_list_is_open(String st){
		this.list_is_open=st;
	}
	public String Get_list_is_open(){
		return this.list_is_open;
	}

//////////////////////////////////////////////////////////////////////////////////	
	public SEFF_File() {
		super();
		this.file_id = "";
		this.file_type = "";
		this.file_name = "";
		this.file_path = "";
		this.file_favorite = "";
		this.file_love = "";
		this.file_size = "";
		this.file_time = "";
		this.file_msg = "";
		
		this.data_user_name = "";
		this.data_machine_type = "";
		this.data_car_type = "";
		this.data_car_brand = "";
		this.data_group_name = "";
		this.data_upload_time = "";
		this.data_eff_briefing = "";
		
		this.list_sel = "";
		this.list_is_open = "";
	}
	// ����һ��������
	public SEFF_File(
			int DB_ID, 
			String file_id,
			String file_type,
			String file_name,
			String file_path,
			String file_favorite,
			String file_love,
			String file_size,
			String file_time,
			String file_msg,
			
			String data_user_name,
			String data_machine_type,
			String data_car_type,
			String data_car_brand,
			String data_group_name,
			String data_upload_time,
			String data_eff_briefing,
			
			String list_sel,
			String list_is_open
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.file_id = file_id;
		this.file_type = file_type;
		this.file_name = file_name;
		this.file_path = file_path;
		this.file_favorite = file_favorite;
		this.file_love = file_love;
		this.file_size = file_size;
		this.file_time = file_time;
		this.file_msg = file_msg;

		this.data_user_name = data_user_name;
		this.data_machine_type = data_machine_type;
		this.data_car_type = data_car_type;
		this.data_car_brand = data_car_brand;
		this.data_group_name = data_group_name;
		this.data_upload_time = data_upload_time;
		this.data_eff_briefing = data_eff_briefing;
		
		this.list_sel = list_sel;
		this.list_is_open = list_is_open;
	}
	//����������
	public SEFF_File(
			String file_id,
			String file_type,
			String file_name,
			String file_path,
			String file_favorite,
			String file_love,
			String file_size,
			String file_time,
			String file_msg,			

			String data_user_name,
			String data_machine_type,
			String data_car_type,
			String data_car_brand,
			String data_group_name,
			String data_upload_time,
			String data_eff_briefing,
			
			String list_sel,
			String list_is_open
			) {
		////////////////////////
		this.file_id = file_id;
		this.file_type = file_type;
		this.file_name = file_name;
		this.file_path = file_path;
		this.file_favorite = file_favorite;
		this.file_love = file_love;
		this.file_size = file_size;
		this.file_time = file_time;
		this.file_msg = file_msg;
		
		this.data_user_name = data_user_name;
		this.data_machine_type = data_machine_type;
		this.data_car_type = data_car_type;
		this.data_car_brand = data_car_brand;
		this.data_group_name = data_group_name;
		this.data_upload_time = data_upload_time;
		this.data_eff_briefing = data_eff_briefing;
		

		this.list_sel = list_sel;
		this.list_is_open = list_is_open;
	}
 
}
