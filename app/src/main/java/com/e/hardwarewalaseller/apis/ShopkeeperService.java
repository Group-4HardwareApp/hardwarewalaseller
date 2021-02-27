package com.e.hardwarewalaseller.apis;
import com.e.hardwarewalaseller.beans.Shopkeeper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ShopkeeperService {


    public static ShopkeeperApi shopkeeperApi;

    public static  ShopkeeperApi getShopkeeperApiInstance()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(shopkeeperApi==null) {
            shopkeeperApi = retrofit.create(ShopkeeperApi.class);
        }
        return shopkeeperApi;
    }

    public interface ShopkeeperApi
    {
        @Multipart
        @POST("shopkeeper/")
        public Call<Shopkeeper>saveShopkeeper(@Part MultipartBody.Part file,
                                              @Part("name") RequestBody name,
                                              @Part("shopName") RequestBody shopName,
                                              @Part("contactNumber") RequestBody contactNumber,
                                              @Part("address") RequestBody address,
                                              @Part("email") RequestBody email,
                                              @Part("token") RequestBody token,
                                              @Part("shopkeeperId") RequestBody shopkeeperId);



        @Multipart
        @POST("shopkeeper/updateshopkeeperimg")
        public Call<Shopkeeper> updateShopkeeperImage(@Part MultipartBody.Part file,@Part("shopkeeperId") RequestBody shopkeeperId);


        @POST("shopkeeper/updateshopkeeper")
        public  Call<Shopkeeper> updateShopkeeper(@Body Shopkeeper shopkeeper);


        @GET("shopkeeper/view/{id}")
        public Call<Shopkeeper> viewShopkeeper(@Path("id") String id);


        @POST("shopkeeper/updatetoken/{shopkeeperId}/{token}")
        public Call<Shopkeeper> updateShopkeeperToken(@Path("shopkeeperId") String shopkeeperId ,@Path("token") String token);
    }
}
