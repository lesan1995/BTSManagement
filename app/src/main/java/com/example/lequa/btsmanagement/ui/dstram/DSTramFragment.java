package com.example.lequa.btsmanagement.ui.dstram;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentDsTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.map.MainFragment;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.google.android.gms.maps.model.LatLng;
import java.util.Collections;
import javax.inject.Inject;
import butterknife.ButterKnife;

public class DSTramFragment extends Fragment implements Injectable {
    private static final String DS_TRAM_TOKEN_KEY = "ds_tram_token";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private DSTramViewModel dsTramViewModel;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentDsTramBinding> dsTramBinding;
    AutoClearedValue<DSTramAdapter> adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        dsTramViewModel= ViewModelProviders.of(this,viewModelFactory).get(DSTramViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarDSTram);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        dsTramViewModel.setToken(getArguments().getString(DS_TRAM_TOKEN_KEY));
        dsTramViewModel.setQuery("");
        DSTramAdapter dsTramAdapter=new DSTramAdapter(dataBindingComponent,
                tram -> goToTramOnMap(tram));
        this.adapter=new AutoClearedValue<>(this,dsTramAdapter);
        dsTramBinding.get().listTram.setAdapter(dsTramAdapter);
        initDSTramList(dsTramViewModel);
    }
    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoat();
            }
        });
    }
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    public void goToTramOnMap(Tram tram){
        MainFragment.sydney=new LatLng(tram.getViDo(),tram.getKinhDo());
        thoat();
    }
    public void initDSTramList(DSTramViewModel dsTramViewModel){
        dsTramViewModel.getListTram().observe(this,listTram->{
            if (listTram != null && listTram.data != null) {
                adapter.get().replace(listTram.data);
            } else {
                //noinspection ConstantConditions
                adapter.get().replace(Collections.emptyList());
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentDsTramBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_ds_tram, container, false,dataBindingComponent);
        dsTramBinding = new AutoClearedValue<>(this, dataBinding);
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
                dsTramViewModel.setQuery(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    public static DSTramFragment create(String token) {
        DSTramFragment dsTramFragment = new DSTramFragment();
        Bundle args = new Bundle();
        args.putString(DS_TRAM_TOKEN_KEY, token);
        dsTramFragment.setArguments(args);
        return dsTramFragment;
    }
}
