package com.example.administrator.mytestdemo.ble.bean;

import android.text.TextUtils;

/**
 * Description : 闹钟实体类
 */
public class ClockVo {

    private int turnOn; // 闹钟开关 1：开，0:关

    public String utcTime; //设置的闹钟时间

    public int[] repeatDay = new int[8] ;//闹钟重复的时间，比如：0、0、0、0、0、0、1、1 表示只在周一唤醒闹钟

    @Override
    public String toString() {
        return "UTC time = "+ utcTime +" ; releatDay = "+ repeatDay.toString();
    }

    public String getRepeatString(){
        StringBuilder sb = new StringBuilder(8);
        for(int i=0;i<repeatDay.length;i++){
            sb.append(repeatDay[i]);
        }
        return sb.toString();
    }


    public String getUtcHexPart(int part){
        if(TextUtils.isEmpty(utcTime) || utcTime.length()<8){
            return "";
        }

        String parTemp = "" ;
        switch (part){

            case 0:
                parTemp = utcTime.substring(0,2);
                break;

            case 1:
                parTemp = utcTime.substring(2,4);
                break;

            case 2:
                parTemp = utcTime.substring(4,6);
                break;

            case 3:
                parTemp = utcTime.substring(6);
                break;
        }

        return parTemp;
    }
}
