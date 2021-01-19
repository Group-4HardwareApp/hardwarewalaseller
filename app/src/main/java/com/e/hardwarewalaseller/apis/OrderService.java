package com.e.hardwarewalaseller.apis;

import com.e.hardwarewalaseller.beans.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class OrderService {
    public static OrderApi orderApi;


    public static OrderApi  getOrderApiInstance()
    {
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ServerAddress.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();


        if(orderApi == null)
        {
            orderApi = retrofit.create(OrderApi.class);
        }
        return orderApi;
    }



    public interface OrderApi
    {
        @GET("/order/orderHistory/{shopkeeperId}")
        public Call<ArrayList<Order>> getPurchaseOrder(@Path("shopkeeperId") String shopkeeperId);

        @GET("/order/onwayorders/{shopkeeperId}")
        public Call<ArrayList<Order>> getOnGoingOrder(@Path("shopkeeperId") String shopkeeperId);

    }


}
