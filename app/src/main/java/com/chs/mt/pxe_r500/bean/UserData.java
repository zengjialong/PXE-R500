package com.chs.mt.pxe_r500.bean;

public class UserData {

	//�û���Ϣ	
	//�û�ID
	private String userId = "";
	//�û��ǳ�
	private String userName = "";
	//�û�ͼ����ַ
	private String avatar = "";
	//�û�email
	private String email = "";
	//�û��绰
	private String phone = "";
	//�û��Ա�
	private String sex = "";
	//�û�״̬��
	private String status = "";
	//�û�����ʱ�䣿
	private String addTime = "";
	//�û����ʱ�䣿
	private String lastTime = "";
	//�û����ID
	private String sessionId = "";

	///////////////////////////////////////////////////////////////////////
	//�û�ID
	public void SetUserId(String m_userId){
		this.userId=m_userId;
	}
	public String GetUserId(){
		return userId;
	}
	//�û��ǳ�
	public void SetUserName(String m_userName){
		this.userName=m_userName;
	}
	public String GetUserName(){
		return userName;
	}
	//�û�ͼ����ַ
	public void SetAvatar(String m_avatar){
		this.avatar=m_avatar;
	}
	public String GetAvatar(){
		return avatar;
	}
	//�û�email
	public void SetEmail(String m_email){
		this.email=m_email;
	}
	public String GetEmail(){
		return email;
	}
	//�û��绰
	public void SetPhone(String m_phone){
		this.phone=m_phone;
	}
	public String GetPhone(){
		return phone;
	}
	//�û��Ա�
	public void SetSex(String m_sex){
		this.sex=m_sex;
	}
	public String GetSex(){
		return sex;
	}
	//�û�״̬��
	public void SetStatus(String m_status){
		this.status=m_status;
	}
	public String GetStatus(){
		return status;
	}
	//�û�����ʱ�䣿
	public void SetAddTime(String m_addTime){
		this.addTime=m_addTime;
	}
	public String GetAddTime(){
		return addTime;
	}
	//�û����ʱ�䣿
	public void SetLastTime(String m_lastTime){
		this.lastTime=m_lastTime;
	}
	public String GetLastTime(){
		return lastTime;
	}
	//�û����ID
	public void SetSessionId(String m_sessionId){
		this.sessionId=m_sessionId;
	}
	public String GetSessionId(){
		return sessionId;
	}

	public UserData(String userId, String userName, String avatar, String email, String phone,
			String sex, String status, String addTime, String lastTime, String sessionId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.avatar = avatar;
		this.email = email;
		this.phone = phone;
		this.userId = sex;
		this.userName = status;
		this.avatar = addTime;
		this.email = lastTime;
		this.phone = sessionId;
	}
	public UserData() {
		super();
		// TODO Auto-generated constructor stub
	}

 
}
