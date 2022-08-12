package com.example.redditapp.tools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.redditapp.R;
import com.example.redditapp.model.Post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mokap {

    private String title;
    private String text;
    private LocalDate creationDate;
    private String imagePath;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Post> getPosts(){
        ArrayList<Post> posts = new ArrayList<Post>();

//        LocalDate date = LocalDate.now();
//        LocalDate yesterday = date.minusDays(1);
//        LocalDate dayBeforeYesterday= yesterday.minusDays(1);

//        Post p1 = new Post("Post1", "Text posta1",date, "");
//        Post p2 = new Post("Post2", "Tekst posta2", yesterday , "");
//        Post p3 = new Post("Jadran", "Tradicionalni u mirnom ambijentu",dayBeforeYesterday,"");

//        posts.add(p1);
//        posts.add(p2);
//        posts.add(p3);

        return posts;
    }
}
