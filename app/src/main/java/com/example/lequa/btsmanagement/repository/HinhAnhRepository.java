package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.HinhAnhTramService;
import com.example.lequa.btsmanagement.db.HinhAnhTramDAO;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.model.HinhAnhTram;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;

@Singleton
public class HinhAnhRepository {
    private final AppExecutors appExecutors;
    private final HinhAnhTramDAO hinhAnhTramDAO;
    private final HinhAnhTramService hinhAnhTramService;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    HinhAnhRepository(AppExecutors appExecutors, MyDatabase myDatabase,
                      HinhAnhTramDAO hinhAnhTramDAO,HinhAnhTramService hinhAnhTramService){
        this.appExecutors=appExecutors;
        this.hinhAnhTramDAO=hinhAnhTramDAO;
        this.hinhAnhTramService=hinhAnhTramService;
        this.myDatabase=myDatabase;
    }
    public LiveData<Resource<List<HinhAnhTram>>> getHinhAnhTramByIDTram(String token, int idTram){
        return new NetworkBoundResource<List<HinhAnhTram>,List<HinhAnhTram>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<HinhAnhTram> item) {
                hinhAnhTramDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<HinhAnhTram> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("HinhAnh");
            }

            @NonNull
            @Override
            protected LiveData<List<HinhAnhTram>> loadFromDb() {
                return hinhAnhTramDAO.loadByIDTram(idTram);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<HinhAnhTram>>> createCall() {
                return hinhAnhTramService.getHinhAnhTramByIDTram("bearer "+token,idTram+"");
            }
        }.asLiveData();
    }
    public LiveData<Resource<HinhAnhTram>> addHinhAnh(int idTram,MultipartBody.Part file, String token){
        return new NetworkBoundResource<HinhAnhTram,HinhAnhTram>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull HinhAnhTram item) {
                hinhAnhTramDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable HinhAnhTram data) {
                return data==null;
            }

            @NonNull
            @Override
            protected LiveData<HinhAnhTram> loadFromDb() {
                return hinhAnhTramDAO.load(0);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HinhAnhTram>> createCall() {
                return hinhAnhTramService.postHinhAnh("bearer "+token,idTram+"",file);
            }
        }.asLiveData();

    }
    public LiveData<Resource<HinhAnhTram>> deleteHinhAnh(HinhAnhTram hinhAnhTram, String token){
        return new NetworkBoundResource<HinhAnhTram,HinhAnhTram>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull HinhAnhTram item) {
                hinhAnhTramDAO.delete(item.getIDHinhAnh());
            }

            @Override
            protected boolean shouldFetch(@Nullable HinhAnhTram data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<HinhAnhTram> loadFromDb() {
                return hinhAnhTramDAO.load(hinhAnhTram.getIDHinhAnh());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HinhAnhTram>> createCall() {
                return hinhAnhTramService.deleteHinhAnhTram("bearer "+token,hinhAnhTram.getIDHinhAnh()+"");
            }
        }.asLiveData();
    }
}