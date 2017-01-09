package com.example.administrator.mytestdemo.stringbuilder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

/**
 * Created by lsq on 1/9/2017.
 */

public class BadgeView extends View {

    private String mText = "Loading";
    private int mTextColor;
    private int mTextSize;
    private Rect mTextBound;  //画笔,文本绘制范围
    private Paint mTextPaint;
    private int mTextWidth;     //文字宽
    private int mTextHeight;    //文字高
    private int mWidth;         //View宽
    private int mHeight;        //View高
    private int mRadius;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
           /*
         * 获取基本属性
		 */
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RTextView);
        mText = a.getString(R.styleable.RTextView_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.RTextView_textSize, 20);
        mTextColor = a.getColor(R.styleable.RTextView_textColor, Color.BLACK);
        a.recycle();

		/*
         * 初始化画笔
		 */
        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        KLog.i("-----w:" + w + "---h:" + h);
        mTextWidth = w;
        mTextHeight = h;
        setText(mText);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTextWidth = onMeasureR(0, widthMeasureSpec);
        mTextHeight = onMeasureR(1, heightMeasureSpec);
        setMeasuredDimension(mTextWidth, mTextHeight);
        KLog.i("-----width:" + mTextWidth + "---height:" + mTextHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        KLog.i("------onDraw");
        float startX = mWidth * 1.0f / 2;
        Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
        float startY = mHeight * 1.0f / 2 - fm.descent + (fm.bottom - fm.top) / 2.0f;

        //画圆角矩形
        mTextPaint.setColor(Color.LTGRAY);
        RectF oval3 = new RectF(0, 0, mWidth, mHeight);// 设置个新的长方形
        canvas.drawRoundRect(oval3, mRadius, mRadius, mTextPaint);//第二个参数是x半径，第三个参数是y半径

        // 绘制文字
        KLog.i("----startX:" + startX);
        KLog.i("----startY:" + startY);
        mTextPaint.setColor(mTextColor);
        canvas.drawText(mText, startX, startY, mTextPaint);
    }

    /**
     * 计算控件宽高
     *
     * @param widthHeightMode 0宽，1高
     * @param oldMeasure
     * @return
     */
    public int onMeasureR(int widthHeightMode, int oldMeasure) {
        int newSize = 0;
        int mode = MeasureSpec.getMode(oldMeasure);
        int oldSize = MeasureSpec.getSize(oldMeasure);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                newSize = oldSize;
                break;
            case MeasureSpec.AT_MOST:
                if (widthHeightMode == 0) {
                    newSize = measureTextWidth();
                } else if (widthHeightMode == 1) {
                    newSize = measureTextHeight();
                }
                break;
        }
        return newSize;
    }

    private int measureTextHeight() {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (int) Math.abs((fontMetrics.bottom - fontMetrics.top));
    }

    private int measureTextWidth() {
        return (int) mTextPaint.measureText(mText);
    }

    public void setText(String text) {
        mText = text;
        KLog.i("----需要绘制的文字是：" + mText);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mTextHeight = measureTextHeight();
        mTextWidth = measureTextWidth();

        mHeight = getPaddingTop() + getPaddingBottom() + mTextHeight;
        mRadius = mTextBound.height() / 2;
        mWidth = getPaddingLeft() + getPaddingRight() + mTextWidth + 2 * mRadius;

        getLayoutParams().width = mWidth;
        getLayoutParams().height = mHeight;
        setLayoutParams(getLayoutParams());
        invalidate();
    }
}
