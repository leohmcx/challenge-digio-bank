package com.bank.dto;

import com.bank.exception.AbstractErrorException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8659539917093650919L;

    private final ErrorType errorType;
    private final String message;
    private final Map<String, String> details;

    public static ErrorResponse build(Exception ex) {
        if (ex instanceof AbstractErrorException abstractErrorException) {
            return build(abstractErrorException);
        }

        return buildDefault();
    }

    public static ErrorResponse build(AbstractErrorException ex) {
        return ErrorResponse.builder().errorType(ex.getErrorType()).message(ex.getMessage())
            .details(isFalse(ex.getDetails().isEmpty()) ? ex.getDetails() : null).build();
    }

    public static ErrorResponse buildDefault() {
        return ErrorResponse.builder()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(Message.FALHA_INESPERADA.name().replace("_", " "))
                .build();
    }
}
