package com.silentsoftware.rayne.bluetoothroundrobin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.silentsoftware.rayne.mediaplayerinfo.MediaPlayerInfo;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.textList);
        tv.setText("New Text");
        MediaPlayerInfo mediaPlayers = new MediaPlayerInfo(this);
        mediaPlayers.refreshMediaPlayers();
        final int listSize = mediaPlayers.getMediaPlayersInfo().size();
        String mediaPlayersText = "";
        for (int i = 0; i < listSize; i++) {
            mediaPlayersText += mediaPlayers.getMediaPlayersInfo().get(i).activityInfo.applicationInfo.loadLabel(getPackageManager()) + "\n";
        }
        tv.setText(mediaPlayersText);
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageDrawable(mediaPlayers.getMediaPlayerIcon(1));
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
