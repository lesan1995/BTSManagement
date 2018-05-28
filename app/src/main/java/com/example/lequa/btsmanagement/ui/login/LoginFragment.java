package com.example.lequa.btsmanagement.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.FragmentLoginBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.UserLogin;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class LoginFragment extends Fragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LoginViewModel viewModel;

    @Inject
    NavigationController navigationController;

    AutoClearedValue<FragmentLoginBinding> binding;

    private static final String LOGIN_DANG_XUAT_KEY = "login_dang_xuat";
    private String tokenStr,usernameStr;

    int tmpNull=0;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel=ViewModelProviders.of(this,viewModelFactory).get(LoginViewModel.class);
        if(getArguments().getBoolean(LOGIN_DANG_XUAT_KEY)){
            viewModel.setDeleteAllData(false);
        }
        viewModel.getResultDeleteAllData().observe(this,result->{
            if(result.status==Status.SUCCESS){

            }
        });
        viewModel.getLogin().observe(this,user->{
            tmpNull++;
            if(tmpNull>1){
                if(user.data!=null){
                    if(user.status==Status.SUCCESS){
                        Toast.makeText(getActivity().getApplicationContext(),"Login Success ",Toast.LENGTH_LONG).show();
                        setUserToken(user.data.getUserName(),user.data.getAccessToken());
                        viewModel.setUser(user.data.getUserName(),user.data.getAccessToken());
                       // navigationController.navigateToMain(user.data.getUserName(),user.data.getAccessToken());
                    }
                }
                else{
                    if(binding.get().getDisplayFirst()==true){
                        binding.get().setDisplayFirst(false);
                        binding.get().setDisplayInput(true);
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),"Login Fail",Toast.LENGTH_LONG).show();
                    }

                }
                tmpNull=0;
            }
        });
        viewModel.getUser().observe(this,result->{
            if(result.status==Status.SUCCESS){
                navigationController.navigateToMain(usernameStr,tokenStr,result.data.getChucVu());
            }

        });
        binding.get().btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSystem();
            }
        });
        binding.get().btnLoginFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setLogin(new UserLogin("", ""));
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLoginBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_login, container, false);
        binding = new AutoClearedValue<>(this, dataBinding);
        binding.get().setDisplayFirst(true);
        binding.get().setDisplayInput(false);
        return dataBinding.getRoot();
    }
    public void setUserToken(String usernameStr,String tokenStr){
        this.usernameStr=usernameStr;this.tokenStr=tokenStr;
    }
    public void loginSystem(){
        String email= binding.get().edUserName.getText().toString();
        String password=binding.get().edPassword.getText().toString();
        if(validateEmail(email)&&validatePassword(password))
            viewModel.setLogin(new UserLogin(email, password));

    }
    public boolean validateEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        if(! matcher.find()){
            Toast.makeText(getActivity().getApplicationContext(),"Email không đúng định dạng.",
                    Toast.LENGTH_LONG).show();
            return false;
        };
        if(emailStr.length()<6){
            Toast.makeText(getActivity().getApplicationContext(),"Email phải lớn hơn 6 ký tự.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public boolean validatePassword(String password) {
        if(password.length()<6){
            Toast.makeText(getActivity().getApplicationContext(),"Password phải lớn hơn 6 ký tự.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public static LoginFragment create(boolean dangXuat) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(LOGIN_DANG_XUAT_KEY,dangXuat);
        loginFragment.setArguments(args);
        return loginFragment;
    }
    public void myOnKeyDown(int key_code){
    }
}
