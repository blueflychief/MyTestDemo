package com.example.administrator.mytestdemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class DrawableUtil {
    /**
     * 根据bitmap压缩图片质量
     *
     * @param bitmap 未压缩的bitmap
     * @return 压缩后的bitmap
     */
    public static Bitmap cQuality(Bitmap bitmap) {
        Log.i("DrawableUtil", "-----start_compress--");
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int beginRate = 90;
        //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bOut);
        Log.i("DrawableUtil", "-----bOut.size--" + bOut.size() / 1024 / 1024);
        while ((bOut.size() / 1024 > 100) && (beginRate > 10)) {  //如果压缩后大于100Kb，则提高压缩率，重新压缩
            beginRate -= 10;
            bOut.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, bOut);
        }
        ByteArrayInputStream bInt = new ByteArrayInputStream(bOut.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bInt);
        Log.i("DrawableUtil", "-----end_compress--");
        if (newBitmap != null) {
            return newBitmap;
        } else {
            return bitmap;
        }
    }


    public static boolean getCacheImage(String filePath, String cachePath) {
        OutputStream out = null;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;  //设置为true，只读尺寸信息，不加载像素信息到内存
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, option);  //此时bitmap为空
        option.inJustDecodeBounds = false;
        int bWidth = option.outWidth;
        int bHeight = option.outHeight;
        int toWidth = 400;
        int toHeight = 800;
        int be = 1;  //be = 1代表不缩放
        if (bWidth / toWidth > bHeight / toHeight && bWidth > toWidth) {
            be = (int) bWidth / toWidth;
        } else if (bWidth / toWidth > toHeight) {
            be = (int) bHeight / toHeight;
        }
        option.inSampleSize = be; //设置缩放比例
        bitmap = BitmapFactory.decodeFile(filePath, option);
        try {
            out = new FileOutputStream(new File(cachePath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }

    /**
     * 将bitmap保存到本地
     *
     * @param file_path
     * @param bitmap
     */
    public static boolean storeBitmapToFile(@NonNull String file_path, Bitmap bitmap) {
        File file = new File(file_path);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = null;
        if (bitmap != null) {
            try {
                out = new FileOutputStream(file_path);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file.exists() && file.length() > 0) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 压缩图片
     *
     * @param imagePath    图片路径
     * @param saveFile     保存路径
     * @param maxHeight    最大高度
     * @param maxWidth     最大宽度
     * @param quality      压缩质量
     * @param adjustRotate 是否矫正方向
     * @return
     */
    public static Bitmap compressImage(String imagePath, String saveFile, int maxWidth, int maxHeight, int quality, boolean adjustRotate) {
        long start = System.currentTimeMillis();
        Log.i("DrawableUtil", "-----start_compress--");
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;  //图片实际高
        int actualWidth = options.outWidth;     //图片实际宽

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth * 1.0f / maxHeight;

//     按比例计算最终宽高
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight * 1.0f / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth * 1.0f / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = maxWidth;
            } else {
                actualHeight = maxHeight;
                actualWidth = maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options.outHeight, options.outWidth, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
//        Paint提供了FILTER_BITMAP_FLAG标示，这样的话在处理bitmap缩放的时候，就可以达到双缓冲的效果，图片处理的过程就更加顺畅了
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      矫正图片的方向
        if (adjustRotate) {
            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(saveFile);
            if (quality > 100 || quality < 0) {
                quality = 100;
            }
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("DrawableUtil", "compress total time:" + (System.currentTimeMillis() - start));
        return scaledBitmap;

    }


    /**
     * 计算出缩放比
     *
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(int actHeight, int actWidth, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (actHeight > reqHeight || actWidth > reqWidth) {
            final int heightRatio = Math.round((float) actHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) actWidth / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = actWidth * actHeight;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
