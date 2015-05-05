package com.silentsoftware.rayne.bluetoothroundrobin;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Steven on 5/3/2015.
 */
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private AccessibilityServiceInfo mAccessibilityServiceInfo;
    private static Boolean mIsEnabled = false;

    @Override
    public void onDestroy() {
        mIsEnabled = false;
        super.onDestroy();
    }

    public static Boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        if (eventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return;
        final String packageName = (String) event.getPackageName();
        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Log.d("accessibility", packageName);
            Intent intent = new Intent();
            intent.putExtra("package_name", packageName);
            intent.setAction("com.silentsoftware.rayne.bluetoothroundrobin.accessibility.notificationchanged");
            sendBroadcast(intent);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        mAccessibilityServiceInfo = new AccessibilityServiceInfo();
        mAccessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        mAccessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        mAccessibilityServiceInfo.notificationTimeout = 100;
        this.setServiceInfo(mAccessibilityServiceInfo);
        mIsEnabled = true;
        super.onServiceConnected();
    }
}
