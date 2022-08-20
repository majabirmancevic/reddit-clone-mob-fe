package com.example.redditapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.redditapp.ApiClient.TokenUtils;
import com.example.redditapp.R;
import com.example.redditapp.activities.EditCommunityActivity;
import com.example.redditapp.activities.SuspendCommunityActivity;
import com.example.redditapp.adapters.PostAdapter;
import com.example.redditapp.adapters.PostListAdapter;
import com.example.redditapp.model.Community;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.User;
import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.service.PostApiService;
import com.example.redditapp.service.UserApiService;
import com.example.redditapp.tools.FragmentTransition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostsFragment extends Fragment {

    private RecyclerView recyclerView;
    String role;
    Long userId;
    String communityName;
    Long communityId;
    String communityDesc;
    Button editCommunity,suspendCommunity;
    User user;


    public static ListPostsFragment newInstance() {
        return new ListPostsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.community, container, false);
        recyclerView = view.findViewById(R.id.recycle_vi);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PostListAdapter(new ArrayList<>(), getContext(), getActivity()));

        Bundle bundle = this.getArguments();
        if(bundle != null){
            communityName = bundle.getString("communityName");
            role = bundle.getString("role");
            userId = bundle.getLong("idUser");

            System.out.println("COMM NAME "+ communityName);
            System.out.println("ROLE "+ role);
            System.out.println("USER ID "+ userId);
        }
        suspendCommunity = view.findViewById(R.id.suspend_community);
        editCommunity = view.findViewById(R.id.edit_community);

        if(!role.equals("ADMIN")){
            suspendCommunity.setVisibility(View.INVISIBLE);
            System.out.println("ADMIN IS "+ role);
        }

        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(CommunityApiService.class);
        Call<Community> call = communityApiService.getCommunityByName(communityName);
        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                if(response.code() == 200){

                    System.out.println("FROM BASE : " + response.body().getUserId() + response.body().getName());

                    Community community = response.body();
                    communityId = community.getId();
                    communityDesc = community.getDescription();
                    Long user = community.getUserId();
                    System.out.println("COMM " + user+ "LOGGED "+ userId);
                    if(user != userId){
                        editCommunity.setVisibility(View.INVISIBLE);
                    }
                }
                else
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

                Intent intent = new Intent(getActivity(), EditCommunityActivity.class);
                intent.putExtra("idCommunity", communityId);
                intent.putExtra("communityName",communityName);
                intent.putExtra("communityDesc",communityDesc);
                getContext().startActivity(intent);

            }
        });

        return view;
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

//    public boolean isAdmin(){
//        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(UserApiService.class);
//        Call<User> call = userApiService.findByUsername(TokenUtils.loggedUsername(getActivity()));
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(response.code() == 200){
//                    user = response.body();
//                    if(user.getRole() == "ADMIN"){
//                        admin = true;
//                    }
//                    else{
//                        admin = false;
//                    }
//                    System.out.println("RETURN "+ admin);
//                }
//                else{
//                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return admin;
//    }


}
