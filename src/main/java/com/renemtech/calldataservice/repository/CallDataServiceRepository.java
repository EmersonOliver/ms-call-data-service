package com.renemtech.calldataservice.repository;

import com.renemtech.calldataservice.model.ReceiverCallEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CallDataServiceRepository implements PanacheRepositoryBase<ReceiverCallEntity, UUID> {


}
