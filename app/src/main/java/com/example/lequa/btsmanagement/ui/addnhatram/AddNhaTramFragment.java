package com.example.lequa.btsmanagement.ui.addnhatram;

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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.FragmentAddNhaTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.ui.nhatram.NhaMangAdapter;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNhaTramFragment extends Fragment implements Injectable {
    private static final String ADD_NHA_TRAM_TOKEN_KEY = "add_nha_tram_token";
    private static final String ADD_NHA_TRAM_ID_KEY = "add_nha_tram_id";
    AutoClearedValue<FragmentAddNhaTramBinding> addNhaTramBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private AddNhaTramViewModel addNhaTramViewModel;
    @BindView(R.id.spNhaMang) Spinner spNhaMang;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addNhaTramViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddNhaTramViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarAddNhaTram);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);

        addNhaTramViewModel.setDisplayListNhaMang(getArguments().getString(ADD_NHA_TRAM_TOKEN_KEY),true);
        addNhaTramViewModel.getListNhaMang().observe(this,listNhaMang->{
            if(listNhaMang.status== Status.SUCCESS){
                spNhaMang.setAdapter(new NhaMangAdapter(getContext(),listNhaMang.data));
            }
        });
        addNhaTramViewModel.getResultInsertNhaTram().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1){
                    Toast.makeText(getActivity().getApplicationContext(),"Lưu Thành Công",Toast.LENGTH_LONG).show();
                    thoat();
                }
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1)
                Toast.makeText(getActivity().getApplicationContext(),"Không thể có nhiều nhà mạng trong cùng 1 trạm",Toast.LENGTH_LONG).show();
            }
        });
        setSeekBar();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddNhaTramBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_nha_tram, container, false);
        addNhaTramBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static AddNhaTramFragment create(String idTram, String token) {
        AddNhaTramFragment addNhaTramFragment = new AddNhaTramFragment();
        Bundle args = new Bundle();
        args.putString(ADD_NHA_TRAM_ID_KEY, idTram);
        args.putString(ADD_NHA_TRAM_TOKEN_KEY, token);
        addNhaTramFragment.setArguments(args);
        return addNhaTramFragment;
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
    @OnClick(R.id.btnThoatAddNhaTram)
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    @OnClick(R.id.btnLuuAddNhaTram)
    public void luu(){
        NhaMang nhaMang=((NhaMangAdapter)spNhaMang.getAdapter()).getListData().get(spNhaMang.getSelectedItemPosition());
        NhaTram nhaTram=new NhaTram();
        nhaTram.setIDNhaTram(0);
        nhaTram.setIDTram(Integer.parseInt(getArguments().getString(ADD_NHA_TRAM_ID_KEY)));
        nhaTram.setIDNhaMang(nhaMang.getIDNhaMang());
        nhaTram.setCauCap(addNhaTramBinding.get().sbCauCap.getProgress());
        nhaTram.setHeThongDien(addNhaTramBinding.get().sbHeThongDien.getProgress());
        nhaTram.setHangRao(addNhaTramBinding.get().sbHangRao.getProgress());
        nhaTram.setDieuHoa(addNhaTramBinding.get().sbDieuHoa.getProgress());
        nhaTram.setOnAp(addNhaTramBinding.get().sbOnAp.getProgress());
        nhaTram.setCanhBao(addNhaTramBinding.get().sbCanhBao.getProgress());
        nhaTram.setBinhCuuHoa(addNhaTramBinding.get().sbBinhCuuHoa.getProgress());
        nhaTram.setMayPhatDien(addNhaTramBinding.get().sbMayPhatDien.getProgress());
        nhaTram.setChungMayPhat(addNhaTramBinding.get().rbCo.isChecked());
        addNhaTramViewModel.setInsertNhaTram(nhaTram);
    }


    public void setSeekBar(){
        addNhaTramBinding.get().sbCauCap.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvCauCap.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbHeThongDien.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvHeThongDien.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbHangRao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvHangRao.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbDieuHoa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvDieuHoa.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbOnAp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvOnAp.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbCanhBao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvCanhBao.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbBinhCuuHoa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvBinhCuuHoa.setText(seekBar.getProgress()+"/15");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addNhaTramBinding.get().sbMayPhatDien.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addNhaTramBinding.get().tvMayPhatDien.setText(seekBar.getProgress()+"/15");
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
