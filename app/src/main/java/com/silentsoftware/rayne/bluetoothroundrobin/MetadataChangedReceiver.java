package com.silentsoftware.rayne.bluetoothroundrobin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.silentsoftware.rayne.mediaplayerinfo.MediaPlayerInfo;

/**
 * Created by Steven on 5/2/2015.
 */
public class MetadataChangedReceiver extends BroadcastReceiver {
    private MediaPlayerInfo mMediaPlayerInfo;
    private Context mContext;
    private long time = 0;

    public MetadataChangedReceiver(Context context) {
        this.mContext = context;
        this.mMediaPlayerInfo = new MediaPlayerInfo(context);
    }

    public MediaPlayerInfo getMediaPlayerInfo() {
        return mMediaPlayerInfo;
    }

    public void setMediaPlayerInfo(MediaPlayerInfo mMediaPlayerInfo) {
        this.mMediaPlayerInfo = mMediaPlayerInfo;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean isValidIntent = false;
        if (intent.getAction().equals("com.silentsoftware.rayne.bluetoothroundrobin.notificationlistener.notificationadded") ||
                intent.getAction().equals("com.silentsoftware.rayne.bluetoothroundrobin.notificationlistener.notificationremoved")) {
            if (intent.getStringExtra("package_name").equals(mMediaPlayerInfo.getSelectedMediaPlayerPackageName())) {
                isValidIntent = true;
            }
        } else {
            isValidIntent = true;
        }
        if (isValidIntent) {
            if (time == 0)
                time = System.nanoTime();
            long timeElapsed = ((System.nanoTime() - time) / 1000000000);
            if (timeElapsed > mMediaPlayerInfo.getMinimumListeningTime()) {
                time = 0;
                stop();
            }
        }
        if (isValidIntent)
            Log.d("meta", "IsValid");
    }

    public void hostingStart() {
        mMediaPlayerInfo.sendMediaCommand(KeyEvent.KEYCODE_MEDIA_PLAY);
    }

    public void stop() {
        mMediaPlayerInfo.sendMediaCommand(KeyEvent.KEYCODE_MEDIA_PAUSE);
    }
}
