package com.example.lequa.btsmanagement.ui.chitietmatdien;

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
import com.example.lequa.btsmanagement.databinding.FragmentCtMatDienBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ChiTietMatDienFragment extends Fragment implements Injectable {
    private static final String CT_MD_TOKEN_KEY = "ct_md_token";
    private static final String CT_MD_ID_MD_KEY = "ct_md_id_md";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;
    private ChiTietMatDienViewModel chiTietMatDienViewModel;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentCtMatDienBinding> ctMatDienBinding;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chiTietMatDienViewModel= ViewModelProviders.of(this,viewModelFactory).get(ChiTietMatDienViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        chiTietMatDienViewModel.setMatDien(Integer.parseInt(getArguments().getString(CT_MD_ID_MD_KEY)),
                getArguments().getString(CT_MD_TOKEN_KEY));
        chiTietMatDienViewModel.getMatDien().observe(this,matDien->{
            if(matDien.status== Status.SUCCESS)
                ctMatDienBinding.get().setMatDien(matDien.data);
                if(matDien.data!=null){
                    if(matDien.data.getTienPhat()>0){
                        ctMatDienBinding.get().trangThai.setImageResource(R.drawable.icon_phat);
                    }
                    else ctMatDienBinding.get().trangThai.setImageResource(R.drawable.icon_khong_phat);
                }

        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentCtMatDienBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_ct_mat_dien, container, false,dataBindingComponent);
        ctMatDienBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static ChiTietMatDienFragment create(String idUser,String token) {
        ChiTietMatDienFragment chiTietMatDienFragment = new ChiTietMatDienFragment();
        Bundle args = new Bundle();
        args.putString(CT_MD_TOKEN_KEY, token);
        args.putString(CT_MD_ID_MD_KEY, idUser);
        chiTietMatDienFragment.setArguments(args);
        return chiTietMatDienFragment;
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
    public static String getDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            date = format.parse(dateStr);
            c.setTime(date);
            return c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "01-01-1990";
    }
    public static String getTime(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            date = format.parse(dateStr);
            c.setTime(date);
            String hour=c.get(Calendar.HOUR_OF_DAY)+"";
            if(hour.length()==1) hour="0"+hour;
            String minute=c.get(Calendar.MINUTE)+"";
            if(minute.length()==1) minute="0"+minute;
            return hour+":"+minute;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00";
    }
    public static String getDateTime(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            date = format.parse(dateStr);
            c.setTime(date);
            String hour=c.get(Calendar.HOUR_OF_DAY)+"";
            if(hour.length()==1) hour="0"+hour;
            String minute=c.get(Calendar.MINUTE)+"";
            if(minute.length()==1) minute="0"+minute;
            return hour+":"+minute+" "+c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "01-01-1990";
    }

}
