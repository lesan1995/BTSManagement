package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.TramService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.TramDAO;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class DSTramRepository {
    private final AppExecutors appExecutors;
    private final TramService tramService;
    private final TramDAO tramDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    DSTramRepository(AppExecutors appExecutors, MyDatabase myDatabase,TramDAO tramDAO,TramService tramService){
        this.myDatabase=myDatabase;
        this.tramDAO=tramDAO;
        this.tramService=tramService;
        this.appExecutors=appExecutors;
    }
    public LiveData<Resource<List<Tram>>> getListTram(String token,String query){
        return new NetworkBoundResource<List<Tram>, List<Tram>>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull List<Tram> item) {
                tramDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Tram> data) {
                return data==null||data.isEmpty()||rateLimit.shouldFetch("DSTram");
            }

            @NonNull
            @Override
            protected LiveData<List<Tram>> loadFromDb() {
                return tramDAO.loadWithTenTram("%"+query+"%");
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Tram>>> createCall() {
                return tramService.getTrams("bearer "+token);
            }
        }.asLiveData();
    }
}
