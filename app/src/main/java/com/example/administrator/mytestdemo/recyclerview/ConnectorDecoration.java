package com.example.administrator.mytestdemo.recyclerview;

/**
 * Created by Administrator on 10/19/2016.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

import com.example.administrator.mytestdemo.R;

/**
 *  在主要和次要网格项之间绘制连接线
 */
public class ConnectorDecoration extends ItemDecoration {

    private Paint mLinePaint;
    private int mLineLength;

    public ConnectorDecoration(Context context) {
        super();
        mLineLength =context.getResources()
                .getDimensionPixelOffset(R.dimen.inset_margin);
        int connectorStroke = context.getResources()
                .getDimensionPixelOffset(R.dimen.connector_stroke);

        // 允许锯齿
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(connectorStroke);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        final RecyclerView.LayoutManager manager = parent.getLayoutManager();

        // 遍历子视图，并在未占据两列的每个子项上绘制中心线
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            boolean isLarge = parent
                    .getChildViewHolder(child)
                    .getPosition() % 3 == 0;

            // 得到视图当前所在的位置，以此绘制线段
            if (!isLarge) {
                final int childLeft = manager.getDecoratedLeft(child);
                final int childRight = manager.getDecoratedRight(child);
                final int childTop = manager.getDecoratedTop(child);
                final int x = childLeft + ((childRight - childLeft) / 2);

                c.drawLine(x, childTop - mLineLength,
                        x, childTop + mLineLength,
                        mLinePaint);
            }
        }
    }
}