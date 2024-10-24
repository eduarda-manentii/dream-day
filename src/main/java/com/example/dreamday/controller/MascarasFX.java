package com.example.dreamday.controller;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

import java.util.function.UnaryOperator;

public class MascarasFX {

    public static void mascaraNumeroInteiro(TextField textField){
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static void mascaraNumero(TextField textField){
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            newValue = newValue.replaceAll(",",".");
            if(!newValue.isEmpty()){
                try{
                    Double.parseDouble(newValue);
                    textField.setText(newValue.replaceAll(",","."));
                }catch(Exception e){
                    textField.setText(oldValue);
                }
            }
        });
    }

    public static void mascaraData(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.length() > 10) {
                return null;
            }
            if (!novoTexto.matches("\\d{0,2}/?\\d{0,2}/?\\d{0,4}")) {
                return null;
            }

            return change;
        };
        textField.setTextFormatter(new TextFormatter<>(filtro));
        textField.setOnKeyTyped((KeyEvent event) -> {
            String currentText = textField.getText();
            if(!"0123456789".contains(event.getCharacter())) {
                event.consume();
            }

            if (!event.getCharacter().trim().isEmpty()) {
                if (currentText.length() == 2 || currentText.length() == 5) {
                    textField.setText(currentText + "/");
                    textField.positionCaret(textField.getText().length());
                }
            }

            if (currentText.length() >= 10) {
                event.consume();
            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {
            if (!textField.getText().matches("\\d{0,2}/?\\d{0,2}/?\\d{0,4}")) {
                textField.setText(textField.getText().replaceAll("[^\\d/]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });
    }

    public static void mascaraData(DatePicker datePicker){
        datePicker.getEditor().setOnKeyTyped((KeyEvent event) -> {
            if(!"0123456789".contains(event.getCharacter())){
                event.consume();
            }
            if(event.getCharacter().trim().isEmpty()){
                if(datePicker.getEditor().getText().length()==3){
                    datePicker.getEditor().setText(datePicker.getEditor().getText().substring(0,2));
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }
                if(datePicker.getEditor().getText().length()==6){
                    datePicker.getEditor().setText(datePicker.getEditor().getText().substring(0,5));
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }

            } else{
                if(datePicker.getEditor().getText().length()==10) event.consume();
                if(datePicker.getEditor().getText().length()==2){
                    datePicker.getEditor().setText(datePicker.getEditor().getText()+"/");
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }
                if(datePicker.getEditor().getText().length()==5){
                    datePicker.getEditor().setText(datePicker.getEditor().getText()+"/");
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }
            }
        });

        datePicker.getEditor().setOnKeyReleased((KeyEvent evt) -> {
            if(!datePicker.getEditor().getText().matches("\\d/*")){
                datePicker.getEditor().setText(datePicker.getEditor().getText().replaceAll("[^\\d/]", ""));
                datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
            }
        });
    }

    public static void mascaraCPF(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.length() > 14) {
                return null;
            }
            if (!novoTexto.matches("\\d{0,3}\\.?\\d{0,3}\\.?\\d{0,3}\\-?\\d{0,2}")) {
                return null;
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter<>(filtro));
        textField.setOnKeyTyped((KeyEvent event) -> {
            String currentText = textField.getText();
            if (!"0123456789".contains(event.getCharacter())) {
                event.consume();
            }

            if (!event.getCharacter().trim().isEmpty()) {
                if (currentText.length() == 3 || currentText.length() == 7) {
                    textField.setText(currentText + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (currentText.length() == 11) {
                    textField.setText(currentText + "-");
                    textField.positionCaret(textField.getText().length());
                }
            }
            if (currentText.length() >= 14) {
                event.consume();
            }
        });
        textField.setOnKeyReleased((KeyEvent evt) -> {
            if (!textField.getText().matches("\\d{0,3}\\.?\\d{0,3}\\.?\\d{0,3}\\-?\\d{0,2}")) {
                textField.setText(textField.getText().replaceAll("[^\\d.-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });
    }

    public static void mascaraEmail(TextField textField){
        textField.setOnKeyTyped((KeyEvent event) -> {
            if(!"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz._-@".contains(event.getCharacter())){
                event.consume();
            }
            if("@".equals(event.getCharacter())&&textField.getText().contains("@")){
                event.consume();
            }
            if("@".equals(event.getCharacter())&& textField.getText().isEmpty()){
                event.consume();
            }
        });

    }

    public static void mascaraTelefone(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.length() > 15) {
                return null;
            }
            if (!novoTexto.matches("\\(\\d{0,2}\\)?\\s?\\d{0,5}-?\\d{0,5}")) {
                return null;
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter<>(filtro));
        textField.setOnKeyTyped((KeyEvent event) -> {
            String currentText = textField.getText();
            if (!"0123456789".contains(event.getCharacter())) {
                event.consume();
            }
            if (!event.getCharacter().trim().isEmpty()) {
                if (currentText.isEmpty()) {
                    textField.setText("(" + event.getCharacter());
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                } else if (currentText.length() == 3) {
                    textField.setText(currentText + ") ");
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                } else if (currentText.length() == 9) {
                    textField.setText(currentText + "-");
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                }
            }
        });
        textField.setOnKeyReleased((KeyEvent evt) -> {
            if (!textField.getText().matches("\\(\\d{0,2}\\)?\\s?\\d{0,5}-?\\d{0,5}")) {
                textField.setText(textField.getText().replaceAll("[^\\d()\\s-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });
    }

}