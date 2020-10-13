package com.chs.mt.pxe_r500.MusicBox;

/**
 * Created by Administrator on 2017/10/20.
 */

public class MDef {
    public static final int MBCMDLENG = 16;

    public static final int MAGIC = 0x01fe;
    public static final int FALGS = 0x0000;
    public static final int CMDTYPE_ID = 0x0000;
    //CMDTYPE
    public static final int CMDTYPE_P = 'P';//用于应用切换这里的应用不同于AP这种概念的应用，准确说应该是功能切换。比如插卡播歌和U盘播歌是同一个MUSIC AP中的两个不同功能
    public static final int CMDTYPE_C = 'C';//用于简单动作控制 通过转发按键消息来发成目的，区别在于控制类定义使用了很多特殊的虚拟按键
    public static final int CMDTYPE_K = 'K';//用于模拟按键控制都是通过转发按键消息来发成目的，区别在于控制类定义使用了很多特殊的虚拟按键
    public static final int CMDTYPE_Q = 'Q';//用于获取状态信息 获取状态信息是异步的，上位机发送查询命令，下位机收到命令分发处理时异步应答
    public static final int CMDTYPE_A = 'A';//用于应答查询类的状态信息请求获取状态信息是异步的，上位机发送查询命令，下位机收到命令分发处理时异步应答
    public static final int CMDTYPE_R = 'R';//用于简单应答以确保命令处理与否用于非查询类命令的应答
    public static final int CMDTYPE_S = 'S';//用于复杂动作控制或设置除非带ACK，否则不需要应答
    public static final int CMDTYPE_B = 'B';//用于
    //应用切换类
    public static final int CMDID_P_PushSong  = 10;//蓝牙推歌
    public static final int CMDID_P_MusicPlay = 11;//音乐播放
    public static final int CMDID_P_LINEIN    = 12;//LINEIN
    public static final int CMDID_P_FM        = 13;//FM收音
    public static final int CMDID_P_Alarm     = 14;//闹钟应用
    public static final int CMDID_P_Card      = 15;//插卡播歌
    public static final int CMDID_P_UHost     = 16;//U盘播歌
    //控制类
    public static final int CMDID_C_Play      = 11;//播放11
    public static final int CMDID_C_Stop      = 12;//暂停12
    public static final int CMDID_C_Next      = 13;//下一曲
    public static final int CMDID_C_pre       = 14;//上一曲
    public static final int CMDID_C_MuteOff   = 15;//取消静音
    public static final int CMDID_C_ValAdd    = 16;//音量加
    public static final int CMDID_C_ValSUB    = 17;// 音量减
    public static final int CMDID_C_MuteON    = 18;// 静音
    public static final int CMDID_C_EQC       = 19;// EQ切换
    public static final int CMDID_C_LOOP      = 20;// LOOP切换
    public static final int CMDID_C_CLOCK     = 21;// CLOCK
    public static final int CMDID_C_NextDir   = 22;// 下一个目录
    public static final int CMDID_C_PreDir    = 23;// 上一个目录
    public static final int CMDID_C_FMSearch  = 35;// FM搜台
    public static final int CMDID_C_FMCancelSearch   = 36;// 取消FM搜台
    public static final int CMDID_C_FMNextCh   = 37;// 下一个频道
    public static final int CMDID_C_FMPreCh    = 38;// 上一个频道
    public static final int CMDID_C_FMMuteOn   = 39;// FM静音
    public static final int CMDID_C_BluetoothStop= 40;// 蓝牙断开连接
    public static final int CMDID_C_CallAnswer   = 41;// 接听电话
    public static final int CMDID_C_Call         = 42;// 挂断电话
    public static final int CMDID_C_CallRingOff  = 43;// 末号回拨
    public static final int CMDID_C_Snooze       = 44;// 闹钟Snooze
    public static final int CMDID_C_SnoozeOff    = 45;// 闹钟关掉

