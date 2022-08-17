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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        Long parentId = getIntent().getLongExtra("idParent", 0L);

        EditText etComment = findViewById(R.id.edit_comment);
        etComment.setText(getIntent().getStringExtra("text"));

        Button btnEdit = findViewById(R.id.buttonEdit_comment);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setText(etComment.getText().toString());
                editComment(parentId,comment);
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

    public void editComment(Long id, Comment comment){
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommentApiService.class);
        Call<ResponseBody> call = commentApiService.editComment(comment,id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Toast.makeText(EditCommentActivity.this, "Successfully edited comment!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditCommentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(EditCommentActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditCommentActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
