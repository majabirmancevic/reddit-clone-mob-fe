package com.example.redditapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.R;
import com.example.redditapp.activities.DetailPostActivity;
import com.example.redditapp.model.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.RecyclerViewHolder> {

    private List<Post> aData;
    private Context context;
    private FragmentActivity activity;

    public PostListAdapter(List<Post> aData, Context context, FragmentActivity activity) {
        this.aData = aData;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public PostListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list, parent,false);

        return new PostListAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Post post = aData.get(position);

        holder.communityName.setText(post.getCommunityName());
        holder.userName.setText(post.getUserName());
        holder.titlePost.setText(post.getPostName());
        holder.text.setText(post.getText());
        holder.commentCount.setText(post.getCommentCount().toString());
        holder.reactionCount.setText(post.getReactionCount().toString());
        holder.dateCreation.setText(post.getDuration());

        holder.btnViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailPostActivity.class);
                intent.putExtra("postID",post.getId());
                intent.putExtra("userID", post.getUserId());
                intent.putExtra("communityName", post.getCommunityName());
                intent.putExtra("userName", post.getUserName());
                intent.putExtra("title", post.getPostName());
                intent.putExtra("text", post.getText());
                intent.putExtra("dateCreation",post.getDuration());
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
        TextView communityName;
        TextView userName;
        TextView titlePost;
        TextView text;
        TextView commentCount;
        TextView reactionCount;
        TextView dateCreation;
        Button btnViewPost;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            communityName = (TextView)itemView.findViewById(R.id.communityName);
            userName = (TextView)itemView.findViewById(R.id.userName);
            titlePost = (TextView)itemView.findViewById(R.id.titlePost);
            text = (TextView)itemView.findViewById(R.id.textPost);
            commentCount = (TextView)itemView.findViewById(R.id.commentCount);
            btnViewPost = (Button)itemView.findViewById(R.id.btnViewPost);
            reactionCount = (TextView)itemView.findViewById(R.id.reactionCount);
            dateCreation = (TextView)itemView.findViewById(R.id.dateCreation);
        }
    }
}
