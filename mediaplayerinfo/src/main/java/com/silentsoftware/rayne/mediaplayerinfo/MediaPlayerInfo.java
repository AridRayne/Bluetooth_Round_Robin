package com.silentsoftware.rayne.mediaplayerinfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Steven on 4/15/2015.
 */
public class MediaPlayerInfo {
    private Context _context;
    private List<ResolveInfo> _media_players_info;

    public MediaPlayerInfo(Context context) {
        _context = context;
    }

    public List<ResolveInfo> get_media_players_info() {
        return _media_players_info;
    }

    public ResolveInfo get_media_player_info(int index) {
        return _media_players_info.get(index);
    }

    public void RefreshMediaPlayers() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        _media_players_info = _context.getPackageManager().queryBroadcastReceivers(intent, 0);
    }
}
