package com.renemtech.calldataservice.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.Arrays;

public class BusinessException extends RuntimeException {

    private String msg = "A generic error occurred while running the application.";
    private Throwable root;

    @Getter
    private static Response.Status status;

    @Getter
    private Response.Status responseStatus;

    public BusinessException() {
    }

    public static BusinessException generic() {
        throw new BusinessException("A generic error occurred while running the application.");
    }

    public static BusinessException notDataFound() {
        status = Arrays.stream(Response.Status.values()).filter(p-> p.getStatusCode() == 422)
                .findFirst().orElse(Response.Status.NOT_FOUND);
        throw new BusinessException("No information was found with the search parameters");
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }
    public BusinessException(String message, Response.Status status) {
        super(message);
        this.msg = message;
        this.responseStatus = status;
    }
}
