package com.example.lequa.btsmanagement.ui.addtram;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.AddTramRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class AddTramViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Tram> tram= new MutableLiveData<>();
    final MutableLiveData<Boolean> displayListUser = new MutableLiveData<>();
    private final LiveData<Resource<List<UserBTS>>> listUser;
    private final LiveData<Resource<Tram>> resultInsertTram;
    @Inject
    AddTramViewModel(AddTramRepository addTramRepository){
        listUser= Transformations.switchMap(displayListUser, displayListUser->{
            if(displayListUser==null){
                return AbsentLiveData.create();

            }else{
                return addTramRepository.getAllUser(token.getValue());
            }
        });
        resultInsertTram= Transformations.switchMap(tram, tram->{
            if(tram==null){
                return AbsentLiveData.create();

            }else{
                return addTramRepository.addTram(tram,token.getValue());
            }
        });
    }
    public LiveData<Resource<List<UserBTS>>> getListUser(){
        return this.listUser;
    }
    public LiveData<Resource<Tram>> getResultInsertTram(){
        return this.resultInsertTram;
    }
    public void setDisplayListUser(String token,Boolean user){
        this.token.setValue(token);
        this.displayListUser.setValue(user);
    }
    public void setInsertTram(Tram tram){
        this.tram.setValue(tram);
    }
}
