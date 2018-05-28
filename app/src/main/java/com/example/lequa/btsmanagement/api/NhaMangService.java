package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.NhaMang;

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

public interface NhaMangService {
    @GET("api/NhaMangs")
    LiveData<ApiResponse<List<NhaMang>>> getNhaMangs(@Header("Authorization") String token);
    @GET("api/NhaMangs/{id}")
    LiveData<ApiResponse<NhaMang>> getNhaMangs(@Header("Authorization") String token, @Query("id") String id);
    @Multipart
    @POST("api/NhaMangs")
    LiveData<ApiResponse<NhaMang>> postNhaMangs(@Header("Authorization") String token, @Query("tenNhaMang") String tenNhaMang, @Part MultipartBody.Part image);
    @PUT("api/NhaMangs/{id}")
    LiveData<ApiResponse<Void>> putNhaMangs(@Header("Authorization") String token, @Query("id") String id, @Body NhaMang nhaMang);
    @Multipart
    @POST("api/ChangeImageNhaMang")
    LiveData<ApiResponse<NhaMang>> changeImageNhaMang(@Header("Authorization") String token, @Query("idNhaMang") String idNhaMang, @Part MultipartBody.Part image);

}
