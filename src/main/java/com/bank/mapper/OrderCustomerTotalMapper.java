package com.bank.mapper;

import com.bank.client.dto.response.CustomerOrder;
import com.bank.client.dto.response.ProductDetails;
import com.bank.dto.response.OrderCustomerTotal;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.bank.util.DecimalFormatter.format;

public class OrderCustomerTotalMapper implements BiFunction<List<CustomerOrder>, Map<String, ProductDetails>, List<OrderCustomerTotal>> {

    @Override
    public List<OrderCustomerTotal> apply(List<CustomerOrder> orders, Map<String, ProductDetails> prod) {
        return orders.parallelStream()
                .map(order -> OrderCustomerTotal.builder()
                        .name(order.getName())
                        .document(order.getDocument())
                        .purchases(order.getPurchases().parallelStream()
                                .filter(purchase -> prod.containsKey(purchase.getId()))
                                .map(purchase -> OrderCustomerTotal.Purchase.builder()
                                        .id(purchase.getId())
                                        .type(prod.get(purchase.getId()).getType())
                                        .price(format(prod.get(purchase.getId()).getPrice()))
                                        .harvest(prod.get(purchase.getId()).getHarvest())
                                        .year(prod.get(purchase.getId()).getYear())
                                        .quantity(purchase.getQuantity())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }
}
