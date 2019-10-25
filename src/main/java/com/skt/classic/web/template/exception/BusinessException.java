package com.skt.classic.web.template.exception;

import org.springframework.stereotype.Component;

@Component
public class BusinessException extends AbsBusinessException {
    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public BusinessException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public BusinessException(String code, String msg) {
        super(code, msg);
    }

    public BusinessException() {
    }
}
