package com.example.lequa.btsmanagement.ui.tram;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.repository.TramRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class TramViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idTram = new MutableLiveData<>();
    final MutableLiveData<String> idUser = new MutableLiveData<>();
    final MutableLiveData<Integer> idNhaTram = new MutableLiveData<>();
    final MutableLiveData<Integer> idNhaMang = new MutableLiveData<>();
    final MutableLiveData<Tram> tramUpdate = new MutableLiveData<>();
    final MutableLiveData<Boolean> updateQuanLy = new MutableLiveData<>();
    final MutableLiveData<NhaTram> deleteNhaTram = new MutableLiveData<>();
    private final LiveData<Resource<Tram>> tram;
    private final LiveData<Resource<UserBTS>> user;
    private final LiveData<Resource<List<NhaTram>>> listNhaTram;
    private final LiveData<Resource<List<NhaMang>>> listNhaMang;
    private final LiveData<Resource<NhaTram>> nhaTram;
    private final LiveData<Resource<NhaMang>> nhaMang;
    private final LiveData<Resource<Tram>> resultUpdateTram;
    private final LiveData<Resource<List<UserBTS>>> listUser;
    private final LiveData<Resource<NhaTram>> resultDeleteNhaTram;
    @Inject
    TramViewModel(TramRepository tramRepository){
        tram= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getTram(idTram,token.getValue());
            }
        });
        user= Transformations.switchMap(idUser, idUser->{
            if(idUser==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getUser(idUser,token.getValue());
            }
        });
        listNhaTram= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getListNhaTram(idTram,token.getValue());
            }
        });
        listNhaMang= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getListNhaMang(token.getValue());
            }
        });
        nhaTram= Transformations.switchMap(idNhaTram, idNhaTram->{
            if(idNhaTram==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getNhaTram(idNhaTram,token.getValue());
            }
        });
        nhaMang= Transformations.switchMap(idNhaMang, idNhaMang->{
            if(idNhaMang==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getNhaMang(idNhaMang,token.getValue());
            }
        });
        resultUpdateTram= Transformations.switchMap(tramUpdate, tramUpdate->{
            if(tramUpdate==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.updateTram(tramUpdate,token.getValue());
            }
        });
        listUser= Transformations.switchMap(updateQuanLy, updateQuanLy->{
            if(updateQuanLy==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.getAllUser(token.getValue());
            }
        });
        resultDeleteNhaTram= Transformations.switchMap(deleteNhaTram, deleteNhaTram->{
            if(deleteNhaTram==null){
                return AbsentLiveData.create();

            }else{
                return tramRepository.deleteNhaTram(deleteNhaTram,token.getValue());
            }
        });

    }
    public LiveData<Resource<Tram>> getTram(){
        return this.tram;
    }
    public LiveData<Resource<UserBTS>> getUser(){
        return this.user;
    }
    public LiveData<Resource<List<NhaTram>>> getListNhaTram(){
        return this.listNhaTram;
    }
    public LiveData<Resource<List<NhaMang>>> getListNhaMang(){
        return this.listNhaMang;
    }
    public LiveData<Resource<NhaTram>> getNhaTram(){
        return this.nhaTram;
    }
    public LiveData<Resource<NhaMang>> getNhaMang(){
        return this.nhaMang;
    }
    public LiveData<Resource<Tram>> getResultUpdateTram(){
        return this.resultUpdateTram;
    }
    public LiveData<Resource<List<UserBTS>>> getAllUser(){
        return this.listUser;
    }
    public LiveData<Resource<NhaTram>> getResultDeleteNhaTram(){
        return this.resultDeleteNhaTram;
    }
    public void setTram(int idTram,String token){
        this.token.setValue(token);
        this.idTram.setValue(idTram);
    }
    public void setUser(String idUser){
        this.idUser.setValue(idUser);
    }
    public void setNhaTram(int idNhaTram){
        this.idNhaTram.setValue(idNhaTram);
    }
    public void setNhaMang(int idNhaMang){
        this.idNhaMang.setValue(idNhaMang);
    }
    public void setUpdateTram(Tram tram){
        this.tramUpdate.setValue(tram);
    }
    public void setUpdateQuanLy(Boolean quanLy){
        this.updateQuanLy.setValue(quanLy);
    }
    public void setDeleteNhaTram(NhaTram nhaTram){
        this.deleteNhaTram.setValue(nhaTram);
    }
}
