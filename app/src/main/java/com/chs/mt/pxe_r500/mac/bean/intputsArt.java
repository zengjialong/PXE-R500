package com.chs.mt.pxe_r500.mac.bean;

/**
 * Created by Administrator on 2017/7/11.
 */

public class intputsArt {

    public int INS_CH_MAX;//最大通道
    public int INS_CH_MAX_USE;//最大通道
    public int INS_CH_MAX_EQ;//最大EQ
    public int INS_CH_MAX_EQ_USE;//最大EQ
    public String[] Name = new String[16];//通道名字
    //滤波器
    public XOverArt XOver = new XOverArt();
    //EQ
    public EQArt EQ = new EQArt();

}
