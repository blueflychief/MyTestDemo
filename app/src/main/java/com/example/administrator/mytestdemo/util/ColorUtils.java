package com.example.administrator.mytestdemo.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * Generate thumb and background color state list use tintColor
 * Created by kyle on 15/11/4.
 */
public class ColorUtils {
    private static final int ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int CHECKED_ATTR = android.R.attr.state_checked;
    private static final int PRESSED_ATTR = android.R.attr.state_pressed;

    public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {PRESSED_ATTR, -CHECKED_ATTR},
                {PRESSED_ATTR, CHECKED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {CHECKED_ATTR, PRESSED_ATTR},
                {-CHECKED_ATTR, PRESSED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor - 0xD0000000,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }

    public static SpannableStringBuilder formatColor(Context context, String str, int colorId, int start_index, int end_index) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        formatColor(context, colorId, start_index, end_index, style);
        return style;
    }

    //格式化颜色
    public static SpannableStringBuilder formatColor(Context context, int stringId, String str, int colorId, int startIndex) {
        String s = String.format(context.getResources().getString(stringId), str);
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        formatColor(context, colorId, startIndex, s.length(), style);
        return style;
    }

    public static void formatColor(Context context, int colorId, int start_index, int end_index, SpannableStringBuilder style) {
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorId)), start_index, end_index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
