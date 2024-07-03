package com.bank.interceptor;

import com.bank.dto.ErrorResponse;
import com.bank.dto.ErrorType;
import com.bank.exception.ClientErrorException;
import com.bank.exception.ServerErrorException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerErrorInterceptor {

    private static final String ERROR_DETAIL_PROPERTY = "response body";

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ClientErrorException clientErrorException) {
        final var error = ErrorResponse.builder()
                .errorType(clientErrorException.getErrorType())
                .message(clientErrorException.getMessage())
                .details(clientErrorException.getDetails())
                .build();

        log.error("Error API {}", clientErrorException.getMessage());

        return new ResponseEntity<>(error, clientErrorException.getErrorType().getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ServerErrorException serverErrorException) {
        final var error = ErrorResponse.builder()
                .errorType(serverErrorException.getErrorType())
                .message(serverErrorException.getMessage())
                .details(serverErrorException.getDetails())
                .build();

        log.error("Internal Error {}", serverErrorException.getMessage());

        return new ResponseEntity<>(error, serverErrorException.getErrorType().getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(FeignException feignException) {
        final var errorMessage = formatErrorMessage(feignException);
        final var errorDetail = Map.of(ERROR_DETAIL_PROPERTY, feignException.contentUTF8());

        final var error = ErrorResponse.builder()
            .errorType(ErrorType.of(HttpStatus.valueOf(feignException.status())))
            .message(errorMessage)
            .details(errorDetail)
            .build();

        log.error("Error while calling client {}", errorMessage);

        return new ResponseEntity<>(error, error.getErrorType().getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException ex) {
        final var detail = format("'%s' should be a valid '%s' and '%s' isn't", ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName(), ex.getValue());

        final var error = ErrorResponse.builder()
                .errorType(ErrorType.of(HttpStatus.BAD_REQUEST))
                .message("Your request parameter is not valid")
                .details(Map.of(ERROR_DETAIL_PROPERTY, detail))
                .build();

        log.error("Error while controller param mismatch {}", error);

        return new ResponseEntity<>(error, error.getErrorType().getHttpStatus());
    }

    private String formatErrorMessage(FeignException exception) {
        return exception.getMessage()
                .replace(": [" + exception.contentUTF8() + "]", EMPTY);
    }
}
