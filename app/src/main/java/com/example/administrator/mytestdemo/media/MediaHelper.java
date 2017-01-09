package com.example.administrator.mytestdemo.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by lsq on 12/6/2016.
 */

public class MediaHelper {
    private Activity mContext;
    private String mTempDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final int TYPE_SELECT_PICTURE = 101;    // 选图标记
    public static final int TYPE_TAKE_PICTURE = 102;    // 拍照标记
    public static final int TYPE_CROP_PICTURE = 103;    //裁剪图片

    private String mTakePicPath;    //照相存储路径
    private String mSelectPicPath;  //选图存储路径
    private String mCropPicPath;    //裁剪图片存储路径

    /**
     * @param activity
     * @param directory 存储的目录
     */
    public MediaHelper(Activity activity, String directory) {
        mContext = activity;
        mTempDirectory = directory;
    }

    /**
     * 获取图片的uri
     *
     * @param type
     * @return
     */
    public Uri getPictureUri(int type) {
        String p = null;
        switch (type) {
            case TYPE_SELECT_PICTURE:
                p = mSelectPicPath;
                break;
            case TYPE_TAKE_PICTURE:
                p = mTakePicPath;
                break;
            case TYPE_CROP_PICTURE:
                p = mCropPicPath;
                break;
        }
        if (!TextUtils.isEmpty(p)) {
            File file = new File(p);
            if (file.exists()) {
                return Uri.fromFile(file);
            }
        }
        return null;
    }


    /**
     * 照相
     */
    public void takePicture() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTakePicPath = mTempDirectory + File.separator + System.currentTimeMillis() + "pic.jpg";//照片存储路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTakePicPath)));
        mContext.startActivityForResult(takeIntent, TYPE_TAKE_PICTURE);
    }

    /**
     * 选择图片
     */
    public void selectPicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mContext.startActivityForResult(pickIntent, TYPE_SELECT_PICTURE);
    }


    /**
     * 裁剪图片
     *
     * @param sourceUri 源文件的uri
     */
    public void cropPicture(Uri sourceUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(sourceUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 1000);
        intent.putExtra("outputY", 1000);
        intent.putExtra("return-data", false);
        mCropPicPath = mTempDirectory + File.separator + System.currentTimeMillis() + "crop.jpg";
        File file = new File(mCropPicPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  //目标文件的地址
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mContext.startActivityForResult(intent, TYPE_CROP_PICTURE);
    }

    /**
     * 获取文件的真实路径
     *
     * @param context
     * @param uri
     * @return
     */
    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
