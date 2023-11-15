package com.pizzara.logic;

import com.pizzara.data.ProductDAO;
import com.pizzara.model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public Product getProductById(int id) {
        return productDAO.getById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    public List<Product> getAllProductsWithIngredientsAndQuantities() {
        return productDAO.getAllProductsWithIngredientsAndQuantities();
    }

    public void createProduct(Product product) {
        productDAO.insert(product);
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    public void deleteProduct(Product product) {
        productDAO.delete(product);
    }
}