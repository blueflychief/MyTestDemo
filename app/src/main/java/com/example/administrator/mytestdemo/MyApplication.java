package com.example.administrator.mytestdemo;

import android.app.Application;

import com.example.administrator.mytestdemo.util.KLog;
import com.yolanda.nohttp.NoHttp;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        KLog.init(true, "klog");
        NoHttp.initialize(this);
    }

    public static MyApplication getApplication() {
        return INSTANCE;
    }

}
