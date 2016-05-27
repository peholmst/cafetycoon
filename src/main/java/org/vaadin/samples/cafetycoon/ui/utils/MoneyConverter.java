package org.vaadin.samples.cafetycoon.ui.utils;

import java.math.BigDecimal;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * A one-way converter from {@code BigDecimal} to {@code String}, using
 * {@link MoneyUtils#formatMoney(BigDecimal)}.
 */
@SuppressWarnings("serial")
public class MoneyConverter implements Converter<String, BigDecimal> {

	@Override
	public BigDecimal convertToModel(String value, Class<? extends BigDecimal> targetType, Locale locale)
			throws ConversionException {
		throw new RuntimeException("This is a one-way converter");
	}

	@Override
	public String convertToPresentation(BigDecimal value, Class<? extends String> targetType, Locale locale)
			throws ConversionException {
		if (value == null) {
			return "";
		} else {
			return MoneyUtils.formatMoney(value);
		}
	}

	@Override
	public Class<BigDecimal> getModelType() {
		return BigDecimal.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
}
