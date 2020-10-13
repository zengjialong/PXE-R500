package com.chs.mt.pxe_r500.bean;

public class LoginInfo {
	//code
	private int code = 0;
	//message
	private String message = "";
	//data
	private String data = "";
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
	//code
	public void SetCode(int code){
		this.code=code;
	}
	//message
	public void SetMessage(String message){
		this.message=message;
	}
	//data
	public void SetData(String data){
		this.data=data;
	}
	//�û�ID
	public void SetUserId(String m_userId){
		this.userId=m_userId;
	}
	//�û��ǳ�
	public void SetUserName(String m_userName){
		this.userName=m_userName;
	}
	//�û�ͼ����ַ
	public void SetAvatar(String m_avatar){
		this.avatar=m_avatar;
	}
	//�û�email
	public void SetEmail(String m_email){
		this.email=m_email;
	}
	//�û��绰
	public void SetPhone(String m_phone){
		this.phone=m_phone;
	}
	//�û��Ա�
	public void SetSex(String m_sex){
		this.sex=m_sex;
	}
	//�û�״̬��
	public void SetStatus(String m_status){
		this.status=m_status;
	}
	//�û�����ʱ�䣿
	public void SetAddTime(String m_addTime){
		this.addTime=m_addTime;
	}
	//�û����ʱ�䣿
	public void SetLastTime(String m_lastTime){
		this.lastTime=m_lastTime;
	}
	//�û����ID
	public void SetSessionId(String m_sessionId){
		this.sessionId=m_sessionId;
	}
	///////////////////////////// GET //////////////////////////////////////////
	//code
	public int GetCode(){
		return code;
	}
	//message
	public String GetMessage(){
		return message;
	}
	//data
	public String GetData(){
		return data;
	}
	//�û�ID
	public String Get_userId(){
		return userId;
	}
	//�û��ǳ�
	public String Get_userName(){
		return userName;
	}
	//�û�ͼ����ַ
	public String Get_avatar(){
		return avatar;
	}
	//�û�email
	public String Get_email(){
		return email;
	}
	//�û��绰
	public String Get_phone(){
		return phone;
	}
	//�û��Ա�
	public String Get_sex(){
		return sex;
	}
	//�û�״̬��
	public String Get_status(){
		return status;
	}
	//�û�����ʱ�䣿
	public String Get_addTime(){
		return addTime;
	}
	//�û����ʱ�䣿
	public String Get_lastTime(){
		return lastTime;
	}
	//�û����ID
	public String Get_sessionId(){
		return sessionId;
	}
	
	@Override
	public String toString(){
        return "FUCK"+this.phone + ", " + this.lastTime + ", " + this.sessionId;
    }
}
