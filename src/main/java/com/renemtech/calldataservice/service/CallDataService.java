package com.renemtech.calldataservice.service;


import com.renemtech.calldataservice.api.ParametersServiceClient;
import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.exceptions.BusinessException;
import com.renemtech.calldataservice.model.CallDataEntity;
import com.renemtech.calldataservice.model.CallerLocationEntity;
import com.renemtech.calldataservice.model.dto.CallDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CallerDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CreateCallDataRequest;
import com.renemtech.calldataservice.model.dto.UpdateCallDataRequest;
import com.renemtech.calldataservice.rabbitmq.message.Quarantine;
import com.renemtech.calldataservice.rabbitmq.producer.CallDataQuarantineProducer;
import com.renemtech.calldataservice.repository.CallDataServiceRepository;
import com.renemtech.calldataservice.repository.CallLocationDataRespository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@ApplicationScoped
public class CallDataService {


    @Inject
    CallDataServiceRepository callDataServiceRepository;

    @Inject
    CallLocationDataRespository callLocationDataRespository;

    @Inject
    CallDataQuarantineProducer producer;

    @Inject
    @RestClient
    ParametersServiceClient client;

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
    public CallDataDetailsResponse detailsCallData(String callID) {
        CallDataEntity callDataEntity = this.callDataServiceRepository.findById(UUID.fromString(callID));
        List<CallerDataDetailsResponse> callerDetails =
                callDataEntity.getCallerLocations().stream().map(detail -> CallerDataDetailsResponse
                        .builder().callerLocation(detail.getCallerLocation())
                        .callerLongitude(detail.getCallerLongitude())
                        .receiverAreaCode(detail.getReceiverAreaCode())
                        .callerLatitude(detail.getCallerLatitude())
                        .receiverLongitude(detail.getReceiverLongitude())
                        .receiverDeviceImei(detail.getReceiverDeviceImei()).receiverDeviceModel(detail.getReceiverDeviceModel())
                        .receiverLatitude(detail.getReceiverLatitude())
                        .build()).toList();

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

    public Map<String, Object> checkCallByCaller(String callid, String callerReceiverNumber, String callDhStart, CallStatus status) {
        try {
            Map<String, Object> response = new HashMap<>();
            Date dateCallDhStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(callDhStart);
            this.callDataServiceRepository.findByIdOptional(UUID.fromString(callid))
                    .filter(call -> validateDhStartCall(call.getCallDhStart(), dateCallDhStart) && call.getCallStatus().equals(status)
                            && call.getReceiveNumber().equals(callerReceiverNumber))
                    .stream().findFirst()
                    .orElseThrow(BusinessException::notDataFound);
            response.put("checked", "OK");
            return response;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendoToQuarantine(Quarantine quarantine) {
        this.producer.sendMessage(quarantine);
    }

    private Boolean validateDhStartCall(Date dhStart, Date dhStartRequest) {
        LocalDateTime localDateTimeStart = dhStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateTimeStartReq = dhStartRequest.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTimeStart.isAfter(localDateTimeStartReq);
    }
}
