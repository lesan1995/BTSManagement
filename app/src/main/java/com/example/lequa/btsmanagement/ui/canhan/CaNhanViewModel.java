package com.example.lequa.btsmanagement.ui.canhan;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.CaNhanRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;

import okhttp3.MultipartBody;

public class CaNhanViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<String> email = new MutableLiveData<>();
    final MutableLiveData<MultipartBody.Part> image = new MutableLiveData<>();
    private final LiveData<Resource<UserBTS>> user;
    private final LiveData<Resource<UserBTS>> resultUpdateImage;
    @Inject
    public CaNhanViewModel(CaNhanRepository caNhanRepository){
        user= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return caNhanRepository.getUser(email.getValue(),token);
            }
        });
        resultUpdateImage= Transformations.switchMap(image, image->{
            if(image==null){
                return AbsentLiveData.create();

            }else{
                return caNhanRepository.addHinhAnh(image,email.getValue(),token.getValue());
            }
        });
    }
    public LiveData<Resource<UserBTS>> getUser(){
        return this.user;
    }
    public LiveData<Resource<UserBTS>> getResultUpdateImage(){
        return this.resultUpdateImage;
    }
    public void setUser(String email,String token){
        this.email.setValue(email);
        this.token.setValue(token);
    }
    public void setImage(MultipartBody.Part image){
        this.image.setValue(image);
    }
}
