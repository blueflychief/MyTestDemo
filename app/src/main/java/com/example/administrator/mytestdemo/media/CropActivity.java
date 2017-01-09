package com.example.administrator.mytestdemo.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_SELECT_PICTURE = 0;    // 相册选图标记
    private static final int REQUEST_TAKE_PICTURE = 1;    // 相机拍照标记
    private String mTempPhotoPath;
    private ImageView ivPic;
    private Uri imageUri;
    private String imageCropUri;


    private String mTakePicPath;    //照相存储路径
    private String mSelectPicPath;  //选图存储路径
    private MediaHelper mMediaHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        findViewById(R.id.btTakePic).setOnClickListener(this);
        findViewById(R.id.btSelectPic).setOnClickListener(this);
        ivPic = (ImageView) findViewById(R.id.ivPic);

        mMediaHelper = new MediaHelper(this, Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btTakePic://拍照裁图
//                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                //下面这句指定调用相机拍照后的照片存储的路径
//                mTakePicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pic.jpg";
//                File file = new File(mTakePicPath);
//                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(takeIntent, REQUEST_TAKE_PICTURE);
                mMediaHelper.takePicture();
                break;
            case R.id.btSelectPic://选图裁图
//                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
//                // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
//                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(pickIntent, REQUEST_SELECT_PICTURE);
                mMediaHelper.selectPicture();
                break;
        }
    }

    /**
     * @param uri 源文件的地址
     */
    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        imageCropUri = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crop.jpg";
        File file = new File(imageCropUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  //目标文件的地址
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RESULT_CAMERA_CROP_PATH_RESULT);
    }

    private static final int RESULT_CAMERA_CROP_PATH_RESULT = 301;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MediaHelper.TYPE_SELECT_PICTURE:
                    mMediaHelper.cropPicture(data.getData());
                    break;
                case MediaHelper.TYPE_TAKE_PICTURE:
                    mMediaHelper.cropPicture(mMediaHelper.getPictureUri(requestCode));
                    break;
                case MediaHelper.TYPE_CROP_PICTURE:
                    if (data.getData() != null) {
                        KLog.i("-------path:" + mMediaHelper.getRealFilePath(this, data.getData()));
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                            ivPic.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
//            switch (requestCode) {
//                case REQUEST_TAKE_PICTURE:
//                case REQUEST_SELECT_PICTURE:
//                    cropImg(Uri.fromFile(new File(mTempPhotoPath)));
//                    break;
//                case RESULT_CAMERA_CROP_PATH_RESULT: {
//                    if (data.getData() != null) {
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(imageCropUri))));
//                            ivPic.setImageBitmap(bitmap);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                break;
//            }
        }

    }

    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {

            return Environment.getExternalStorageDirectory().getPath();
        }

        return Environment.getExternalStorageDirectory().getPath();
    }
}
