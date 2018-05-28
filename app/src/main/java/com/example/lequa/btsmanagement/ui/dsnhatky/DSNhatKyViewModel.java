package com.example.lequa.btsmanagement.ui.dsnhatky;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.repository.DSNhatKyRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class DSNhatKyViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idTram = new MutableLiveData<>();
    final MutableLiveData<String> query = new MutableLiveData<>();
    final MutableLiveData<NhatKy> nhatKy = new MutableLiveData<>();
    private final LiveData<Resource<List<NhatKy>>> listNhatKy;
    private final LiveData<Resource<NhatKy>> resultStatus;
    @Inject
    public DSNhatKyViewModel(DSNhatKyRepository dsNhatKyRepository){
        listNhatKy= Transformations.switchMap(query, query->{
            if(query==null){
                return AbsentLiveData.create();

            }else{
                return dsNhatKyRepository.getListNhatKy(query,idTram.getValue(),token.getValue());
            }
        });
        resultStatus= Transformations.switchMap(nhatKy, nhatKy->{
            if(nhatKy==null){
                return AbsentLiveData.create();

            }else{
                return dsNhatKyRepository.putNhatKy(nhatKy,token.getValue());
            }
        });
    }
    public LiveData<Resource<List<NhatKy>>> getListNhatKy(){
        return this.listNhatKy;
    }
    public LiveData<Resource<NhatKy>> getResultUpdate(){
        return this.resultStatus;
    }
    public void setNhatKy(int idTram,String token){
        this.token.setValue(token);
        this.idTram.setValue(idTram);
    }
    public void setQuery(String query){
        this.query.setValue(query);
    }
    public void setUpdate(NhatKy nhatKy){
        this.nhatKy.setValue(nhatKy);
    }
}
