package com.example.administrator.mytestdemo.recyclerview;

/**
 * Created by Administrator on 10/19/2016.
 */

import android.support.v7.widget.GridLayoutManager;

/**
 * 该类用于生成交错效果，要么 1 行 2 列，要么 1 行 1 列
 */
public class GridStaggerLookup extends GridLayoutManager.SpanSizeLookup {

    @Override
    public int getSpanSize(int position) {

        // 每隔 3 个位置占据 2 列，其他位置则占 1 列
        int pos = position % 3 == 0 ? 2 : 1;
        return pos;
    }
}