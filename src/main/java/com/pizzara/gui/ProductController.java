package com.pizzara.gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pizzara.logic.ProductService;
import com.pizzara.model.Ingredient;
import com.pizzara.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductController {
    private final ProductService productService = new ProductService();
    @FXML
    private ListView<Product> productListView;

    @FXML
    private Label productNameLabel;

    @FXML
    private VBox ingredientDetailsVBox;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() {
        readProductList();
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
            for (Map.Entry<Ingredient, Double> entry : selectedProduct.getIngredientWithQuantities().entrySet()) {
                Ingredient ingredient = entry.getKey();
                double quantity = entry.getValue();
                Label ingredientLabel = new Label(ingredient.getName() + " - " + quantity + "g");
                ingredientDetailsVBox.getChildren().add(ingredientLabel);
            }
        }
    }
}