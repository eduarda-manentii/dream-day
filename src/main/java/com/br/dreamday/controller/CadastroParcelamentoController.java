package com.br.dreamday.controller;

import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.Parcela;
import com.br.dreamday.domain.Parcelamento;
import com.br.dreamday.domain.ParcelamentoStatus;
import com.br.dreamday.service.ParcelaService;
import com.br.dreamday.service.ParcelamentoService;
import com.br.dreamday.utils.MascarasFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

public class CadastroParcelamentoController {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Label lblNovoOrcamento;

    @FXML
    private Label lblValorParcelas;

    @FXML
    private Label lblValorTotalResultado;

    @FXML
    private TextField txtDataVencimento;

    @FXML
    private TextArea txtObservacao;

    @FXML
    private TextField txtQtdeParcelas;

    private Orcamento orcamento;

    private ParcelamentoService parcelamentoService;

    private ParcelaService parcelaService;

    @FXML
    public void initialize() {
        parcelamentoService = new ParcelamentoService();
        parcelaService = new ParcelaService();

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                if (newText.isEmpty() || (Integer.parseInt(newText) >= 0 && Integer.parseInt(newText) <= 24)) {
                    return change;
                }
            }
            return null;
        };
        MascarasFX.mascaraData(txtDataVencimento);

        txtQtdeParcelas.setTextFormatter(new TextFormatter<>(filter));
    }

    @FXML
    void onButtonCancelarClicked() {

    }

    @FXML
    void onButtonSalvarClicked() {
        Parcelamento parcelamento = new Parcelamento();
        parcelamento.setOrcamento(orcamento);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataVencimento = LocalDate.parse(txtDataVencimento.getText(), formatter);

        parcelamento.setDataVencimento(dataVencimento);
        parcelamento.setStatus(ParcelamentoStatus.AGUARDANDO_PAGAMENTO);
        parcelamento.setQtdeParcelas(Integer.parseInt(txtQtdeParcelas.getText()));
        parcelamento.setValor(orcamento.getCustoEstimado());

        BigDecimal valorParcela = getValorParcela();

        parcelamentoService.salvar(parcelamento);
// TODO: O erro ocorre por que ele não recupera o id do parcelamento, (retornar o id do parcelamento no service)
//        for (int i = 0; i < parcelamento.getQtdeParcelas(); i++) {
//            Parcela parcela = new Parcela();
//            parcela.setParcelamento(parcelamento);
//            parcela.setValor(valorParcela);
//            parcela.setObservacao(parcelamento.getObservacao());
//            parcelaService.salvar(parcela);
//        }



    }

    void setAttributes(Orcamento orcamento) {
        this.orcamento = orcamento;
        lblValorTotalResultado.setText("R$ " + orcamento.getCustoEstimado());
    }

    @FXML
    void onQtdeParcelasChanged() {
        try {
            lblValorParcelas.setText("R$ " + getValorParcela());
        } catch (NumberFormatException e) {
            lblValorParcelas.setText("Por favor, insira um número válido.");
        }
    }

    private BigDecimal getValorParcela() {
        int qtdeParcelas = Integer.parseInt(txtQtdeParcelas.getText());
        BigDecimal valorTotal = orcamento.getCustoEstimado();
        return valorTotal.divide(BigDecimal.valueOf(qtdeParcelas), 2, BigDecimal.ROUND_HALF_UP);
    }

}
