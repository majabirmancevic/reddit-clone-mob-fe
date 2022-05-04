package com.example.redditapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.redditapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIME_OUT = 3000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
