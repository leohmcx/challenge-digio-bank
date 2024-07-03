package com.bank.exception;

import com.bank.dto.ErrorType;

import java.io.Serial;

public class ServerErrorException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = -5809529426540256544L;

    public ServerErrorException(String msg) {
        super(msg);
    }

    public ServerErrorException(String msg, Throwable th) {
        super(msg, th);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.INTERNAL_ERROR;
    }
}
