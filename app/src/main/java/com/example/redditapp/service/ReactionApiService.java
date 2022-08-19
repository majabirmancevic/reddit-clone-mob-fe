package com.example.redditapp.service;

import com.example.redditapp.model.Post;
import com.example.redditapp.model.Reaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReactionApiService {

    @GET("/api/votes/voter/{id}")
    Call<Integer> getKarma(@Path("id") Long id);

    @POST("/api/votes/post")
    Call<Void> votePost(@Body Reaction reaction);

    @POST("/api/votes/comment")
    Call<Void> voteComment(@Body Reaction reaction);
}
