package com.example.redditapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.adapters.CommentAdapter;
import com.example.redditapp.model.Comment;
import com.example.redditapp.service.CommentApiService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recycler;
    TextView tvUsername,tvKomentar,tvReactionCount;
    Button btnReply,btnEdit,btnDelete;

    Long parentId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        parentId = getIntent().getLongExtra("commentParentID",0L);

        recycler = findViewById(R.id.comments_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(new CommentAdapter(new ArrayList<>(), this, CommentActivity.this));

        tvUsername = findViewById(R.id.username_comment);
        tvKomentar = findViewById(R.id.description_comments);
        tvReactionCount = findViewById(R.id.reactionCount_comment);
        btnReply = findViewById(R.id.btnReplyComment);
        btnEdit = findViewById(R.id.btnEditComment_list);
        btnDelete = findViewById(R.id.btnDeleteComment_list);

        tvUsername.setText(getIntent().getStringExtra("username"));
        tvKomentar.setText(getIntent().getStringExtra("textComment"));
        tvReactionCount.setText(getIntent().getStringExtra("reactionCount"));


        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentActivity.this,ReplyCommentActivity.class);
                intent.putExtra("idParent",parentId);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentActivity.this,EditCommentActivity.class);
                intent.putExtra("text",tvKomentar.getText().toString());
                intent.putExtra("idParent", parentId);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getComments();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getComments() {
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommentApiService.class);
        Call<List<Comment>> call = commentApiService.getCommentsByParent(parentId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                List<Comment> allComments = response.body();
                recycler.setAdapter(new CommentAdapter(allComments, CommentActivity.this, CommentActivity.this));
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public void deleteComment(){
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommentApiService.class);
        Call<Void> call = commentApiService.deleteComment(parentId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(CommentActivity.this, "Successfully deleted comment!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CommentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CommentActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
