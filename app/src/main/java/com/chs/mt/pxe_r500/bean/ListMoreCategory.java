package com.chs.mt.pxe_r500.bean;



public class ListMoreCategory {
	
	public static final String T_ID = "t_id";
	
	public static final String T_IC = "icon";
	public static final String T_TITLE = "title";
	public static final String T_MORE_URL = "more_url";
	
	private int id;	//���ݿ���
	private String icon = "";
	private String title = "";
	private String more_url = "";

	///////////////////////////////////////////////////////////////////////
	//ID	 id
	public void Set_id(int id){
		this.id=id;
	}
	public int Get_id(){
		return id;
	}
	
	// icon
	public void Set_icon(String icon){
		this.icon=icon;
	}
	public String Get_icon(){
		return icon;
	}
	
	//title
	public void Set_title(String title){
		this.title=title;
	}
	public String Get_title(){
		return title;
	}
	//more_url
	public void Set_more_url(String more_url){
		this.more_url=more_url;
	}
	public String Get_more_url(){
		return more_url;
	}

//////////////////////////////////////////////////////////////////////////////////	
	public ListMoreCategory() {
		super();
		this.icon = "";
		this.title = "";
		this.more_url = "";
	}

	// ����һ��������
	public ListMoreCategory(
			int DB_ID, 
			String icon,
			String title,
			String more_url
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.icon = icon;
		this.title = title;
		this.more_url = more_url;
	}
	//����������
	public ListMoreCategory(
			String icon,
			String title,
			String more_url
			) {
		////////////////////////
		this.icon = icon;
		this.title = title;
		this.more_url = more_url;
	}
 
}
