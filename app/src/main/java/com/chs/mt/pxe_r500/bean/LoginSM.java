package com.chs.mt.pxe_r500.bean;



public class LoginSM {
	
	public static final String T_ID = "t_id";
	
	public static final String T_NAME = "name";
	public static final String T_PASSWORD = "password";
	public static final String T_RE_PASSWORD = "re_password";
	public static final String T_AUTO_LOGIN = "auto_login";
	public static final String T_RECENTLY = "recently";

	private int id;	//���ݿ���
	private String name = "";
	private String password = "";
	private String re_password = "";
	private String auto_login = "";
	private String recently = "";
	///////////////////////////////////////////////////////////////////////
	//ID	 id
	public void Set_id(int id){
		this.id=id;
	}
	public int Get_id(){
		return id;
	}
	//name
	public void Set_name(String name){
		this.name=name;
	}
	public String Get_name(){
		return name;
	}
	
	// password
	public void Set_password(String st){
		this.password=st;
	}
	public String Get_password(){
		return password;
	}
	
	// re_password
	public void Set_re_password(String re_password){
		this.re_password=re_password;
	}
	public String Get_re_password(){
		return re_password;
	}
	
	
	//auto_login
	public void Set_auto_login(String auto_login){
		this.auto_login=auto_login;
	}
	public String Get_auto_login(){
		return auto_login;
	}
	
	//recently
	public void Set_recently(String recently){
		this.recently=recently;
	}
	public String Get_recently(){
		return recently;
	}
//////////////////////////////////////////////////////////////////////////////////	
	public LoginSM() {
		super();
		this.name = "";
		this.password = "";
		this.re_password = "";
		this.auto_login = "";
		this.recently = "";
	}
	
	// ����һ��������
	public LoginSM(
			int DB_ID, 
			String name,
			String password,
			String re_password,
			String auto_login,
			String recently
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.name = name;
		this.password = password;
		this.re_password = re_password;
		this.auto_login = auto_login;
		this.recently = recently;
	}

	//����������
	public LoginSM(
			String name,
			String password,
			String re_password,
			String auto_login,
			String recently
			) {
		////////////////////////
		this.name = name;
		this.password = password;
		this.re_password = re_password;
		this.auto_login = auto_login;
		this.recently = recently;
	}
 
}
