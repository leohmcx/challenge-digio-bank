package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum Message {

    UNEXPECTED_FAILED("falha.inesperada"),
    NOTHING_FOUND("nenhum.resultado.encontrado"),
    SERVICE_UNAVAILABLE("servico.indisponivel"),
    FEIGN_CLIENT_FAILED("falha.feign.client");

    private final String message;
}
