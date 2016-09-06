package com.example.administrator.mytestdemo.carousel.cb;

import android.content.Context;
import android.view.View;

public interface Holder<T> {
    View createView(Context context);

    void UpdateUI(Context context, int position, T data);
}