//package com.jiawa.wiki.config;
//
//
//import com.jiawa.wiki.interceptor.LogInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Autowired
//    LogInterceptor logInterceptor;
//
//    //拦截器先结束 过滤器才结束
//    public void addInterceptor(InterceptorRegistry registry){
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/login");
//    }
//}
