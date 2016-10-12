package com.example.administrator.mytestdemo.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.mytestdemo.R;

public class MediaPlayerActivity extends AppCompatActivity {

//    private static final String mVideoUrl = "http://kuaikuai.oss-cn-beijing.aliyuncs.com/upload/e3890058-8af7-470f-9687-42274957371b1476174025721_video.mp4";
    private static final String mVideoUrl = "/storage/emulated/0/DCIM/Camera/VID_20161011_180344.mp4";

    private static final String TAG = "MediaPlayerActivity";
    private SurfaceView sv;
    //    private EditText et_path;
//    private Button btn_play, btn_pause, btn_replay, btn_stop;
//    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private int currentPosition = 0;
    private ImageView iv_play;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        sv = (SurfaceView) findViewById(R.id.sv);
        iv_play = (ImageView) findViewById(R.id.iv_play);
//        seekBar = (SeekBar) findViewById(seekBar);
//        et_path = (EditText) findViewById(et_path);
//
//        btn_play = (Button) findViewById(btn_play);
//        btn_pause = (Button) findViewById(btn_pause);
//        btn_replay = (Button) findViewById(btn_replay);
//        btn_stop = (Button) findViewById(btn_stop);
//
//        btn_play.setOnClickListener(click);
//        btn_pause.setOnClickListener(click);
//        btn_replay.setOnClickListener(click);
//        btn_stop.setOnClickListener(click);
        iv_play.setOnClickListener(click);
//        seekBar.setOnSeekBarChangeListener(change);

        // 为SurfaceHolder添加回调
        sv.getHolder().addCallback(mSurfaceCallback);

        // 4.0版本之下需要设置的属性
        // 设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到界面
        // sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        // SurfaceHolder被修改的时候回调
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "SurfaceHolder 被销毁");
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "SurfaceHolder 被创建");
            if (currentPosition > 0) {
                // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
                play(currentPosition);
                currentPosition = 0;

            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.i(TAG, "SurfaceHolder 大小被改变");
            play(0);
        }

    };
//
//    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            // 当进度条停止修改的时候触发
//            // 取得当前进度条的刻度
//            int progress = seekBar.getProgress();
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                // 设置当前播放的位置
//                mediaPlayer.seekTo(progress);
//            }
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//
//        }
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress,
//                                      boolean fromUser) {
//
//        }
//    };

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_play:
                    play(0);
                    break;
                case R.id.btn_pause:
                    pause();
                    break;
                case R.id.btn_replay:
                    replay();
                    break;
                case R.id.btn_stop:
                    stop();
                    break;
                case R.id.iv_play:
                    replay();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }


    /**
     * 开始播放
     *
     * @param msec 播放初始位置
     */
    protected void play(final int msec) {
//        // 获取视频文件地址
//        String path = et_path.getText().toString().trim();
//        File file = new File(mVideoUrl);
//        if (!file.exists()) {
//            Toast.makeText(this, "视频文件路径错误", Toast.LENGTH_SHORT).show();
//            return;
//        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mediaPlayer.setDataSource(mVideoUrl);
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(sv.getHolder());
            Log.i(TAG, "开始装载");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(TAG, "装载完成");
                    iv_play.setAlpha(0.0f);
                    mediaPlayer.start();
                    // 按照初始位置播放
                    mediaPlayer.seekTo(msec);
                    // 设置进度条的最大进度为视频流的最大播放时长
//                    seekBar.setMax(mediaPlayer.getDuration());
//                    // 开始线程，更新进度条的刻度
//                    new Thread() {
//
//                        @Override
//                        public void run() {
//                            try {
//                                isPlaying = true;
//                                while (isPlaying) {
//                                    int current = mediaPlayer
//                                            .getCurrentPosition();
//                                    seekBar.setProgress(current);
//
//                                    sleep(500);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//
//                    btn_play.setEnabled(false);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    iv_play.setAlpha(1.0f);
                    stop();
                    // 在播放完毕被回调
//                    btn_play.setEnabled(true);
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 发生错误重新播放
                    stop();
                    play(0);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
         * 停止播放
         */
    protected void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            //            btn_play.setEnabled(true);
            isPlaying = false;
        }
    }

    /**
     * 重新开始播放
     */
    protected void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            Toast.makeText(this, "重新播放", Toast.LENGTH_SHORT).show();
//            btn_pause.setText("暂停");
            return;
        }
        isPlaying = false;
        play(0);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPosition=mediaPlayer.getCurrentPosition();
            stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (currentPosition) {
//        }
    }

    /**
     * 暂停或继续
     */
    protected void pause() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (!mediaPlayer.isPlaying()) {
                //            if (btn_pause.getText().toString().trim().equals("继续")) {
//            btn_pause.setText("暂停");
                mediaPlayer.start();
                Toast.makeText(this, "继续播放", Toast.LENGTH_SHORT).show();
                isPlaying=true;
                return;
            } else {
                mediaPlayer.pause();
//            btn_pause.setText("继续");
                isPlaying=false;
                Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show();

            }
        }

    }

}
