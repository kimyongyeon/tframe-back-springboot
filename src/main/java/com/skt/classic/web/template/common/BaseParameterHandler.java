package com.skt.classic.web.template.common;

import org.springframework.data.domain.Pageable;

public class BaseParameterHandler extends PageableParameterHandler<Object> {

    public BaseParameterHandler(final Object parameter) {
        super(parameter);
    }

    @Override
    public Object getOriginParameter() {
        if(parameter instanceof PageableParameter) {
            return ((PageableParameter)parameter).getParameter();
        }

        return null;
    }

    @Override
    public Pageable getPageable() {
        if(parameter instanceof Pageable) {
            return (Pageable)parameter;
        }

        if(parameter instanceof PageableParameter) {
            return ((PageableParameter)parameter).getPageable();
        }

        return null;
    }

}