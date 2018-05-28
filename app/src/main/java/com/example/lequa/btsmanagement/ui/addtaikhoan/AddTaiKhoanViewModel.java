package com.example.lequa.btsmanagement.ui.addtaikhoan;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.Register;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.AddTaiKhoanRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;

public class AddTaiKhoanViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Register> registerUser = new MutableLiveData<>();
    private final LiveData<Resource<UserBTS>> resultRegisterUser;
    @Inject
    public AddTaiKhoanViewModel(AddTaiKhoanRepository addTaiKhoanRepository){
        resultRegisterUser= Transformations.switchMap(registerUser, registerUser->{
            if(registerUser==null){
                return AbsentLiveData.create();

            }else{
                return addTaiKhoanRepository.registerTaiKhoan(registerUser,token.getValue());
            }
        });
    }
    public LiveData<Resource<UserBTS>> getResultRegisterUser(){
        return this.resultRegisterUser;
    }
    public void setRegister(String token,Register user){
        this.token.setValue(token);
        this.registerUser.setValue(user);
    }

}
