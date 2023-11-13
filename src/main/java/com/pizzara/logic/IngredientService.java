package com.pizzara.logic;

import com.pizzara.data.IngredientDAO;
import com.pizzara.model.Ingredient;

import java.util.List;

public class IngredientService {
    private IngredientDAO ingredientDAO;

    public IngredientService() {
        this.ingredientDAO = new IngredientDAO();
    }

    public Ingredient getIngredientById(int id) {
        return ingredientDAO.getById(id);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientDAO.getAll();
    }

    public void createIngredient(Ingredient ingredient) {
        ingredientDAO.insert(ingredient);
    }

    public void updateIngredient(Ingredient ingredient) {
        ingredientDAO.update(ingredient);
    }

    public void deleteIngredient(Ingredient ingredient) {
        ingredientDAO.delete(ingredient);
    }
}