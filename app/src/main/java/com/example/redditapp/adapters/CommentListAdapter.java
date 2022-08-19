package com.example.redditapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.R;
import com.example.redditapp.activities.CommentActivity;
import com.example.redditapp.activities.DetailPostActivity;
import com.example.redditapp.model.Comment;
import com.example.redditapp.model.User;
import com.example.redditapp.service.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.RecyclerViewHolder> {
    private List<Comment> aData;
    private Context context;
    private FragmentActivity activity;
    private String displayName;
    private User user;



    public CommentListAdapter(List<Comment> aData, Context context, FragmentActivity activity) {
        this.aData = aData;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public CommentListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent,false);
        return new CommentListAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder( CommentListAdapter.RecyclerViewHolder holder, int position) {
        Comment comment = aData.get(position);

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(comment.getUserName());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    user = response.body();
                    displayName = user.getDisplayName();

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CommentActivity.class);
                intent.putExtra("userID", comment.getUserId());
                intent.putExtra("commentParentID", comment.getId());
                intent.putExtra("postID", comment.getPostId());
                intent.putExtra("username", comment.getUserName());
                intent.putExtra("textComment", comment.getText());
                intent.putExtra("reactionCount",comment.getReactionCount().toString());
                intent.putExtra("timestamp",comment.getTimestamp());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
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

//        Button btnEdit;
//        Button btnDelete;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView)itemView.findViewById(R.id.username_comment_list);
            text = (TextView)itemView.findViewById(R.id.description_comment_list);
            timestamp = (TextView)itemView.findViewById(R.id.timestamp);

//            btnEdit = (Button)itemView.findViewById(R.id.btnEditComment_list);
//            btnDelete = (Button) itemView.findViewById(R.id.btnDeleteComment_list);
        }
    }

    public String getUser(String username){

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    user = response.body();
                    displayName = user.getDisplayName();

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
        return displayName;
    }
}
