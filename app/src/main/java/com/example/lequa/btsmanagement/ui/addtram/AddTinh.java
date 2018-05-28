package com.example.lequa.btsmanagement.ui.addtram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.tinh.Quan;
import com.example.lequa.btsmanagement.tinh.Tinh;
import com.example.lequa.btsmanagement.tinh.TinhUtil;
import com.example.lequa.btsmanagement.tinh.Xa;
import com.example.lequa.btsmanagement.ui.edit.QuanAdapter;
import com.example.lequa.btsmanagement.ui.edit.TinhAdapter;
import com.example.lequa.btsmanagement.ui.edit.XaAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTinh extends Activity {
    @BindView(R.id.spTinh) Spinner spTinh;
    @BindView(R.id.spQuan) Spinner spQuan;
    @BindView(R.id.spXa) Spinner spXa;
    TinhUtil tinhUtil;
    List<Tinh> listTinh;
    List<Quan> listQuan;
    List<Xa> listXa;
    String codeResult,diaChi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_edit_tinh);
        ButterKnife.bind(this);
        init();
        setSpinnerTinh();
    }
    public void init(){
        tinhUtil=new TinhUtil(this.getApplication());
        listTinh=tinhUtil.getListTinh();Collections.sort(listTinh);
        listQuan=tinhUtil.getListQuan();Collections.sort(listQuan);
        listXa=tinhUtil.getListXa();Collections.sort(listXa);
    }
    public void setSpinnerTinh(){
        TinhAdapter tinhAdapter=new TinhAdapter(this,listTinh);
        spTinh.setAdapter(tinhAdapter);
        spTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSpinnerQuan(listTinh.get(i).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void setSpinnerQuan(String codeTinh){
        List<Quan> listQuan=getListQuan(codeTinh);
        QuanAdapter quanAdapter=new QuanAdapter(this,listQuan);
        spQuan.setAdapter(quanAdapter);
        spQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSpinnerXa(listQuan.get(i).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void setSpinnerXa(String codeQuan){
        List<Xa> listXa=getListXa(codeQuan);
        XaAdapter xaAdapter=new XaAdapter(this,listXa);
        spXa.setAdapter(xaAdapter);
        spXa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                codeResult=listXa.get(i).getCode();
                diaChi=listXa.get(i).getPath();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public List<Quan> getListQuan(String codeTinh){
        List<Quan> result=new ArrayList<>();
        for (Quan quan:listQuan) {
            if(quan.getParentCode().equals(codeTinh)) result.add(quan);
        }
        return result;
    }
    public List<Xa> getListXa(String codeQuan){
        List<Xa> result=new ArrayList<>();
        for (Xa xa:listXa) {
            if(xa.getParentCode().equals(codeQuan)) result.add(xa);
        }
        return result;
    }
    @OnClick(R.id.btnThoatEditTinh)
    public void Thoat(){
        this.finish();
    }
    @OnClick(R.id.btnLuuEditTinh)
    public void Luu(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("codeResult",codeResult);
        returnIntent.putExtra("diaChi",diaChi);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}

