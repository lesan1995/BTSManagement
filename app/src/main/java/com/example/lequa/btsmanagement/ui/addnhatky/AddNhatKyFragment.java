package com.example.lequa.btsmanagement.ui.addnhatky;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.FragmentAddNhatKyBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNhatKyFragment extends Fragment implements Injectable {
    private static final String ADD_NK_TOKEN_KEY = "add_nk_token";
    private static final String ADD_NK_ID_KEY = "add_nk_id";
    AutoClearedValue<FragmentAddNhatKyBinding> addNhatKyBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AddNhatKyViewModel addNhatKyViewModel;
    List<String> listLoaiNhatKy=new ArrayList<String>();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addNhatKyViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddNhatKyViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        addNhatKyViewModel.getResultInsertNhatKy().observe(this,result->{
            int i=0;
            if(result.status== Status.SUCCESS){
                i++;
                if(i==1){
                    Toast.makeText(getActivity().getApplicationContext(),"Lưu Thành Công",Toast.LENGTH_LONG).show();
                    thoat();
                }
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1)
                    Toast.makeText(getActivity().getApplicationContext(),"Có lỗi xảy ra trong quá trình báo cáo "+result.message,Toast.LENGTH_LONG).show();
            }
        });
        setSpinner();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddNhatKyBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_nhat_ky, container, false);
        addNhatKyBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static AddNhatKyFragment create(String idTram, String token) {
        AddNhatKyFragment addNhatKyFragment = new AddNhatKyFragment();
        Bundle args = new Bundle();
        args.putString(ADD_NK_ID_KEY, idTram);
        args.putString(ADD_NK_TOKEN_KEY, token);
        addNhatKyFragment.setArguments(args);
        return addNhatKyFragment;
    }
    public void setSpinner(){
        ArrayAdapter<String> adapter;
        listLoaiNhatKy.add("Nhật Ký");
        listLoaiNhatKy.add("Sự Cố");
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, listLoaiNhatKy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addNhatKyBinding.get().spLoaiBaoCao.setAdapter(adapter);
    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoat();
            }
        });
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
    public void luu(){
        if(!validate()) return;
        NhatKy nhatKy=new NhatKy();
        nhatKy.setIDNhatKy(0);
        nhatKy.setIDTram(Integer.parseInt(getArguments().getString(ADD_NK_ID_KEY)));
        if(listLoaiNhatKy.get(addNhatKyBinding.get().spLoaiBaoCao.getSelectedItemPosition()).equals("Nhật Ký"))
        nhatKy.setLoai("NhatKy");
        else nhatKy.setLoai("SuCo");
        nhatKy.setTieuDe(addNhatKyBinding.get().edTieuDe.getText().toString());
        nhatKy.setNoiDung(addNhatKyBinding.get().edNoiDung.getText().toString());
        nhatKy.setDaGiaiQuyet(false);
        addNhatKyViewModel.setNhatKy(nhatKy,getArguments().getString(ADD_NK_TOKEN_KEY));

    }
    public boolean validate(){
        if(addNhatKyBinding.get().edTieuDe.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập tiêu đề",Toast.LENGTH_LONG).show();
            return false;
        }
        if(addNhatKyBinding.get().edNoiDung.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập nội dung",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
