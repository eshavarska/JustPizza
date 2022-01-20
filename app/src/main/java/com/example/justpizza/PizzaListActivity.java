package com.example.justpizza;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PizzaListActivity extends BaseActivity {

    ListView listView;
    ArrayList<Pizza> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_list);

        listView = (ListView) findViewById(R.id.list_view_pizzas);

        arrayList = new ArrayList<>();
        new fetchData().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pizza entry = (Pizza) parent.getItemAtPosition(position);

                Intent i = new Intent(PizzaListActivity.this, OrderActivity.class);
                i.putExtra("id", entry.getPizzaId());
                i.putExtra("name", entry.getPizzaName());
                i.putExtra("price", entry.getPizzaPrice());
                i.putExtra("ingredients", String.join(", ", entry.getPizzaIngredients()));
                startActivity(i);
            }
        });
    }

    public class fetchData extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            arrayList.clear();
            String result = null;
            try {
                URL url = new URL("https://android-app-pizza-api.herokuapp.com/pizza-api");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("pizzas");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    JSONArray ingredientsJSON = jsonObject.getJSONArray("ingredients");
                    Double price = jsonObject.getDouble("price");

                    String[] ingredients = toStringArray(ingredientsJSON);

                    Pizza model = new Pizza(id, name, ingredients, price);
                    arrayList.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PizzaAdapter adapter = new PizzaAdapter(PizzaListActivity.this, arrayList);
            listView.setAdapter(adapter);

        }

        public String[] toStringArray(JSONArray array) {
            if(array==null)
                return null;

            String[] arr=new String[array.length()];
            for(int i=0; i<arr.length; i++) {
                arr[i]=array.optString(i);
            }
            return arr;
        }
    }
}