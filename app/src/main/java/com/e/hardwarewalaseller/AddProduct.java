package com.e.hardwarewalaseller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.e.hardwarewalaseller.apis.ProductService;
import com.e.hardwarewalaseller.beans.Category;
import com.e.hardwarewalaseller.beans.Product;
import com.e.hardwarewalaseller.databinding.ActivityAddProductBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class AddProduct extends AppCompatActivity {
    String pos;
    Product p = new Product();
    public static ArrayList<Category> categoryList;
    String categoryId;
    Uri imageUri;
    Uri secondImageUri;
    Uri thirdImageuri;
    AwesomeValidation awesomeValidation;
    MultipartBody.Part body,body2,body3;
    ActivityAddProductBinding activityAddProductBinding;
    boolean isUpdate = false;
    ArrayAdapter<Category> adapter;
    boolean isSpinnerTouched=false;
    ProgressDialog progressDialog;
    AlertDialog.Builder ab;
    SharedPreferences sp ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          LayoutInflater inflater = LayoutInflater.from(AddProduct.this);
        activityAddProductBinding = ActivityAddProductBinding.inflate(inflater);
        View view = activityAddProductBinding.getRoot();
        setContentView(view);

        awesomeValidation  =  new AwesomeValidation(BASIC);
        activityAddProductBinding.ll2.setVisibility(View.VISIBLE);
        activityAddProductBinding.spinner.setVisibility(View.VISIBLE);
        activityAddProductBinding.tvcat.setVisibility(View.VISIBLE);
//        Toast.makeText(this, "+oncreate", Toast.LENGTH_SHORT).show();
        sp = getSharedPreferences("user",MODE_PRIVATE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }

        int initialposition = activityAddProductBinding.spinner.getSelectedItemPosition();
        activityAddProductBinding.spinner.setSelection(initialposition,false);

        /*activityAddProductBinding.spinner*/
        getCategoryList();

        validatingField();

        addBrandName();

        Intent intentupdate = getIntent();
        p= (Product) intentupdate.getSerializableExtra("product");
        isUpdate = intentupdate.getBooleanExtra("isUpdate",false);
        if(isUpdate)
        {   activityAddProductBinding.btnupdateImage.setVisibility(View.VISIBLE);
            activityAddProductBinding.etcategoryname.setFocusable(false);
            activityAddProductBinding.etcategoryname.setClickable(false);
            activityAddProductBinding.etcategoryname.setCursorVisible(false);
            activityAddProductBinding.ll3.setVisibility(View.VISIBLE);
            activityAddProductBinding.ilcategoryname.setVisibility(View.VISIBLE);
            activityAddProductBinding.btnaddproduct.setText("Update Product");
            for(Category c: categoryList)
            {
                if(c.getCategoryId().equals(p.getCategoryId()))
                {
                    activityAddProductBinding.etcategoryname.setText("" + c.getCategoryName());
                    break;
                }

            }

            initComponents();
//            Toast.makeText(this, "update working", Toast.LENGTH_SHORT).show();
            UpdateProductDetails();
//            Toast.makeText(this, "calling image upload", Toast.LENGTH_SHORT).show();
        }
        else
        {

            addProduct();
            addProductImage();
        }

    }

    private void addBrandName() {
//        activityAddProductBinding.etbrandname.auto
        ArrayList<String> brandList = new ArrayList<>();
        brandList.add("Sintex");
        brandList.add("Tango");
        brandList.add("Havells ");
        brandList.add("Philips");
        brandList.add("Polycab");
            brandList.add("Taparia");
            brandList.add("Hitachi");
            brandList.add("Vega");
            brandList.add("Godrej");
            brandList.add("Tata");
            brandList.add("Cumi Metabo");
            brandList.add("Rolson");
            brandList.add("Dewalt Leatherman");
            brandList.add("Jackly ");
            brandList.add("Europa");
            brandList.add("Aakrati ");
            brandList.add("Jaquar");
            brandList.add("Ceramic");
            brandList.add("Kaymo");
            brandList.add("Cumi");
            brandList.add("Hindware");
            brandList.add("Taisen ");
            brandList.add("Kasta");
            brandList.add("WaterWall");
            brandList.add("Supreme");
            brandList.add("Astral ");
        brandList.add("CPVC");
            brandList.add("Prince");
            brandList.add("German");
            brandList.add("Curved");
            brandList.add("Ketsy");
            brandList.add("DOCOSS");
            brandList.add("SCONNA");
            brandList.add("Alton");
            brandList.add("Arise");
            brandList.add("Quick");
            brandList.add("Zesta");
            brandList.add("Hexagon");
            brandList.add("lavabo");
            brandList.add("ParryWare");


            ArrayAdapter<String> brandAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,brandList);
            activityAddProductBinding.etbrandname.setAdapter(brandAdapter);
    }

    private void UpdateProductDetails() {
        setSupportActionBar(activityAddProductBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Product Details");

        addProductImage();
        activityAddProductBinding.btnupdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected())
                {

                   if(imageUri!=null)
                   {
                       String PID=p.getProductId();
//                       Toast.makeText(AddProduct.this, "PID"+p.getProductId(), Toast.LENGTH_SHORT).show();
                       RequestBody productId= RequestBody.create(MultipartBody.FORM,PID);
//                       body =checkforImage();


                       progressDialog =new ProgressDialog(AddProduct.this);

                       progressDialog.setTitle("Uploading Image");
                       progressDialog.setMessage("Please wait while uploading product image..");
                       progressDialog.show();
                       ab=new AlertDialog.Builder(AddProduct.this);
//                       activityAddProductBinding.
                       ab.setView(R.layout.lottie_image_upload);

                       ab.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               finish();
                           }

                       });

                       ProductService.ProductApi productApiForImageUpdate=ProductService.getProductApiInstance();
                       Call<Product> call = productApiForImageUpdate.updateProductImage(body,productId);
                       call.enqueue(new Callback<Product>() {
                           @Override
                           public void onResponse(Call<Product> call, Response<Product> response) {

                               Toast.makeText(AddProduct.this, "Image Updated Successfully !", Toast.LENGTH_SHORT).show();
                               progressDialog.dismiss();
                               ab.show();
//                               Toast.makeText(AddProduct.this, "imgurl"+p.getImageUrl(), Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onFailure(Call<Product> call, Throwable t) {
                               Toast.makeText(AddProduct.this, "failed", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
                   else
                   {
                       Toast.makeText(AddProduct.this, "Select Image to Update", Toast.LENGTH_SHORT).show();
                   }



                }
                else
                {
                    Toast.makeText(AddProduct.this, "No Internet Connnection", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        Toast.makeText(this, "Jam", Toast.LENGTH_SHORT).show();

        //update product details rather than image

        activityAddProductBinding.btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected())
                {
                    //
//                    Toast.makeText(AddProduct.this, "isconnected", Toast.LENGTH_SHORT).show();

                    if(awesomeValidation.validate())
                    {

                        String PID=p.getProductId();
//                        Toast.makeText(AddProduct.this, "PID"+PID, Toast.LENGTH_SHORT).show();
                        String productname = activityAddProductBinding.etproductname.getText().toString().trim();
                        String productprice = activityAddProductBinding.etprice.getText().toString().trim();
                        String discount = activityAddProductBinding.etdiscount.getText().toString().trim();
                        String brand = activityAddProductBinding.etbrandname.getText().toString().trim();
                        String quantity= activityAddProductBinding.etquantity.getText().toString().trim();
                        String description = activityAddProductBinding.etdescription.getText().toString().trim();
                        String categoryName=activityAddProductBinding.etcategoryname.getText().toString().trim();
                        String CategoryId;
                        if(isSpinnerTouched)
                        {
                             CategoryId=categoryId;
//                            Toast.makeText(AddProduct.this, "variant catID"+CategoryId, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            CategoryId=p.getCategoryId();
//                            Toast.makeText(AddProduct.this, "old catid"+CategoryId, Toast.LENGTH_SHORT).show();
                        }
//
//                        Toast.makeText(AddProduct.this, "finalCatID"+CategoryId+categoryName, Toast.LENGTH_SHORT).show();



                        Product p=new Product();

                        p.setProductId(PID);
//                        p.setPrice(Double.parseDouble(productprice));

                        p.setPrice(Integer.parseInt(productprice));

                        p.setName(productname);
                        p.setQtyInStock(Integer.parseInt(quantity));
                        p.setDescription(description);
                        p.setBrand(brand);
                        p.setDiscount((int) Double.parseDouble(discount));
                        p.setCategoryId(CategoryId);

//                        Toast.makeText(AddProduct.this, "shId"+p.getshopkeeperId(), Toast.LENGTH_SHORT).show();

                        progressDialog =new ProgressDialog(AddProduct.this);
                        progressDialog.setTitle("Update Product info");
                        progressDialog.setMessage("Please wait while updating product info..");
                        progressDialog.show();
                        ab=new AlertDialog.Builder(AddProduct.this);
                        ab.setView(R.layout.lottie_update_product_info);
                        ab.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();


                                Intent intent=new Intent(AddProduct.this,Home.class);
                                startActivity(intent);
                            }

                        });
                        ab.setCancelable(false);

                        Dialog outtouch = ab.create();
                        outtouch.setCanceledOnTouchOutside(false);


                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.updateProduct(p);
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                    Product p=response.body();

//                                Toast.makeText(AddProduct.this, "IMP"+p.getProductId()+p.getCategoryId(), Toast.LENGTH_SHORT).show();

                                Log.e("DAMASCUS",""+p.getCategoryId()+p.getProductId()+p.getName());

                                Toast.makeText(AddProduct.this, "Product Update Successfully", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
//                                activityfinished
                                  progressDialog.dismiss();
                                 ab.show();
//                                finish();

                            }

                                @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(AddProduct.this, "Failure", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }

                    else

                    {
//                        Toast.makeText(AddProduct.this, "else of validate", Toast.LENGTH_SHORT).show();
                    }

                }

                else
                {
                    Toast.makeText(AddProduct.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    private void initComponents() {
        Log.e("TAG","3 images");
        Picasso.get().load(p.getImageUrl()).into(activityAddProductBinding.iv1);
        Picasso.get().load(p.getSecondImageUrl()).into(activityAddProductBinding.iv2);
        Picasso.get().load(p.getThirdImageurl()).into(activityAddProductBinding.iv3);
        activityAddProductBinding.etproductname.setText(p.getName());
        activityAddProductBinding.etprice.setText("" + (int)p.getPrice());
        activityAddProductBinding.etquantity.setText("" + p.getQtyInStock());
        activityAddProductBinding.etbrandname.setText(p.getBrand());
        activityAddProductBinding.etdescription.setText(p.getDescription());
        activityAddProductBinding.etdiscount.setText("" +(int) p.getDiscount());



    }


    private void getCategoryList() {
        SharedPreferences sharedPreferences =getSharedPreferences("categories",MODE_PRIVATE );
        Gson gson =new Gson();
        String json =sharedPreferences.getString("jsoncategorylist",null);
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        categoryList = gson.fromJson(json,type);


        if(categoryList==null)
        {
//            Toast.makeText(this, "list null", Toast.LENGTH_SHORT).show();
        }
        else
        {
          /*  for(Category c: categoryList)
            {
                Toast.makeText(this, "cccc"+c.getCategoryName(), Toast.LENGTH_SHORT).show();
            }*/
//            categoryId = categorySpinner();
            categorySpinner();
        }

    }

//        call spinner to fetch category




    private void addProductImage() {

        activityAddProductBinding.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AddProduct.this, "Adding first product image", Toast.LENGTH_SHORT).show();
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select image"),111);
            }
        });
        activityAddProductBinding.iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select image"),112);
            }
        });
        activityAddProductBinding.iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select image"),113);
            }
        });


    }

    private void validatingField() {

        awesomeValidation.addValidation(activityAddProductBinding.etproductname,"[^\\s*$][a-zA-Z\\s]+","Enter correct product name");
        awesomeValidation.addValidation(activityAddProductBinding.etbrandname,"[^\\s*$][a-zA-Z\\s]+","Enter correct Brand name");

        /*awesomeValidation.addValidation(activityAddProductBinding.etdiscount,"[^\\\\s*$][0-9\\\\s]+","Enter discount");
        awesomeValidation.addValidation(activityAddProductBinding.etdescription,"[^\\s*$][a-zA-Z0-9,/\\s]+","Enter correct description");
        */
        awesomeValidation.addValidation(activityAddProductBinding.etprice,"[^\\\\s*$][0-9\\\\s]+","Enter price");
        awesomeValidation.addValidation(activityAddProductBinding.etquantity,"[^\\s*$][0-9]*$","Enter quantitiy");
        //^[0-9]*$
        //empty ^$


        //non empty ==> [^\s]*

    }

    /**/private void addProduct() {

        setSupportActionBar(activityAddProductBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Product");

        activityAddProductBinding.btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected())
                {

                    if(awesomeValidation.validate())
                    {
                        String productname = activityAddProductBinding.etproductname.getText().toString().trim();
                        String productprice = activityAddProductBinding.etprice.getText().toString().trim();
                        String discount = activityAddProductBinding.etdiscount.getText().toString().trim();
                        String brand = activityAddProductBinding.etbrandname.getText().toString().trim();
                        String quantity= activityAddProductBinding.etquantity.getText().toString().trim();
                        String description = activityAddProductBinding.etdescription.getText().toString().trim();



                        String shopKeeperId = sp.getString("userId","");
                        //let this variable be capital K
//                        Toast.makeText(AddProduct.this, "shopkeeperId;;>"+shopKeeperId, Toast.LENGTH_SHORT).show();


                        //Toast.makeText(AddProduct.this, "categoryIdDD"+categoryId, Toast.LENGTH_SHORT).show();
                        String CatId=categoryId;
                        Log.e("SSCatId==>val",""+CatId);


//                        commented code for  uploading imagge
                        if(imageUri != null && secondImageUri != null && thirdImageuri != null)
                        {
//                            Toast.makeText(AddProduct.this, "SSSSSSSSSSSSS", Toast.LENGTH_SHORT).show();

                            if(imageUri != null && secondImageUri != null && thirdImageuri != null) {

                                File file = FileUtils.getFile(AddProduct.this, imageUri);
                                RequestBody requestFile =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                                file
                                        );
                                body =
                                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);


                                File file2 = FileUtils.getFile(AddProduct.this, secondImageUri);
                                RequestBody requestFile2 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(secondImageUri))),
                                                file2
                                        );
                                body2 =
                                        MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);

                                File file3 = FileUtils.getFile(AddProduct.this, thirdImageuri);
                                RequestBody requestFile3 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(thirdImageuri))),
                                                file3
                                        );
                                body3 =
                                        MultipartBody.Part.createFormData("file3", file3.getName(), requestFile3);

                                RequestBody productName = RequestBody.create(MultipartBody.FORM, productname);
                                RequestBody price = RequestBody.create(MultipartBody.FORM, productprice);
                                RequestBody Discount = RequestBody.create(MultipartBody.FORM, discount);
                                RequestBody Brand = RequestBody.create(MultipartBody.FORM, brand);
                                RequestBody Quantity = RequestBody.create(MultipartBody.FORM, quantity);
                                RequestBody Description = RequestBody.create(MultipartBody.FORM, description);
                                RequestBody shopkeeperId = RequestBody.create(MultipartBody.FORM, shopKeeperId);
