package com.example.redditapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.Community;
import com.example.redditapp.service.CommunityApiService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommunityFragment extends Fragment {

    static final String TAG = AddCommunityFragment.class.getSimpleName();
    public static AddCommunityFragment newInstance() {
        return new AddCommunityFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.add_community, vg, false);
        EditText nameEt = view.findViewById(R.id.nameCommunity_add);
        EditText descEt = view.findViewById(R.id.descCommunity_add);

        Button add = (Button) view.findViewById(R.id.buttonAdd_community);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validField(nameEt, descEt)) {
                    String name = nameEt.getText().toString().trim();
                    String desc = descEt.getText().toString().trim();

                    Community community = new Community(name, desc);
                    createCommunity(community);
                }
                else{
                    Toast.makeText(getActivity(), "Fill in all the fields correctly",Toast.LENGTH_SHORT).show();
                }
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

    private void createCommunity(Community community){

        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(CommunityApiService.class);
        Call<ResponseBody> call = communityApiService.createCommunity(community);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201) {
                    Toast.makeText(getActivity(), "Community created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "Be sure all fields are filled", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, t.toString());
            }
        });
    }

    public boolean validField(EditText name, EditText desc){

        boolean valid = true;

        if(name.getText().toString().length() == 0 ) {
            name.setError("Name is required!");
            valid = false;
        }
        else {
            name.setError(null);
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
