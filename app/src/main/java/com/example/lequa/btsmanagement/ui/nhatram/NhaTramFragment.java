package com.example.lequa.btsmanagement.ui.nhatram;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentNhaTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.ui.edit.UserAdapter;
import com.example.lequa.btsmanagement.ui.tram.TramViewModel;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.util.List;

import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NhaTramFragment extends Fragment implements Injectable {
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentNhaTramBinding> nhaTramBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private NhaTramViewModel nhaTramViewModel;

    private static final String NHA_TRAM_TOKEN_KEY = "nha_tram_token";
    private static final String NHA_TRAM_ID_KEY = "nha_tram_id";
    private static final String NHA_TRAM_CV_KEY = "nha_tram_cv";
    private boolean isEditThongSo=false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nhaTramViewModel= ViewModelProviders.of(this,viewModelFactory).get(NhaTramViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarNhaTram);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        setStateSeekBar(false);
        nhaTramViewModel.setNhaTram(Integer.parseInt(getArguments().getString(NHA_TRAM_ID_KEY)),getArguments().getString(NHA_TRAM_TOKEN_KEY));
        nhaTramViewModel.getNhaTram().observe(this,nhaTram->{
            if(nhaTram.status== Status.SUCCESS){
                nhaTramBinding.get().setNhaTram(nhaTram.data);
                nhaTramViewModel.setTram(nhaTram.data.getIDTram());
                nhaTramViewModel.setNhaMang(nhaTram.data.getIDNhaMang());
            }
        });
        nhaTramViewModel.getTram().observe(this,tram->{
            if(tram.status== Status.SUCCESS){
                nhaTramBinding.get().setTram(tram.data);
            }
        });
        nhaTramViewModel.getNhaMang().observe(this,nhaMang->{
            if(nhaMang.status== Status.SUCCESS){
                nhaTramBinding.get().setNhaMang(nhaMang.data);
            }
        });
        nhaTramViewModel.getResultUpdateNhaTram().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Update Thành Công",Toast.LENGTH_LONG).show();
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Không thể có nhiều nhà mạng trong cùng 1 trạm ",Toast.LENGTH_LONG).show();
            }
        });
        nhaTramViewModel.getResultUpdateThongSo().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Update Thành Công",Toast.LENGTH_LONG).show();
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Không thể update",Toast.LENGTH_LONG).show();
            }
        });
        setSeekBar();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentNhaTramBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_nha_tram, container, false,dataBindingComponent);
        nhaTramBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static NhaTramFragment create(String idNhaTram, String token) {
        NhaTramFragment nhaTramFragment = new NhaTramFragment();
        Bundle args = new Bundle();
        args.putString(NHA_TRAM_ID_KEY, idNhaTram);
        args.putString(NHA_TRAM_TOKEN_KEY, token);
        nhaTramFragment.setArguments(args);
        return nhaTramFragment;
    }
    public static NhaTramFragment create(String idNhaTram, String token,String chucVu) {
        NhaTramFragment nhaTramFragment = new NhaTramFragment();
        Bundle args = new Bundle();
        args.putString(NHA_TRAM_ID_KEY, idNhaTram);
        args.putString(NHA_TRAM_TOKEN_KEY, token);
        args.putString(NHA_TRAM_CV_KEY, chucVu);
        nhaTramFragment.setArguments(args);
        return nhaTramFragment;
    }
    public void setStateSeekBar(boolean stateSb){
        nhaTramBinding.get().sbBinhCuuHoa.setEnabled(stateSb);
        nhaTramBinding.get().sbCauCap.setEnabled(stateSb);
        nhaTramBinding.get().sbHeThongDien.setEnabled(stateSb);
        nhaTramBinding.get().sbHangRao.setEnabled(stateSb);
        nhaTramBinding.get().sbDieuHoa.setEnabled(stateSb);
        nhaTramBinding.get().sbOnAp.setEnabled(stateSb);
        nhaTramBinding.get().sbCanhBao.setEnabled(stateSb);
        nhaTramBinding.get().sbMayPhatDien.setEnabled(stateSb);
        nhaTramBinding.get().rbCo.setEnabled(stateSb);
        nhaTramBinding.get().rbKhong.setEnabled(stateSb);

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
    @OnClick(R.id.btnEditNhaMang)
    public void showEdit(){
        if(getArguments().getString(NHA_TRAM_CV_KEY).equals("QuanLy")){
            showEditThongSo();
        }
        else{
            showEditNhaMang();
        }
    }

    public void showEditNhaMang(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.popup_edit_nha_mang, null);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
        Button bn=(Button)mView.findViewById(R.id.btnThoatEditNhaMang);
        Spinner spNhaMang=mView.findViewById(R.id.spEditNhaMang);
        Button btnLuu=mView.findViewById(R.id.btnLuuEditNhaMang);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NhaMang nhaMang=((NhaMangAdapter)spNhaMang.getAdapter()).getListData().get(spNhaMang.getSelectedItemPosition());
                NhaTram nhaTram=nhaTramBinding.get().getNhaTram();
                if(!nhaTram.getIDNhaMang().equals(nhaMang.getIDNhaMang())){
                    nhaTram.setIDNhaMang(nhaMang.getIDNhaMang());
                    nhaTramViewModel.setUpdateNhaTram(nhaTram);
                }
                dialog.dismiss();
            }
        });

        nhaTramViewModel.setUpdateNhaMang(true);
        nhaTramViewModel.getListNhaMang().observe(this,listNhaMang->{
            if(listNhaMang.status==Status.SUCCESS){
                spNhaMang.setAdapter(new NhaMangAdapter(getContext(),listNhaMang.data));
                spNhaMang.setSelection(getIndexNhaMang(listNhaMang.data,nhaTramBinding.get().getNhaMang()));
            }
        });
    }
    public int getIndexNhaMang(List<NhaMang> list, NhaMang nhaMang){
        int i=0;
        for (NhaMang item:list) {
            if(item.getIDNhaMang().equals(nhaMang.getIDNhaMang())) return i;
            i++;
        }
        return 0;
    }
    public void showEditThongSo(){
        if(!isEditThongSo){
            nhaTramBinding.get().btnEditNhaMang.setImageResource(R.drawable.ic_save);
            setStateSeekBar(true);
            isEditThongSo=true;
        }
        else{
            updateThongSo();
            nhaTramBinding.get().btnEditNhaMang.setImageResource(R.drawable.ic_edit);
            setStateSeekBar(false);
            isEditThongSo=false;

        }
    }
    public void updateThongSo(){
        NhaTram nhaTram=nhaTramBinding.get().getNhaTram();
        nhaTram.setCauCap(nhaTramBinding.get().sbCauCap.getProgress());
        nhaTram.setHeThongDien(nhaTramBinding.get().sbHeThongDien.getProgress());
        nhaTram.setHangRao(nhaTramBinding.get().sbHangRao.getProgress());
        nhaTram.setDieuHoa(nhaTramBinding.get().sbDieuHoa.getProgress());
        nhaTram.setOnAp(nhaTramBinding.get().sbOnAp.getProgress());
        nhaTram.setCanhBao(nhaTramBinding.get().sbCanhBao.getProgress());
        nhaTram.setBinhCuuHoa(nhaTramBinding.get().sbBinhCuuHoa.getProgress());
        nhaTram.setMayPhatDien(nhaTramBinding.get().sbMayPhatDien.getProgress());
        nhaTram.setChungMayPhat(nhaTramBinding.get().rbCo.isChecked());
        nhaTramViewModel.setUpdateThongSo(nhaTram);

    }
    public void setSeekBar(){
        nhaTramBinding.get().sbCauCap.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvCauCap.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbHeThongDien.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvHeThongDien.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbHangRao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvHangRao.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbDieuHoa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvDieuHoa.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbOnAp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvOnAp.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbCanhBao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvCanhBao.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbBinhCuuHoa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvBinhCuuHoa.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nhaTramBinding.get().sbMayPhatDien.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nhaTramBinding.get().tvMayPhatDien.setText(seekBar.getProgress()+"/15");
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
