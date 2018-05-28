package com.example.lequa.btsmanagement.ui.chittiettaikhoan;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.ChiTietTaiKhoanRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class ChiTietTaiKhoanViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<String> idUser = new MutableLiveData<>();
    private final LiveData<Resource<UserBTS>> user;
    @Inject
    public ChiTietTaiKhoanViewModel(ChiTietTaiKhoanRepository chiTietTaiKhoanRepository){
        user= Transformations.switchMap(idUser, idUser->{
            if(idUser==null){
                return AbsentLiveData.create();

            }else{
                return chiTietTaiKhoanRepository.getUser(idUser,token.getValue());
            }
        });
    }
    public LiveData<Resource<UserBTS>> getUser(){
        return this.user;
    }
    public void setUser(String idUser,String token){
        this.token.setValue(token);
        this.idUser.setValue(idUser);
    }
}
