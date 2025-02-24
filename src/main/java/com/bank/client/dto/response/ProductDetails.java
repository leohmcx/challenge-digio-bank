package com.bank.client.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(exclude = {"id", "price", "harvest"})
public class ProductDetails {
    @JsonAlias("codigo")
    private String id;

    @JsonAlias("tipo_vinho")
    private String type;

    @JsonAlias("preco")
    private BigDecimal price;

    @JsonAlias("safra")
    private String harvest;

    @JsonAlias("ano_compra")
    private Integer year;
}
