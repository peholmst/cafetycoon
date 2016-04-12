package org.vaadin.samples.cafetycoon.ui.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static String formatMoney(BigDecimal amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(amount.doubleValue());
    }    
}
