package com.example.lequa.btsmanagement.ui.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.MainRespository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<String> email = new MutableLiveData<>();
    final MutableLiveData<Integer> deleteTram = new MutableLiveData<>();
    final MutableLiveData<Boolean> isDeleteTram = new MutableLiveData<>();
    final MutableLiveData<Boolean> displayListNhaMang = new MutableLiveData<>();
    private final LiveData<Resource<UserBTS>> user;
    private final LiveData<Resource<List<Tram>>> listTram;
    private final LiveData<Resource<Tram>> resultDeleteTram;
    private final LiveData<Resource<List<UserBTS>>> listUser;
    private final LiveData<Resource<List<NhaMang>>> listNhaMang;
    @Inject
    public MainViewModel(MainRespository mainRespository){
        user= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return mainRespository.getUser(email.getValue(),token);
            }
        });
        listTram= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return mainRespository.getListTram(token);
            }
        });
        resultDeleteTram= Transformations.switchMap(isDeleteTram, isDeleteTram->{
            if(isDeleteTram==null){
                return AbsentLiveData.create();

            }else{
                return mainRespository.deleteTram(deleteTram.getValue(),token.getValue());
            }
        });
        listUser= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return mainRespository.getAllUser(token);
            }
        });
        listNhaMang= Transformations.switchMap(displayListNhaMang, displayListNhaMang->{
            if(displayListNhaMang==null){
                return AbsentLiveData.create();

            }else{
                return mainRespository.getListNhaMang(token.getValue());
            }
        });
    }
    public LiveData<Resource<UserBTS>> getUser(){
        return this.user;
    }
    public LiveData<Resource<List<Tram>>> getListTram(){
        return this.listTram;
    }
    public LiveData<Resource<Tram>> getResultDeleteTram(){
        return this.resultDeleteTram;
    }
    public LiveData<Resource<List<UserBTS>>> getListUser(){
        return this.listUser;
    }
    public LiveData<Resource<List<NhaMang>>> getListNhaMang(){
        return this.listNhaMang;
    }
    public void setUser(String email,String token){
        this.token.setValue(token);
        this.email.setValue(email);
    }
    public void setDeleteTram(int idTram,boolean isDeleteTram){
        this.deleteTram.setValue(idTram);
        this.isDeleteTram.setValue(isDeleteTram);
    }
    public void setDisplayListNhaMang(boolean displayListNhaMang){
        this.displayListNhaMang.setValue(displayListNhaMang);
    }
}
