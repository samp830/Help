package com.example.gates.pebbleandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.thingspace.cloud.sdk.CloudAPI;

public class MainActivity extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    public static AudioManager mAudioManager;
    Button startbutton;
    int counter = 1;
   public static CloudAPI cloudAPI;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    double lat;
    double longit;
    boolean isAuthorized;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create an instance of GoogleAPIClient.
        startbutton = (Button) findViewById(R.id.startButton);
        Button add = (Button) findViewById(R.id.add_contact);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), add_contact.class));

            }
        });

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

            }
        });


        cloudAPI = new CloudAPI("iki4v_jPHzsnc2qfa2pkA05p6TYa", "9pdMWhOdViiq2w1E3dt8O4zElQka", "http://localhost:8080/java-sdk-browser/callback");

        if(!cloudAPI.hasAuthorization(this)) {
            cloudAPI.authorize(this);
        }







    }

    @Override
    protected void onResume() {
        super.onResume();
        //mGoogleApiClient.connect();

        if(!cloudAPI.hasAuthorization(this) && !isAuthorized) {
            cloudAPI.authorize(this);
        }
        else {
            isAuthorized = true;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently



        // ...
    }

    @Override
    public void onConnected(Bundle connectionHint) {


    }


    @Override
    public void onConnectionSuspended(int i) {

    }
    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {

        super.onStop();
    }
    private void call(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:9086756366"));
        try{
            startActivity(callIntent);
            Toast.makeText(getApplicationContext(), "SMS done", Toast.LENGTH_LONG).show();
        }
        catch (SecurityException s){
            Toast.makeText(getApplicationContext(), "No Permissions", Toast.LENGTH_LONG).show();
            s.printStackTrace();
        }

    }

    protected void text(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String phoneNo = "9086756366";
        String location = " latitude: " + String.valueOf(lat) + " longitude: " + String.valueOf(longit);
        String message = preferences.getString("message", "I NEED HELP. MY LAST KNOWN LOCATION IS: " + location);
        try {
            SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), phoneNo, Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickStartService(View V)
    {
        if (counter % 2 == 1) {Intent i = new Intent(this, recorder.class);
            //i.setAction(com.wordpress.httpstheredefiningproductions.phonefinder.startforeground);
            startService(i);
            startbutton.setText("Stop Listening");
        }
        else {

            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            //Stop the running service from here//MyService is your service class name
            stopService(new Intent(this, recorder.class));

            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);


            startbutton.setText("Start Listening");
        }


        counter = counter + 1;

    }
}