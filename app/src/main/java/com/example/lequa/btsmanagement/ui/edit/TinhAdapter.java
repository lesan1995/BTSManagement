package com.example.lequa.btsmanagement.ui.edit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.tinh.Tinh;

import java.util.List;

public class TinhAdapter  extends BaseAdapter {

    private List<Tinh> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public TinhAdapter(Context aContext,  List<Tinh> listData) {
        this.context = aContext;
        this.listData = listData;
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
            holder.tenTinh = (TextView) convertView.findViewById(R.id.tenTinh);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Tinh tinh = this.listData.get(position);
        holder.tenTinh.setText(tinh.getName());
        return convertView;
    }

    static class ViewHolder {
        TextView tenTinh;
    }

}
