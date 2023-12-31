package com.pizzara.data;

import com.pizzara.model.Ingredient;
import com.pizzara.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO extends GenericDAO<Product> {
    private static final String TABLE_NAME = "product";

    public Product getById(int id) {
        return super.getById(id, TABLE_NAME, Product.class);
    }

    public List<Product> getAll() {
        return super.getAllEntities(TABLE_NAME, Product.class);
    }

    public List<Product> getAllProductsWithIngredientsAndQuantitiesLoaded() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.price AS product_price, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.type AS ingredient_type, " +
                "pi.quantity AS ingredient_quantity " +
                "FROM product p " +
                "JOIN product_ingredient pi ON p.id = pi.product_id " +
                "JOIN ingredient i ON pi.ingredient_id = i.id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            Map<Integer, Product> productMap = new HashMap<>();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product product = productMap.computeIfAbsent(productId, id -> {
                    Product newProduct = new Product();
                    newProduct.setId(productId);
                    try {
                        newProduct.setName(resultSet.getString("product_name"));
                        newProduct.setPrice(resultSet.getDouble("product_price"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    newProduct.setIngredientWithQuantities(new HashMap<>());

                    return newProduct;
                });

                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setType(resultSet.getString("ingredient_type"));

                Integer quantity = resultSet.getInt("ingredient_quantity");

                product.getIngredientWithQuantities().put(ingredient, quantity);
            }

            products.addAll(productMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void updateProductIngredients(Product product) {
        String updateSql = "UPDATE product_ingredient SET quantity = ? WHERE product_id = ? AND ingredient_id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            for (Map.Entry<Ingredient, Integer> entry : product.getIngredientWithQuantities().entrySet()) {
                Ingredient ingredient = entry.getKey();
                Integer quantity = entry.getValue();

                updateStatement.setInt(1, quantity);
                updateStatement.setInt(2, product.getId());
                updateStatement.setInt(3, ingredient.getId());

                updateStatement.addBatch();
            }

            updateStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}