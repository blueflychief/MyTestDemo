package com.example.administrator.mytestdemo.photofilterssdk.imageprocessors.sdk.subfilters;

import android.graphics.Bitmap;

import com.example.administrator.mytestdemo.photofilterssdk.imageprocessors.sdk.ImageProcessor;
import com.example.administrator.mytestdemo.photofilterssdk.imageprocessors.sdk.SubFilter;


/**
 * @author varun on 28/07/15.
 */
public class SaturationSubFilter implements SubFilter {
    private static String tag = "";

    // The Level value is float, where level = 1 has no effect on the image
    private float level;

    public SaturationSubFilter(float level) {
        this.level = level;
    }

    @Override
    public Bitmap process(Bitmap inputImage) {
        return ImageProcessor.doSaturation(inputImage, level);
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        SaturationSubFilter.tag = (String) tag;
    }

    public void setLevel(float level) {
        this.level = level;
    }
}
