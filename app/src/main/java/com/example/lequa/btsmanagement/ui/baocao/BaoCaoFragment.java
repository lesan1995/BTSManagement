package com.example.lequa.btsmanagement.ui.baocao;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentDsNhatKyBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.dsnhatky.DSNhatKyAdapter;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.inject.Inject;
import butterknife.ButterKnife;

public class BaoCaoFragment extends Fragment implements Injectable {
    private static final String DS_BC_TOKEN_KEY = "ds_bc_token";
    private static final String DS_BC_CV_KEY = "ds_bc_cv";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;
    private BaoCaoViewModel baoCaoViewModel;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentDsNhatKyBinding> dsBaoCaoBinding;
    AutoClearedValue<DSNhatKyAdapter> adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        baoCaoViewModel= ViewModelProviders.of(this,viewModelFactory).get(BaoCaoViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        dsBaoCaoBinding.get().tvTenToolbar.setText("Báo Cáo Sự Cố");
        setBack(toolbar);
        baoCaoViewModel.setNhatKy(getArguments().getString(DS_BC_TOKEN_KEY));
        baoCaoViewModel.setQuery("TatCa");
        DSNhatKyAdapter dsNhatKyAdapter=new DSNhatKyAdapter(dataBindingComponent,
                v-> navigationController.navigateToNhatKy(v.getIDNhatKy()+"",getArguments().getString(DS_BC_TOKEN_KEY)),
                nhatKy -> none(nhatKy));
        this.adapter=new AutoClearedValue<>(this,dsNhatKyAdapter);
        dsBaoCaoBinding.get().listNhatKy.setAdapter(dsNhatKyAdapter);
        initDSBaoCaoList(baoCaoViewModel);
        phanQuyenGiaoDien();
    }
    public void phanQuyenGiaoDien(){
        if(getArguments().getString(DS_BC_CV_KEY).equals("Admin")){
            dsBaoCaoBinding.get().fabThemNhatKy.setVisibility(View.GONE);
        }
        else if(getArguments().getString(DS_BC_CV_KEY).equals("QuanLy")){
        }
    }
    public void none(NhatKy nhatKy){

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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bc_tram, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hoanthanh:
                baoCaoViewModel.setQuery("HoanThanh");
                return true;
            case R.id.action_chuahoanthanh:
                baoCaoViewModel.setQuery("ChuaHoanThanh");
                return true;
            case R.id.action_suco:
                baoCaoViewModel.setQuery("All");
                return true;
            default:
                break;
        }

        return false;
    }
    public void initDSBaoCaoList(BaoCaoViewModel baoCaoViewModel){
        baoCaoViewModel.getListNhatKy().observe(this,listNhatKy->{
            if (listNhatKy != null && listNhatKy.data != null) {
                adapter.get().replace(listNhatKy.data);
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
        FragmentDsNhatKyBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_ds_nhat_ky, container, false,dataBindingComponent);
        dsBaoCaoBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static BaoCaoFragment create(String cv,String token) {
        BaoCaoFragment dsNhatKyFragment = new BaoCaoFragment();
        Bundle args = new Bundle();
        args.putString(DS_BC_TOKEN_KEY, token);
        args.putString(DS_BC_CV_KEY, cv);
        dsNhatKyFragment.setArguments(args);
        return dsNhatKyFragment;
    }
    public static String getDate(String dateStr){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            String dateTmp=dateStr.substring(0,dateStr.lastIndexOf("."));
            date = format.parse(dateTmp);
            c.setTime(date);
            String hour=c.get(Calendar.HOUR_OF_DAY)+"";
            if(hour.length()==1) hour="0"+hour;
            String minute=c.get(Calendar.MINUTE)+"";
            if(minute.length()==1) minute="0"+minute;
            return hour+":"+minute+" "+c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00 01-01-1990";
    }
}

