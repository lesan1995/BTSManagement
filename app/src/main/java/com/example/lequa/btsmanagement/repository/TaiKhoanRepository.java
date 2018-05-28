package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.UserService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.UserDAO;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaiKhoanRepository {
    private final AppExecutors appExecutors;
    private final UserService userService;
    private final UserDAO userDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    TaiKhoanRepository(AppExecutors appExecutors, MyDatabase myDatabase, UserDAO userDAO, UserService userService){
        this.myDatabase=myDatabase;
        this.userDAO=userDAO;
        this.userService=userService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<List<UserBTS>>> getAllUser(String token,String query){
        return new NetworkBoundResource<List<UserBTS>,List<UserBTS>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<UserBTS> item) {
                Log.d("TramRepository","saveCallResult");
                userDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<UserBTS> data) {
                Log.d("TramRepository","shouldFetch");
                return data==null||data.isEmpty()||rateLimit.shouldFetch("TaiKhoan");
            }

            @NonNull
            @Override
            protected LiveData<List<UserBTS>> loadFromDb() {
                Log.d("TramRepository","loadFromDb");
                return userDAO.loadWithName(query+"%");
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<UserBTS>>> createCall() {
                Log.d("TramRepository","createCall");
                return userService.getAllUser("bearer "+token);
            }
        }.asLiveData();
    }
}
