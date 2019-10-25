package com.skt.classic.web.template.common;

import org.springframework.data.domain.Pageable;

public class PageableParameter {
    private Pageable pageable;
    private Object parameter;

    public PageableParameter(final Pageable pageable) {
        this(pageable, null);
    }

    public PageableParameter(final Pageable pageable, final Object parameter) {
        this.pageable = pageable;
        this.parameter = parameter;
    }

    public Pageable getPageable() {
        return pageable;
    }
    public void setPageable(final Pageable pageable) {
        this.pageable = pageable;
    }
    public Object getParameter() {
        return parameter;
    }
    public void setParameter(final Object parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "PageableParameter [pageable=" + pageable + ", parameter=" + parameter + "]";
    }

}

