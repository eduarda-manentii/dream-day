package com.br.dreamday.controller;

import com.br.dreamday.domain.Parcela;
import com.br.dreamday.domain.Parcelamento;
import com.br.dreamday.service.ParcelaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ParcelasControllerWindow {

    @FXML
    private TableColumn<Parcela, String> parteColumn;

    @FXML
    private Label qtdeParcelasLbl;

    @FXML
    private TableColumn<Parcela, String> statusColumn;

    @FXML
    private TableView<Parcela> tableParcelas;

    @FXML
    private TableColumn<Parcela, String> valorColumn;

    @FXML
    private Label valorParceladoLbl;

    @FXML
    private Button btnVoltar;

    private ParcelaService parcelaService;

    private Parcelamento parcelamento;

    private ObservableList<Parcela> parcelas;

    public ParcelasControllerWindow() {
        parcelaService = new ParcelaService();
    }

    public void definirAtributos(Parcelamento parcelamento) {
        qtdeParcelasLbl.setText(parcelamento.getQtdeParcelas().toString());
        valorParceladoLbl.setText(parcelamento.getValor().toString());

        parcelas = FXCollections.observableArrayList(
                parcelaService.listarPorParcelamento(parcelamento.getId()));

        parteColumn.setCellValueFactory(param -> {
            Parcela parcela = param.getValue();
            int index = parcelas.indexOf(parcela) + 1;
            String customParteValue = index + "/" + parcela.getParcelamento().getQtdeParcelas();
            return new SimpleStringProperty(customParteValue);
        });

        valorColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("parcelaStatus"));
        tableParcelas.setItems(parcelas);
    }

    @FXML
    void aoClicarVoltar() {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.close();
    }

}
