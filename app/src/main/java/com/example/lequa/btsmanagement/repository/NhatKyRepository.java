package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.NhatKyService;
import com.example.lequa.btsmanagement.api.TramService;
import com.example.lequa.btsmanagement.api.UserService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.NhatKyDAO;
import com.example.lequa.btsmanagement.db.TramDAO;
import com.example.lequa.btsmanagement.db.UserDAO;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NhatKyRepository {
    private final AppExecutors appExecutors;
    private final UserService userService;
    private final TramService tramService;
    private final NhatKyService nhatKyService;
    private final NhatKyDAO nhatKyDAO;
    private final UserDAO userDAO;
    private final TramDAO tramDAO;
    private final MyDatabase myDatabase;
    @Inject
    NhatKyRepository(AppExecutors appExecutors, MyDatabase myDatabase, NhatKyDAO nhatKyDAO,UserDAO userDAO,TramDAO tramDAO,
                       NhatKyService nhatKyService,UserService userService,TramService tramService){
        this.myDatabase=myDatabase;
        this.nhatKyDAO=nhatKyDAO;
        this.userDAO=userDAO;
        this.tramDAO=tramDAO;
        this.nhatKyService=nhatKyService;
        this.userService=userService;
        this.tramService=tramService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<NhatKy>> getNhatKy(int idNhatKy, String token){
        return new NetworkBoundResource<NhatKy, NhatKy>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull NhatKy item) {
                nhatKyDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable NhatKy data) {
                return data==null;
            }

            @NonNull
            @Override
            protected LiveData<NhatKy> loadFromDb() {
                return nhatKyDAO.load(idNhatKy);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NhatKy>> createCall() {
                return nhatKyService.getNhatKys("bearer "+token,idNhatKy+"");
            }
        }.asLiveData();

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
}

