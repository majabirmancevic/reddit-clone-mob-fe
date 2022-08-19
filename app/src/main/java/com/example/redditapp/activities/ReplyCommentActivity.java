package com.example.redditapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.Comment;
import com.example.redditapp.service.CommentApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        Long parentId = getIntent().getLongExtra("idParent",0L);
        Long postId = getIntent().getLongExtra("idPost",0L);
        EditText text = findViewById(R.id.reply);
        Button btnReply = findViewById(R.id.buttonReply);

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setParentId(parentId);
                comment.setText(text.getText().toString());
                comment.setPostId(postId);

                reply(comment);
            }
        });
    }

    public void reply(Comment comment){
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(ReplyCommentActivity.this).create(CommentApiService.class);
        Call<ResponseBody> call = commentApiService.createComment(comment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201){
                    Toast.makeText(ReplyCommentActivity.this, "Successfully added reply!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReplyCommentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ReplyCommentActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReplyCommentActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
