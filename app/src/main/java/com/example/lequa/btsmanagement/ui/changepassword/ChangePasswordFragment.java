package com.example.lequa.btsmanagement.ui.changepassword;

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
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentChangePasswordBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.Password;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordFragment extends Fragment implements Injectable {
    private static final String CHANGE_PASSWORD_TOKEN_KEY = "change_password_token";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private ChangePasswordViewModel changePasswordViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentChangePasswordBinding> changePasswordBinding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changePasswordViewModel= ViewModelProviders.of(this,viewModelFactory).get(ChangePasswordViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);

        changePasswordViewModel.getResult().observe(this,result->{
            if (result.status== Status.SUCCESS){
                Toast.makeText(getActivity().getApplicationContext(),"Thay đổi password thành công",Toast.LENGTH_LONG).show();
                navigationController.navigateToLogin(true);

            }
            else if (result.status== Status.ERROR){
                Toast.makeText(getActivity().getApplicationContext(),"Không thể thay đổi password "+result.message,Toast.LENGTH_LONG).show();

            }

        });

    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoat();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentChangePasswordBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_change_password, container, false,dataBindingComponent);
        changePasswordBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static ChangePasswordFragment create(String token) {
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(CHANGE_PASSWORD_TOKEN_KEY, token);
        changePasswordFragment.setArguments(args);
        return changePasswordFragment;
    }
    @OnClick(R.id.btnThoat)
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    @OnClick(R.id.btnLuu)
    public void changePassword(){
        if(!validate()) return;
        Password password=new Password();
        password.setOldPassword(changePasswordBinding.get().oldPassword.getText().toString());
        password.setNewPassword(changePasswordBinding.get().newPassword.getText().toString());
        password.setConfirmPassword(changePasswordBinding.get().confirmPassword.getText().toString());
        changePasswordViewModel.setPassword(getArguments().getString(CHANGE_PASSWORD_TOKEN_KEY),password);
    }
    public boolean validate(){
        if(changePasswordBinding.get().oldPassword.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập old password",Toast.LENGTH_LONG).show();
            return false;
        }
        if(changePasswordBinding.get().newPassword.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập new password",Toast.LENGTH_LONG).show();
            return false;
        }
        if(changePasswordBinding.get().confirmPassword.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập confirm password",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!changePasswordBinding.get().confirmPassword.getText().toString().equals(
                changePasswordBinding.get().newPassword.getText().toString()
        )) {
            Toast.makeText(getActivity().getApplicationContext(),"Confirm password không chính xác",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!validatePassword(changePasswordBinding.get().newPassword.getText().toString())){
            Toast.makeText(getActivity().getApplicationContext(),"Password không đủ mạnh",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
    public boolean validatePassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

}
