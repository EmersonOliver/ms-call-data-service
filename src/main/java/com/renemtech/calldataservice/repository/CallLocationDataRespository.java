package com.renemtech.calldataservice.repository;

import com.renemtech.calldataservice.model.CallerCallEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CallLocationDataRespository implements PanacheRepositoryBase<CallerCallEntity, Long> {

    public Optional<CallerCallEntity> findLocationByDataCall(UUID callId) {
        return this.find("callId =: callId ", Parameters.with("callId", callId)).stream().findFirst();
    }

}
