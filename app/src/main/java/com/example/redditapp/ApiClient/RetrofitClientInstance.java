package com.example.redditapp.ApiClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit = null;
    static final String BASE_URL = "http://192.168.10.109:8080";




    public static Retrofit getRetrofitInstance(Activity activity){
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        okhttpBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request();
                SharedPreferences preferences =  activity.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                String retrivedToken  = preferences.getString("TOKEN",null);

                Request.Builder newRequest = request.newBuilder()
                        .header("Authorization", "Bearer " + retrivedToken);
                return chain.proceed(newRequest.build());
            }
        });

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okhttpBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Retrofit getRetrofitInstanceOrders(Activity activity){
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        okhttpBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request();
                SharedPreferences preferences =  activity.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                String retrivedToken  = preferences.getString("TOKEN",null);

                Request.Builder newRequest = request.newBuilder()
                        .header("Authorization", "Bearer " + retrivedToken);
                return chain.proceed(newRequest.build());
            }
        });




        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okhttpBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
