package com.example.administrator.mytestdemo.appupdate;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";
    private String mTempPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private DecimalFormat df = new DecimalFormat("0.##");
    private boolean isDownload = false;

    public DownloadService() {
        super("DownloadService");
    }

    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String command = intent.getStringExtra("command");
        Bundle b = new Bundle();
        if (command.equals("download")) {
            isDownload = true;
            try {
                downLoadFile(intent.getStringExtra("url"), receiver);
            } catch (Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(-1, b);
            }
        }
        this.stopSelf();
    }


    private File downLoadFile(String httpUrl, ResultReceiver receiver) {
        if (TextUtils.isEmpty(httpUrl)) throw new IllegalArgumentException();
        File file = new File(mTempPath);
        if (!file.exists()) file.mkdirs();
        file = new File(mTempPath + File.separator + "update.apk");
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        int currentTotal = 0;
        try {
            java.net.URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            Bundle b = new Bundle();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int total = connection.getContentLength();
                if (file.exists() && file.length() == total) {
                    b.putInt("complete", total);
                    receiver.send(100, b);
                    isDownload = false;
                    return file;
                } else {
                    Log.i(TAG, "-----文件存在，现已删除");
                    file.delete();
                    file.createNewFile();
                    Log.i(TAG, "-----文件已创建");
                }
                b.putInt("total", total);
                receiver.send(0, b);
                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int count = 1;
                int len = 0;
                while (isDownload && (len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                    currentTotal += len;
                    if (currentTotal > (count * 1024 * 1024)) {
                        Log.i(TAG, "-----currentTotal:" + currentTotal);
                        Log.i(TAG, "-----写入文件大小:" + file.length());
                        Log.i(TAG, "-----完成百分比:" + df.format(currentTotal * 100.0 / total));
                        b.putInt("currentTotal", currentTotal);
                        count++;
                        receiver.send(50, b);
                    }
                }
                if (currentTotal == total) {
                    b.putInt("complete", currentTotal);
                    receiver.send(100, b);
                } else {
                    receiver.send(99, Bundle.EMPTY);
                }
                isDownload = false;
            } else {
                receiver.send(-2, Bundle.EMPTY);
            }
        } catch (Exception e) {
            receiver.send(-1, Bundle.EMPTY);
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                inputStream = null;
                outputStream = null;
            }
        }
        return file;
    }

    @Override
    public void onDestroy() {
        isDownload = false;
        super.onDestroy();
        Log.i(TAG, "-----DownloadService:onDestroy()");
    }
}
