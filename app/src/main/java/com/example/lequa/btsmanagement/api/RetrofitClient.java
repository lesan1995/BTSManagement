package com.example.lequa.btsmanagement.api;

import com.example.lequa.btsmanagement.util.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        if (retrofit==null) {

            retrofit = new Retrofit.Builder()

                    .baseUrl(baseUrl)

                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())

                    .build();

        }
        return retrofit;

    }
}
