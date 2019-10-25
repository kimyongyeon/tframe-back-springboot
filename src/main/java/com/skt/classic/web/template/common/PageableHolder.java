package com.skt.classic.web.template.common;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public final class PageableHolder {

    private static ThreadLocal<Integer> TOTAL_COUNT = new ThreadLocal<>();

    protected static void setCount(final int count) {
        TOTAL_COUNT.set(count);
    }

    public static <T> Page<T> getPage(final List<T> content, final Pageable pageable){
        return new PageImpl<>(content, pageable, TOTAL_COUNT.get());
    }

    public static <T> Page<T> getPage(final Pageable pageable, final Function<Pageable, List<T>> stmt){
        if(stmt == null) {
            throw new IllegalArgumentException("Mybatis Pageable SQL 호출함수가 존재하지 않습니다.");
        }

        List<T> list = stmt.apply(pageable);
        return new PageImpl<>(list, pageable, TOTAL_COUNT.get());
    }
}