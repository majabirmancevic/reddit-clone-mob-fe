package com.example.redditapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.User;
import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.service.UserApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuspendCommunityActivity extends AppCompatActivity {


   // User user;
    boolean admin;
    Button btnSuspend;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suspend_community);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));



        EditText reason = findViewById(R.id.suspendReason);
        Button btnSuspend = findViewById(R.id.buttonSuspend_community);


        btnSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suspendCommunity(getIntent().getLongExtra("idCommunity",0L),reason.getText().toString());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void suspendCommunity(Long id, String reason){
        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommunityApiService.class);
        Call<ResponseBody> call = communityApiService.suspendCommunity(id,reason);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Intent intent = new Intent(SuspendCommunityActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SuspendCommunityActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SuspendCommunityActivity.this, "Error! " , Toast.LENGTH_SHORT).show();
            }
        });
    }


}
