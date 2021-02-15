package com.e.hardwaremallseller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.hardwaremallseller.adapter.OrderHistoryAdapter;
import com.e.hardwaremallseller.beans.ItemList;
import com.e.hardwaremallseller.databinding.ActivityPackageHistoryBinding;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    OrderHistoryAdapter orderHistoryAdapter;
    ArrayList<ItemList> orderItemsList;
    ActivityPackageHistoryBinding activityPackageHistoryBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPackageHistoryBinding = ActivityPackageHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(activityPackageHistoryBinding.getRoot());

        setSupportActionBar(activityPackageHistoryBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
//        Toast.makeText(this, "OH", Toast.LENGTH_SHORT).show();

        activityPackageHistoryBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Intent in = getIntent();
        orderItemsList = (ArrayList<ItemList>) in.getSerializableExtra("itemList");

        for (ItemList i : orderItemsList) {
            Log.e("items", "" + i);
            orderHistoryAdapter = new OrderHistoryAdapter(OrderHistory.this, orderItemsList);
            activityPackageHistoryBinding.rvpackagehistory.setAdapter(orderHistoryAdapter);
            activityPackageHistoryBinding.rvpackagehistory.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
