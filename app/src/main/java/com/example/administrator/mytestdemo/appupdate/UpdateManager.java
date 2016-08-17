package com.example.administrator.mytestdemo.appupdate;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.example.administrator.mytestdemo.BaseActivity;
import com.example.administrator.mytestdemo.nohttp.CallServer;
import com.example.administrator.mytestdemo.nohttp.HttpListener;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.widge.CustomDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;


public class UpdateManager extends BaseActivity {

    private static final String URL = "http://test.kuaikuaikeji.com/kas/appcheck21?build=14805";
    private static final String TAG = "UpdateManager";
    private FragmentManager fragmentManager;
    private CustomDialog customDialog;

    public UpdateManager() {
    }

    private static class UpdateManagerHolder {
        private static final UpdateManager sInstance = new UpdateManager();

    }

    public static UpdateManager getInstance() {
        return UpdateManagerHolder.sInstance;
    }


    public void checkUpdate() {
        Request request = NoHttp.createStringRequest(URL, RequestMethod.GET);
        CallServer.getRequestInstance().add(this, 100, request, new HttpListener() {
            @Override
            public void onSucceed(int what, Response response) {
                KLog.i("---response:" + response.get().toString());
                getNewVersion(response.get().toString());
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                KLog.i("---onFailed:" + responseCode);
            }
        }, true, false);
    }

    public void getNewVersion(String versionString) {
        UpdateBean bean = null;
        try {
            bean = JSON.parseObject(versionString, UpdateBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bean != null && !TextUtils.isEmpty(bean.fileurl)) {
            UpdateActivity.startUpdateActivity(bean);
        }

    }
}
