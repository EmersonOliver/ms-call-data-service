package com.renemtech.calldataservice.rabbitmq.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import java.text.MessageFormat;

@ApplicationScoped
public class ErrorsProducerLog {

    @Inject
    Logger log;

    @Inject
    @Channel("topic-erros-call")
    Emitter<String> emitter;

    public void sendLogsErros(String message) {
        log.warn(MessageFormat.format("Send message {0} erros to topic-erros.", message));
        emitter.send(message);
    }

}
