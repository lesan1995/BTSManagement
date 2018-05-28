package com.example.lequa.btsmanagement.ui.editcanhan;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.EditCaNhanRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;

public class EditCaNhanViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<String> email = new MutableLiveData<>();
    final MutableLiveData<UserBTS> userUpdate = new MutableLiveData<>();
    private final LiveData<Resource<UserBTS>> user;
    private final LiveData<Resource<UserBTS>> resultUpdate;
    @Inject
    public EditCaNhanViewModel(EditCaNhanRepository editCaNhanRepository){
        user= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return editCaNhanRepository.getUser(email.getValue(),token);
            }
        });
        resultUpdate= Transformations.switchMap(userUpdate, userUpdate->{
            if(userUpdate==null){
                return AbsentLiveData.create();

            }else{
                return editCaNhanRepository.updateUser(userUpdate,token.getValue());
            }
        });
    }
    public LiveData<Resource<UserBTS>> getUser(){
        return this.user;
    }
    public LiveData<Resource<UserBTS>> getResultUpdate(){
        return this.resultUpdate;
    }
    public void setUser(String email,String token){
        this.email.setValue(email);
        this.token.setValue(token);
    }
    public void setUserUpdate(UserBTS userUpdate){
        this.userUpdate.setValue(userUpdate);
    }
}
