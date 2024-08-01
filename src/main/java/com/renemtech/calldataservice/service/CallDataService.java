package com.renemtech.calldataservice.service;


import com.renemtech.calldataservice.api.ParametersServiceClient;
import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.exceptions.BusinessException;
import com.renemtech.calldataservice.model.ReceiverCallEntity;
import com.renemtech.calldataservice.model.CallerCallEntity;
import com.renemtech.calldataservice.model.dto.CallDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CallerDataDetailsResponse;
import com.renemtech.calldataservice.model.dto.CreateCallDataRequest;
import com.renemtech.calldataservice.model.dto.UpdateCallDataRequest;
import com.renemtech.calldataservice.rabbitmq.message.Quarantine;
import com.renemtech.calldataservice.rabbitmq.producer.CallDataQuarantineProducer;
import com.renemtech.calldataservice.repository.CallDataServiceRepository;
import com.renemtech.calldataservice.repository.CallLocationDataRespository;
import com.renemtech.calldataservice.utils.CryptoUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

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
    public Map<String, Object> createCallDataStart(CreateCallDataRequest request)  {
        ReceiverCallEntity receiverEntity = ReceiverCallEntity.builder()
                .receiveNumber(request.getReceiverNumber())
                .build();

        callDataServiceRepository.persistAndFlush(receiverEntity);
        Map<String, Object> mapsAuth = CryptoUtils.generateAuthCall(request.getCallerNumber());

        CallerCallEntity callerEntity
                = CallerCallEntity.builder()
                .callId(receiverEntity.getCallId())
                .callerAreaCode(request.getCallerAreaCode())
                .callerDeviceImei(request.getCallerDeviceImei())
                .callerDeviceModel(request.getCallerDeviceModel())
                .callerLatitude(request.getCallerLatitude())
                .callerLongitude(request.getCallerLongitude())
                .callerNumber(request.getCallerNumber())
                .callType(request.getCallType())
                .hashCall(String.valueOf(mapsAuth.get("hash")))
                .callData(receiverEntity)
                .build();
        callLocationDataRespository.persistAndFlush(callerEntity);
        mapsAuth.put("callId", callerEntity.getCallId());
        mapsAuth.remove("hash");


        return mapsAuth;
    }

    @Transactional
    public Optional<ReceiverCallEntity> receiverCallUpdate(String callId, String callerNumber, CallStatus status, UpdateCallDataRequest request, String salt) throws NoSuchAlgorithmException {

        ReceiverCallEntity receiverEntity =
                this.callDataServiceRepository.findById(UUID.fromString(callId));

        CallerCallEntity callerEntity =
                this.callLocationDataRespository.findLocationByDataCall(receiverEntity.getCallId())
                        .orElseThrow(BusinessException::notDataFound);
        if (!CryptoUtils.validatePassword(callerNumber, callerEntity.getHashCall(), salt)) {
            throw new BusinessException("You are receiving a suspicious call!");
        }
        if (!callerEntity.getCallerNumber().equals(callerNumber)) {
            throw new BusinessException("Caller Number is not equals in hash call", Response.Status.CONFLICT);
        }

        callerEntity.setCallStatus(status);
        status.build(callerEntity);
        receiverEntity.setCarrier(request.getCarrier());
        receiverEntity.setReceiverDeviceModel(request.getReceiverDeviceModel());
        receiverEntity.setReceiveNumber(request.getReceiverNumber());
        receiverEntity.setReceiverLongitude(request.getReceiverLongitude());
        receiverEntity.setReceiverLatitude(request.getReceiverLatitude());
        receiverEntity.setReceiverAreaCode(request.getReceiverAreaCode());
        receiverEntity.setReceiverDeviceImei(request.getReceiverDeviceImei());

        this.callDataServiceRepository.persistAndFlush(receiverEntity);
        return Optional.of(receiverEntity);
    }

    @Transactional
    public CallDataDetailsResponse detailsCallData(String callID) {
        ReceiverCallEntity receiverCallEntity = this.callDataServiceRepository.findById(UUID.fromString(callID));

        List<CallerDataDetailsResponse> callerDetails =
                receiverCallEntity.getCallerLocations().stream().map(detail -> CallerDataDetailsResponse
                        .builder().callerLocation(detail.getCallerLocation())
                        .callerLongitude(detail.getCallerLongitude())
                        .callerAreaCode(detail.getCallerAreaCode())
                        .callerLatitude(detail.getCallerLatitude())
                        .callerLongitude(detail.getCallerLongitude())
                        .callerDeviceImei(detail.getCallerDeviceImei()).callerDeviceModel(detail.getCallerDeviceModel())
                        .callerLatitude(detail.getCallerLatitude())
                        .callId(detail.getCallId())
                        .callDuration(detail.getCallDuration())
                        .callType(detail.getCallType())
                        .callDhEnd(detail.getCallDhEnd())
                        .callDhStart(detail.getCallDhStart())
                        .callType(detail.getCallType())
                        .callStatus(detail.getCallStatus())
                        .callType(detail.getCallType())
                        .build()).toList();

        return CallDataDetailsResponse.builder()
                .callId(receiverCallEntity.getCallId())
                .receiveNumber(receiverCallEntity.getReceiveNumber())
                .carrier(receiverCallEntity.getCarrier())
                .details(callerDetails)
                .build();
    }

    public Map<String, Object> checkCallByCaller(String callid, String callerReceiverNumber, String callDhStart, CallStatus status, String salt) {
        try {
            Map<String, Object> response = new HashMap<>();
            Date dateCallDhStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(callDhStart);
            this.callDataServiceRepository.findByIdOptional(UUID.fromString(callid)).map(ReceiverCallEntity::getCallerLocations)
                    .map(List::stream).flatMap(Stream::findFirst)
                    .filter(call -> validateDhStartCall(call.getCallDhStart(), dateCallDhStart)
                            && CryptoUtils.validatePassword(call.getCallerNumber(), call.getHashCall(), salt)
                            && call.getCallStatus().equals(status)
                            && call.getCallerNumber().equals(callerReceiverNumber))
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
