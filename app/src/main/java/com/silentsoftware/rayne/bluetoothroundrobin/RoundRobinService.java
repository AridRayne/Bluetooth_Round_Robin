package com.silentsoftware.rayne.bluetoothroundrobin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.util.Log;

import static android.app.Notification.FLAG_ONGOING_EVENT;

public class RoundRobinService extends Service {
    private String mMode;
    private WifiP2pManager mWifiManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mWifiDirectBroadcastReceiver;
    private IntentFilter mWifiDirectIntentFilter;
    private MetadataChangedReceiver mMetadataBroadcastReceiver;
    private IntentFilter mMetadataIntentFilter;
    private NotificationManager mNotificationManager;

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

        mMetadataBroadcastReceiver = new MetadataChangedReceiver(this);
        mMetadataIntentFilter = new IntentFilter();

        //Standard android broadcast intents
        mMetadataIntentFilter.addAction("com.android.music.metachanged");
        mMetadataIntentFilter.addAction("com.android.music.playbackcomplete");
        mMetadataIntentFilter.addAction("com.android.music.playstatechanged");

        //iHeartRadio broadcast intents
        mMetadataIntentFilter.addAction("player_manager_on_meta_data_changed_action");
        mMetadataIntentFilter.addAction("player_manager_on_state_changed_action");
        mMetadataIntentFilter.addAction("player_manager_on_track_changed_action");

//        mMetadataIntentFilter.addAction("com.adam.aslfms.notify.playstatechanged");
        registerReceiver(mMetadataBroadcastReceiver, mMetadataIntentFilter);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

        if (mMode.equals("host")) {
            showNotification(true);
            mMetadataBroadcastReceiver.hostingStart();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        mMetadataBroadcastReceiver.stop();
        unregisterReceiver(mWifiDirectBroadcastReceiver);
        unregisterReceiver(mMetadataBroadcastReceiver);
        clearNotification();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private void showNotification(Boolean hosting) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        Notification notification = new Notification(R.mipmap.ic_launcher, "Service Running", System.currentTimeMillis());
        notification.flags = FLAG_ONGOING_EVENT;
        if (hosting)
            notification.setLatestEventInfo(this, getText(R.string.app_name), "Hosting the party", pendingIntent);
        else
            notification.setLatestEventInfo(this, getText(R.string.app_name), "Enjoying the party", pendingIntent);
        mNotificationManager.notify(0, notification);
    }

    private void clearNotification() {
        mNotificationManager.cancel(0);
    }
}
