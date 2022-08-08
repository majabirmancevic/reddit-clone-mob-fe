package com.example.redditapp.service;

import com.example.redditapp.model.AuthenticationResponse;
import com.example.redditapp.model.LoginData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationApiService {

    @POST("/api/auth/login")
    Call<AuthenticationResponse> login(@Body LoginData loginData);
}
