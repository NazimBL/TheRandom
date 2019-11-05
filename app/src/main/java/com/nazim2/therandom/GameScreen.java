package com.nazim2.therandom;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class GameScreen extends ActionBarActivity {

    int count=0,score=0,mprogress=0,max=400,j=0,list=0,n=0;
    String[] randomArray =new String[400];
    String[] said=new String[400];
    String[] boxArray =new String[15366];
    Register_Dialog registerDialog=null;
    MediaPlayer themeSong=null,correctMedia=null;
    boolean win=true,dialogTag=false,progressTag=false,soundTag=false;
    EditText e=null;
    Button ok;

    TextView scoreView,randomText,bestScore;
    ProgressBar pb;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

     if(!soundTag) {

         themeSong = MediaPlayer.create(GameScreen.this, R.raw.them);
         themeSong.start();
         themeSong.setLooping(true);
     }



        randomText = (TextView) findViewById(R.id.random_text);
        scoreView = (TextView) findViewById(R.id.score_view);
        bestScore= (TextView)findViewById(R.id.best_score);

        randomText.setText("random");


        e=(EditText) findViewById(R.id.edit_text);

        ok=(Button) findViewById(R.id.ok_button);

        pb=(ProgressBar) findViewById(R.id.pb);
        progressUpdate();


        bestScoreSave();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                said[count] = e.getText().toString();


                if (win && checkBox(e.getText().toString()) && usedByComputer() && usedByplayer() && checkRelation() && mprogress < 10) {


                    randomText.setText(randomWord());

                    randomArray[j] = randomText.getText().toString();
                    j++;
                    mprogress = 0;
                    score += 50;
                    scoreView.setText("Your Score : " + score);

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout_id));
                    TextView text = (TextView) layout.findViewById(R.id.tvtoast);
                    text.setText(" + 50 ! ");
                    text.setTextColor(Color.BLACK);
                    text.setTextSize(30);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);

                    correctMedia= MediaPlayer.create(GameScreen.this,R.raw.correct);
                    correctMedia.start();

                    toast.show();

                    if(!correctMedia.isPlaying()) correctMedia.release();
                    e.setText("");

                    win = true;
                    progressTag=true;
                    count++;
                } else {

                  themeSong.pause();
                    win = false;

                    if(score>max) {

                        dialogTag=true;
                        registerDialog=new Register_Dialog();
              registerDialog.show(getFragmentManager(),"Naz");

                  max=score;
                 bestScore.setText("Best Score : "+max);

                  }
               else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(GameScreen.this);
                        builder.setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(GameScreen.this,GameScreen.class));
                                GameScreen.this.finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GameScreen.this.finish();
                            }
                        }).setMessage("Replay ?").setTitle("GAME OVER").show();

                    }
                }
            }


        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        themeSong.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences save = getSharedPreferences("save",400);
        SharedPreferences.Editor editor = save.edit();
        editor.putInt("max", max);
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!themeSong.isPlaying() && !soundTag) themeSong.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!themeSong.isPlaying() && !soundTag) themeSong.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.sound: {

                if(!soundTag) {
                    themeSong.pause();
                    item.setIcon(R.drawable.mute);
                    soundTag = true;
                }

                else{
                    item.setIcon(R.drawable.sound_on);
                    if(!themeSong.isPlaying())themeSong.start();
                    soundTag=false;
                }

            }

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public boolean checkBox(String in) {

        boolean tag = false;
        try {
            AssetManager assetManager = getAssets();

            InputStream input;
            input = assetManager.open("Box.txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(input));
            String str;


            StringBuilder builder = new StringBuilder();
            while ((str = bf.readLine()) != null) {
                builder.append(str + "\n");

                if (str.equals(in))
                {
                    list=n;
                    tag = true;
                }
                n++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tag;
    }

    public void bestScoreSave(){

        SharedPreferences load = getSharedPreferences("save",0);
        max = load.getInt("max", 400);
        bestScore.setText("Best Score : "+max);
    }


    public void progressUpdate() {



        if (win == true) {
            mprogress = 0;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mprogress < 10) {


                        mprogress++;
                        boolean post = handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(mprogress);


                                if (mprogress >= 10) {

                                   themeSong.pause();
                                    if (win && !GameScreen.this.isFinishing()) {
                                        Toast.makeText(GameScreen.this, " OUT OF TIME", Toast.LENGTH_LONG).show();

                                        AlertDialog.Builder builder=new AlertDialog.Builder(GameScreen.this);
                                        builder.setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                startActivity(new Intent(GameScreen.this,GameScreen.class));
                                                GameScreen.this.finish();
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                GameScreen.this.finish();
                                            }
                                        }).setMessage("Replay ?").setTitle("GAME OVER").show();

                                    }
                                    if (score > max && dialogTag==false&&!GameScreen.this.isFinishing()) {
                                        registerDialog = new Register_Dialog();
                                       registerDialog.show(getFragmentManager(), "Naz");
                                    }
                                }
                            }

                        });
                  try
                            {
                              Thread.sleep(1000);
                            }
                   catch(InterruptedException e) { e.printStackTrace();}
                     };
                    }
            }).start();
        }
        }
    public boolean usedByComputer()  {
        boolean tag=true;
        for(int i=0;i<j;i++) {
            if(randomArray[i].equals(e.getText().toString())) tag=false;

        } return tag;
    }


    public String randomWord() {

        int rando = (int) (Math.random()*15364);

        try {

            AssetManager assetManager = getAssets();

            int i=0;
            InputStream input;
            input = assetManager.open("Box.txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(input));
            String str;


            while (i<15365 ) {
                str=bf.readLine();
                boxArray[i]=str;

                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(boxArray[rando].equals("*")) return boxArray[rando+1];

        else {
            return boxArray[rando];
        }
    }
    public boolean usedByplayer(){

        boolean tag=true;
        int i;
for(i=0;i<j;i++)
{ if(i!=count && (said[i].equals(said[count]))) tag=false;}


        return tag;
    }
    public int code(String str){

        int code=0,i=0;

        while (!boxArray[i].equals(str)) {
            if(boxArray[i].equals("*")) code++;
            i++;
        }

        return code;
    }
    public boolean checkRelation(){
        boolean tag=true;
        int i;

        if(count>0) {
for(i=0;i<j;i++)
{  if(code(e.getText().toString())==code(said[i]) ||code(e.getText().toString())==code(randomArray[i]) ) tag=false;}


        }   return tag;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(GameScreen.this);
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

