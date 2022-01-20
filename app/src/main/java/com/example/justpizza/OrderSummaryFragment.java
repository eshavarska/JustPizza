package com.example.justpizza;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderSummaryFragment extends Fragment {

    public OrderSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.order_summary_list, container, false);

        // Create a list students
        final ArrayList<OrderEntry> arrayList = readFromDB();


        OrderSummaryAdapter adapter = new OrderSummaryAdapter(getActivity(), arrayList);

        final ListView listView = rootview.findViewById(R.id.order_summary_list_view);
        listView.setAdapter(adapter);

        return rootview;
    }

    private ArrayList<OrderEntry> readFromDB() {
        ArrayList<OrderEntry> arrayList = new ArrayList<>();
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(getContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                MyDataBaseHelper.ORDERS_TABLE_NAME,
                new String[]{
                        MyDataBaseHelper.ORDERS_COLUMN_PIZZA_NAME, MyDataBaseHelper.ORDERS_COLUMN_ADDITIVES,MyDataBaseHelper.ORDERS_COLUMN_QUANTITY, MyDataBaseHelper.ORDERS_COLUMN_PRICE},
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
        // получаване на индексите и стойностите на колоните
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_PIZZA_NAME));
            String additives = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_ADDITIVES));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_QUANTITY));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_PRICE));

            arrayList.add(new OrderEntry(name, additives, quantity, price));
        }

        cursor.close();


        return arrayList;
    }
}