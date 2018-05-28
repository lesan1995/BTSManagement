package com.example.lequa.btsmanagement.ui.addtaikhoan;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
import com.example.lequa.btsmanagement.databinding.FragmentAddTaiKhoanBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.Register;
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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaiKhoanFragment extends Fragment implements Injectable,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String REGISTER_TOKEN_KEY = "register_token";
    AutoClearedValue<FragmentAddTaiKhoanBinding> addTaiKhoanBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AddTaiKhoanViewModel addTaiKhoanViewModel;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private String ngaySinh="";
    private Double kinhDo=0d,ViDo=0d;


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
        addTaiKhoanViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddTaiKhoanViewModel.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarAddTaiKhoan);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setBack(toolbar);
        addTaiKhoanViewModel.getResultRegisterUser().observe(this,result->{
            if(result.status== Status.SUCCESS){
                Toast.makeText(getActivity().getApplicationContext(),"Thêm Tài Khoản Thành Công",Toast.LENGTH_LONG).show();
                thoat();
            }
            else if(result.status==Status.ERROR){
                 Toast.makeText(getActivity().getApplicationContext(),"Lỗi Không thể thêm "+result.message,Toast.LENGTH_LONG).show();
            }

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
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddTaiKhoanBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_tai_khoan, container, false);
        addTaiKhoanBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    @OnClick(R.id.btnChooseBirthday)
    public void ChonNgaySinh(){
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDateSetListener, 1990, 10, 3);

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Chọn",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            addTaiKhoanBinding.get().tvNgaySinh.
                            setText(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());
                            ngaySinh=datePicker.getYear() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth();
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

    public void setBack(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoat();
            }
        });
    }
    @OnClick(R.id.btnThoatAddTaiKhoan)
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    @OnClick(R.id.btnLuuAddTaiKhoan)
    public void registerTaiKhoan(){
        if(!checkData()) return;
        Register register=new Register();
        register.setChucVu("QuanLy");
        register.setPassword("123123Aa@");
        register.setConfirmPassword("123123Aa@");
        register.setDiaChi(addTaiKhoanBinding.get().edDiaChi.getText().toString()+"|"+ViDo+","+kinhDo);
        register.setEmail(addTaiKhoanBinding.get().edEmail.getText().toString());
        register.setPhone(addTaiKhoanBinding.get().edPhone.getText().toString());
        register.setTen(addTaiKhoanBinding.get().edHoTen.getText().toString());
        if(addTaiKhoanBinding.get().rbNam.isChecked())
        register.setGioiTinh("Nam");
        else register.setGioiTinh("Nu");
        register.setImage("tam_12345.jpg");
        register.setNgaySinh(ngaySinh);
        addTaiKhoanViewModel.setRegister(getArguments().getString(REGISTER_TOKEN_KEY),register);
    }
    public Boolean checkData(){
        if(addTaiKhoanBinding.get().edHoTen.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Họ Tên",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTaiKhoanBinding.get().edDiaChi.getText().toString().equals("")||ViDo==0||kinhDo==0){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Địa Chỉ",Toast.LENGTH_LONG).show();
            return false;
        }else if(addTaiKhoanBinding.get().tvNgaySinh.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Ngày Sinh",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTaiKhoanBinding.get().edEmail.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Email",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTaiKhoanBinding.get().edPhone.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập Số Điện Thoại",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!validate(addTaiKhoanBinding.get().edEmail.getText().toString())){
            Toast.makeText(getActivity().getApplicationContext(),"Địa chỉ email sai định dạng",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(addTaiKhoanBinding.get().edPhone.getText().toString().length()<10){
            Toast.makeText(getActivity().getApplicationContext(),"Số Điện Thoại không đúng định dạng",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public static AddTaiKhoanFragment create(String token) {
        AddTaiKhoanFragment addTaiKhoanFragment = new AddTaiKhoanFragment();
        Bundle args = new Bundle();
        args.putString(REGISTER_TOKEN_KEY, token);
        addTaiKhoanFragment.setArguments(args);
        return addTaiKhoanFragment;
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
            kinhDo=place.getLatLng().longitude;
            ViDo=place.getLatLng().latitude;
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
