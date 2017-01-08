package com.hzj.eventcollection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hzj on 2017/1/8.
 */

public class ListViewAdapter extends BaseAdapter {
    private List<String> demos;

    public ListViewAdapter(List<String> demos) {
        super();
        this.demos = demos;
    }

    @Override
    public int getCount() {
        return demos.size();
    }

    @Override
    public Object getItem(int position) {
        return demos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);

            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(demos.get(position));
        return convertView;
    }

    static class ViewHolder {
        public TextView text;
        public ImageView icon;
    }
}
