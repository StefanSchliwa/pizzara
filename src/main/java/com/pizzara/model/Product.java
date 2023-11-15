package com.pizzara.model;

import java.util.Map;

public class Product {
    private int id;
    private String name;
    private double price;
    private Map<Ingredient, Double> ingredientWithQuantities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<Ingredient, Double> getIngredientWithQuantities() {
        return ingredientWithQuantities;
    }

    public void setIngredientWithQuantities(Map<Ingredient, Double> ingredientWithQuantities) {
        this.ingredientWithQuantities = ingredientWithQuantities;
    }
}