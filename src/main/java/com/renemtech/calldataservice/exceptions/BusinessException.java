package com.renemtech.calldataservice.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class BusinessException extends RuntimeException {

    private final Response.Status status;

    public BusinessException(Response.Status status) {
            this.status = status;
    }

    public static BusinessException generic() {
        throw new BusinessException("A generic error occurred while running the application.", Response.Status.INTERNAL_SERVER_ERROR);
    }

    public static BusinessException notDataFound() {
        Response.Status status =  Arrays.stream(Response.Status.values()).filter(p-> p.getStatusCode() == 422)
                .findFirst().orElse(Response.Status.NOT_FOUND);
        throw new BusinessException("No information was found with the search parameters",status);
    }

    public BusinessException(String message, Response.Status status) {
        super(message);
        this.status = status;
    }


}
