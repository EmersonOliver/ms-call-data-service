package com.renemtech.calldataservice.resources;


import com.renemtech.calldataservice.api.ParametersServiceClient;
import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.model.CallDataEntity;
import com.renemtech.calldataservice.model.dto.CallDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CreateCallDataRequest;
import com.renemtech.calldataservice.model.dto.UpdateCallDataRequest;
import com.renemtech.calldataservice.rabbitmq.message.Quarantine;
import com.renemtech.calldataservice.service.CallDataService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Path("callDataService")
public class CallDataServiceResource {

    @Inject
    CallDataService service;

    @Inject
    @RestClient
    ParametersServiceClient client;

    @POST
    @Path("create")
    public Response createCallData(CreateCallDataRequest request) {
        UUID response = service.createCallDataStart(request).orElse(null);
        return buildResponseProtocol(response, Response.Status.OK);
    }

    @GET
    @Path("details/{callId}")
    public Response getCallDataServiceDetails(@PathParam("callId") String callId) {
        CallDataDetailsResponse response = this.service.detailsCallData(callId);
        return buildResponse(response, Response.Status.OK);
    }

    @GET
    @Path("check/{callid}")
    public Response getCallDataServiceByCallerReceiver(@PathParam("callid") String callid,
                                                       @QueryParam("callerReceiverNumber") String callerReceiverNumber,
                                                       @QueryParam("callDhStart") String callDhStart,
                                                       @QueryParam("callStatus") CallStatus status) {
        return buildResponse(this.service.checkCallByCaller(callid, callerReceiverNumber, callDhStart, status), Response.Status.OK);
    }

    @PUT
    @Path("update/{callId}")
    public Response updateCallerInfo(@PathParam("callId") String callId,
                                     @QueryParam("callNumber") String callNumber,
                                     @QueryParam("status") CallStatus status,
                                     UpdateCallDataRequest request) {
        Optional<CallDataEntity> response = this.service.receiverCallStatus(callId, callNumber, status, request);
        return response.isPresent() ? buildResponse(response, Response.Status.OK)
                : Response.notModified().build();
    }

    @POST
    @Path("toQuarantine")
    public Response sendToQuarantine(Quarantine req) {
        this.service.sendoToQuarantine(req);
        return Response.accepted().build();
    }

    @GET
    @Path("status")
    public Response getStatus() {
        return Response.ok(client.getParametersDeviceId("")).build();
    }

    public <T> Response buildResponseProtocol(T type, Response.Status status) {
        if (type != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("protocol", type);
            return Response.status(status).entity(map)
                    .build();
        }
        return Response.notModified().build();
    }

    public <T> Response buildResponse(T type, Response.Status status) {
        return Response.status(status).entity(type)
                .build();
    }

}
