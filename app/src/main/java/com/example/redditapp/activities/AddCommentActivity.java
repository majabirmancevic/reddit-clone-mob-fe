package com.example.redditapp.activities;

import android.content.Intent;
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
import com.example.redditapp.model.Comment;
import com.example.redditapp.service.CommentApiService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommentActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        Long idPost = getIntent().getLongExtra("idPost",0L);

        EditText text = findViewById(R.id.add_comment);
        Button btnAdd = findViewById(R.id.buttonAdd_comment);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setText(text.getText().toString());
                comment.setPostId(idPost);
                comment.setParentId(null);
                createComment(comment);
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

    public void createComment(Comment comment){
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommentApiService.class);
        Call<ResponseBody> call = commentApiService.createComment(comment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201){
                    Toast.makeText(AddCommentActivity.this, "Successfully added comment!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCommentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(AddCommentActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
