package com.example.redditapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.R;
import com.example.redditapp.activities.SuspendCommunityActivity;
import com.example.redditapp.adapters.PostAdapter;
import com.example.redditapp.adapters.PostListAdapter;
import com.example.redditapp.model.Community;
import com.example.redditapp.model.Post;
import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.service.PostApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostsFragment extends Fragment {

    private RecyclerView recyclerView;
    String communityName;
    Long communityId;
    Button editCommunity,suspendCommunity;

    public static ListPostsFragment newInstance() {
        return new ListPostsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.community, container, false);
        recyclerView = view.findViewById(R.id.recycle_vi);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PostListAdapter(new ArrayList<>(), getContext(), getActivity()));

        suspendCommunity = view.findViewById(R.id.suspend_community);
        editCommunity = view.findViewById(R.id.edit_community);


        Bundle bundle = this.getArguments();
        if(bundle != null){
            communityName = bundle.getString("communityName");
            System.out.println("COMM NAME "+ communityName);
        }

        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(CommunityApiService.class);
        Call<Community> call = communityApiService.getCommunityByName(communityName);
        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                if(response.code() == 200){
                    Community community = response.body();
                    communityId = community.getId();
                }else
                {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Community> call, Throwable t) {
            }
        });


        suspendCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SuspendCommunityActivity.class);
                intent.putExtra("idCommunity",communityId);
                intent.putExtra("username", bundle.getString("username"));
                getContext().startActivity(intent);
            }
        });

        editCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // ovo korostimo ako je nasa arhitekrura takva da imamo jednu aktivnost
        // i vise fragmentaa gde svaki od njih ima svoj menu unutar toolbar-a
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        getPosts();
    }


    private void getPosts() {
        PostApiService postApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(PostApiService.class);
        Call<List<Post>> call = postApiService.getPostsByCommunity(communityId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.code() == 200){
                    recyclerView.setAdapter(new PostListAdapter(response.body(), getContext(), getActivity()));
                }
                else{
                    Toast.makeText(getActivity(), "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }



}
