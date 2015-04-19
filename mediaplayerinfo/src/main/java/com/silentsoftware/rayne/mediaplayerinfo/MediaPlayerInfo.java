package com.silentsoftware.rayne.mediaplayerinfo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.KeyEvent;

import java.util.List;

/**
 * Created by Steven on 4/15/2015.
 */
public class MediaPlayerInfo {
    private Context mContext;
    private List<ResolveInfo> mMediaPlayersInfo;

    public MediaPlayerInfo(Context context) {
        mContext = context;
    }

    public List<ResolveInfo> getMediaPlayersInfo() {
        return mMediaPlayersInfo;
    }

    public void refreshMediaPlayers() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mMediaPlayersInfo = mContext.getPackageManager().queryBroadcastReceivers(intent, 0);
    }

    public Drawable getMediaPlayerIcon(String packageName) {
        try {
            return mContext.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public Drawable getMediaPlayerIcon(int index) {
        return getMediaPlayerIcon(mMediaPlayersInfo.get(index).activityInfo.packageName);
    }

    /**
     * Implemented from <a href="https://code.google.com/p/media-button-router/">MediaButtonRouter</a>
     *
     * @param componentName
     */
    public void sendMedia(ComponentName componentName, int mediaCommand) {
        Intent mediaButtonDownIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent downKey = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, mediaCommand, 0);
        mediaButtonDownIntent.putExtra(Intent.EXTRA_KEY_EVENT, downKey);

        Intent mediaButtonUpIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent upKey = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, mediaCommand, 0);
        mediaButtonUpIntent.putExtra(Intent.EXTRA_KEY_EVENT, upKey);

        mediaButtonDownIntent.setComponent(componentName);
        mediaButtonUpIntent.setComponent(componentName);

        mContext.sendOrderedBroadcast(mediaButtonDownIntent, null, null, null, Activity.RESULT_OK, null, null);
        mContext.sendOrderedBroadcast(mediaButtonUpIntent, null, null, null, Activity.RESULT_OK, null, null);
    }

    public void sendMediaCommand(int index, int mediaCommand) {
        ComponentName componentName = new ComponentName(mMediaPlayersInfo.get(index).activityInfo.packageName, mMediaPlayersInfo.get(index).activityInfo.name);
        sendMedia(componentName, mediaCommand);
    }
}
