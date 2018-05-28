package com.example.lequa.btsmanagement.api;

import android.arch.lifecycle.LiveData;

import com.example.lequa.btsmanagement.model.Register;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("api/Account/Register")
    LiveData<ApiResponse<Void>> registerTaiKhoan(@Header("Authorization") String token, @Body Register register);
}
