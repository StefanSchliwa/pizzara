package com.pizzara.gui;

import java.util.List;
import java.util.Map;

import com.pizzara.logic.ProductService;
import com.pizzara.model.Ingredient;
import com.pizzara.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ProductController {
    private final ProductService productService = new ProductService();
    @FXML
    private ListView<Product> productListView;

    @FXML
    private Label productNameLabel;

    @FXML
    private VBox ingredientDetailsVBox;

    @FXML
    private void initialize() {
        readProductList();
    }

    @FXML
    private void switchToDashboard(ActionEvent event) {
        Navigator.switchStage(event, "dashboard");
    }

    @FXML
    private void readProductList() {
        List<Product> products = productService.getAllProductsWithIngredientsAndQuantities();
        ObservableList<Product> observableList = FXCollections.observableArrayList(products);
        productListView.setItems(observableList);

        productListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setText(null);
                } else {
                    // Hier können auch andere Steuerelemente wie Labels, Buttons usw. verwendet werden
                    setText(product.getName() + " - $" + product.getPrice());
                }
            }
        });
    }

    @FXML
    private void handleProductSelection() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            productNameLabel.setText("Rezept: " + selectedProduct.getName());
            ingredientDetailsVBox.getChildren().clear();
            for (Map.Entry<Ingredient, Integer> entry : selectedProduct.getIngredientWithQuantities().entrySet()) {
                Ingredient ingredient = entry.getKey();
                int quantity = entry.getValue();
                Label ingredientLabel = new Label(ingredient.getName() + " - " + quantity + "g");
                ingredientDetailsVBox.getChildren().add(ingredientLabel);
            }
        }
    }
}