package com.example.lequa.btsmanagement.ui.nhatky;

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
import com.example.lequa.btsmanagement.databinding.FragmentNhatKyBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class NhatKyFragment extends Fragment implements Injectable {
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentNhatKyBinding> nhatKyBinding;
    private static final String NK_TOKEN_KEY = "nk_token";
    private static final String NK_ID_KEY = "nk_id";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private NhatKyViewModel nhatKyViewModel;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nhatKyViewModel= ViewModelProviders.of(this,viewModelFactory).get(NhatKyViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        nhatKyViewModel.setIDNhatKy(Integer.parseInt(getArguments().getString(NK_ID_KEY)), getArguments().getString(NK_TOKEN_KEY));
        nhatKyViewModel.getNhatKyResult().observe(this, result -> {
            if (result.status== Status.SUCCESS)
                nhatKyBinding.get().setNhatKy(result.data);
                if(result.data!=null) nhatKyViewModel.setNhatKy(result.data);
        });
        nhatKyViewModel.getUserResult().observe(this, result -> {
            if (result.status== Status.SUCCESS)
                nhatKyBinding.get().setUser(result.data);
        });
        nhatKyViewModel.getTramResult().observe(this, result -> {
            if (result.status== Status.SUCCESS)
                nhatKyBinding.get().setTram(result.data);
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
        FragmentNhatKyBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_nhat_ky, container, false,dataBindingComponent);
        nhatKyBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static NhatKyFragment create(String idNhatKy,String token) {
        NhatKyFragment nhatKyFragment = new NhatKyFragment();
        Bundle args = new Bundle();
        args.putString(NK_TOKEN_KEY, token);
        args.putString(NK_ID_KEY, idNhatKy);
        nhatKyFragment.setArguments(args);
        return nhatKyFragment;
    }
}
