package com.example.dhp.fullscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final String mypreference = "mypref";
    Button button1;
    boolean flag = false;
    SharedPreferences store;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        store = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        final String prefBool = "prefBool";
        if (store.contains(prefBool)) {
            flag = store.getBoolean(prefBool, false);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = store.edit();
                if (!flag) {
                    try {
                        Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
                        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
                        stdin.writeBytes("settings put global policy_control immersive.full=*\n");
                        stdin.flush();
                        flag = true;
                        editor.putBoolean(prefBool, true).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
                        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
                        stdin.writeBytes("settings put global policy_control immersive.full=null\n");
                        stdin.flush();
                        flag = false;
                        editor.putBoolean(prefBool, false).commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    button1.performClick();
    finish();
    }
}
