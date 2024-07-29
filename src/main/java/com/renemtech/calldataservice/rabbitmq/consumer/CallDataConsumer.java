package com.renemtech.calldataservice.rabbitmq.consumer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class CallDataConsumer {


    @Incoming("queue-call-data-service")
    public void consumer(byte[] payload) {
        System.out.println("Payload --> " + new String(payload));
    }
}

