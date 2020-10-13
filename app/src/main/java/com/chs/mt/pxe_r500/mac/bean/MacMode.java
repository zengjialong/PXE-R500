package com.chs.mt.pxe_r500.mac.bean;

import com.chs.mt.pxe_r500.datastruct.Define;

/**
 * Created by Administrator on 2017/7/11.
 */

public class MacMode {
    public int[] HEAD_DATA = new int[Define.MAC_MAX];//包头
    public int[] MacMode = new int[Define.MAC_MAX];//序号
    public int MacModeMax;

    public MacModeArt[]  MacModeArt = new MacModeArt[Define.MAC_MAX];
    public String[] MacType = new String[Define.MAC_MAX];//机型名字
    public String[] MacTypeDisplay = new String[Define.MAC_MAX];//机型显示的名字，有时机型显示和名字和本名不一样
    public String[] MCU_Versions = new String[Define.MAC_MAX];//版本号

    public MacMode(){
        for(int j=0;j<Define.MAC_MAX;j++){
            MacModeArt[j]= new MacModeArt();
        }
    }
}
