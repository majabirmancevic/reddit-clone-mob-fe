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
import com.example.redditapp.activities.CommentActivity;
import com.example.redditapp.activities.DetailPostActivity;
import com.example.redditapp.model.Comment;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.RecyclerViewHolder> {
    private List<Comment> aData;
    private Context context;
    private FragmentActivity activity;


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

        holder.userName.setText(comment.getUserName());
        holder.text.setText(comment.getText());
        holder.reactionCount.setText(comment.getReactionCount().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CommentActivity.class);
                intent.putExtra("commentParentID", comment.getId());
                intent.putExtra("username", comment.getUserName());
                intent.putExtra("textComment", comment.getText());
                intent.putExtra("reactionCount",comment.getReactionCount().toString());
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
        //TODO : TEXTVIEW DATE_CREATION
        Button btnEdit;
        Button btnDelete;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView)itemView.findViewById(R.id.username_comment_list);
            text = (TextView)itemView.findViewById(R.id.description_comment_list);
            reactionCount = (TextView)itemView.findViewById(R.id.reactionCount_comment_list);

            //TODO: IF USERID == COMMENT USERID
            btnEdit = (Button)itemView.findViewById(R.id.btnEditComment_list);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeleteComment_list);
        }
    }
}
