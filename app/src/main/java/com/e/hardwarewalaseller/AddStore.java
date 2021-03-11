package com.e.hardwarewalaseller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.e.hardwarewalaseller.apis.ShopkeeperService;
import com.e.hardwarewalaseller.beans.Shopkeeper;
import com.e.hardwarewalaseller.databinding.ActivityAddStoreBinding;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class AddStore extends AppCompatActivity {
    Uri imageUri;


    ActivityAddStoreBinding activityAddStoreBinding;
    SharedPreferences sp = null;
    AwesomeValidation awesomeValidation;
    ProgressDialog progressDialog;
    boolean updateStoreInfo = false;
    MultipartBody.Part body;
    AlertDialog.Builder ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(AddStore.this);
        activityAddStoreBinding = ActivityAddStoreBinding.inflate(inflater);

        sp = getSharedPreferences("user", MODE_PRIVATE);

        View view = activityAddStoreBinding.getRoot();
        setContentView(view);
        awesomeValidation = new AwesomeValidation(BASIC);

        awesomeValidation.addValidation(activityAddStoreBinding.etownername, "[^\\s*$][a-zA-Z\\s]+", "Enter correct Name");
        awesomeValidation.addValidation(activityAddStoreBinding.etemail, Patterns.EMAIL_ADDRESS, "Enter correct Email");
        awesomeValidation.addValidation(activityAddStoreBinding.etphoneno, "^[0-9]{10}$", "Enter correct  Mobile number");
        awesomeValidation.addValidation(activityAddStoreBinding.etstorename, "[^\\s*$][a-zA-Z\\s]+", "Enter correct Storename");
        awesomeValidation.addValidation(activityAddStoreBinding.etaddress, "[^\\s*$][a-zA-Z0-9,/\\s]+", "Enter correct Address");
        addStore();

        activityAddStoreBinding.civdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
//                Toast.makeText(AddStore.this, "Add Store IMG", Toast.LENGTH_SHORT).show();
            }
        });

        Intent in = getIntent();
        updateStoreInfo = in.getBooleanExtra("updateStore", false);
        if (updateStoreInfo) {
            updateStoreData();
//            Toast.makeText(this, "Update Store intent", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Add Store Screen", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStoreData() {

        setSupportActionBar(activityAddStoreBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Store");

        activityAddStoreBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        activityAddStoreBinding.btnupdateImage.setVisibility(View.VISIBLE);
        preferenceCall();
//        Toast.makeText(this, "IN update Store", Toast.LENGTH_SHORT).show();

        activityAddStoreBinding.btnaddstore.setText("Update Store Info");

        activityAddStoreBinding.btnupdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {

                    if (imageUri != null) {
//                    String shopkeeperId=sp.getString("userId","shopkeeper");
                        RequestBody shopkeeperId = RequestBody.create(MultipartBody.FORM, sp.getString("userId", "shopkeeper"));

                        body = checkforImage();

                        progressDialog =new ProgressDialog(AddStore.this);

                        progressDialog.setTitle("Uploading Image");
                        progressDialog.setMessage("Please wait while uploading image..");
                        progressDialog.show();

                        ShopkeeperService.ShopkeeperApi shopkeeperApiforImageUpdate = ShopkeeperService.getShopkeeperApiInstance();

                        Call<Shopkeeper> callforImageUpdate = shopkeeperApiforImageUpdate.updateShopkeeperImage(body, shopkeeperId);
                        callforImageUpdate.enqueue(new Callback<Shopkeeper>() {
                            @Override
                            public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
//                                Toast.makeText(AddStore.this, "ImageUpdated", Toast.LENGTH_SHORT).show();
                                Shopkeeper shopkeeper = response.body();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("imageUrl", shopkeeper.getImageUrl());
                                editor.commit();
                                progressDialog.dismiss();

//                                Toast.makeText(AddStore.this, "Image Updated Successfully !", Toast.LENGTH_SHORT).show();

                                Log.e("EUREKA", "EUREKA");
                            }

                            @Override
                            public void onFailure(Call<Shopkeeper> call, Throwable t) {

                                Toast.makeText(AddStore.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(AddStore.this, "Select Image to Update", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddStore.this, "No Interent Connection to Update Image", Toast.LENGTH_SHORT).show();
                }


            }
        });


        activityAddStoreBinding.btnaddstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {

                    if (awesomeValidation.validate()) {

                        String addresss = activityAddStoreBinding.etaddress.getText().toString().trim();
                        String email = activityAddStoreBinding.etemail.getText().toString().trim();
                        String ownername = activityAddStoreBinding.etownername.getText().toString().trim();
                        String phone = activityAddStoreBinding.etphoneno.getText().toString().trim();
                        String storename = activityAddStoreBinding.etstorename.getText().toString().trim();
                        String id = sp.getString("userId", "id here");
//                        String token = sp.getString("token","token here");

                        String token = FirebaseInstanceId.getInstance().getToken();
                        Shopkeeper s = new Shopkeeper();
                        s.setShopkeeperId(id);
                        s.setName(ownername);
                        s.setAddress(addresss);
                        s.setContactNumber(phone);
                        s.setEmail(email);
                        s.setShopName(storename);
                        s.setToken(token);

                        Log.e("prec", "Updated" + s.getName() + s.getShopkeeperId() + s.getAddress());

                        progressDialog = new ProgressDialog(AddStore.this);
                        progressDialog.setTitle("Updating Store");
                        progressDialog.setMessage("Please wait while updating store..");
                        progressDialog.show();

                        ShopkeeperService.ShopkeeperApi shopkeeperApi = ShopkeeperService.getShopkeeperApiInstance();
                        Call<Shopkeeper> call = shopkeeperApi.updateShopkeeper(s);
                        call.enqueue(new Callback<Shopkeeper>() {
                            @Override
                            public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {

                                Shopkeeper shopkeeper = response.body();

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("name", shopkeeper.getName());
                                editor.putString("address", shopkeeper.getAddress());
                                editor.putString("mobile", shopkeeper.getContactNumber());
                                editor.putString("imageUrl", shopkeeper.getImageUrl());
                                editor.putString("email", shopkeeper.getEmail());
                                editor.putString("userId", shopkeeper.getShopkeeperId());
                                editor.putString("storename", shopkeeper.getShopName());
                                editor.putString("token", shopkeeper.getToken());

                                editor.commit();

                                Toast.makeText(AddStore.this, " Store updated successfully !", Toast.LENGTH_SHORT).show();
//                                Log.e("siddharth","INFO"+nam+addr);
                                progressDialog.dismiss();
//                                finish();

                                Intent intent=new Intent(AddStore.this,Home.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onFailure(Call<Shopkeeper> call, Throwable t) {
                                Toast.makeText(AddStore.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(AddStore.this, "Enter correct input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddStore.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityAddStoreBinding.civdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                //update side
//                Toast.makeText(AddStore.this, "INSIDE UPDTE==>URI__"+imageUri, Toast.LENGTH_SHORT).show();
                Log.e("URIAFTER", "" + imageUri);
            }
        });

        updateStoreInfo = false;
    }

    private void preferenceCall() {
        activityAddStoreBinding.etownername.setText(sp.getString("name", "name here"));
        activityAddStoreBinding.etstorename.setText(sp.getString("storename", "shop name here"));
        activityAddStoreBinding.etemail.setText(sp.getString("email", "email here"));
        activityAddStoreBinding.etphoneno.setText(sp.getString("mobile", "mobile number here"));
        activityAddStoreBinding.etaddress.setText(sp.getString("address", "address here"));
        Picasso.get().load(sp.getString("imageUrl", "imageurl here")).into(activityAddStoreBinding.civdp);
    }

    private void addStore() {


        setSupportActionBar(activityAddStoreBinding.toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Profile");


        activityAddStoreBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        activityAddStoreBinding.btnaddstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    Log.e("isConnected", "TRUE");
//        Log.e("VALIDATION",""+awesomeValidation.validate());
                    if (awesomeValidation.validate()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String address = activityAddStoreBinding.etaddress.getText().toString().trim();
                        String email = activityAddStoreBinding.etemail.getText().toString().trim();
                        String ownername = activityAddStoreBinding.etownername.getText().toString().trim();
                        String phone = activityAddStoreBinding.etphoneno.getText().toString().trim();
                        String storename = activityAddStoreBinding.etstorename.getText().toString().trim();
//                        String token = "tok_qe2k3";
                        String token = FirebaseInstanceId.getInstance().getToken();

                        if (imageUri != null) {
                            body = checkforImage();
                            RequestBody username = RequestBody.create(MultipartBody.FORM, ownername);
                            RequestBody useraddress = RequestBody.create(MultipartBody.FORM, address);
                            RequestBody useremail = RequestBody.create(MultipartBody.FORM, email);
                            RequestBody usercontact = RequestBody.create(MultipartBody.FORM, phone);
                            RequestBody userstorename = RequestBody.create(MultipartBody.FORM, storename);
                            RequestBody usertoken = RequestBody.create(MultipartBody.FORM, token);
                            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, id);

                            ShopkeeperService.ShopkeeperApi shopkeeperApi = ShopkeeperService.getShopkeeperApiInstance();
                            Call<Shopkeeper> call = shopkeeperApi.saveShopkeeper(body, username, userstorename, usercontact, useraddress, useremail, usertoken, userId);
                            call.enqueue(new Callback<Shopkeeper>() {
                                @Override
                                public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {

                                    Shopkeeper shopkeeper = response.body();

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("name", shopkeeper.getName());
                                    editor.putString("address", shopkeeper.getAddress());
                                    editor.putString("mobile", shopkeeper.getContactNumber());
                                    editor.putString("imageUrl", shopkeeper.getImageUrl());
                                    editor.putString("email", shopkeeper.getEmail());
                                    editor.putString("userId", shopkeeper.getShopkeeperId());
                                    editor.putString("storename", shopkeeper.getShopName());
                                    editor.putString("token", shopkeeper.getToken());
//                                    editor.putString("ImageUri",""+imageUri);
                                    editor.commit();

                                    Toast.makeText(AddStore.this, "Store added Succesfully !", Toast.LENGTH_SHORT).show();
                                    Log.e("AddStore", "In AddStore");
                                    Log.e("imageurl+uri", imageUri + "" + shopkeeper.getImageUrl());
                                    Intent in = new Intent(AddStore.this, Home.class);
                                    startActivity(in);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Shopkeeper> call, Throwable t) {
                                    Toast.makeText(AddStore.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                                    Log.e("THROWED", "" + t);
                                }
                            });
                        } else {
                            Toast.makeText(AddStore.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddStore.this, "Enter correct input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private MultipartBody.Part checkforImage() {

//        Toast.makeText(this, "IN IMAGE BODY", Toast.LENGTH_SHORT).show();
        File file = FileUtils.getFile(AddStore.this, imageUri);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Log.e("BODY", "" + body);
        return body;
    }

    private void uploadImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddStore.this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Log.e("URI", "" + imageUri);
//                Toast.makeText(this, "FIRST"+imageUri, Toast.LENGTH_SHORT).show();
                activityAddStoreBinding.civdp.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("ERRORIMAGE", "" + error);

            }
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