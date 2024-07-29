package com.renemtech.calldataservice.model;

import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.enuns.CallType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "call_data")
public class CallDataEntity {

    @Id
    @Column(name = "call_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID callId;

    @Column(name = "caller_number")
    private String callerNumber;

    @Column(name = "receive_number")
    private String receiveNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "call_dh_start")
    private Date callDhStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "call_dh_end")
    private Date callDhEnd;

    @Column(name = "call_duration")
    private String callDuration;

    @Column(name = "call_type")
    private CallType callType;

    @Column(name = "call_status")
    private CallStatus callStatus;

    @Column(name = "carrier")
    private String carrier;


}
