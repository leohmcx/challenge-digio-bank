package com.bank.controller;

import com.bank.dto.response.OrderCustomerTotal;
import com.bank.dto.response.RecommendationCustomer;
import com.bank.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1")
public class CustomerOrderController implements CustomerOrderControllerApi {

    private final CustomerOrderService service;

    @GetMapping("/compras")
    public ResponseEntity<List<OrderCustomerTotal>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/maior_compra/{year}")
    public ResponseEntity<OrderCustomerTotal> findGreatestBy(@PathVariable Integer year) {
        return ResponseEntity.ok(service.findGreatestBy(year));
    }

    @GetMapping("/clientes_fieis")
    public ResponseEntity<List<OrderCustomerTotal>> findLoyalCustomers() {
        return ResponseEntity.ok(service.findLoyalCustomers());
    }

    @GetMapping("/recomendacao/cliente/tipo")
    public ResponseEntity<List<RecommendationCustomer>> findCustomerRecommendations() {
        return ResponseEntity.ok(service.findCustomerRecommendations());
    }
}
