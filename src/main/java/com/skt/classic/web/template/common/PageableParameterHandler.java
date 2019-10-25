package com.skt.classic.web.template.common;

import org.springframework.data.domain.Pageable;

public abstract class PageableParameterHandler<T> {

    protected T parameter;

    public PageableParameterHandler(final T parameter) {
        this.parameter = parameter;
    }

    public abstract Object getOriginParameter();

    public abstract Pageable getPageable();
}
