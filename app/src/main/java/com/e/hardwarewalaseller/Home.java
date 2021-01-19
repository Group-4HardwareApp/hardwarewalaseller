package com.e.hardwarewalaseller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.hardwarewalaseller.adapter.CategoryAdapter;
import com.e.hardwarewalaseller.adapter.ProductAdapter;
import com.e.hardwarewalaseller.apis.CategoryService;
import com.e.hardwarewalaseller.apis.ProductService;
import com.e.hardwarewalaseller.apis.ShopkeeperService;
import com.e.hardwarewalaseller.beans.Category;
import com.e.hardwarewalaseller.beans.Product;
import com.e.hardwarewalaseller.beans.Shopkeeper;
import com.e.hardwarewalaseller.databinding.ActivityHomeBinding;
import com.e.hardwarewalaseller.databinding.CategoryItemListBinding;
import com.e.hardwarewalaseller.databinding.NavigationHeaderBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.jackandphantom.circularimageview.CircleImage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    //    NavigationHeaderBinding navigationHeaderBinding;
    ActivityHomeBinding activityHomeBinding;
    ActionBarDrawerToggle toggle;
    String inputQuery;
    FirebaseUser currentUser;
    //hardcoded

    FirebaseAuth mAuth;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    SharedPreferences sp;
    SearchView searchView;
    String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String TOKENDISP = FirebaseInstanceId.getInstance().getToken();
