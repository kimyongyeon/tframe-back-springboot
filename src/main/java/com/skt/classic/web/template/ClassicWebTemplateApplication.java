package com.skt.classic.web.template;

import com.skt.classic.web.template.config.StorageProperties;
import com.skt.classic.web.template.service.storage.StorageService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * security 사용이 필요할 경우 exclude 속성을 제거하시오.
 * security 사용할려면 config 설정 파일도 반드시 만들어야 한다.
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@MapperScan(basePackages = "com.skt.classic.web.template.mapper")
@EnableConfigurationProperties(StorageProperties.class)
public class ClassicWebTemplateApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        System.setProperty("spring.devtools.livereload.enabled", "true");
        SpringApplication.run(ClassicWebTemplateApplication.class, args);
    }

    /**
     * @description init/파일저장 관련 초기화를 위한 메서드 입니다.
     * @param storageService
     * @return
     */
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }


}
