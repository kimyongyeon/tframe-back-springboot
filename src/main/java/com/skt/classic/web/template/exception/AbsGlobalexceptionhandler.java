package com.skt.classic.web.template.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public abstract class AbsGlobalexceptionhandler {

    @ExceptionHandler(Exception.class)
    public String customerException(Exception e) {
        log.error(this.getClass().getName(), e);
        return "500";
    }

    @ExceptionHandler(RuntimeException.class)
    public String customerRunException(RuntimeException e, Model model) {
        log.error(this.getClass().getName(), e);
        model.addAttribute("className", this.getClass().getName());
        model.addAttribute("msg", e.getMessage());
        return "500";
    }

    @ExceptionHandler(DataAccessException.class)
    public String customerDataAccessException(DataAccessException e) {
        log.error(this.getClass().getName(), e);
        return "500";
    }

}
