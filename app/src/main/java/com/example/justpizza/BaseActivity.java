package com.example.justpizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Идентификация на избора
        int itemId = item.getItemId();
        if (itemId == R.id.action_info) {
            // изпълнение на действие при избор на бутона За Нас;
            startActivity(new Intent(BaseActivity.this, AboutUsActivity.class));
            return true;
        }else if(itemId == R.id.action_pizzas_list){
            // изпълнение на действие при избор на бутона Пици;
            startActivity(new Intent(BaseActivity.this, PizzaListActivity.class));
            return true;
//        }else if(itemId == R.id.action_settings){
//            return true;
        }else if(itemId == R.id.action_cart){
            startActivity(new Intent(BaseActivity.this, FinishOrderActivity.class));
            return true;
        }else if(itemId == R.id.action_favourites){
            startActivity(new Intent(BaseActivity.this, FavouritePizzasActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//
//    public byte[] makebyte(Order modeldata) {
//        try {
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(modeldata);
//            byte[] employeeAsBytes = baos.toByteArray();
//            ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
//            return employeeAsBytes;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public Order read(byte[] data) {
//        try {
//            ByteArrayInputStream baip = new ByteArrayInputStream(data);
//            ObjectInputStream ois = new ObjectInputStream(baip);
//            Order dataobj = (Order) ois.readObject();
//            return dataobj ;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}