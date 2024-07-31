package com.renemtech.calldataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caller_location")
@SequenceGenerator(name = "sq_id_caller_location", sequenceName = "seq_caller_location", initialValue = 1, allocationSize = 1)
public class CallerLocationEntity {

    @Id
    @GeneratedValue(generator = "sq_id_caller_location", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_caller_location")
    private Long callerLocation;

    @Column(name = "fk_call_id")
    private UUID callId;

    @Column(name = "caller_latitude")
    private Double callerLatitude;

    @Column(name = "caller_longitude")
    private Double callerLongitude;

    @Column(name = "receiver_latitude")
    private Double receiverLatitude;

    @Column(name = "receiver_longitude")
    private Double receiverLongitude;

    @Column(name = "caller_device_model")
    private String callerDeviceModel;

    @Column(name = "caller_device_imei")
    private String callerDeviceImei;

    @Column(name = "receiver_device_model")
    private String receiverDeviceModel;

    @Column(name = "receiver_device_imei")
    private String receiverDeviceImei;

    @Column(name = "receiver_network_type")
    private String receiverNetworkType;

    @Column(name = "caller_area_code")
    private String callerAreaCode;

    @Column(name = "receiver_area_code")
    private String receiverAreaCode;


    @JsonIgnoreProperties("callerLocations")
    @ManyToOne
    @JoinColumn(name = "fk_call_id", referencedColumnName = "call_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_call_id_call_data"))
    private CallDataEntity callData;
}
