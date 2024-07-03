package com.bank.dto;

import com.bank.exception.AbstractErrorException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import static com.bank.dto.ErrorType.INTERNAL_ERROR;
import static com.bank.dto.Message.UNEXPECTED_FAILED;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8659539917093650919L;

    private final String message;
    private final ErrorType errorType;
    private final Map<String, String> details;

    public static ErrorResponse build(Exception ex) {
        if (ex instanceof AbstractErrorException abstractErrorException) {
            return build(abstractErrorException);
        }

        return buildDefault();
    }

    public static ErrorResponse build(AbstractErrorException ex) {
        return ErrorResponse.builder()
                .errorType(ex.getErrorType())
                .message(ex.getMessage())
                .details(isFalse(ex.getDetails().isEmpty()) ? ex.getDetails() : null)
                .build();
    }

    public static ErrorResponse buildDefault() {
        return ErrorResponse.builder()
                .errorType(INTERNAL_ERROR)
                .message(UNEXPECTED_FAILED.name().replace("_", " "))
                .build();
    }
}
