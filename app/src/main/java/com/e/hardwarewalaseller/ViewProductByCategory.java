package com.e.hardwarewalaseller;

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
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.hardwarewalaseller.adapter.ProductAdapter;
import com.e.hardwarewalaseller.apis.ProductService;
import com.e.hardwarewalaseller.beans.Category;
import com.e.hardwarewalaseller.beans.Product;
import com.e.hardwarewalaseller.databinding.ActivityViewProductByCategoryBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProductByCategory extends AppCompatActivity {
    SharedPreferences sp;
    ProductAdapter productAdapter;
    ArrayList<Product> productList =null;
    ActivityViewProductByCategoryBinding activityViewProductByCategoryBinding;
    String shopkeeperId="";
    Category category=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityViewProductByCategoryBinding = ActivityViewProductByCategoryBinding.inflate(LayoutInflater.from(this));
        setContentView(activityViewProductByCategoryBinding.getRoot());

        setSupportActionBar(activityViewProductByCategoryBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        shopkeeperId = sp.getString("userId", "");

        category = (Category) in.getSerializableExtra("category");
//        String categoryId = category.getCategoryId();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(productAdapter!=null)
            productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isConnected()) {
            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = productApi.getProductByCategoryAndShopKeeper(category.getCategoryId(), shopkeeperId);
//            Toast.makeText(this, ""+category.getCategoryId()+"  "+shopkeeperId, Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                     productList = response.body();
                    productAdapter = new ProductAdapter(ViewProductByCategory.this, productList);

                    if (productList == null) {
//                        Toast.makeText(ViewProductByCategory.this, "WASP", Toast.LENGTH_SHORT).show();
                        Log.e("SHowing Null", "SSSS");
                    }
//                    Toast.makeText(ViewProductByCategory.this, "all products of this category", Toast.LENGTH_SHORT).show();
                    productAdapter.notifyDataSetChanged();


                    activityViewProductByCategoryBinding.rvcategoryshopkeeper.setAdapter(productAdapter);
                    activityViewProductByCategoryBinding.rvcategoryshopkeeper.setLayoutManager(new GridLayoutManager(ViewProductByCategory.this, 2));

                    productAdapter.setOnItemClickListener(new ProductAdapter.onRecyclerViewClick() {
                        @Override
                        public void onItemClick(Product p, int position) {
//                            Toast.makeText(ViewProductByCategory.this, "Send Intent or send product", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(ViewProductByCategory.this, ViewProduct.class);
                            in.putExtra("product", p);
                            startActivity(in);

                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(ViewProductByCategory.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                    Log.e("TAG",""+t);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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



