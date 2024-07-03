package com.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class GeneralException extends RuntimeException {

    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pb.setTitle("Internal exception error");

        return pb;
    }
}
