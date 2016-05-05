package dev.journey.toolkit.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by mwp on 16/5/5.
 */
public class StringUtils {
    public static String formatDouble(double value) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.UP);
            return df.format(value);
        } catch (Exception e) {
            return Double.toString(value);
        }
    }

    public static String to100Percent(double value) {
        try {
            DecimalFormat df = new DecimalFormat("#.##%");
            df.setRoundingMode(RoundingMode.UP);
            return df.format(value);
        } catch (Exception e) {
            return Double.toString(value);
        }
    }
}
