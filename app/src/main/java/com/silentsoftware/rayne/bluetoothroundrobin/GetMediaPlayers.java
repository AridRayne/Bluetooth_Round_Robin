package com.silentsoftware.rayne.bluetoothroundrobin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Steven on 4/15/2015.
 */
public class GetMediaPlayers {
    private Context _context;
    private List<ResolveInfo> _mediaPlayers;

    public GetMediaPlayers(Context context) {
        _context = context;
    }

    public List<ResolveInfo> get_mediaPlayers() {
        return _mediaPlayers;
    }

    public void RefreshMediaPlayers() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        _mediaPlayers = _context.getPackageManager().queryBroadcastReceivers(intent, 0);
    }
}
