package com.example.administrator.mytestdemo.videorecorder.recorder;

/**
 * Created by Administrator on 8/21/2016.
 */
public interface RecordCallback {
    void onRecordStart();

    void onRecording(long recordTime);

    void onRecordStop();

    void onRecordFailed();
}
