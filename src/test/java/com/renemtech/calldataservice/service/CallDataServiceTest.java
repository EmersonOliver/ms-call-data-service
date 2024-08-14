package com.renemtech.calldataservice.service;

import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.enuns.CallType;
import com.renemtech.calldataservice.model.dto.CreateCallDataRequest;
import com.renemtech.calldataservice.model.dto.UpdateCallDataRequest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
class CallDataServiceTest {

    @InjectMock
    CallDataService service;


    private static Stream<Arguments> callParameters() {
        Arguments case01 = Arguments.of(CallStatus.IN_PROGRESS, 1);
        Arguments case02 = Arguments.of(CallStatus.CANCELLED, 1);
        Arguments case03 = Arguments.of(CallStatus.BUSY, 1);
        Arguments case04 = Arguments.of(CallStatus.ACCEPTED, 1);
        Arguments case05 = Arguments.of(CallStatus.ON_HOLD, 1);
        Arguments case06 = Arguments.of(CallStatus.MISSED, 1);
        Arguments case07 = Arguments.of(CallStatus.TRANSFERRED, 1);
        return Stream.of(case01, case02, case03, case04, case05, case06, case07);
    }


    @ParameterizedTest
    @MethodSource("callParameters")
    void given_createCallDataStart_test(CallStatus callStatus, int callTimes) {
        CreateCallDataRequest request = createCallDataRequest();
        request.setCallStatus(callStatus);
        Assertions.assertDoesNotThrow(() -> this.service.createCallDataStart(request));
        verify(service, times(callTimes)).createCallDataStart(ArgumentMatchers.any(CreateCallDataRequest.class));
    }

    @ParameterizedTest
    @MethodSource("callParameters")
    void given_updateCallData_test(CallStatus callStatus)  {
        UpdateCallDataRequest request = updateCallDataRequest();
        request.setCallStatus(callStatus);
        Assertions.assertDoesNotThrow(() -> this.service.receiverCallUpdate(UUID.randomUUID().toString(), "11987654321", callStatus, request, "d7f767dcc32d8cebf23ad09e797795ee"));
    }


    static UpdateCallDataRequest updateCallDataRequest() {
        return UpdateCallDataRequest.builder().receiverNumber("11987654321")
                .callType(CallType.VOICE)
                .callStatus(CallStatus.COMPLETED)
                .carrier("Vivo")
                .receiverLatitude(-23.5558)
                .receiverLongitude(-46.6396)
                .receiverDeviceModel("iPhone 12")
                .receiverDeviceImei("987654321012345")
                .receiverNetworkType("5G")
                .receiverAreaCode("21").build();
    }

    static CreateCallDataRequest createCallDataRequest() {
        return CreateCallDataRequest
                .builder()
                .callerNumber("11987654321")
                .receiverNumber("11987654322")
                .callType(CallType.VOICE)
                .callStatus(CallStatus.COMPLETED)
                .carrier("Vivo")
                .callerLatitude(-23.5558)
                .callerLongitude(-46.6396)
                .callerDeviceModel("Samsung Galaxy S21")
                .callerDeviceImei("123456789012345")
                .callerNetworkType("4G")
                .callerAreaCode("11")
                .build();
    }


}
