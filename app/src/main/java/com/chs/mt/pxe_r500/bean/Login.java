package com.chs.mt.pxe_r500.bean;

import java.util.ArrayList;

public class Login {  

	//code
	private int code = 0;
	//message
	private String message = "";
	//data
	private ArrayList<UserData> data;	

	///////////////////////////////////////////////////////////////////////
	//code
	public void SetCode(int code){
		this.code=code;
	}
	public int GetCode(){
		return code;
	}
	//message
	public void SetMessage(String message){
		this.message=message;
	}
	public String GetMessage(){
		return message;
	}
	//data
	public void SetData(ArrayList<UserData> data){
		this.data=data;
	}
	public ArrayList<UserData> Get_userId(){
		return data;
	}
}
