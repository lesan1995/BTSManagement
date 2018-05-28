package com.example.lequa.btsmanagement.ui.hinhanh;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.lequa.btsmanagement.model.HinhAnhTram;
import com.example.lequa.btsmanagement.repository.HinhAnhRepository;
import com.example.lequa.btsmanagement.util.AbsentLiveData;
import com.example.lequa.btsmanagement.vo.Resource;

import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;

public class HinhAnhViewModel extends ViewModel {
    final MutableLiveData<String> token = new MutableLiveData<>();
    final MutableLiveData<Integer> idTram = new MutableLiveData<>();
    final MutableLiveData<MultipartBody.Part> image = new MutableLiveData<>();
    final MutableLiveData<HinhAnhTram> deleteHinhAnhTram = new MutableLiveData<>();
    private final LiveData<Resource<List<HinhAnhTram>>> listHinhAnhTram;
    private final LiveData<Resource<HinhAnhTram>> resultUpload;
    private final LiveData<Resource<HinhAnhTram>> resultDeleteHinhAnhTram;
    @Inject
    public HinhAnhViewModel(HinhAnhRepository hinhAnhRepository){
        listHinhAnhTram= Transformations.switchMap(idTram, idTram->{
            if(idTram==null){
                return AbsentLiveData.create();

            }else{
                return hinhAnhRepository.getHinhAnhTramByIDTram(token.getValue(),idTram);
            }
        });
        resultUpload= Transformations.switchMap(image, image->{
            if(image==null){
                return AbsentLiveData.create();

            }else{
                return hinhAnhRepository.addHinhAnh(idTram.getValue(),image,token.getValue());
            }
        });
        resultDeleteHinhAnhTram= Transformations.switchMap(deleteHinhAnhTram, deleteHinhAnhTram->{
            if(deleteHinhAnhTram==null){
                return AbsentLiveData.create();

            }else{
                return hinhAnhRepository.deleteHinhAnh(deleteHinhAnhTram,token.getValue());
            }
        });
    }
    public LiveData<Resource<List<HinhAnhTram>>> getListHinhAnhTram(){
        return this.listHinhAnhTram;
    }
    public LiveData<Resource<HinhAnhTram>> getResultUpload(){
        return this.resultUpload;
    }
    public LiveData<Resource<HinhAnhTram>> getResultDeleteHinhAnhTram(){
        return this.resultDeleteHinhAnhTram;
    }
    public void setHinhAnh(String token,int idTram){
        this.token.setValue(token);
        this.idTram.setValue(idTram);
    }
    public void setUpload(MultipartBody.Part file){
        this.image.setValue(file);
    }
    public void setDeleteHinhAnhTram(HinhAnhTram hinhAnhTram){
        this.deleteHinhAnhTram.setValue(hinhAnhTram);
    }
}
