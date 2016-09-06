package com.example.administrator.mytestdemo.carousel.cb;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.ImageLoader;

public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        //可以通过layout文件来创建，也可以用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.drawable.ms_video_focus_icon);
        ImageLoader.loadImage(context, data, imageView);
    }
}
