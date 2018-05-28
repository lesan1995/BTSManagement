package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.MatDien;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MatDienService {
    @GET("api/MatDiens")
    LiveData<ApiResponse<List<MatDien>>> getMatDiens(@Header("Authorization") String token);
    @GET("api/MatDiens/{id}")
    LiveData<ApiResponse<MatDien>> getMatDiens(@Header("Authorization") String token, @Query("id") String id);
    @GET("api/MatDienByIDTram")
    LiveData<ApiResponse<List<MatDien>>> getMatDienByIDTram(@Header("Authorization") String token, @Query("id") String id);
    @POST("api/MatDiens")
    LiveData<ApiResponse<MatDien>> postMatDiens(@Header("Authorization") String token, @Body MatDien matDien);
}
