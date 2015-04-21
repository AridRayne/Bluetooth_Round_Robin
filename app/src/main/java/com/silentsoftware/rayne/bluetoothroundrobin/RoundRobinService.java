package com.silentsoftware.rayne.bluetoothroundrobin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;

public class RoundRobinService extends Service {
    private String mMode;
    private WifiP2pManager mWifiManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mBroadcastReceiver;

    public RoundRobinService() {
    }

    @Override
    public void onCreate() {
        mWifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiManager.initialize(this, getMainLooper(), null);
        mBroadcastReceiver = new WifiDirectBroadcastReceiver(mWifiManager, mChannel, this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMode = intent.getStringExtra("mode");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
