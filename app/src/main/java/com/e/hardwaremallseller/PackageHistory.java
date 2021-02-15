package com.e.hardwaremallseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.hardwaremallseller.adapter.PackageHistoryAdapter;
import com.e.hardwaremallseller.apis.OrderService;
import com.e.hardwaremallseller.beans.Order;
import com.e.hardwaremallseller.databinding.ActivityPackageHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageHistory extends AppCompatActivity {
    ActivityPackageHistoryBinding activityPackageHistoryBinding;
    PackageHistoryAdapter packageHistoryAdapter;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPackageHistoryBinding = ActivityPackageHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(activityPackageHistoryBinding.getRoot());

//        activityPackageHistoryBinding.toolbar.setTitle("Package History");


        Intent in = getIntent();
        String Status = in.getStringExtra("status");
//        String shopkeeperId = "9GqnMcWuDOdrL99lPbdelvybnNV2";
        String shopkeeperId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(this, "shopkeeperId" + shopkeeperId, Toast.LENGTH_SHORT).show();

        if (isConnected()) {
            //String history orders st
            if (Status.equals("history")) {


                setSupportActionBar(activityPackageHistoryBinding.toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Order History");

                OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
                Call<ArrayList<Order>> call = orderApi.getPurchaseOrder(shopkeeperId);
                call.enqueue(new Callback<ArrayList<Order>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                        ArrayList<Order> orderList = response.body();
//                        Toast.makeText(PackageHistory.this, "IN onresponse", Toast.LENGTH_SHORT).show();
                        packageHistoryAdapter = new PackageHistoryAdapter(PackageHistory.this, orderList);
                        activityPackageHistoryBinding.rvpackagehistory.setAdapter(packageHistoryAdapter);
                        activityPackageHistoryBinding.rvpackagehistory.setLayoutManager(new LinearLayoutManager(PackageHistory.this));
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                        Toast.makeText(PackageHistory.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (Status.equals("current")) {
                //we used same code for both conditions but api is different.
                setSupportActionBar(activityPackageHistoryBinding.toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Manage Orders");




                OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
                Call<ArrayList<Order>> call = orderApi.getOnGoingOrder(shopkeeperId);
                call.enqueue(new Callback<ArrayList<Order>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                        ArrayList<Order> orderList = response.body();
                        packageHistoryAdapter = new PackageHistoryAdapter(PackageHistory.this, orderList);
                        activityPackageHistoryBinding.rvpackagehistory.setAdapter(packageHistoryAdapter);
                        activityPackageHistoryBinding.rvpackagehistory.setLayoutManager(new LinearLayoutManager(PackageHistory.this));
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                        Log.e("ERROR",""+t);
                        Toast.makeText(PackageHistory.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //String history orders end

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
