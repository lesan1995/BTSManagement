package com.example.lequa.btsmanagement.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lequa.btsmanagement.R;
import com.example.lequa.btsmanagement.model.Tram;

import java.util.List;

public class TramAdapter extends ArrayAdapter<Tram> {

    private List<Tram> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public TramAdapter (Context context, int resourceId,List<Tram> item)
    {
        super(context, resourceId,item);
        this.listData=item;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Tram getItem(int position) {
        return listData.get(position);
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TramAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_tinh, null);
            holder = new TramAdapter.ViewHolder();
            holder.tenTram = (TextView) convertView.findViewById(R.id.tenTinh);
            convertView.setTag(holder);
        } else {
            holder = (TramAdapter.ViewHolder) convertView.getTag();
        }
        Tram tram = getItem(position);
        holder.tenTram.setText(tram.getTenTram());
        return convertView;
    }

    static class ViewHolder {
        TextView tenTram;
    }
    public List<Tram> getListData(){
        return listData;
    }
    public void replaceTram(List<Tram> listData){
        this.listData=listData;
        this.notifyDataSetChanged();

    }

}

