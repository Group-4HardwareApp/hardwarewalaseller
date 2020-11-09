package com.e.hardwarewalaseller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(AddStore.this);
        activityAddStoreBinding = ActivityAddStoreBinding.inflate(inflater);

        View view = activityAddStoreBinding.getRoot();
        setContentView(view);
        awesomeValidation  = new AwesomeValidation(BASIC);


        activityAddStoreBinding.civdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddStore.this);
            }
        });

        awesomeValidation.addValidation(activityAddStoreBinding.etownername,"[^\\s*$][a-zA-Z\\s]+","Enter correct name");
        awesomeValidation.addValidation(activityAddStoreBinding.etemail, Patterns.EMAIL_ADDRESS,"Enter correct Email");
        awesomeValidation.addValidation(activityAddStoreBinding.etphoneno,"^[0-9]{10}$","Enter correct  Mobile number");
        awesomeValidation.addValidation(activityAddStoreBinding.etstorename,"[^\\s*$][a-zA-Z\\s]+","Enter correct Storename");
        awesomeValidation.addValidation(activityAddStoreBinding.etaddress,"[^\\s*$][a-zA-Z0-9,/\\s]+","Enter correct Address");

        activityAddStoreBinding.btnaddstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    Log.e("isConnected","TRUE");
        Log.e("VALIDATION",""+awesomeValidation.validate());

                    if(awesomeValidation.validate())
                    {
                        String address = activityAddStoreBinding.etaddress.getText().toString().trim();
                        String email = activityAddStoreBinding.etemail.getText().toString().trim();
                        String ownername = activityAddStoreBinding.etownername.getText().toString().trim();
                        String phone = activityAddStoreBinding.etphoneno.getText().toString().trim();
                        String storename = activityAddStoreBinding.etstorename.getText().toString().trim();
                        String token = "tok_qe2k3";


                        if(imageUri!=null)
                        {
                            File file =  FileUtils.getFile(AddStore.this,imageUri);
                            RequestBody requestFile =
                                    RequestBody.create(
                                            MediaType.parse("image/*"),
                                            file);

                            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                            RequestBody username = RequestBody.create(okhttp3.MultipartBody.FORM,ownername);
                            RequestBody useraddress = RequestBody.create(okhttp3.MultipartBody.FORM,address);
                            RequestBody useremail = RequestBody.create(okhttp3.MultipartBody.FORM,email);
                            RequestBody usercontact = RequestBody.create(okhttp3.MultipartBody.FORM,phone);
                            RequestBody userstorename = RequestBody.create(okhttp3.MultipartBody.FORM,storename);
                            RequestBody usertoken = RequestBody.create(okhttp3.MultipartBody.FORM,token);

                            ShopkeeperService.ShopkeeperApi shopkeeperApi =ShopkeeperService.getShopkeeperApiInstance();
                            Call<Shopkeeper> call = shopkeeperApi.saveShopkeeper(body,username,userstorename,usercontact,useraddress,useremail,usertoken);
                            call.enqueue(new Callback<Shopkeeper>() {
                                @Override
                                public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {

                                    Shopkeeper shopkeeper = response.body();
                                    Toast.makeText(AddStore.this, "Success", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Shopkeeper> call, Throwable t) {
                                    Toast.makeText(AddStore.this, "Failure", Toast.LENGTH_SHORT).show();
                                    Log.e("THROWED",""+t);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(AddStore.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(AddStore.this, "Enter Correct input", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 imageUri = result.getUri();
                Log.e("URI",""+imageUri);
//                Toast.makeText(this, "FIRST"+imageUri, Toast.LENGTH_SHORT).show();
                activityAddStoreBinding.civdp.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("ERRORIMAGE",""+error);

            }
        }
    }


    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}