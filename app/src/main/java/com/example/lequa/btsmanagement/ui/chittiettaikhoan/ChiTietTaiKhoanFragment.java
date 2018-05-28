package com.example.lequa.btsmanagement.ui.chittiettaikhoan;

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
import com.example.lequa.btsmanagement.databinding.FragmentTaiKhoanBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.databinding.FragmentChiTietTaiKhoanBinding;
import com.example.lequa.btsmanagement.ui.taikhoan.TaiKhoanFragment;
import com.example.lequa.btsmanagement.ui.taikhoan.TaiKhoanViewModel;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ChiTietTaiKhoanFragment extends Fragment implements Injectable {
    private static final String CT_TAI_KHOAN_TOKEN_KEY = "ct_tai_khoan_token";
    private static final String CT_TAI_KHOAN_ID_USER_KEY = "ct_id_user_token";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private ChiTietTaiKhoanViewModel chiTietTaiKhoanViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentChiTietTaiKhoanBinding> chitietTaiKhoanBinding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chiTietTaiKhoanViewModel= ViewModelProviders.of(this,viewModelFactory).get(ChiTietTaiKhoanViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarTaiKhoan);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        chiTietTaiKhoanViewModel.setUser(getArguments().getString(CT_TAI_KHOAN_ID_USER_KEY),
                getArguments().getString(CT_TAI_KHOAN_TOKEN_KEY));
        chiTietTaiKhoanViewModel.getUser().observe(this,user->{
            if(user.status== Status.SUCCESS)
            chitietTaiKhoanBinding.get().setUser(user.data);
            //chitietTaiKhoanBinding.get().tvDiaChi.setText(user.data.getDiaChi().substring(0,user.data.getDiaChi().indexOf("|")));
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
        FragmentChiTietTaiKhoanBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_chi_tiet_tai_khoan, container, false,dataBindingComponent);
        chitietTaiKhoanBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static ChiTietTaiKhoanFragment create(String idUser,String token) {
        ChiTietTaiKhoanFragment chiTietTaiKhoanFragment = new ChiTietTaiKhoanFragment();
        Bundle args = new Bundle();
        args.putString(CT_TAI_KHOAN_TOKEN_KEY, token);
        args.putString(CT_TAI_KHOAN_ID_USER_KEY, idUser);
        chiTietTaiKhoanFragment.setArguments(args);
        return chiTietTaiKhoanFragment;
    }
}
