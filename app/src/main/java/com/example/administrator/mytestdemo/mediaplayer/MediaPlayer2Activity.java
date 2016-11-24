package com.example.administrator.mytestdemo.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.ToastUtils;

import java.io.IOException;

public class MediaPlayer2Activity extends Activity implements OnCompletionListener, OnErrorListener, OnInfoListener,
        OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

//    private static final String mVideoUrl = "http://kuaikuai.oss-cn-beijing.aliyuncs.com/upload/e3890058-8af7-470f-9687-42274957371b1476174025721_video.mp4";
//    private static final String mVideoUrl = "http://kuaikuai.oss-cn-beijing.aliyuncs.com/upload/d2a226b1-3344-499c-8acc-d08831334a77.mp4";
    private static final String mVideoUrl = "http://kuaikuai.oss-cn-beijing.aliyuncs.com/upload/e3890058-8af7-470f-9687-42274957371b1476238338240_video.mp4";

    private Display currDisplay;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private ImageView iv_play;
    private ProgressBar pb_loading;
    private int currentPosition = 0;
    private RelativeLayout rl_root;
    private boolean isPlaying = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_media_player2);

        surfaceView = (SurfaceView) this.findViewById(R.id.sv_surfaceview);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        currDisplay = this.getWindowManager().getDefaultDisplay();
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                    iv_play.setVisibility(View.GONE);
                    player.start();
                    isPlaying = true;
//                    player.seekTo(0);
                }
            }
        });

    }

    private void initPlayer() {
        if (player == null) {
            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnInfoListener(this);
            player.setOnPreparedListener(this);
            player.setOnSeekCompleteListener(this);
            player.setOnVideoSizeChangedListener(this);
            try {
                player.setDataSource(mVideoUrl);
            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                e.printStackTrace();
                ToastUtils.showToast("视频无法播放");
                finish();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        KLog.v("----------surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initPlayer();
        if (player != null) {
            player.setDisplay(holder);
            player.prepareAsync();
            iv_play.setVisibility(View.GONE);
            pb_loading.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        KLog.v("----------Surface Destory:::", "surfaceDestroyed called");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
        // 当video大小改变时触发
        //这个方法在设置player的source后至少触发一次
        KLog.v("----------Video Size Change", "onVideoSizeChanged called");

    }

    @Override
    public void onSeekComplete(MediaPlayer arg0) {
        // seek操作完成时触发
        KLog.v("----------Seek Completion", "onSeekComplete called");

    }

    @Override
    public void onPrepared(MediaPlayer player) {
        KLog.v("----------onPrepared");

        //视频宽高
        int vw = player.getVideoWidth();
        int vh = player.getVideoHeight();

        //父容器宽高
        int pw = rl_root.getMeasuredWidth();
        int ph = rl_root.getMeasuredHeight();


        float wRatio = (float) vw / (float) pw;
        float hRatio = (float) vh / (float) ph;
        float ratio = 1.0f;
        //如果视频的宽或者高大于屏幕的宽高
        if (vw > pw || vh > ph) {
            ratio = Math.max(wRatio, hRatio);   //需要缩小
        } else {
            ratio = Math.min(wRatio, hRatio);   //需要放大
        }
        vw = (int) Math.ceil((float) vw / ratio);
        vh = (int) Math.ceil((float) vh / ratio);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(vw, vh);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        surfaceView.setLayoutParams(layoutParams);
        pb_loading.setVisibility(View.GONE);
        player.start();
        isPlaying = true;
    }

    @Override
    public boolean onInfo(MediaPlayer player, int whatInfo, int extra) {
        // 当一些特定信息出现或者警告时触发
        switch (whatInfo) {
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                break;
        }

        KLog.i("---------whatInfo:" + whatInfo + "---" + extra);
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            currentPosition = player.getCurrentPosition();
            player.pause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (player != null && isPlaying) {
            if (currentPosition > 0) {
                player.start();
                player.seekTo(currentPosition);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    @Override
    public boolean onError(MediaPlayer player, int whatError, int extra) {


        switch (whatError) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                KLog.v("----------Play Error:::", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                KLog.v("----------Play Error:::", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        // 当MediaPlayer播放完成后触发
        iv_play.setVisibility(View.VISIBLE);
        isPlaying = false;
        KLog.v("----------Play Over:::", "onComletion called");
    }
}