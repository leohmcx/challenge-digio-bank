package com.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderCustomerTotal {

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cpf")
    private String document;

    @JsonProperty("totalQty")
    private Integer totalQuantity;

    @JsonProperty("totalValor")
    private BigDecimal totalValue;

    @JsonProperty("compras")
    private List<Purchase> purchases;

    @Data
    @Builder
    public static final class Purchase {
        @JsonProperty("codigo")
        private String id;

        @JsonProperty("tipo_vinho")
        private String type;

        @JsonProperty("preco")
        private BigDecimal price;

        @JsonProperty("safra")
        private String harvest;

        @JsonProperty("ano_compra")
        private Integer year;

        @JsonProperty("quantidade")
        private Integer quantity;
    }
}
