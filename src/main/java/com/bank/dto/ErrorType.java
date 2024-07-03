package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
public enum ErrorType {

    BUSINESS(HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    NO_CONTENT(HttpStatus.NO_CONTENT),
    VALIDATION(HttpStatus.BAD_REQUEST),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY);

    private final HttpStatus httpStatus;

    public static ErrorType of(HttpStatus value) {
        return Arrays.stream(values())
            .filter(errorType -> value.equals(errorType.getHttpStatus()))
            .findFirst()
            .orElse(INTERNAL_ERROR);
    }
}
