package com.example.redditapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.Community;
import com.example.redditapp.service.CommunityApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCommunityActivity extends AppCompatActivity {

    EditText nameEt,descEt;
    Button btnEdit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_community);

         nameEt = findViewById(R.id.nameCommunity_edit);
         descEt = findViewById(R.id.descCommunity_edit);
         btnEdit = findViewById(R.id.buttonEdit_community);

        nameEt.setText(getIntent().getStringExtra("communityName"));
        descEt.setText(getIntent().getStringExtra("communityDesc"));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Community community = new Community(nameEt.getText().toString(),descEt.getText().toString());
//                community.setName(nameEt.getText().toString());
//                community.setDescription(descEt.getText().toString());
                editCommunity(getIntent().getLongExtra("idCommunity",0L),community);
            }
        });
    }

    private void editCommunity(Long id, Community community) {
        CommunityApiService communityApiService = RetrofitClientInstance.getRetrofitInstance(EditCommunityActivity.this).create(CommunityApiService.class);
        Call<ResponseBody> call = communityApiService.editCommunity(community,id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Toast.makeText(EditCommunityActivity.this, "Successfully edited community!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditCommunityActivity.this, MainActivity.class);
                    EditCommunityActivity.this.startActivity(intent);
                }else{
                    Toast.makeText(EditCommunityActivity.this, "Code response: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditCommunityActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
