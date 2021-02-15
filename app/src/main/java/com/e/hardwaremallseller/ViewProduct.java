package com.e.hardwaremallseller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.hardwaremallseller.adapter.SliderAdapterExample;
import com.e.hardwaremallseller.apis.CommentService;
import com.e.hardwaremallseller.apis.ProductService;
import com.e.hardwaremallseller.beans.Comment;
import com.e.hardwaremallseller.beans.Product;
import com.e.hardwaremallseller.beans.SliderItem;
import com.e.hardwaremallseller.databinding.ActivityViewProductBinding;
import com.github.chrisbanes.photoview.PhotoView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProduct extends AppCompatActivity {

    ActivityViewProductBinding activityViewProductBinding;
    Product p = new Product();
    private SliderAdapterExample sliderAdapterExample;
    float rate,avg,avgRate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewProductBinding = ActivityViewProductBinding.inflate(LayoutInflater.from(this));
        setContentView(activityViewProductBinding.getRoot());
        initComponent();
        //getting intent
        Intent in = getIntent();
        p = (Product) in.getSerializableExtra("product");
        editProduct();
        imageZoom();
        showData();
        deleteProduct();
        viewRating();
    }
/*
    @Override
        protected void onStop() {
        super.onStop();
        finish();
    }
*/

    private void deleteProduct() {
        activityViewProductBinding.btndelelteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = p.getProductId();
//                Toast.makeText(ViewProduct.this, "PID" + id, Toast.LENGTH_SHORT).show();
//                Toast.makeText(ViewProduct.this, "SID" + p.getShopkeeperId(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(Home.this, "Sign Out", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder ab = new AlertDialog.Builder(ViewProduct.this);
                ab.setMessage("Do you want to delete this product ?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog pd = new ProgressDialog(ViewProduct.this);
                        pd.setTitle("Please wait...");
                        pd.show();

                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.deleteProduct(id);
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                Toast.makeText(ViewProduct.this, "Product Deleted !", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(ViewProduct.this, "Unable to delete product", Toast.LENGTH_SHORT).show();
                            }
                        });
                        pd.dismiss();
                        //here method to lead to intent
                        finish();
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.show();
            }
        });

    }

    private void editProduct() {

        activityViewProductBinding.btneditproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insend = new Intent(ViewProduct.this, UpdateProduct.class);
                insend.putExtra("productInfo", p);
                startActivity(insend);
//            Intent x = new Intent(ViewProduct.this,AddProduct.class);
//            x.putExtra("true",true);
//            startActivity(x);
            }
        });
    }

    private void initComponent() {
        setSupportActionBar(activityViewProductBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Information");
        activityViewProductBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    private void showData() {
//        Picasso.get().load(p.getImageUrl()).into(activityViewProductBinding.iv);
        sliderAdapterExample = new SliderAdapterExample(this);
        activityViewProductBinding.iv.setSliderAdapter(sliderAdapterExample);
        activityViewProductBinding.iv.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        activityViewProductBinding.iv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        activityViewProductBinding.iv.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        activityViewProductBinding.iv.setIndicatorSelectedColor(R.color.colorblack);
        activityViewProductBinding.iv.setIndicatorMargin(1);
        activityViewProductBinding.iv.setIndicatorUnselectedColor(Color.WHITE);
        activityViewProductBinding.iv.setScrollTimeInSec(2);
        activityViewProductBinding.iv.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {

            }
        });


        activityViewProductBinding.tvName.setText(p.getName());
        activityViewProductBinding.tvPrice.setText("₹ " + p.getPrice() + "/-");
        activityViewProductBinding.tvDiscount.setText("" + p.getDiscount() + "%");
        activityViewProductBinding.tvBrand.setText(p.getBrand());
        activityViewProductBinding.tvQuantity.setText("" + p.getQtyInStock());
        activityViewProductBinding.tvDescription.setText(p.getDescription());
        renewItems(activityViewProductBinding.getRoot());


    }

    public void renewItems(View view) {

        List<SliderItem> sliderItemList = new ArrayList<>();
        if (p.getImageUrl()!=null){
            SliderItem sliderItem1=new SliderItem();
            sliderItem1.setImageUrl(p.getImageUrl());
            sliderItemList.add(sliderItem1);
            if (p.getSecondImageUrl()!=null){
                SliderItem sliderItem2=new SliderItem();
                sliderItem2.setImageUrl(p.getSecondImageUrl());
                sliderItemList.add(sliderItem2);
                if (p.getThirdImageurl()!=null){
                    SliderItem sliderItem3=new SliderItem();
                    sliderItem3.setImageUrl(p.getThirdImageurl());
                    sliderItemList.add(sliderItem3);
                }
            }
        }
        sliderAdapterExample.renewItems(sliderItemList);
    }



    private void imageZoom() {

        activityViewProductBinding.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewProduct.this);
                View mView = LayoutInflater.from(ViewProduct.this).inflate(R.layout.imagezoom, null);
                PhotoView photoView = mView.findViewById(R.id.photoview);
                Picasso.get().load(p.getImageUrl())
                        .placeholder(R.drawable.nm)
                        .into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }


    private void viewRating() {

        if (isConnected()) {
            String id = p.getProductId();
            CommentService.CommentApi commentApi = CommentService.getCommentApiInstance();
            Call<ArrayList<Comment>> call = commentApi.getCommentOfProduct(id);
            call.enqueue(new Callback<ArrayList<Comment>>() {
                @Override
                public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                    if (response.code() == 200) {
                        final ArrayList<Comment> commentList = response.body();
                        for (Comment c : commentList) {
                            rate = Float.valueOf(c.getRating()).floatValue();
                            avgRate = avgRate + rate;
                            avg = avgRate / 5;
                            activityViewProductBinding.ratingBar.setRating(avg);
                            activityViewProductBinding.tvRate.setText("" + avg + "/5");
                        }
                        activityViewProductBinding.tvviewrating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(ViewProduct.this,RatingActivity.class);
                                in.putExtra("commentList",commentList);
                                startActivity(in);
                            }
                        });
                    } else
                        Toast.makeText(ViewProduct.this, "Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

                }
            });
        } else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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
