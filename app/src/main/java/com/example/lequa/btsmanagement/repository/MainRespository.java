package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.NhaMangService;
import com.example.lequa.btsmanagement.api.TramService;
import com.example.lequa.btsmanagement.api.UserService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.NhaMangDAO;
import com.example.lequa.btsmanagement.db.TramDAO;
import com.example.lequa.btsmanagement.db.UserDAO;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainRespository {
    private final AppExecutors appExecutors;
    private final UserService userService;
    private final TramService tramService;
    private final NhaMangService nhaMangService;
    private final UserDAO userDAO;
    private final TramDAO tramDAO;
    private final NhaMangDAO nhaMangDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    MainRespository(AppExecutors appExecutors, MyDatabase myDatabase, UserDAO userDAO,TramDAO tramDAO,NhaMangDAO nhaMangDAO,
                    UserService userService,TramService tramService,NhaMangService nhaMangService){
        this.myDatabase=myDatabase;
        this.userDAO=userDAO;
        this.tramDAO=tramDAO;
        this.nhaMangDAO=nhaMangDAO;
        this.userService=userService;
        this.tramService=tramService;
        this.nhaMangService=nhaMangService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<UserBTS>> getUser(String email,String token){
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
    public LiveData<Resource<List<Tram>>> getListTram(String token){
        return new NetworkBoundResource<List<Tram>, List<Tram>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<Tram> item) {
                tramDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Tram> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("MainGetListTram");
            }

            @NonNull
            @Override
            protected LiveData<List<Tram>> loadFromDb() {
                return tramDAO.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Tram>>> createCall() {
                return tramService.getTrams("bearer "+token);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Tram>> deleteTram(int idTram, String token){
        return new NetworkBoundResource<Tram,Tram>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Tram item) {
                tramDAO.delete(item.getIDTram());
            }

            @Override
            protected boolean shouldFetch(@Nullable Tram data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Tram> loadFromDb() {
                return tramDAO.load(idTram);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Tram>> createCall() {
                return tramService.deleteTram("bearer "+token,idTram+"");
            }
        }.asLiveData();
    }
    public LiveData<Resource<List<UserBTS>>> getAllUser(String token){
        return new NetworkBoundResource<List<UserBTS>,List<UserBTS>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<UserBTS> item) {
                userDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<UserBTS> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("MainGetAllUser");
            }

            @NonNull
            @Override
            protected LiveData<List<UserBTS>> loadFromDb() {
                return userDAO.loadAllQuanLy();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<UserBTS>>> createCall() {
                return userService.getAllUser("bearer "+token);
            }
        }.asLiveData();
    }
    public LiveData<Resource<List<NhaMang>>> getListNhaMang(String token){
        return new NetworkBoundResource<List<NhaMang>,List<NhaMang>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<NhaMang> item) {
                nhaMangDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<NhaMang> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("MainGetListNhaMang");
            }

            @NonNull
            @Override
            protected LiveData<List<NhaMang>> loadFromDb() {
                return nhaMangDAO.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<NhaMang>>> createCall() {
                return nhaMangService.getNhaMangs("bearer "+token);
            }
        }.asLiveData();
    }

}
