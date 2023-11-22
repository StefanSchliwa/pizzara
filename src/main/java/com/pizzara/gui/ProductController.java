package com.pizzara.gui;

import java.util.List;
import java.util.Map;

import com.pizzara.data.IngredientDAO;
import com.pizzara.data.ProductDAO;
import com.pizzara.model.Ingredient;
import com.pizzara.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ProductController {
    private final ProductDAO productDAO = new ProductDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();
    private boolean editMode = false;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private Label productNameLabel;

    @FXML
    private VBox ingredientDetailsVBox;

    @FXML
    private VBox ingredientChoiceBoxesVBox;

    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        readProductList();
        refreshButtonState();
    }

    @FXML
    private void switchToDashboard(ActionEvent event) {
        Navigator.switchStage(event, "dashboard");
    }

    @FXML
    private void readProductList() {
        List<Product> products = productDAO.getAllProductsWithIngredientsAndQuantitiesLoaded();
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
        ingredientChoiceBoxesVBox.getChildren().clear();

        if (selectedProduct != null) {
            productNameLabel.setText("Rezept: " + selectedProduct.getName());
            ingredientDetailsVBox.getChildren().clear();

            if (editMode) {
                for (Map.Entry<Ingredient, Integer> entry : selectedProduct.getIngredientWithQuantities().entrySet()) {
                    Ingredient ingredient = entry.getKey();
                    int quantity = entry.getValue();

                    ChoiceBox<Ingredient> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(ingredientDAO.getAll()));
                    choiceBox.setConverter(new StringConverter<>() {
                        @Override
                        public String toString(Ingredient object) {
                            return object.getName();
                        }

                        @Override
                        public Ingredient fromString(String string) {
                            return null;
                        }
                    });
                    choiceBox.setValue(ingredient);

                    TextField quantityInput = new TextField(Integer.toString(quantity));
                    quantityInput.setPromptText("Quantity");
                    quantityInput.setMaxWidth(50);
                    quantityInput.setVisible(true);

                    HBox ingredientBox = new HBox(choiceBox, quantityInput);
                    ingredientChoiceBoxesVBox.getChildren().add(ingredientBox);
                }
            } else {
                for (Map.Entry<Ingredient, Integer> entry : selectedProduct.getIngredientWithQuantities().entrySet()) {
                    Ingredient ingredient = entry.getKey();
                    int quantity = entry.getValue();
                    Label ingredientLabel = new Label(ingredient.getName() + " - " + quantity + "g");
                    ingredientDetailsVBox.getChildren().add(ingredientLabel);
                }
            }
        }

        refreshButtonState();
    }

    @FXML
    private void saveChanges() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null && editMode) {
            selectedProduct.getIngredientWithQuantities().clear();

            for (Node node : ingredientChoiceBoxesVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox ingredientBox = (HBox) node;

                    // Find the ChoiceBox and TextField within the HBox
                    ChoiceBox<Ingredient> choiceBox = findChoiceBox(ingredientBox);
                    TextField quantityInput = findQuantityTextField(ingredientBox);

                    if (choiceBox != null && quantityInput != null) {
                        Ingredient selectedIngredient = choiceBox.getValue();
                        int quantity = Integer.parseInt(quantityInput.getText());

                        selectedProduct.getIngredientWithQuantities().put(selectedIngredient, quantity);
                    }
                }
            }

            handleProductSelection();

            productDAO.updateProductIngredients(selectedProduct);
        }
    }

    private ChoiceBox<Ingredient> findChoiceBox(HBox ingredientBox) {
        return ingredientBox.getChildren().stream()
                .filter(child -> child instanceof ChoiceBox)
                .map(child -> (ChoiceBox<Ingredient>) child)
                .findFirst()
                .orElse(null);
    }

    private TextField findQuantityTextField(HBox ingredientBox) {
        return ingredientBox.getChildren().stream()
                .filter(child -> child instanceof TextField)
                .map(child -> (TextField) child)
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void toggleEditMode() {
        editMode = !editMode;
        handleProductSelection();
    }

    private void refreshButtonState() {
        editButton.setDisable(productListView.getSelectionModel().getSelectedItem() == null);
        editButton.setText(editMode ? "Abbrechen" : "Bearbeiten");

        saveButton.setDisable(!editMode);
    }
}