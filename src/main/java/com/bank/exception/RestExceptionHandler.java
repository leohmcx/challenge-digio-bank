package com.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

import static java.lang.String.format;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ProblemDetail handleGeneralException(GeneralException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final var fieldErrors = e.getFieldErrors().parallelStream()
                .map(f -> new InvalidParam(f.getField(), f.getDefaultMessage()))
                .toList();

        final var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Your request parameter is not valid");
        pb.setProperty("invalid-params", fieldErrors);

        return pb;
    }

    @ExceptionHandler
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException ex) {
        final var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Your request parameter is not valid");
        pb.setProperty("invalid-params", format("'%s' should be a valid '%s' and '%s' isn't", ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName(), ex.getValue()));

        return pb;
    }

    private record InvalidParam(String name, String reason){}
}
