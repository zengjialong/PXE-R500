package com.chs.mt.pxe_r500.bean;

/**
 * Created by Administrator on 2017/10/27.
 */

public class DSP_AI {
    //状态码：200请求成功，24400出错
    public  String code = "";
    public  String message = "";
    public  String AgentID = "";
    public  String softtype = "";
    public  String macName = "";
    public  String macID = "";
    public  String ip = "";
    public  String ctime = "";

    //广告开关（1为有广告，0为没广告，后面将没有字段；注意做判断）
    public  String Ad_Status = "";
    // 广告标题
    public  String Ad_Title = "";
    //广告图片
    public  String Ad_Image_Path = "";
    //点击广告 调整到浏览器的广告地址，如果没有广告地址则不做点击跳转，注意做判断
    public  String Ad_URL = "";
    //APP端点击关闭调用
    public  String Ad_Close_URL = "";
    //广告AdID
    public  String AdID = "";

    //软件升级开关（1为开启软件升级，0则关闭升级，后面将没字段）
    public  String Upgrade_Status = "";
    //升级说明
    public  String Upgrade_Instructions = "";
    //将要升级到的软件版本
    public  String Upgrade_Latest_Version = "";
    // 新版本软件下载地址
    public  String Upgrade_URL = "";
}
