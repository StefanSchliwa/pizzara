package com.pizzara.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {
    private Stage stage;
    private Scene scene;

    public void switchToProductConfigurator(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("product-configurator.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToIngredientOverview(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ingredient-overview.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}