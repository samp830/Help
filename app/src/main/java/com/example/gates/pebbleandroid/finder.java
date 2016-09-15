package com.example.gates.pebbleandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by gates on 4/17/2016.
 */
public class finder extends Activity {
    //used to see whcih service to launch
    public static int whichservice = 1;
    Button startbutton;
    public static AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finder);
        startbutton = (Button) findViewById(R.id.startbutton);
        startbutton.performClick();




    }



    //start the service
    public void onClickStartService(View V)
    {



        //used to start

        Intent i = new Intent(this, recorder.class);
        //i.setAction(com.wordpress.httpstheredefiningproductions.phonefinder.startforeground);
        startService(i);




    }


    //Stop the started service
    public void onClickStopService(View V)
    {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Stop the running service from here//MyService is your service class name
        stopService(new Intent(this, recorder.class));

        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
        mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
    }


    @Override
    public void onResume(){
        super.onResume();
        // recorder.stopcpu();
        stopService(new Intent(this, recorder.class));



    }
}