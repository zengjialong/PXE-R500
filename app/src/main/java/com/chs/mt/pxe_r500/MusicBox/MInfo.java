package com.chs.mt.pxe_r500.MusicBox;

import com.chs.mt.pxe_r500.datastruct.MacCfg;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MInfo {
    public  Boolean UPDATE = false;
    public  Boolean UPDATELIST = false;
    public  Boolean BoolHaveUHost = false;
    public  Boolean BoolHaveUPdateList = true;
    public  Boolean BoolHaveUPdateID3 = false;
    public  Boolean UPDATESONGSINList = false;
    public  Boolean BoolHaveUPdateStates = false;
//    public  Boolean BoolHaveManualPause = true;
    public  int UpdateListStart=1;
    public  int UpdateListNum=5;

    //U盘识别
    public  byte[] USBID = new byte[10];
    public  String USBIDST = "";
    //文件列表File
    public  int FileTotal = 0;
    public  int UpdateFileListStart = 1;
    public  int UpdateFileListNum = 5;
    public  int UpdateFileOfMusicListNum = 0;//当前要更新的文件列表
    public  Boolean BoolHaveUpdateFileTotal = false;
    public  Boolean BoolHaveUPdateFileList = true;

    public  Boolean BoolMLDialogCanShow = false;
    public  Boolean BoolFLDialogCanShow = false;

    public  int ap_type = 0;//当前应用类型
    public  int max_volume = 0;//最大音量
    public  int min_volume = 0;//最小音量
    public  int cur_volume = 0;//当前音量
    public  int cur_eqtype = 0;//当前EQ模式
    public  int mute_flag = 0;//是否静音
    public  int battery_state = 0;//当前电池状态
    public  int battery_value = 0;//当前电池电量0-4
    public  int sdcard_in = 0;//当前SD卡状态，无插卡，插卡，卡播放
    public  int uhost_in = 0;//当前UHost状态，无UHost，插UHost，UHost播放
    public  int usb_in = 0;//当前UHost状态，无usb，连接usb，连接充电适配线
    public  int linein_in = 0;//是否连接LINEIN线
    public  int hp_in = 0;//是否连接耳机
    public  int config_flag = 0;//是否完成配置
    public  int alarm_clock = 0;//是否正在闹钟
    public  int app_argv = 0;//当前应用完成配置时，传递给APK的应用相关参数。
    public  int dialog_type = 0;//对话框类型
    public  int dialog_button_type = 0;//对话框按键组合类型
    public  int dialog_desc_id = 0;//对话框描述信息ID
    public  int dialog_active_default = 0;//默认激活按键项
    public  int dae_mode = 0;//当前数字音效模式0：无，1：EQ音效，2：DAE音效
    public  int dae_option = 0;//bit0-VBASS开 关，bit1-Treble开 关，bit2-虚拟环绕开 关
    public  int reserve_bytes = 0;//RESERVE

    public  int repeat_mode = 0;//循环模式
    public  int play_status = 0;//播放状态
    public  int ab_status = 0;//AB复读状态
    public  int fast_status = 0;//快进状态
    public  int err_status = 0;//错误状态
    public  int lyric = 0;//歌词存在标志
    public  int file_switch = 0;//文件切换标志
    public  int reserve = 0;//
    public  int cur_time = 0;//当前播放时间
    public  int total_time = 0;//歌曲总时间
    public  int file_seq = 0;//当前歌曲序号
    public  int file_total = 0;//歌曲总数

    //0:自动切换推送保持现有的模式（推送模式优先）。
    //1：固定为推送模式。
    //2：固定为U盘模式。
    public  int PUMode = 0;

    //蓝牙推送
    public  Boolean BoolPhoneBlueMusicPush = false;
    //歌曲名字
    public  String FileName = "";
    //歌曲格式
    public  int Form = 0;
    //播放模式-单曲，循环，随机
    public  int PlayMode = 0;
    //播放暂停
    //public  int PlayPause = 1;
    //当前播放时间
    public  long PlayTime = 0;
    public  String PlayTimeSt = "00.00";
    //歌曲总时间
    public  long TotalTime = 0;
    public  String TotalTimeSt = "00.00";

    //当前音量
    public  int MaxVolume = 31;
    public  int Volume;

    //歌曲排号
    public  int ID = 0;
    //Cur Music
    public  int CurID = 0;
    //All Music total
    public  int Total = 0;

    //0-MusicType 0-mp3,1-wma,2-wav,3-flac,4-ape,5-mp3,6-mp3
    public  int    ID3_Type = 10;
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

    public void resID3Info(){
//        //标题
//        ID3_Title = "";
//        //作者
//        ID3_Artist = "";
//        //专集
//        ID3_Album = "";
//        //出品年代
//        ID3_Year = "";
//        //备注
//        ID3_Comment = "";
//        //风格类型
//        ID3_Genre = "";
    }

    public void resetData(){
        for(int i=0;i<10;i++){
            USBID[i]=0;
        }
        USBIDST = "";
        UPDATE = false;
        UPDATELIST = false;
        BoolHaveUHost = false;
        BoolHaveUPdateList = true;
        BoolHaveUPdateID3 = false;
        UPDATESONGSINList = false;
        BoolHaveUPdateStates = false;
//        BoolHaveManualPause = true;
        UpdateListStart=1;
        UpdateListNum=5;
        //0:自动切换推送保持现有的模式（推送模式优先）。
        //1：固定为推送模式。
        //2：固定为U盘模式。
        PUMode = 0;

        ap_type = 0;//当前应用类型
        max_volume = 0;//最大音量
        min_volume = 0;//最小音量
        cur_volume = 0;//当前音量
        cur_eqtype = 0;//当前EQ模式
        mute_flag = 0;//是否静音
        battery_state = 0;//当前电池状态
        battery_value = 0;//当前电池电量0-4
        sdcard_in = 0;//当前SD卡状态，无插卡，插卡，卡播放
        uhost_in = 0;//当前UHost状态，无UHost，插UHost，UHost播放
        usb_in = 0;//当前UHost状态，无usb，连接usb，连接充电适配线
        linein_in = 0;//是否连接LINEIN线
        hp_in = 0;//是否连接耳机
        config_flag = 0;//是否完成配置
        alarm_clock = 0;//是否正在闹钟
        app_argv = 0;//当前应用完成配置时，传递给APK的应用相关参数。
        dialog_type = 0;//对话框类型
        dialog_button_type = 0;//对话框按键组合类型
        dialog_desc_id = 0;//对话框描述信息ID
        dialog_active_default = 0;//默认激活按键项
        dae_mode = 0;//当前数字音效模式0：无，1：EQ音效，2：DAE音效
        dae_option = 0;//bit0-VBASS开 关，bit1-Treble开 关，bit2-虚拟环绕开 关
        reserve_bytes = 0;//RESERVE

        repeat_mode = 0;//循环模式
        play_status = MDef.PLAY_STATUS_PAUSE;//播放状态
        ab_status = 0;//AB复读状态
        fast_status = 0;//快进状态
        err_status = 0;//错误状态
        lyric = 0;//歌词存在标志
        file_switch = 0;//文件切换标志
        reserve = 0;//
        cur_time = 0;//当前播放时间
        total_time = 0;//歌曲总时间
        file_seq = 0;//当前歌曲序号
        file_total = 0;//歌曲总数

        //蓝牙推送
        BoolPhoneBlueMusicPush = false;
        if(!MacCfg.BOOL_FlashMusicList){
            //歌曲名字
            FileName = "";
            //歌曲格式
            Form = 0;
            //播放模式-单曲，循环，随机
            PlayMode = 0;
            //播放暂停
//        PlayPause = 1;
            //当前播放时间
            PlayTime = 0;
            PlayTimeSt = "00.00";
            //歌曲总时间
            TotalTime = 0;
            TotalTimeSt = "00.00";

            //当前音量
            MaxVolume = 31;
            Volume=0;

            //歌曲排号
            ID = 10;
            //Cur Music
            CurID = 0;
            //All Music total
            Total = 0;

            ID3_Type = 0;
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
        }else {
            MacCfg.BOOL_FlashMusicList = false;
        }



        FileTotal = 0;
        UpdateFileListStart = 1;
        UpdateFileListNum = 5;
        BoolHaveUpdateFileTotal = false;
        BoolHaveUPdateFileList = true;
        UpdateFileOfMusicListNum = 0;//当前要更新的文件列表
    }


}
