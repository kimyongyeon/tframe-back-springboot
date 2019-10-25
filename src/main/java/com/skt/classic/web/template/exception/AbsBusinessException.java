package com.skt.classic.web.template.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class AbsBusinessException extends RuntimeException {
    public AbsBusinessException(Throwable throwable) {
        log.error(this.getClass().getName(), throwable);
    }
    public AbsBusinessException(String code, Throwable throwable) {
        log.error(this.getClass().getName(), code);
        log.error(this.getClass().getName(), throwable);
    }
    public AbsBusinessException(String code, String msg) {
        log.error(this.getClass().getName(), code);
        log.error(this.getClass().getName(), msg);
    }

    public AbsBusinessException() {
    }
}
