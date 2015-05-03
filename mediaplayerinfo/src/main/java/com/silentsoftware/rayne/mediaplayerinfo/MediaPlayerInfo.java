package com.silentsoftware.rayne.mediaplayerinfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Steven on 4/15/2015.
 */
public class MediaPlayerInfo {
    private Context mContext;
    private List<ResolveInfo> mMediaPlayersInfo;
    private String mSelectedMediaPlayerPackageName;
    private ComponentName mComponentName;
    private Boolean mLaunchPlayer;

    public MediaPlayerInfo(Context context) {
        mContext = context;
        unflattenComponentName(PreferenceManager.getDefaultSharedPreferences(context).getString("media_player_list", ""));
        setLaunchPlayer(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("launch_player", false));
    }

    public List<ResolveInfo> getMediaPlayersInfo() {
        return mMediaPlayersInfo;
    }

    public String getSelectedMediaPlayerPackageName() {
        return mSelectedMediaPlayerPackageName;
    }

    public void setSelectedMediaPlayerPackageName(String selectedMediaPlayerPackageName) {
        this.mSelectedMediaPlayerPackageName = selectedMediaPlayerPackageName;
    }

    public Boolean getLaunchPlayer() {
        return mLaunchPlayer;
    }

    public void setLaunchPlayer(Boolean launchPlayer) {
        mLaunchPlayer = launchPlayer;
    }

    public void unflattenComponentName(String componentNameString) {
        mComponentName = ComponentName.unflattenFromString(componentNameString);
        mSelectedMediaPlayerPackageName = componentNameString.substring(0, componentNameString.indexOf("/"));
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
    public void sendMediaCommand(ComponentName componentName, int mediaCommand) {
        Intent mediaButtonDownIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent downKey = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, mediaCommand, 0);
        mediaButtonDownIntent.putExtra(Intent.EXTRA_KEY_EVENT, downKey);

        Intent mediaButtonUpIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent upKey = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, mediaCommand, 0);
        mediaButtonUpIntent.putExtra(Intent.EXTRA_KEY_EVENT, upKey);

        mediaButtonDownIntent.setComponent(componentName);
        mediaButtonUpIntent.setComponent(componentName);

        if (mLaunchPlayer && mediaCommand == KeyEvent.KEYCODE_MEDIA_PLAY) {
            ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();
            activityManager.getRunningTasks(100);
            List<ActivityManager.RunningTaskInfo> a = activityManager.getRunningTasks(100);
            Boolean appAlreadyRunning = false;
            Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(mSelectedMediaPlayerPackageName);
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
                if (processInfo.processName.equals(mSelectedMediaPlayerPackageName) && processInfo.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
                    appAlreadyRunning = true;
            }
            if (!appAlreadyRunning) {
                mContext.startActivity(launchIntent);
            }
        }
        mContext.sendOrderedBroadcast(mediaButtonDownIntent, null, null, null, Activity.RESULT_OK, null, null);
        mContext.sendOrderedBroadcast(mediaButtonUpIntent, null, null, null, Activity.RESULT_OK, null, null);
    }

    public void sendMediaCommand(int index, int mediaCommand) {
        ComponentName componentName = new ComponentName(mMediaPlayersInfo.get(index).activityInfo.packageName, mMediaPlayersInfo.get(index).activityInfo.name);
        sendMediaCommand(componentName, mediaCommand);
        Toast.makeText(mContext, mMediaPlayersInfo.get(index).activityInfo.packageName, Toast.LENGTH_LONG).show();
        Toast.makeText(mContext, mMediaPlayersInfo.get(index).activityInfo.name, Toast.LENGTH_LONG).show();
    }

    public void sendMediaCommand(int mediaCommand) {
        sendMediaCommand(mComponentName, mediaCommand);
    }

    public String getFlatComponentName(int index) {
        return new ComponentName(mMediaPlayersInfo.get(index).activityInfo.packageName, mMediaPlayersInfo.get(index).activityInfo.name).flattenToString();
    }
}
