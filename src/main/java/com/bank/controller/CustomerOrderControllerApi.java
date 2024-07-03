package com.bank.controller;

import com.bank.dto.response.OrderCustomerTotal;
import com.bank.dto.response.RecommendationCustomer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@OpenAPIDefinition(info = @Info(
        title = "Purchase Order API",
        version = "1.0",
        description = "API for purchase order management"
))
public interface CustomerOrderControllerApi {

    @Operation(summary = "Retornar uma lista das compras ordenadas de forma crescente por valor"
            , description = "Retornar uma lista das compras ordenadas de forma crescente por valor, deve conter o " +
            "nome dos clientes, cpf dos clientes, <br/> dado dos produtos, quantidade das compras e " +
            "valores totais de cada compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase orders"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<List<OrderCustomerTotal>> findAll();

    @Operation(summary = "Retornar a maior compra do ano informando os dados da compra"
            , description = "Retornar a maior compra do ano informando os dados da compra disponibilizados, deve ter " +
            "o nome do cliente, cpf do cliente, dado do produto, quantidade da compra e seu valor total.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greatest purchase order found per year"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<OrderCustomerTotal> findGreatestBy(@PathVariable Integer year);

    @Operation(summary = "Retornar o Top 3 clientes mais fieis"
            , description = "Retornar o Top 3 clientes mais fieis, clientes que possuem mais compras recorrentes com " +
            "maiores valores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top 3 clientes mais fieis obtidas"),
            @ApiResponse(responseCode = "404", description = "Top 3 clientes mais fieis não encontradas"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<List<OrderCustomerTotal>> findLoyalCustomers();

    @Operation(summary = "Retornar uma recomendação"
            , description = "Retornar uma recomendação de vinho baseado nos tipos de vinho que o cliente mais compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recomendações de vinhos obtidas"),
            @ApiResponse(responseCode = "404", description = "Recomendações de vinhos não encontradas"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    ResponseEntity<List<RecommendationCustomer>> findCustomerRecommendations();
}
