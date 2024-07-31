package com.renemtech.calldataservice.rabbitmq.message;


import com.renemtech.calldataservice.model.dto.CallDataDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quarantine {
    private CallDataDetailsResponse callQuarantine;
}
