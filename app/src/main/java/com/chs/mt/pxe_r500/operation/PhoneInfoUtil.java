package com.chs.mt.pxe_r500.operation;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.chs.mt.pxe_r500.datastruct.MacCfg;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class PhoneInfoUtil {

    public static boolean getHoneInfo(){
//        MacCfg.PhoneMAC = getNewMac();
        MacCfg.PhoneName = android.os.Build.MODEL;
        MacCfg.PhoneOS = android.os.Build.VERSION.SDK;
        MacCfg.PhoneOS_Mode = android.os.Build.VERSION.RELEASE;

//        if(MacCfg.PhoneMAC == null){
            MacCfg.PhoneMAC = getBlueTooth();
//        }

//        String handSetInfo=
//                "MacCfg.PhoneMAC:" + MacCfg.PhoneMAC +
//                ",MacCfg.PhoneName:" + MacCfg.PhoneName +
//                ",MacCfg.PhoneOS:" + MacCfg.PhoneOS+//SDK
//                ",MacCfg.PhoneOS_Mode:"+MacCfg.PhoneOS_Mode;
//        System.out.println("BUG  handSetInfo="+handSetInfo);
        if((MacCfg.PhoneMAC != null)
                &&(MacCfg.PhoneName != null)
                &&(MacCfg.PhoneOS != null)
                &&(MacCfg.PhoneOS_Mode != null)){
            return true;
        }
        return false;
    }
    private static String getBlueTooth(){
        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        return m_szBTMAC;
    }
    /**
     * 获取电话号码
     */
    public static String getPhoneNumber(Context mContext) {
        TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String PhoneNumber  = "";//获取本机号码
        try {
            PhoneNumber  = tm.getLine1Number();//获取本机号码
        }catch (SecurityException e){

        }
        return PhoneNumber;
    }
    /**  * 通过网络接口取  * @return  */
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0"))
                    continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
