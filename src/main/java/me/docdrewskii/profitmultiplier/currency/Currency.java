package me.docdrewskii.profitmultiplier.currency;

import java.text.DecimalFormat;

public class Currency {

    private final String name;
    private final String symbol;
    private final boolean suffix;
    private final DecimalFormat format;

    public Currency(String name, String symbol, boolean suffix, String pattern) {
        this.name = name;
        this.symbol = symbol == null ? "" : symbol;
        this.suffix = suffix;
        DecimalFormat df;
        try {
            df = new DecimalFormat(pattern == null ? "#,##0.##" : pattern);
        } catch (IllegalArgumentException e) {
            df = new DecimalFormat("#,##0.##");
        }
        this.format = df;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String format(double amount) {
        String number = format.format(amount);
        return suffix ? number + symbol : symbol + number;
    }
}
