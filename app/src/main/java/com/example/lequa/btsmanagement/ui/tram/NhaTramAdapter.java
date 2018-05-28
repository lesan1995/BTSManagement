package com.example.lequa.btsmanagement.ui.tram;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.databinding.ItemNhaTramBinding;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import java.util.Collections;
import java.util.List;

public class NhaTramAdapter  extends BaseAdapter {
    private List<NhaTram> listNhaTram= Collections.emptyList();
    private List<NhaMang> listNhaMang= Collections.emptyList();
    private final DataBindingComponent dataBindingComponent;
    private final NhaTramClickCallback callback;
    private final NhaTramClickLongCallback longCallback;
    public NhaTramAdapter(DataBindingComponent dataBindingComponent,NhaTramClickCallback callback,
                          NhaTramClickLongCallback longCallback) {
        this.dataBindingComponent=dataBindingComponent;
        this.callback=callback;
        this.longCallback=longCallback;
    }

    @Override
    public int getCount() {
        return listNhaTram.size();
    }

    @Override
    public Object getItem(int position) {
        return listNhaTram.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NhaTramAdapter.ViewHolder holder;
        if (convertView == null) {
            ItemNhaTramBinding binding= DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_nha_tram,parent,false,dataBindingComponent);

            convertView = binding.getRoot();
            holder = new NhaTramAdapter.ViewHolder(binding);
            convertView.setTag(holder);
        } else {
            holder = (NhaTramAdapter.ViewHolder) convertView.getTag();
        }
        NhaTram nhaTram=this.listNhaTram.get(position);
        holder.binding.setNhaTram(nhaTram);
        for (NhaMang nhaMang:listNhaMang) {
            if(nhaMang.getIDNhaMang().equals(nhaTram.getIDNhaMang())){
                holder.binding.setNhaMang(nhaMang);
                break;
            }
        }
        holder.binding.executePendingBindings();
        convertView.setOnClickListener(v->{
            callback.onClick(nhaTram);
        });
        convertView.setOnLongClickListener(v->{
            longCallback.onClick(nhaTram);
            return true;
        });
        return convertView;

    }
    public void replaceNhaTram(List<NhaTram> listNhaTram){
        this.listNhaTram=listNhaTram;
        this.notifyDataSetChanged();
    }
    public void replaceNhaMang(List<NhaMang> listNhaMang){
        this.listNhaMang=listNhaMang;
        this.notifyDataSetChanged();

    }
    static class ViewHolder {
        public final ItemNhaTramBinding binding;
        ViewHolder(ItemNhaTramBinding binding){
            this.binding=binding;
        }
    }
    public interface NhaTramClickCallback {
        void onClick(NhaTram nhaTram);
    }
    public interface NhaTramClickLongCallback {
        void onClick(NhaTram nhaTram);
    }

}
