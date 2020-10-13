package com.chs.mt.pxe_r500.bean;



public class ImageUrl {
	
	public static final String T_ID = "t_id";
	
	public static final String T_IDN = "id";
	public static final String T_UID = "uid";
	public static final String T_TITLE = "title";
	public static final String T_EFFNAME = "effname";
	public static final String T_IMG_PATH = "img_path";
	public static final String T_URL = "url";
	
	private int id;	//���ݿ���
	private String idn = "";
	private String uid = "";
	private String title = "";
	private String effname = "";
	private String img_path = "";
	private String url = "";
	///////////////////////////////////////////////////////////////////////
	//ID	 id
	public void Set_id(int id){
		this.id=id;
	}
	public int Get_id(){
		return id;
	}
	
	// idn
	public void Set_idn(String st){
		this.idn=st;
	}
	public String Get_idn(){
		return idn;
	}
	
	// uid
	public void Set_uid(String uid){
		this.uid=uid;
	}
	public String Get_uid(){
		return uid;
	}
	
	//title
	public void Set_title(String title){
		this.title=title;
	}
	public String Get_title(){
		return title;
	}
	//effname
	public void Set_effname(String effname){
		this.effname=effname;
	}
	public String Get_effname(){
		return effname;
	}
	//img_path
	public void Set_img_path(String img_path){
		this.img_path=img_path;
	}
	public String Get_img_path(){
		return img_path;
	}
	//url
	public void Set_url(String url){
		this.url=url;
	}
	public String Get_url(){
		return url;
	}
//////////////////////////////////////////////////////////////////////////////////	
	public ImageUrl() {
		super();
		this.idn = "";
		this.uid = "";
		this.title = "";
		this.effname = "";
		this.img_path = "";
		this.url = "";
	}

	// ����һ��������
	public ImageUrl(
			int DB_ID, 
			String idn,
			String uid,
			String title,
			String effname,
			String img_path,
			String url
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.idn = idn;
		this.uid = uid;
		this.title = title;
		this.effname = effname;
		this.img_path = img_path;
		this.url = url;
	}
	//����������
	public ImageUrl(
			String idn,
			String uid,
			String title,
			String effname,
			String img_path,
			String url
			) {
		////////////////////////
		this.idn = idn;
		this.uid = uid;
		this.title = title;
		this.effname = effname;
		this.img_path = img_path;
		this.url = url;
	}
 
}
