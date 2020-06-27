package com.example.cafepaymentreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PayAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CafeMenu> menu;

    public PayAdapter(Context context, ArrayList<CafeMenu> data) {
        mContext = context;
        menu = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CafeMenu getItem(int position) {
        return menu.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_view, null);

        TextView name = (TextView)view.findViewById(R.id.menu_name);
        TextView value = (TextView)view.findViewById(R.id.menu_value);


        if(menu.get(position).getCount() != 0) {
            name.setText(menu.get(position).getName());
            value.setText(String.valueOf(menu.get(position).getCount()));
        } else {
            name.setText("");
            value.setText("");
        }
        return view;
    }
}
