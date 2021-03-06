package com.example.administrator.mytestdemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.administrator.mytestdemo.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/11/4.
 */
public class NetworkUtils {


    // wifi status
    /**
     * Wifi is not enable
     */
    public static final int WIFI_STATUS_DISABLE = -1;

    /**
     * Wifi is enable, but not connect to any network
     */
    public static final int WIFI_STATUS_ENABLE = 0;

    /**
     * Wifi is enable and connect to some network except CMCC wifi
     */
    public static final int WIFI_STATUS_CONNECT = 1;

    /**
     * Wifi is enable and connect with CMCC wifi, but may not verified
     */
    public static final int WIFI_STATUS_CONNECT_CMCC = 2;


    public static final String SSID_CMCC = "CMCC";
    public static final String SSID_CMCC_EDU = "CMCC-EDU";

    /**
     * 获取当前网络类型
     *
     * @return 0：未连接   1：WIFI网络   2：Mobile网络    3：ETHERNET网络
     */
    public static final int NETTYPE_WIFI = 1;
    public static final int NETTYPE_MOBILE = 2;

    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = NETTYPE_MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public static boolean isNetConnect() {
        int net_status = getNetworkType();
        return net_status != 0;
    }


    public static int getWifiStatus() {
        Context context = MyApplication.getApplication().getApplicationContext();
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            ConnectivityManager connect = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State state = connect.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
            if (state == NetworkInfo.State.CONNECTED) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null
                        && info.getSSID() != null
                        && (info.getSSID().equals(SSID_CMCC) || info.getSSID()
                        .equals(SSID_CMCC_EDU))) {
                    return WIFI_STATUS_CONNECT_CMCC; // connect to CMCC, but may
                    // be not verified
                } else {
                    return WIFI_STATUS_CONNECT; // connect to wifi
                }
            } else {
                return WIFI_STATUS_ENABLE; // wifi is enable, but no wifi
                // connect
            }
        } else {
            return WIFI_STATUS_DISABLE; // wifi is not enable
        }
    }


    public static boolean isCmwap() {
        int wifiStatus = getWifiStatus();
        if (wifiStatus == WIFI_STATUS_DISABLE
                || wifiStatus == WIFI_STATUS_ENABLE) {
            ConnectivityManager conManager = (ConnectivityManager) MyApplication.getApplication().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = conManager.getActiveNetworkInfo();
            if (ni != null) {
                String apn = ni.getExtraInfo();
                if (apn != null && apn.equalsIgnoreCase("cmwap")) {
                    return true;
                }
            }
        }
        return false;
    }


    public static final String REGX_MAIL = "^[\\-\\.\\w]+@[\\.\\-\\w]+(\\.\\w+)+$";
    public static final String REGX_HTTP = "^[A-Za-z][A-Za-z0-9+.-]{1,120}:[A-Za-z0-9/](([A-Za-z0-9$_.+!*,;/?:@&~=-])|%[A-Fa-f0-9]{2}){1,333}(#([a-zA-Z0-9][a-zA-Z0-9$_.+!*,;/?:@&~=%-]{0,1000}))?$";
    public static final String REGX_PHONE = "(1)\\d{10}";

    public static boolean isValidMailAddress(String address) {
        return address.matches(REGX_MAIL);
    }

    public static boolean isValidHttpAddress(String address) {
        return address.matches(REGX_HTTP);
    }

    public static boolean isValidMobPhone(String phone) {
        return phone.matches(REGX_PHONE);
    }


    public static String directHttp(final String webUrl) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                doHttpGetRequest(webUrl);
            }
        }.start();
        return null;
    }

    public static String doHttpGetRequest(String webUrl) {
        HttpURLConnection connection = null;
        URL url;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String tempLine;
        StringBuffer resultBuffer;
        try {
            url = new URL(webUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                resultBuffer = new StringBuffer();
                reader = new BufferedReader(inputStreamReader);
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }
                KLog.i("------返回结果：" + resultBuffer.toString());
                return resultBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
