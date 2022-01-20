package com.example.justpizza;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PizzaAdapter extends BaseAdapter {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    Context context;
    ArrayList<Pizza> arrayList;
    String[] ingredientsList;

    public PizzaAdapter(Context context, ArrayList<Pizza> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pizza_list_item, parent, false);
        }
        TextView name, ingredients, price;
        name = (TextView) convertView.findViewById(R.id.name);
        ingredients = (TextView) convertView.findViewById(R.id.ingredients);
        ingredientsList = arrayList.get(position).getPizzaIngredients();
        price = (TextView) convertView.findViewById(R.id.price);

        name.setText(arrayList.get(position).getPizzaName());
        ingredients.setText(String.join(", ", ingredientsList));
        price.setText("Цена: " + df.format(arrayList.get(position).getPizzaPrice()));

        return convertView;
    }
}
