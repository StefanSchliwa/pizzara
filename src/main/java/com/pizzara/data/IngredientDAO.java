package com.pizzara.data;

import com.pizzara.model.Ingredient;

import java.util.List;

public class IngredientDAO extends GenericDAO<Ingredient> {
    private static final String TABLE_NAME = "ingredient";

    public Ingredient getById(int id) {
        return super.getById(id, TABLE_NAME, Ingredient.class);
    }

    public void insert(Ingredient ingredient) {
        super.insertEntity(TABLE_NAME, ingredient);
    }

    public void update(Ingredient ingredient) {
        super.updateEntity(TABLE_NAME, ingredient);
    }

    public void delete(Ingredient ingredient) {
        super.deleteEntity(TABLE_NAME, ingredient);
    }

    public List<Ingredient> getAll() {
        return super.getAllEntities(TABLE_NAME, Ingredient.class);
    }
}