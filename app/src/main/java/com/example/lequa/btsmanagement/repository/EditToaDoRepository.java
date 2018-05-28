package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.TramService;
import com.example.lequa.btsmanagement.api.UserService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.TramDAO;
import com.example.lequa.btsmanagement.db.UserDAO;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EditToaDoRepository {
    private final AppExecutors appExecutors;
    private final UserService userService;
    private final TramService tramService;
    private final UserDAO userDAO;
    private final TramDAO tramDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    EditToaDoRepository(AppExecutors appExecutors, MyDatabase myDatabase,TramDAO tramDAO,UserDAO userDAO,
                    UserService userService,TramService tramService){
        this.myDatabase=myDatabase;
        this.userDAO=userDAO;
        this.tramDAO=tramDAO;
        this.userService=userService;
        this.tramService=tramService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<Tram>> getTram(int idTram, String token){
        return new NetworkBoundResource<Tram, Tram>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Tram item) {
                tramDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Tram data) {
                return data==null;
            }

            @NonNull
            @Override
            protected LiveData<Tram> loadFromDb() {
                return tramDAO.load(idTram);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Tram>> createCall() {
                return tramService.getTrams("bearer "+token,idTram+"");
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
                return data==null||data.isEmpty()||rateLimit.shouldFetch("EditToaDo");
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
    public LiveData<Resource<UserBTS>> getUser(String idUser, String token){
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
                return userDAO.load(idUser);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserBTS>> createCall() {
                return userService.getUser("bearer "+token,idUser);
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
                return data==null||data.isEmpty()||rateLimit.shouldFetch("EditToaDoGetAllUser");
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
    public LiveData<Resource<Tram>> updateTram(Tram tram, String token){
        return new NetworkBoundResource<Tram,Integer>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Integer item) {
                tramDAO.save(tram);
            }

            @Override
            protected boolean shouldFetch(@Nullable Tram data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Tram> loadFromDb() {
                return tramDAO.load(tram.getIDTram());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Integer>> createCall() {
                return tramService.putTram("bearer "+token,tram.getIDTram()+"",tram);
            }
        }.asLiveData();
    }
}
