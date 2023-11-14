package com.pizzara.data;

import com.pizzara.model.Product;
import com.pizzara.model.ProductIngredient;

import java.util.List;

public class ProductIngredientDAO extends GenericDAO<ProductIngredient> {
    private static final String TABLE_NAME = "product_ingredient";

    public ProductIngredient getById(int id) {
        return super.getById(id, TABLE_NAME, ProductIngredient.class);
    }

    public void insert(ProductIngredient productIngredient) {
        super.insertEntity(TABLE_NAME, productIngredient);
    }

    public void update(ProductIngredient productIngredient) {
        super.updateEntity(TABLE_NAME, productIngredient);
    }

    public void delete(ProductIngredient productIngredient) {
        super.deleteEntity(TABLE_NAME, productIngredient);
    }

    public List<ProductIngredient> getAll() {
        return super.getAllEntities(TABLE_NAME, ProductIngredient.class);
    }
}