package com.example.justpizza;

import android.content.Context;
import android.os.Build;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderSummaryAdapter extends ArrayAdapter<OrderEntry> {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public OrderSummaryAdapter(Context context, ArrayList<OrderEntry> arrayList) {
        super(context, 0, arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.summary_list_item, parent, false);
        }
        OrderEntry current = getItem(position);
        TextView nameTextView = listItemView.findViewById(R.id.name);
        nameTextView.setText(current.getPizzaName());
        TextView quantityTextView = listItemView.findViewById(R.id.quantity);
        quantityTextView.setText(Integer.toString(current.getQuantity()));
        TextView priceTextView = listItemView.findViewById(R.id.price);
        priceTextView.setText(df.format(current.getPrice()));

        return listItemView;
    }
}