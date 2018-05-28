package com.example.lequa.btsmanagement.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.binding.FragmentDataBindingComponent;
import com.example.lequa.btsmanagement.databinding.FragmentMainBinding;
import com.example.lequa.btsmanagement.databinding.NavHeaderMainBinding;
import com.example.lequa.btsmanagement.databinding.ItemTramBinding;
import com.example.lequa.btsmanagement.di.Injectable;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.ui.common.NavigationController;
import com.example.lequa.btsmanagement.ui.edit.UserAdapter;
import com.example.lequa.btsmanagement.util.AutoClearedValue;
import com.example.lequa.btsmanagement.vo.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LOCATION_SERVICE;

public class MainFragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener, Injectable, OnMapReadyCallback {
    private GoogleMap mMap;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentMainBinding> mainBinding;
    AutoClearedValue<NavHeaderMainBinding> navBinding;
    ArrayAdapter<Tram> adapter;
    SearchView.SearchAutoComplete searchAutoComplete;


    private static final String MAIN_TOKEN_KEY = "main_token";
    private static final String MAIN_EMAIL_KEY = "email_token";
    private static final String MAIN_CV_KEY = "cv_token";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private MainViewModel mainViewModel;

    private Animation show_fab,hide_fab;
    @BindView(R.id.fab_chi_tiet_tram)
    FloatingActionButton fabChiTietTram;
    @BindView(R.id.fab_nhat_ky) FloatingActionButton fabNhatKy;
    @BindView(R.id.fab_hinh_anh) FloatingActionButton fabHinhAnh;
    @BindView(R.id.fab_them_tram) FloatingActionButton fabThemTram;
    @BindView(R.id.fab_mat_dien) FloatingActionButton fabMatDien;

    private String idMarkerClick = null;
    private String idUserClick=null;
    private boolean tramClick=false;
    private boolean userClick=false;

    public static LatLng sydney=new LatLng(16.077809, 108.154008);
    List<UserBTS> listUser=new ArrayList<>();
    List<Tram> listTram=new ArrayList<>();

    private static String tokenTMP,emailTMP,chucVuTMP;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        adapter=new ArrayAdapter<Tram>(getActivity(), android.R.layout.simple_dropdown_item_1line, listTram);

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        tokenTMP=getArguments().getString(MAIN_TOKEN_KEY);
        emailTMP=getArguments().getString(MAIN_EMAIL_KEY);
        chucVuTMP=getArguments().getString(MAIN_CV_KEY);

        mainViewModel.setUser(getArguments().getString(MAIN_EMAIL_KEY), getArguments().getString(MAIN_TOKEN_KEY));
        mainViewModel.getUser().observe(this, user -> {
            navBinding.get().setUser(user.data);
            navBinding.get().executePendingBindings();
        });
        mainViewModel.getListTram().observe(this, tram -> {
            if (tram.status == Status.SUCCESS)
                //addTramToMap(tram.data);
                setListTram(tram.data);
        });
        mainViewModel.getListUser().observe(this,listUser->{
            if(listUser.status== Status.SUCCESS){
                setListUser(listUser.data);
            }
        });

        mainViewModel.getResultDeleteTram().observe(this,result->{
            int i=0;
            if(result.status==Status.SUCCESS&&result.data!=null){
                //i++;
                //if(i==1)
                    Toast.makeText(getActivity().getApplicationContext(),"Delete Thành Công",Toast.LENGTH_LONG).show();
            }
            else if(result.status==Status.ERROR){
                i++;
                if(i==1) Toast.makeText(getActivity().getApplicationContext(),"Delete lỗi "+result.message,Toast.LENGTH_LONG).show();
            }
        });

