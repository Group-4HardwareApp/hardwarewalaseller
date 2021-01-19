package com.e.hardwarewalaseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.hardwarewalaseller.databinding.ActivityAddStoreBinding;
import com.squareup.picasso.Picasso;

public class UpdateStore extends AppCompatActivity {

    ActivityAddStoreBinding activityAddStoreBinding;
    SharedPreferences sp = null;
    boolean property = false;
    String buttonName = "Edit Store";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        revlayout(buttonName);//1
        fieldproperties(property);//2
        preferenceCall();//3

        activityAddStoreBinding.btnaddstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(UpdateStore.this, "Edit Store Clicked", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(UpdateStore.this, AddStore.class);
                in.putExtra("updateStore", true);
                startActivity(in);
//                finish();
            }
        });
    }

    private void preferenceCall() {
        activityAddStoreBinding.etownername.setText(sp.getString("name", "name here"));
        activityAddStoreBinding.etstorename.setText(sp.getString("storename", "shop name here"));
        activityAddStoreBinding.etemail.setText(sp.getString("email", "email here"));
        activityAddStoreBinding.etphoneno.setText(sp.getString("mobile", "mobile number here"));
        activityAddStoreBinding.etaddress.setText(sp.getString("address", "address here"));
        Picasso.get().load(sp.getString("imageUrl", "imageurl here")).into(activityAddStoreBinding.civdp);
    }

    private void fieldproperties(boolean property) {

        activityAddStoreBinding.etownername.setFocusable(property);
        activityAddStoreBinding.etownername.setClickable(property);

        activityAddStoreBinding.etstorename.setFocusable(property);
        activityAddStoreBinding.etstorename.setClickable(property);

        activityAddStoreBinding.etphoneno.setFocusable(property);
        activityAddStoreBinding.etphoneno.setClickable(property);

        activityAddStoreBinding.etemail.setFocusable(property);
        activityAddStoreBinding.etemail.setClickable(property);

        activityAddStoreBinding.etaddress.setFocusable(property);
        activityAddStoreBinding.etaddress.setClickable(property);


        activityAddStoreBinding.civdp.setFocusable(property);
        activityAddStoreBinding.civdp.setClickable(property);
    }

    private void revlayout(String buttonName) {
        LayoutInflater inflater = LayoutInflater.from(UpdateStore.this);
        activityAddStoreBinding = ActivityAddStoreBinding.inflate(inflater);
        View view = activityAddStoreBinding.getRoot();
        setContentView(view);
        activityAddStoreBinding.btnaddstore.setText(buttonName);

        setSupportActionBar(activityAddStoreBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Store Info");

        activityAddStoreBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
