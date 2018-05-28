package com.example.lequa.btsmanagement.ui.addnhatky;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.repository.AddNhatKyRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import javax.inject.Inject;

public class AddNhatKyViewModel extends ViewModel{
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<NhatKy> nhatKy= new MutableLiveData<>();
    private final LiveData<Resource<NhatKy>> resultInsertNhatKy;
    @Inject
    AddNhatKyViewModel(AddNhatKyRepository addNhatKyRepository){
        resultInsertNhatKy= Transformations.switchMap(nhatKy, nhatKy->{
            if(nhatKy==null){
                return AbsentLiveData.create();

            }else{
                return addNhatKyRepository.addNhatKy(nhatKy,token.getValue());
            }
        });
    }
    public LiveData<Resource<NhatKy>> getResultInsertNhatKy(){
        return this.resultInsertNhatKy;
    }
    public void setNhatKy(NhatKy nhatKy,String token){
        this.token.setValue(token);
        this.nhatKy.setValue(nhatKy);

    }
}
