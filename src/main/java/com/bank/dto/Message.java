package com.bank.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Message {

    FALHA_INESPERADA("falha.inesperada"),
    NENHUM_RESULTADO_ENCONTRADO("nenhum.resultado.encontrado"),
    SERVICO_INDISPONIVEL("servico.indisponivel"),
    FALHA_FEIGN_CLIENT("falha.feign.client");

    private final String message;
}
