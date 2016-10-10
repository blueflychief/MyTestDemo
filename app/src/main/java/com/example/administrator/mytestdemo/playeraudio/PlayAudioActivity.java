package com.example.administrator.mytestdemo.playeraudio;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

import java.io.IOException;

public class PlayAudioActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static final String audioUrl = "http://kuaikuai.oss-cn-beijing.aliyuncs.com/upload/8335806f-da84-4208-99ae-c6dce48d97c8.mp3";
    private Button bt_player;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        bt_player = (Button) findViewById(R.id.bt_player);
        bt_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!releasePlayer()) {
                    initAudioPlayer();
                }
            }
        });
    }

    private void initAudioPlayer() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        try {
            mPlayer.setDataSource(audioUrl);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.i("--------onDestroy");
        releasePlayer();
    }

    /**
     * @return true 暂停播放
     */
    private boolean releasePlayer() {
        KLog.i("--------releasePlayer");
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                return true;
            }
            mPlayer.release();
            mPlayer = null;
            return false;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        KLog.i("--------onCompletion");
        releasePlayer();
    }
}
