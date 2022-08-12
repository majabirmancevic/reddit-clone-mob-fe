package com.example.redditapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.TokenUtils;
import com.example.redditapp.R;
import com.example.redditapp.fragments.PostFragment;
import com.example.redditapp.model.Post;
import com.example.redditapp.tools.FragmentTransition;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.RecyclerViewHolder> {

    private List<Post> aData;
    private Context context;

    static final String TAG = PostFragment.class.getSimpleName();
    private FragmentActivity activity;

    public PostAdapter(List<Post> aData, Context context, FragmentActivity activity) {
        this.aData = aData;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list, null);
        return new RecyclerViewHolder(view);
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
                Fragment fragment = PostFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", (Parcelable) post);
                fragment.setArguments(bundle);

                FragmentTransition.to(fragment, activity, true);
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
//            String role = TokenUtils.getRole(activity);
//            if(role.equals("KUPAC")){
//                //deleteButton.setVisibility(View.INVISIBLE);
//
//            }
//            if(role.equals("PRODAVAC")){
//
//            }
        }
    }
}
