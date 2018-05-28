package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.HinhAnhTram;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface HinhAnhTramService {
    @GET("api/HinhAnhTramsByIDTram")
    LiveData<ApiResponse<List<HinhAnhTram>>> getHinhAnhTramByIDTram(@Header("Authorization") String token, @Query("id") String id);
    @Multipart
    @POST("api/HinhAnhTrams")
    LiveData<ApiResponse<HinhAnhTram>> postHinhAnh(@Header("Authorization") String token, @Query("idTram") String idTram, @Part MultipartBody.Part file);
    @DELETE("api/HinhAnhTrams/{id}")
    LiveData<ApiResponse<HinhAnhTram>> deleteHinhAnhTram(@Header("Authorization") String token, @Query("id") String id);
}
