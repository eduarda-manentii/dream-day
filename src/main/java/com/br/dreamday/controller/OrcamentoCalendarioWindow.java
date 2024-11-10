package com.br.dreamday.controller;

import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.service.OrcamentoService;
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
import java.util.*;

public class OrcamentoCalendarioWindow implements Initializable {

    @FXML
    private Text ano;

    @FXML
    private Text mes;

    @FXML
    private FlowPane calendario;

    private ZonedDateTime dataFoco;
    private ZonedDateTime hoje;
    private final OrcamentoService orcamentoService;

    public OrcamentoCalendarioWindow() {
        this.orcamentoService = new OrcamentoService();
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
        mes.setText(String.valueOf(dataFoco.getMonth()));

        double calendarWidth = calendario.getPrefWidth();
        double calendarHeight = calendario.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendario.getHgap();
        double spacingV = calendario.getVgap();

        Map<Integer, List<Orcamento>> calendarioOrcamentoMap = getOrcamentosDoMes(dataFoco);

        int monthMaxDate = dataFoco.getMonth().maxLength();
        if(dataFoco.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        int dateOffset = ZonedDateTime.of(
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

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<Orcamento> orcamentosCalendario = calendarioOrcamentoMap.get(currentDate);
                        if(orcamentosCalendario != null) {
                            criaOrcamentoCalendario(orcamentosCalendario, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if(hoje.getYear() == dataFoco.getYear() && hoje.getMonth() == dataFoco.getMonth() && hoje.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendario.getChildren().add(stackPane);
            }
        }
    }

    private void criaOrcamentoCalendario(List<Orcamento> orcamentosCalendario, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();

        for (int k = 0; k < orcamentosCalendario.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                break;
            }
            Text text = new Text(orcamentosCalendario.get(k).getCliente().getNome() + ", " + orcamentosCalendario.get(k).getDataCriacao().getDayOfMonth());
            calendarActivityBox.getChildren().add(text);
            calendarActivityBox.setOnMouseClicked(mouseEvent -> {
                abrirModalOrcamentos(orcamentosCalendario);
            });
        }

        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<Orcamento>> getOrcamentosDoMes(ZonedDateTime dataFoco) {
        List<Orcamento> orcamentosDoMes = orcamentoService.listarPor(dataFoco.toLocalDate());
        return criaMapaDoCalendario(orcamentosDoMes);
    }

    private Map<Integer, List<Orcamento>> criaMapaDoCalendario(List<Orcamento> orcamentos) {
        Map<Integer, List<Orcamento>> calendarioOrcamentoMap = new HashMap<>();

        for (Orcamento orcamento: orcamentos) {
            int dataOrcamento = orcamento.getDataCriacao().getDayOfMonth();
            if(!calendarioOrcamentoMap.containsKey(dataOrcamento)){
                calendarioOrcamentoMap.put(dataOrcamento, List.of(orcamento));
            } else {
                List<Orcamento> novaLista = new ArrayList<>(calendarioOrcamentoMap.get(dataOrcamento));
                novaLista.add(orcamento);
                calendarioOrcamentoMap.put(dataOrcamento, novaLista);
            }
        }
        return  calendarioOrcamentoMap;
    }

    private void abrirModalOrcamentos(List<Orcamento> orcamentosDoDia) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Orçamentos do Dia");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10;");

        for (Orcamento orcamento : orcamentosDoDia) {
            Label label = new Label("Cliente: " + orcamento.getCliente().getNome() +
                    "\nData de Criação: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(orcamento.getDataCriacao()) +
                    "\nCusto Estimado: " + orcamento.getCustoEstimado() +
                    "\nValor Total: " + orcamento.getValorTotal() +
                    "\nObservações: " + orcamento.getObservaces());
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
