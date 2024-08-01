package com.renemtech.calldataservice.rabbitmq.message;


import com.renemtech.calldataservice.model.dto.CallerDataDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quarantine {
    private CallerDataDetailsResponse callQuarantine;
}
