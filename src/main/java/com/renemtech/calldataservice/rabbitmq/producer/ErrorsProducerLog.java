package com.renemtech.calldataservice.rabbitmq.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.text.MessageFormat;

@Slf4j
@ApplicationScoped
public class ErrorsProducerLog {

    @Inject
    @Channel("topic-erros-call")
    Emitter<String> emitter;

    public void sendLogsErros(String message) {
        log.warn(MessageFormat.format("Send message {0} erros to topic-erros.", message));
        emitter.send(message);
    }

}
