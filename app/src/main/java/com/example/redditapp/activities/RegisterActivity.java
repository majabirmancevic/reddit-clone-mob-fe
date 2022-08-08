package com.example.redditapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.model.User;
import com.example.redditapp.service.UserApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        final EditText usernameEt = (EditText) findViewById(R.id.username_reg);
        final EditText passwordEt = (EditText) findViewById(R.id.password_reg);
        final EditText emailEt = (EditText) findViewById(R.id.email_reg);

        Button buttonRegister = (Button) findViewById(R.id.registerBtn);
        Button buttonLogin = findViewById(R.id.loginButton);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();

                if(validFields(usernameEt,passwordEt,emailEt)) {
                    User user = new User(username,password,email);
                    createUser(user);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Fill in all the fields!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });
    }

    private void createUser(User user) {
        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(RegisterActivity.this).create(UserApiService.class);
        Call<ResponseBody> call = userApiService.create(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201) {
                    Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    openLoginActivity();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Failed registration", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, t.toString());
                Toast.makeText(RegisterActivity.this, "Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean validFields(EditText username,EditText password,EditText email){
        boolean valid = true;
        if(username.getText().toString().length() == 0 ) {
            username.setError("Username is required!");
            valid = false;
        }
        else {
            username.setError(null);
        }
        if(password.getText().toString().length() == 0 ) {
            password.setError("Password is required!");
            valid = false;
        }
        else {
            password.setError(null);
        }
        if(email.getText().toString().length() == 0 ) {
            email.setError("Email is required!");
            valid = false;
        }
        else {
            email.setError(null);
        }

        String emailP = email.getText().toString().trim();
        if(isValidEmailAddress(emailP)){
            valid = true;
        }
        else {
            valid = false;
        }

        return valid;
    }


    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
