package com.bank.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@RequiredArgsConstructor
public class NoContentException extends GeneralException {

    private final String description;

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Product Customer not found");
        pb.setDetail(description);

        return pb;
    }
}