//        Toast.makeText(this, "" + TOKENDISP, Toast.LENGTH_SHORT).show();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        Toast.makeText(this, "currentUser from FB"+currentUser, Toast.LENGTH_SHORT).show();
        Log.e("currentUser==>", "from firebase" + currentUser);
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        activityHomeBinding = ActivityHomeBinding.inflate(inflater);
        View v = activityHomeBinding.getRoot();
        setContentView(v);
        Log.e("TAG", "TOKEN" + TOKENDISP);

        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(activityHomeBinding.toolbar);
        getNavigationDrawer();
        getCategories();

        voicesearch();
        search();

    }

    private void search() {

        activityHomeBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHomeBinding.tvAppName.setVisibility(View.GONE);
                activityHomeBinding.etSearch.setVisibility(View.VISIBLE);
                activityHomeBinding.ivcross.setVisibility(View.VISIBLE);
                activityHomeBinding.etSearch.requestFocus();
                activityHomeBinding.etSearch.setFocusableInTouchMode(true);
                //check below
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(activityHomeBinding.etSearch, InputMethodManager.SHOW_FORCED);

            }
        });
        activityHomeBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString().trim();
                activityHomeBinding.categories.setVisibility(View.GONE);
                activityHomeBinding.rvcategory.setVisibility(View.GONE);

                activityHomeBinding.searchresult.setVisibility(View.VISIBLE);
                activityHomeBinding.rvproduct.setVisibility(View.VISIBLE);

                searchProduct();
                if(name.isEmpty()){


                    activityHomeBinding.ivcross.setVisibility(View.GONE);
                    activityHomeBinding.rvcategory.setVisibility(View.VISIBLE);

                    activityHomeBinding.searchresult.setVisibility(View.GONE);
                    activityHomeBinding.rvproduct.setVisibility(View.GONE);

                    activityHomeBinding.etSearch.setVisibility(View.GONE);
                    activityHomeBinding.tvAppName.setVisibility(View.VISIBLE);

                    //if name is empty
                    closeKeyboard();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        activityHomeBinding.ivcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Home.this, ""+name, Toast.LENGTH_SHORT).show();
                activityHomeBinding.etSearch.setText(" ");
                activityHomeBinding.ivcross.setVisibility(View.GONE);
                closeKeyboard();
            }
        });



    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view !=null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            //check below


        }
    }


    private void searchProduct() {


        if (isConnected()) {
            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
            //check here below Id
            String shopkeeperId = sp.getString("userId", "");
//            Toast.makeText(Home.this, "" + shopkeeperId, Toast.LENGTH_SHORT).show();
            Call<ArrayList<Product>> call = productApi.viewProductOfShopkeeper(name, shopkeeperId);
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if(response.code()==200)
                    {
                        ArrayList<Product> productList = response.body();
                        productAdapter = new ProductAdapter(Home.this, productList);

//                            searchView.setFocusable(false);

                        if (productList == null) {
//                                Toast.makeText(Home.this, "WASP", Toast.LENGTH_SHORT).show();
                            Log.e("SHowing Null", "SSSS");
                        }
                        activityHomeBinding.rvproduct.setAdapter(productAdapter);
                        activityHomeBinding.searchresult.setVisibility(View.VISIBLE);
                        activityHomeBinding.rvproduct.setLayoutManager(new GridLayoutManager(Home.this, 2));
                        productAdapter.notifyDataSetChanged();
                        productAdapter.setOnItemClickListener(new ProductAdapter.onRecyclerViewClick() {
                            @Override
                            public void onItemClick(Product p, int position) {
                                Intent in = new Intent(Home.this, ViewProduct.class);
                                in.putExtra("product", p);
                                startActivity(in);
                            }
                        });
//                            Toast.makeText(Home.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }




    }

    private void voicesearch() {

        activityHomeBinding.ivmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());

                if(intent.resolveActivity(getPackageManager())!=null)
                {
//                    Toast.makeText(Home.this, "in if of voice !!", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent,10);
                }
                else
                {
                    Toast.makeText(Home.this, "Your device Doesn't support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this, "on activity ", Toast.LENGTH_SHORT).show();
        switch (requestCode)
        {
            case 10:
                if(resultCode==RESULT_OK && data!=null)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    Toast.makeText(this, ""+result.get(0), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "mutch", Toast.LENGTH_SHORT).show();
                    Log.e("TAG",""+result.get(0));
                    activityHomeBinding.etSearch.setText(result.get(0).trim());
//                    activityHomeBinding.ivcross.setVisibility(View.VISIBLE);
//                    activityHomeBinding.tvAppName.setVisibility(View.GONE);
//                    Log.e("TAG",""+result);
                }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserProfile();
    }

    private void checkUserProfile() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = sp.getString("userId", "Not found");
        Log.e("TAG", "id===>" + id);
        if (!id.equals("Not found")) {
            Log.e("TAG", "IN__IF_IDS" + id + currentUserId);
//            Toast.makeText(this, "in 1st if", Toast.LENGTH_SHORT).show();
            if (!id.equals(currentUserId)) {
//                Toast.makeText(this, "" + currentUserId + "____" + id, Toast.LENGTH_SHORT).show();
                ShopkeeperService.ShopkeeperApi shopkeeperApi = ShopkeeperService.getShopkeeperApiInstance();
                Call<Shopkeeper> call = shopkeeperApi.viewShopkeeper(currentUserId);
                call.enqueue(new Callback<Shopkeeper>() {
                    @Override
                    public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
//                            Toast.makeText(Home.this, "response !!", Toast.LENGTH_SHORT).show();
                        if (response.code() == 200) {
//                            Toast.makeText(Home.this, "200!", Toast.LENGTH_SHORT).show();
                            saveUserProfile(response.body());

                        } else if (response.code() == 404) {
//                            Toast.makeText(Home.this, "400!", Toast.LENGTH_SHORT).show();
                            sendUserToProfileActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<Shopkeeper> call, Throwable t) {
                        Toast.makeText(Home.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                    }
                });

                // Call api to check user profile exist or not
                // if exist then update data in shared preferences and user will stay on homeactivity
                // if not then send user to create profile activity
            }
        } else {
            Log.e("TAG", "IN__ELSE_IDS" + id + currentUserId);
//            Toast.makeText(this, "BLANK", Toast.LENGTH_SHORT).show();
            ShopkeeperService.ShopkeeperApi shopkeeperApi = ShopkeeperService.getShopkeeperApiInstance();
            Call<Shopkeeper> call = shopkeeperApi.viewShopkeeper(currentUserId);
            call.enqueue(new Callback<Shopkeeper>() {
                @Override
                public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
//                            saveUserProfile(response.body());
                    Toast.makeText(Home.this, "response !!" + response.code(), Toast.LENGTH_SHORT).show();
                    if (response.code() == 200) {
                        Toast.makeText(Home.this, "200!", Toast.LENGTH_SHORT).show();

                        saveUserProfile(response.body());

                    } else if (response.code() == 404) {
//                        Toast.makeText(Home.this, "404!", Toast.LENGTH_SHORT).show();
                        sendUserToProfileActivity();
                    }
                }

                @Override
                public void onFailure(Call<Shopkeeper> call, Throwable t) {
                    Toast.makeText(Home.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                }
            });
        }
        navHeader();
    }

    public void sendUserToProfileActivity() {
        Intent in = new Intent(Home.this, AddStore.class);
        startActivity(in);
    }

    private void sendUserToLoginActivity() {
        Intent in = new Intent(Home.this, LogInActivity.class);
        startActivity(in);
        finish();
    }

    public void saveUserProfile(Shopkeeper shopkeeper) {
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
    }

    private void getCategories() {
        if (isConnected()) {
            CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
            Call<ArrayList<Category>> call = categoryApi.getCategoryList();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    ArrayList<Category> categoryList = response.body();
                    //sharedpref
                    SharedPreferences sharedPreferences = getSharedPreferences("categories", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(categoryList);
                    editor.putString("jsoncategorylist", json);
                    editor.apply();
                    editor.commit();
                    categoryAdapter = new CategoryAdapter(Home.this, categoryList);
                    activityHomeBinding.rvcategory.setAdapter(categoryAdapter);
                    activityHomeBinding.rvcategory.setLayoutManager(new GridLayoutManager(Home.this, 2));
//                    Toast.makeText(Home.this, "CATEGORY SUcccess ", Toast.LENGTH_SHORT).show();
                    //loading category
                    categoryAdapter.notifyDataSetChanged();
                    categoryAdapter.setOnItemClickListener(new CategoryAdapter.onRecyclerViewClick() {
                        @Override
                        public void onItemClick(Category c, int position) {
//                            Toast.makeText(Home.this, "Send Intent or send product", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(Home.this, ViewProductByCategory.class);
//                        in.putExtra("categoryId",c.getCategoryId());
                            in.putExtra("category", c);
                            startActivity(in);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Toast.makeText(Home.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                }
            });

            activityHomeBinding.addproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(Home.this, "Add Product !", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Home.this, AddProduct.class);
//                            in.putExtra("categoryList",categoryList);
                    startActivity(in);
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(this, activityHomeBinding.drawer, activityHomeBinding.toolbar, R.string.open, R.string.close);
//        activityHomeBinding.drawer.addDrawerListener(toggle);
        activityHomeBinding.drawer.addDrawerListener(toggle);
        toggle.syncState();
//        activityHomeBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        activityHomeBinding.navigationview.setItemIconTintList(null);
        activityHomeBinding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.updatestore) {
//                    Toast.makeText(Home.this, "Update Store clicked !", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Home.this, UpdateStore.class);
                    startActivity(in);
                } else if (id == R.id.stats) {
//                    Toast.makeText(Home.this, "Statistics !", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Home.this, Summary.class);
                    startActivity(in);
                } else if (id == R.id.orderhistory) {
//                    Toast.makeText(Home.this, "Order History !", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Home.this, PackageHistory.class);
                    in.putExtra("status", "history");
                    startActivity(in);
                } else if (id == R.id.manageorders) {
//                    Toast.makeText(Home.this, "Manage Order !", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Home.this, PackageHistory.class);
                    in.putExtra("status", "current");
                    startActivity(in);
                } else if (id == R.id.signout) {
//                    Toast.makeText(Home.this, "Sign Out", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder ab = new AlertDialog.Builder(Home.this);
                    ab.setMessage("Do you want to logout ?");
                    ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = new ProgressDialog(Home.this);
                            pd.setTitle("Please wait...");
                            pd.show();
                           /* SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();*/
                            mAuth.signOut();
                            pd.dismiss();
                            navigateUserToLoginActivity();
                        }
                    });
                    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ab.show();
                }

                activityHomeBinding.drawer.closeDrawers();
                return false;
            }
        });
    }

    private void navHeader() {

        View headerLayout = activityHomeBinding.navigationview.getHeaderView(0);
        CircularImageView userImage = headerLayout.findViewById(R.id.civdp);

        TextView tvUsername = headerLayout.findViewById(R.id.username);
        TextView shopkeeperId = headerLayout.findViewById(R.id.shopkeeperId);

        Picasso.get().load(sp.getString("imageUrl", "imageurl here")).into(userImage);
        tvUsername.setText(sp.getString("name", "name here"));
        shopkeeperId.setText(sp.getString("userId", "id here"));
    }












