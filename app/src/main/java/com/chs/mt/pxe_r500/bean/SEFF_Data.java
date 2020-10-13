package com.chs.mt.pxe_r500.bean;



public class SEFF_Data {
	
	public static final String T_ID = "t_id";
	
	public static final String T_CID = "cid";
	public static final String T_UID = "uid";
	public static final String T_HIGHDATA = "highData";
	public static final String T_VERSION = "version";
	public static final String T_TYPE = "type";
	public static final String T_CTIME = "ctime";
	public static final String T_BRAND = "brand";
	public static final String T_MACMODEL = "macModel";
	public static final String T_DETAILS = "details";
	public static final String T_EFFNAME = "effName";
	
	private int id;	//���ݿ���
	//��ǰ��ϢID	 cid
	private String cid = "";
	//�����û� uid
	private String uid = "";
	//��Ƶ��Ϣ highData
	private String highData = "";
	//�汾�� version
	private String version = "";
	//���� 1�������� 2˽������ type
	private String type = "";
	//���ʱ�� ctime
	private String ctime = "";
	//Ʒ�� brand
	private String brand = "";
	//���� macModel
	private String macModel = "";
	//���� details
	private String details = "";
	//��Ч���� effName
	private String effName = "";	
	///////////////////////////////////////////////////////////////////////
	//��ǰ��ϢID	 cid
	public void Set_id(int id){
		this.id=id;
	}
	public int Get_id(){
		return id;
	}

	//��ǰ��ϢID	 cid
	public void Set_cid(String st){
		this.cid=st;
	}
	public String Get_cid(){
		return cid;
	}
	//�����û� uid
	public void Set_uid(String st){
		this.uid=st;
	}
	public String Get_uid(){
		return uid;
	}
	//��Ƶ��Ϣ highData
	public void Set_highData(String st){
		this.highData=st;
	}
	public String Get_highData(){
		return highData;
	}
	//�汾�� version
	public void Set_version(String st){
		this.version=st;
	}
	public String Get_version(){
		return version;
	}
	//���� 1�������� 2˽������ type
	public void Set_type(String st){
		this.type=st;
	}
	public String Get_type(){
		return type;
	}
	//���ʱ�� ctime
	public void Set_ctime(String st){
		this.ctime=st;
	}
	public String Get_ctime(){
		return ctime;
	}
	//Ʒ�� brand
	public void Set_brand(String st){
		this.brand=st;
	}
	public String Get_brand(){
		return brand;
	}
	//���� macModel
	public void Set_macModel(String st){
		this.macModel=st;
	}
	public String Get_macModel(){
		return macModel;
	}
	//���� details
	public void Set_details(String st){
		this.details=st;
	}
	public String Get_details(){
		return details;
	}
	//��Ч���� effName
	public void Set_effName(String st){
		this.effName=st;
	}
	public String Get_effName(){
		return effName;
	}
//////////////////////////////////////////////////////////////////////////////////	
	public SEFF_Data() {
		super();
		this.cid = "";
		//�����û� uid
		this.uid = "";
		//��Ƶ��Ϣ highData
		this.highData = "";
		//�汾�� version
		this.version = "";
		//���� 1�������� 2˽������ type
		this.type = "";
		//���ʱ�� ctime
		this.ctime = "";
		//Ʒ�� brand
		this.brand = "";
		//���� macModel
		this.macModel = "";
		//���� details
		this.details = "";
		//��Ч���� effName
		this.effName = "";	
	}
	// ����һ��������
	public SEFF_Data(
			int DB_ID, 
			String cid,
			String uid,
			String highData,
			String version,
			String type,
			String ctime,
			String brand,
			String macModel,
			String details,
			String effName
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.cid = cid;
		//�����û� uid
		this.uid = uid;
		//��Ƶ��Ϣ highData
		this.highData = highData;
		//�汾�� version
		this.version = version;
		//���� 1�������� 2˽������ type
		this.type = type;
		//���ʱ�� ctime
		this.ctime = ctime;
		//Ʒ�� brand
		this.brand = brand;
		//���� macModel
		this.macModel = macModel;
		//���� details
		this.details = details;
		//��Ч���� effName
		this.effName = effName;	
	}
	//����������
	public SEFF_Data(
			String cid,
			String uid,
			String highData,
			String version,
			String type,
			String ctime,
			String brand,
			String macModel,
			String details,
			String effName) {
		////////////////////////
		this.cid = cid;
		//�����û� uid
		this.uid = uid;
		//��Ƶ��Ϣ highData
		this.highData = highData;
		//�汾�� version
		this.version = version;
		//���� 1�������� 2˽������ type
		this.type = type;
		//���ʱ�� ctime
		this.ctime = ctime;
		//Ʒ�� brand
		this.brand = brand;
		//���� macModel
		this.macModel = macModel;
		//���� details
		this.details = details;
		//��Ч���� effName
		this.effName = effName;	
	}
 
}
