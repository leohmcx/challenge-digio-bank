package com.bank.client;

import com.bank.client.dto.response.ProductDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("product-api")
public interface ProductDetailsClient {

    @GetMapping
    List<ProductDetails> getProducts();
}
