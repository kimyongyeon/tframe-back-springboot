package com.skt.classic.web.template.common;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.springframework.data.domain.Pageable;

public class MapperParameterHandler extends PageableParameterHandler<ParamMap<?>> {

    public MapperParameterHandler(final ParamMap<?> parameter) {
        super(parameter);
    }

    @Override
    public Object getOriginParameter() {
        Set<String> keySet = parameter.keySet();
        List<String> paramKeys = keySet.stream()
                .filter(key -> key.startsWith("param"))
                .filter(key -> !(parameter.get(key) instanceof Pageable))
                .sorted()
                .collect(Collectors.toList());

        List<String> argKeys = keySet.stream()
                .filter(key -> key.startsWith("arg"))
                .filter(key -> !(parameter.get(key) instanceof Pageable))
                .sorted()
                .collect(Collectors.toList());

        int size = paramKeys.size();
        if(size == 0) {
            return null;
        }
        if(size == 1) {
            String key = paramKeys.get(0);
            return parameter.get(key);
        }

        if(size != argKeys.size()) {
            throw new IllegalArgumentException("param and arg must be same count!");
        }

        ParamMap<Object> originMap = new ParamMap<>();
        for(int i=0; i<size; i++) {
            String paramKey = "param" + (i+1);
            Object paramValue = parameter.get(paramKeys.get(i));
            originMap.put(paramKey, paramValue);

            String argKey = "arg" + (i+1);
            Object argValue = parameter.get(argKeys.get(i));
            originMap.put(argKey, argValue);
        }


        return originMap;
    }

    @Override
    public Pageable getPageable() {
        return (Pageable) parameter
                .values()
                .stream()
                .filter(v -> v instanceof Pageable)
                .findFirst()
                .get();
    }

}

