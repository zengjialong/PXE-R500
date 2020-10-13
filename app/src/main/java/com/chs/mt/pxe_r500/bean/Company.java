package com.chs.mt.pxe_r500.bean;

public class Company {

	private String company_name = "";
	private String company_tel = "";
	private String company_contacts = "";
	private String company_web = "";
	private String company_weixin = "";
	private String company_qq = "";
	private String company_briefing_en = "";
	private String company_briefing_zh = "";
	private String company_brand = "";

	///////////////////////////////////////////////////////////////////////
	//company_name
	public void Set_company_name(String st){
		this.company_name=st;
	}
	public String Get_company_name(){
		return company_name;
	}
	//company_tel
	public void Set_company_tel(String st){
		this.company_tel=st;
	}
	public String Get_company_tel(){
		return company_tel;
	}
	//company_contacts
	public void Set_company_contacts(String st){
		this.company_contacts=st;
	}
	public String Get_company_contacts(){
		return company_contacts;
	}
	//company_web
	public void Set_company_web(String st){
		this.company_web=st;
	}
	public String Get_company_web(){
		return company_web;
	}
	//company_weixin
	public void Set_company_weixin(String st){
		this.company_weixin=st;
	}
	public String Get_company_weixin(){
		return company_weixin;
	}
	//company_qq
	public void Set_company_qq(String st){
		this.company_qq=st;
	}
	public String Get_company_qq(){
		return company_qq;
	}
	//company_briefing_en
	public void Set_company_briefing_en(String st){
		this.company_briefing_en=st;
	}
	public String Get_company_briefing_en(){
		return company_briefing_en;
	}
	//company_briefing_zh
	public void Set_company_briefing_zh(String st){
		this.company_briefing_zh=st;
	}
	public String Get_company_briefing_zh(){
		return company_briefing_zh;
	}
	//company_brand
	public void Set_company_brand(String st){
		this.company_brand=st;
	}
	public String Get_company_brand(){
		return company_brand;
	}
	
	public Company() {
		super();
		this.company_name="";
		this.company_tel="";
		this.company_contacts="";
		this.company_web="";
		this.company_weixin="";
		this.company_qq="";
		this.company_briefing_en="";
		this.company_briefing_zh="";
		this.company_brand="";
	}
	
	public Company(
			String company_name,
			String company_tel,
			String company_contacts,
			String company_web,
			String company_weixin,
			String company_qq,
			String company_briefing_en,
			String company_briefing_zh,
			String company_brand
			) {
		super();
		this.company_name=company_name;
		this.company_tel=company_tel;
		this.company_contacts=company_contacts;
		this.company_web=company_web;
		this.company_weixin=company_weixin;
		this.company_qq=company_qq;
		this.company_briefing_en=company_briefing_en;
		this.company_briefing_zh=company_briefing_zh;
		this.company_brand=company_brand;
	}
 
}
