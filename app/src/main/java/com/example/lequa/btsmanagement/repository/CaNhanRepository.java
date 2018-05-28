package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.UserService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.UserDAO;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;

@Singleton
public class CaNhanRepository {
    private final AppExecutors appExecutors;
    private final UserService userService;
    private final UserDAO userDAO;
    private final MyDatabase myDatabase;
    @Inject
    CaNhanRepository(AppExecutors appExecutors, MyDatabase myDatabase, UserDAO userDAO, UserService userService){
        this.myDatabase=myDatabase;
        this.userDAO=userDAO;
        this.userService=userService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<UserBTS>> getUser(String email, String token){
        return new NetworkBoundResource<UserBTS, UserBTS>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull UserBTS item) {
                userDAO.save(item);
            }
            @Override
            protected boolean shouldFetch(@Nullable UserBTS data) {
                return data==null;
            }
            @NonNull
            @Override
            protected LiveData<UserBTS> loadFromDb() {
                return userDAO.loadWithEmail(email);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserBTS>> createCall() {
                return userService.getUser("bearer "+token);
            }
        }.asLiveData();
    }
    public LiveData<Resource<UserBTS>> addHinhAnh(MultipartBody.Part file,String email, String token){
        return new NetworkBoundResource<UserBTS,UserBTS>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull UserBTS item) {
                userDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable UserBTS data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UserBTS> loadFromDb() {
                return userDAO.loadWithEmail(email);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserBTS>> createCall() {
                return userService.postHinhAnh("bearer "+token,file);
            }
        }.asLiveData();
    }
}
