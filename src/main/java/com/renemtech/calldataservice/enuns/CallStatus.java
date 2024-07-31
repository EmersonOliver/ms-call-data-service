package com.renemtech.calldataservice.enuns;

import com.renemtech.calldataservice.model.CallerCallEntity;

import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum CallStatus {
    COMPLETED {
        //Chamada completada com sucesso.
        @Override
        public void build(CallerCallEntity call) {
            call.setCallDhEnd(new Date());
            long callTime = call.getCallDhEnd().getTime() - call.getCallDhStart().getTime();
            long durationCall = TimeUnit.MILLISECONDS.toMinutes(callTime);
            call.setCallDuration(MessageFormat.format("{0} Minutes", durationCall));
        }
    },
    MISSED {
        //Chamada perdida.
        @Override
        public void build(CallerCallEntity call) {
            Date callTimestamp = new Date();
            call.setCallDhStart(callTimestamp);
            call.setCallDhEnd(callTimestamp);
        }
    },
    REJECTED {
        //Chamada rejeitada pelo destinatário.
        @Override
        public void build(CallerCallEntity call) {
            call.setCallDhEnd(new Date());
            long callTime = call.getCallDhEnd().getTime() - call.getCallDhStart().getTime();
            long durationCall = TimeUnit.MILLISECONDS.toHours(callTime);
            call.setCallDuration(MessageFormat.format("{0} Minutes", durationCall));
        }
    },
    BUSY {
        //Destinatário estava ocupado.
        @Override
        public void build(CallerCallEntity call) {
            Date callTimestamp = new Date();
            call.setCallDhStart(callTimestamp);
            call.setCallDhEnd(callTimestamp);
        }
    },
    FAILED {
        //Falha ao tentar completar a chamada.
        @Override
        public void build(CallerCallEntity call) {
            Date callTimestamp = new Date();
            call.setCallDhStart(callTimestamp);
            call.setCallDhEnd(callTimestamp);
        }
    },
    IN_PROGRESS {
        //Chamada em andamento.
        @Override
        public void build(CallerCallEntity call) {
            Date callTimestamp = new Date();
            call.setCallDhStart(callTimestamp);
        }
    },
    ON_HOLD {
        //Chamada em espera.
        @Override
        public void build(CallerCallEntity call) {
            return;
        }
    },
    TRANSFERRED {
        //Chamada transferida para outro número.
        @Override
        public void build(CallerCallEntity call) {
            return;
        }
    },
    CANCELLED {
        //Chamada cancelada
        @Override
        public void build(CallerCallEntity call) {
            Date callTimestamp = new Date();
            call.setCallDhStart(callTimestamp);
            call.setCallDhEnd(callTimestamp);
        }
    },
    ACCEPTED {
        //Chamada Aceita.
        @Override
        public void build(CallerCallEntity call) {
            call.setCallDhStart(new Date());
        }
    };

    public abstract void build(CallerCallEntity call);

}
