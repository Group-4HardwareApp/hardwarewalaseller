package com.e.hardwarewalaseller.apis;

import com.e.hardwarewalaseller.beans.Category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class CategoryService {

public static CategoryApi categoryApi;

public static CategoryApi getCategoryApiInstance()
{

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ServerAddress.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    if(categoryApi==null)
    {
        categoryApi = retrofit.create(CategoryApi.class);
    }
    return categoryApi;

}



    public interface CategoryApi
    {
        @GET("category/")
        public Call<ArrayList<Category>>getCategoryList();
    }

}
