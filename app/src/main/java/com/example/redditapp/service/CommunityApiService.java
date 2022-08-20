package com.example.redditapp.service;

import com.example.redditapp.model.Community;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommunityApiService {

    @GET("/api/community/")
    Call<List<Community>> getCommunities();

    @GET("/api/community/all")
    Call<List<Community>> getNotDeletedCommunities();

    @GET("/api/community/byName/{name}")
    Call<Community> getCommunityByName(@Path("name") String name);

    @POST("/api/community/")
    Call <ResponseBody> createCommunity(@Body Community community);

    @POST("/api/community/{id}")
    Call<ResponseBody> suspendCommunity(@Path("id") Long id, @Body String suspendedReason);

    @PUT("/api/community/edit/{id}")
    Call<ResponseBody> editCommunity(@Body Community community, @Path("id") Long id);
}
