package com.example.lequa.btsmanagement.ui.taikhoan;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemTaiKhoanBinding;
import com.example.lequa.btsmanagement.model.UserBTS;
import com.example.lequa.btsmanagement.ui.common.DataBoundListAdapter;
import com.example.lequa.btsmanagement.util.Objects;

public class TaiKhoanAdapter extends DataBoundListAdapter<UserBTS, ItemTaiKhoanBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final TaiKhoanClickCallback callback;
    public TaiKhoanAdapter(DataBindingComponent dataBindingComponent,TaiKhoanClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback=callback;
    }
    @Override
    protected ItemTaiKhoanBinding createBinding(ViewGroup parent) {
        ItemTaiKhoanBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_tai_khoan, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            UserBTS userBTS = binding.getUser();
            if (userBTS != null && callback != null) {
                callback.onClick(userBTS);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemTaiKhoanBinding binding, UserBTS item) {
        binding.setUser(item);
    }

    @Override
    protected boolean areItemsTheSame(UserBTS oldItem, UserBTS newItem) {
        return Objects.equals(oldItem.getIDUser(),newItem.getIDUser());
    }

    @Override
    protected boolean areContentsTheSame(UserBTS oldItem, UserBTS newItem) {
        return Objects.equals(oldItem.getChucVu(),newItem.getChucVu())
                && oldItem.getTen().equals(newItem.getTen())
                && oldItem.getDiaChi().equals(newItem.getDiaChi())
                && oldItem.getEmail().equals(newItem.getEmail())
                && oldItem.getGioiTinh().equals(newItem.getGioiTinh())
                && oldItem.getImage().equals(newItem.getImage())
                && oldItem.getNgaySinh().equals(newItem.getNgaySinh())
                && oldItem.getPhone().equals(newItem.getPhone());
    }
    public interface TaiKhoanClickCallback {
        void onClick(UserBTS userBTS);
    }
}
