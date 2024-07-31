package com.renemtech.calldataservice.exceptions.handlers;

import com.renemtech.calldataservice.exceptions.BusinessException;
import com.renemtech.calldataservice.exceptions.mapper.ErrorMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;

@Provider
public class MapperExceptionHandler implements ExceptionMapper<Exception> {

    private Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    @Override
    public Response toResponse(Exception exception) {
        ErrorMapper errorMapper = ErrorMapper.builder()
                .status(status.getReasonPhrase())
                .message(exception.getMessage())
                .details(new ArrayList<>())
                .build();
        if(exception instanceof BusinessException) {
            if(((BusinessException) exception).getStatus() != null) {
                status = ((BusinessException) exception).getStatus();
                errorMapper.setStatus(status.getReasonPhrase());
            }
        }
        return Response.status(status).entity(errorMapper).build();
    }
}
