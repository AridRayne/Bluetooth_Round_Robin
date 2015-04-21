package com.silentsoftware.rayne.bluetoothroundrobin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by Steven on 4/21/2015.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    RoundRobinService mService;

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, RoundRobinService service) {
        mManager = manager;
        mChannel = channel;
        mService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
