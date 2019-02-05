package com.webxert.attrixstudent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!AppGenericClass.getInstance(Splash.this).getPrefs(AppGenericClass.LOGGED_IN).equals("")) {
                    startActivity(new Intent(Splash.this, Home.class));
                    finish();
                } else {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }
}
