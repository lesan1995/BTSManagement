package com.example.lequa.btsmanagement.ui.dsmatdien;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.MatDien;
import com.example.lequa.btsmanagement.repository.DSMatDienRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class DSMatDienViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idTram = new MutableLiveData<>();
    private final LiveData<Resource<List<MatDien>>> listMatDien;
    @Inject
    public DSMatDienViewModel(DSMatDienRepository dsMatDienRepository){
        listMatDien= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return dsMatDienRepository.getListMatDien(idTram,token.getValue());
            }
        });
    }
    public LiveData<Resource<List<MatDien>>> getListMatDien(){
        return this.listMatDien;
    }
    public void setMatDien(int idTram,String token){
        this.token.setValue(token);
        this.idTram.setValue(idTram);
    }
}
