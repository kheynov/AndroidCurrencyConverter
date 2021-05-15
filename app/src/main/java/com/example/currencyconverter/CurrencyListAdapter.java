package com.example.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<CurrencyListItem> list;

    @Override
    public int getCount() {
        return list.size();
    }

    public CurrencyListAdapter(Context context, ArrayList<CurrencyListItem> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CurrencyListItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.fullName = view.findViewById(R.id.item_full_name);
            viewHolder.shortName = view.findViewById(R.id.item_short_name);
            viewHolder.price = view.findViewById(R.id.item_price);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        CurrencyListItem item = getItem(position);
        viewHolder.fullName.setText(item.getFullName());
        viewHolder.shortName.setText(item.getShortName());
        viewHolder.price.setText(item.getPrice());
        return view;
    }

    private static class ViewHolder {
        private TextView fullName;
        private TextView shortName;
        private TextView price;
    }
}
