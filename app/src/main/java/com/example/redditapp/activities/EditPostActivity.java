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
import com.example.redditapp.fragments.PostFragment;
import com.example.redditapp.model.Post;
import com.example.redditapp.service.PostApiService;
import com.example.redditapp.tools.FragmentTransition;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

//        post.setText(getIntent().getStringExtra("text"));
//        post.setPostName(getIntent().getStringExtra("title"));
        Long idPost = getIntent().getLongExtra("postID", 0L);
        Long idUser = getIntent().getLongExtra("userID", 0L);

        EditText titleEt = findViewById(R.id.title_post_edit);
        EditText textEt = findViewById(R.id.description_post_edit);
        Button btnEdit = findViewById(R.id.buttonEdit_post);

        titleEt.setText(getIntent().getStringExtra("text"));
        textEt.setText(getIntent().getStringExtra("title"));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post();
                post.setText(textEt.getText().toString());
                post.setPostName(titleEt.getText().toString());
                editPost(idPost,post);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void editPost(Long id, Post post){
        PostApiService postApiService = RetrofitClientInstance.getRetrofitInstance(EditPostActivity.this).create(PostApiService.class);
        Call<ResponseBody> call = postApiService.editPost(id, post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Toast.makeText(EditPostActivity.this, "Successfully edited post!", Toast.LENGTH_SHORT).show();
                    //FragmentTransition.to(PostFragment.newInstance(), EditPostActivity.this, false);
                    Intent intent = new Intent(EditPostActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(EditPostActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
