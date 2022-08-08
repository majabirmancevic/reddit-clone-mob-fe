package com.example.redditapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.User;
import com.example.redditapp.service.AuthenticationApiService;
import com.example.redditapp.service.UserApiService;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreenActivity extends Activity {

    static final String TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String usernamePref = preferences.getString("username", "");

        if(usernamePref == ""){
            //TODO : MAIN ACTIVITY ZA NEULOGOVANOG KORISNIKA
        }
        else if(usernamePref != "") {
            System.out.println("USER PREF NOW: " + usernamePref);
            findLoggedIn();
        }

        int SPLASH_TIME_OUT = 3000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void findLoggedIn() {

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String userPref = preferences.getString("username", "");
        Long id = preferences.getLong("id", 0L);

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(SplashScreenActivity.this).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(userPref);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                System.out.println(response.body());
                if(String.valueOf(user.getRole()).equals("USER")) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(String.valueOf(user.getRole()).equals("ADMIN")) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
