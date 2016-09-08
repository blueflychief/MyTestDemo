package com.example.administrator.mytestdemo.photofilterssdk.imageprocessors;

import android.graphics.Bitmap;

import com.example.administrator.mytestdemo.photofilterssdk.imageprocessors.sdk.Filter;


/**
 * @author Varun on 01/07/15.
 */
public class ThumbnailItem {
    public Bitmap image;
    public Filter filter;

    public ThumbnailItem() {
        image = null;
        filter = new Filter();
    }
}
