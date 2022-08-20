package com.example.redditapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.auth0.android.jwt.JWT;
import com.example.redditapp.ApiClient.RetrofitClientInstance;
import com.example.redditapp.activities.LoginActivity;
import com.example.redditapp.activities.RegisterActivity;
import com.example.redditapp.activities.SplashScreenActivity;
import com.example.redditapp.adapters.DrawerListAdapter;
import com.example.redditapp.fragments.AddCommunityFragment;
import com.example.redditapp.fragments.AddPostFragment;
import com.example.redditapp.fragments.PostFragment;
import com.example.redditapp.fragments.ProfileFragment;
import com.example.redditapp.model.NavItem;
import com.example.redditapp.model.User;
import com.example.redditapp.service.UserApiService;
import com.example.redditapp.tools.FragmentTransition;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private RelativeLayout mDrawerPane;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findLoggedIn();
        prepareMenu(mNavItems);

        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);

        mDrawerPane = findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        SharedPreferences preferences =  getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);


        RelativeLayout profileLayout = findViewById(R.id.profileBox);
        TextView user = findViewById(R.id.userName);

        if(retrivedToken == null) {
            profileLayout.setVisibility(View.INVISIBLE);
        }

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(ProfileFragment.newInstance(), MainActivity.this, false);
                setTitle("Profile");
                mDrawerLayout.closeDrawer(mDrawerPane);
            }
        });

        // Specificiramo da kada se drawer zatvori prikazujemo jednu ikonu
        // kada se drawer otvori drugu. Za to je potrebo da ispranvo povezemo
        // Toolbar i ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }

        /*
         * drawer-u specificiramo za koju referencu toolbar-a da se veze
         * Specificiramo sta ce da pise unutar Toolbar-a kada se drawer otvori/zatvori
         * i specificiramo sta ce da se desava kada se drawer otvori/zatvori.
         * */
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Reddit Clone");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Izborom na neki element iz liste, pokrecemo akciju
        if (savedInstanceState == null) {
            String position = getIntent().getStringExtra("position");
            if(position != null){
                selectItemFromDrawer(Integer.valueOf(position));
            }else {
                selectItemFromDrawer(0);
            }
        }
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
       // String role = TokenUtils.getRole(MainActivity.this);

        SharedPreferences preferences =  getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        //JWT jwt = new JWT(retrivedToken);

        if(retrivedToken == null){
            mNavItems.add(new NavItem(getString(R.string.home), getString(R.string.home_long), R.drawable.ic_home));
            mNavItems.add(new NavItem(getString(R.string.registration), getString(R.string.register_long), R.drawable.ic_register));
            mNavItems.add(new NavItem(getString(R.string.login), getString(R.string.login_long), R.drawable.ic_login));
            System.out.println(mNavItems);
        }
        else{
            mNavItems.add(new NavItem(getString(R.string.home), getString(R.string.home_long), R.drawable.ic_home));
            mNavItems.add(new NavItem(getString(R.string.newCommunity), "Add new community", R.drawable.ic_community_add));
            mNavItems.add(new NavItem("Add post", "Add new post in community", R.drawable.ic_post_add));
            mNavItems.add(new NavItem(getString(R.string.logout), getString(R.string.long_logut), R.drawable.ic_logout));
            System.out.println("NAV ITEMS " + mNavItems);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {

        SharedPreferences preferences =  getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);


        if(retrivedToken == null){
            if(position == 0){
                FragmentTransition.to(PostFragment.newInstance(), this, false);
            }
            else if(position == 1){
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }else if(position == 2){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Log.e("DRAWER", "Nesto van opsega!");
            }
        } else
        {
            if(position == 0){
                FragmentTransition.to(PostFragment.newInstance(), this, false);
            }
            else if(position == 1){
                FragmentTransition.to(AddCommunityFragment.newInstance(), this, false);
            }
            else if(position == 2){
                FragmentTransition.to(AddPostFragment.newInstance(), this, false);
            }
            else if(position == 3){
                preferences.edit().clear().apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Log.e("DRAWER", "Nesto van opsega!");
            }
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    private void findLoggedIn() {

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String userPref = preferences.getString("username", "");
        //       Long idUser = preferences.getLong("id", 0L);

        UserApiService userApiService = RetrofitClientInstance.getRetrofitInstance(MainActivity.this).create(UserApiService.class);
        Call<User> call = userApiService.findByUsername(userPref);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user != null) {
                    preferences.edit().putLong("idUser", user.getId()).apply();
                    preferences.edit().putString("role",user.getRole()).apply();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }
}