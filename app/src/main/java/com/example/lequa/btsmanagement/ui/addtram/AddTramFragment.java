package com.example.lequa.btsmanagement.ui.addtram;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.FragmentAddTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.edit.UserAdapter;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTramFragment extends Fragment implements Injectable {
    private static final String ADD_TRAM_TOKEN_KEY = "add_tram_token";
    AutoClearedValue<FragmentAddTramBinding> addTramBinding;
    @Inject
    NavigationController navigationController;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AddTramViewModel addTramViewModel;

    @BindView(R.id.spQuanLy) Spinner spQuanLy;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addTramViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddTramViewModel.class);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarAddTram);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);

        addTramViewModel.setDisplayListUser(getArguments().getString(ADD_TRAM_TOKEN_KEY),true);
        addTramViewModel.getListUser().observe(this,listUser->{
            if(listUser.status== Status.SUCCESS){
                spQuanLy.setAdapter(new UserAdapter(getContext(),listUser.data));
            }
        });
        addTramViewModel.getResultInsertTram().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1) {
                    Toast.makeText(getActivity().getApplicationContext(),"Thêm Thành Công",Toast.LENGTH_LONG).show();
                    thoat();
                }
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Lỗi Không thể thêm "+result.message,Toast.LENGTH_LONG).show();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddTramBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_tram, container, false);
        addTramBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoat();
            }
        });
    }
    @OnClick(R.id.btnThoatAddTram)
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    @OnClick(R.id.btnLuuToaDo)
    public void luu(){
        if(!checkData()) return;
        Tram tram=new Tram();
        tram.setIDTram(0);
        tram.setTenTram(addTramBinding.get().edTen.getText().toString());
        tram.setTinh(addTramBinding.get().getMatinh());
        tram.setKinhDo(addTramBinding.get().getKinhDo());
        tram.setViDo(addTramBinding.get().getViDo());
        UserBTS userBTS=((UserAdapter)spQuanLy.getAdapter()).getListData().get(spQuanLy.getSelectedItemPosition());
        tram.setIDQuanLy(userBTS.getIDUser());
        tram.setBanKinhPhuSong(Double.parseDouble(addTramBinding.get().edBanKinh.getText().toString()));
        tram.setCotAnten(addTramBinding.get().sbCotAnten.getProgress());
        tram.setCotTiepDat(addTramBinding.get().sbCotTiepDat.getProgress());
        tram.setSanTram(addTramBinding.get().sbSanTram.getProgress());
        addTramViewModel.setInsertTram(tram);
    }
    public static AddTramFragment create(String token) {
        AddTramFragment addTramFragment = new AddTramFragment();
        Bundle args = new Bundle();
        args.putString(ADD_TRAM_TOKEN_KEY, token);
        addTramFragment.setArguments(args);
        return addTramFragment;
    }
    @OnClick(R.id.btnTinh)
    public void showAddTinh(){
        Intent myIntent = new Intent(getActivity(), AddTinh.class);
        getActivity().startActivityForResult(myIntent,3);
    }
    @OnClick(R.id.btnAddToaDo)
    public void showAddToaDo(){
//        Intent myIntent = new Intent(getActivity(), AddToaDo.class);
//        getActivity().startActivityForResult(myIntent,4);
        navigationController.navigateToToaDo(getArguments().getString(ADD_TRAM_TOKEN_KEY));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                addTramBinding.get().setMatinh(data.getStringExtra("codeResult"));
                addTramBinding.get().tvTinh.setText(data.getStringExtra("diaChi"));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }else if (requestCode == 4) {
            if(resultCode == Activity.RESULT_OK){
                addTramBinding.get().setKinhDo(data.getDoubleExtra("KinhDo",108.154008));
                addTramBinding.get().setViDo(data.getDoubleExtra("ViDo",16.077809));
                addTramBinding.get().tvToaDo.setText(data.getDoubleExtra("ViDo",16.077809)+" ,"+
                        data.getDoubleExtra("KinhDo",108.154008));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    public Boolean checkData(){
        if(addTramBinding.get().edTen.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập tên Trạm",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTramBinding.get().getMatinh()==null){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa chọn địa chỉ trạm",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTramBinding.get().getKinhDo()==null){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa chọn vị trí trạm",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTramBinding.get().edBanKinh.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập bán kính trạm",Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            ((UserAdapter)spQuanLy.getAdapter()).getListData().get(spQuanLy.getSelectedItemPosition());
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa chọn người quản lý",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
