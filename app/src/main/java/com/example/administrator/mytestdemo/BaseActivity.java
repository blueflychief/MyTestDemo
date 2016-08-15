package com.example.administrator.mytestdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.mytestdemo.common.AppCommon;
import com.example.administrator.mytestdemo.util.INetworkStatus;
import com.example.administrator.mytestdemo.util.NetworkUtils;

public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver myReceiver;
    protected INetworkStatus mINetworkStatus;
    protected boolean mIsRegisterNetworkReceiver = false;

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkReceiver();
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterNetworkReceiver(myReceiver);
    }


    private void registerNetworkReceiver() {
        if (!mIsRegisterNetworkReceiver) return;
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mINetworkStatus != null) {
                    if (AppCommon.NETWORK_STATUS != NetworkUtils.getNetworkType()) {
                        mINetworkStatus.onStatusChange(NetworkUtils.getNetworkType());
                    }
                }
            }
        };
        this.registerReceiver(myReceiver, filter);
    }

    private void unregisterNetworkReceiver(BroadcastReceiver receiver) {
        if (!mIsRegisterNetworkReceiver) return;
        this.unregisterReceiver(receiver);
    }

    public void setNetworkStatusChangeListener(INetworkStatus iNetworkStatus) {
        mIsRegisterNetworkReceiver = true;
        mINetworkStatus = iNetworkStatus;
    }

}
