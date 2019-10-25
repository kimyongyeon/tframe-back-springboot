package com.skt.classic.web.template.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Component
@ControllerAdvice
public class GlobalExceptionhandler extends AbsGlobalexceptionhandler {
    @Override
    public String customerException(Exception e) {
        return super.customerException(e);
    }

    @Override
    public String customerRunException(RuntimeException e, Model model) {
        return super.customerRunException(e, model);
    }

    @Override
    public String customerDataAccessException(DataAccessException e) {
        return super.customerDataAccessException(e);
    }
}
