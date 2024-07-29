package com.renemtech.calldataservice.model.dto;


import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.enuns.CallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallDataDetailsResponse {

    private UUID callId;
    private String callerNumber;
    private String receiveNumber;
    private Date callDhStart;
    private Date callDhEnd;
    private String callDuration;
    private CallType callType;
    private CallStatus callStatus;
    private String carrier;
    private List<CallerDataDetailsResponse> details;

}
