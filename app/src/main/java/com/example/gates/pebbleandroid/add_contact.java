package com.example.gates.pebbleandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by gates on 4/17/2016.
 */
public class add_contact extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        final EditText edittext = (EditText) findViewById(R.id.number);
        Button add = (Button) findViewById(R.id.submit_button);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] prev_array = loadArray("contacts", getApplicationContext());
                String valuenumber = edittext.getText().toString();
                String[] temp_array = {valuenumber};
                String[] final_array = concat(temp_array, prev_array);
                //add to array
                boolean commited = saveArray(final_array, "contacts", getApplicationContext());
                if (commited == true) {
                    Toast.makeText(getApplicationContext(), valuenumber + " added",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), valuenumber + " something went wrong",
                            Toast.LENGTH_LONG).show();
                }



                //redirect back
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

    }

    public boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("contacts", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("contacts", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public String[] concat(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c= new String[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}


