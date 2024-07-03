package com.bank.client.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(exclude = {"document", "purchases"})
public class CustomerOrder {

    @JsonAlias("nome")
    private String name;

    @JsonAlias("cpf")
    private String document;

    @JsonAlias("compras")
    private List<Purchase> purchases;

    @Data
    public static final class Purchase {
        @JsonAlias("codigo")
        private String id;

        @JsonAlias("quantidade")
        private Integer quantity;
    }
}
