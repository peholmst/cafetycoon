package org.vaadin.samples.cafetycoon.ui.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for working with money.
 */
public final class MoneyUtils {

	private MoneyUtils() {
	}

	/**
	 * Formats the specified amount using the US locale.
	 * 
	 * @param amount
	 *            the amount to format.
	 * @return a formatted string representation of the amount.
	 */
	public static String formatMoney(BigDecimal amount) {
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
		return currencyFormatter.format(amount.doubleValue());
	}
}
