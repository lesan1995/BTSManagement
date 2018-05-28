package com.example.lequa.btsmanagement.ui.dstram;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.repository.DSTramRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class DSTramViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<String> query = new MutableLiveData<>();
    private final LiveData<Resource<List<Tram>>> listTram;
    @Inject
    public DSTramViewModel(DSTramRepository dsTramRepository){
        listTram= Transformations.switchMap(query, query->{
            if(query==null){
                return AbsentLiveData.create();

            }else{
                return dsTramRepository.getListTram(token.getValue(),query);
            }
        });
    }
    public LiveData<Resource<List<Tram>>> getListTram(){
        return this.listTram;
    }
    public void setToken(String token){
        this.token.setValue(token);
    }
    public void setQuery(String query){
        this.query.setValue(query);
    }
}
