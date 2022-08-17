package com.example.redditapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.activities.CommentActivity;
import com.example.redditapp.activities.EditCommentActivity;
import com.example.redditapp.model.Comment;
import com.example.redditapp.service.CommentApiService;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.RecyclerViewHolder> {

    private List<Comment> aData;
    private Context context;
    private FragmentActivity activity;


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

        holder.userName.setText(comment.getUserName());
        holder.text.setText(comment.getText());
        holder.reactionCount.setText(comment.getReactionCount().toString());

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
        Button btnEdit;
        Button btnDelete;
         //TODO : TEXTVIEW DATE_CREATION

        RecyclerView recyclerView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            //TODO: IF USERID == COMMENT USERID
            userName = (TextView)itemView.findViewById(R.id.username_comment1);
            text = (TextView)itemView.findViewById(R.id.description_comments1);
            reactionCount = (TextView)itemView.findViewById(R.id.reactionCount_comment1);
            btnEdit = (Button) itemView.findViewById(R.id.btnEditComment_list1);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeleteComment_list1);
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
}
