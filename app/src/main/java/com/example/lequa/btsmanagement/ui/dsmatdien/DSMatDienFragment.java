package com.example.lequa.btsmanagement.ui.dsmatdien;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
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
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentDsMatDienBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;
import butterknife.ButterKnife;

public class DSMatDienFragment extends Fragment implements Injectable {
    private static final String DS_MD_TOKEN_KEY = "ds_md_token";
    private static final String DS_MD_IDTRAM_KEY = "ds_md_idtram";
    private static final String DS_MD_CV_KEY = "ds_md_cv";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;
    private DSMatDienViewModel dsMatDienViewModel;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentDsMatDienBinding> dsMatDienBinding;
    AutoClearedValue<DSMatDienAdapter> adapter;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dsMatDienViewModel= ViewModelProviders.of(this,viewModelFactory).get(DSMatDienViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        dsMatDienViewModel.setMatDien(Integer.parseInt(getArguments().getString(DS_MD_IDTRAM_KEY)),
                getArguments().getString(DS_MD_TOKEN_KEY));
        DSMatDienAdapter dsMatDienAdapter=new DSMatDienAdapter(dataBindingComponent,
                v-> navigationController.navigateToChiTietMatDien(v.getIDMatDien()+"",getArguments().getString(DS_MD_TOKEN_KEY)));
        this.adapter=new AutoClearedValue<>(this,dsMatDienAdapter);
        dsMatDienBinding.get().listMatDien.setAdapter(dsMatDienAdapter);
        initDSMatDienList(dsMatDienViewModel);
        dsMatDienBinding.get().fabThemMatDien.setOnClickListener(v->{
            navigationController.navigateToAddMatDien(getArguments().getString(DS_MD_IDTRAM_KEY),getArguments().getString(DS_MD_TOKEN_KEY));
        });
        phanQuyenGiaoDien();
    }
    public void phanQuyenGiaoDien(){
        if(getArguments().getString(DS_MD_CV_KEY).equals("Admin")){
            dsMatDienBinding.get().fabThemMatDien.setVisibility(View.GONE);
        }
        else if(getArguments().getString(DS_MD_CV_KEY).equals("QuanLy")){
        }
    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoat();
            }
        });
    }
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    public void initDSMatDienList(DSMatDienViewModel dsMatDienViewModel){
        dsMatDienViewModel.getListMatDien().observe(this,listMatDien->{
            if (listMatDien != null && listMatDien.data != null) {
                adapter.get().replace(listMatDien.data);
            } else {
                //noinspection ConstantConditions
                adapter.get().replace(Collections.emptyList());
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentDsMatDienBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_ds_mat_dien, container, false,dataBindingComponent);
        dsMatDienBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static DSMatDienFragment create(String idTram,String cv,String token) {
        DSMatDienFragment dsMatDienFragment = new DSMatDienFragment();
        Bundle args = new Bundle();
        args.putString(DS_MD_TOKEN_KEY, token);
        args.putString(DS_MD_IDTRAM_KEY, idTram);
        args.putString(DS_MD_CV_KEY, cv);
        dsMatDienFragment.setArguments(args);
        return dsMatDienFragment;
    }
    public static String getDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            date = format.parse(dateStr);
            c.setTime(date);
            return c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "01-01-1990";
    }
    public static String getTime(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            date = format.parse(dateStr);
            c.setTime(date);
            String hour=c.get(Calendar.HOUR_OF_DAY)+"";
            if(hour.length()==1) hour="0"+hour;
            String minute=c.get(Calendar.MINUTE)+"";
            if(minute.length()==1) minute="0"+minute;
            return hour+":"+minute;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00";
    }
}
