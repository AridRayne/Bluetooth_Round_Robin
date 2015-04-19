package com.silentsoftware.rayne.mediaplayerinfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

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
        try {
            return mContext.getPackageManager().getApplicationIcon(mMediaPlayersInfo.get(index).activityInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
