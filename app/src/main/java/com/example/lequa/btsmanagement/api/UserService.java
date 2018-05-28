package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.UserBTS;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {
    @GET("api/Account/GetAllUser")
    LiveData<ApiResponse<List<UserBTS>>> getAllUser(@Header("Authorization") String token);
    @GET("api/Account/GetUser")
    LiveData<ApiResponse<UserBTS>> getUser(@Header("Authorization") String token);
    @GET("api/Account/GetUser")
    LiveData<ApiResponse<UserBTS>> getUser(@Header("Authorization") String token, @Query("id") String id);
    @PUT("api/Account/PutUser")
    LiveData<ApiResponse<Void>> putUser(@Header("Authorization") String token, @Body UserBTS userBTS);
    @Multipart
    @POST("api/Account/PostImage")
    LiveData<ApiResponse<UserBTS>> postHinhAnh(@Header("Authorization") String token, @Part MultipartBody.Part file);

}
