package com.silentsoftware.rayne.bluetoothroundrobin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.silentsoftware.rayne.mediaplayerinfo.MediaPlayerInfo;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private MediaPlayerInfo mMediaPlayers;
    private Boolean mPlaying = false;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaPlayers = new MediaPlayerInfo(this);
        mMediaPlayers.refreshMediaPlayers();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, RoundRobinService.class);
        switch (position) {
            case 0:
                intent.putExtra("mode", "host");
                this.startService(intent);
                break;
            case 1:
                intent.putExtra("mode", "client");
                this.startService(intent);
                break;
            case 2:
                this.stopService(intent);
                break;
            default:
                break;
        }
    }
}
