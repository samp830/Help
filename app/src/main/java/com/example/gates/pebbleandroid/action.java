package com.example.gates.pebbleandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by gates on 4/17/2016.
 */
public class action extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    double lat;
    double longit;

    public GoogleApiClient mGoogleApiClient;
    String[] phoneNos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.action);

        phoneNos = loadArray("contacts", getApplicationContext());
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



        mGoogleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onConnected(Bundle connectionHint) {


        Toast.makeText(getApplicationContext(), " on connect!!",
                Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details
            Toast.makeText(getApplicationContext(), " fuck your permission!!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            longit = mLastLocation.getLongitude();

            //get the current time
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            String[] datatoenter = new String[] {"lat: " + String.valueOf(lat), "long: " + String.valueOf(longit), "day: " + String.valueOf(day)};

            final String filename = "data.txt";
            final File root = new File(Environment.getExternalStorageDirectory().getPath());
            Log.d(">>>>>>>>>>>", Environment.getExternalStorageDirectory().getPath());
            File file = new File(root, filename);


            if (!file.exists()) {
                try {
                    file.createNewFile();
                    FileOutputStream writer = null;
                    try {
                        writer = openFileOutput(file.getName(), Context.MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    for (String string: datatoenter){
                        writer.write(string.getBytes());
                        writer.flush();
                    }

                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


new Thread() {
    public void run() {
        Log.d(">>>>>>>>>>>", filename);
        Log.d(">>>>>>>>>>>", root.getPath());
        if (MainActivity.cloudAPI.uploadFile(getApplicationContext(), "/VZMOBILE", filename, root.getPath())) {
            Log.d(">>>>>>>>>>>", "File Uploaded");

        } else {
            Log.d(">>>>>>>>>>>", "File Not Uploaded");
        }
    }
}.start();





            text();
            call();

        }
        else {
            Toast.makeText(getApplicationContext(), "null!!",
                    Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection Failed!",
                Toast.LENGTH_LONG).show();

    }


    private void call(){
        for (String s: phoneNos) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + s));
            try {
                startActivity(callIntent);
                Toast.makeText(getApplicationContext(), "SMS done", Toast.LENGTH_LONG).show();
            } catch (SecurityException q) {
                Toast.makeText(getApplicationContext(), "No Permissions", Toast.LENGTH_LONG).show();
                q.printStackTrace();
            }
        }

    }

    protected void text(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        for (String s: phoneNos) {
            //Do your stuff here
            String location = " latitude: " + String.valueOf(lat) + " longitude: " + String.valueOf(longit);
            String message = preferences.getString("message", "I NEED HELP. MY LAST KNOWN LOCATION IS: " + location);
            try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(s, null, message, null, null);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
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

    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("contacts", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }


}
