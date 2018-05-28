package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.NhatKy;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface NhatKyService {
    @GET("api/NhatKies")
    LiveData<ApiResponse<List<NhatKy>>> getNhatKys(@Header("Authorization") String token);
    @GET("api/NhatKies/{id}")
    LiveData<ApiResponse<NhatKy>> getNhatKys(@Header("Authorization") String token, @Query("id") String id);
    @GET("api/NhatKyByIDTram")
    LiveData<ApiResponse<List<NhatKy>>> getNhatKyByIDTram(@Header("Authorization") String token, @Query("id") String id);
    @POST("api/NhatKies")
    LiveData<ApiResponse<NhatKy>> postNhatKy(@Header("Authorization") String token, @Body NhatKy nhatKy);
    @PUT("api/NhatKies")
    LiveData<ApiResponse<Void>> changeTrangThai(@Header("Authorization") String token, @Query("idNhatKy") String idNhatKy, @Query("daGiaiQuyet") boolean daGiaiQuyet);
    @DELETE("api/NhatKies/{id}")
    LiveData<ApiResponse<NhatKy>> deleteNhatKy(@Header("Authorization") String token, @Query("id") String id);
}
