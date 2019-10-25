package com.skt.classic.web.template.common;

import org.springframework.data.domain.Pageable;

public abstract class Dialect {

    public abstract String getPagingSQL(String sql, Pageable pageable);

    public String getCountSQL(final String sql) {
        return String.format("select count(1) from ( %s ) tmp", sql);
    }
}
