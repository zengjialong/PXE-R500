package com.chs.mt.pxe_r500.bean;



public class MacTypes {
	
	public static final String T_ID = "t_id";
	
	public static final String T_CID = "cid";
	public static final String T_NAME = "name";
	
	private int id;	//���ݿ���
	//cid
	private String cid = "";
	// name
	private String name = "";
	
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
	
	//name
	public void Set_name(String name){
		this.name=name;
	}
	public String Get_name(){
		return name;
	}
	
//////////////////////////////////////////////////////////////////////////////////	
	public MacTypes() {
		super();
		this.cid = "";
		//�����û� uid
		this.name = "";
		
	}
	// ����һ��������
	public MacTypes(int DB_ID, String cid,String name) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.cid = cid;
		//�����û� uid
		this.name = name;
	}
	//����������
	public MacTypes(String cid,String name) {
		////////////////////////
		this.cid = cid;
		//�����û� uid
		this.name = name;
	}
 
}