//applying new search
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater1 = getMenuInflater();
        inflater1.inflate(R.menu.toolbar_menu_itemlist, menu);



        MenuItem home =menu.findItem(R.id.voice);

        // new code

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        ComponentName cn = new ComponentName(this, Home.class);


        searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setIconifiedByDefault(true);
//        searchView.setBackgroundColor(getResources().getColor(R.color.offWhite));
//        searchView.getSolidColor();

//        searchView.setBackground(getDrawable(R.drawable.search`viewbg));
//        getResources().getColor(R.color.colorYellow)

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    MenuItemCompat.collapseActionView(searchItem);
                }
            }


        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText( Home.this, "ZIP"+query, Toast.LENGTH_SHORT).show();
                inputQuery = query;

                if (isConnected()) {
                    ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                    //check here below Id
                    String shopkeeperId = sp.getString("userId", "");
                    Toast.makeText(Home.this, ""+shopkeeperId, Toast.LENGTH_SHORT).show();
//                    String shopkeeperId ="jnwnfhgjhdjjhjhd";//uncomment upperLine in future
                    Call<ArrayList<Product>> call = productApi.viewProductOfShopkeeper(inputQuery, shopkeeperId);
                    call.enqueue(new Callback<ArrayList<Product>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                            ArrayList<Product> productList = response.body();
                            productAdapter = new ProductAdapter(Home.this, productList);

//                            searchView.setFocusable(false);

                            if (productList == null) {
//                                Toast.makeText(Home.this, "WASP", Toast.LENGTH_SHORT).show();
                                Log.e("SHowing Null", "SSSS");
                            }
                            activityHomeBinding.rvproduct.setAdapter(productAdapter);
                            activityHomeBinding.searchresult.setVisibility(View.VISIBLE);
//                            Toast.makeText(Home.this, "Product adapter set", Toast.LENGTH_SHORT).show();
                            activityHomeBinding.rvproduct.setLayoutManager(new GridLayoutManager(Home.this, 2));
                            productAdapter.notifyDataSetChanged();
                            productAdapter.setOnItemClickListener(new ProductAdapter.onRecyclerViewClick() {
                                @Override
                                public void onItemClick(Product p, int position) {
//                                    Toast.makeText(Home.this, "Send Intent or send product", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(Home.this, ViewProduct.class);
                                    in.putExtra("product", p);
                                    startActivity(in);
                                }
                            });
//                            Toast.makeText(Home.this, "Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                            Toast.makeText(Home.this, "Bad response from server", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
*//*
                searchView.clearFocus(); // close the keyboard on load
*//*
//                searchView.setQuery("",false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
*/
    private void navigateUserToLoginActivity() {
        Intent in = new Intent(Home.this, LogInActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
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
