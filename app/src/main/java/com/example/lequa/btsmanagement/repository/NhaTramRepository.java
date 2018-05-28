package com.example.lequa.btsmanagement.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lequa.btsmanagement.AppExecutors;
import com.example.lequa.btsmanagement.api.ApiResponse;
import com.example.lequa.btsmanagement.api.NhaMangService;
import com.example.lequa.btsmanagement.api.NhaTramService;
import com.example.lequa.btsmanagement.api.TramService;
import com.example.lequa.btsmanagement.db.MyDatabase;
import com.example.lequa.btsmanagement.db.NhaMangDAO;
import com.example.lequa.btsmanagement.db.NhaTramDAO;
import com.example.lequa.btsmanagement.db.TramDAO;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.util.RateLimiter;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NhaTramRepository {
    private final AppExecutors appExecutors;
    private final TramService tramService;
    private final NhaTramService nhaTramService;
    private final NhaMangService nhaMangService;
    private final TramDAO tramDAO;
    private final NhaTramDAO nhaTramDAO;
    private final NhaMangDAO nhaMangDAO;
    private final MyDatabase myDatabase;
    private RateLimiter<String> rateLimit = new RateLimiter<>(20, TimeUnit.SECONDS);
    @Inject
    NhaTramRepository(AppExecutors appExecutors, MyDatabase myDatabase, TramDAO tramDAO,NhaTramDAO nhaTramDAO,
                   NhaMangDAO nhaMangDAO, TramService tramService, NhaTramService nhaTramService,
                   NhaMangService nhaMangService){
        this.myDatabase=myDatabase;
        this.tramDAO=tramDAO;
        this.nhaTramDAO=nhaTramDAO;
        this.nhaMangDAO=nhaMangDAO;
        this.tramService=tramService;
        this.nhaTramService=nhaTramService;
        this.nhaMangService=nhaMangService;
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
    public LiveData<Resource<NhaTram>> getNhaTram(int idNhaTram, String token){
        return new NetworkBoundResource<NhaTram, NhaTram>(appExecutors){
            @Override
            protected void saveCallResult(@NonNull NhaTram item) {
                nhaTramDAO.save(item);
            }
            @Override
            protected boolean shouldFetch(@Nullable NhaTram data) {
                return data==null;
            }
            @NonNull
            @Override
            protected LiveData<NhaTram> loadFromDb() {
                return nhaTramDAO.load(idNhaTram);
            }
            @NonNull
            @Override
            protected LiveData<ApiResponse<NhaTram>> createCall() {
                return nhaTramService.getNhaTrams("bearer "+token,idNhaTram+"");
            }
        }.asLiveData();
    }
    public LiveData<Resource<NhaMang>> getNhaMang(int idNhaMang, String token){
        return new NetworkBoundResource<NhaMang, NhaMang>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull NhaMang item) {
                nhaMangDAO.save(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable NhaMang data) {
                return data==null;
            }

            @NonNull
            @Override
            protected LiveData<NhaMang> loadFromDb() {
                return nhaMangDAO.load(idNhaMang);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NhaMang>> createCall() {
                return nhaMangService.getNhaMangs("bearer "+token,idNhaMang+"");
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
                return data==null||data.isEmpty()||rateLimit.shouldFetch("NhaTram");
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
    public LiveData<Resource<NhaTram>> updateNhaTram(NhaTram nhaTram, String token){
        return new NetworkBoundResource<NhaTram,Integer>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Integer item) {
                nhaTramDAO.save(nhaTram);
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
            protected LiveData<ApiResponse<Integer>> createCall() {
                return nhaTramService.putNhaTram("bearer "+token,nhaTram.getIDNhaTram()+"",nhaTram.getIDNhaMang()+"");
            }
        }.asLiveData();
    }
    public LiveData<Resource<NhaTram>> updateThongSo(NhaTram nhaTram, String token){
        return new NetworkBoundResource<NhaTram,Integer>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull Integer item) {
                nhaTramDAO.save(nhaTram);
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
            protected LiveData<ApiResponse<Integer>> createCall() {
                return nhaTramService.putNhaTram("bearer "+token,nhaTram.getIDNhaTram()+"",nhaTram);
            }
        }.asLiveData();
    }

}
