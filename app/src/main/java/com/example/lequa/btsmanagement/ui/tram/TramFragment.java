package com.example.lequa.btsmanagement.ui.tram;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingComponent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TramFragment extends Fragment implements Injectable{

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentTramBinding> tramBinding;
    AutoClearedValue<NhaTramAdapter> adapter;

    private static final String TRAM_TOKEN_KEY = "tram_token";
    private static final String TRAM_ID_KEY = "tram_id";
    private static final String TRAM_CV_KEY = "tram_cv";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private TramViewModel tramViewModel;

    private boolean isEdit=false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tramViewModel= ViewModelProviders.of(this,viewModelFactory).get(TramViewModel.class);

        tramViewModel.setTram(Integer.parseInt(getArguments().getString(TRAM_ID_KEY)),getArguments().getString(TRAM_TOKEN_KEY));
        tramViewModel.getTram().observe(this,tram->{
            if(tram.status== Status.SUCCESS){
                tramBinding.get().setTram(tram.data);
                tramViewModel.setUser(tram.data.getIDQuanLy());
            }
        });
        tramViewModel.getUser().observe(this,user->{
            if(user.status==Status.SUCCESS){
                tramBinding.get().setUser(user.data);
            }
        });
        tramViewModel.getResultUpdateTram().observe(this,result->{
            if(result.status==Status.SUCCESS){
            }
        });

        tramViewModel.getResultDeleteNhaTram().observe(this,result->{
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

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarTram);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);

        tramBinding.get().sbCotAnten.setEnabled(false);
        tramBinding.get().sbCotTiepDat.setEnabled(false);
        tramBinding.get().sbSanTram.setEnabled(false);

        NhaTramAdapter nhaTramAdapter=new NhaTramAdapter(dataBindingComponent,
                nhaTram -> navigationController.navigateToNhaTram(nhaTram.getIDNhaTram()+"",getArguments().getString(TRAM_TOKEN_KEY),
                        getArguments().getString(TRAM_CV_KEY)),
                nhaTram->deleteNhaTram(nhaTram));
        this.adapter=new AutoClearedValue<>(this,nhaTramAdapter);
        tramBinding.get().lvNhaTram.setAdapter(nhaTramAdapter);
        initListNhaTram(tramViewModel);


        tramBinding.get().btnAddNhaTram.setOnClickListener(v->{
            navigationController.navigateToAddNhaTram(getArguments().getString(TRAM_ID_KEY),getArguments().getString(TRAM_TOKEN_KEY));
        });

        tramBinding.get().btnEditTram.setOnClickListener(v->{
            navigationController.navigateToEditToaDo(getArguments().getString(TRAM_ID_KEY),getArguments().getString(TRAM_TOKEN_KEY));
        });
        phanQuyenGiaoDien();
        setSeekBar();

    }
    public void phanQuyenGiaoDien(){
        if(getArguments().getString(TRAM_CV_KEY).equals("Admin")){
            tramBinding.get().btnEditThongSo.setVisibility(View.GONE);
        }
        else if(getArguments().getString(TRAM_CV_KEY).equals("QuanLy")){
            tramBinding.get().btnEditTram.setVisibility(View.GONE);
            tramBinding.get().btnAddNhaTram.setVisibility(View.GONE);
        }
    }
    private void initListNhaTram(TramViewModel tramViewModel){
        tramViewModel.getListNhaMang().observe(this,listNhaMang->{
            if(listNhaMang.status== Status.SUCCESS){
                adapter.get().replaceNhaMang(listNhaMang.data);
            }
        });
        tramViewModel.getListNhaTram().observe(this,listNhaTram->{
            if(listNhaTram.status== Status.SUCCESS){
                adapter.get().replaceNhaTram(listNhaTram.data);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentTramBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_tram, container, false,dataBindingComponent);
        tramBinding = new AutoClearedValue<>(this, dataBinding);

        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static TramFragment create(String idTram, String token) {
        TramFragment tramFragment = new TramFragment();
        Bundle args = new Bundle();
        args.putString(TRAM_ID_KEY, idTram);
        args.putString(TRAM_TOKEN_KEY, token);
        tramFragment.setArguments(args);
        return tramFragment;
    }
    public static TramFragment create(String idTram, String token,String chucVu) {
        TramFragment tramFragment = new TramFragment();
        Bundle args = new Bundle();
        args.putString(TRAM_ID_KEY, idTram);
        args.putString(TRAM_TOKEN_KEY, token);
        args.putString(TRAM_CV_KEY, chucVu);
        tramFragment.setArguments(args);
        return tramFragment;
    }

    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments().getString(TRAM_CV_KEY)!=null){
                    navigationController.navigateToHome();
                }
                else{
                    FragmentManager fm = getFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                        getActivity().onBackPressed();
                    }
                }

            }
        });
    }

    public void deleteNhaTram(NhaTram nhaTram){
        if(getArguments().getString(TRAM_CV_KEY).equals("QuanLy")) return;
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Xóa Nhà Trạm")
                .setMessage("Bạn có chắc chắn muốn xóa nhà trạm này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        tramViewModel.setDeleteNhaTram(nhaTram);
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
    @OnClick(R.id.btnEditThongSo)
    public void editThongSo(){
        if(!isEdit){
            tramBinding.get().btnEditThongSo.setImageResource(R.drawable.ic_save);
            tramBinding.get().sbCotAnten.setEnabled(true);
            tramBinding.get().sbCotTiepDat.setEnabled(true);
            tramBinding.get().sbSanTram.setEnabled(true);
            isEdit=true;
        }
        else{
            tramBinding.get().btnEditThongSo.setImageResource(R.drawable.ic_edit);
            tramBinding.get().sbCotAnten.setEnabled(false);
            tramBinding.get().sbCotTiepDat.setEnabled(false);
            tramBinding.get().sbSanTram.setEnabled(false);
            isEdit=false;
            capNhatThongSo();
        }
    }
    public void capNhatThongSo(){
        Tram tram=tramBinding.get().getTram();
        tram.setCotTiepDat(tramBinding.get().sbCotTiepDat.getProgress());
        tram.setCotAnten(tramBinding.get().sbCotAnten.getProgress());
        tram.setSanTram(tramBinding.get().sbSanTram.getProgress());
        tramViewModel.setUpdateTram(tram);
    }
    public void setSeekBar() {
        tramBinding.get().sbCotTiepDat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tramBinding.get().tvCotTiepDat.setText(seekBar.getProgress() + "/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tramBinding.get().sbCotAnten.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tramBinding.get().tvCotAnten.setText(seekBar.getProgress() + "/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tramBinding.get().sbSanTram.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tramBinding.get().tvSanTram.setText(seekBar.getProgress() + "/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
