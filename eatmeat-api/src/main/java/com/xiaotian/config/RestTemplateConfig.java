package com.xiaotian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiaotian
 * @description
 * @date 2022-01-23 12:03
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
