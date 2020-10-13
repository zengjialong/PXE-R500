package com.chs.mt.pxe_r500.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SEff_ListDat implements Serializable {
	public static final String LIST_DROP_DOWN_OPEN =  "1";
	public static final String LIST_DROP_DOWN_CLOSE = "0";
	
	public static final String LIST_HEAD_HIDE  =  "0";
	public static final String LIST_DROP_WSEL  =  "1";
	public static final String LIST_DROP_SELED =  "2";
	
	public int id;
	public String name;
	public String filePath;
	public String user;
	public String upload_time;
	public String fravorite;
	public String love;
	public String sel;//0:����ʾѡ���1����ʾѡ���2��ѡ���ѡ��
	public String isOpen;
	public String fileType;
    public SEff_ListDat(
    		int id,
    		String name, 
    		String filePath,
    		String user, 
    		String upload_time,
    		String fravorite,
    		String love,
    		String sel,
    		String isOpen,
    		String fileType) {
        super();
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.user = user;
        this.upload_time = upload_time;
        this.fravorite = fravorite;
        this.love = love;
        this.sel = sel;
        this.isOpen = isOpen;
        this.fileType = fileType;
    }
    ///
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    ///
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    ///
    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return this.user;
    }    
    ///
    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
    public String getUpload_time() {
        return this.upload_time;
    }
    ///
    public void setFravorite(String fravorite) {
        this.fravorite = fravorite;
    }
    public String getFravorite() {
        return this.fravorite;
    }
    ///
    public void setLove(String love) {
        this.love = love;
    }
    public String getLove() {
        return this.love;
    }
    ///
    public void setSel(String sel) {
        this.sel = sel;
    }
    public String getSel() {
        return this.sel;
    }
    //
    public void setOpen(String isOpen) {
        this.isOpen = isOpen;
    }
    public String isOpen() {
        return this.isOpen;
    }
    public void setfilePath(String filePath) {
    	this.filePath = filePath;
    }
    public String getfilePath() {
        return this.filePath;
    }
    
    public void setfileType(String fileType) {
    	this.fileType = fileType;
    }
    public String getfileType() {
        return this.fileType;
    }
} 
