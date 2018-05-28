package com.example.lequa.btsmanagement.ui.dsmang;

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
import com.example.lequa.btsmanagement.databinding.FragmentDsNhaMangBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import java.util.Collections;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DSMangFragment extends Fragment implements Injectable {
    private static final String DS_MANG_TOKEN_KEY = "ds_mang_token";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private DSMangViewModel dsMangViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentDsNhaMangBinding> dsMangBinding;
    AutoClearedValue<DSMangAdapter> adapter;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dsMangViewModel= ViewModelProviders.of(this,viewModelFactory).get(DSMangViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        dsMangViewModel.setToken(getArguments().getString(DS_MANG_TOKEN_KEY));
        DSMangAdapter dsMangAdapter=new DSMangAdapter(dataBindingComponent,
                nhaMang ->navigate(nhaMang.getIDNhaMang()));
        this.adapter=new AutoClearedValue<>(this,dsMangAdapter);
        dsMangBinding.get().listNhaMang.setAdapter(dsMangAdapter);

        initNhaMangList(dsMangViewModel);

    }
    public void navigate(int idNhaMang){

    }
    public void initNhaMangList(DSMangViewModel dsMangViewModel){
        dsMangViewModel.getListNhaMang().observe(this,listNhaMang->{
            if (listNhaMang != null && listNhaMang.data != null) {
                adapter.get().replace(listNhaMang.data);
            } else {
                //noinspection ConstantConditions
                adapter.get().replace(Collections.emptyList());
            }
        });
    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentDsNhaMangBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_ds_nha_mang, container, false,dataBindingComponent);
        dsMangBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static DSMangFragment create(String token) {
        DSMangFragment dsMangFragment = new DSMangFragment();
        Bundle args = new Bundle();
        args.putString(DS_MANG_TOKEN_KEY, token);
        dsMangFragment.setArguments(args);
        return dsMangFragment;
    }
}
