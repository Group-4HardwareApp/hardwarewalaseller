package com.e.hardwarewalaseller.apis;

import com.e.hardwarewalaseller.beans.Product;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ProductService {

    public static ProductApi productApi;

    public static ProductApi getProductApiInstance()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(productApi==null) {
            productApi = retrofit.create(ProductApi.class);
        }
        return productApi;


    }

    public interface ProductApi
    {
        @GET("product/shopkeeperproducts/{name}/{shopkeeperId}")
        public Call<ArrayList<Product>> viewProductOfShopkeeper(@Path("name") String name, @Path("shopkeeperId") String shopkeeperId);

        @GET("product/productlist/{categoryId}/{shopkeeperId}")
        public Call<ArrayList<Product>> getProductByCategoryAndShopKeeper(@Path("categoryId") String categoryId,@Path("shopkeeperId") String shopkeeperId);


        @DELETE("product/{id}")
        public Call<Product> deleteProduct(@Path("id") String id);

        @Multipart
        @POST("product/save")
        public Call<Product> saveProduct(@Part MultipartBody.Part file,
                                         @Part  MultipartBody.Part file2,
                                         @Part  MultipartBody.Part file3,
                                         @Part("categoryId") RequestBody categoryId,
                                         @Part("shopkeeperId") RequestBody shopkeeperId,
                                         @Part("name") RequestBody name,
                                         @Part("price") RequestBody price,
                                         @Part("discount") RequestBody discount,
                                         @Part("brand") RequestBody brand,
                                         @Part("qtyInStock") RequestBody qtyInStock,
                                         @Part("description") RequestBody description);




        @POST("product/updateproduct")
         public  Call<Product> updateProduct(@Body Product product);

        @Multipart
        @POST("product/updateproductimg")
        public Call<Product> updateProductImage(@Part MultipartBody.Part file,@Part("productId") RequestBody productId);

    }



}
