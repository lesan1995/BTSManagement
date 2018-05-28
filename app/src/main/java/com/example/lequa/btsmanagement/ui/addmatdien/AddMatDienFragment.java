package com.example.lequa.btsmanagement.ui.addmatdien;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.FragmentAddMatDienBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.MatDien;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMatDienFragment extends Fragment implements Injectable {
    private static final String ADD_MAT_DIEN_TOKEN_KEY = "add_mat_dien_token";
    private static final String ADD_MAT_DIEN_ID_TRAM_KEY = "add_id_tram_token";
    AutoClearedValue<FragmentAddMatDienBinding> addMatDienBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AddMatDienViewModel addMatDienViewModel;

    private static final String TIME="time";
    private static final String DATETIME="datetime";
    private static Calendar calThoiGianMat,calThoiGianBat,calThoiGianTat;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addMatDienViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddMatDienViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        addMatDienViewModel.getResultInsertMatDien().observe(this,result->{
            if(result.status== Status.SUCCESS){
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
                        Toast.makeText(getActivity().getApplicationContext(),"Báo cáo lỗi",Toast.LENGTH_LONG).show();
                }
            }
        });
        initTime();
    }

    @OnClick(R.id.btnLuu)
    public void luu(){
        if(!validate()) return;
        MatDien matDien=new MatDien();
        matDien.setIDMatDien(0);
        matDien.setIDTram(Integer.parseInt(getArguments().getString(ADD_MAT_DIEN_ID_TRAM_KEY)));
        matDien.setNgayMatDien(calThoiGianMat.get(Calendar.YEAR)+"-"+(calThoiGianMat.get(Calendar.MONTH)+1)+"-"+calThoiGianMat.get(Calendar.DAY_OF_MONTH));
        matDien.setGioMatDien(calThoiGianMat.get(Calendar.HOUR_OF_DAY)+":"+calThoiGianMat.get(Calendar.MINUTE));
        matDien.setThoiGianMayNo(calThoiGianBat.get(Calendar.YEAR)+"-"+(calThoiGianBat.get(Calendar.MONTH)+1)+"-"+calThoiGianBat.get(Calendar.DAY_OF_MONTH)+" "+calThoiGianBat.get(Calendar.HOUR_OF_DAY)+":"+calThoiGianBat.get(Calendar.MINUTE));
        matDien.setThoiGianNgung(calThoiGianTat.get(Calendar.YEAR)+"-"+(calThoiGianTat.get(Calendar.MONTH)+1)+"-"+calThoiGianTat.get(Calendar.DAY_OF_MONTH)+" "+calThoiGianTat.get(Calendar.HOUR_OF_DAY)+":"+calThoiGianTat.get(Calendar.MINUTE));
        matDien.setQuangDuongDiChuyen(Double.parseDouble(addMatDienBinding.get().edQuangDuong.getText().toString()));
        addMatDienViewModel.setInsertMatDien(getArguments().getString(ADD_MAT_DIEN_TOKEN_KEY),matDien);
    }
    public boolean validate(){
        if(calThoiGianMat.after(calThoiGianBat)){
            Toast.makeText(getActivity().getApplicationContext(),"Thời gian mất điện phải trước thời gian bật máy nỗ",Toast.LENGTH_LONG).show();
            return false;
        }
        if(calThoiGianBat.after(calThoiGianTat)){
            Toast.makeText(getActivity().getApplicationContext(),"Thời gian bật máy nỗ phải trước thời gian tắt máy nỗ",Toast.LENGTH_LONG).show();
            return false;
        }
        Date dateCurrent = Calendar.getInstance().getTime();
        Calendar timeCurrent=Calendar.getInstance();timeCurrent.setTime(dateCurrent);
        if(calThoiGianBat.after(timeCurrent)){
            Toast.makeText(getActivity().getApplicationContext(),"Thời gian tắt máy nỗ phải trước thời gian hiện tại",Toast.LENGTH_LONG).show();
            return false;
        }
        if(Double.parseDouble(addMatDienBinding.get().edQuangDuong.getText().toString())<=0){
            Toast.makeText(getActivity().getApplicationContext(),"Quãng đường di chuyển phải lớn hơn 0",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddMatDienBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_mat_dien, container, false);
        addMatDienBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static AddMatDienFragment create(String idTram,String token) {
        AddMatDienFragment addMatDienFragment = new AddMatDienFragment();
        Bundle args = new Bundle();
        args.putString(ADD_MAT_DIEN_TOKEN_KEY, token);
        args.putString(ADD_MAT_DIEN_ID_TRAM_KEY, idTram);
        addMatDienFragment.setArguments(args);
        return addMatDienFragment;
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
    public void initTime(){
        Date dateCurrent = Calendar.getInstance().getTime();
        calThoiGianMat=Calendar.getInstance();calThoiGianMat.setTime(dateCurrent);
        setTimeForTextView(addMatDienBinding.get().tvGioMatDien,calThoiGianMat,TIME);
        setTimeForTextView(addMatDienBinding.get().tvNgayMatDien,calThoiGianMat,DATETIME);
        calThoiGianBat=Calendar.getInstance();calThoiGianBat.setTime(dateCurrent);
        setTimeForTextView(addMatDienBinding.get().tvGioMayNo,calThoiGianBat,TIME);
        setTimeForTextView(addMatDienBinding.get().tvNgayMayNo,calThoiGianBat,DATETIME);
        calThoiGianTat=Calendar.getInstance();calThoiGianTat.setTime(dateCurrent);
        setTimeForTextView(addMatDienBinding.get().tvGioNgung,calThoiGianTat,TIME);
        setTimeForTextView(addMatDienBinding.get().tvNgayNgung,calThoiGianTat,DATETIME);
    }
    public void setTimeForTextView(TextView textView, Calendar cal,String type){
        switch (type){
            case TIME:
                String hour=cal.get(Calendar.HOUR_OF_DAY)+""; if(hour.length()==1) hour="0"+hour;
                String minute=cal.get(Calendar.MINUTE)+""; if(minute.length()==1) minute="0"+minute;
                textView.setText(hour+":"+minute);
                break;
            case DATETIME:
                String day=cal.get(Calendar.DAY_OF_MONTH)+""; if(day.length()==1) day="0"+day;
                String month=(cal.get(Calendar.MONTH)+1)+""; if(month.length()==1) month="0"+month;
                String year=cal.get(Calendar.YEAR)+"";
                textView.setText(day+"/"+month+"/"+year);
                break;
        }
    }
    @OnClick(R.id.btnGioMatDien)
    public void chonGioMatDien(){
        final TimePickerDialog.OnTimeSetListener mTimeSetListener=new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calThoiGianMat.set(Calendar.HOUR_OF_DAY, i);
                calThoiGianMat.set(Calendar.MINUTE, i1);
                setTimeForTextView(addMatDienBinding.get().tvGioMatDien,calThoiGianMat,TIME);
            }
        };

        TimePickerDialog tp1 = new TimePickerDialog(getContext(), mTimeSetListener, calThoiGianMat.get(Calendar.HOUR_OF_DAY), calThoiGianMat.get(Calendar.MINUTE), true);
        tp1.show();
    }
    @OnClick(R.id.btnGioMayNo)
    public void chonGioMayNo(){
        final TimePickerDialog.OnTimeSetListener mTimeSetListener=new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calThoiGianBat.set(Calendar.HOUR_OF_DAY, i);
                calThoiGianBat.set(Calendar.MINUTE, i1);
                setTimeForTextView(addMatDienBinding.get().tvGioMayNo,calThoiGianBat,TIME);
            }
        };

        TimePickerDialog tp1 = new TimePickerDialog(getContext(), mTimeSetListener, calThoiGianBat.get(Calendar.HOUR_OF_DAY), calThoiGianBat.get(Calendar.MINUTE), true);
        tp1.show();
    }
    @OnClick(R.id.btnGioNgung)
    public void chonGioNgung(){
        final TimePickerDialog.OnTimeSetListener mTimeSetListener=new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calThoiGianTat.set(Calendar.HOUR_OF_DAY, i);
                calThoiGianTat.set(Calendar.MINUTE, i1);
                setTimeForTextView(addMatDienBinding.get().tvGioNgung,calThoiGianTat,TIME);
            }
        };

        TimePickerDialog tp1 = new TimePickerDialog(getContext(), mTimeSetListener, calThoiGianTat.get(Calendar.HOUR_OF_DAY), calThoiGianTat.get(Calendar.MINUTE), true);
        tp1.show();
    }
    @OnClick(R.id.btnNgayMatDien)
    public void ChonNgayMatDien(){
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calThoiGianMat.set(Calendar.YEAR,year);calThoiGianMat.set(Calendar.MONTH,monthOfYear);
                calThoiGianMat.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setTimeForTextView(addMatDienBinding.get().tvNgayMatDien,calThoiGianMat,DATETIME);
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDateSetListener,
                calThoiGianMat.get(Calendar.YEAR), calThoiGianMat.get(Calendar.MONTH), calThoiGianMat.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @OnClick(R.id.btnNgayMayNo)
    public void ChonNgayMayNo(){
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calThoiGianBat.set(Calendar.YEAR,year);calThoiGianBat.set(Calendar.MONTH,monthOfYear);
                calThoiGianBat.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setTimeForTextView(addMatDienBinding.get().tvNgayMayNo,calThoiGianBat,DATETIME);
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDateSetListener,
                calThoiGianBat.get(Calendar.YEAR), calThoiGianBat.get(Calendar.MONTH), calThoiGianBat.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @OnClick(R.id.btnNgayNgung)
    public void ChonNgayNgung(){
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calThoiGianTat.set(Calendar.YEAR,year);calThoiGianTat.set(Calendar.MONTH,monthOfYear);
                calThoiGianTat.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setTimeForTextView(addMatDienBinding.get().tvNgayNgung,calThoiGianTat,DATETIME);
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDateSetListener,
                calThoiGianTat.get(Calendar.YEAR), calThoiGianTat.get(Calendar.MONTH), calThoiGianTat.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
