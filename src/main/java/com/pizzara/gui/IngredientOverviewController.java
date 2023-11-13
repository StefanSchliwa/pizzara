package com.pizzara.gui;

import com.pizzara.logic.IngredientService;
import com.pizzara.model.Ingredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class IngredientOverviewController {
    private final IngredientService ingredientService = new IngredientService();

    @FXML
    private ListView<Ingredient> ingredientList;

    @FXML
    private void initialize() {
        // Initialize the UI components or any other setup
        readIngredientList();
    }

    @FXML
    private void readIngredientList() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        ObservableList<Ingredient> observableList = FXCollections.observableArrayList(ingredients);
        ingredientList.setItems(observableList);
    }

    @FXML
    private void createIngredient() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setHeaderText(null);
        nameDialog.setTitle("Create Ingredient");
        nameDialog.setContentText("Enter ingredient name:");
        Optional<String> nameResult = nameDialog.showAndWait();

        if (nameResult.isPresent()) {
            TextInputDialog typeDialog = new TextInputDialog();
            typeDialog.setHeaderText(null);
            typeDialog.setTitle("Create Ingredient");
            typeDialog.setContentText("Enter ingredient type:");
            Optional<String> typeResult = typeDialog.showAndWait();

            typeResult.ifPresent(type -> {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(nameResult.get());
                newIngredient.setTyp(type);
                ingredientService.createIngredient(newIngredient);
                readIngredientList();
            });
        }
    }

    @FXML
    private void updateIngredient() {
        Ingredient selectedIngredient = ingredientList.getSelectionModel().getSelectedItem();

        if (selectedIngredient != null) {
            TextInputDialog nameDialog = new TextInputDialog(selectedIngredient.getName());
            nameDialog.setHeaderText(null);
            nameDialog.setTitle("Update Ingredient");
            nameDialog.setContentText("Enter new ingredient name:");
            Optional<String> newNameResult = nameDialog.showAndWait();

            newNameResult.ifPresent(newName -> {
                TextInputDialog typeDialog = new TextInputDialog(selectedIngredient.getTyp());
                typeDialog.setHeaderText(null);
                typeDialog.setTitle("Update Ingredient");
                typeDialog.setContentText("Enter new ingredient type:");
                Optional<String> newTypeResult = typeDialog.showAndWait();

                newTypeResult.ifPresent(newType -> {
                    selectedIngredient.setName(newName);
                    selectedIngredient.setTyp(newType);
                    ingredientService.updateIngredient(selectedIngredient);
                    readIngredientList();
                });
            });
        } else {
            showAlert("Update Ingredient", "Please select an ingredient to update.");
        }
    }

    @FXML
    private void deleteIngredient() {
        Ingredient selectedIngredient = ingredientList.getSelectionModel().getSelectedItem();

        if (selectedIngredient != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Ingredient");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to delete this ingredient?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ingredientService.deleteIngredient(selectedIngredient);
                readIngredientList();
            }
        } else {
            showAlert("Delete Ingredient", "Please select an ingredient to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}