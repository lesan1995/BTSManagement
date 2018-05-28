package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.RegisterService;
import com.example.lequa.btsmanagement.api.UserService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.UserDAO;
import com.example.lequa.btsmanagement.model.Register;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddTaiKhoanRepository {
    private final AppExecutors appExecutors;
    private final UserService userService;
    private final RegisterService registerService;
    private final UserDAO userDAO;
    private final MyDatabase myDatabase;
    @Inject
    AddTaiKhoanRepository(AppExecutors appExecutors, MyDatabase myDatabase, UserDAO userDAO, UserService userService,
                          RegisterService registerService){
        this.myDatabase=myDatabase;
        this.userDAO=userDAO;
        this.userService=userService;
        this.registerService=registerService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<UserBTS>> registerTaiKhoan(Register taiKhoan, String token){
        return new NetworkBoundResource<UserBTS,Void>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Void item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable UserBTS data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UserBTS> loadFromDb() {
                return userDAO.loadWithEmail(taiKhoan.getEmail());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return registerService.registerTaiKhoan("bearer "+token,taiKhoan);
            }
        }.asLiveData();
    }
}
