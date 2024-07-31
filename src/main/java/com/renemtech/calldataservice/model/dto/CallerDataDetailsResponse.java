package com.renemtech.calldataservice.model.dto;

import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.enuns.CallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallerDataDetailsResponse {

    private Long callerLocation;
    private UUID callId;
    private Double callerLatitude;
    private Double callerLongitude;
    private String callerDeviceModel;
    private String callerDeviceImei;
    private String callerAreaCode;
    private Date callDhStart;
    private Date callDhEnd;
    private String callDuration;
    private CallType callType;
    private CallStatus callStatus;

}