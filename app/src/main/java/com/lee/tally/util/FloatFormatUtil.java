package com.lee.tally.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloatFormatUtil {
    public static float reformatFloat(float num) {
        BigDecimal decimal = new BigDecimal(num);
        return decimal.setScale(4, RoundingMode.HALF_UP).floatValue();
    }

    public static String getPercentage(float num) {
        BigDecimal decimal = new BigDecimal(100 * num);
        return decimal.setScale(2, RoundingMode.HALF_UP).floatValue() + "%";
    }
}
