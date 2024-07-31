package com.renemtech.calldataservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private Double receiverLatitude;
    private Double receiverLongitude;
    private String callerDeviceModel;
    private String callerDeviceImei;
    private String receiverDeviceModel;
    private String receiverDeviceImei;
    private String receiverNetworkType;
    private String callerAreaCode;
    private String receiverAreaCode;

}