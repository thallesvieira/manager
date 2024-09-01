package com.calculator.manager.domain.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class with common methods that will be used in the API
 */
public class Utils {

    /**
     * Method to convert double in string with US currency
     * @param value
     * @return
     */
    public static final String getCurrencyValue(Double value) {
        String valorString = NumberFormat.getCurrencyInstance(Locale.US).format(value);
        return valorString;
    }
}
