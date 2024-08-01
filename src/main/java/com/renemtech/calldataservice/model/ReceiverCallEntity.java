package com.renemtech.calldataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.renemtech.calldataservice.enuns.CallStatus;
import com.renemtech.calldataservice.enuns.CallType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "set_receiver_call")
public class ReceiverCallEntity {

    @Id
    @Column(name = "call_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID callId;

    @Column(name = "receive_number")
    private String receiveNumber;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "receiver_area_code")
    private String receiverAreaCode;

    @Column(name = "receiver_latitude")
    private Double receiverLatitude;

    @Column(name = "receiver_longitude")
    private Double receiverLongitude;

    @Column(name = "receiver_device_model")
    private String receiverDeviceModel;

    @Column(name = "receiver_device_imei")
    private String receiverDeviceImei;

    @JsonIgnoreProperties("callData")
    @OneToMany(mappedBy = "callData", fetch = FetchType.EAGER)
    private List<CallerCallEntity> callerLocations;

}
