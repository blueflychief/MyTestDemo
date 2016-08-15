package com.example.administrator.mytestdemo.appupdate;

import android.support.v4.app.FragmentManager;

import com.example.administrator.mytestdemo.widge.CustomDialog;


public class UpdateManager {

    private static final String CHECK_URL = "http://www.baidu.com";
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

    }



}
