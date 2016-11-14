package com.example.administrator.mytestdemo.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;


public class DeviceUtil {

    //是否有虚拟导航按键
    public static boolean hasNavigationBar(Context ctx) {
        //判断是否有物理返回键、菜单键
        boolean hasMenuKey = ViewConfiguration.get(ctx)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !hasMenuKey && !hasBackKey;
    }

    //获取NavigationBar高度
    public static int getNavigationBarHeight(Context ctx) {
        if (hasNavigationBar(ctx)) {
            Resources resources = ctx.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height",
                    "dimen", "android");
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
