package com.br.dreamday;

import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.OrcamentoStatus;
import com.br.dreamday.domain.Parcelamento;
import com.br.dreamday.domain.ParcelamentoStatus;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.service.ParcelamentoService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MainViewApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainViewApplication.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("DreamDay <3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
