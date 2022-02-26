package com.shrewd.poppydealers.utilities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.id, Constants.description, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(Constants.description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
    }
}
