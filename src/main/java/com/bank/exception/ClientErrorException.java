package com.bank.exception;

import com.bank.dto.ErrorType;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ClientErrorException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = 1486297407038116871L;

    private final ErrorType errorType;

    public ClientErrorException(ErrorType errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }
}
