package com.example.lequa.btsmanagement.ui.edittoado;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.EditToaDoRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;
import java.util.List;
import javax.inject.Inject;

public class EditToaDoViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idTram = new MutableLiveData<>();
    final MutableLiveData<String> idUser = new MutableLiveData<>();
    final MutableLiveData<Tram> tramUpdate= new MutableLiveData<>();
    private final LiveData<Resource<List<Tram>>> listTram;
    private final LiveData<Resource<List<UserBTS>>> listUser;
    private final LiveData<Resource<Tram>> resultUpdateTram;
    private final LiveData<Resource<Tram>> tram;
    private final LiveData<Resource<UserBTS>> user;
    @Inject
    public EditToaDoViewModel(EditToaDoRepository editToaDoRepository){
        tram= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return editToaDoRepository.getTram(idTram,token.getValue());
            }
        });
        user= Transformations.switchMap(idUser, idUser->{
            if(idUser==null){
                return AbsentLiveData.create();

            }else{
                return editToaDoRepository.getUser(idUser,token.getValue());
            }
        });
        listTram= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return editToaDoRepository.getListTram(token);
            }
        });
        listUser= Transformations.switchMap(token, token->{
            if(token==null){
                return AbsentLiveData.create();

            }else{
                return editToaDoRepository.getAllUser(token);
            }
        });
        resultUpdateTram= Transformations.switchMap(tramUpdate, tramUpdate->{
            if(tramUpdate==null){
                return AbsentLiveData.create();

            }else{
                return editToaDoRepository.updateTram(tramUpdate,token.getValue());
            }
        });
    }
    public LiveData<Resource<Tram>> getTram(){
        return this.tram;
    }
    public LiveData<Resource<UserBTS>> getUser(){
        return this.user;
    }
    public LiveData<Resource<List<UserBTS>>> getListUser(){
        return this.listUser;
    }
    public LiveData<Resource<List<Tram>>> getListTram(){
        return this.listTram;
    }
    public LiveData<Resource<Tram>> getResultUpdateTram(){
        return this.resultUpdateTram;
    }
    public void setTram(int idTram,String token){
        this.token.setValue(token);
        this.idTram.setValue(idTram);
    }
    public void setUser(String idUser){
        this.idUser.setValue(idUser);
    }
    public void setUpdateTram(Tram tram){
        this.tramUpdate.setValue(tram);
    }
}
