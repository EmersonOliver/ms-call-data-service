package com.renemtech.calldataservice.api;


import com.renemtech.calldataservice.api.json.ParametersResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "parameters-api")
public interface ParametersServiceClient {

    @GET
    ParametersResponse getParametersDeviceId(@HeaderParam("deviceId") String deviceId);

}
