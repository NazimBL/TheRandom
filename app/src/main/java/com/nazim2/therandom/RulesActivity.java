package com.nazim2.therandom;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;



public class RulesActivity extends Activity {

    MediaPlayer themeSong=null;


    protected void onPause(){
        super.onPause();
    themeSong.pause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        themeSong = MediaPlayer.create(RulesActivity.this, R.raw.them);

        themeSong.start();
        themeSong.setScreenOnWhilePlaying(true);
        themeSong.setLooping(true);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(!themeSong.isPlaying()) themeSong.start();
    }

}
