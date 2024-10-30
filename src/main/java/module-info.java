module com.br.dreamday {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires org.postgresql.jdbc;
    requires jdk.jdi;

    opens com.br.dreamday.component to javafx.fxml;
    opens com.br.dreamday.controller to javafx.fxml;
    opens com.br.dreamday to javafx.fxml;
    exports com.br.dreamday;
    exports com.br.dreamday.controller;
    opens com.br.dreamday.domain to javafx.base;
    exports com.br.dreamday.utils;
    opens com.br.dreamday.utils to javafx.fxml;
    exports com.br.dreamday.domain;
}
