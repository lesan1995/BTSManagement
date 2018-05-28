package com.example.lequa.btsmanagement.ui.dstram;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemDsTramBinding;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.ui.common.DataBoundListAdapter;
import com.example.lequa.btsmanagement.util.Objects;

public class DSTramAdapter extends DataBoundListAdapter<Tram, ItemDsTramBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final DSTramClickCallback callback;
    public DSTramAdapter(DataBindingComponent dataBindingComponent,DSTramClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback=callback;
    }
    @Override
    protected ItemDsTramBinding createBinding(ViewGroup parent) {
        ItemDsTramBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_ds_tram, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Tram tram = binding.getTram();
            if (tram != null && callback != null) {
                callback.onClick(tram);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemDsTramBinding binding, Tram item) {
        binding.setTram(item);
    }

    @Override
    protected boolean areItemsTheSame(Tram oldItem, Tram newItem) {
        return Objects.equals(oldItem.getIDTram(),newItem.getIDTram());
    }

    @Override
    protected boolean areContentsTheSame(Tram oldItem, Tram newItem) {
        return Objects.equals(oldItem.getTenTram(),newItem.getTenTram())
                && oldItem.getIDQuanLy().equals(newItem.getIDQuanLy())
                && oldItem.getTinh().equals(newItem.getTinh())
                && oldItem.getKinhDo().equals(newItem.getKinhDo())
                && oldItem.getViDo().equals(newItem.getViDo())
                && oldItem.getBanKinhPhuSong().equals(newItem.getBanKinhPhuSong())
                && oldItem.getCotAnten().equals(newItem.getCotAnten())
                && oldItem.getCotTiepDat().equals(newItem.getCotTiepDat())
                && oldItem.getSanTram().equals(newItem.getSanTram());
    }
    public interface DSTramClickCallback {
        void onClick(Tram tram);
    }
}

