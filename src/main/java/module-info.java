module com.example.dreamday {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.dreamday to javafx.fxml;
    exports com.example.dreamday;
}