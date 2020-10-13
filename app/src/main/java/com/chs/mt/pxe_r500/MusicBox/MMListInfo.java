package com.chs.mt.pxe_r500.MusicBox;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MMListInfo {
    public  String USBIDST = "";
    int DB_ID=0;
    //歌曲名字
    public  String FileName = "";
    //歌曲格式
    public  int Form = 0;
    public  String FormSt = "";
    //歌曲排号
    public  int ID = 0;
    //Cur Music
    public  int CurID = 0;
    public  int CurFileID = 0;
    //All Music total
    public  int Total = 0;
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
    //风格类型
    public  String ID3_Genre = "";
    public  boolean boolSel = false;

    public void resetData(){
        USBIDST = "";
        DB_ID=0;
        boolSel = false;
        //歌曲名字
        FileName = "";
        //歌曲格式
        Form = 0;
        FormSt = "";
        //歌曲排号
        ID = 0;
        CurFileID = 0;
        //Cur Music
        CurID = 0;
        //All Music total
        Total = 0;

        //标题
        ID3_Title = "";
        //作者
        ID3_Artist = "";
        //专集
        ID3_Album = "";
        //出品年代
        ID3_Year = "";
        //备注
        ID3_Comment = "";
        //风格类型
        ID3_Genre = "";
    }
    public MMListInfo(
            int DB_ID,
            int CurID,
            int Total,
            String FileName,
            int Form,
            String FormSt,
            int CurFileID,
            String ID3_Title,
            String ID3_Artist,
            String ID3_Album,
            String ID3_Year,
            String ID3_Comment,
            String ID3_Genre,
            String USBIDST
    ) {
        super();
        this.DB_ID = DB_ID;
        this.CurID = CurID;
        this.Total = Total;
        this.FileName = FileName;
        this.Form = Form;
        this.FormSt = FormSt;
        this.CurFileID = CurFileID;
        this.ID3_Title = ID3_Title;
        this.ID3_Artist = ID3_Artist;
        this.ID3_Album = ID3_Album;
        this.ID3_Year = ID3_Year;
        this.ID3_Comment = ID3_Comment;
        this.ID3_Genre = ID3_Genre;
        this.USBIDST = USBIDST;
    }

    public MMListInfo( ) {
        super();
        resetData();
    }
}