    public static final int CMDID_C_PUMode     = 0xC0;//曲目号点播
    //模拟按键
    public static final int CMDID_K_POWER       = 10;// POWER
    public static final int CMDID_K_PLAY_PAUSE  = 11;// PLAY/PAUSE
    public static final int CMDID_K_MODE        = 12;// MODE
    public static final int CMDID_K_NEXT        = 13;// NEXT
    public static final int CMDID_K_PREV        = 14;// PREV
    public static final int CMDID_K_VOL         = 15;// VOL
    public static final int CMDID_K_VOLAdd      = 16;// VOL+
    public static final int CMDID_K_VOLSub      = 17;// VOL-
    public static final int CMDID_K_MUTE        = 18;// MUTE
    public static final int CMDID_K_EQ          = 19;// EQ
    public static final int CMDID_K_LOOP        = 20;// LOOP
    public static final int CMDID_K_CLOCK       = 21;// CLOCK
    public static final int CMDID_K_FOLDER_N    = 22;// FOLDER+
    public static final int CMDID_K_FOLDER_P    = 23;// FOLDER-

    //应答类（除非特殊说明，查询一般不带参数，应答一般会带参数。）
    public static final int CMDID_A_StatusMsg    = 0;// 获取状态信息
    public static final int CMDID_A_DiskSize     = 1;// 获取磁盘容量
    public static final int CMDID_A_CurSongNameInfo  = 16;//获取本地音乐当前歌曲文件名和ID3信息
    public static final int CMDID_A_CurSongLrC       = 17;//获取本地音乐当前歌曲歌词文件
    public static final int CMDID_A_MusicListInfo    = 18;//获取指定序号本地音乐的播放列表信息
    public static final int CMDID_A_FileTotalInfo    = 19;//获取文件列表信息
    public static final int CMDID_A_FileListInfo     = 20;//获取文件列表信息
    public static final int CMDID_A_FMList           = 32;//获取收音机电台列表
    public static final int CMDID_A_FMCurChName      = 33;//获取当前电台名字
    public static final int CMDID_A_AlarmClockList   = 48;//获取闹钟记录列表
    public static final int CMDID_A_AlarmClockListBIn= 49;//获取内置闹铃列表

    //查询类
    public static final int CMDID_Q_StatusMsg    = 0;// 获取状态信息
    public static final int CMDID_Q_DiskSize     = 1;// 获取磁盘容量
    public static final int CMDID_Q_CurSongNameInfo  = 16;//获取本地音乐当前歌曲文件名和ID3信息
    public static final int CMDID_Q_CurSongLrC       = 17;//获取本地音乐当前歌曲歌词文件
    public static final int CMDID_Q_MusicListInfo    = 18;//获取指定序号本地音乐的播放列表信息
    public static final int CMDID_Q_FileTotalInfo    = 19;//获取指定序号本地音乐的文件列表信息
    public static final int CMDID_Q_FileListInfo     = 20;//获取指定序号本地音乐的文件列表信息
    public static final int CMDID_Q_FMList           = 32;//获取收音机电台列表
    public static final int CMDID_Q_FMCurChName      = 33;//获取当前电台名字
    public static final int CMDID_Q_AlarmClockList   = 48;//获取闹钟记录列表
    public static final int CMDID_Q_AlarmClockListBIn= 49;//获取内置闹铃列表
    //设置类
    public static final int CMDID_S_SystemData       = 0;// 系统参数设置
    public static final int CMDID_S_BUSYStatus       = 1;// BUSY状态
    public static final int CMDID_S_DialogMsg        = 2;// 对话框提示
    public static final int CMDID_S_MusicData        = 16;//音乐参数设置
    public static final int CMDID_S_MusicNumPlay     = 17;//曲目号点播
    public static final int CMDID_S_UpdatePlayList   = 18;//上传排好序播放列表（歌曲序号表）
    public static final int CMDID_S_CDisk            = 19;//切换磁盘
    public static final int CMDID_S_FMFreqRange      = 32;//设置电台频段
    public static final int CMDID_S_FMFreqPoint      = 33;//设置电台频点
    public static final int CMDID_S_AddAlarm         = 48;//添加或更新闹钟记录
    public static final int CMDID_S_DeleteAlarmList  = 49;//删除闹钟记录
    public static final int CMDID_S_LINEINData       = 64;//LINEIN参数设置

