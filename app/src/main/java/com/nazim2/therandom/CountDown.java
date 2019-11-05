package com.nazim2.therandom;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;


public class CountDown extends Activity {

   TextView n=null;
    int num=3;
    MediaPlayer count;
    boolean tag=true;



    @Override
    protected void onPause() {
        super.onPause();
    if(count!=null) count.release();
      finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);


        count=MediaPlayer.create(CountDown.this,R.raw.countdown);
        count.setLooping(true);
        count.start();

        n=(TextView)findViewById(R.id.num);


       if(tag) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   while (num > -1) {
                       CountDown.this.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {

                               n.setText("" + num--);
                               if (num < 0) {
                                   n.setText("GO !");
                                  if(count!=null) count.release();

                                   if(!CountDown.this.isFinishing()) startActivity(new Intent(CountDown.this,GameScreen.class));

                               }

                           }
                       });
                       try {
                           Thread.sleep(1000);


                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
           }).start();
       }
    }
    private void runOnUiThread() {

        n.setText("" + num--);
    }
}
