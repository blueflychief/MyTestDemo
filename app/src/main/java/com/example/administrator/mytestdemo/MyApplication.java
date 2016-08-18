package com.example.administrator.mytestdemo;

import android.app.Application;

import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.PreferencesUtils;
import com.yolanda.nohttp.NoHttp;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        KLog.init(true, "klog");
        PreferencesUtils.init(this);
        NoHttp.initialize(this);
    }

    public static MyApplication getApplication() {
        return INSTANCE;
    }

}
