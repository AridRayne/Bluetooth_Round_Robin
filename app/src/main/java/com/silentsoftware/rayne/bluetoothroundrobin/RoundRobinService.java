package com.silentsoftware.rayne.bluetoothroundrobin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.util.Log;

public class RoundRobinService extends Service {
    private String mMode;
    private WifiP2pManager mWifiManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mWifiDirectBroadcastReceiver;
    private IntentFilter mWifiDirectIntentFilter;

    public RoundRobinService() {
    }

    @Override
    public void onCreate() {
        mWifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiManager.initialize(this, getMainLooper(), null);
        mWifiDirectBroadcastReceiver = new WifiDirectBroadcastReceiver(mWifiManager, mChannel, this);
        mWifiDirectIntentFilter = new IntentFilter();
        mWifiDirectIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mWifiDirectIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mWifiDirectIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mWifiDirectIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(mWifiDirectBroadcastReceiver, mWifiDirectIntentFilter);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMode = intent.getStringExtra("mode");
        mWifiManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("p2p", "Found Peers");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("p2p", "Failed " + reason);
            }

        });
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mWifiDirectBroadcastReceiver);
//        unregisterReceiver(mMetadataBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
