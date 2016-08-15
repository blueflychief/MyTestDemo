package com.example.administrator.mytestdemo.appupdate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.mytestdemo.BaseActivity;
import com.example.administrator.mytestdemo.MyApplication;
import com.example.administrator.mytestdemo.R;

/**
 * Created by Administrator on 8/15/2016.
 */
public class UpdateActivity extends BaseActivity implements View.OnClickListener {
    private static final String URL = "http://27.221.81.15/dd.myapp.com/16891/63C4DA61823B87026BBC8C22BBBE212F.apk?mkey=575e443c53406290&f=8b5d&c=0&fsname=com.daimajia.gold_3.2.0_80.apk&p=.apk";
    private static final String TAG = "UpdateActivity";
    private LinearLayout fl_root;
    private TextView tv_progress;
    private int total;
    private ProgressBar pb_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_update);
        fl_root = (LinearLayout) findViewById(R.id.fl_root);
        loadPreUpdateView();
    }


    private void loadPreUpdateView() {
        fl_root.removeAllViews();
        View view = View.inflate(this, R.layout.view_pre_updata, null);
        fl_root.addView(view);
        view.findViewById(R.id.tv_update_now).setOnClickListener(this);
        view.findViewById(R.id.tv_update_later).setOnClickListener(this);
    }

    private void loadUpdatingView() {
        fl_root.removeAllViews();
        View view = View.inflate(this, R.layout.view_updating, null);
        fl_root.addView(view);
        view.findViewById(R.id.tv_cancel_download).setOnClickListener(this);
        pb_bar = (ProgressBar) view.findViewById(R.id.pb_bar);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
    }


    private void startDownloadService(String url) {
        Intent intent = new Intent(MyApplication.getApplication(), DownloadService.class);
        intent.putExtra("receiver", new ResultReceiver(new Handler()) {

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                switch (resultCode) {
                    case 0:
                        total = resultData.getInt("total");
                        Log.i(TAG, "-----文件大小：" + total + ":开始下载");
                        break;
                    case -1:
                        Log.i(TAG, "-----下载出错");
                        break;
                    case -2:
                        Log.i(TAG, "-----获取文件出错");
                        break;
                    case 50:
                        Log.i(TAG, "-----正在下载currentTotal:" + resultData.getInt("currentTotal"));
                        pb_bar.setProgress(resultData.getInt("currentTotal") * 100 / total);
                        tv_progress.setText("文件大小：" + total + "当前下载大小：" + resultData.getInt("currentTotal"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update_now:
                loadUpdatingView();
                startDownloadService(URL);
                break;
            case R.id.tv_update_later:
                finish();
                break;
            case R.id.tv_cancel_download:
                stopDownloadService();
                finish();
                break;
        }
    }
}
