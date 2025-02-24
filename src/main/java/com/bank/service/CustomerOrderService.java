package com.bank.service;

import com.bank.client.CustomerOrderClient;
import com.bank.client.ProductDetailsClient;
import com.bank.client.dto.response.CustomerOrder;
import com.bank.client.dto.response.ProductDetails;
import com.bank.dto.response.OrderCustomerTotal;
import com.bank.dto.response.OrderCustomerTotal.Purchase;
import com.bank.dto.response.RecommendationCustomer;
import com.bank.exception.ClientErrorException;
import com.bank.mapper.OrderCustomerTotalMapper;
import com.bank.monitoring.Observable;
import com.bank.service.support.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.bank.dto.ErrorType.NO_CONTENT;
import static com.bank.dto.Message.NOTHING_FOUND;
import static com.bank.monitoring.Observable.OperationType.REST;
import static com.bank.monitoring.domain.MetricValues.ALL_PURCHASES;
import static com.bank.monitoring.domain.MetricValues.GREATEST_PURCHASE;
import static com.bank.monitoring.domain.MetricValues.LOYAL_CUSTOMERS;
import static com.bank.monitoring.domain.MetricValues.RECOMMENDATION;
import static com.bank.util.DecimalFormatter.format;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import static org.bouncycastle.math.ec.ECConstants.THREE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final MessageService messageService;
    private final CustomerOrderClient customerOrderClient;
    private final ProductDetailsClient productDetailsClient;

    @Observable(operation = ALL_PURCHASES, type = REST)
    public List<OrderCustomerTotal> findAll() {
        return getOrderCustomer(null).parallelStream()
            .sorted(comparing(OrderCustomerTotal::getTotalValue))
            .toList();
    }

    @Observable(operation = GREATEST_PURCHASE, type = REST)
    public OrderCustomerTotal findGreatestBy(final Integer year) {
        return getOrderCustomer(year).parallelStream()
                .max(comparing(OrderCustomerTotal::getTotalValue))
                .orElseThrow(() -> new ClientErrorException(NO_CONTENT, messageService.get(NOTHING_FOUND)));
    }

    @Observable(operation = LOYAL_CUSTOMERS, type = REST)
    public List<OrderCustomerTotal> findLoyalCustomers() {
        return getOrderCustomer(null).parallelStream()
                .sorted(comparing(OrderCustomerTotal::getTotalValue).reversed())
                .limit(THREE.intValue())
                .toList();
    }

    @Observable(operation = RECOMMENDATION, type = REST)
    public List<RecommendationCustomer> findCustomerRecommendations() {
        return getOrderCustomer(null).parallelStream()
                .map(CustomerOrderService::getRecommendationCustomer)
                .toList();
    }

    private static RecommendationCustomer getRecommendationCustomer(final OrderCustomerTotal order) {
        final var purchaseMap = order.getPurchases().stream()
                .collect(groupingBy(Purchase::getType, summingInt(Purchase::getQuantity))).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        return RecommendationCustomer.builder()
                .name(order.getName())
                .document(order.getDocument())
                .productType(purchaseMap.getKey())
                .totalQuantity(purchaseMap.getValue())
                .build();
    }

    private List<OrderCustomerTotal> getOrderCustomer(final Integer year) {
        return new OrderCustomerTotalMapper().apply(getCustomerOrders(), getStringProductDetailsMap(year)).stream()
                .map(order -> summarizer(year, order))
                .toList();
    }

    private Map<String, ProductDetails> getStringProductDetailsMap(Integer year) {
        final var products = ofNullable(productDetailsClient.getProducts())
                .orElseThrow(() -> new ClientErrorException(NO_CONTENT, messageService.get(NOTHING_FOUND)));
        log.info("Product details integration finished, quantity: {}", products.size());

        final var productsMap = of(products.stream()
                .filter(product -> isNull(year) || product.getYear().equals(year))
                .collect(toMap(ProductDetails::getId, identity())))
                .filter(MapUtils::isNotEmpty)
                .orElseThrow(() -> new ClientErrorException(NO_CONTENT, messageService.get(NOTHING_FOUND)));
        log.info("Product details filter by year finished, quantity: {}", productsMap.size());

        return productsMap;
    }

    private List<CustomerOrder> getCustomerOrders() {
        final var customerOrders = ofNullable(customerOrderClient.getCustomerOrders())
                .orElseThrow(() -> new ClientErrorException(NO_CONTENT, messageService.get(NOTHING_FOUND)));
        log.info("Customer orders integration finished, quantity: {}", customerOrders.size());

        return customerOrders;
    }

    private OrderCustomerTotal summarizer(final Integer year, final OrderCustomerTotal order) {
        if (nonNull(year)) {
            order.getPurchases().stream()
                    .max(comparing(p -> p.getPrice().multiply(valueOf(p.getQuantity()))))
                    .ifPresent(p -> order.setPurchases(singletonList(p)));
        }

        var totalQuantity = order.getPurchases().stream()
                .map(Purchase::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();

        var totalValue = (order.getPurchases().stream()
                .map(Purchase::getPrice)
                .reduce(ZERO, BigDecimal::add))
                .multiply(valueOf(totalQuantity));

        order.setTotalQuantity(totalQuantity);
        order.setTotalValue(format(totalValue));

        return order;
    }
}
