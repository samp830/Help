package com.example.gates.pebbleandroid;

/**
 * Created by gates on 4/17/2016.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class recorder extends Service
{

    public static PowerManager mgr;






    private static int FOREGROUND_ID=1338;



    //methods



    //other stuff

    static public AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    static public Vibrator v;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));
    protected boolean mIsListening;
    protected volatile boolean mIsCountDownOn;

    static String TAG = "Icaro";

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    private int mBindFlag;
    private Messenger mServiceMessenger;

    @Override
    public void onCreate()
    {
        super.onCreate();
        v =  (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<recorder> mtarget;

        IncomingHandler(recorder target)
        {
            mtarget = new WeakReference<recorder>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final recorder target = mtarget.get();

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:

                    if (Build.VERSION.SDK_INT >= 16);//Build.VERSION_CODES.JELLY_BEAN)
                {

                }
                if (!target.mIsListening)
                {


                    target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                    target.mIsListening = true;


                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER, +AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,  + AudioManager.FLAG_PLAY_SOUND);




                }

                break;

                case MSG_RECOGNIZER_CANCEL:
                    target.mSpeechRecognizer.cancel();
                    target.mIsListening = false;
                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
                    mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);

                    try {
                        Thread.sleep((1000));
                    } catch (InterruptedException e) {
                    }
                    break;
            }
        }
    }



    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub


        }

        @Override
        public void onFinish()
        {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
                mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
            }
            catch (RemoteException e)
            {

            }
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
        }
    };

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        Toast.makeText(getApplicationContext(), "app started",
                Toast.LENGTH_LONG).show();

        try
        {
            Message msg = new Message();
            msg.what = MSG_RECOGNIZER_START_LISTENING;
            mServerMessenger.send(msg);
        }
        catch (RemoteException e)
        {

        }
        return  START_STICKY;
    }




    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false );
        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {
        SoundPool sp;
        int sound=0;

        @Override
        public void onBeginningOfSpeech()
        {
            mAudioManager = null;
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }

            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {
            String sTest = "";
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
        }

        @Override
        public void onEndOfSpeech()
        {
            mAudioManager = null;
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);

        }

        @Override
        public void onError(int error)
        {
            mAudioManager = null;
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }


            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try
            {
                mIsListening = false;
                mServerMessenger.send(message);
                // mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true  );
                mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);


            }
            catch (RemoteException e)
            {

            }




        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
            if (Build.VERSION.SDK_INT >= 16);//Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();


            }

            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false  );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
        }

        @Override
        public void onResults(Bundle results)
        {

            mAudioManager = null;
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


            //make it have sound again
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false );
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
            //start of stuff to do with input
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            sound = sp.load(recorder.this, R.raw.sound1, 1);

            String result;
            String help="help";


            //amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            // for (int i = 0; i < data.size(); i++) {
            //amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);

            result = (String) data.get(0);
            mAudioManager = null;
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //Toast.makeText(getApplicationContext(), result,
            //        Toast.LENGTH_LONG).show();

            if ((result.toLowerCase().contains(help.toLowerCase()) == true )) {
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);





                mAudioManager = null;
                mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false );
                mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                // mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
                mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);


                if (sound != 0) {
                    v.vibrate(3000);
                    sp.play(sound, 1, 1, 0, 0, 1);
                    try{ Thread.sleep(1300); }catch(InterruptedException e){ }
                    sp.play(sound, 1, 1, 0, 0, 1);
                    try{ Thread.sleep(1300); }catch(InterruptedException e){ }



                }
                //invoke activity to connect
                Intent dialogIntent = new Intent(getBaseContext(), action.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(dialogIntent);



            }

            //}
            //end of stuff to do with input
            //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try
            {
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }







            //if the beep has just been called

            if ((result.toLowerCase().contains(help.toLowerCase()) == true ))
            {
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false );
                mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
            }//then if it did not beep then we go ahead and wait

            else{
                //try {
                //    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false );
                //     mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
                //     Thread.sleep((30000));
                //   } catch (InterruptedException e) {
                //    }
            }


        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}