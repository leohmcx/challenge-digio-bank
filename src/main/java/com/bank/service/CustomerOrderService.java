package com.bank.service;

import com.bank.client.CustomerOrderClient;
import com.bank.client.ProductDetailsClient;
import com.bank.client.dto.response.ProductDetails;
import com.bank.dto.response.OrderCustomerTotal;
import com.bank.dto.response.OrderCustomerTotal.Purchase;
import com.bank.dto.response.RecommendationCustomer;
import com.bank.exception.NoContentException;
import com.bank.mapper.OrderCustomerTotalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.bank.util.DecimalFormatter.format;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import static org.bouncycastle.math.ec.ECConstants.THREE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final CustomerOrderClient customerOrderClient;
    private final ProductDetailsClient productDetailsClient;

    public List<OrderCustomerTotal> findAll() {
        return getOrderCustomer(null).parallelStream()
                .sorted(comparing(OrderCustomerTotal::getTotalValue))
                .toList();
    }

    public OrderCustomerTotal findGreatestBy(final Integer year) {
        return getOrderCustomer(year).parallelStream()
                .max(comparing(OrderCustomerTotal::getTotalValue))
                .orElseThrow(() -> new NoContentException("No greatest value found"));
    }

    public List<OrderCustomerTotal> findLoyalCustomers() {
        return getOrderCustomer(null).parallelStream()
                .sorted(comparing(OrderCustomerTotal::getTotalValue).reversed())
                .limit(THREE.intValue())
                .toList();
    }

    public List<RecommendationCustomer> findCustomerRecommendations() {
        return getOrderCustomer(null).parallelStream()
                .map(order -> {
                    final var purchaseMap = order.getPurchases().parallelStream()
                            .collect(groupingBy(Purchase::getType, summingInt(Purchase::getQuantity)))
                            .entrySet()
                            .parallelStream()
                            .max(Map.Entry.comparingByValue())
                            .orElseThrow();

                    return RecommendationCustomer.builder()
                            .name(order.getName())
                            .document(order.getDocument())
                            .productType(purchaseMap.getKey())
                            .totalQuantity(purchaseMap.getValue())
                            .build();
                }).toList();
    }

    private List<OrderCustomerTotal> getOrderCustomer(final Integer year) {
        final var customerOrders = ofNullable(customerOrderClient.getCustomerOrders())
                .orElseThrow(() -> new NoContentException("No customer found"));
        log.info("Customer orders integration finished, quantity: {}", customerOrders.size());

        final var products = ofNullable(productDetailsClient.getProducts())
                .orElseThrow(() -> new NoContentException("No product found"));
        log.info("Product details integration finished, quantity: {}", products.size());

        final var productsMap = of(products.parallelStream()
                .filter(product -> isNull(year) || product.getYear().equals(year))
                .collect(toMap(ProductDetails::getId, p -> p)))
                .filter(MapUtils::isNotEmpty)
                .orElseThrow(() -> new NoContentException("No product found for year " + year));
        log.info("Product details filter by year finished, quantity: {}", productsMap.size());

        return new OrderCustomerTotalMapper().apply(customerOrders, productsMap).parallelStream()
                .peek(order -> {
                    if (nonNull(year)) {
                        order.getPurchases().parallelStream()
                                .max(comparing(p -> p.getPrice().multiply(valueOf(p.getQuantity()))))
                                .ifPresent(p -> order.setPurchases(singletonList(p)));
                    }

                    var totalQuantity = order.getPurchases().parallelStream()
                            .map(Purchase::getQuantity)
                            .mapToInt(Integer::intValue)
                            .sum();

                    var totalValue = (order.getPurchases().parallelStream()
                            .map(Purchase::getPrice)
                            .reduce(ZERO, BigDecimal::add))
                            .multiply(valueOf(totalQuantity));

                    order.setTotalQuantity(totalQuantity);
                    order.setTotalValue(format(totalValue));
                }).toList();
    }
}
