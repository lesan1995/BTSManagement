package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.NhaMangService;
import com.example.lequa.btsmanagement.api.NhaTramService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.NhaMangDAO;
import com.example.lequa.btsmanagement.db.NhaTramDAO;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddNhaTramRepository {
    private final AppExecutors appExecutors;
    private final NhaTramService nhaTramService;
    private final NhaMangService nhaMangService;
    private final NhaTramDAO nhaTramDAO;
    private final NhaMangDAO nhaMangDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    AddNhaTramRepository(AppExecutors appExecutors, MyDatabase myDatabase,NhaTramDAO nhaTramDAO, NhaMangDAO nhaMangDAO,
                    NhaTramService nhaTramService, NhaMangService nhaMangService){
        this.myDatabase=myDatabase;
        this.nhaTramDAO=nhaTramDAO;
        this.nhaMangDAO=nhaMangDAO;
        this.nhaTramService=nhaTramService;
        this.nhaMangService=nhaMangService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<List<NhaMang>>> getListNhaMang(String token){
        return new NetworkBoundResource<List<NhaMang>,List<NhaMang>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<NhaMang> item) {
                nhaMangDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<NhaMang> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("AddNhaTram");
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
    public LiveData<Resource<NhaTram>> addNhaTram(NhaTram nhaTram, String token){
        return new NetworkBoundResource<NhaTram,NhaTram>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull NhaTram item) {
                nhaTramDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable NhaTram data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<NhaTram> loadFromDb() {
                return nhaTramDAO.load(nhaTram.getIDNhaTram());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NhaTram>> createCall() {
                return nhaTramService.postNhaTram("bearer "+token,nhaTram);
            }
        }.asLiveData();
    }
}
