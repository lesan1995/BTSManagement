package com.example.lequa.btsmanagement.ui.taikhoan;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.database.MatrixCursor;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentTaiKhoanBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaiKhoanFragment extends Fragment implements Injectable {
    private static final String TAI_KHOAN_TOKEN_KEY = "tai_khoan_token";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private TaiKhoanViewModel taiKhoanViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentTaiKhoanBinding> taiKhoanBinding;
    AutoClearedValue<TaiKhoanAdapter> adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        taiKhoanViewModel= ViewModelProviders.of(this,viewModelFactory).get(TaiKhoanViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarTaiKhoan);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        taiKhoanViewModel.setToken(getArguments().getString(TAI_KHOAN_TOKEN_KEY));
        taiKhoanViewModel.setQuery("");
        TaiKhoanAdapter taiKhoanAdapter=new TaiKhoanAdapter(dataBindingComponent,
                userBTS -> navigationController.navigateToChiTietTaiKhoan(userBTS.getIDUser(),
                        getArguments().getString(TAI_KHOAN_TOKEN_KEY)));
        this.adapter=new AutoClearedValue<>(this,taiKhoanAdapter);
        taiKhoanBinding.get().listTaiKhoan.setAdapter(taiKhoanAdapter);

        initTaiKhoanList(taiKhoanViewModel);

    }
    public void initTaiKhoanList(TaiKhoanViewModel taiKhoanViewModel){
        taiKhoanViewModel.getAllUser().observe(this,listUser->{
            if (listUser != null && listUser.data != null) {
                adapter.get().replace(listUser.data);
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
        FragmentTaiKhoanBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_tai_khoan, container, false,dataBindingComponent);
        taiKhoanBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tim_kiem, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                taiKhoanViewModel.setQuery(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    public static TaiKhoanFragment create(String token) {
        TaiKhoanFragment taiKhoanFragment = new TaiKhoanFragment();
        Bundle args = new Bundle();
        args.putString(TAI_KHOAN_TOKEN_KEY, token);
        taiKhoanFragment.setArguments(args);
        return taiKhoanFragment;
    }
    @OnClick(R.id.fabThemTaiKhoan)
    public void themTaiKhoan(){
        navigationController.navigateToAddTaiKhoan(getArguments().getString(TAI_KHOAN_TOKEN_KEY));
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
}
