package com.e.hardwarewalaseller;

import android.Manifest;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.e.hardwarewalaseller.apis.ProductNameService;
import com.e.hardwarewalaseller.apis.ProductService;
import com.e.hardwarewalaseller.beans.Category;
import com.e.hardwarewalaseller.beans.Product;
import com.e.hardwarewalaseller.beans.ProductName;
import com.e.hardwarewalaseller.databinding.ActivityAddProductBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class AddProduct extends AppCompatActivity {

    Product p = new Product();
    public static ArrayList<Category> categoryList;
    String categoryId;
    Uri imageUri=null;
    Uri secondImageUri=null;
    Uri thirdImageuri=null;
    AwesomeValidation awesomeValidation;
    MultipartBody.Part body,body2,body3;
    ActivityAddProductBinding activityAddProductBinding;
    boolean isUpdate = false;
    ArrayAdapter<Category> adapter;
    boolean isSpinnerTouched=false;
    ProgressDialog progressDialog;
    AlertDialog.Builder ab;
    SharedPreferences sp ;
    boolean flag;
    List<MultipartBody.Part> bodyList=new ArrayList<>();
//    HashMap<Integer,MultipartBody.Part> bodyMap = new HashMap<>();
    int counter[]=new int[3];

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
        sp = getSharedPreferences("user",MODE_PRIVATE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }

        int initialposition = activityAddProductBinding.spinner.getSelectedItemPosition();
        activityAddProductBinding.spinner.setSelection(initialposition,false);

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
            UpdateProductDetails();
        }
        else
        {

            addProduct();
            addProductImage();

        }

    }

    public void addProductName(String id) {

        String category_Id = id;
        if(category_Id==null)
        {

//            category_Id="5UCzKgjSkYItfDHmp7f9";
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
        }

          ArrayList<String> productNameList=new ArrayList<>();
            ProductNameService.ProductNameApi productNameApi = ProductNameService.getProductNameApiInstance();
            Call<ArrayList<ProductName>> call= productNameApi.getProductNameListByCategory(category_Id);
            call.enqueue(new Callback<ArrayList<ProductName>>() {
                @Override
                public void onResponse(Call<ArrayList<ProductName>> call, Response<ArrayList<ProductName>> response) {
                    if(response.code()==200) {
                        ArrayList<ProductName> productNameArrayList = response.body();

                        if(productNameArrayList==null)
                        {
                            Log.e("TAG","NULL for categoryID");
                        }
                        for (ProductName p : productNameArrayList) {
//                        System.out.println(p.getProductName());
                            Log.e("TAG", "" + p.getProductName());
                            String pn = p.getProductName();
                            productNameList.add(pn);
//                        Toast.makeText(AddProduct.this, "toast"+pn, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(AddProduct.this, "200 failure", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter<String> productNameAdapter= new ArrayAdapter<String>(AddProduct.this, android.R.layout.simple_list_item_1,productNameList);
                    activityAddProductBinding.etproductname.setAdapter(productNameAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<ProductName>> call, Throwable t) {
                    Toast.makeText(AddProduct.this, "api failure....", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


    }

    private void addBrandName() {

        ArrayList<String> brandList = new ArrayList<>();
        brandList.add("Other");
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


        activityAddProductBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AddProduct.this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                AddProduct.super.onBackPressed();
                            }
                        }).create().show();            }
        });

        addProductImage();

        activityAddProductBinding.btnupdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected())
                {
                    bodyList.clear();
                    String PID = p.getProductId();
                    RequestBody productId = RequestBody.create(MultipartBody.FORM, PID);
                        if(imageUri != null) {
                            flag=true;
                            File file = FileUtils.getFile(AddProduct.this, imageUri);
                            RequestBody requestFile =
                                    RequestBody.create(
                                            MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                            file);
                            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                            bodyList.add( MultipartBody.Part.createFormData("file", file.getName(), requestFile));
                            counter[0]=1;
                        }
                        else
                        {
                            bodyList.add(null);
                        }

                            if(secondImageUri != null ) {
                                flag=true;
                                File file2 = FileUtils.getFile(AddProduct.this, secondImageUri);
                                RequestBody requestFile2 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(secondImageUri))),
                                                file2);

                                bodyList.add( MultipartBody.Part.createFormData("file", file2.getName(), requestFile2));
                                body2= MultipartBody.Part.createFormData("file", file2.getName(), requestFile2);
                                counter[1]=2;
                            }
                            else
                            {
                                bodyList.add(null);
                            }

                                if (thirdImageuri != null) {
                                    flag=true;
                                    File file3 = FileUtils.getFile(AddProduct.this, thirdImageuri);
                                    RequestBody requestFile3 =
                                            RequestBody.create(
                                                    MediaType.parse(Objects.requireNonNull(getContentResolver().getType(thirdImageuri))),
                                                    file3);
                                    bodyList.add( MultipartBody.Part.createFormData("file", file3.getName(), requestFile3));

                                    Log.e("SS",""+MultipartBody.Part.createFormData("file", file3.getName(), requestFile3));
//                                    body3= MultipartBody.Part.createFormData("file", file3.getName(), requestFile3);
                                    counter[2]=3;
                                    }
                                else
                                {
                                    bodyList.add(null);
                                }


                        if(flag)
                        {
                                    progressDialog =new ProgressDialog(AddProduct.this);
                                    progressDialog.setTitle("Uploading Image");
                                    progressDialog.setMessage("Please wait while uploading product image..");
                                    progressDialog.show();

                                    ab=new AlertDialog.Builder(AddProduct.this);

                                    ab.setView(R.layout.lottie_image_upload);

                                    ab.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }

                                    });
                                    Log.e("TAG for update","1"+body+"\n2"+body2+"\n3"+body3);

                                    Log.e("SIZE",""+bodyList.size());
