package com.example.lequa.btsmanagement.ui.hinhanh;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemHinhAnhBinding;
import com.example.lequa.btsmanagement.model.HinhAnhTram;
import com.example.lequa.btsmanagement.ui.common.DataBoundListAdapter;
import com.example.lequa.btsmanagement.util.Objects;

public class HinhAnhAdapter extends DataBoundListAdapter<HinhAnhTram, ItemHinhAnhBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final HinhAnhTramClickCallback callback;
    public HinhAnhAdapter(DataBindingComponent dataBindingComponent,HinhAnhTramClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback=callback;
    }
    @Override
    protected ItemHinhAnhBinding createBinding(ViewGroup parent) {
        ItemHinhAnhBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_hinh_anh, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnLongClickListener(v -> {
            HinhAnhTram hinhAnhTram = binding.getHinhAnh();
            if (hinhAnhTram != null && callback != null) {
                callback.onClick(hinhAnhTram);
            }
            return false;
        });
        return binding;
    }

    @Override
    protected void bind(ItemHinhAnhBinding binding, HinhAnhTram item) {
        binding.setHinhAnh(item);
    }

    @Override
    protected boolean areItemsTheSame(HinhAnhTram oldItem, HinhAnhTram newItem) {
        return Objects.equals(oldItem.getIDHinhAnh(),newItem.getIDHinhAnh());
    }

    @Override
    protected boolean areContentsTheSame(HinhAnhTram oldItem, HinhAnhTram newItem) {
        return Objects.equals(oldItem.getIDTram(),newItem.getIDTram())
                &&oldItem.getTen().equals(newItem.getTen());
    }
    public interface HinhAnhTramClickCallback {
        void onClick(HinhAnhTram hinhAnhTram);
    }
}
