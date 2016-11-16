package com.example.administrator.mytestdemo;

import android.app.Application;

import com.elvishew.xlog.KLog;
import com.example.administrator.mytestdemo.util.PreferencesUtils;
import com.example.administrator.mytestdemo.util.ToastUtils;
import com.yolanda.nohttp.NoHttp;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
//        KLog.init(true, "klog");
        KLog.initKLog(true, "test_demo");
        ToastUtils.init(this);
        PreferencesUtils.init(this);
        NoHttp.initialize(this);

    }

    public static MyApplication getApplication() {
        return INSTANCE;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.exit(0);
    }
}
