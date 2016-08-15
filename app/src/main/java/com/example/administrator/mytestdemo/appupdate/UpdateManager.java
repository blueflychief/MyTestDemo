package com.example.administrator.mytestdemo.appupdate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mytestdemo.MyApplication;
import com.example.administrator.mytestdemo.nohttp.CallServer;
import com.example.administrator.mytestdemo.nohttp.HttpListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;


public class UpdateManager {
    private static final String CHECK_URL = "http://www.baidu.com";
    private static final String TAG = "UpdateManager";

    public UpdateManager() {
    }

    private static class UpdateManagerHolder {
        private static final UpdateManager sInstance = new UpdateManager();

    }

    public static UpdateManager getInstance() {
        return UpdateManagerHolder.sInstance;
    }


    public void checkUpdate() {
        Request<String> request = NoHttp.createStringRequest(CHECK_URL, RequestMethod.GET);
        request.add("build", "14853");
        CallServer.getRequestInstance().add(null, 1, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (what == 1) {
                    UpdateBean bean = null;
                    try {
                        bean = JSON.parseObject(response.get(), UpdateBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (bean != null) {
                        if (!TextUtils.isEmpty(bean.getFileurl())) {
                            startDownloadService(bean.getFileurl());
                        }
                    }

                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

            }
        }, true, false);
    }


    private void startDownloadService(String url) {
        Intent intent = new Intent(MyApplication.getApplication(), DownloadService.class);
        intent.putExtra("receiver", new ResultReceiver(new Handler()) {

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                switch (resultCode) {
                    case 0:
                        Log.i(TAG, "-----文件大小：" + resultData.getInt("total") + ":开始下载");
                        break;
                    case -1:
                        Log.i(TAG, "-----下载出错");
                        break;
                    case -2:
                        Log.i(TAG, "-----获取文件出错");
                        break;
                    case 50:
                        Log.i(TAG, "-----正在下载currentTotal:" + resultData.getInt("currentTotal"));
                        break;
                    case 99:
                        Log.i(TAG, "-----下载取消");
                        break;
                    case 100:
                        Log.i(TAG, "-----下载完成，文件大小:" + resultData.getInt("complete"));
                        break;
                }
            }
        });
        intent.putExtra("command", "download");
        intent.putExtra("url", url);
        MyApplication.getApplication().startService(intent);

    }

    public void stopDownloadService() {
        Intent intent = new Intent(MyApplication.getApplication(), DownloadService.class);
        MyApplication.getApplication().stopService(intent);
    }
}
