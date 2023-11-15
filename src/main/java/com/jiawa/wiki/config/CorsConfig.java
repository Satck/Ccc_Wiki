package com.jiawa.wiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
                // 针对所有的映射地址 接口
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedMethods(CorsConfiguration.ALL)
                .allowCredentials(true) // 允许前端带上它的凭证信息 比如cookie信息
                .maxAge(3600); // 1小时内不需要再预检（发OPTIONS请求，option请求不处理逻辑问题 只是检查接口是否存在 要是存在的话 就会发送get请求，成功一次后 会一个小时后再发// ）
    }

}
