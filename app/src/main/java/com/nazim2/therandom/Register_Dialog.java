package com.nazim2.therandom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;



public class Register_Dialog extends DialogFragment {

    LayoutInflater inflater=null;
    MediaPlayer nextEp=null;
    View view=null;

    @Override
    public void onStop() {
        super.onStop();
    nextEp.release();
    }


    @Override
    public void onResume() {
        super.onResume();
   nextEp.start();
    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

       getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        nextEp=MediaPlayer.create(getActivity(),R.raw.nextepz);
        nextEp.start();
        nextEp.setLooping(true);
        nextEp.setScreenOnWhilePlaying(true);
        inflater=getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.register_dialog,null);



        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());



        builder.setView(view).setCancelable(true).
        setCancelable(false).setNeutralButton("Come Back To Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        nextEp.release();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                });



        return builder.create();



        }

    }

