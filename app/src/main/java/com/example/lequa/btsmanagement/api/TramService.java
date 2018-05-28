package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.Tram;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TramService {
    @GET("api/Trams")
    LiveData<ApiResponse<List<Tram>>> getTrams(@Header("Authorization") String token);
    @GET("api/Trams/{id}")
    LiveData<ApiResponse<Tram>> getTrams(@Header("Authorization") String token, @Query("id") String id);
    @POST("api/Trams")
    LiveData<ApiResponse<Tram>> postTram(@Header("Authorization") String token, @Body Tram tram);
    @PUT("api/Trams/{id}")
    LiveData<ApiResponse<Integer>> putTram(@Header("Authorization") String token, @Query("id") String id, @Body Tram tram);
    @DELETE("api/Trams/{id}")
    LiveData<ApiResponse<Tram>> deleteTram(@Header("Authorization") String token, @Query("id") String id);
}
