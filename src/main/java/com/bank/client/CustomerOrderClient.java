package com.bank.client;

import com.bank.client.dto.response.CustomerOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("customer-order-api")
public interface CustomerOrderClient {

    @GetMapping
    List<CustomerOrder> getCustomerOrders();
}
