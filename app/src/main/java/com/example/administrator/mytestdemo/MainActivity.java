package com.example.administrator.mytestdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.elvishew.xlog.KLog;
import com.example.administrator.mytestdemo.alipay.PayDemoActivity;
import com.example.administrator.mytestdemo.appupdate.CheckUpdateActivity;
import com.example.administrator.mytestdemo.ble.SearchBleActivity;
import com.example.administrator.mytestdemo.carousel.CBActivity;
import com.example.administrator.mytestdemo.customerview.CustomActivity;
import com.example.administrator.mytestdemo.flowtag.FlowTagActivity;
import com.example.administrator.mytestdemo.mediaplayer.MediaPlayer2Activity;
import com.example.administrator.mytestdemo.photofilterssdk.imageprocessors.PhotoFilterActivity;
import com.example.administrator.mytestdemo.playeraudio.PlayAudioActivity;
import com.example.administrator.mytestdemo.popwindow.PopwindowActivity;
import com.example.administrator.mytestdemo.premission.PermissionActivity;
import com.example.administrator.mytestdemo.premission.YzjPermissionActivity;
import com.example.administrator.mytestdemo.recyclerview.RecyclerViewActivity;
import com.example.administrator.mytestdemo.scroller.ScrollerActivity;
import com.example.administrator.mytestdemo.stickview.StickerViewActivity;
import com.example.administrator.mytestdemo.superrecycler.SuperRecyclerActivity;
import com.example.administrator.mytestdemo.textlength.TextLengthActivity;
import com.example.administrator.mytestdemo.util.DrawableUtil;
import com.example.administrator.mytestdemo.util.INetworkStatus;
import com.example.administrator.mytestdemo.videorecorder.NewRecordVideoActivity;
import com.example.administrator.mytestdemo.wifi.WifiTestActivity;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener, INetworkStatus {
    private Button bt_alipay;
    private Button bt_compress;
    private Button bt_leak;
    private Button bt_update;
    private Button bt_video_Record;
    private Button bt_ble;
    private Button bt_cb;
    private Button bt_recycler;
    private Button bt_string;
    private Button bt_wifi;
    private Button bt_superrecycler;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bt_alipay = (Button) findViewById(R.id.bt_alipay);
        bt_compress = (Button) findViewById(R.id.bt_compress);
        imageView = (ImageView) findViewById(R.id.iv);
        bt_leak = (Button) findViewById(R.id.bt_leak);
        bt_update = (Button) findViewById(R.id.bt_update);
        bt_video_Record = (Button) findViewById(R.id.bt_video_Record);
        bt_string = (Button) findViewById(R.id.bt_string);
        bt_wifi = (Button) findViewById(R.id.bt_wifi);
        bt_recycler = (Button) findViewById(R.id.bt_recycler);
        bt_ble = (Button) findViewById(R.id.bt_ble);
        bt_cb = (Button) findViewById(R.id.bt_cb);
        bt_superrecycler = (Button) findViewById(R.id.bt_superrecycler);
        bt_alipay.setOnClickListener(this);
        bt_leak.setOnClickListener(this);
        bt_compress.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        bt_video_Record.setOnClickListener(this);
        bt_ble.setOnClickListener(this);
        bt_cb.setOnClickListener(this);
        bt_recycler.setOnClickListener(this);
        bt_superrecycler.setOnClickListener(this);
        findViewById(R.id.bt_stickview).setOnClickListener(this);
        findViewById(R.id.bt_photo_filter).setOnClickListener(this);
        findViewById(R.id.bt_wifi).setOnClickListener(this);
        findViewById(R.id.bt_audio).setOnClickListener(this);
        findViewById(R.id.bt_permission).setOnClickListener(this);
        findViewById(R.id.bt_yzjpermission).setOnClickListener(this);
        findViewById(R.id.bt_mediaplayer).setOnClickListener(this);
        findViewById(R.id.bt_customview).setOnClickListener(this);
        findViewById(R.id.bt_ppw).setOnClickListener(this);
        findViewById(R.id.bt_flow).setOnClickListener(this);
        findViewById(R.id.bt_matrix).setOnClickListener(this);
        findViewById(R.id.bt_scroller).setOnClickListener(this);
        bt_string.setOnClickListener(this);
        setNetworkStatusChangeListener(this);


    }

    @Override
    public void onClick(View view) {
        KLog.i(TestData.testJson);
        switch (view.getId()) {
            case R.id.bt_alipay:
                startActivity(new Intent(this, PayDemoActivity.class));
                KLog.i("-------MainActivtiy_click");

                break;
            case R.id.bt_compress:
//                Bitmap bitmap = DrawableUtil.cQuality(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "9999.jpg"));
//                DrawableUtil.storeBitmapToFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "9999-1.jpg", bitmap);

//                SiliCompressor.with(this).compress(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "9999.jpg");

//                ImageLoader.loadImage(this, "http://image6.huangye88.com/2013/03/28/2a569ac6dbab1216.jpg", imageView);
//
                Bitmap bitmap = DrawableUtil.compressImage(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "2222.jpg",
                        Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "2222_2.jpg", 800, 800, 100, false);
                imageView.setImageBitmap(bitmap);

//                ImageUtil.compressImage(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "2222.jpg",
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "2222_c.jpg", 800, 800, Bitmap.CompressFormat.JPEG, 100);
                break;
            case R.id.bt_leak:
                startActivity(new Intent(this, LeakTestActivity.class));
                break;
            case R.id.bt_update:
                startActivity(new Intent(this, CheckUpdateActivity.class));
                break;
            case R.id.bt_video_Record:
                startActivity(new Intent(MainActivity.this, NewRecordVideoActivity.class));
                break;
            case R.id.bt_ble:
                startActivity(new Intent(MainActivity.this, SearchBleActivity.class));
                break;
            case R.id.bt_cb:
                startActivity(new Intent(MainActivity.this, CBActivity.class));
                break;
            case R.id.bt_stickview:
                startActivity(new Intent(MainActivity.this, StickerViewActivity.class));
                break;
            case R.id.bt_photo_filter:
                startActivity(new Intent(MainActivity.this, PhotoFilterActivity.class));
                break;
            case R.id.bt_recycler:
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                break;
            case R.id.bt_string:
                startActivity(new Intent(MainActivity.this, TextLengthActivity.class));
                break;
            case R.id.bt_wifi:
                startActivity(new Intent(MainActivity.this, WifiTestActivity.class));
                break;
            case R.id.bt_superrecycler:
                startActivity(new Intent(MainActivity.this, SuperRecyclerActivity.class));
                break;

            case R.id.bt_permission:
                startActivity(new Intent(MainActivity.this, PermissionActivity.class));
                break;
            case R.id.bt_yzjpermission:
                startActivity(new Intent(MainActivity.this, YzjPermissionActivity.class));
                break;

            case R.id.bt_audio:
                startActivity(new Intent(MainActivity.this, PlayAudioActivity.class));
                break;
//            case R.id.bt_vertical_viewpager:
//                startActivity(new Intent(MainActivity.this, ViewPagerActivity.class));
//                break;
            case R.id.bt_mediaplayer:
//                startActivity(new Intent(MainActivity.this, MediaPlayerActivity.class));
                startActivity(new Intent(MainActivity.this, MediaPlayer2Activity.class));
                break;
            case R.id.bt_customview:
//                startActivity(new Intent(MainActivity.this, MediaPlayerActivity.class));
                startActivity(new Intent(MainActivity.this, CustomActivity.class));
                break;
            case R.id.bt_ppw:
//                startActivity(new Intent(MainActivity.this, MediaPlayerActivity.class));
                startActivity(new Intent(MainActivity.this, PopwindowActivity.class));
                break;
            case R.id.bt_flow:
                startActivity(new Intent(MainActivity.this, FlowTagActivity.class));
                break;
            case R.id.bt_matrix:
                startActivity(new Intent(MainActivity.this, MatrixActivity.class));
                break;
            case R.id.bt_scroller:
                startActivity(new Intent(MainActivity.this, ScrollerActivity.class));
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ColorDrawable drawable = new ColorDrawable(0x55ffFF00);
        imageView.setImageDrawable(drawable);

    }

    @Override
    public void onStatusChange(int type) {
        KLog.i("网络发生了变化：" + type);
    }


}
