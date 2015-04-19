package com.silentsoftware.rayne.bluetoothroundrobin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.silentsoftware.rayne.mediaplayerinfo.MediaPlayerInfo;


public class MainActivity extends ActionBarActivity {
    private MediaPlayerInfo mMediaPlayers;
    private Boolean mPlaying = false;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.textList);
        tv.setText("New Text");
        mMediaPlayers = new MediaPlayerInfo(this);
        mMediaPlayers.refreshMediaPlayers();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tv.setText(mSharedPreferences.getString("media_player_list", ""));
    }

    public void startPlaying(View v) {
        mMediaPlayers.unflattenComponentName(mSharedPreferences.getString("media_player_list", ""));
        if (!mPlaying) {
            mMediaPlayers.sendMediaCommand(KeyEvent.KEYCODE_MEDIA_PLAY);
            mPlaying = true;
        } else {
            mMediaPlayers.sendMediaCommand(KeyEvent.KEYCODE_MEDIA_PAUSE);
            mPlaying = false;
        }
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
}
