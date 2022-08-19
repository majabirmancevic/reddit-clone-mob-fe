package com.example.redditapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.Community;
import com.example.redditapp.model.Post;
import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.service.PostApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostFragment extends Fragment {
    List<String> communityList;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteText;

    public static AddPostFragment newInstance() {
        return new AddPostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.add_post, container, false);


        EditText titleEt = view.findViewById(R.id.title_post);
        EditText descEt = view.findViewById(R.id.description_post);

        autoCompleteText = view.findViewById(R.id.autoCompleteText);
        adapterItems = new ArrayAdapter<String>(this.getContext(),R.layout.list_item,communityNames());
        autoCompleteText.setAdapter(adapterItems);

        Button add = (Button) view.findViewById(R.id.buttonAdd_post);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validField(titleEt,descEt)){
                    String title = titleEt.getText().toString().trim();
                    String desc = descEt.getText().toString().trim();

                    Post post = new Post();
                    post.setPostName(title);
                    post.setText(desc);
                    post.setCommunityName(autoCompleteText.getText().toString());
                    createPost(post);
                }
                else{
                    Toast.makeText(getActivity(), "Fill in all the fields correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public List<String> communityNames(){
        communityList = new ArrayList<>();

        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(CommunityApiService.class);
        Call<List<Community>> communities = communityApiService.getNotDeletedCommunities();
        communities.enqueue(new Callback<List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response<List<Community>> response) {
                List<Community> listCommunities = response.body();
                for (Community community:listCommunities) {
                    String communityName = community.getName();
                    communityList.add(communityName);
                }
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {

            }
        });

        return communityList;
    }

    public void createPost(Post post){
        PostApiService postApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(PostApiService.class);
        Call<Void> call = postApiService.createPost(post);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.code() + "JE KOD ODGOVORA");
                if(response.code() == 201) {
                    Toast.makeText(getActivity(), "Post successfully created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "Code response "+response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public boolean validField(EditText title, EditText desc){

        boolean valid = true;

        if(title.getText().toString().length() == 0 ) {
            title.setError("Title is required!");
            valid = false;
        }
        else {
            title.setError(null);
        }
        if(desc.getText().toString().length() == 0 ) {
            desc.setError("Description is required!");
            valid = false;
        }
        else {
            desc.setError(null);
        }

        return valid;
    }
}
