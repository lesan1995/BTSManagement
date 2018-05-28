package com.example.lequa.btsmanagement.ui.addmatdien;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.MatDien;
import com.example.lequa.btsmanagement.repository.AddMatDienRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;

public class AddMatDienViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<MatDien> matDien= new MutableLiveData<>();
    private final LiveData<Resource<MatDien>> resultInsertMatDien;
    @Inject
    public AddMatDienViewModel(AddMatDienRepository addMatDienRepository){
        resultInsertMatDien= Transformations.switchMap(matDien, matDien->{
            if(matDien==null){
                return AbsentLiveData.create();

            }else{
                return addMatDienRepository.addMatDien(matDien,token.getValue());
            }
        });
    }
    public LiveData<Resource<MatDien>> getResultInsertMatDien(){
        return this.resultInsertMatDien;
    }
    public void setInsertMatDien(String token,MatDien matDien){
        this.token.setValue(token);
        this.matDien.setValue(matDien);
    }
}
