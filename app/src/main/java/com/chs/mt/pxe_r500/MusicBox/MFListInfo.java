package com.chs.mt.pxe_r500.MusicBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MFListInfo {
    int DB_ID = 0;
    public  boolean sel = false;
    //文件夹名字
    public  String FileName = "";
    //排号
    public  int CurDirID = 0;
    //All 文件夹 total
    public  int TotalFiles = 0;
    public  int TotalSongs = 0;
    //起始和结束序号
    public  int SongsStartID = 0;
    public  int SongsEndID = 0;
    public  String USBIDST = "";
    public  List<MMListInfo> CurMusicList = new ArrayList<MMListInfo>();

    int ListType = MDef.ListType_File;//列表类型
    public  String MName = "";
    public  String MArtist = "";
    public  int PlayID = 0;

    public void resetData(){
        ListType = MDef.ListType_File;//列表类型
        MName = "";
        MArtist = "";
        PlayID = 0;

        DB_ID=0;
        sel = false;
        //文件夹名字
        FileName = "";
        //排号
        CurDirID = 0;
        //All 文件夹 total
        TotalFiles = 0;
        TotalSongs = 0;
        //起始和结束序号
        SongsStartID = 0;
        SongsEndID = 0;
        USBIDST = "";
        CurMusicList.clear();
        CurMusicList.removeAll(CurMusicList);
    }

    public MFListInfo(
            int DB_ID,
            int CurDirID,
            String FileName,
            int TotalFiles,
            int TotalSongs,
            int SongsStartID,
            int SongsEndID,
            String USBIDST
    ) {
        super();
        this.DB_ID = DB_ID;
        this.CurDirID   = CurDirID;
        this.FileName   = FileName;
        this.TotalFiles = TotalFiles;
        this.TotalSongs = TotalSongs;
        this.SongsStartID = SongsStartID;
        this.SongsEndID = SongsEndID;
        this.USBIDST = USBIDST;
    }

    public MFListInfo( ) {
        super();
        resetData();
    }

}
