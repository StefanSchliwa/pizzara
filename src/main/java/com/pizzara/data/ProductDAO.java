package com.pizzara.data;

import com.pizzara.model.Ingredient;
import com.pizzara.model.Product;

import java.util.List;

public class ProductDAO extends GenericDAO<Product> {
    private static final String TABLE_NAME = "product";

    public Product getById(int id) {
        return super.getById(id, TABLE_NAME, Product.class);
    }

    public void insert(Product product) {
        super.insertEntity(TABLE_NAME, product);
    }

    public void update(Product product) {
        super.updateEntity(TABLE_NAME, product);
    }

    public void delete(Product product) {
        super.deleteEntity(TABLE_NAME, product);
    }

    public List<Product> getAll() {
        return super.getAllEntities(TABLE_NAME, Product.class);
    }
}