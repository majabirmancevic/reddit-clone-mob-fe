package com.example.redditapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.activities.DetailPostActivity;
import com.example.redditapp.model.Post;
import com.example.redditapp.service.PostApiService;
import com.example.redditapp.tools.FragmentTransition;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostFragment extends Fragment {

    public static EditPostFragment newInstance() {
        return new EditPostFragment();
    }
    LinearLayout layout;
    Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.edit_post_fragment, container, false);
        layout = view.findViewById(R.id.editLayout);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            post.setText(bundle.getString("text"));
            post.setPostName(bundle.getString("title"));
            post.setId(bundle.getLong("postID"));
           // Long postId = bundle.getLong("postID");
            Long userId = bundle.getLong("userID");
        }

        EditText titleEt = view.findViewById(R.id.title_post_edit);
        EditText textEt = view.findViewById(R.id.description_post_edit);
        Button btnEdit = view.findViewById(R.id.buttonEdit_post);

        titleEt.setText(post.getPostName());
        textEt.setText(post.getText());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.setText(textEt.toString());
                post.setPostName(titleEt.toString());
                editPost(post.getId(),post);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void editPost(Long id, Post post){
        PostApiService postApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(PostApiService.class);
        Call<ResponseBody> call = postApiService.editPost(id, post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Toast.makeText(getActivity(), "Successfully edited post!", Toast.LENGTH_SHORT).show();
                    FragmentTransition.to(PostFragment.newInstance(), getActivity(), false);
                }
                else{
                    Toast.makeText(getActivity(), "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
