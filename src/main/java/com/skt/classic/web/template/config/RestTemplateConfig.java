package com.skt.classic.web.template.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder
                /** rootUri가 필요할시 아래 속성을 지정해 주세요. */
//                .rootUri(prop.getBpcp().getTmembershipUrl())	// base url 설정
                /** 소켓 connection 타임아웃 3초 정의 */
                .setConnectTimeout(Duration.ofSeconds(3))
                /** 소켓 read/write 타임아웃 3초 정의 */
                .setReadTimeout(Duration.ofSeconds(3))
                /** RestClient 로깅이 필요시 interceptor를 만들어 주입해 주세요. */
//                .additionalInterceptors(new RestClientLoggingInterceptor()) // 로깅을 위한 인터셉터 추가
                .build();
    }
}
