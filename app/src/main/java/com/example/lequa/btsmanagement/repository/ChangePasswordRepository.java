package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.PasswordService;
import com.example.lequa.btsmanagement.db.LoginDAO;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.model.Login;
import com.example.lequa.btsmanagement.model.Password;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChangePasswordRepository {
    private final AppExecutors appExecutors;
    private final LoginDAO loginDAO;
    private final PasswordService passwordService;
    private final MyDatabase myDatabase;
    @Inject
    ChangePasswordRepository(AppExecutors appExecutors,PasswordService passwordService,LoginDAO loginDAO,MyDatabase myDatabase){
        this.appExecutors=appExecutors;
        this.passwordService=passwordService;
        this.loginDAO=loginDAO;
        this.myDatabase=myDatabase;
    }
    public LiveData<Resource<Login>> changePassword(Password password, String token ){
        return new NetworkBoundResource<Login,Void>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Void item) {
            }

            @Override
            protected boolean shouldFetch(@Nullable Login data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Login> loadFromDb() {
                return loginDAO.load("bearer");
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return passwordService.changePassword("bearer "+token,password);
            }
        }.asLiveData();
    }
}
