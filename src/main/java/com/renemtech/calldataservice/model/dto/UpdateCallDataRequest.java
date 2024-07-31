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
public class UpdateCallDataRequest {

    private String receiverNumber;
    private CallType callType;
    private CallStatus callStatus;
    private String carrier;
    private Double receiverLatitude;
    private Double receiverLongitude;
    private String receiverDeviceModel;
    private String receiverDeviceImei;
    private String receiverNetworkType;
    private String receiverAreaCode;

}
