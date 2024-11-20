package com.br.dreamday.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {

    public static String converteParaPtBr(Double valor) {
        Locale localeBR = new Locale("pt","BR");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);
        return numberFormat.format(valor);
    }

    public static String converteParaPtBr(BigDecimal valor) {
        return converteParaPtBr(valor.doubleValue());
    }
}
