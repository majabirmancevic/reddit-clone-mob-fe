package com.example.redditapp.service;

import com.example.redditapp.model.Comment;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentApiService {

    @POST("/api/comments/")
    Call<ResponseBody> createComment(@Body Comment comment);

    @GET("/api/comments/")
    Call<List<Comment>> getAllComments();

    @GET("/api/comments/byPost/{postId}")
    Call<List<Comment>> getCommentsByPost(@Path("postId") Long postId);

    @GET("/api/comments/allByParent/{id}")
    Call<List<Comment>> getCommentsByParent(@Path("id") Long id);

    @DELETE("/api/comments/delete/{id}")
    Call<Void> deleteComment(@Path("id") Long id);

    @PUT("/api/comments/edit/{id}")
    Call<ResponseBody> editComment(@Body Comment comment, @Path("id") Long id);
}
