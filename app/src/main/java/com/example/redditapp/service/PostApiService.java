package com.example.redditapp.service;


import com.example.redditapp.model.Post;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostApiService {

    @GET("/api/posts/")
    Call<List<Post>> getPosts();

    @POST("/api/posts/")
    Call <Void> createPost(@Body Post post);

    @GET("api/posts/byCommunity/{id}")
    Call <List<Post>> getPostsByCommunity(@Path("id") Long id);

    @GET("api/posts/notSuspended")
    Call <List<Post>> getPostsNotDeleted();

    @PUT("api/posts/edit/{id}")
    Call<ResponseBody> editPost(@Path("id") Long id, @Body Post post);

    @DELETE("api/posts/{id}")
    Call<Void> deletePost(@Path("id") Long id);
}
