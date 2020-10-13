package com.chs.mt.pxe_r500.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageText_Data implements Serializable {
	public int id;
	public String imagePath;
	public String tv_item;
	public String cid;
	public String text3;
	public String text4;
	public String text5;
	public String showImage;
    public ImageText_Data(
    		int id,
    		String imagePath, 
    		String tv_item, 
    		String cid, 
    		String text3,
    		String text4,
    		String text5,
    		String showImage) {
        super();
        this.id = id;
        this.imagePath = imagePath;
        this.tv_item = tv_item;
        this.cid = cid;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
        this.showImage = showImage;
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
    public void set_tv_item(String name) {
        this.tv_item = name;
    }
    public String get_tv_item() {
        return this.tv_item;
    }
    ///
    public void set_cid(String name) {
        this.cid = name;
    }
    public String get_cid() {
        return this.cid;
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
    ///
    public void setShowImage(String name) {
        this.showImage = name;
    }
    public String getShowImage() {
        return this.showImage;
    }
}
