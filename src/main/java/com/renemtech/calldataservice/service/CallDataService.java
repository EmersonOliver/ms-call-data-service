package com.renemtech.calldataservice.service;


import com.renemtech.calldataservice.api.ParametersServiceClient;
import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.model.CallDataEntity;
import com.renemtech.calldataservice.model.CallerLocationEntity;
import com.renemtech.calldataservice.model.dto.CallDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CallerDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CreateCallDataRequest;
import com.renemtech.calldataservice.model.dto.UpdateCallDataRequest;
import com.renemtech.calldataservice.repository.CallDataServiceRepository;
import com.renemtech.calldataservice.repository.CallLocationDataRespository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CallDataService {


    @Inject
    private CallDataServiceRepository callDataServiceRepository;

    @Inject
    private CallLocationDataRespository callLocationDataRespository;

    @Inject
    @RestClient
    private ParametersServiceClient client;

    @Transactional
    public Optional<UUID> createCallDataStart(CreateCallDataRequest request) {
        CallDataEntity callDataEntity = CallDataEntity.builder()
                .callerNumber(request.getCallerNumber())
                .callStatus(request.getCallStatus())
                .callType(request.getCallType())
                .carrier(request.getCarrier())
                .receiveNumber(request.getReceiverNumber())
                .build();

        callDataServiceRepository.persistAndFlush(callDataEntity);

        CallerLocationEntity callerLocationEntity
                = CallerLocationEntity.builder()
                .callId(callDataEntity.getCallId())
                .callerAreaCode(request.getCallerAreaCode())
                .callerDeviceImei(request.getCallerDeviceImei())
                .callerDeviceModel(request.getCallerDeviceModel())
                .callerLatitude(request.getCallerLatitude())
                .callerLongitude(request.getCallerLongitude())
                .callData(callDataEntity)
                .build();
        callLocationDataRespository.persistAndFlush(callerLocationEntity);

        return Optional.of(callDataEntity.getCallId());
    }

    @Transactional
    public Optional<CallDataEntity> receiverCallStatus(String callId, String callerNumber, CallStatus status, UpdateCallDataRequest request) {

         CallDataEntity callDataEntity =
                this.callDataServiceRepository.findById(UUID.fromString(callId));

        CallerLocationEntity callerLocationEntity =
                this.callLocationDataRespository.findLocationByDataCall(callDataEntity.getCallId()).orElse(null);

        if (!callDataEntity.getCallerNumber().replace("+", "").equals(callerNumber)) {
            return Optional.empty();
        }
        callDataEntity.setCallStatus(status);
        status.build(callDataEntity);
        this.callDataServiceRepository.persistAndFlush(callDataEntity);

        if (callerLocationEntity != null) {
            callerLocationEntity.setReceiverAreaCode(request.getReceiverAreaCode());
            callerLocationEntity.setReceiverLatitude(request.getReceiverLatitude());
            callerLocationEntity.setReceiverLongitude(request.getReceiverLongitude());
            callerLocationEntity.setReceiverDeviceImei(request.getReceiverDeviceImei());
            callerLocationEntity.setReceiverNetworkType(request.getReceiverNetworkType());
            callerLocationEntity.setReceiverDeviceModel(request.getReceiverDeviceModel());
            this.callLocationDataRespository.persistAndFlush(callerLocationEntity);
        }
        return Optional.of(callDataEntity);
    }

    @Transactional
    public CallDataDetailsResponse detailsCallData(String callID){
        CallDataEntity callDataEntity = this.callDataServiceRepository.findById(UUID.fromString(callID));
        List<CallerDataDetailsResponse> callerDetails =
                callDataEntity.getCallerLocations().stream().map(detail-> CallerDataDetailsResponse
                        .builder().billingType(detail.getBillingType()).callCost(detail.getCallCost()).callerLocation(detail.getCallerLocation())
                                .callerNetworkType(detail.getCallerNetworkType()).callerLongitude(detail.getCallerLongitude())
                                .receiverAreaCode(detail.getReceiverAreaCode())
                                .callerLatitude(detail.getCallerLatitude())
                                .receiverLongitude(detail.getReceiverLongitude())
                                .receiverDeviceImei(detail.getReceiverDeviceImei()).receiverDeviceModel(detail.getReceiverDeviceModel())
                                .receiverLatitude(detail.getReceiverLatitude()).videoQuality(detail.getVideoQuality())
                                .build())
                        .toList();
        return CallDataDetailsResponse.builder()
                .callDuration(callDataEntity.getCallDuration())
                .callDhEnd(callDataEntity.getCallDhEnd())
                .callDhStart(callDataEntity.getCallDhStart())
                .details(callerDetails)
                .callerNumber(callDataEntity.getCallerNumber())
                .callId(callDataEntity.getCallId())
                .callStatus(callDataEntity.getCallStatus())
                .receiveNumber(callDataEntity.getReceiveNumber())
                .carrier(callDataEntity.getCarrier())
                .callType(callDataEntity.getCallType())
                .build();
    }

}
