package com.br.dreamday.controller;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.service.ClienteService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class CasamentoCalendarioWindowController implements Initializable {

    @FXML
    private Text ano;

    @FXML
    private Text mes;

    @FXML
    private FlowPane calendario;

    private ZonedDateTime dataFoco;
    private ZonedDateTime hoje;
    private final ClienteService clienteService;

    public CasamentoCalendarioWindowController() {
        this.clienteService = new ClienteService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZonedDateTime agora = ZonedDateTime.now();
        dataFoco = agora;
        hoje = agora;
        desenhaCalendario();
    }

    @FXML
    void retornaUmMes() {
        dataFoco = dataFoco.minusMonths(1);
        calendario.getChildren().clear();
        desenhaCalendario();
    }

    @FXML
    void avancaUmMes() {
        dataFoco = dataFoco.plusMonths(1);
        calendario.getChildren().clear();
        desenhaCalendario();
    }

    private void desenhaCalendario() {
        ano.setText(String.valueOf(dataFoco.getYear()));
        mes.setText(dataFoco.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")));

        double calendarWidth = calendario.getPrefWidth();
        double calendarHeight = calendario.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendario.getHgap();
        double spacingV = calendario.getVgap();

        Map<Integer, List<Cliente>> calendarioCasamentoMap = getOrcamentosDoMes(dataFoco);

        int monthMaxDate = dataFoco.getMonth().maxLength();
        if(dataFoco.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        int dataOffset = ZonedDateTime.of(
                dataFoco.getYear(),
                dataFoco.getMonthValue(),
                1,
                0,
                0,
                0,
                0,
                dataFoco.getZone()
        ).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle retangulo = new Rectangle();
                retangulo.setFill(Color.TRANSPARENT);
                retangulo.setStroke(Color.BLACK);
                retangulo.setStrokeWidth(strokeWidth);
                double larguraRetangulo = (calendarWidth / 7) - strokeWidth - spacingH;
                retangulo.setWidth(larguraRetangulo);
                double alturaRetangulo = (calendarHeight / 6) - strokeWidth - spacingV;
                retangulo.setHeight(alturaRetangulo);
                stackPane.getChildren().add(retangulo);

                int dataCalculada = (j + 1) + (7 * i);
                if(dataCalculada > dataOffset){
                    int currentDate = dataCalculada - dataOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (alturaRetangulo / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<Cliente> casamentosCalendario = calendarioCasamentoMap.get(currentDate);
                        if(casamentosCalendario != null) {
                            criaCasamentosCalendario(casamentosCalendario, alturaRetangulo, larguraRetangulo, stackPane);
                        }
                    }
                    if(hoje.getYear() == dataFoco.getYear() && hoje.getMonth() == dataFoco.getMonth() && hoje.getDayOfMonth() == currentDate){
                        retangulo.setStroke(Color.BLUE);
                    }
                }
                calendario.getChildren().add(stackPane);
            }
        }
    }

    private void criaCasamentosCalendario(List<Cliente> casamentosCalendario, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox boxAtividadeCalendario = new VBox();

        for (int k = 0; k < casamentosCalendario.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                boxAtividadeCalendario.getChildren().add(moreActivities);
                break;
            }
            Text text = new Text(casamentosCalendario.get(k).getEmail());
            boxAtividadeCalendario.getChildren().add(text);
            boxAtividadeCalendario.setOnMouseClicked(mouseEvent -> abrirModalCasamento(casamentosCalendario));
        }

        boxAtividadeCalendario.setTranslateY((rectangleHeight / 2) * 0.20);
        boxAtividadeCalendario.setMaxWidth(rectangleWidth * 0.8);
        boxAtividadeCalendario.setMaxHeight(rectangleHeight * 0.65);
        boxAtividadeCalendario.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(boxAtividadeCalendario);
    }

    private Map<Integer, List<Cliente>> getOrcamentosDoMes(ZonedDateTime dataFoco) {
        return criaMapaDoCalendario(clienteService.listarPor(dataFoco.toLocalDate()));
    }

    private Map<Integer, List<Cliente>> criaMapaDoCalendario(List<Cliente> casamentosDoMes) {
        Map<Integer, List<Cliente>> calendarioCasamentoMap = new HashMap<>();

        for (Cliente casamentoAtual: casamentosDoMes) {
            int dataCasamento = casamentoAtual.getDataCasamento().getDayOfMonth();
            if(!calendarioCasamentoMap.containsKey(dataCasamento)){
                calendarioCasamentoMap.put(dataCasamento, List.of(casamentoAtual));
            } else {
                List<Cliente> novaLista = new ArrayList<>(calendarioCasamentoMap.get(dataCasamento));
                novaLista.add(casamentoAtual);
                calendarioCasamentoMap.put(dataCasamento, novaLista);
            }
        }
        return  calendarioCasamentoMap;
    }

    private void abrirModalCasamento(List<Cliente> casamentosDoDia) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Casamentos do Dia");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10;");

        for (Cliente casamento : casamentosDoDia) {
            Label label = new Label("ID:" + casamento.getId() +
                    "\nCliente: " + casamento.getNome() +
                    "\nData do Casamento: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(casamento.getDataCasamento()) +
                    "\nConjugue: " + casamento.getConjugue() +
                    "\nTelefone: " + casamento.getTelefone() +
                    "\nEmail: " + casamento.getEmail());
            label.setStyle("-fx-background-color: LIGHTGRAY; -fx-padding: 5;");
            vbox.getChildren().add(label);
        }

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(300);

        Scene scene = new Scene(scrollPane, 400, 300);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }
}
