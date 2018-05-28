package com.example.lequa.btsmanagement.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.model.NhaMang;

import java.util.List;

public class NhaMangAdapter extends BaseAdapter {

    private List<NhaMang> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    NhaMang nhaMangTMP=new NhaMang(0,"Tất Cả","");


    public NhaMangAdapter(Context aContext,  List<NhaMang> listData) {
        this.context = aContext;
        this.listData = listData;
        if (listData.get(listData.size()-1).getIDNhaMang()!=0) listData.add(nhaMangTMP);
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_tinh, null);
            holder = new ViewHolder();
            holder.tenNhaMang = (TextView) convertView.findViewById(R.id.tenTinh);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NhaMang nhaMang = this.listData.get(position);
        holder.tenNhaMang.setText(nhaMang.getTenNhaMang());
        return convertView;
    }

    static class ViewHolder {
        TextView tenNhaMang;
    }
    public List<NhaMang> getListData(){
        return listData;
    }

}
