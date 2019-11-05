package com.nazim2.therandom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends Activity {
    Button start_b = null, rules_b = null;
    MediaPlayer themeSong = null;
    ImageButton rate;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        themeSong = MediaPlayer.create(MainActivity.this, R.raw.them);
        themeSong.start();
        themeSong.setLooping(true);
        MobileAds.initialize(this, "ca-app-pub-2561306222851578~8604491646");


        mAdView = (AdView) findViewById(R.id.ad_view);


        AdRequest adRequest = new AdRequest.Builder()

                .build();


        mAdView.loadAd(adRequest);


        start_b = (Button) findViewById(R.id.start_b);
        rules_b = (Button) findViewById(R.id.rules_b);

        rate=(ImageButton)findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, " Rate and Comment my Apps :p  ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse("market://details?id=com.nazim2.therandom&hl=fr"));
                if (!MyStartActivity(intent)) {

                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.nazim2.therandom&hl=fr"));
                    if (!MyStartActivity(intent)) {

                        Toast.makeText(MainActivity.this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        start_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, CountDown.class));

            }
        });
        rules_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, RulesActivity.class));

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        themeSong.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!themeSong.isPlaying()) themeSong.start();
    }
    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit Game").setMessage("Are you sur you want to quit ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }


    }
