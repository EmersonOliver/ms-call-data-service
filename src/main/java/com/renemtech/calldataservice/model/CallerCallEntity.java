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
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "set_caller_call")
@SequenceGenerator(name = "sq_id_caller_location", sequenceName = "seq_caller_location", initialValue = 1, allocationSize = 1)
public class CallerCallEntity {

    @Id
    @GeneratedValue(generator = "sq_id_caller_location", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_caller_location")
    private Long callerLocation;

    @Column(name = "caller_number")
    private String callerNumber;

    @Column(name = "fk_call_id")
    private UUID callId;

    @Column(name = "hash_call")
    private String hashCall;

    @Column(name = "caller_latitude")
    private Double callerLatitude;

    @Column(name = "caller_longitude")
    private Double callerLongitude;

    @Column(name = "caller_device_model")
    private String callerDeviceModel;

    @Column(name = "caller_device_imei")
    private String callerDeviceImei;

    @Column(name = "caller_area_code")
    private String callerAreaCode;

    @Column(name = "call_type")
    private CallType callType;

    @Column(name = "call_status")
    private CallStatus callStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "call_dh_start")
    private Date callDhStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "call_dh_end")
    private Date callDhEnd;

    @Column(name = "call_duration")
    private String callDuration;

    @JsonIgnoreProperties("callerLocations")
    @ManyToOne
    @JoinColumn(name = "fk_call_id", referencedColumnName = "call_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_call_id_call_data"))
    private ReceiverCallEntity callData;
}
