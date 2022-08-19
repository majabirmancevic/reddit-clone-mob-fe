package com.example.redditapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.ApiClient.TokenUtils;
import com.example.redditapp.R;
import com.example.redditapp.activities.DetailPostActivity;
import com.example.redditapp.model.ChangePassword;
import com.example.redditapp.model.User;
import com.example.redditapp.service.ReactionApiService;
import com.example.redditapp.service.UserApiService;
import com.example.redditapp.tools.FragmentTransition;
import com.google.gson.Gson;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    static final String TAG = ProfileFragment.class.getSimpleName();

    private static User user;
    EditText etUsername;
    EditText etEmail;
    EditText etDisplayName;
    EditText etDescription;
    TextView karma;
    int countKarma ;
    Long idLoggedUser;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences preferences =  getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String username  = preferences.getString("username",null);
        idLoggedUser = preferences.getLong("idUser", 0L);

        EditText etOldPassword = view.findViewById(R.id.etOldPassword);
        EditText etNewPassword = view.findViewById(R.id.etNewPassword);

        Button change = view.findViewById(R.id.updateBtn_password);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validPasswordFields(etOldPassword,etNewPassword)){
                    String oldPassword = etOldPassword.getText().toString().trim();
                    String newPassword = etNewPassword.getText().toString().trim();

                    ChangePassword changePassword = new ChangePassword(oldPassword,newPassword);
                    changePasswordApi(changePassword);
                }
            }
        });

        etUsername = view.findViewById(R.id.etUsername_info);
        etEmail = view.findViewById(R.id.etEmail_info);
        etDescription = view.findViewById(R.id.etDescription_info);
        etDisplayName = view.findViewById(R.id.etDisplayName_info);
        karma = view.findViewById(R.id.countKarma);

        getUser(username);
        getKarma();
        Button update = view.findViewById(R.id.updateBtn);
        Button edit = view.findViewById(R.id.editBtn);

        update.setVisibility(View.INVISIBLE);
        etEmail.setEnabled(false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInfoFields(etUsername)) {
                    user.setUsername(etUsername.getText().toString().trim());
                    user.setDescription(etDescription.getText().toString().trim());
                    user.setDisplayName(etDisplayName.getText().toString().trim());
                    updateUser(user.getId(), user);
                }
            }
        });

        etUsername.setEnabled(false);
        etDescription.setEnabled(false);
        etDisplayName.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etUsername.isEnabled() && !etDescription.isEnabled() && !etDisplayName.isEnabled()){
                    etUsername.setEnabled(true);
                    etDescription.setEnabled(true);
                    etDisplayName.setEnabled(true);

                    update.setVisibility(View.VISIBLE);
                }else {
                    etUsername.setEnabled(false);
                    etDescription.setEnabled(false);
                    etDisplayName.setEnabled(false);
                    update.setVisibility(View.INVISIBLE);
                }
            }
        });
        return view;
    }


    private void changePasswordApi(ChangePassword changePassword){
        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(UserApiService.class);
        Call<ResponseBody> call = userApiService.changePassword(user.getId(),changePassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                    FragmentTransition.to(ProfileFragment.newInstance(),getActivity(),false);
                }
                else if(response.code() == 400){
                    Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
                else{
                    System.out.println(response);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, t.toString());
            }
        });

    }

    private void getUser(String username){

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    user = response.body();
                    etUsername.setText(user.getUsername());
                    etEmail.setText(user.getEmail());
                    etDescription.setText(user.getDescription());
                    etDisplayName.setText(user.getDisplayName());
                }
                else if(response.code() == 404){
                    Toast.makeText(getActivity(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
                else{
                    System.out.println(response);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, t.toString());
            }
        });
    }

    private void updateUser(Long id, User user){

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(UserApiService.class);
        Call<User> call = userApiService.update(id, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    Toast.makeText(getActivity(), "Sucessfully updated", Toast.LENGTH_SHORT).show();
                    FragmentTransition.to(ProfileFragment.newInstance(),getActivity(),false);
                }
                else if(response.code() == 404){
                    Toast.makeText(getActivity(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, t.toString());
            }
        });
    }


    public boolean validPasswordFields(EditText oldPassword,EditText newPassword){
        boolean valid = true;
        if(oldPassword.getText().toString().length() == 0 ) {
            oldPassword.setError("Old password is required!");
            valid = false;
        }
        else {
            oldPassword.setError(null);
        }
        if(newPassword.getText().toString().length() == 0 ) {
            newPassword.setError("New password is required!");
            valid = false;
        }
        else {
            newPassword.setError(null);
        }

        return valid;
    }

    public boolean validInfoFields(EditText etUserame){
        boolean valid = true;
        if(etUserame.getText().toString().length() == 0 ) {
            etUserame.setError("Userame is required!");
            valid = false;
        }
        else {
            etUserame.setError(null);
        }
        return valid;
    }

    public void getKarma(){
        ReactionApiService reactionApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(ReactionApiService.class);
        Call<Integer> call = reactionApiService.getKarma((TokenUtils.loggedUserId(getActivity())));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                    countKarma = response.body();
                    karma.setText(String.valueOf(countKarma));
                    Toast.makeText(getActivity()," KARMA = " + countKarma, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"KOD RESPONSE " + response.code(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) { }
        });

    }
}