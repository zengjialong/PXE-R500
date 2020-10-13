package com.chs.mt.pxe_r500.mac.bean;

/**
 * Created by Administrator on 2017/7/11.
 */

public class MacModeArt {

    public int CurMacMode;
    public String MacType;
    public String MacTypeDisplay;
    public String MCU_Versions;

    //主功能
    public boolean BOOL_SYSTEM_SPK_TYPEB;//是否使用SYSTEM_SPK_TYPEB
    public boolean BOOL_RESET_GROUP_DATA;//是否使用数据复位标志，当数据出错时提示用户是否恢复出厂设置
    public boolean BOOL_TRANSMITTAL;//是否使用數據傳輸標誌
    public boolean BOOL_USE_INS;/*输入功能*/
    public boolean BOOL_ENCRYPTION;/*加密功能*/
    public boolean BOOL_HIDEMODE;/*高频二分频*/
    public boolean BOOL_MIXER;/*混音设置  false:无*/
    public boolean BOOL_INPUT_SOURCE;/*音源输入*/
    public boolean BOOL_INPUT_SOURCE_VOL;/*音源音量*/
    public boolean BOOL_MIXER_SOURCE;/*混音音源输入*/
    public boolean BOOL_MIXER_SOURCE_VOL;/*混音音源音量*/
    public boolean BOOL_SUB_VOL;/*低音音量*/
    public boolean BOOL_SET_SPK_TYPE;/*通道类型设置*/

    /*通道联调
     1.前声场，后声场，超低的联调，单独分开
     2.前声场，后声场，超低的联调，一起联调
     3.前声场，后声场，单独分开
     4.前声场，后声场，中置超低的联调，一起联调
     5.设置通道输出类型后的联调
     6.设置通道输出类型后的联调，可联机保存
     7.任意联调，每个通道可以单独联调，可联机保存
     8.固定两两通道联调
     */
    public int LinkMOde;
    public int MAX_USE_GROUP;//最大用户组
    //主音量
    public DataMT Master = new DataMT();
    //延时
    public DataMT Delay = new DataMT();
    //滤波器
    public XOverArt XOver = new XOverArt();
    //EQ
    public EQArt EQ = new EQArt();
    //Output
    public outputArt Out = new outputArt();
    //混音
    public mixerArt Mixer = new mixerArt();
    //音源
    public inputsourceArt inputsource = new inputsourceArt();
    //混音音源
    public inputsourceArt Mixersource = new inputsourceArt();
    //输入
    public intputsArt INS = new intputsArt();

}
