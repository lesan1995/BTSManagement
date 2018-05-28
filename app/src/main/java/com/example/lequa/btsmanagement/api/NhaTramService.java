package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.NhaTram;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface NhaTramService {
    @GET("api/NhaTrams")
    LiveData<ApiResponse<List<NhaTram>>> getNhaTrams(@Header("Authorization") String token);
    @GET("api/NhaTrams/{id}")
    LiveData<ApiResponse<NhaTram>> getNhaTrams(@Header("Authorization") String token, @Query("id") String id);
    @GET("api/NhaTramByIDTram")
    LiveData<ApiResponse<List<NhaTram>>> getNhaTramByIDTram(@Header("Authorization") String token, @Query("id") String id);
    @POST("api/NhaTrams")
    LiveData<ApiResponse<NhaTram>> postNhaTram(@Header("Authorization") String token, @Body NhaTram nhaTram);
    @PUT("api/NhaTrams/{id}")
    LiveData<ApiResponse<Integer>> putNhaTram(@Header("Authorization") String token, @Query("id") String id, @Body NhaTram nhaTram);
    @PUT("api/NhaTrams/{id}")
    LiveData<ApiResponse<Integer>> putNhaTram(@Header("Authorization") String token, @Query("id") String id, @Query("idNhaMang") String idNhaMang);
    @DELETE("api/NhaTrams/{id}")
    LiveData<ApiResponse<NhaTram>> deleteNhaTram(@Header("Authorization") String token, @Query("id") String id);
}
