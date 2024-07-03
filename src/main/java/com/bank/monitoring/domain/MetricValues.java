package com.bank.monitoring.domain;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class MetricValues {

    public static final String ALL_PURCHASES = "find_all_purchases";
    public static final String LOYAL_CUSTOMERS = "loyal_customers";
    public static final String GREATEST_PURCHASE = "greatest_purchase";
    public static final String RECOMMENDATION = "recommendation";
}