//                                Toast.makeText(AddProduct.this, "shopkeeperId22>" + shopkeeperId, Toast.LENGTH_SHORT).show();

                                RequestBody categoryId = RequestBody.create(MultipartBody.FORM, CatId);

//                                Toast.makeText(AddProduct.this, "*****" + CatId + shopKeeperId, Toast.LENGTH_SHORT).show();

                                //next to implement
                                progressDialog = new ProgressDialog(AddProduct.this);
                                progressDialog.setTitle("Adding Product");
                                progressDialog.setMessage("Please wait while adding product..");
                                progressDialog.show();
                                ab = new AlertDialog.Builder(AddProduct.this);
                                ab.setView(R.layout.successbg);
                                ab.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }

                                });
                                ab.setCancelable(false);

                                Dialog outtouch = ab.create();
                                outtouch.setCanceledOnTouchOutside(false);

                                ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                                //commenting
                                Call<Product> call = productApi.saveProduct(body, body2, body3, categoryId, shopkeeperId, productName, price, Discount, Brand, Quantity, Description);

                                call.enqueue(new Callback<Product>() {
                                    @Override
                                    public void onResponse(Call<Product> call, Response<Product> response) {

                                        Product p = response.body();
//                                        Toast.makeText(AddProduct.this, "Product added successfully !", Toast.LENGTH_SHORT).show();
//                                    Log.e("PRODUCT",""+p.getProductId()+"  "+p.getBrand());

                                        //make alert dia here
                                        progressDialog.dismiss();

                                        ab.show();

                                    }

                                    @Override
                                    public void onFailure(Call<Product> call, Throwable t) {
                                        Toast.makeText(AddProduct.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                                        Log.e("THROWED", "" + t);
                                    }
                                });


                            }
                        }
                        else
                        {
                            Toast.makeText(AddProduct.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else
                    {
                        Toast.makeText(AddProduct.this, "Invalid input(s)", Toast.LENGTH_SHORT).show();
                    }




                }

                else
                {
                    Toast.makeText(AddProduct.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                    Log.e("INTERNET NOT CONNECTED","Internet Issue");
                }
            }
        });

    }



