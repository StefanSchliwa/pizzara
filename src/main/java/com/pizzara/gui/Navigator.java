package com.pizzara.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    public static void switchStage(ActionEvent event, String name) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(Navigator.class.getResource(name + ".fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }
}
