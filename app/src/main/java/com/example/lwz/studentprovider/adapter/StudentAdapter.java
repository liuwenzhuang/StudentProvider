package com.example.lwz.studentprovider.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lwz on 2015/7/17.
 */
public class StudentAdapter extends BaseAdapter {
    private Context context = null;
    private List<String> list = null;

    public StudentAdapter(Context context) {
        this.context = context;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null)
            return list.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if(convertView != null)
            textView = (TextView) convertView;
        else
            textView = new TextView(context);
        if(textView != null && list != null)
            textView.setText(list.get(position));
        return textView;
    }
}
