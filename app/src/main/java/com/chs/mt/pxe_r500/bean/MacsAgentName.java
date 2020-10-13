package com.chs.mt.pxe_r500.bean;



public class MacsAgentName {
	
	public static final String T_ID = "t_id";
	
	public static final String T_CID = "cid";
	public static final String T_MID = "mid";
	public static final String T_AgentID = "AgentID";
	public static final String T_CNAME = "cname";
	
	private int id;	//���ݿ���
	private String cid = "";
	private String mid = "";
	private String AgentID = "";
	private String cname = "";
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
	
	// mid
	public void Set_mid(String mid){
		this.mid=mid;
	}
	public String Get_mid(){
		return mid;
	}
	
	//AgentID
	public void Set_AgentID(String AgentID){
		this.AgentID=AgentID;
	}
	public String Get_AgentID(){
		return AgentID;
	}
	//img_path
	public void Set_cname(String cname){
		this.cname=cname;
	}
	public String Get_cname(){
		return cname;
	}
//////////////////////////////////////////////////////////////////////////////////	
	public MacsAgentName() {
		super();
		this.cid = "";
		this.mid = "";
		this.AgentID = "";
		this.cname = "";
	}

	// ����һ��������
	public MacsAgentName(
			int DB_ID, 
			String cid,
			String mid,
			String AgentID,
			String cname
			) {
		super();
		this.id = DB_ID;
		////////////////////////
		this.cid = cid;
		this.mid = mid;
		this.AgentID = AgentID;
		this.cname = cname;
	}
	//����������
	public MacsAgentName(
			String cid,
			String mid,
			String AgentID,
			String cname) {
		////////////////////////
		this.cid = cid;
		this.mid = mid;
		this.AgentID = AgentID;
		this.cname = cname;
	}
 
}
