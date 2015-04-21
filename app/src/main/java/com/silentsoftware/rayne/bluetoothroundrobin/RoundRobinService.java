package com.silentsoftware.rayne.bluetoothroundrobin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RoundRobinService extends Service {
    private String mMode;

    public RoundRobinService() {
    }

    @Override
    public void onCreate() {
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
