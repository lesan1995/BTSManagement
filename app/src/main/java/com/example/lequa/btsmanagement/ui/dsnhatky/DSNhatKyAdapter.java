package com.example.lequa.btsmanagement.ui.dsnhatky;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemNhatKyBinding;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.ui.common.DataBoundListAdapter;
import com.example.lequa.btsmanagement.util.Objects;

public class DSNhatKyAdapter extends DataBoundListAdapter<NhatKy, ItemNhatKyBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final DSNhatKyClickCallback callback;
    private final DSNhatKyClickLongCallback longCallback;
    public DSNhatKyAdapter(DataBindingComponent dataBindingComponent, DSNhatKyClickCallback callback,DSNhatKyClickLongCallback longCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback=callback;
        this.longCallback=longCallback;
    }
    @Override
    protected ItemNhatKyBinding createBinding(ViewGroup parent) {
        ItemNhatKyBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_nhat_ky, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            NhatKy nhatKy = binding.getNhatKy();
            if (nhatKy != null && callback != null) {
                callback.onClick(nhatKy);
            }
        });
        binding.getRoot().setOnLongClickListener(v -> {
            NhatKy nhatKy = binding.getNhatKy();
            if (nhatKy != null && longCallback != null) {
                longCallback.onClick(nhatKy);
            }
            return true;
        });
        return binding;
    }

    @Override
    protected void bind(ItemNhatKyBinding binding, NhatKy item) {
        binding.setNhatKy(item);
    }

    @Override
    protected boolean areItemsTheSame(NhatKy oldItem, NhatKy newItem) {
        return Objects.equals(oldItem.getIDNhatKy(),newItem.getIDNhatKy());
    }

    @Override
    protected boolean areContentsTheSame(NhatKy oldItem, NhatKy newItem) {
        return Objects.equals(oldItem.getIDTram(),newItem.getIDTram())
                && oldItem.getIDQuanLy().equals(newItem.getIDQuanLy())
                && oldItem.getLoai().equals(newItem.getLoai())
                && oldItem.getTieuDe().equals(newItem.getTieuDe())
                && oldItem.getNoiDung().equals(newItem.getNoiDung())
                && oldItem.getThoiGian().equals(newItem.getThoiGian())
                && oldItem.getDaGiaiQuyet().equals(newItem.getDaGiaiQuyet());
    }
    public interface DSNhatKyClickCallback {
        void onClick(NhatKy nhatKy);
    }
    public interface DSNhatKyClickLongCallback {
        void onClick(NhatKy nhatKy);
    }
}

