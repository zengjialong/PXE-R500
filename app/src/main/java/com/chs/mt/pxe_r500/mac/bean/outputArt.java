package com.chs.mt.pxe_r500.mac.bean;

/**
 * Created by Administrator on 2017/7/11.
 */

public class outputArt {
    public int OUT_CH_MAX;//最大通道
    public int OUT_CH_MAX_USE;//最大通道
    public int MaxOutVol;//最大值
    public int StepOutVol;//步进
    public boolean LinkPolor;
    public boolean LinkMute;
    public int[] SPK_TYPE = new int[16];//通道类型
    public String[] Name = new String[16]; //通道名字

}
