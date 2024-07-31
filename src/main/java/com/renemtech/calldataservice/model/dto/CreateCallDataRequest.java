package com.renemtech.calldataservice.model.dto;

import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.enuns.CallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCallDataRequest {


    private String callerNumber;
    private String receiverNumber;
    private CallType callType;
    private CallStatus callStatus;
    private String carrier;
    private Double callerLatitude;
    private Double callerLongitude;
    private String callerDeviceModel;
    private String callerDeviceImei;
    private String callerNetworkType;
    private String callerAreaCode;

}
