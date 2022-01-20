package com.example.justpizza;

public class OrderEntry {

    String mPizzaName;
    String mAdditives;
    int mQuantity;
    double mPrice;

    public OrderEntry(String pizzaName, String additives, int quantity, double price){
        mPizzaName = pizzaName;
        mAdditives = additives;
        mQuantity = quantity;
        mPrice = price;
    }

    public double getPrice() {
        return mPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getPizzaName() {
        return mPizzaName;
    }

    public String getAdditives() {
        return mAdditives;
    }
}
