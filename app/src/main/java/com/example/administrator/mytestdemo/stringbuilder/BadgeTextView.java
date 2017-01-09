package com.example.administrator.mytestdemo.stringbuilder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;

/**
 * Created by lsq on 1/9/2017.
 */

public class BadgeTextView extends TextView {
    public int DEF_COLOR = Color.parseColor("#FD3737");

    private Paint mPaint;
    private Paint mTextPaint;

    private int mCount;
    /**
     * 是否显示红点
     */
    boolean isShowBadge;

    /**
     * 红点宽度
     */
    private int mBadgeWidth;
    /**
     * 红点高度
     */
    private int mBadgeHeight;
    /**
     * 红点半径
     */
    private int circleRadius;
    private int dp1;

    private RectF rectF;
    /**
     * 视图宽度
     */
    private int mViewWidth;

    private int mViewHeight;

    /**
     * 红点距离视图上边距
     */
    private int mPaddingTop;
    /**
     * 红点距离视图右边距
     */
    private int mPaddingRight;

    public BadgeTextView(Context context) {
        this(context, null);
    }

    public BadgeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray mArray = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);
        mPaddingTop = mArray.getDimensionPixelOffset(R.styleable.BadgeView_badge_padding_top, 0);
        mPaddingRight = mArray.getDimensionPixelOffset(R.styleable.BadgeView_badge_padding_right, 0);
        mCount = mArray.getInteger(R.styleable.BadgeView_badge_count, 0);
        isShowBadge = mArray.getBoolean(R.styleable.BadgeView_badge_none_show, false);
        DEF_COLOR = mArray.getColor(R.styleable.BadgeView_badge_color, DEF_COLOR);
        isShowBadge = (mCount > 0);
        dp1 = dip2px(getContext(), 1);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(DEF_COLOR);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);
        resetCount();
        mArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        rectF = new RectF(mViewWidth - mBadgeWidth - mPaddingRight,
                mPaddingTop,
                mViewWidth - mPaddingRight,
                mBadgeHeight + mPaddingTop);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowBadge) {
            if (mCount < 10) {//圆
                canvas.drawCircle(mViewWidth - mBadgeWidth / 2 - mPaddingRight, mBadgeHeight / 2 + mPaddingTop, circleRadius, mPaint);
            } else {//椭圆
                canvas.drawRoundRect(rectF, (int) (mBadgeWidth * 0.6), (int) (mBadgeWidth * 0.6), mPaint);
            }
            if (mCount > 0) {
                Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
                int baseline = (mBadgeHeight + 0 - fontMetrics.bottom - fontMetrics.top) / 2;
                canvas.drawText(mCount + "", mViewWidth - mBadgeWidth / 2 - mPaddingRight, baseline + mPaddingTop, mTextPaint);
            }
        }
    }


    /**
     * 设置小红点
     *
     * @param count
     * @return
     */
    public BadgeTextView setCount(int count) {
        this.mCount = count;
        resetCount();
        return this;
    }

    public BadgeTextView setShown(boolean isShowBadge) {
        this.isShowBadge = isShowBadge;
        invalidate();
        return this;
    }

    /**
     * 设置颜色
     *
     * @param color
     * @return
     */
    public BadgeTextView setColor(int color) {
        mPaint.setColor(color);
        invalidate();
        return this;
    }


    private void resetCount() {
        if (mCount > 99) {
            mCount = 99;
        }

        if (mCount >= 10) {
            mBadgeWidth = dp1 * getRectWidthDp(getContext());
            mBadgeHeight = dp1 * getCircleDp(getContext());
        } else if (mCount > 0) {
            mBadgeWidth = dp1 * getCircleDp(getContext());
            mBadgeHeight = dp1 * getCircleDp(getContext());
        } else {
            mBadgeHeight = mBadgeWidth = dp1 * getNoneDp(getContext());
        }
        circleRadius = mBadgeWidth / 2;

        mTextPaint.setTextSize(mBadgeHeight * 0.8f);

        invalidate();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 得到默认的红点宽高
     *
     * @param mContext
     * @return
     */
    public static int getCircleDp(Context mContext) {
        if (mContext.getResources().getDisplayMetrics().densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return 10;
        } else {
            return 15;
        }
    }

    /**
     * 得到大于10的红点宽
     *
     * @param mContext
     * @return
     */
    public static int getRectWidthDp(Context mContext) {
        if (mContext.getResources().getDisplayMetrics().densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return 15;
        } else {
            return 25;
        }
    }

    /**
     * 得到无红点的dp大小
     *
     * @param mContext
     * @return
     */
    public static int getNoneDp(Context mContext) {
        if (mContext.getResources().getDisplayMetrics().densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return 6;
        } else {
            return 7;
        }
    }
}
