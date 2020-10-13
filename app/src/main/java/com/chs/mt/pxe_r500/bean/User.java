package com.chs.mt.pxe_r500.bean;
import android.graphics.Bitmap;

public class User {
	public static final String PRIVATE = "2";
	public static final String PUBLIC = "1";
	//�û�ID
	private String userId = "";
	//�û���¼����
	private String userLandingName= "";
	//�û��ǳ�
	private String userName = "";
	//�û�ͼ����ַ
	private String avatar = "";
	//�û�ͼ��
	private Bitmap userBitmap;
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
	private String carBrand = "";
	private String carType = "";
	private String macType = "";
	private String sign = "";
	//�û�״̬0����δ��¼����1���Ѿ���¼����2������
	private String onLine = "0";

	private String carBrand_Cid = "";
	private String carType_Cid = "";
	private String macType_Cid = "";

	private String seff_name = "";
	private String seff_detials = "";
	
	private String seffdata_type = "1";
	private String feedback = "";
	private String fileName = "";
	private String password = "";
	
	private String version = "";
	public User() {
		init();
	}

	private void init() {

		this.userId = "";
		this.userLandingName= "";
		this.userName = "";
		this.avatar = "";
		this.email = "";
		this.phone = "";
		this.sex = "";
		this.status = "";
		this.addTime = "";
		this.lastTime = "";
		this.sessionId = "";
		this.carBrand = "";
		this.carType = "";
		this.macType = "";
		this.sign = "";
		
		this.carBrand_Cid = "";
		this.carType_Cid = "";
		this.macType_Cid = "";
		this.seff_name = "";
		this.seff_detials = "";
		this.seffdata_type = "1";
		this.feedback = "";
		this.fileName = "";
		this.password = "";
		this.version = "";
		onLine = "0";
	}

	///////////////////////////////////////////////////////////////////////
	//�û�ID
	public void Set_userId(String m_userId){
		this.userId=m_userId;
	}
	//�û���¼����
	public void Set_userLandingName(String m_userLandingName){
		this.userLandingName=m_userLandingName;
	}
	//�û��ǳ�
	public void Set_userName(String m_userName){
		this.userName=m_userName;
	}
	//�û�ͼ����ַ
	public void Set_avatar(String m_avatar){
		this.avatar=m_avatar;
	}
	//�û�ͼ��
	public void Set_userBitmap(Bitmap m_userBitmap){
		this.userBitmap=m_userBitmap;
	}
	//�û�email
	public void Set_email(String m_email){
		this.email=m_email;
	}
	//�û��绰
	public void Set_phone(String m_phone){
		this.phone=m_phone;
	}
	//�û��Ա�
	public void Set_sex(String m_sex){
		this.sex=m_sex;
	}
	//�û�״̬��
	public void Set_status(String m_status){
		this.status=m_status;
	}
	//�û�����ʱ�䣿
	public void Set_addTime(String m_addTime){
		this.addTime=m_addTime;
	}
	//�û����ʱ�䣿
	public void Set_lastTime(String m_lastTime){
		this.lastTime=m_lastTime;
	}
	//�û����ID
	public void Set_sessionId(String m_sessionId){
		this.sessionId=m_sessionId;
	}
	//�û�����״̬
	public void Set_onLine(String m_onLine){
		this.onLine=m_onLine;
	}
	
	public void Set_carBrand(String carBrand){
		this.carBrand=carBrand;
	}
	public void Set_carType(String carType){
		this.carType=carType;
	}
	public void Set_macType(String macType){
		this.macType=macType;
	}
	public void Set_sign(String sign){
		this.sign=sign;
	}
	
	
	public void Set_carBrand_Cid(String carBrand_Cid){
		this.carBrand_Cid=carBrand_Cid;
	}
	public void Set_carType_Cid(String carType_Cid){
		this.carType_Cid=carType_Cid;
	}
	public void Set_macType_Cid(String macType_Cid){
		this.macType_Cid=macType_Cid;
	}
	public void Set_seff_name(String seff_name){
		this.seff_name=seff_name;
	}
	public void Set_seff_detials(String seff_detials){
		this.seff_detials=seff_detials;
	}
	public void Set_seffdata_type(String seffdata_type){
		this.seffdata_type=seffdata_type;
	}
	public void Set_feedback(String feedback){
		this.feedback=feedback;
	}
	public void Set_fileName(String fileName){
		this.fileName=fileName;
	}
	public void Set_password(String password){
		this.password=password;
	}
	
	
	public void Set_version(String version){
		this.version=version;
	}
	
	///////////////////////////// GET //////////////////////////////////////////
	//�û�ID
	public String Get_userId(){
		return this.userId;
	}
	//�û���¼����
	public String Get_userLandingName(){
		return this.userLandingName;
	}
	//�û��ǳ�
	public String Get_userName(){
		return this.userName;
	}
	//�û�ͼ����ַ
	public String Get_avatar(){
		return this.avatar;
	}
	//�û�ͼ��
	public Bitmap Get_userBitmap(){
		return this.userBitmap;
	}
	//�û�email
	public String Get_email(){
		return this.email;
	}
	//�û��绰
	public String Get_phone(){
		return this.phone;
	}
	//�û��Ա�
	public String Get_sex(){
		return this.sex;
	}
	//�û�״̬��
	public String Get_status(){
		return this.status;
	}
	//�û�����ʱ�䣿
	public String Get_addTime(){
		return this.addTime;
	}
	//�û����ʱ�䣿
	public String Get_lastTime(){
		return this.lastTime;
	}
	//�û����ID
	public String Get_sessionId(){
		return this.sessionId;
	}
	//�û�����״̬
	public String Get_onLine(){
		return this.onLine;
	}
	
	public String Get_carBrand(){
		return this.carBrand;
	}
	public String Get_carType(){
		return this.carType;
	}
	public String Get_macType(){
		return this.macType;
	}
	public String Get_sign(){
		return this.sign;
	}
	//
	public String Get_carBrand_Cid(){
		return this.carBrand_Cid;
	}
	public String Get_carType_Cid(){
		return this.carType_Cid;
	}
	public String Get_macType_Cid(){
		return this.macType_Cid;
	}
	public String Get_seff_name(){
		return this.seff_name;
	}
	public String Get_seff_detials(){
		return this.seff_detials;
	}
	public String Get_seffdata_type(){
		return this.seffdata_type;
	}
	public String Get_feedback(){
		return this.feedback;
	}
	public String Get_fileName(){
		return this.fileName;
	}
	public String Get_password(){
		return this.password;
	}

	public String Get_version(){
		return this.version;
	}
	
	public void clean() {

		this.userId = "";
		this.userLandingName= "";
		this.userName = "";
		this.avatar = "";
		this.email = "";
		this.phone = "";
		this.sex = "";
		this.status = "";
		this.addTime = "";
		this.lastTime = "";
		this.sessionId = "";
		this.carBrand = "";
		this.carType = "";
		this.macType = "";
		this.sign = "";
		this.carBrand_Cid = "";
		this.carType_Cid = "";
		this.macType_Cid = "";
		this.seff_name = "";
		this.seff_detials = "";
		this.seffdata_type = "1";
		this.feedback = "";
		this.fileName = "";
		this.password = "";
		this.version = "";
		onLine = "0";
	}
	
	
}
