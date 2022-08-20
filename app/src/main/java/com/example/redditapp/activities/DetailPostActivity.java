package com.example.redditapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.adapters.CommentListAdapter;
import com.example.redditapp.model.Comment;
import com.example.redditapp.model.Reaction;
import com.example.redditapp.model.ReactionType;
import com.example.redditapp.service.CommentApiService;
import com.example.redditapp.service.PostApiService;
import com.example.redditapp.service.ReactionApiService;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPostActivity extends AppCompatActivity {

    static final String TAG = DetailPostActivity.class.getSimpleName();
    TextView communityName,userName,titlePost,text,dateCreation, reactionCount;
    RecyclerView recyclerView;
    List<Comment> comments;
    ImageView upVote, downVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        recyclerView = findViewById(R.id.recycle_v_all_comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommentListAdapter(new ArrayList<>(), this, DetailPostActivity.this));

        Long idPost = getIntent().getLongExtra("postID", 0L);
        Long idUser = getIntent().getLongExtra("userID", 0L);

        communityName = findViewById(R.id.communityName_detail);
        userName = findViewById(R.id.userName_detail);
        titlePost = findViewById(R.id.titlePost_detail);
        text = findViewById(R.id.textPost_detail);
        dateCreation = findViewById(R.id.dateCreation_detail);
        upVote = findViewById(R.id.upVotePost);
        downVote = findViewById(R.id.downVotePost);
        reactionCount = findViewById(R.id.reactionCount_post);

        ExtendedFloatingActionButton fab = findViewById(R.id.extended_fab);


        Button btnEditPost = findViewById(R.id.btnEditPost);
        Button btnDeletePost = findViewById(R.id.btnDeletePost);

        if(getIntent().getStringExtra("displayName") != null){
           userName.setText(getIntent().getStringExtra("displayName"));
        }else {
            userName.setText(getIntent().getStringExtra("userName"));
        }

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Long idLoggedUser = preferences.getLong("idUser", 0L);

        if(idLoggedUser == 0L){
            fab.setVisibility(View.INVISIBLE);
            upVote.setEnabled(false);
            downVote.setEnabled(false);
        }

        communityName.setText(getIntent().getStringExtra("communityName"));
        titlePost.setText(getIntent().getStringExtra("title"));
        text.setText(getIntent().getStringExtra("text"));
        dateCreation.setText(getIntent().getStringExtra("dateCreation"));
        reactionCount.setText(getIntent().getStringExtra("reactionCount"));


        if(idUser != idLoggedUser){
            btnEditPost.setVisibility(View.INVISIBLE);
            btnDeletePost.setVisibility(View.INVISIBLE);

        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPostActivity.this,AddCommentActivity.class);
                intent.putExtra("idPost",idPost);
                startActivity(intent);
            }
        });

        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailPostActivity.this, EditPostActivity.class);
                intent.putExtra("title", titlePost.getText().toString());
                intent.putExtra("text", text.getText().toString());
                intent.putExtra("postID",idPost);
                intent.putExtra("userID", idUser);
                startActivity(intent);

//                fragment = EditPostFragment.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putLong("postID", idPost);
//                bundle.putLong("userID", idUser);
//                bundle.putString("title", titlePost.getText().toString());
//                bundle.putString("text", text.getText().toString());
//                fragment.setArguments(bundle);
//                FragmentTransition.to(fragment , DetailPostActivity.this, true);
            }
        });

        btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(idPost);
            }
        });

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reaction reaction = new Reaction();
                reaction.setReactionType(ReactionType.UPVOTE);
                reaction.setId(idPost);
                upVoted(reaction);
                downVote.setEnabled(false);
                upVote.setEnabled(false);
            }
        });

        downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reaction reaction = new Reaction();
                reaction.setReactionType(ReactionType.DOWNVOTE);
                reaction.setId(idPost);
                downVoted(reaction);
                downVote.setEnabled(false);
                upVote.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComments();
    }

    private void getComments() {
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(this).create(CommentApiService.class);
        Call<List<Comment>> call = commentApiService.getCommentsByPost(getIntent().getLongExtra("postID", 0L));
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.code() == 200){
                    comments = new ArrayList<>();
                    List<Comment> allComments = response.body();
                    System.out.println(allComments);
                    Toast.makeText(DetailPostActivity.this,"successful " + response.code(), Toast.LENGTH_SHORT).show();
                    for (Comment comment : allComments){
                        if(comment.getParentId() == null){
                            comments.add(comment);
                        }
                    }
                    recyclerView.setAdapter(new CommentListAdapter(comments, getApplicationContext(), DetailPostActivity.this));
                }
                else{
                    Toast.makeText(DetailPostActivity.this,"KOD RESPONSE " + response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public void deletePost(Long id){
        PostApiService postApiService = RetrofitClientInstance.getRetrofitInstance(this).create(PostApiService.class);
        Call<Void> call = postApiService.deletePost(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(DetailPostActivity.this, "Successfully deleted post!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailPostActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getParent(), "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getParent(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean upVoted(Reaction reaction){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(this).create(ReactionApiService.class);
        Call<Void> call = reactionApiService.votePost(reaction);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(DetailPostActivity.this, " + 1 " , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DetailPostActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetailPostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public boolean downVoted(Reaction reaction){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(this).create(ReactionApiService.class);
        Call<Void> call = reactionApiService.votePost(reaction);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(DetailPostActivity.this, " - 1 " , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DetailPostActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getParent(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(DetailPostActivity.this, MainActivity.class);
//        startActivity(intent);
        super.onBackPressed();
    }
}
