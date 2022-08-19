package com.example.redditapp.ApiClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

public class TokenUtils {


    public static String getRole(Activity activity){
        SharedPreferences preferences =  activity.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        if(retrivedToken != null) {
            JWT jwt = new JWT(retrivedToken);
            Claim rol = jwt.getClaim("role");
            String forParse = rol.asString();
            String[] r = forParse.split("_");
            String role = r[1];
            return role;
        }
        return null;
    }

    public static Long loggedUserId(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Long idLoggedUser = preferences.getLong("idUser", 0L);

        return idLoggedUser;
    }
}
