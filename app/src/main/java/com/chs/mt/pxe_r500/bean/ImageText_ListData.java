package com.chs.mt.pxe_r500.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageText_ListData implements Serializable {
	public int id;
	public String imagePath;
	public String text1;
	public String text2;
	public String text3;
	public String text4;
	public String text5;
	
    public ImageText_ListData(
    		int id,
    		String imagePath, 
    		String text1, 
    		String text2, 
    		String text3,
    		String text4,
    		String text5) {
        super();
        this.id = id;
        this.imagePath = imagePath;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
    }
    ///
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
  ///
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    ///
    public void setText1(String name) {
        this.text1 = name;
    }
    public String getText1() {
        return this.text1;
    }
    ///
    public void setText2(String name) {
        this.text2 = name;
    }
    public String getText2() {
        return this.text2;
    }
    ///
    public void setText3(String name) {
        this.text3 = name;
    }
    public String getText3() {
        return this.text3;
    }
    ///
    public void setText4(String name) {
        this.text4 = name;
    }
    public String getText4() {
        return this.text4;
    }
    ///
    public void setText5(String name) {
        this.text5 = name;
    }
    public String getText5() {
        return this.text5;
    }

}
