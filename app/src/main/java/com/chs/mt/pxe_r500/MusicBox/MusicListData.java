package com.chs.mt.pxe_r500.MusicBox;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MusicListData implements Serializable {

    //歌曲排号
    public  int ID = 0;
    //歌曲名字
    public  String FileName = "";
    //标题
    public  String ID3_Title = "";
    //作者
    public  String ID3_Artist = "";
    //专集
    public  String ID3_Album = "";
    //出品年代
    public  String ID3_Year = "";
    //备注
    public  String ID3_Comment = "";
    //类型
    public  String ID3_Genre = "";


    public boolean boolSel =false;
    public boolean boolPlay=false;
} 
