package com.renemtech.calldataservice.enuns;

import com.renemtech.calldataservice.model.CallerCallEntity;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum CallStatus {
    COMPLETED {
        //Chamada completada com sucesso.
        @Override
        public void build(CallerCallEntity call) {
            call.setCallDhEnd(new Date());

            LocalDateTime dhStart = call.getCallDhStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime dhEnd = call.getCallDhEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            boolean isMoreThan1Hour = isCallDurationMoreThan(dhStart, dhEnd, Duration.ofHours(1));
            boolean isMoreThan30Minutes = isCallDurationMoreThan(dhStart, dhEnd, Duration.ofMinutes(1));
            boolean isMoreThan1Second = isCallDurationMoreThan(dhStart, dhEnd, Duration.ofSeconds(1));

            long callTime = call.getCallDhEnd().getTime() - call.getCallDhStart().getTime();
            long durationCall;
            if (isMoreThan1Hour) {
                durationCall = TimeUnit.MILLISECONDS.toHours(callTime);
                call.setCallDuration(MessageFormat.format("{0} Hours", durationCall));
            } else if (isMoreThan30Minutes) {
                durationCall = TimeUnit.MILLISECONDS.toMinutes(callTime);
                call.setCallDuration(MessageFormat.format("{0} Minutes", durationCall));
            } else if (isMoreThan1Second) {
                durationCall = TimeUnit.MILLISECONDS.toSeconds(callTime);
                call.setCallDuration(MessageFormat.format("{0} Seconds", durationCall));
            }


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

            LocalDateTime dhStart = call.getCallDhStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime dhEnd = call.getCallDhEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            boolean isMoreThan1Hour = isCallDurationMoreThan(dhStart, dhEnd, Duration.ofHours(1));
            boolean isMoreThan30Minutes = isCallDurationMoreThan(dhStart, dhEnd, Duration.ofMinutes(1));
            boolean isMoreThan1Second = isCallDurationMoreThan(dhStart, dhEnd, Duration.ofSeconds(1));

            long callTime = call.getCallDhEnd().getTime() - call.getCallDhStart().getTime();
            long durationCall;
            if (isMoreThan1Hour) {
                durationCall = TimeUnit.MILLISECONDS.toHours(callTime);
                call.setCallDuration(MessageFormat.format("{0} Hours", durationCall));
            } else if (isMoreThan30Minutes) {
                durationCall = TimeUnit.MILLISECONDS.toMinutes(callTime);
                call.setCallDuration(MessageFormat.format("{0} Minutes", durationCall));
            } else if (isMoreThan1Second) {
                durationCall = TimeUnit.MILLISECONDS.toSeconds(callTime);
                call.setCallDuration(MessageFormat.format("{0} Seconds", durationCall));
            }

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

    public boolean isCallDurationMoreThan(LocalDateTime start, LocalDateTime end, Duration duration) {
        Duration callDuration = Duration.between(start, end);
        return callDuration.compareTo(duration) > 0;
    }

    public abstract void build(CallerCallEntity call);

}
