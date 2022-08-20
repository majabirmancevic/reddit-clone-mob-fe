package com.example.redditapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.ApiClient.TokenUtils;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.activities.CommentActivity;
import com.example.redditapp.activities.EditCommentActivity;
import com.example.redditapp.model.Comment;
import com.example.redditapp.model.Reaction;
import com.example.redditapp.model.ReactionType;
import com.example.redditapp.model.User;
import com.example.redditapp.service.CommentApiService;
import com.example.redditapp.service.ReactionApiService;
import com.example.redditapp.service.UserApiService;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.RecyclerViewHolder> {

    private List<Comment> aData;
    private Context context;
    private FragmentActivity activity;
    private User getUser;
    private String displayName;


    public CommentAdapter(List<Comment> aData, Context context, FragmentActivity activity) {
        this.aData = aData;
        this.context = context;
        this.activity = activity;
    }



    @Override
    public CommentAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent,false);
        return new CommentAdapter.RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder( CommentAdapter.RecyclerViewHolder holder, int position) {
        Comment comment = aData.get(position);
        Long user = TokenUtils.loggedUserId(activity);

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(comment.getUserName());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    getUser = response.body();
                    displayName = getUser.getDisplayName();

                    if(displayName != null){
                        holder.userName.setText(displayName);
                    }
                    else{
                        holder.userName.setText(comment.getUserName());
                    }
                }
                else{
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.text.setText(comment.getText());
        holder.timestamp.setText(comment.getTimestamp());
        holder.reactionCount.setText(comment.getReactionCount().toString());

        if(comment.getUserId() != user){
            holder.btnDelete.setVisibility(View.INVISIBLE);
            holder.btnEdit.setVisibility(View.INVISIBLE);
        }
        if(user == null){
            holder.downvote.setEnabled(false);
            holder.upvote.setEnabled(false);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditCommentActivity.class);
                intent.putExtra("text",comment.getText());
                intent.putExtra("idParent", comment.getId());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment(comment.getId());
            }
        });

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Reaction reaction = new Reaction();
                reaction.setId(comment.getId());
                reaction.setReactionType(ReactionType.UPVOTE);
                upVoted(reaction);
                holder.upvote.setEnabled(false);
                holder.downvote.setEnabled(false);
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reaction reaction = new Reaction();
                reaction.setId(comment.getId());
                reaction.setReactionType(ReactionType.DOWNVOTE);
                downVoted(reaction);
                holder.upvote.setEnabled(false);
                holder.downvote.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(aData != null) {
            return aData.size();
        }
        return 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView reactionCount;
        TextView userName;
        TextView text;
        TextView timestamp;
        Button btnEdit;
        Button btnDelete;
        ImageView upvote;
        ImageView downvote;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            //TODO: IF USERID == COMMENT USERID
            userName = (TextView)itemView.findViewById(R.id.username_comment1);
            text = (TextView)itemView.findViewById(R.id.description_comments1);
            reactionCount = (TextView)itemView.findViewById(R.id.reactionCount_comment1);
            btnEdit = (Button) itemView.findViewById(R.id.btnEditComment_list1);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeleteComment_list1);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp2);
            upvote = itemView.findViewById(R.id.upVoteReply);
            downvote = itemView.findViewById(R.id.downVoteReply);
        }
    }

    public void deleteComment(Long id){
        CommentApiService commentApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(CommentApiService.class);
        Call<Void> call = commentApiService.deleteComment(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(activity, "Successfully deleted comment!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, MainActivity.class);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(activity, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean upVoted(Reaction reaction){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(ReactionApiService.class);
        Call<Void> call = reactionApiService.voteComment(reaction);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(activity, " + 1 " , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(activity, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public boolean downVoted(Reaction reaction){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(ReactionApiService.class);
        Call<Void> call = reactionApiService.voteComment(reaction);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(activity, " - 1 " , Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(activity, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

}
