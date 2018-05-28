package com.example.lequa.btsmanagement.ui.hinhanh;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentHinhAnhBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.HinhAnhTram;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.ui.taikhoan.TaiKhoanViewModel;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class HinhAnhFragment extends Fragment implements Injectable {
    private static final String HINH_ANH_TOKEN_KEY = "hinh_anh_token";
    private static final String HINH_ANH_ID_TRAM_KEY = "hinh_anh_id_tram";
    public final static int PICK_IMAGE_REQUEST = 7;
    public final static int READ_EXTERNAL_REQUEST = 8;


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private HinhAnhViewModel hinhAnhViewModel;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentHinhAnhBinding> hinhanhBinding;
    AutoClearedValue<HinhAnhAdapter> adapter;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hinhAnhViewModel= ViewModelProviders.of(this,viewModelFactory).get(HinhAnhViewModel.class);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarHinhAnh);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);

        hinhAnhViewModel.setHinhAnh(getArguments().getString(HINH_ANH_TOKEN_KEY),
                Integer.parseInt(getArguments().getString(HINH_ANH_ID_TRAM_KEY)));
        HinhAnhAdapter hinhAnhAdapter=new HinhAnhAdapter(dataBindingComponent,hinhAnhTram->deleteHinhAnhTram(hinhAnhTram));
        this.adapter=new AutoClearedValue<>(this,hinhAnhAdapter);
        hinhanhBinding.get().listHinhAnh.setAdapter(hinhAnhAdapter);
        initHinhAnhList(hinhAnhViewModel);


        hinhanhBinding.get().fabThemAnh.setOnClickListener(v->{
            requestPermionAndPickImage();
        });

        hinhAnhViewModel.getResultUpload().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1) {
                    Toast.makeText(getActivity().getApplicationContext(),"Thêm Hình Ảnh Thành Công",Toast.LENGTH_LONG).show();
                    hinhAnhViewModel.setHinhAnh(getArguments().getString(HINH_ANH_TOKEN_KEY),
                            Integer.parseInt(getArguments().getString(HINH_ANH_ID_TRAM_KEY)));
                }
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Lỗi Không thể thêm ",Toast.LENGTH_LONG).show();
            }
        });
        hinhAnhViewModel.getResultDeleteHinhAnhTram().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Delete Thành Công",Toast.LENGTH_LONG).show();
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Delete lỗi "+result.message,Toast.LENGTH_LONG).show();
            }
        });



    }
    public void initHinhAnhList(HinhAnhViewModel hinhAnhViewModel){
        hinhAnhViewModel.getListHinhAnhTram().observe(this,listHinhAnh->{
            if (listHinhAnh != null && listHinhAnh.data != null) {
                adapter.get().replace(listHinhAnh.data);
            } else {
                adapter.get().replace(Collections.emptyList());
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentHinhAnhBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_hinh_anh, container, false,dataBindingComponent);
        hinhanhBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static HinhAnhFragment create(String idTram,String token) {
        HinhAnhFragment hinhAnhFragment = new HinhAnhFragment();
        Bundle args = new Bundle();
        args.putString(HINH_ANH_TOKEN_KEY, token);
        args.putString(HINH_ANH_ID_TRAM_KEY, idTram);
        hinhAnhFragment.setArguments(args);
        return hinhAnhFragment;
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
                    READ_EXTERNAL_STORAGE}, READ_EXTERNAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode != READ_EXTERNAL_REQUEST) return;
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
            getActivity().startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    String imagePath = ReadPathUtil.getPath(getContext(), data.getData());
                    File file = new File(imagePath);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    hinhAnhViewModel.setUpload(body);
                }
                break;
        }
    }
    public void deleteHinhAnhTram(HinhAnhTram hinhAnhTram){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Xóa Hình Ảnh")
                .setMessage("Bạn có chắc chắn muốn xóa hình ảnh này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        hinhAnhViewModel.setDeleteHinhAnhTram(hinhAnhTram);
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

}
