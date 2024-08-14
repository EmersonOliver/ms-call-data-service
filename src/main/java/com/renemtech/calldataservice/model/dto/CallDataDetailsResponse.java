package com.renemtech.calldataservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallDataDetailsResponse {

    private UUID callId;
    private String receiveNumber;
    private String carrier;
    private List<CallerDataDetailsResponse> details;

}
