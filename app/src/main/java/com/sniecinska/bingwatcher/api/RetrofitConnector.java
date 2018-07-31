package com.sniecinska.bingwatcher.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ewasniecinska on 26.07.2018.
 */

public class RetrofitConnector {

    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Service getService(){
        Service service = getRetrofit().create(Service.class);
        return service;
    }

    public static Retrofit getRetrofit2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.trakt.tv/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Service getService2(){
        Service service = getRetrofit2().create(Service.class);
        return service;
    }

}
