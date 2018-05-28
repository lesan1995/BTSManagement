package com.example.lequa.btsmanagement.ui.toado;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentToaDoBinding;
import com.example.lequa.btsmanagement.databinding.ItemTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.edit.UserAdapter;
import com.example.lequa.btsmanagement.ui.map.MainViewModel;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class ToaDo extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks ,OnMapReadyCallback,Injectable {

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentToaDoBinding> toaDoBinding;
    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TOA_DO_TOKEN_KEY = "toa_do_token";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;
    private ToaDoViewModel toaDoViewModel;

    List<Tram> listTram=new ArrayList<>();
    List<UserBTS> listUser=new ArrayList<>();
    Marker markerChoose=null;
    Circle circleChoose=null;

    public static final int REP_DELAY = 50;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mBanKinhIncrement = false;
    private boolean mBanKinhDecrement = false;
    private boolean mToaDoLeft = false;
    private boolean mToaDoRight = false;
    private boolean mToaDoUp = false;
    private boolean mToaDoDown = false;



    private static final String LOG_TAG = "ToaDo";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @BindView(R.id.spQuanLy) Spinner spQuanLy;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        toaDoBinding.get().setKinhDo(0d);
        toaDoBinding.get().setViDo(0d);toaDoBinding.get().setBanKinh(0d);

        toaDoViewModel = ViewModelProviders.of(this, viewModelFactory).get(ToaDoViewModel.class);
        toaDoViewModel.setToken(getArguments().getString(TOA_DO_TOKEN_KEY));
        toaDoViewModel.getListTram().observe(this, tram -> {
            if (tram.status == Status.SUCCESS){
                setListTram(tram.data);
            }

        });
        toaDoViewModel.getListUser().observe(this,listUser->{
            if(listUser.status== Status.SUCCESS){
                spQuanLy.setAdapter(new UserAdapter(getContext(),listUser.data));
                setListUser(listUser.data);
            }
        });
        toaDoViewModel.getResultInsertTram().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS){
                i++;
                if(i==1) {
                    Toast.makeText(getActivity().getApplicationContext(),"Thêm Trạm Thành Công",Toast.LENGTH_LONG).show();
                    thoat();
                }
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Lỗi Không thể thêm ",Toast.LENGTH_LONG).show();
            }
        });



        setAppLocal(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapToaDo);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView = (AutoCompleteTextView) getActivity().findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        setEventButton();


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentToaDoBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_toa_do, container, false,dataBindingComponent);
        toaDoBinding = new AutoClearedValue<>(this, dataBinding);
        ButterKnife.bind(this,dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public static ToaDo create( String token) {
        ToaDo toaDo = new ToaDo();
        Bundle args = new Bundle();
        args.putString(TOA_DO_TOKEN_KEY, token);
        toaDo.setArguments(args);
        return toaDo;
    }
    @OnTextChanged(R.id.edBanKinh)
    public void hienThiBanKinh(){
        try{
            Double banKinh=Double.parseDouble(toaDoBinding.get().edBanKinh.getText().toString());
            if(banKinh>0){
                setToaDoBTS(toaDoBinding.get().getViDo(),toaDoBinding.get().getKinhDo(),banKinh);
            }
        }
        catch (Exception e){

        }
    }
    @OnTextChanged(R.id.autoCompleteTextView)
    public void datTenTram(){
        try{
            toaDoBinding.get().edTenTram.setText("Trạm BTS "+toaDoBinding.get().autoCompleteTextView.getText().
                    toString().substring(0,toaDoBinding.get().autoCompleteTextView.getText().toString().indexOf(",")));
        }
        catch (Exception e){

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                ItemTramBinding itemTramBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_tram, null, false);
                itemTramBinding.setTentram(marker.getTitle());
                itemTramBinding.executePendingBindings();
                return itemTramBinding.getRoot();
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                Location location=mMap.getMyLocation();
                setToaDoBTS(location.getLatitude(),location.getLongitude(),toaDoBinding.get().getBanKinh());
                mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());
                //TODO: Any custom actions
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setToaDoBTS(latLng.latitude,latLng.longitude,toaDoBinding.get().getBanKinh());
                mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getSnippet()!=null&&marker.getSnippet().contains("user:")){
                    spQuanLy.setSelection(getIndexUser(listUser,marker.getSnippet().substring(marker.getSnippet().indexOf(":")+1)));
                }

                Toast.makeText(getActivity().getApplicationContext(),"Khoảng cách : "+
                        getDistance(markerChoose.getPosition(), marker.getPosition()
                        )+" m",Toast.LENGTH_LONG).show();
            }
        });
        LatLng sydney = new LatLng(16.077809, 108.154008);
        CameraPosition mCameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(12)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        markerChoose=mMap.addMarker(new MarkerOptions().position(sydney));
        circleChoose=mMap.addCircle(new CircleOptions().
                center(sydney)
                .strokeWidth(1f)
                .strokeColor(Color.RED)
                .fillColor(0x22CC0000)
        );
        setToaDoBTS(sydney.latitude,sydney.longitude,toaDoBinding.get().getBanKinh());
        mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());
    }
    public int getIndexUser(List<UserBTS> list,String idUser){
        int i=0;
        for (UserBTS user:list) {
            if(user.getIDUser().equals(idUser)) return i;
            i++;
        }
        return 0;
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
    @OnClick(R.id.btnThoatToaDo)
    public void thoat(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }
    @OnClick(R.id.btnLuuToaDo)
    public void luu(){
        if(!checkData()) return;
        Tram tram=new Tram();
        tram.setIDTram(0);
        tram.setTenTram(toaDoBinding.get().edTenTram.getText().toString());
        tram.setTinh(toaDoBinding.get().getDiaChi());
        tram.setKinhDo(toaDoBinding.get().getKinhDo());
        tram.setViDo(toaDoBinding.get().getViDo());
        UserBTS userBTS=((UserAdapter)spQuanLy.getAdapter()).getListData().get(spQuanLy.getSelectedItemPosition());
        tram.setIDQuanLy(userBTS.getIDUser());
        tram.setBanKinhPhuSong(toaDoBinding.get().getBanKinh());
        tram.setCotAnten(15);
        tram.setCotTiepDat(15);
        tram.setSanTram(15);
        toaDoViewModel.setInsertTram(tram);
    }
    public Boolean checkData(){
        if(toaDoBinding.get().edTenTram.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa nhập tên Trạm",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(toaDoBinding.get().autoCompleteTextView.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa chọn địa chỉ trạm",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(toaDoBinding.get().getBanKinh()<=0){
            Toast.makeText(getActivity().getApplicationContext(),"Bán kính trạm phải lớn hơn 0",Toast.LENGTH_LONG).show();
            return false;
        }
        try{
            ((UserAdapter)spQuanLy.getAdapter()).getListData().get(spQuanLy.getSelectedItemPosition());
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(),"Bạn chưa chọn người quản lý",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
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
            setToaDoBTS(place.getLatLng().latitude,place.getLatLng().longitude,toaDoBinding.get().getBanKinh());
            zoomToaDo();
        }
    };
    public void setToaDoBTS(Double viDo,Double kinhDo,Double banKinh){
        toaDoBinding.get().setViDo(viDo);
        toaDoBinding.get().setKinhDo(kinhDo);
        toaDoBinding.get().setBanKinh(banKinh);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(viDo, kinhDo, 1);
            toaDoBinding.get().setDiaChi(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        markerChoose.setPosition(new LatLng(viDo,kinhDo));
        circleChoose.setCenter(new LatLng(viDo,kinhDo));
        circleChoose.setRadius(banKinh * 1000);
    }
    public double getDistance(LatLng p1,LatLng p2){
        int R=6378137;
        double dLat=Math.toRadians(p2.latitude-p1.latitude);
        double dLong=Math.toRadians(p2.longitude-p1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(p1.latitude)) * Math.cos(Math.toRadians(p2.latitude)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d; // returns the distance in meter

    }
    public void setListTram(List<Tram> listTram){
        this.listTram=listTram;
        addTramToMap();
    }
    public void setListUser(List<UserBTS> listUser){
        this.listUser=listUser;
        addUserToMap();
    }

    public void addTramToMap(){
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_bts_3);
        icon = Bitmap.createScaledBitmap(icon, 100, 100, false);
        for (Tram item : listTram) {
            LatLng sydney = new LatLng(item.getViDo(), item.getKinhDo());

            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title(item.getTenTram())
                    .snippet(item.getIDTram() + "")
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))

            );
            mMap.addCircle(new CircleOptions().
                    center(sydney).
                    radius(item.getBanKinhPhuSong() * 1000)
                    .strokeWidth(1f)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
            );

        }
    }
    public void addUserToMap(){
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_user);
        icon = Bitmap.createScaledBitmap(icon, 50, 50, false);
        for (UserBTS item : listUser) {
            try{
                String toaDo=item.getDiaChi().substring(item.getDiaChi().indexOf("|")+1);
                Double viDo=Double.parseDouble(toaDo.substring(0,toaDo.indexOf(",")));
                Double kinhDo=Double.parseDouble(toaDo.substring(toaDo.indexOf(",")+1));
                LatLng sydney = new LatLng(viDo, kinhDo);

                mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(item.getTen())
                        .snippet("user:"+item.getIDUser())
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))

                );
            }catch (Exception e){

            }

        }
    }
    public void zoomToaDo(){
        CameraPosition mCameraPosition = new CameraPosition.Builder()
                .target(new LatLng(toaDoBinding.get().getViDo(),toaDoBinding.get().getKinhDo()))
                .zoom(15)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
    }
    public void tangBanKinh(){
        toaDoBinding.get().edBanKinh.setText((toaDoBinding.get().getBanKinh()+0.01)+"");

    }
    public void giamBanKinh(){
        double bk=toaDoBinding.get().getBanKinh()-0.01;
        if(bk<0) toaDoBinding.get().edBanKinh.setText("0");
        else toaDoBinding.get().edBanKinh.setText(bk+"");
    }
    public void leftToaDo(){
        setToaDoBTS(toaDoBinding.get().getViDo(),toaDoBinding.get().getKinhDo()-0.0001,toaDoBinding.get().getBanKinh());
        mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());

    }
    public void upToaDo(){
        setToaDoBTS(toaDoBinding.get().getViDo()+0.0001,toaDoBinding.get().getKinhDo(),toaDoBinding.get().getBanKinh());
        mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());


    }
    public void downToaDo(){
        setToaDoBTS(toaDoBinding.get().getViDo()-0.0001,toaDoBinding.get().getKinhDo(),toaDoBinding.get().getBanKinh());
        mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());

    }
    public void rightToaDo(){
        setToaDoBTS(toaDoBinding.get().getViDo(),toaDoBinding.get().getKinhDo()+0.0001,toaDoBinding.get().getBanKinh());
        mAutocompleteTextView.setText(toaDoBinding.get().getDiaChi());
    }
    class BanKinhUpdater implements Runnable {
        public void run() {
            if( mBanKinhIncrement ){
                tangBanKinh();
                repeatUpdateHandler.postDelayed( new BanKinhUpdater(), REP_DELAY );
            } else if( mBanKinhDecrement ){
                giamBanKinh();
                repeatUpdateHandler.postDelayed( new BanKinhUpdater(), REP_DELAY );
            }
            else if(mToaDoLeft){
                leftToaDo();
                repeatUpdateHandler.postDelayed( new BanKinhUpdater(), REP_DELAY );
            }
            else if(mToaDoRight){
                rightToaDo();
                repeatUpdateHandler.postDelayed( new BanKinhUpdater(), REP_DELAY );
            }
            else if(mToaDoUp){
                upToaDo();
                repeatUpdateHandler.postDelayed( new BanKinhUpdater(), REP_DELAY );
            }
            else if(mToaDoDown){
                downToaDo();
                repeatUpdateHandler.postDelayed( new BanKinhUpdater(), REP_DELAY );
            }
        }
    }
    public void setEventButton(){
        toaDoBinding.get().btnUpBanKinh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mBanKinhIncrement = true;
                repeatUpdateHandler.post( new BanKinhUpdater() );
                return false;
            }
        });
        toaDoBinding.get().btnUpBanKinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tangBanKinh();
            }
        });
        toaDoBinding.get().btnUpBanKinh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( (motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
                        && mBanKinhIncrement ){
                    mBanKinhIncrement = false;
                }
                return false;
            }
        });
        toaDoBinding.get().btnDownBanKinh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mBanKinhDecrement = true;
                repeatUpdateHandler.post( new BanKinhUpdater() );
                return false;
            }
        });
        toaDoBinding.get().btnDownBanKinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giamBanKinh();
            }
        });
        toaDoBinding.get().btnDownBanKinh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( (motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
                        && mBanKinhDecrement ){
                    mBanKinhDecrement = false;
                }
                return false;
            }
        });


        toaDoBinding.get().btnLeftToaDo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mToaDoLeft = true;
                repeatUpdateHandler.post( new BanKinhUpdater() );
                return false;
            }
        });
        toaDoBinding.get().btnLeftToaDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftToaDo();
            }
        });
        toaDoBinding.get().btnLeftToaDo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( (motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
                        && mToaDoLeft ){
                    mToaDoLeft = false;
                }
                return false;
            }
        });

        toaDoBinding.get().btnRightToaDo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mToaDoRight = true;
                repeatUpdateHandler.post( new BanKinhUpdater() );
                return false;
            }
        });
        toaDoBinding.get().btnRightToaDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightToaDo();
            }
        });
        toaDoBinding.get().btnRightToaDo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( (motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
                        && mToaDoRight ){
                    mToaDoRight = false;
                }
                return false;
            }
        });

        toaDoBinding.get().btnUpToaDo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mToaDoUp = true;
                repeatUpdateHandler.post( new BanKinhUpdater() );
                return false;
            }
        });
        toaDoBinding.get().btnUpToaDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upToaDo();
            }
        });
        toaDoBinding.get().btnUpToaDo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( (motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
                        && mToaDoUp ){
                    mToaDoUp = false;
                }
                return false;
            }
        });

        toaDoBinding.get().btnDownToaDo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mToaDoDown = true;
                repeatUpdateHandler.post( new BanKinhUpdater() );
                return false;
            }
        });
        toaDoBinding.get().btnDownToaDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downToaDo();
            }
        });
        toaDoBinding.get().btnDownToaDo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( (motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
                        && mToaDoDown ){
                    mToaDoDown = false;
                }
                return false;
            }
        });


    }

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
    public static void setAppLocal(Context mContext) {
        String languageToLoad = "vi";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity().getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}
