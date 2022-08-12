package com.example.redditapp.service;

import com.example.redditapp.model.Post;
import com.example.redditapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PostApiService {

    @GET("/api/posts/")
    Call<List<Post>> getPosts();
}
