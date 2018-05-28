package com.example.lequa.btsmanagement.ui.addnhatram;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.repository.AddNhaTramRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class AddNhaTramViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<NhaTram> nhaTram= new MutableLiveData<>();
    final MutableLiveData<Boolean> displayListNhaMang = new MutableLiveData<>();
    private final LiveData<Resource<List<NhaMang>>> listNhaMang;
    private final LiveData<Resource<NhaTram>> resultInsertNhaTram;
    @Inject
    AddNhaTramViewModel(AddNhaTramRepository addNhaTramRepository){
        listNhaMang= Transformations.switchMap(displayListNhaMang, displayListNhaMang->{
            if(displayListNhaMang==null){
                return AbsentLiveData.create();

            }else{
                return addNhaTramRepository.getListNhaMang(token.getValue());
            }
        });
        resultInsertNhaTram= Transformations.switchMap(nhaTram, nhaTram->{
            if(nhaTram==null){
                return AbsentLiveData.create();

            }else{
                return addNhaTramRepository.addNhaTram(nhaTram,token.getValue());
            }
        });
    }
    public LiveData<Resource<List<NhaMang>>> getListNhaMang(){
        return this.listNhaMang;
    }
    public LiveData<Resource<NhaTram>> getResultInsertNhaTram(){
        return this.resultInsertNhaTram;
    }
    public void setDisplayListNhaMang(String token,Boolean nhaMang){
        this.token.setValue(token);
        this.displayListNhaMang.setValue(nhaMang);
    }
    public void setInsertNhaTram(NhaTram nhaTram){
        this.nhaTram.setValue(nhaTram);
    }
}
