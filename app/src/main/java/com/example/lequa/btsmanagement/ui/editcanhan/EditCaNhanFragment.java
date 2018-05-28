package com.example.lequa.btsmanagement.ui.editcanhan;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentEditCaNhanBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.Register;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.ui.toado.PlaceArrayAdapter;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCaNhanFragment extends Fragment implements Injectable,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentEditCaNhanBinding> editCaNhanBinding;

    private static final String EDIT_CA_NHAN_TOKEN_KEY = "edit_ca_nhan_token";
    private static final String EDIT_CA_NHAN_EMAIL_KEY = "edit_ca_nhan_email";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private EditCaNhanViewModel editCaNhanViewModel;

    private int tmpNam,tmpThang,tmpNgay;
    private String ngayThang="";


    private static final String LOG_TAG = "ToaDo";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editCaNhanViewModel= ViewModelProviders.of(this,viewModelFactory).get(EditCaNhanViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        editCaNhanViewModel.setUser(getArguments().getString(EDIT_CA_NHAN_EMAIL_KEY), getArguments().getString(EDIT_CA_NHAN_TOKEN_KEY));
        editCaNhanViewModel.getUser().observe(this, user -> {
            if (user.status== Status.SUCCESS)
                editCaNhanBinding.get().setUser(user.data);



        });
        setAppLocal(getContext());
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView = (AutoCompleteTextView) getActivity().findViewById(R.id
                .edDiaChi);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        editCaNhanViewModel.getResultUpdate().observe(this,result->{
            if(result.status==Status.SUCCESS){
                Toast.makeText(getActivity().getApplicationContext(),"Update Thành Công",Toast.LENGTH_LONG).show();
                thoat();
            }
            else if(result.status==Status.ERROR){
                Toast.makeText(getActivity().getApplicationContext(),"Update Không Thành Công "+result.message,Toast.LENGTH_LONG).show();
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
        FragmentEditCaNhanBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_edit_ca_nhan, container, false,dataBindingComponent);
        editCaNhanBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static EditCaNhanFragment create(String email,String token) {
        EditCaNhanFragment editCaNhanFragment = new EditCaNhanFragment();
        Bundle args = new Bundle();
        args.putString(EDIT_CA_NHAN_TOKEN_KEY, token);
        args.putString(EDIT_CA_NHAN_EMAIL_KEY, email);
        editCaNhanFragment.setArguments(args);
        return editCaNhanFragment;
    }
    public void getDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            date = format.parse(dateStr);
            c.setTime(date);
            tmpNam=c.get(Calendar.YEAR);tmpThang=c.get(Calendar.MONTH);tmpNgay=c.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.btnChooseBirthday)
    public void ChonNgaySinh(){
        getDate(editCaNhanBinding.get().tvNgaySinh.getText().toString());
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDateSetListener, tmpNam, tmpThang, tmpNgay);

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Chọn",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            editCaNhanBinding.get().tvNgaySinh.
                                    setText(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());
                            ngayThang=datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth()+"T00:00:00";
                        }
                    }
                });
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        datePickerDialog.show();
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
    @OnClick(R.id.btnCapNhat)
    public void registerTaiKhoan(){
        if(!checkData()) return;
        UserBTS userBTS=new UserBTS();
        userBTS.setIDUser(editCaNhanBinding.get().getUser().getIDUser());
        userBTS.setChucVu(editCaNhanBinding.get().getUser().getChucVu());
        userBTS.setDiaChi(editCaNhanBinding.get().getUser().getDiaChi());
        userBTS.setEmail(editCaNhanBinding.get().getUser().getEmail());
        userBTS.setPhone(editCaNhanBinding.get().edPhone.getText().toString());
        userBTS.setTen(editCaNhanBinding.get().edHoTen.getText().toString());
        if(editCaNhanBinding.get().rbNam.isChecked())
            userBTS.setGioiTinh("Nam");
        else userBTS.setGioiTinh("Nu");
        userBTS.setImage(editCaNhanBinding.get().getUser().getImage());
        if(ngayThang.equals(""))
            userBTS.setNgaySinh(editCaNhanBinding.get().getUser().getNgaySinh());
        else userBTS.setNgaySinh(ngayThang);
        editCaNhanViewModel.setUserUpdate(userBTS);
    }
    public Boolean checkData(){
        if(editCaNhanBinding.get().edHoTen.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Họ Tên",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(editCaNhanBinding.get().edDiaChi.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Địa Chỉ",Toast.LENGTH_LONG).show();
            return false;
        }else if(editCaNhanBinding.get().tvNgaySinh.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Ngày Sinh",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(editCaNhanBinding.get().edPhone.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Số Điện Thoại",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(editCaNhanBinding.get().edPhone.getText().toString().length()<10){
            Toast.makeText(getActivity().getApplicationContext(),"Số Điện Thoại không đúng định dạng",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public static void setAppLocal(Context mContext) {
        String languageToLoad = "vi";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            editCaNhanBinding.get().getUser().setDiaChi(place.getAddress()+"|"+place.getLatLng().latitude+","+place.getLatLng().longitude);
        }
    };
    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getContext(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
