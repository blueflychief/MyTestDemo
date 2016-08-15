package com.example.administrator.mytestdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.administrator.mytestdemo.appupdate.UpdateManager;

public class CheckUpdateActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkupdate);
        findViewById(R.id.bt_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager.getInstance().checkUpdate();
            }
        });


        findViewById(R.id.bt_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager.getInstance().stopDownloadService();
            }
        });
    }
}
