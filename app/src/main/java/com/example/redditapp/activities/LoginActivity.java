package com.example.redditapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;
import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.AuthenticationResponse;
import com.example.redditapp.model.LoginData;
import com.example.redditapp.service.AuthenticationApiService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Button buttonLogin = (Button) findViewById(R.id.loginBtn);
        Button buttonRegister = (Button) findViewById(R.id.registerBtn);
        Button buttonGuest = (Button) findViewById(R.id.guestBtn);

        EditText username = findViewById(R.id.loginUsername);
        EditText password = findViewById(R.id.loginPassword);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid = validLoginFields(username, password);

                if(valid){
                    LoginData data = new LoginData();
                    data.setUsername(username.getText().toString().trim());
                    data.setPassword(password.getText().toString().trim());
                    loginApi(data);
                }
            }
        });


        buttonGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : NAPRAVITI ACTIVITY ZA NEULOGOVANOG KORISNIKA
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
    }

    private void loginApi(LoginData loginData) {

        AuthenticationApiService authenticationApiService = RetrofitClientInstance.getRetrofitInstance(LoginActivity.this).create(AuthenticationApiService.class);
        Call<AuthenticationResponse> call = authenticationApiService.login(loginData);
        call.enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {

                try {
                    if(response.code() == 200){
                        AuthenticationResponse authResponse = response.body();

                        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN", authResponse.getAuthenticationToken()).apply();
                        preferences.edit().putString("username", authResponse.getUsername()).apply();
                        
                        openMain();
                        finish();
                    }
                    else if(response.code() == 403){
                        Toast.makeText(LoginActivity.this, "You are blocked", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.code() == 404) {
                        Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                Log.i(TAG, t.toString());
            }

        });
    }

    private boolean validLoginFields(EditText username, EditText password) {

        boolean valid = true;
        if(username.getText().toString().length() == 0 ) {
            username.setError("Username is required!");
            valid = false;
        }
        else {
            username.setError(null);
        }
        if(password.getText().toString().length() == 0 ) {
            password.setError("Password is required!");
            valid = false;
        }
        else {
            password.setError(null);
        }

        return valid;
    }

    public void openMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void openRegister(){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

}
