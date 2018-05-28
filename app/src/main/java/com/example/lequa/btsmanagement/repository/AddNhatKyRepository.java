package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.NhatKyService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.NhatKyDAO;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddNhatKyRepository {
    private final AppExecutors appExecutors;
    private final NhatKyService nhatKyService;
    private final NhatKyDAO nhatKyDAO;
    private final MyDatabase myDatabase;
    @Inject
    AddNhatKyRepository(AppExecutors appExecutors, MyDatabase myDatabase, NhatKyDAO nhatKyDAO,
                        NhatKyService nhatKyService){
        this.myDatabase=myDatabase;
        this.nhatKyDAO=nhatKyDAO;
        this.nhatKyService=nhatKyService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<NhatKy>> addNhatKy(NhatKy nhatKy, String token){
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
                return nhatKyDAO.load(nhatKy.getIDNhatKy());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NhatKy>> createCall() {
                return nhatKyService.postNhatKy("bearer "+token,nhatKy);
            }
        }.asLiveData();
    }
}