        show_fab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_show);
        hide_fab = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab_hide);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        if(getArguments().getString(MAIN_CV_KEY).equals("Admin"))
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        else if(getArguments().getString(MAIN_CV_KEY).equals("QuanLy"))
            navigationView.inflateMenu(R.menu.user_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);



        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 230);

        mapFragment.getMapAsync(this);

        phanQuyenGiaoDien();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentMainBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_main, container, false, dataBindingComponent);
        mainBinding = new AutoClearedValue<>(this, dataBinding);
        NavHeaderMainBinding dataNavBinding = DataBindingUtil.inflate(inflater, R.layout.nav_header_main, mainBinding.get().navView, false,
                dataBindingComponent);
        navBinding = new AutoClearedValue<>(this, dataNavBinding);
        mainBinding.get().navView.addHeaderView(dataNavBinding.getRoot());
        ButterKnife.bind(this, dataBinding.getRoot());
        return dataBinding.getRoot();
    }
    public void phanQuyenGiaoDien(){
        if(getArguments().getString(MAIN_CV_KEY).equals("Admin")){
        }
        else if(getArguments().getString(MAIN_CV_KEY).equals("QuanLy")){
            fabThemTram.hide();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ca_nhan) {
            navigationController.navigateToCaNhan(getArguments().getString(MAIN_EMAIL_KEY), getArguments().getString(MAIN_TOKEN_KEY));
            // Handle the camera action
        } else if (id == R.id.nav_tai_khoan) {
            navigationController.navigateToTaiKhoan(getArguments().getString(MAIN_TOKEN_KEY));

        } else if (id == R.id.nav_tram) {
            navigationController.navigateToDSTram(getArguments().getString(MAIN_TOKEN_KEY));

        } else if (id == R.id.nav_nha_mang) {
            navigationController.navigateToDSMang(getArguments().getString(MAIN_TOKEN_KEY));
        }
//        } else if (id == R.id.nav_tim_nang_cao) {
//            //showNhaMang();
//
//        }
//        else if (id == R.id.nav_mat_dien) {
//            navigationController.navigateToThongKe();
//        }
        else if (id == R.id.nav_bao_cao) {
            navigationController.navigateToBaoCao(getArguments().getString(MAIN_CV_KEY),getArguments().getString(MAIN_TOKEN_KEY));

        }  else if (id == R.id.nav_thong_tin) {
            navigationController.navigateToThongTin();

        } else if (id == R.id.nav_dang_xuat) {
            navigationController.navigateToLogin(true);

        }
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                sydney=marker.getPosition();
                marker.showInfoWindow();
                //Animations
                if(!marker.getSnippet().contains("user:")){
                    tramClick=true;userClick=false;
                    idMarkerClick = marker.getSnippet();
                    fabChiTietTram.startAnimation(show_fab);
                    fabNhatKy.startAnimation(show_fab);
                    fabHinhAnh.startAnimation(show_fab);
                    fabMatDien.startAnimation(show_fab);
                }else{
                    tramClick=false;userClick=true;
                    fabChiTietTram.startAnimation(show_fab);
                    fabNhatKy.startAnimation(hide_fab);
                    fabHinhAnh.startAnimation(hide_fab);
                    fabMatDien.startAnimation(hide_fab);
                    idUserClick=marker.getSnippet().substring(marker.getSnippet().indexOf(":")+1);
                }
                return true;
            }
        });
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                if(!marker.getSnippet().contains("user:"))
                deleteTram(Integer.parseInt(marker.getSnippet()));
            }
        });
        zoomCamera(12);
    }
    public void zoomCamera(int zoom){
        CameraPosition mCameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(zoom)
                .build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
    }
    public static MainFragment create() {
        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MAIN_EMAIL_KEY, emailTMP);
        args.putString(MAIN_TOKEN_KEY, tokenTMP);
        args.putString(MAIN_CV_KEY, chucVuTMP);
        mainFragment.setArguments(args);
        return mainFragment;
    }
    public static MainFragment create(String email, String token,String chucVu) {
        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MAIN_EMAIL_KEY, email);
        args.putString(MAIN_TOKEN_KEY, token);
        args.putString(MAIN_CV_KEY, chucVu);
        mainFragment.setArguments(args);
        return mainFragment;
    }
    public void setListTram(List<Tram> listTram){
        this.listTram=listTram;
        addTramToAdapter();
        addTramToMap();
    }
    public void addTramToAdapter(){
        adapter=new ArrayAdapter<Tram>(getActivity(), android.R.layout.simple_dropdown_item_1line, listTram);
        searchAutoComplete.setAdapter(adapter);
    }
    public void setListUser(List<UserBTS> listUser){
        this.listUser=listUser;
        addTramToMap();
    }

    public void addTramToMap() {
        mMap.clear();
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
        addUserToMap();

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


    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        // Get SearchView autocomplete object.
        searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchAutoComplete.setThreshold(1);

        searchAutoComplete.setAdapter(adapter);


        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                Tram selected=(Tram)adapterView.getAdapter().getItem(itemIndex);
                searchAutoComplete.setText(selected.getTenTram());
                sydney=new LatLng(selected.getViDo(),selected.getKinhDo());
                zoomCamera(15);
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.action_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.action_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.action_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                break;
        }

        return false;
    }

    @OnClick(R.id.fab_chi_tiet_tram)
    public void showChiTietTram() {
        if(tramClick){
                navigationController.navigateToTram(idMarkerClick, getArguments().getString(MAIN_TOKEN_KEY),getArguments().getString(MAIN_CV_KEY));
        }
        else if(userClick)
            navigationController.navigateToChiTietTaiKhoan(idUserClick, getArguments().getString(MAIN_TOKEN_KEY));

    }

    @OnClick(R.id.fab_them_tram)
    public void showThemTram() {
        navigationController.navigateToToaDo(getArguments().getString(MAIN_TOKEN_KEY));
    }
    @OnClick(R.id.fab_hinh_anh)
    public void showHinhAnhTram() {
        navigationController.navigateToHinhAnh(idMarkerClick, getArguments().getString(MAIN_TOKEN_KEY));

    }
    @OnClick(R.id.fab_mat_dien)
    public void showMatDien(){
        navigationController.navigateToDSMatDien(idMarkerClick,getArguments().getString(MAIN_CV_KEY), getArguments().getString(MAIN_TOKEN_KEY));
    }
    @OnClick(R.id.fab_nhat_ky)
    public void showNhatKy(){
        navigationController.navigateToDSNhatKy(idMarkerClick,getArguments().getString(MAIN_CV_KEY), getArguments().getString(MAIN_TOKEN_KEY));
    }
    public void deleteTram(int idTram){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Xóa Trạm")
                .setMessage("Bạn có chắc chắn muốn xóa trạm này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        mainViewModel.setDeleteTram(idTram,true);
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
    public void showNhaMang(){
        AlertDialog.Builder mBuilder ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            mBuilder = new AlertDialog.Builder(getContext());
        }
        View mView = getLayoutInflater().inflate(R.layout.popup_list_nha_mang, null);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
        Spinner spNhaMang=mView.findViewById(R.id.spNhaMang);
        Button btnChon=mView.findViewById(R.id.btnChonNhaMang);
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NhaMang nhaMang=((NhaMangAdapter)spNhaMang.getAdapter()).getListData().get(spNhaMang.getSelectedItemPosition());
                Toast.makeText(getActivity().getApplicationContext(),nhaMang.getIDNhaMang()+"",Toast.LENGTH_LONG).show();
//                Tram tram=tramBinding.get().getTram();
//                tram.setIDQuanLy(userBTS.getIDUser());
//                tramViewModel.setUpdateTram(tram);
                dialog.dismiss();
            }
        });

        mainViewModel.setDisplayListNhaMang(true);
        mainViewModel.getListNhaMang().observe(this,listNhaMang->{
            if(listNhaMang.status==Status.SUCCESS){
                spNhaMang.setAdapter(new NhaMangAdapter(getContext(),listNhaMang.data));
            }
        });
    }

}
