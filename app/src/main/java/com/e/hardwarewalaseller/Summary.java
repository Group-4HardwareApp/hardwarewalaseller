package com.e.hardwarewalaseller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.hardwarewalaseller.adapter.PackageHistoryAdapter;
import com.e.hardwarewalaseller.apis.OrderService;
import com.e.hardwarewalaseller.beans.ItemList;
import com.e.hardwarewalaseller.beans.Order;
import com.e.hardwarewalaseller.databinding.ActivitystatsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Summary extends AppCompatActivity {
    double totalAmount = 0;
    int TotalproductQuantity = 0;
    ActivitystatsBinding activitystatsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitystatsBinding = ActivitystatsBinding.inflate(LayoutInflater.from(this));
        setContentView(activitystatsBinding.getRoot());



        setSupportActionBar(activitystatsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales & Profit");


        activitystatsBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        activitystatsBinding.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Summary.this, "Total amount gain till date", Toast.LENGTH_SHORT).show();
            }
        });

        activitystatsBinding.cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Summary.this, "Total number of products sold till date", Toast.LENGTH_SHORT).show();
            }
        });

//        String shopkeeperId = "9GqnMcWuDOdrL99lPbdelvybnNV2";
        String shopkeeperId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (isConnected()) {
            OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
            Call<ArrayList<Order>> call = orderApi.getPurchaseOrder(shopkeeperId);
            call.enqueue(new Callback<ArrayList<Order>>() {
                @Override
                public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                    ArrayList<Order> orderList = response.body();
                    ArrayList<ItemList> itemList;

                    for (Order o : orderList) {
                        if (o.getShippingStatus().equals("Delivered")) {
                            totalAmount = totalAmount + o.getTotalAmount();
                            itemList = (ArrayList<ItemList>) o.getItemList();

                            for (ItemList i : itemList) {
                                TotalproductQuantity = (int) (TotalproductQuantity + i.getQty());
                            }
                        }
                    }
//                    Toast.makeText(Summary.this, ""+totalAmount, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Summary.this, ""+TotalproductQuantity, Toast.LENGTH_SHORT).show();
                    activitystatsBinding.tvamount.setText("" + totalAmount);
                    activitystatsBinding.tvproductcount.setText("" + TotalproductQuantity);
                }

                @Override
                public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                    Toast.makeText(Summary.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                }
            });
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
