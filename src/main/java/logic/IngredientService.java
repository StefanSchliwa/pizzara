package logic;

import data.IngredientDAO;
import model.Ingredient;

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
        return ingredientDAO.getAllIngredients();
    }

    public void createIngredient(Ingredient ingredient) {
        ingredientDAO.insert(ingredient);
    }

    public void updateIngredient(Ingredient ingredient) {
        ingredientDAO.update(ingredient);
    }

    public void deleteIngredient(int id) {
        ingredientDAO.delete(id);
    }
}