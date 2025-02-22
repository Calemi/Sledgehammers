package com.calemi.ccore.api.string;

import java.text.DecimalFormat;

/**
 * Use this class to help format strings.
 */
public class StringHelper {

    /**
     * Surrounds the string with brackets.
     * @param str The string to be boxed.
     * @return a boxed string (like [str])
     */
    public static String boxString(String str) {
        return "[" + str + "]";
    }

    /**
     * @param amount The number to insert commas in.
     * @return A number with commas (like 1,000,000)
     */
    public static String insertCommas(int amount) {

        String number = String.valueOf(amount);
        double amountD = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amountD);
    }
}