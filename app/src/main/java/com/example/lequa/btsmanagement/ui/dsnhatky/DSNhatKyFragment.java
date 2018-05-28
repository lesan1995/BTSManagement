package com.example.lequa.btsmanagement.ui.dsnhatky;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentDsNhatKyBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.dsnhatky.DSNhatKyAdapter;
import com.example.lequa.btsmanagement.ui.dsnhatky.DSNhatKyViewModel;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;
import butterknife.ButterKnife;

public class DSNhatKyFragment extends Fragment implements Injectable {
    private static final String DS_NK_TOKEN_KEY = "ds_nk_token";
    private static final String DS_NK_IDTRAM_KEY = "ds_nk_idtram";
    private static final String DS_NK_CV_KEY = "ds_nk_cv";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;
    private DSNhatKyViewModel dsNhatKyViewModel;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentDsNhatKyBinding> dsNhatKyBinding;
    AutoClearedValue<DSNhatKyAdapter> adapter;
    private String query="TatCa";
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        dsNhatKyViewModel= ViewModelProviders.of(this,viewModelFactory).get(DSNhatKyViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        dsNhatKyViewModel.setNhatKy(Integer.parseInt(getArguments().getString(DS_NK_IDTRAM_KEY)),
                getArguments().getString(DS_NK_TOKEN_KEY));
        dsNhatKyViewModel.setQuery("TatCa");
        DSNhatKyAdapter dsNhatKyAdapter=new DSNhatKyAdapter(dataBindingComponent,
                v-> navigationController.navigateToNhatKy(v.getIDNhatKy()+"",getArguments().getString(DS_NK_TOKEN_KEY)),
                nhatKy-> updateNhatKy(nhatKy));
        this.adapter=new AutoClearedValue<>(this,dsNhatKyAdapter);
        dsNhatKyBinding.get().listNhatKy.setAdapter(dsNhatKyAdapter);
        initDSNhatKyList(dsNhatKyViewModel);
        dsNhatKyBinding.get().fabThemNhatKy.setOnClickListener(v->{
            navigationController.navigateToAddNhatKy(getArguments().getString(DS_NK_IDTRAM_KEY),getArguments().getString(DS_NK_TOKEN_KEY));
        });
        phanQuyenGiaoDien();

        dsNhatKyViewModel.getResultUpdate().observe(this,result->{
            if(result.status== Status.SUCCESS){
                dsNhatKyViewModel.setQuery(query);
            }
            else if(result.status== Status.ERROR){
                Toast.makeText(getActivity().getApplicationContext(),"Lỗi xảy ra "+result.message,Toast.LENGTH_LONG).show();
            }
        });

    }
    public void phanQuyenGiaoDien(){
        if(getArguments().getString(DS_NK_CV_KEY).equals("Admin")){
            dsNhatKyBinding.get().fabThemNhatKy.setVisibility(View.GONE);
        }
        else if(getArguments().getString(DS_NK_CV_KEY).equals("QuanLy")){
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nk_tram, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nhatky:
                query="NhatKy";
                dsNhatKyViewModel.setQuery("NhatKy");
                return true;
            case R.id.action_suco:
                query="SuCo";
                dsNhatKyViewModel.setQuery("SuCo");
                return true;
            case R.id.action_tatca:
                query="All";
                dsNhatKyViewModel.setQuery("All");
            default:
                break;
        }

        return false;
    }
    public void initDSNhatKyList(DSNhatKyViewModel dsNhatKyViewModel){
        dsNhatKyViewModel.getListNhatKy().observe(this,listNhatKy->{
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
        dsNhatKyBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static DSNhatKyFragment create(String idTram, String cv, String token) {
        DSNhatKyFragment dsNhatKyFragment = new DSNhatKyFragment();
        Bundle args = new Bundle();
        args.putString(DS_NK_TOKEN_KEY, token);
        args.putString(DS_NK_IDTRAM_KEY, idTram);
        args.putString(DS_NK_CV_KEY, cv);
        dsNhatKyFragment.setArguments(args);
        return dsNhatKyFragment;
    }
    public void updateNhatKy(NhatKy nhatKy){
        if(!getArguments().getString(DS_NK_CV_KEY).equals("QuanLy")) return;
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Thay đổi Trạng Thái")
                .setMessage("Bạn muốn thay đổi trạng thái báo cáo này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                         nhatKy.setDaGiaiQuyet(!nhatKy.getDaGiaiQuyet());
                        dsNhatKyViewModel.setUpdate(nhatKy);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
