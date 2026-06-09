package me.docdrewskii.profitmultiplier.currency;

import java.text.DecimalFormat;

/**
 * A display currency: a symbol, where it sits relative to the number, and a number format.
 *
 * ProfitMultiplier never moves money itself — it scales the price the shop transaction
 * already uses, so the multiplier is inherently currency-agnostic (works with Vault,
 * PlayerPoints, gems, tokens, or any currency the shop pays in). This class only controls
 * how amounts are RENDERED in messages and placeholders.
 */
public class Currency {

    private final String name;
    private final String symbol;
    private final boolean suffix;       // true = symbol after the number
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
