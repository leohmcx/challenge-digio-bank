package com.bank.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static java.math.RoundingMode.FLOOR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DecimalFormatter {

    private static final int DECIMAL_PLACES = 2;

    public static BigDecimal format(final BigDecimal valor) {
        return valor.setScale(DECIMAL_PLACES, FLOOR);
    }
}
