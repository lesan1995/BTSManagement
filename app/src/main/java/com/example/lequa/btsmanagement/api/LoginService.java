package com.example.lequa.btsmanagement.api;
import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.Login;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("Token")
    LiveData<ApiResponse<Login>> postLogin(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);
}
