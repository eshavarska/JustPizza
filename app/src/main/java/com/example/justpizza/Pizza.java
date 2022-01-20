package com.example.justpizza;

public class Pizza {
    
    int pizzaId;
    String pizzaName;
    String[] pizzaIngredients;
    double pizzaPrice;

    public Pizza(int id, String name, String[] ingredients, double price){
        pizzaId = id;
        pizzaName = name;
        pizzaIngredients = ingredients;
        pizzaPrice = price;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public String[] getPizzaIngredients() {
        return pizzaIngredients;
    }

    public double getPizzaPrice() {
        return pizzaPrice;
    }
}
