package com.example.lequa.btsmanagement.tinh;

import android.app.Application;
import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TinhUtil {
    Gson gson=new Gson();
    private HashMap<String,Object> hashMapTinh;
    private List<Tinh> listTinh;
    private HashMap<String,Object> hashMapQuan;
    private List<Quan> listQuan;
    private HashMap<String,Object> hashMapXa;
    private List<Xa> listXa;
    private Context context;
    @Inject
    public TinhUtil(Application application){
        this.context=application.getApplicationContext();
        listTinh=new ArrayList<>();
        listQuan=new ArrayList<>();
        listXa=new ArrayList<>();
        init();
    }
    public void init(){
        try {

            InputStream insTinh = context.getResources().openRawResource(
                    context.getResources().getIdentifier("tinh_tp",
                            "raw", context.getPackageName()));
            hashMapTinh = new ObjectMapper().readValue(insTinh, HashMap.class);

            String jsonTinh = gson.toJson(hashMapTinh.values());
            TypeToken<List<Tinh>> listTinhType = new TypeToken<List<Tinh>>(){};
            listTinh = gson.fromJson(jsonTinh, listTinhType.getType());

            InputStream insQuan = context.getResources().openRawResource(
                    context.getResources().getIdentifier("quan_huyen",
                            "raw", context.getPackageName()));
            hashMapQuan = new ObjectMapper().readValue(insQuan, HashMap.class);

            String jsonQuan = gson.toJson(hashMapQuan.values());
            TypeToken<List<Quan>> listQuanType = new TypeToken<List<Quan>>(){};
            listQuan = gson.fromJson(jsonQuan, listQuanType.getType());


            InputStream insXa = context.getResources().openRawResource(
                    context.getResources().getIdentifier("xa_phuong",
                            "raw", context.getPackageName()));
            hashMapXa = new ObjectMapper().readValue(insXa, HashMap.class);

            String jsonXa = gson.toJson(hashMapXa.values());
            TypeToken<List<Xa>> listXaType = new TypeToken<List<Xa>>(){};
            listXa = gson.fromJson(jsonXa, listXaType.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Tinh> getListTinh(){
        return listTinh;
    }
    public Tinh getTinh(String code){
        String json = gson.toJson(hashMapTinh.get(code));
        return gson.fromJson(json,Tinh.class);
    }
    public List<Quan> getListQuan(){
        return listQuan;
    }
    public Quan getQuan(String code){
        String json = gson.toJson(hashMapQuan.get(code));
        return gson.fromJson(json,Quan.class);
    }
    public List<Xa> getListXa(){
        return listXa;
    }
    public Xa getXa(String code){
        String json = gson.toJson(hashMapXa.get(code));
        return gson.fromJson(json,Xa.class);
    }
}
