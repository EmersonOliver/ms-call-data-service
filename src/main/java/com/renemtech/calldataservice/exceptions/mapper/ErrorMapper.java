package com.renemtech.calldataservice.exceptions.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMapper {

    private String message;
    private String status;
    private List<String> details = new ArrayList<>();

}
