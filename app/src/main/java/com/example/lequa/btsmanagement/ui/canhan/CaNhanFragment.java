package com.example.lequa.btsmanagement.ui.canhan;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentCaNhanBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.hinhanh.ReadPathUtil;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class CaNhanFragment extends Fragment implements Injectable {
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentCaNhanBinding> caNhanBinding;

    private static final String CA_NHAN_TOKEN_KEY = "ca_nhan_token";
    private static final String CA_NHAN_EMAIL_KEY = "ca_nhan_email";
    public final static int CN_PICK_IMAGE_REQUEST = 9;
    public final static int CN_READ_EXTERNAL_REQUEST = 10;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private CaNhanViewModel caNhanViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        caNhanViewModel= ViewModelProviders.of(this,viewModelFactory).get(CaNhanViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarTaiKhoan);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        caNhanViewModel.setUser(getArguments().getString(CA_NHAN_EMAIL_KEY), getArguments().getString(CA_NHAN_TOKEN_KEY));
        caNhanViewModel.getUser().observe(this, user -> {
            if (user.status== Status.SUCCESS)
            caNhanBinding.get().setUser(user.data);
        });
        caNhanViewModel.getResultUpdateImage().observe(this,result->{
            if(result.status==Status.SUCCESS){
                Toast.makeText(getActivity().getApplicationContext(),"Update ảnh đại diện thành công",Toast.LENGTH_LONG).show();
            }
            else if(result.status==Status.ERROR){
                Toast.makeText(getActivity().getApplicationContext(),"Update ảnh đại diện không thành công "+result.message,Toast.LENGTH_LONG).show();
            }
        });

        caNhanBinding.get().btnChinhSuaThongTinCaNhan.setOnClickListener(v->{
            navigationController.navigateToEditCaNhan(getArguments().getString(CA_NHAN_EMAIL_KEY), getArguments().getString(CA_NHAN_TOKEN_KEY));
        });
        caNhanBinding.get().btnDoiMatKhau.setOnClickListener(v->{
            navigationController.navigateToChangePassword( getArguments().getString(CA_NHAN_TOKEN_KEY));
        });
        caNhanBinding.get().btnChooseImage.setOnClickListener(v->{
            requestPermionAndPickImage();
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
        FragmentCaNhanBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_ca_nhan, container, false,dataBindingComponent);
        caNhanBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static CaNhanFragment create(String email,String token) {
        CaNhanFragment caNhanFragment = new CaNhanFragment();
        Bundle args = new Bundle();
        args.putString(CA_NHAN_TOKEN_KEY, token);
        args.putString(CA_NHAN_EMAIL_KEY, email);
        caNhanFragment.setArguments(args);
        return caNhanFragment;
    }
    private void requestPermionAndPickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickImage();
            return;
        }
        // Các bạn nhớ request permison cho các máy M trở lên nhé, k là crash ngay đấy.
        int result = ContextCompat.checkSelfPermission(getContext(),
                READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            requestPermissions(new String[]{
                    READ_EXTERNAL_STORAGE}, CN_READ_EXTERNAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode != CN_READ_EXTERNAL_REQUEST) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Permission Denied",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            getActivity().startActivityForResult(intent, CN_PICK_IMAGE_REQUEST);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case CN_PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    String imagePath = ReadPathUtil.getPath(getContext(), data.getData());
                    File file = new File(imagePath);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    caNhanViewModel.setImage(body);
                }
                break;
        }
    }

}
