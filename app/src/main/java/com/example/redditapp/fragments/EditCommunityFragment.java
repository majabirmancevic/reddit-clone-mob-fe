package com.example.redditapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.Community;
import com.example.redditapp.model.Post;
import com.example.redditapp.service.CommunityApiService;
import com.example.redditapp.service.PostApiService;
import com.example.redditapp.tools.FragmentTransition;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCommunityFragment extends Fragment {

    public static EditCommunityFragment newInstance() {
        return new EditCommunityFragment();
    }
    Community community;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.edit_community, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            community.setId(bundle.getLong("idCommunity"));
            community.setDescription(bundle.getString("communityDesc"));
            community.setName(bundle.getString("communityName"));
        }

        EditText nameEt = view.findViewById(R.id.nameCommunity_edit);
        EditText descEt = view.findViewById(R.id.descCommunity_edit);
        Button btnEdit = view.findViewById(R.id.buttonEdit_community);

        nameEt.setText(bundle.getString("communityName"));
        descEt.setText(bundle.getString("communityDesc"));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                community.setName(nameEt.getText().toString());
                community.setDescription(descEt.getText().toString());
                editCommunity(bundle.getLong("idCommunity"),community);
            }
        });

        return view;
    }

    private void editCommunity(Long id, Community community) {
        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(CommunityApiService.class);
        Call<ResponseBody> call = communityApiService.editCommunity(community,id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Toast.makeText(getActivity(), "Successfully edited community!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    getContext().startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
