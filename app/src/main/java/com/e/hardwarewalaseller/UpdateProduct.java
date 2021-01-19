package com.e.hardwarewalaseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.hardwarewalaseller.apis.CategoryService;
import com.e.hardwarewalaseller.beans.Category;
import com.e.hardwarewalaseller.beans.Product;
import com.e.hardwarewalaseller.databinding.ActivityAddProductBinding;
import com.google.common.graph.ImmutableNetwork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProduct extends AppCompatActivity {


    boolean property = false;
    Product p = new Product();
    ActivityAddProductBinding activityAddProductBinding;
    String buttonName = "Edit Product";
    public static ArrayList<Category> categoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        revlayout(buttonName);//1
        categorySharedPref();
//        getCategories();
        getIntentData();
        initComponents();
        fieldProperties(property);
        sendingData();
    }

//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(this, "OnStop", Toast.LENGTH_SHORT).show();
//        finish();
//    }

    private void categorySharedPref() {

        SharedPreferences sharedPreferences = getSharedPreferences("categories", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("jsoncategorylist", null);
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        categoryList = gson.fromJson(json, type);

        if (categoryList == null) {
//            Toast.makeText(this, "list null", Toast.LENGTH_SHORT).show();
        } else {
/*
            for(Category c: categoryList)
            {
                Toast.makeText(this, "cccc"+c.getCategoryName(), Toast.LENGTH_SHORT).show();
            }
*/
        }
    }

    private void sendingData() {

        activityAddProductBinding.btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* for(Category c: categoryList)
                {
                    Toast.makeText(UpdateProduct.this, "cccc"+c.getCategoryName(), Toast.LENGTH_SHORT).show();
                }*/
//                Toast.makeText(UpdateProduct.this, "to final update info", Toast.LENGTH_SHORT).show();
//                Intent insendProduct = new Intent(UpdateProduct.this, UpdateProductInfo.class);
                Intent insendProduct = new Intent(UpdateProduct.this, AddProduct.class);
                insendProduct.putExtra("isUpdate", true);
                insendProduct.putExtra("categoryList", categoryList);
                insendProduct.putExtra("product", p);
                startActivity(insendProduct);
            }
        });
    }

    private void getIntentData() {
        Intent inrecieve = getIntent();
        p = (Product) inrecieve.getSerializableExtra("productInfo");
    }

    private void initComponents() {
        for (Category c : categoryList) {
            if (c.getCategoryId().equals(p.getCategoryId())) {
                activityAddProductBinding.etcategoryname.setText("" + c.getCategoryName());
                break;
            } else {
//                Toast.makeText(this, "No Category Match", Toast.LENGTH_SHORT).show();
            }
        }
        Picasso.get().load(p.getImageUrl()).into(activityAddProductBinding.iv1);
        Picasso.get().load(p.getSecondImageUrl()).into(activityAddProductBinding.iv2);
        Picasso.get().load(p.getThirdImageurl()).into(activityAddProductBinding.iv3);
        activityAddProductBinding.etproductname.setText(p.getName());
        activityAddProductBinding.etprice.setText("" + (int) p.getPrice());
        activityAddProductBinding.etquantity.setText("" + p.getQtyInStock());
        activityAddProductBinding.etbrandname.setText(p.getBrand());
        activityAddProductBinding.etdescription.setText(p.getDescription());
        activityAddProductBinding.etdiscount.setText("" + (int) p.getDiscount());
    }

    private void fieldProperties(boolean property) {

        activityAddProductBinding.etcategoryname.setFocusable(property);
        activityAddProductBinding.etcategoryname.setClickable(property);
        activityAddProductBinding.etcategoryname.setCursorVisible(property);

        activityAddProductBinding.etproductname.setFocusable(property);
        activityAddProductBinding.etproductname.setClickable(property);
        activityAddProductBinding.etproductname.setCursorVisible(property);

        activityAddProductBinding.etprice.setFocusable(property);
        activityAddProductBinding.etprice.setClickable(property);
        activityAddProductBinding.etprice.setCursorVisible(property);

        activityAddProductBinding.etbrandname.setFocusable(property);
        activityAddProductBinding.etbrandname.setClickable(property);
        activityAddProductBinding.etbrandname.setCursorVisible(property);

        activityAddProductBinding.etquantity.setFocusable(property);
        activityAddProductBinding.etquantity.setClickable(property);
        activityAddProductBinding.etquantity.setCursorVisible(property);

        activityAddProductBinding.etdiscount.setFocusable(property);
        activityAddProductBinding.etdiscount.setClickable(property);
        activityAddProductBinding.etdiscount.setCursorVisible(property);

        activityAddProductBinding.etdescription.setFocusable(property);
        activityAddProductBinding.etdescription.setClickable(property);
        activityAddProductBinding.etdescription.setCursorVisible(property);
    }

    private void revlayout(String buttonName) {


        LayoutInflater inflater = LayoutInflater.from(UpdateProduct.this);
        activityAddProductBinding = ActivityAddProductBinding.inflate(inflater);
        View view = activityAddProductBinding.getRoot();
        setContentView(view);

        setSupportActionBar(activityAddProductBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Product Details");

        activityAddProductBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityAddProductBinding.ll3.setVisibility(View.VISIBLE);
        activityAddProductBinding.ilcategoryname.setVisibility(View.VISIBLE);
        activityAddProductBinding.btnaddproduct.setText(buttonName);
    }

}

