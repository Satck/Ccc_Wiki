package com.example.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    /**
     * 常见的请求方式 GET PUT POST DELETE
     * @return
     */

    @RequestMapping("/hello")
    // 如果只是简单的用@RequestMapping注解 表示这个接口支持所有的请求方式
    // 如果要制定某种方式的话 eg get请求 那么就用GetMapping（）
    public String hello(){
        return "Hello World";
    }
}
