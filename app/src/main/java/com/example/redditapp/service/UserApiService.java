package com.example.redditapp.service;

import com.example.redditapp.model.ChangePassword;
import com.example.redditapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiService {
    @POST("/api/auth/signup")
    Call<ResponseBody> create(@Body User user);

    @GET("/api/auth/loggedUser/{username}")
    Call<User> findByUsername(@Path("username") String username);

    @POST("/api/auth/changePassword/{id}")
    Call<ResponseBody> changePassword(@Path("id") Long id,@Body ChangePassword changePassword);

    @PUT("/api/auth/users/{id}")
    Call<User> update(@Path("id") Long id, @Body User userDto);
}