//                                    Log.e("DATA",""+bodyList.get(0)+bodyList.get(1)+bodyList.get(2));
                            ProductService.ProductApi productApiForImageUpdate = ProductService.getProductApiInstance();
                            Call<Product> call = productApiForImageUpdate.updateProductImage(bodyList, productId,counter);
                            call.enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                    if(response.code()==200) {
                                        progressDialog.dismiss();
                                    Toast.makeText(AddProduct.this, "Image Updated Successfully !", Toast.LENGTH_SHORT).show();
                                    ab.show();}
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Toast.makeText(AddProduct.this, "failed", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace();
                                    progressDialog.dismiss();
                                }

                            });
                        }
                        else
                        {
                            Toast.makeText(AddProduct.this, "Please select image to upload", Toast.LENGTH_SHORT).show();
                        }
}
                else
                {
                    Toast.makeText(AddProduct.this, "No Internet Connnection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        activityAddProductBinding.btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected())
                {
                    if(awesomeValidation.validate())
                    {

                        String PID=p.getProductId();
//                        Toast.makeText(AddProduct.this, "PID"+PID, Toast.LENGTH_SHORT).show();
                        String productname = activityAddProductBinding.etproductname.getText().toString().trim();
                        String productprice = activityAddProductBinding.etprice.getText().toString().trim();

                        String  discount = activityAddProductBinding.etdiscount.getText().toString().trim();
                        String brand = activityAddProductBinding.etbrandname.getText().toString().trim();
                        String quantity= activityAddProductBinding.etquantity.getText().toString().trim();
                        String description = activityAddProductBinding.etdescription.getText().toString().trim();
                        String categoryName=activityAddProductBinding.etcategoryname.getText().toString().trim();
                        String CategoryId;
                        if(isSpinnerTouched)
                        {
                            CategoryId=categoryId;
                            Toast.makeText(AddProduct.this, "variant catID"+CategoryId, Toast.LENGTH_SHORT).show();
                            Log.e("CATEGORYspintouch",""+CategoryId);
                        }
                        else
                        {
                            CategoryId=p.getCategoryId();
                            Toast.makeText(AddProduct.this, "old catid"+CategoryId, Toast.LENGTH_SHORT).show();
                            Log.e("CATEGORY","not spin"+CategoryId);
                        }

//                        Toast.makeText(AddProduct.this, "finalCatID"+CategoryId+categoryName, Toast.LENGTH_SHORT).show();
                        Log.e("CATEGORY",""+CategoryId);

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


        activityAddProductBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AddProduct.this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                AddProduct.super.onBackPressed();
                            }
                        }).create().show();
            }
        });


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
                        bodyList.clear();
                        ArrayList<MultipartBody.Part> fileList = new ArrayList<>();
                        if(imageUri != null)
                        {
                            File file = FileUtils.getFile(AddProduct.this, imageUri);
                            RequestBody requestFile =
                                    RequestBody.create(
                                            MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                            file
                                    );
                            bodyList.add(MultipartBody.Part.createFormData("file", file.getName(), requestFile));
                            Log.e("",""+bodyList.get(0));

                         if(secondImageUri != null ) {
                                Log.e("TAG", "secondImageUri" + secondImageUri);
                                File file2 = FileUtils.getFile(AddProduct.this, secondImageUri);
                                RequestBody requestFile2 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(secondImageUri))),
                                                file2);

                                bodyList.add(MultipartBody.Part.createFormData("file", file2.getName(), requestFile2));
                         }
                         if (thirdImageuri != null) {
                                    File file3 = FileUtils.getFile(AddProduct.this, thirdImageuri);
                                    RequestBody requestFile3 =
                                            RequestBody.create(
                                                    MediaType.parse(Objects.requireNonNull(getContentResolver().getType(thirdImageuri))),
                                                    file3);
                                    bodyList.add( MultipartBody.Part.createFormData("file", file3.getName(), requestFile3));
                          }


                            RequestBody productName = RequestBody.create(MultipartBody.FORM, productname);
                            RequestBody price = RequestBody.create(MultipartBody.FORM, productprice);
                            RequestBody Discount = RequestBody.create(MultipartBody.FORM, discount);
                            RequestBody Brand = RequestBody.create(MultipartBody.FORM, brand);
                            RequestBody Quantity = RequestBody.create(MultipartBody.FORM, quantity);
                            RequestBody Description = RequestBody.create(MultipartBody.FORM, description);
                            RequestBody shopkeeperId = RequestBody.create(MultipartBody.FORM, shopKeeperId);
                            RequestBody categoryId = RequestBody.create(MultipartBody.FORM, CatId);
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
                            Log.e("TAGS","CATID"+categoryId+"SHOP"+shopkeeperId);
                            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                            Call<Product> call = productApi.multProductImages(bodyList, categoryId, shopkeeperId, productName,price, Discount, Brand, Quantity, Description);
                            call.enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                if(response.code()==200) {
                                    Product p = response.body();
                                    progressDialog.dismiss();
                                    ab.show();
                                }
                                 else{
                                    Toast.makeText(AddProduct.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                }

                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Toast.makeText(AddProduct.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                                    Log.e("THROWED", "" + t);
                                }
                            });
                    }
                        else
                        {
                            Toast.makeText(AddProduct.this, "Please upload image(s)", Toast.LENGTH_SHORT).show();
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

                String id;
                if(categoryId==null)
                {
                    Category c= (Category) activityAddProductBinding.spinner.getSelectedItem();
                    categoryId=c.getCategoryId();
                    id=c.getCategoryId();
                    addProductName(id);
//                    Toast.makeText(AddProduct.this, "categoryId__ifnull"+categoryId, Toast.LENGTH_SHORT).show();
                }

                if(!isSpinnerTouched)//imp method
                    return;

                Category c= (Category) activityAddProductBinding.spinner.getSelectedItem();
                String name =c.getCategoryName();
                id=c.getCategoryId();
//                Toast.makeText(AddProduct.this, ""+name, Toast.LENGTH_SHORT).show();
                categoryId=c.getCategoryId();
                activityAddProductBinding.etcategoryname.setText(name);
                Toast.makeText(AddProduct.this, "variant catID"+categoryId+"\n"+name, Toast.LENGTH_SHORT).show();

                //check for category to prodcut name
//
                addProductName(id);

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
