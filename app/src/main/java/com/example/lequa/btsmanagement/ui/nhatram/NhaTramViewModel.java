package com.example.lequa.btsmanagement.ui.nhatram;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.repository.NhaTramRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;
import java.util.List;
import javax.inject.Inject;

public class NhaTramViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idTram = new MutableLiveData<>();
    final MutableLiveData<Integer> idNhaTram = new MutableLiveData<>();
    final MutableLiveData<Integer> idNhaMang = new MutableLiveData<>();
    final MutableLiveData<NhaTram> nhaTramUpdate = new MutableLiveData<>();
    final MutableLiveData<NhaTram> thongSoUpdate = new MutableLiveData<>();
    final MutableLiveData<Boolean> updateNhaMang = new MutableLiveData<>();
    private final LiveData<Resource<Tram>> tram;
    private final LiveData<Resource<NhaTram>> nhaTram;
    private final LiveData<Resource<NhaMang>> nhaMang;
    private final LiveData<Resource<List<NhaMang>>> listNhaMang;
    private final LiveData<Resource<NhaTram>> resultUpdateNhaTram;
    private final LiveData<Resource<NhaTram>> resultUpdateThongSo;
    @Inject
    NhaTramViewModel(NhaTramRepository nhaTramRepository){
        tram= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return nhaTramRepository.getTram(idTram,token.getValue());
            }
        });
        nhaTram= Transformations.switchMap(idNhaTram, idNhaTram->{
            if(idNhaTram==null){
                return AbsentLiveData.create();

            }else{
                return nhaTramRepository.getNhaTram(idNhaTram,token.getValue());
            }
        });
        nhaMang= Transformations.switchMap(idNhaMang, idNhaMang->{
            if(idNhaMang==null){
                return AbsentLiveData.create();

            }else{
                return nhaTramRepository.getNhaMang(idNhaMang,token.getValue());
            }
        });
        listNhaMang= Transformations.switchMap(updateNhaMang, updateNhaMang->{
            if(updateNhaMang==null){
                return AbsentLiveData.create();

            }else{
                return nhaTramRepository.getListNhaMang(token.getValue());
            }
        });
        resultUpdateNhaTram= Transformations.switchMap(nhaTramUpdate, nhaTramUpdate->{
            if(nhaTramUpdate==null){
                return AbsentLiveData.create();

            }else{
                return nhaTramRepository.updateNhaTram(nhaTramUpdate,token.getValue());
            }
        });
        resultUpdateThongSo= Transformations.switchMap(thongSoUpdate, thongSoUpdate->{
            if(thongSoUpdate==null){
                return AbsentLiveData.create();

            }else{
                return nhaTramRepository.updateThongSo(thongSoUpdate,token.getValue());
            }
        });
    }
    public LiveData<Resource<Tram>> getTram(){
        return this.tram;
    }
    public LiveData<Resource<NhaTram>> getNhaTram(){
        return this.nhaTram;
    }
    public LiveData<Resource<NhaMang>> getNhaMang(){
        return this.nhaMang;
    }
    public LiveData<Resource<List<NhaMang>>> getListNhaMang(){
        return this.listNhaMang;
    }
    public LiveData<Resource<NhaTram>> getResultUpdateNhaTram(){
        return this.resultUpdateNhaTram;
    }
    public LiveData<Resource<NhaTram>> getResultUpdateThongSo(){
        return this.resultUpdateThongSo;
    }
    public void setNhaTram(int idNhaTram,String token){
        this.token.setValue(token);
        this.idNhaTram.setValue(idNhaTram);
    }
    public void setTram(int idTram){
        this.idTram.setValue(idTram);
    }
    public void setNhaMang(int idNhaMang){
        this.idNhaMang.setValue(idNhaMang);
    }
    public void setUpdateNhaTram(NhaTram nhaTram){
        this.nhaTramUpdate.setValue(nhaTram);
    }
    public void setUpdateNhaMang(Boolean nhaMang){
        this.updateNhaMang.setValue(nhaMang);
    }
    public void setUpdateThongSo(NhaTram nhaTram){
        this.thongSoUpdate.setValue(nhaTram);
    }

}
