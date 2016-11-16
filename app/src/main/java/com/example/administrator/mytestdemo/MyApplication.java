package com.example.administrator.mytestdemo;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.SystemPrinter;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.PreferencesUtils;
import com.example.administrator.mytestdemo.util.ToastUtils;
import com.yolanda.nohttp.NoHttp;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        KLog.init(true, "klog");
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
