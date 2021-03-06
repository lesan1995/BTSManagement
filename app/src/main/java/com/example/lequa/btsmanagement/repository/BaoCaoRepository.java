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
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BaoCaoRepository {
    private final AppExecutors appExecutors;
    private final NhatKyService nhatKyService;
    private final NhatKyDAO nhatKyDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    BaoCaoRepository(AppExecutors appExecutors, MyDatabase myDatabase, NhatKyDAO nhatKyDAO,
                       NhatKyService nhatKyService){
        this.myDatabase=myDatabase;
        this.nhatKyDAO=nhatKyDAO;
        this.nhatKyService=nhatKyService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<List<NhatKy>>> getListNhatKy(String query, String token){
        return new NetworkBoundResource<List<NhatKy>, List<NhatKy>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<NhatKy> item) {
                nhatKyDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<NhatKy> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("BaoCao");
            }

            @NonNull
            @Override
            protected LiveData<List<NhatKy>> loadFromDb() {
                switch (query){
                    case "HoanThanh":return nhatKyDAO.loadSCHT();
                    case "ChuaHoanThanh":return nhatKyDAO.loadSCCHT();
                    default:return nhatKyDAO.loadSC();
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<NhatKy>>> createCall() {
                return nhatKyService.getNhatKys("bearer "+token);
            }
        }.asLiveData();
    }
}

