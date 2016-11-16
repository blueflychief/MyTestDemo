package com.example.administrator.mytestdemo.ble;

import android.os.Environment;
import android.text.TextUtils;

import com.example.administrator.mytestdemo.ble.bean.ClockVo;
import com.example.administrator.mytestdemo.ble.bean.UserInfoEntity;
import com.elvishew.xlog.KLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by guoning on 16-4-13.
 */
public class DataCreator {

    private static final int data_size = 20;

    private static  byte[] user_id = DataUtils.intToByteArray(01020304);


    public static void setUserId(int userId){
        user_id = DataUtils.intToByteArray(userId) ;
    }

    public static byte[] createGetPulse(int userId){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x0a;

        //用户id
        setUserId(userId);
        System.arraycopy(user_id, 0, data, index, user_id.length);
        index += user_id.length;

        return data;
    }

    public static String getCurrentTime(){
        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time ;
    }

    public static String getFileTime(){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("_yyyy-MM-dd_HH_mm_ss");
        String time=format.format(date);
        return time ;
    }


    public static String getCurrentHmsTime(){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        String time=format.format(date);
        return time ;
    }

    public static String getDiffTime(String starTime , String endTime){

        if(TextUtils.isEmpty(starTime) || TextUtils.isEmpty(endTime)){
            return "" ;
        }

        try{
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date begin=dfs.parse(starTime);
            Date end = dfs.parse(endTime);
            long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒

            long day1=between/(24*3600);
            long hour1=between%(24*3600)/3600;
            long minute1=between%3600/60;
            long second1=between%60/60;
            String result = /*""+day1+"天"+*/hour1+"时:"+minute1+"分:"+second1+"秒";
            System.out.println(/*""+day1+"天"+*/hour1+"时:"+minute1+"分:"+second1+"秒");
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] createStopCourse(){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x0b;

        //用户id
        byte[] id = DataUtils.intToByteArray(1);
        System.arraycopy(id, 0, data, index, id.length);
        index += id.length;

        return data;
    }

    public static byte[] createRealtimeHBAndActionComplete(){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x0a;

        //用户id
        byte[] id = DataUtils.intToByteArray(1);
        System.arraycopy(id, 0, data, index, id.length);

        return data;
    }

    /**
     * 设置闹钟
     * @return
     */
    public  static byte[] createClockProtocol(ArrayList<ClockVo> clockList){

        if (clockList == null && clockList.isEmpty()) {
            return null;
        }

        if(clockList.size() < 3){ // 需要同时设置三个闹钟
            return null;
        }

        ClockVo clock0 = clockList.get(0);
        ClockVo clock1 = clockList.get(1);
        ClockVo clock2 = clockList.get(2);

        byte[] data = new byte[data_size];
        int index = 0;

        //命令类型
        data[index++] = 0x08;


        //闹钟1开关
        data[index++] = DataUtils.getStringToByte(clock0.getRepeatString());

        //闹钟1时间信息
        data[index++] = DataUtils.getHexToByte(clock0.getUtcHexPart(0));
        data[index++] = DataUtils.getHexToByte(clock0.getUtcHexPart(1));
        data[index++] = DataUtils.getHexToByte(clock0.getUtcHexPart(2));
        data[index++] = DataUtils.getHexToByte(clock0.getUtcHexPart(3));

        //闹钟2开关
        data[index++] = DataUtils.getStringToByte(clock1.getRepeatString());

        //闹钟2时间信息
        data[index++] = DataUtils.getHexToByte(clock1.getUtcHexPart(0));
        data[index++] = DataUtils.getHexToByte(clock1.getUtcHexPart(1));
        data[index++] = DataUtils.getHexToByte(clock1.getUtcHexPart(2));
        data[index++] = DataUtils.getHexToByte(clock1.getUtcHexPart(3));

        //闹钟3开关
        data[index++] = DataUtils.getStringToByte(clock2.getRepeatString());

        //闹钟3时间信息
        data[index++] = DataUtils.getHexToByte(clock2.getUtcHexPart(0));
        data[index++] = DataUtils.getHexToByte(clock2.getUtcHexPart(1));
        data[index++] = DataUtils.getHexToByte(clock2.getUtcHexPart(2));
        data[index] = DataUtils.getHexToByte(clock2.getUtcHexPart(3));

        return data;
    }


    public static byte[] createIdentifyAction(int action_code, int x, int y, int z, int roll, int pitch, int yaw, byte t){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x0e;

        //用户id
        byte[] id = DataUtils.intToByteArray(1);
        System.arraycopy(id, 0, data, index, id.length);
        index += id.length;

        byte[] action = DataUtils.intToByteArray(action_code);

        System.arraycopy(action, 2, data, index, 2);

        index += 2;

        byte[] xb = DataUtils.intToByteArray(x);

        System.arraycopy(xb, 2, data, index, 2);

        index += 2;

        byte[] yb = DataUtils.intToByteArray(y);

        System.arraycopy(yb, 2, data, index, 2);

        index += 2;

        byte[] zb = DataUtils.intToByteArray(z);

        System.arraycopy(zb, 2, data, index, 2);

        index += 2;

        byte[] rollb = DataUtils.intToByteArray(roll);

        System.arraycopy(rollb, 2, data, index, 2);

        index += 2;

        byte[] pitchb = DataUtils.intToByteArray(pitch);

        System.arraycopy(pitchb, 2, data, index, 2);

        index += 2;

        byte[] yawb = DataUtils.intToByteArray(yaw);

        System.arraycopy(yawb, 2, data, index, 2);

        index += 2;

        data[index]= t;

        return data;
    }


    /**
     * utc转16进制字符串,为了向腕表同步手机时间
     * @return
     */
    public static byte[] getSystemCurrentTime(int userId){
        ClockVo vo1 = new ClockVo();
        long timeMillis = System.currentTimeMillis()/1000;
        vo1.utcTime = DataUtils.getLongToHex(timeMillis);
        return DataCreator.setTime(userId , vo1) ;
    }

    /**
     * 3.2.25.同步步数数据协议
     * @param userId
     * @return
     */
    public static byte[] syncStepDataProc(int userId){
        byte[] data = new byte[data_size];
        int index = 0;

        //命令类型
        data[index++] = 0x1f;

        setUserId(userId);

        //用户id
        System.arraycopy(user_id, 0, data, index, user_id.length);

        return data ;
    }


    /***
     * BLE设置时间协议
     * @return
     */
    public static byte[] setTime(int userId , ClockVo vo){
        byte[] data = new byte[data_size];
        int index = 0;

        //命令类型
        data[index++] = 0x07;

        setUserId(userId);

        //用户id
        System.arraycopy(user_id, 0, data, index, user_id.length);

        index += user_id.length;

        //闹钟1时间信息
        data[index++] = DataUtils.getHexToByte(vo.getUtcHexPart(0));
        data[index++] = DataUtils.getHexToByte(vo.getUtcHexPart(1));
        data[index++] = DataUtils.getHexToByte(vo.getUtcHexPart(2));
        data[index++] = DataUtils.getHexToByte(vo.getUtcHexPart(3));

        return data;
    }



    /***
     * BLE查询用户信息接口命令
     * @return
     */
    public static byte[] checkUserInformation(){
        byte[] data = new byte[data_size];
        //命令类型
        data[0] = 0x14;
        return data;
    }


    /**
     * 查询剩余电量
     * @return
     */
    public static byte[] querySurplusCapatity(){
        byte[] data = new byte[data_size];
        //命令类型
        data[0] = 0x1c;
        return data;
    }


    /**
     * 跑步模式协议
     * @return
     * type有三种类型,0 : 退出跑步模式0x00 , 1: 进入跑步模式，开启心率模块0x01 , 2 . 获取实时步数、实时心率0x02
     */
    public static byte[] queryRunModeHbData(int type,int distance){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x18;

        switch (type){

            case 0: //退出跑步模式0x00
                data[index++] = 0x00 ;
            break;

            case 1: //进入跑步模式0x01
                data[index++] = 0x01 ;
                break;

            case 2: //实时获取当前心率和步数
                data[index++] = 0x02 ;
                if(distance != 0){
                    byte[] distance2  = DataUtils.intToByteArray(distance) ;
                    System.arraycopy(distance2, 0, data, index, distance2.length);
                }
                break;
        }
        return data;
    }


    /**
     * BLE设置心率测量期间LED呼吸灯常亮接口命令
     * @param isOn
     * @return
     */
    public static byte[] setLEDLightAlwaysOn(boolean isOn){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x15;

        int onOff = isOn ? 1 : 0 ;

        data[index++] = (byte)onOff;

        return data;
    }


    /**
     * BLE设置心率测量期间屏幕常亮接口命令
     * @return
     */
    public static byte[] setScreenAlwaysOn(boolean isOn){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x16;

        int onOff = isOn ? 1 : 0 ;

        data[index++] = (byte)onOff;

        return data;
    }

    /***
     * BLE查询NRF51822软件版本号协议
     * @return 
     */
    public static byte[] checkBleNRF51822Version(){
        byte[] data = new byte[data_size];
        //命令类型
        data[0] = 0x17;
        return data;
    }



    /***
     * 进入OTA升级模式
     * @return
     */
    public static byte[] enterOTAUpdateMode(){
        byte[] data = new byte[data_size];
        //命令类型
        data[0] = 0x0f;
        return data;
    }

    public static byte[] createUserPractice(UserInfoEntity entity, int action_code, int target_kcal){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x09;

        //用户id
        byte[] id = DataUtils.intToByteArray(entity.getUser_id());
        System.arraycopy(id, 0, data, index, id.length);
        index += id.length;

        //身高
        data[index++] = (byte) entity.getHeight();

        //体重
        String[] weight = String.valueOf(entity.getWeight()).split("\\.");
        data[index++] = (byte) Integer.parseInt(weight[0]);
        if (weight.length < 2) {
            data[index++] = 0;
        } else {
            data[index++] = (byte) Integer.parseInt(weight[1]);
        }

        //年龄
        data[index++] = (byte) entity.getAge();

        //性别
        data[index++] = (byte) entity.getSex();

        //静态心率
        data[index++] = (byte) entity.getPulse();

        byte[] action = DataUtils.intToByteArray(action_code);

        System.arraycopy(action, 2, data, index, 2);

        index += 2;

        data[index] = (byte) target_kcal;


        return data;
    }

    /**
     * 获取实时心率和动作完成度
     * @return
     */
    public static byte[] createHRCmd(int userId) {
        byte[] data = new byte[data_size];
        int index = 0;
        //命令类型
        data[index++] = 0x0a;
        //用户id
        byte[] id = DataUtils.intToByteArray(userId);
        System.arraycopy(id, 0, data, index, id.length);
        index += id.length;
        return data;
    }

    public static byte[] createUserConfig(UserInfoEntity entity) {

        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x05;

        //用户id
        byte[] id = DataUtils.intToByteArray(entity.getUser_id());
        System.arraycopy(id, 0, data, index, id.length);
        index += id.length;

        //身高
        data[index++] = (byte) entity.getHeight();

        //体重
        String[] weight = String.valueOf(entity.getWeight()).split("\\.");
        data[index++] = (byte) Integer.parseInt(weight[0]);
        if (weight.length < 2) {
            data[index++] = 0;
        } else {
            data[index++] = (byte) Integer.parseInt(weight[1]);
        }

        //年龄
        data[index++] = (byte) entity.getAge();

        //性别
        data[index++] = (byte) entity.getSex();

        //静态心率
        data[index] = (byte) entity.getPulse();

        return data;
    }


    /**获取动作原始数据长度*/
    public static byte[] getSourceDataLength(){
        byte[] data = new byte[data_size];
        int index = 0;

        //命令类型
        data[index++] = 0x11;

        return data;
    }

    public static byte[] getSourceAllData(int type){ // 获取动作原始数据命令
        byte[] data = new byte[data_size];
        int index = 0;

        //命令类型
        data[index++] = 0x12;

        //数据类型
        data[index] = (byte) type;

        return data;
    }

    /**
     * 获取静止心率数据
     * @return
     */
    public static byte[] getStaticHeartData(int userId){
        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x0d;

        setUserId(userId);

        //用户id
        System.arraycopy(user_id, 0, data, index, user_id.length);

        return data;
    }


    /**
     * 获取运动心率数据
     * @param type 0:代表获取心率段数 , 1: 代表心率
     * @return
     */
    public static byte[] getSportHeartData(int type,int userId) {

        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x06;

        //数据类型
        data[index++] = (byte) type;

        setUserId(userId);

        //用户id
        System.arraycopy(user_id, 0, data, index, user_id.length);

        return data;
    }


    /**
     * 设置deviceID协议
     * @return
     */
    public static byte[] setDeviceId(String deviceId){

        if(TextUtils.isEmpty(deviceId)){
            throw new IllegalArgumentException("参数错误");
        }

        int deviceidInt = Integer.parseInt(deviceId);

        byte[] trueId = DataUtils.intToByteArray(deviceidInt);


        byte[] data = new byte[data_size];

        int index = 0;

        //命令类型
        data[index++] = 0x1a;

        //DeviceId
        System.arraycopy(trueId, 0, data, index, trueId.length);

        return data;
    }


    /**
     *   查询DeviceId
     * @return
     */
    public static byte[] checkDeviceId(){
        byte[] data = new byte[data_size];
        int index = 0;
        //命令类型
        data[index++] = 0x1b;
        return data;
    }





    public static byte[] createM4versionCheckingData() {
        byte[] data = new byte[data_size];
        int index = 0;
        //命令类型
        data[index++] = 0x10;
        return data;
    }

    public static byte[] createM4BinFirstPackageData(int filelenth) {

        byte[] data = new byte[data_size];
        int index = 0;
        //命令类型
        data[index++] = 0x13;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x00;

        byte[] datalen = DataUtils.intToByteArray(filelenth);
        System.arraycopy(datalen, 0, data, index, datalen.length);

        return data;

    }

    public static byte[] createM4FilePackage(int dataindex, byte[] content_data) {
        dataindex = dataindex + 1;
        short shortindex = (short) dataindex;
        byte[] datalen = DataUtils.shortToByteArray(shortindex);

        byte[] data = new byte[data_size];
        KLog.e("M4 File index:[" + dataindex + "] Length: + [" + content_data.length + "]");
        int index = 0;
        //命令类型
        data[index++] = 0x13;
        data[index++] = 0x00;

        System.arraycopy(datalen, 0, data, index, datalen.length);

        StringBuffer numbersb = new StringBuffer();
        numbersb.append("#check numbersb #:");
        for (int i = 0; i < datalen.length; i++) {
            numbersb.append(DataUtils.byteTohex(datalen[i])).append(" ");
        }
        KLog.e("##:" + numbersb.toString());

        index++;
        index++;

        System.arraycopy(content_data, 0, data, index, content_data.length);


        StringBuffer sb = new StringBuffer();
        sb.append("#check#:");
        for (int i = 0; i < data.length; i++) {
            sb.append(DataUtils.byteTohex(data[i])).append(" ");
        }
        KLog.e("##:" + sb.toString());


        return data;
    }


    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    public static byte[] createM4FileLastPackage(byte[] content_data) {
        byte[] data = new byte[data_size];
        int index = 0;
        //命令类型
        data[index++] = 0x13;
        data[index++] = 0x00;

        char fixdata = 0xff;
        data[index++] = (byte) fixdata;
        data[index++] = (byte) fixdata;
        System.arraycopy(content_data, 0, data, index, content_data.length);

        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < data.length; i++) {
            sb.append(DataUtils.byteTohex(data[i])).append(" ");
        }
        sb.append("]");
        KLog.e("#checkSum#:" + sb.toString());

        return data;
    }

}
