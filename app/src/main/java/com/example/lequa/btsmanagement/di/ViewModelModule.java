package com.example.lequa.btsmanagement.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.lequa.btsmanagement.ui.addmatdien.AddMatDienViewModel;
import com.example.lequa.btsmanagement.ui.addnhatky.AddNhatKyViewModel;
import com.example.lequa.btsmanagement.ui.addnhatram.AddNhaTramViewModel;
import com.example.lequa.btsmanagement.ui.addtaikhoan.AddTaiKhoanViewModel;
import com.example.lequa.btsmanagement.ui.addtram.AddTramViewModel;
import com.example.lequa.btsmanagement.ui.baocao.BaoCaoViewModel;
import com.example.lequa.btsmanagement.ui.canhan.CaNhanViewModel;
import com.example.lequa.btsmanagement.ui.changepassword.ChangePasswordViewModel;
import com.example.lequa.btsmanagement.ui.chitietmatdien.ChiTietMatDienViewModel;
import com.example.lequa.btsmanagement.ui.chittiettaikhoan.ChiTietTaiKhoanViewModel;
import com.example.lequa.btsmanagement.ui.dsmang.DSMangViewModel;
import com.example.lequa.btsmanagement.ui.dsmatdien.DSMatDienViewModel;
import com.example.lequa.btsmanagement.ui.dsnhatky.DSNhatKyViewModel;
import com.example.lequa.btsmanagement.ui.dstram.DSTramViewModel;
import com.example.lequa.btsmanagement.ui.editcanhan.EditCaNhanViewModel;
import com.example.lequa.btsmanagement.ui.edittoado.EditToaDoViewModel;
import com.example.lequa.btsmanagement.ui.hinhanh.HinhAnhViewModel;
import com.example.lequa.btsmanagement.ui.login.LoginViewModel;
import com.example.lequa.btsmanagement.ui.map.MainViewModel;
import com.example.lequa.btsmanagement.ui.nhatky.NhatKyViewModel;
import com.example.lequa.btsmanagement.ui.nhatram.NhaTramViewModel;
import com.example.lequa.btsmanagement.ui.taikhoan.TaiKhoanViewModel;
import com.example.lequa.btsmanagement.ui.toado.ToaDoViewModel;
import com.example.lequa.btsmanagement.ui.tram.TramViewModel;
import com.example.lequa.btsmanagement.viewmodel.BTSViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(TramViewModel.class)
    abstract ViewModel bindTramViewModel(TramViewModel tramViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(NhaTramViewModel.class)
    abstract ViewModel bindNhaTramViewModel(NhaTramViewModel nhaTramViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(AddNhaTramViewModel.class)
    abstract ViewModel bindAddNhaTramViewModel(AddNhaTramViewModel addNhaTramViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(AddTramViewModel.class)
    abstract ViewModel bindAddTramViewModel(AddTramViewModel addTramViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(TaiKhoanViewModel.class)
    abstract ViewModel bindTaiKhoanViewModel(TaiKhoanViewModel taiKhoanViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(ChiTietTaiKhoanViewModel.class)
    abstract ViewModel bindChiTietTaiKhoanViewModel(ChiTietTaiKhoanViewModel chitietTaiKhoanViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(HinhAnhViewModel.class)
    abstract ViewModel bindHinhAnhViewModel(HinhAnhViewModel hinhAnhViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(ToaDoViewModel.class)
    abstract ViewModel bindToaDoViewModel(ToaDoViewModel toaDoViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(EditToaDoViewModel.class)
    abstract ViewModel bindEditToaDoViewModel(EditToaDoViewModel editToaDoViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(AddTaiKhoanViewModel.class)
    abstract ViewModel bindAddTaiKhoanViewModel(AddTaiKhoanViewModel addTaiKhoanViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(DSTramViewModel.class)
    abstract ViewModel bindDSTramViewModel(DSTramViewModel dsTramViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(CaNhanViewModel.class)
    abstract ViewModel bindCaNhanViewModel(CaNhanViewModel caNhanViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(EditCaNhanViewModel.class)
    abstract ViewModel bindEditCaNhanViewModel(EditCaNhanViewModel editCaNhanViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel.class)
    abstract ViewModel bindChangePasswordViewModel(ChangePasswordViewModel changePasswordViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(DSMatDienViewModel.class)
    abstract ViewModel bindDSMatDienViewModel(DSMatDienViewModel dsMatDienViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(ChiTietMatDienViewModel.class)
    abstract ViewModel bindChiTietMatDienViewModel(ChiTietMatDienViewModel chiTietMatDienViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(AddMatDienViewModel.class)
    abstract ViewModel bindAddMatDienViewModel(AddMatDienViewModel addMatDienViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(DSMangViewModel.class)
    abstract ViewModel bindDSMangViewModel(DSMangViewModel dsMangViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(DSNhatKyViewModel.class)
    abstract ViewModel bindDSNhatKyViewModel(DSNhatKyViewModel dsNhatKyViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(NhatKyViewModel.class)
    abstract ViewModel bindNhatKyViewModel(NhatKyViewModel nhatKyViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(BaoCaoViewModel.class)
    abstract ViewModel bindBaoCaoViewModel(BaoCaoViewModel baoCaoViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(AddNhatKyViewModel.class)
    abstract ViewModel bindAddNhatKyViewModel(AddNhatKyViewModel addNhatKyViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(BTSViewModelFactory factory);
}