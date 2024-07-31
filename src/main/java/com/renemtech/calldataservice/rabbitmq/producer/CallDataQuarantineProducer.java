package com.renemtech.calldataservice.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renemtech.calldataservice.rabbitmq.message.Quarantine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CallDataQuarantineProducer {

    @Inject
    Logger log;

    @Inject
    @Channel("topic-call-quarantine")
    Emitter<Quarantine> quarantineEmitter;

    public void sendMessage(Quarantine message)  {
        log.info("Send message to topic-call-quarantine");
        quarantineEmitter.send(message);
    }

}
