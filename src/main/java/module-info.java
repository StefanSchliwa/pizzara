module com.pizzara.pizzara {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.persistence;
    requires java.sql;
    requires google.maps.services;
    requires javafaker;

    opens com.pizzara.gui to javafx.fxml;
    exports com.pizzara.gui;
    exports com.pizzara.app;
    opens com.pizzara.app to javafx.fxml;
}