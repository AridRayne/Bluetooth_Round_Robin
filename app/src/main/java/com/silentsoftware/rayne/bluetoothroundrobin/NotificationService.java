package com.silentsoftware.rayne.bluetoothroundrobin;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Steven on 5/3/2015.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        Log.d("notification", "notification added");
//        Log.d("notification", "package " + sbn.getPackageName());
//        Log.d("notification", "ticker " + sbn.getNotification().tickerText);
        Intent intent = new Intent();
        intent.putExtra("package_name", sbn.getPackageName());
        intent.setAction("com.silentsoftware.rayne.bluetoothroundrobin.notificationlistener.notificationadded");
        sendBroadcast(intent);
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.d("notification", "notification removed");
        Intent intent = new Intent();
        intent.putExtra("package_name", sbn.getPackageName());
        intent.setAction("com.silentsoftware.rayne.bluetoothroundrobin.notificationlistener.notificationremoved");
        sendBroadcast(intent);
        super.onNotificationRemoved(sbn);
    }
}
