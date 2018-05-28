package com.example.lequa.btsmanagement.ui.dsmatdien;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemDsMatDienBinding;
import com.example.lequa.btsmanagement.model.MatDien;
import com.example.lequa.btsmanagement.ui.common.DataBoundListAdapter;
import com.example.lequa.btsmanagement.util.Objects;

public class DSMatDienAdapter extends DataBoundListAdapter<MatDien, ItemDsMatDienBinding> {
    private final android.databinding.DataBindingComponent dataBindingComponent;
    private final DSMatDienClickCallback callback;
    public DSMatDienAdapter(DataBindingComponent dataBindingComponent,DSMatDienClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback=callback;
    }
    @Override
    protected ItemDsMatDienBinding createBinding(ViewGroup parent) {
        ItemDsMatDienBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_ds_mat_dien, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            MatDien matDien = binding.getMatDien();
            if (matDien != null && callback != null) {
                callback.onClick(matDien);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemDsMatDienBinding binding, MatDien item) {
        binding.setMatDien(item);
    }

    @Override
    protected boolean areItemsTheSame(MatDien oldItem, MatDien newItem) {
        return Objects.equals(oldItem.getIDMatDien(),newItem.getIDMatDien());
    }

    @Override
    protected boolean areContentsTheSame(MatDien oldItem, MatDien newItem) {
        return Objects.equals(oldItem.getNgayMatDien(),newItem.getNgayMatDien())
                && oldItem.getGioMatDien().equals(newItem.getGioMatDien())
                && oldItem.getIDTram().equals(newItem.getIDTram())
                && oldItem.getThoiGianMayNo().equals(newItem.getThoiGianMayNo())
                && oldItem.getThoiGianNgung().equals(newItem.getThoiGianNgung())
                && oldItem.getTongThoiGianChay().equals(newItem.getTongThoiGianChay())
                && oldItem.getQuangDuongDiChuyen().equals(newItem.getQuangDuongDiChuyen())
                && oldItem.getTienPhat().equals(newItem.getTienPhat());
    }
    public interface DSMatDienClickCallback {
        void onClick(MatDien matDien);
    }
}
