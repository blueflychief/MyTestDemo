package com.example.administrator.mytestdemo.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.widget.RelativeLayout;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

import java.io.IOException;

public class MediaPlayer2Activity extends Activity implements OnCompletionListener, OnErrorListener, OnInfoListener,
        OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

    private static final String mVideoUrl = "http://kuaikuai.oss-cn-beijing.aliyuncs.com/upload/e3890058-8af7-470f-9687-42274957371b1476174025721_video.mp4";

    private Display currDisplay;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    //private boolean readyToPlay = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_media_player2);

        surfaceView = (SurfaceView) this.findViewById(R.id.video_surface);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        currDisplay = this.getWindowManager().getDefaultDisplay();

        initPlayer();
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
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        KLog.v("----------Surface Change:::", "surfaceChanged called");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //在这里我们指定MediaPlayer在当前的Surface中进行播放
        player.setDisplay(holder);
        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
        player.prepareAsync();

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
        // 当prepare完成后，该方法触发，在这里我们播放视频
        KLog.v("----------onPrepared", "onSeekComplete called");
        //首先取得video的宽和高
        int vWidth = player.getVideoWidth();
        int vHeight = player.getVideoHeight();

        //如果视频的宽或者高大于屏幕的宽高
        if (vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()) {
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            float wRatio = (float) vWidth / (float) currDisplay.getWidth();
            float hRatio = (float) vHeight / (float) currDisplay.getHeight();

            //选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);

            vWidth = (int) Math.ceil((float) vWidth / ratio);
            vHeight = (int) Math.ceil((float) vHeight / ratio);
            //设置surfaceView的布局参数
            surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));
        } else {
            surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));
        }
        player.start();
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
        KLog.v("----------Play Over:::", "onComletion called");
        this.finish();
    }
}