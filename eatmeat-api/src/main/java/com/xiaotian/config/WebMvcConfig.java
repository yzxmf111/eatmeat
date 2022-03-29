package com.xiaotian.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: Tian
 * @time: 2022/3/19 22:44
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").
                addResourceLocations("file:/Users/xiaotian/Downloads/items/userface/") //最后的'/'不要忘记了
                .addResourceLocations("classpath:/META-INF/resources/"); //swagger2默认的映射位置
    }
}
