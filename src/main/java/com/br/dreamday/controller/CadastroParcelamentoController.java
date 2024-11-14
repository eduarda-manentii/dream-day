package com.br.dreamday.controller;

import com.br.dreamday.domain.*;
import com.br.dreamday.service.ParcelaService;
import com.br.dreamday.service.ParcelamentoService;
import com.br.dreamday.utils.MascarasFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

public class CadastroParcelamentoController {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Label lblValorParcelas;

    @FXML
    private Label lblValorTotalResultado;

    @FXML
    private TextField txtDataVencimento;

    @FXML
    private TextField txtQtdeParcelas;

    private Orcamento orcamento;

    private ParcelamentoService parcelamentoService;

    private ParcelaService parcelaService;

    private static final DateTimeFormatter FORMATADO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final int MAX_PARCELAS = 24;
    private static final int MIN_PARCELAS = 1;

    @FXML
    public void initialize() {
        parcelamentoService = new ParcelamentoService();
        parcelaService = new ParcelaService();
        MascarasFX.mascaraData(txtDataVencimento);
        formataQtdeParcelas();
    }

    @FXML
    void aoClicarCancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void aoClicarSalvar() {
        if (entradaValida()) {
            Parcelamento parcelamento = criarParcelamento();
            Long idParcelamento = parcelamentoService.salvar(parcelamento);
            parcelamento.setId(idParcelamento);

            criarParcelas(parcelamento);

            exibirMensagem("Parcelamento salvo com sucesso!");

            Stage stage = (Stage) btnSalvar.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void aoAlterarQtdeParcelas() {
        try {
            lblValorParcelas.setText(formatarMoeda(calcularValorParcela()));
        } catch (NumberFormatException e) {
            lblValorParcelas.setText("Por favor, insira um número válido.");
        }
    }

    void definirAtributos(Orcamento orcamento) {
        this.orcamento = orcamento;
        lblValorTotalResultado.setText(formatarMoeda(orcamento.getCustoEstimado()));
    }

    private boolean entradaValida() {
        if (!isDataVencimentoValida()) {
            exibirMensagem("Por favor, insira uma data válida para o vencimento.");
            return false;
        }
        if (!isQtdeParcelasValida()) {
            exibirMensagem("A quantidade de parcelas deve estar entre 1 e 24.");
            return false;
        }
        return true;
    }

    private boolean isDataVencimentoValida() {
        try {
            LocalDate.parse(txtDataVencimento.getText(), FORMATADO_DATA);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isQtdeParcelasValida() {
        try {
            int qtdeParcelas = Integer.parseInt(txtQtdeParcelas.getText());
            return qtdeParcelas >= MIN_PARCELAS && qtdeParcelas <= MAX_PARCELAS;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Parcelamento criarParcelamento() {
        Parcelamento parcelamento = new Parcelamento();
        parcelamento.setOrcamento(orcamento);
        parcelamento.setDataVencimento(parseDataVencimento());
        parcelamento.setStatus(ParcelamentoStatus.AGUARDANDO_PAGAMENTO);
        parcelamento.setQtdeParcelas(Integer.parseInt(txtQtdeParcelas.getText()));
        parcelamento.setValor(orcamento.getCustoEstimado());
        return parcelamento;
    }

    private LocalDate parseDataVencimento() {
        return LocalDate.parse(txtDataVencimento.getText(), FORMATADO_DATA);
    }

    private void criarParcelas(Parcelamento parcelamento) {
        BigDecimal valorParcela = calcularValorParcela();

        for (int i = 0; i < parcelamento.getQtdeParcelas(); i++) {
            Parcela parcela = new Parcela();
            parcela.setParcelamento(parcelamento);
            parcela.setValor(valorParcela);
            parcela.setParcelaStatus(ParcelaStatus.AGUARDANDO_PAGAMENTO);
            parcelaService.salvar(parcela);
        }
    }

    private BigDecimal calcularValorParcela() {
        int qtdeParcelas = Integer.parseInt(txtQtdeParcelas.getText());
        BigDecimal valorTotal = orcamento.getCustoEstimado();
        return valorTotal.divide(BigDecimal.valueOf(qtdeParcelas), 2, RoundingMode.HALF_UP);
    }

    private void formataQtdeParcelas() {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.matches("\\d*") && !novoTexto.isEmpty()) {
                int valor = Integer.parseInt(novoTexto);
                if (valor >= MIN_PARCELAS && valor <= MAX_PARCELAS) {
                    return change;
                }
            }
            return null;
        };

        txtQtdeParcelas.setTextFormatter(new TextFormatter<>(filtro));
    }

    private void exibirMensagem(String mensagem) {
        ButtonType tipoBotao = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Aviso");
        dialog.setContentText(mensagem);
        dialog.getDialogPane().getButtonTypes().add(tipoBotao);
        dialog.showAndWait();
    }

    private String formatarMoeda(BigDecimal valor) {
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP);
    }

}
