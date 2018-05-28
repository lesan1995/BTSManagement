package com.example.lequa.btsmanagement.ui.chitietmatdien;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.MatDien;
import com.example.lequa.btsmanagement.repository.MatDienRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;

public class ChiTietMatDienViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idMatDien = new MutableLiveData<>();
    private final LiveData<Resource<MatDien>> matDien;
    @Inject
    public ChiTietMatDienViewModel(MatDienRepository matDienRepository){
        matDien= Transformations.switchMap(idMatDien, idMatDien->{
            if(idMatDien==null){
                return AbsentLiveData.create();

            }else{
                return matDienRepository.getMatDien(idMatDien,token.getValue());
            }
        });
    }
    public LiveData<Resource<MatDien>> getMatDien(){
        return this.matDien;
    }
    public void setMatDien(int idTram,String token){
        this.token.setValue(token);
        this.idMatDien.setValue(idTram);
    }
}
