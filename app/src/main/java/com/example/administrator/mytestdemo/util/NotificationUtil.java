package com.example.administrator.mytestdemo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.administrator.mytestdemo.R;

/**
 * Created by LuoShuiquan on 9/1/2016.
 */
public class NotificationUtil {


    public static NotificationManager createNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.mipmap.ic_launcher, "下载", System
                .currentTimeMillis());

        RemoteViews view = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.view_notify_progress);
        notification.contentView = view;
        PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;
        return notificationManager;
    }
}
