package com.example.administrator.mytestdemo.appupdate;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.PreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";
    private String mDownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath();   //下载目录
    private DecimalFormat df = new DecimalFormat("0.##");
    private boolean isDownload = false;
    public static final int RESULT_START = 1;       //开始下载
    public static final int RESULT_FAILED = -1;     //下载失败
    public static final int RESULT_SOURSE_BAD = -2;     //源文件错误
    public static final int RESULT_CANCEL = 0;      //下载取消
    public static final int RESULT_DOWNLOADING = 50;  //正在下载
    public static final int RESULT_PAUSE = 80;      //暂停下载
    public static final int RESULT_OK = 100;        //下载完成

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
        boolean canContinue = intent.getBooleanExtra("can_continue", false);
        String fileName = intent.getStringExtra("file_name");
        String downloadUrl = intent.getStringExtra("download_url");
        if (command.equals("download")) {

            try {
//                downLoadFile(downloadUrl, fileName, canContinue, receiver);
                downLoad(downloadUrl, fileName, canContinue, receiver);
            } catch (Exception e) {
                receiver.send(RESULT_FAILED, Bundle.EMPTY);
            }
        }
        this.stopSelf();
    }

    private File downLoad(String downloadUrl, String fileName, boolean canContinue, ResultReceiver receiver) {
        if (TextUtils.isEmpty(downloadUrl)) {
            throw new IllegalArgumentException();
        }
        File file = new File(mDownloadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(mDownloadPath + File.separator + fileName);
        InputStream inputStream = null;
        RandomAccessFile raf;
        HttpURLConnection connection = null;
        int currentTotal;
        int total = getDownloadFileLength(downloadUrl);
        if (total <= 0) {
            receiver.send(RESULT_SOURSE_BAD, Bundle.EMPTY);   //文件获取错误
            return null;
        }
        Bundle bundle = new Bundle();
        isDownload = true;
        bundle.putInt("total", total);
        receiver.send(RESULT_START, bundle);   //开始下载
        try {
            int start = 0;
            if (file.exists()) {
                if (file.length() == total) {  //已完成，直接返回
                    KLog.i("----文件已下载，直接返回");
                    bundle.putInt("currentTotal", total);
                    receiver.send(RESULT_OK, bundle);
                    return file;
                }

                if (canContinue) {
                    KLog.i("----文件file.length：" + file.length());
                    KLog.i("----文件保存的已下载大小：" + (int) PreferencesUtils.get(fileName, 0));
                    if ((file.length() != 0) && (file.length() == (int) PreferencesUtils.get(fileName, 0))) {//继续下载
                        start = (int) PreferencesUtils.get(fileName, 0);
                        KLog.i("----文件已经从上次断点开始下载");
                        bundle.putInt("currentTotal", start);
                        receiver.send(RESULT_DOWNLOADING, bundle);
                    } else {//从头开始下载
                        file.delete();
                        KLog.i("----文件记录损坏，已删除文件");
                        file.createNewFile();
                        KLog.i("----重新创建了文件");
                    }
                } else {
                    file.delete();
                    KLog.i("----文件存在，已删除文件");
                    file.createNewFile();
                    KLog.i("----重新创建了文件");
                }
            } else {
                file.createNewFile();
            }

            currentTotal = start;

            URL url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + start + "-" + total);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_PARTIAL || responseCode == HttpURLConnection.HTTP_OK) {
                if (HttpURLConnection.HTTP_OK == responseCode) { //不支持断点
                    start = 0;
                    if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                    }
                }
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                inputStream = connection.getInputStream();
                byte buf[] = new byte[4096];
                int len = -1;
                int count = 1;
                while ((len = inputStream.read(buf)) != -1) {
                    if (!isDownload) {   //暂停下载
                        if (HttpURLConnection.HTTP_PARTIAL == responseCode) {
                            PreferencesUtils.put(fileName, currentTotal);
                        }
                        bundle.putInt("currentTotal", currentTotal);
                        receiver.send(RESULT_PAUSE, bundle);
                        return file;
                    }
                    raf.write(buf, 0, len);
                    currentTotal += len;
                    KLog.i("------while_currentTotal:" + currentTotal);
                    if (currentTotal > (count * 1024 * 512 + start)) {
                        if (HttpURLConnection.HTTP_PARTIAL == responseCode) {
                            PreferencesUtils.put(fileName, currentTotal);
                        }
                        bundle.putInt("currentTotal", currentTotal);
                        receiver.send(RESULT_DOWNLOADING, bundle);
                        count++;
                    }

                }
                isDownload = false;   //下载完成
                bundle.putInt("currentTotal", currentTotal);
                receiver.send(RESULT_OK, bundle);
                if (HttpURLConnection.HTTP_PARTIAL == responseCode) {
                    PreferencesUtils.remove(fileName);
                }
            }
        } catch (Exception e) {
            isDownload = false;
            receiver.send(RESULT_FAILED, Bundle.EMPTY);
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    private File downLoadFile(String downloadUrl, String fileName, boolean canContinue, ResultReceiver receiver) {
        if (TextUtils.isEmpty(downloadUrl)) throw new IllegalArgumentException();
        File file = new File(mDownloadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(mDownloadPath + File.separator + fileName);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        int currentTotal = 0;
        try {
            URL url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Bundle b = new Bundle();
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


    /**
     * 获取网络文件的长度
     *
     * @param downloadUrl
     * @return
     */
    private int getDownloadFileLength(String downloadUrl) {
        HttpURLConnection connection = null;
        int length = -1;
        URL url;
        try {
            url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                length = connection.getContentLength();
            }
            if (length <= 0) {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return length;
    }


    @Override
    public void onDestroy() {
        isDownload = false;
        super.onDestroy();
        Log.i(TAG, "-----DownloadService:onDestroy()");
    }
}