/*
    private MultipartBody.Part checkforImage() {


        File file =  FileUtils.getFile(AddProduct.this,imageUri);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Log.e("BODY",""+body);
        return body;

    }
*/

    private void categorySpinner() {

         adapter = new ArrayAdapter<Category>(AddProduct.this,android.R.layout.simple_spinner_dropdown_item,categoryList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setPrompt("Select Category!");

        activityAddProductBinding.spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        activityAddProductBinding.spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouched =true;
                return false;
            }
        });

        activityAddProductBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String text = adapterView.getItemAtPosition(position).toString();

                //callhere


                if(categoryId==null)
                {
                    Category c= (Category) activityAddProductBinding.spinner.getSelectedItem();
                    categoryId=c.getCategoryId();
//                    Toast.makeText(AddProduct.this, "categoryId__ifnull"+categoryId, Toast.LENGTH_SHORT).show();
                }

                if(!isSpinnerTouched)//imp method
                    return;

                Category c= (Category) activityAddProductBinding.spinner.getSelectedItem();
                String name =c.getCategoryName();
//                Toast.makeText(AddProduct.this, ""+name, Toast.LENGTH_SHORT).show();
                categoryId=c.getCategoryId();
                activityAddProductBinding.etcategoryname.setText(name);

//                pos=adapter.getItem(position).toString();

//                Toast.makeText(AddProduct.this, "showing pos after click"+pos, Toast.LENGTH_SHORT).show();
//                Toast.makeText(AddProduct.this, "nxt name+cat"+categoryId+name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

//                Toast.makeText(AddProduct.this, "Nothing selected in spinner !", Toast.LENGTH_SHORT).show();
            }
        });

//        return categoryId;
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(activityAddProductBinding.iv1);
//            Toast.makeText(this, ""+imageUri, Toast.LENGTH_SHORT).show();

        }
        if (requestCode == 112 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            secondImageUri = data.getData();
            Picasso.get().load(secondImageUri).into(activityAddProductBinding.iv2);
//            Toast.makeText(this, ""+secondImageUri, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 113 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            thirdImageuri = data.getData();
            Picasso.get().load(thirdImageuri).into(activityAddProductBinding.iv3);
//            Toast.makeText(this, ""+thirdImageuri, Toast.LENGTH_SHORT).show();
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
