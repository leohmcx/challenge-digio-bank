package com.bank.util;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static java.math.RoundingMode.FLOOR;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class DecimalFormatter {

    private static final int DECIMAL_PLACES = 2;

    public static BigDecimal format(final BigDecimal valor) {
        return valor.setScale(DECIMAL_PLACES, FLOOR);
    }
}
