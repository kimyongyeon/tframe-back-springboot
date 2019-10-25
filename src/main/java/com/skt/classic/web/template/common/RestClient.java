package com.skt.classic.web.template.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public abstract class RestClient extends RestTemplate {

    private String baseHttpUrl;

    @Autowired
    protected RestTemplate restTemplate;

    public String getBaseHttpUrl() {
        return baseHttpUrl;
    }

    public void setBaseHttpUrl(final String baseHttpUrl) {
        this.baseHttpUrl = baseHttpUrl;
    }

    /** GET 호출 wrapper method */
    public <T> T getForObject(final String path, final Class<T> responseType, final Object... uriVariables)
            throws RestClientException {
        return restTemplate.getForObject(baseHttpUrl + path, responseType, uriVariables);
    }

    /** POST 호출 wrapper method */
    public <T> T postForObject(final String path, @Nullable final Object request, final Class<T> responseType,
                               final Object... uriVariables) throws RestClientException {
        return restTemplate.postForObject(baseHttpUrl + path, request, responseType, uriVariables);
    }

    /** PUT 호출 wrapper method */
    public <T> T putForObject(final String path, @Nullable final Object request, final Class<T> responseType,
                              final Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<>(request);
        ResponseEntity<T> response = restTemplate.exchange(baseHttpUrl + path, HttpMethod.PUT, requestEntity, responseType,
                uriVariables);
        return response.getBody();
    }

    /** DELETE 호출 wrapper method */
    public <T> T deleteForObject(final String path, @Nullable final Object request, final Class<T> responseType,
                                 final Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<>(request);
        ResponseEntity<T> response = restTemplate.exchange(baseHttpUrl + path, HttpMethod.DELETE, requestEntity, responseType,
                uriVariables);
        return response.getBody();
    }

    /**
     * @deprecated putForObject 를 이용해주세요.
     */
    @Deprecated
    public void put(final String path, final Object request, final Object... uriVariables) throws RestClientException {
        restTemplate.put(baseHttpUrl + path, request, uriVariables);
    }

    /**
     * @deprecated deleteForObject 를 이용해주세요.
     */
    @Deprecated
    public void delete(final String path, final Object... uriVariables) throws RestClientException {
        restTemplate.delete(baseHttpUrl + path, uriVariables);
    }
}
