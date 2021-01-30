package com.e.hardwarewalaseller.apis;

import com.e.hardwarewalaseller.beans.ProductName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ProductNameService {

    public  static  ProductNameApi productNameApi;

    public static ProductNameApi getProductNameApiInstance()
    {
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(ServerAddress.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

        if(productNameApi==null)
        {
            productNameApi = retrofit.create(ProductNameApi.class);
        }
        return productNameApi;
    }


    public interface ProductNameApi
    {


        @GET("productname/productnamelist/{categoryId}")
        public Call<ArrayList<ProductName>> getProductNameListByCategory(@Path("categoryId") String categoryId);


    }

}
