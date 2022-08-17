package com.example.redditapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.R;
import com.example.redditapp.activities.DetailPostActivity;
import com.example.redditapp.fragments.ListPostsFragment;
import com.example.redditapp.fragments.PostFragment;
import com.example.redditapp.model.Community;
import com.example.redditapp.model.Post;

import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.tools.FragmentTransition;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.RecyclerViewHolder> {

    private List<Post> aData;
    private Context context;
    private Long communityId;
    private FragmentActivity activity;

    public PostAdapter(List<Post> aData, Context context, FragmentActivity activity) {
        this.aData = aData;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list, parent,false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Post post = aData.get(position);
        getCommunityId(post.getCommunityName());

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
//                Fragment fragment = PostFragment.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("post", (Parcelable) post);
//                fragment.setArguments(bundle);
//                FragmentTransition.to(fragment, activity, true);

                Intent intent = new Intent(activity, DetailPostActivity.class);
                intent.putExtra("postID",post.getId());
                intent.putExtra("userID", post.getUserId());
                intent.putExtra("communityName", post.getCommunityName());
                intent.putExtra("userName", post.getUserName());
                intent.putExtra("title", post.getPostName());
                intent.putExtra("text", post.getText());
                intent.putExtra("dateCreation",post.getDuration());
                context.startActivity(intent);
                System.out.println("USER ID FROM POST! " + post.getUserId());

            }
        });

        holder.communityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = ListPostsFragment.newInstance();
                Bundle bundle = new Bundle();

                bundle.putLong("communityId", communityId);
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
            }
        }

    private void getCommunityId(String name){
        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(activity).create(CommunityApiService.class);
        Call<Community> call = communityApiService.getCommunityByName(name);
        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                System.out.println("CODE ODGOVOR " + response.code());
                if(response.code() == 200){
                    Community community = response.body();
                    communityId = community.getId();
                }else
                {
                    Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Community> call, Throwable t) {
            }
        });
    }

}