    public static final int CHAN_SPP      = 0;
    public static final int CHAN_TWISPI   = 1;

    public static final int SEG_NULL      = 0;
    public static final int SEG_PartOne   = 1;
    public static final int SEG_PartLater = 2;
    public static final int SEG_PartFinal = 3;

    public static final int ACK_NULL      = 0;
    public static final int ACK_Require   = 1;


    public static final int PLAY_MODE_SNGLE  = 1;
    public static final int PLAY_MODE_RANDOM = 3;
    public static final int PLAY_MODE_LOOP   = 0;

    public static final int PLAY_STATUS_STOP  = 0;
    public static final int PLAY_STATUS_PAUSE = 1;
    public static final int PLAY_STATUS_PLAY  = 2;
    public static final int PLAY_STATUS_PLAYAB    = 3;//AB复读
    public static final int PLAY_STATUS_PLAYFAST  = 4;//快进

    public static final int UHOST_PUSH  = 2;
    public static final int BLUE_PUSH   = 0;

    public static final int UHOST_IN    = 1;
    public static final int UHOST_OUT   = 0;

    public static final byte[] ID3SPACING = {0x00,0x01,(byte) 0xff,(byte)0xfe};

    public static final int ListType_File = 0;
    public static final int ListType_Music = 1;

    public static final String MUSIC_TYPE_MP3  = "mp3";
    public static final String MUSIC_TYPE_WMA  = "wma";
    public static final String MUSIC_TYPE_WAV  = "wav";
    public static final String MUSIC_TYPE_FLAC = "flac";
    public static final String MUSIC_TYPE_APE  = "ape";

    //0:自动切换推送保持现有的模式（推送模式优先）。
    //1：固定为推送模式。
    //2：固定为U盘模式。
    public static final int PUMode_AUTOP = 0;
    public static final int PUMode_P     = 1;
    public static final int PUMode_U     = 2;

    public static final int CODE_ANSI = 0;
    public static final int CODE_UnicodeLittleEndian = 1;
    public static final int CODE_UnicodeBigEndian = 2;
    public static final int CODE_UTF_8 = 3;

    //中间变量
    public static int U0HeadCnt = 0;
    public static Boolean U0HeadFlag = false;
    public static int U0DataCnt = 0;
    public static Boolean U0RecFrameFlg = false;
//    public static Boolean BoolRecMusicFlg = false;
    public static Boolean BOOL_ShowConnectAgainMsg = false;
    public static Boolean BOOL_BluetoothBusy = false;
    public static Boolean BOOL_ManualUpdateLUHost = false;
    public static Boolean BoolHaveManualPause = false;

    public static String MUSICPAGE_Status = "MUSICPAGE_Status";
    public static String MUSICPAGE_UpdateSongInList   = "MUSICPAGE_UpdateSongInList";
    public static String MUSICPAGE_List   = "MUSICPAGE_List";
    public static String MUSICPAGE_FileList   = "MUSICPAGE_FileList";
    public static String MUSICPAGE_LRC    = "MUSICPAGE_LRC";
    public static String MUSICPAGE_LRCIMG = "MUSICPAGE_LRCIMG";
    public static String MUSICPAGE_ID3    = "MUSICPAGE_ID3";
    public static String MUSICMSG_MSGTYPE = "type";
    public static String MUSICMSG_MSG     = "msg";
    public static String MUSICPAGE_FListShowLoading   = "MUSICPAGE_FListShowLoading";
    public static String MUSICPAGE_MListShowLoading   = "MUSICPAGE_MListShowLoading";
    public static String MUSICPAGE_ShowConnectAgainMsg   = "MUSICPAGE_ShowConnectAgainMsg";
}
