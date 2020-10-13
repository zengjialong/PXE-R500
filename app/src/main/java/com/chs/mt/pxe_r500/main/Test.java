package com.chs.mt.pxe_r500.main;


import com.chs.mt.pxe_r500.datastruct.MacCfg;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {

        for (int i = 0; i < MacCfg.F_Left_Sound.length ; i++) {

            if(MacCfg.F_Left_Sound[i]==4){
                System.out.println("BUG 是否包含这个值"+true);
            }
        }

     //   System.out.println("BUG 是否包含这个值"+useList(MacCfg.F_Left_Sound,"2"));
       // getNextDay();
    }

    public static boolean useList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }


    public static Date getNextDay() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        System.out.println(sdf.format(date));
        return date;
    }

}
