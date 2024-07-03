package com.bank.exception;

import com.bank.dto.ErrorType;
import lombok.Getter;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class AbstractErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8004100005407012963L;

    private final Map<String, String> details;

    AbstractErrorException(String msg) {
        super(msg);
        details = new ConcurrentHashMap<>();
    }

    AbstractErrorException(String msg, Throwable th) {
        super(msg, th);
        details = new ConcurrentHashMap<>();
    }

    public abstract ErrorType getErrorType();

}
