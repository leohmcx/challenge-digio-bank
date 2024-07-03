package com.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendationCustomer {

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cpf")
    private String document;

    @JsonProperty("recomendacao")
    private String productType;

    @JsonProperty("totalQty")
    private Integer totalQuantity;
}
