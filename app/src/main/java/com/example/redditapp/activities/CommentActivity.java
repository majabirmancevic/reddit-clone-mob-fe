package com.example.redditapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.example.redditapp.model.Reaction;
import com.example.redditapp.model.ReactionType;
import com.example.redditapp.model.User;
import com.example.redditapp.service.CommentApiService;
import com.example.redditapp.service.ReactionApiService;
import com.example.redditapp.service.UserApiService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recycler;
    TextView tvUsername,tvKomentar,tvReactionCount, timestamp;
    ImageView upVoteComment, downVoteComment;
    Button btnReply,btnEdit,btnDelete;

    Long parentId, postId, userId ;
    private User user;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        parentId = getIntent().getLongExtra("commentParentID",0L);
        postId = getIntent().getLongExtra("postID",0L);
        userId = getIntent().getLongExtra("userID", 0L);

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
        timestamp = findViewById(R.id.timestamp1);
        upVoteComment = findViewById(R.id.upVoteComment);
        downVoteComment = findViewById(R.id.downVoteComment);

        //   tvUsername.setText(getIntent().getStringExtra("username"));
        tvKomentar.setText(getIntent().getStringExtra("textComment"));
        tvReactionCount.setText(getIntent().getStringExtra("reactionCount"));
        timestamp.setText(getIntent().getStringExtra("timestamp"));
        getUser();


        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Long idLoggedUser = preferences.getLong("idUser", 0L);

        if(idLoggedUser != userId){
            btnDelete.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
        }
        if(idLoggedUser == 0L){
            btnReply.setVisibility(View.INVISIBLE);
            upVoteComment.setEnabled(false);
            downVoteComment.setEnabled(false);
        }

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentActivity.this,ReplyCommentActivity.class);
                intent.putExtra("idParent",parentId);
                intent.putExtra("idPost",postId);
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
                deleteComment(parentId);
            }
        });

        upVoteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reaction reaction = new Reaction();
                reaction.setId(parentId);
                reaction.setReactionType(ReactionType.UPVOTE);
                upVoted(reaction);
                upVoteComment.setEnabled(false);
                downVoteComment.setEnabled(false);

            }
        });

        downVoteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reaction reaction = new Reaction();
                reaction.setId(parentId);
                reaction.setReactionType(ReactionType.DOWNVOTE);
                downVoted(reaction);
                upVoteComment.setEnabled(false);
                downVoteComment.setEnabled(false);
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

    public boolean upVoted(Reaction reaction){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(this).create(ReactionApiService.class);
        Call<Void> call = reactionApiService.voteComment(reaction);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(CommentActivity.this, " + 1 " , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CommentActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getParent(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public boolean downVoted(Reaction reaction){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(this).create(ReactionApiService.class);
        Call<Void> call = reactionApiService.voteComment(reaction);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(CommentActivity.this, " - 1 " , Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(CommentActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getParent(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
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

    public void deleteComment(Long id){
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommentApiService.class);
        Call<Void> call = commentApiService.deleteComment(id);
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

    public void getUser(){

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(CommentActivity.this).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(getIntent().getStringExtra("username"));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    user = response.body();
                    displayName = user.getDisplayName();

                    if(displayName != null){
                        tvUsername.setText(displayName);
                    }
                    else {
                        tvUsername.setText(getIntent().getStringExtra("username"));
                    }

                }
                else{
                    Toast.makeText(CommentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
