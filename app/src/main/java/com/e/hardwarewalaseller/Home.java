package com.e.hardwarewalaseller;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.e.hardwarewalaseller.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

public class Home extends AppCompatActivity {


    ActivityHomeBinding activityHomeBinding;
    ActionBarDrawerToggle toggle;
    String inputQuery;
    ImageButton addproduct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = LayoutInflater.from(Home.this);
        activityHomeBinding = ActivityHomeBinding.inflate(inflater);
        View v = activityHomeBinding.getRoot();
        setContentView(v);

        setSupportActionBar(activityHomeBinding.toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this,activityHomeBinding.drawer,activityHomeBinding.toolbar,R.string.open,R.string.close);
//        activityHomeBinding.drawer.addDrawerListener(toggle);
        activityHomeBinding.drawer.addDrawerListener(toggle);
        toggle.syncState();
//        activityHomeBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        activityHomeBinding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id==R.id.updatestore)
                {
                    Toast.makeText(Home.this, "Update Store clicked !", Toast.LENGTH_SHORT).show();
                }


                else if(id==R.id.stats)
                {
                    Toast.makeText(Home.this, "stats Clicked !", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater1 = getMenuInflater();
        inflater1.inflate(R.menu.toolbar_menu_itemlist, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        ComponentName cn = new ComponentName(this, Home.class);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    MenuItemCompat.collapseActionView(searchItem);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Home.this, "ZIP"+query, Toast.LENGTH_SHORT).show();
                inputQuery=query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }
             

}
