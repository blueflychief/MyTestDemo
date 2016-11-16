package com.example.administrator.mytestdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.elvishew.xlog.KLog;

public class ImageLoader {

    public static void loadImage(Context context, String url, final ImageView imageView) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (bitmap != null) {
                    KLog.i("-----width:"+bitmap.getWidth());
                    KLog.i("-----height:"+bitmap.getHeight());
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }

    public static void loadImage(Context context, int resId, final ImageView imageView) {
        Glide.with(context).load(resId).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (bitmap != null) {
                    KLog.i("-----width:"+bitmap.getWidth());
                    KLog.i("-----height:"+bitmap.getHeight());
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }
}
