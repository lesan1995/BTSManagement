package com.example.lequa.btsmanagement.ui.toado;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.ToaDoRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class ToaDoViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Tram> tram= new MutableLiveData<>();
    private final LiveData<Resource<List<Tram>>> listTram;
    private final LiveData<Resource<List<UserBTS>>> listUser;
    private final LiveData<Resource<Tram>> resultInsertTram;
    @Inject
    public ToaDoViewModel(ToaDoRepository toaDoRepository){
        listTram= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return toaDoRepository.getListTram(token);
            }
        });
        listUser= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return toaDoRepository.getAllUser(token);
            }
        });
        resultInsertTram= Transformations.switchMap(tram, tram->{
            if(tram==null){
                return AbsentLiveData.create();

            }else{
                return toaDoRepository.addTram(tram,token.getValue());
            }
        });
    }
    public LiveData<Resource<List<UserBTS>>> getListUser(){
        return this.listUser;
    }
    public LiveData<Resource<List<Tram>>> getListTram(){
        return this.listTram;
    }
    public LiveData<Resource<Tram>> getResultInsertTram(){
        return this.resultInsertTram;
    }
    public void setToken(String token){ this.token.setValue(token); }
    public void setInsertTram(Tram tram){
        this.tram.setValue(tram);
    }
}
