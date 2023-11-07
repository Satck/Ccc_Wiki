package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TestController {

    // 读取properties中的自定义配置 test.hello 并且将他赋值给test，如果没有读取到properties中的属性 就默认为 冒号后面的 （优先读取properties中的选项）；
    @Value("${test.hello:TEST}")
    private  String test ;

    @Autowired
    private TestService testService;

    /**
     * 常见的请求方式 GET PUT POST DELETE
     * @return
     */

    @RequestMapping("/hello")
    // 如果只是简单的用@RequestMapping注解 表示这个接口支持所有的请求方式
    // 如果要制定某种方式的话 eg get请求 那么就用GetMapping（）
    // PostMapping 不可以使用浏览器来测试
    public String hello(){
        return "Hello World" +test;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello world postMapping : " + name ;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }
}
