package com.example.justpizza;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FavouritePizzasActivity extends BaseActivity {

    ListView listView;
    ArrayList<Pizza> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_pizzas);

        listView = (ListView) findViewById(R.id.list_view_pizzas);

        arrayList = new ArrayList<>();

        arrayList = readFromDB();

        PizzaAdapter adapter = new PizzaAdapter(FavouritePizzasActivity.this, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pizza entry = (Pizza) parent.getItemAtPosition(position);

                Intent i = new Intent(FavouritePizzasActivity.this, OrderActivity.class);
                i.putExtra("id", entry.getPizzaId());
                i.putExtra("name", entry.getPizzaName());
                i.putExtra("price", entry.getPizzaPrice());
                i.putExtra("ingredients", String.join(", ", entry.getPizzaIngredients()));
                startActivity(i);
            }
        });
    }

    private ArrayList<Pizza> readFromDB() {
        ArrayList<Pizza> arrayList = new ArrayList<>();
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                MyDataBaseHelper.FAVOURITES_TABLE_NAME,
                new String[]{
                        MyDataBaseHelper.FAVOURITES_COLUMN_ID, MyDataBaseHelper.FAVOURITES_COLUMN_PIZZA_NAME,MyDataBaseHelper.FAVOURITES_COLUMN_INGREDIENTS, MyDataBaseHelper.FAVOURITES_COLUMN_PRICE},
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            // получаване на индексите и стойностите на колоните
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseHelper.FAVOURITES_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseHelper.FAVOURITES_COLUMN_PIZZA_NAME));
            String ingredientsStr = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseHelper.FAVOURITES_COLUMN_INGREDIENTS));
            String[] ingredients = ingredientsStr.split(",");
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_PRICE));

            arrayList.add(new Pizza(id, name, ingredients, price));
        }

        cursor.close();
        return arrayList;
    }
}