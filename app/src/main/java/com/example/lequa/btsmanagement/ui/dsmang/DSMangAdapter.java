package com.example.lequa.btsmanagement.ui.dsmang;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemDsNhaMangBinding;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.ui.common.DataBoundListAdapter;
import com.example.lequa.btsmanagement.util.Objects;

public class DSMangAdapter extends DataBoundListAdapter<NhaMang, ItemDsNhaMangBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final NhaMangClickCallback callback;
    public DSMangAdapter(DataBindingComponent dataBindingComponent, NhaMangClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback=callback;
    }
    @Override
    protected ItemDsNhaMangBinding createBinding(ViewGroup parent) {
        ItemDsNhaMangBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_ds_nha_mang, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            NhaMang nhaMang = binding.getNhaMang();
            if (nhaMang != null && callback != null) {
                callback.onClick(nhaMang);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemDsNhaMangBinding binding, NhaMang item) {
        binding.setNhaMang(item);
    }

    @Override
    protected boolean areItemsTheSame(NhaMang oldItem, NhaMang newItem) {
        return Objects.equals(oldItem.getIDNhaMang(),newItem.getIDNhaMang());
    }

    @Override
    protected boolean areContentsTheSame(NhaMang oldItem, NhaMang newItem) {
        return Objects.equals(oldItem.getTenNhaMang(),newItem.getTenNhaMang())
                && oldItem.getImage().equals(newItem.getImage());
    }
    public interface NhaMangClickCallback {
        void onClick(NhaMang nhaMang);
    }
}
