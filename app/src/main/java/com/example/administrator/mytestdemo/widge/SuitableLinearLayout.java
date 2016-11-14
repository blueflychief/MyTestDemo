package com.example.administrator.mytestdemo.widge;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.example.administrator.mytestdemo.util.DeviceUtil;


/**
 * 解决popwindow在navigationbar显示与不显示的便宜问题
 */

public class SuitableLinearLayout extends LinearLayout {
    private Context context;

    public SuitableLinearLayout(Context context) {
        this(context,null);
    }

    public SuitableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public SuitableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!isOffset()) {
            if (this.getPaddingBottom()!=0) {
                this.setPadding(0, 0, 0, 0);
            }
        } else{
            this.setPadding(0, 0, 0, DeviceUtil.getNavigationBarHeight(context));
            invalidate();
        }
        super.onLayout(true, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public boolean isOffset() {
        return getDecorViewHeight() > getScreenHeight();
    }


    public int getDecorViewHeight(){
        return ((Activity)this.context).getWindow().getDecorView().getHeight();
    }

    public int getScreenHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
