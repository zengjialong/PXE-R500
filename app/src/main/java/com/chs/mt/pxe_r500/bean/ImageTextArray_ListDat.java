package com.chs.mt.pxe_r500.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageTextArray_ListDat implements Serializable {
	private int id;
	private String title="";
	private String[] imagePath = new String[5];
	private String[] text1 = new String[5];
	private String[] text2 = new String[5];
	private String[] text3 = new String[5];
	private String[] text4 = new String[5];
	private String[] text5 = new String[5];

	
    public ImageTextArray_ListDat(
    		int id,
    		String title,
    		int index,
    		String imagePath, 
    		String text1, 
    		String text2, 
    		String text3,
    		String text4,
    		String text5) {
        super();
        this.id = id;
        this.title=title;
        this.imagePath[index] = imagePath;
        this.text1[index] = text1;
        this.text2[index] = text2;
        this.text3[index] = text3;
        this.text4[index] = text4;
        this.text5[index] = text5;
    }
    ///
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
  ///
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
  ///
    public void setImagePath(int index,String imagePath) {
        this.imagePath[index] = imagePath;
    }
    public String getImagePath(int index) {
        return this.imagePath[index];
    }
    ///
    public void setText1(int index,String name) {
        this.text1[index] = name;
    }
    public String getText1(int index) {
        return this.text1[index];
    }
    ///
    public void setText2(int index,String name) {
        this.text2[index] = name;
    }
    public String getText2(int index) {
        return this.text2[index];
    }
    ///
    public void setText3(int index,String name) {
        this.text3[index] = name;
    }
    public String getText3(int index) {
        return this.text3[index];
    }
    ///
    public void setText4(int index,String name) {
        this.text4[index] = name;
    }
    public String getText4(int index) {
        return this.text4[index];
    }
    ///
    public void setText5(int index,String name) {
        this.text5[index] = name;
    }
    public String getText5(int index) {
        return this.text5[index];
    }

}
