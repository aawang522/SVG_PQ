package com.svg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.svg.R;
import com.svg.bean.ShijianBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件adatper
 * Created by Administrator on 2018/4/4.
 */

public class AdapterShijian extends BaseAdapter {
    List<ShijianBean> list = new ArrayList<>();
    Context mContext;
    private LayoutInflater inflater;

    public AdapterShijian(Context context, List<ShijianBean> listInfo) {
        this.mContext = context;
        list = listInfo;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView经过缓存后，不需要重复渲染，提高性能
        if(null == convertView){
            convertView = inflater.inflate(R.layout.adapter_shijianiitem, null);
        }
        ViewHolder viewHolder = getViewHolder(convertView);
        ShijianBean info = list.get(position);

        viewHolder.txtDate.setText(info.getDate());
        viewHolder.txtTime.setText(info.getTime());
        viewHolder.txtName.setText(info.getTitle());
        viewHolder.txtValue.setText(info.getValue());
        return convertView;
    }

    private class ViewHolder {
        private TextView txtDate, txtTime, txtName, txtValue;

        ViewHolder(View view) {
            txtDate =(TextView)view.findViewById(R.id.txtDate);
            txtTime =(TextView)view.findViewById(R.id.txtTime);
            txtName =(TextView)view.findViewById(R.id.txtName);
            txtValue =(TextView)view.findViewById(R.id.txtValue);
        }
    }

    private ViewHolder getViewHolder(View view){
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        if(null == viewHolder){
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }
}
