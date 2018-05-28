package com.example.lequa.btsmanagement.ui.taikhoan;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.TaiKhoanRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class TaiKhoanViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<String> query = new MutableLiveData<>();
    private final LiveData<Resource<List<UserBTS>>> listUser;
    @Inject
    TaiKhoanViewModel(TaiKhoanRepository taiKhoanRepository){
        listUser= Transformations.switchMap(query, query->{
            if(query==null){
                return AbsentLiveData.create();

            }else{
                return taiKhoanRepository.getAllUser(token.getValue(),query);
            }
        });
    }
    public LiveData<Resource<List<UserBTS>>> getAllUser(){
        return this.listUser;
    }
    public void setToken(String token){
        this.token.setValue(token);
}
    public void setQuery(String query){
        this.query.setValue(query);
    }
}
