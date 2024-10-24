module com.example.dreamday {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.dreamday to javafx.fxml;
    exports com.example.dreamday;
    exports com.example.dreamday.controller;
    opens com.example.dreamday.controller to javafx.fxml;
    opens com.example.dreamday.domain to javafx.base;
}