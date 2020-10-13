package com.chs.mt.pxe_r500.bean;
import java.util.ArrayList;
import java.util.List;

public class VenderOption {
	public static  final String LASTVERSION = "lastversion";
	public static  final String VERSION_URL= "version_url";
	
	public List<CarBrands> List_CarBrands = new ArrayList<CarBrands>();
	public List<CarTypes> List_CarTypes = new ArrayList<CarTypes>();
	public List<MacTypes> List_MacTypes = new ArrayList<MacTypes>();
	public String types = "";//"1": "��������","2": "˽������"

	private String lastversion = "";
	private String version_url= "";
	
	///////////////////////////////////////////////////////////////////////
	public VenderOption() {
		this.lastversion = "";
		this.version_url = "";
		this.types = "";
	}
	///////////////////////////////////////////////////////////////////////
	public void Set_lastversion(String st){
		lastversion=st;
	}
	public String Get_lastversion(){
		return this.lastversion;
	}

	public void Set_version_url(String st){
		version_url=st;
	}
	public String Get_version_url(){
		return this.version_url;
	}
	
	
	
}
