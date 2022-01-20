package com.example.justpizza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    public final static String ORDERS_TABLE_NAME = "ordered_pizzas_table";
    public final static String ORDERS_COLUMN_ID = "ID";
    public final static String ORDERS_COLUMN_PIZZA_NAME = "NAME";
    public final static String ORDERS_COLUMN_ADDITIVES = "ADDITIVES";
    public final static String ORDERS_COLUMN_PRICE = "PRICE";
    public final static String ORDERS_COLUMN_QUANTITY = "QUANTITY";

    public final static String FAVOURITES_TABLE_NAME = "favourite_pizzas_table";
    public final static String FAVOURITES_COLUMN_ID = "ID";
    public final static String FAVOURITES_COLUMN_PIZZA_NAME = "NAME";
    public final static String FAVOURITES_COLUMN_INGREDIENTS = "INGREDIENTS";
    public final static String FAVOURITES_COLUMN_PRICE = "PRICE";


    //отговаря на номера на базата данни
    private static final int DATABASE_VERSION = 6;

    // конструктор на класа
    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ORDERS_TABLE_NAME
                + "("+ ORDERS_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ORDERS_COLUMN_PIZZA_NAME +" TEXT,"
                + ORDERS_COLUMN_ADDITIVES + " TEXT,"
                + ORDERS_COLUMN_PRICE +" REAL, "
                + ORDERS_COLUMN_QUANTITY + " INTEGER);");

        db.execSQL("CREATE TABLE " + FAVOURITES_TABLE_NAME
                + "("+ FAVOURITES_COLUMN_ID +" INTEGER PRIMARY KEY,"
                + FAVOURITES_COLUMN_PIZZA_NAME +" TEXT,"
                + FAVOURITES_COLUMN_INGREDIENTS + " TEXT,"
                + FAVOURITES_COLUMN_PRICE +" REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITES_TABLE_NAME);
        onCreate(db);
    }
}
