package com.example.redditapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.R;
import com.example.redditapp.adapters.PostAdapter;
import com.example.redditapp.adapters.PostListAdapter;
import com.example.redditapp.model.Post;
import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.service.PostApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    private RecyclerView recyclerView;
    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.recycler_view, vg, false);
        recyclerView = view.findViewById(R.id.recycle_v);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PostAdapter(new ArrayList<>(), getContext(), getActivity()));

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        // ovo korostimo ako je nasa arhitekrura takva da imamo jednu aktivnost
//        // i vise fragmentaa gde svaki od njih ima svoj menu unutar toolbar-a
//        menu.clear();
//        inflater.inflate(R.menu.menu_main, menu);
//    }

    @Override
    public void onResume(){
        super.onResume();
        getPosts();
    }

    private void getPosts(){

        PostApiService postApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(PostApiService.class);
        Call<List<Post>> call = postApiService.getPostsNotDeleted();
        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                recyclerView.setAdapter(new PostAdapter(response.body(), getContext(), getActivity()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }


}
