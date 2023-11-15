package com.pizzara.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {
    @FXML
    private void switchToProductConfigurator(ActionEvent event) {
        Navigator.switchStage(event,"product");
    }

    @FXML
    private void switchToIngredientOverview(ActionEvent event) {
        Navigator.switchStage(event,"ingredient");
    }
}