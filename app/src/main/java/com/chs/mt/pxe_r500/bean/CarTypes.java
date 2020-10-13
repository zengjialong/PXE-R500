package com.chs.mt.pxe_r500.bean;



public class CarTypes {
	
	public static final String T_ID = "t_id";
	
	public static final String T_CID = "cid";
	public static final String T_BRAND_ID = "brand_id";
	public static final String T_NAME = "name";
	public static final String T_IMG_PATH = "img_path";
	private int id;	//���ݿ���
	//cid
	private String cid = "";
	// brand_id
	private String brand_id = "";
	// name
	private String name = "";
	// img_path
	private String img_path = "";
	///////////////////////////////////////////////////////////////////////
	//ID	 id
	public void Set_id(int id){
		this.id=id;
	}
	public int Get_id(){
		return id;
	}
	
	// cid
	public void Set_cid(String st){
		this.cid=st;
	}
	public String Get_cid(){
		return cid;
	}
	
	// brand_id
	public void Set_brand_id(String brand_id){
		this.brand_id=brand_id;
	}
	public String Get_brand_id(){
		return brand_id;
	}
	
	//name
	public void Set_name(String name){
		this.name=name;
	}
	public String Get_name(){
		return name;
	}
	//img_path
	public void Set_img_path(String img_path){
		this.img_path=img_path;
	}
	public String Get_img_path(){
		return img_path;
	}
//////////////////////////////////////////////////////////////////////////////////	
	public CarTypes() {
		super();
		this.cid = "";
		this.brand_id = "";
		this.name = "";
		this.img_path = "";
	}
	// ����һ��������
	public CarTypes(
			int DB_ID, 
			String cid,
			String brand_id,
			String name,
			String img_path
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.cid = cid;
		this.brand_id = brand_id;
		this.name = name;
		this.img_path = img_path;
	}
	//����������
	public CarTypes(
			String cid,
			String brand_id,
			String name,
			String img_path) {
		////////////////////////
		this.cid = cid;
		this.brand_id = brand_id;
		this.name = name;
		this.img_path = img_path;
	}
 
}
