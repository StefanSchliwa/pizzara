package data;

import model.Ingredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO extends GenericDAO<Ingredient> {
    @Override
    public Ingredient getById(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_INGREDIENT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setTyp(resultSet.getString("typ"));
                return ingredient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(Ingredient ingredient) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.INSERT_INGREDIENT)) {
            preparedStatement.setString(1, ingredient.getName());
            preparedStatement.setString(2, ingredient.getTyp());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ingredient ingredient) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.UPDATE_INGREDIENT)) {
            preparedStatement.setString(1, ingredient.getName());
            preparedStatement.setString(2, ingredient.getTyp());
            preparedStatement.setInt(3, ingredient.getIngredientId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_INGREDIENT)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_ALL_INGREDIENTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("name"));
                ingredient.setTyp(resultSet.getString("typ"));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}