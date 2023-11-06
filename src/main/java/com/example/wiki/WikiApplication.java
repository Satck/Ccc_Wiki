package com.example.wiki;

import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@ComponentScan({"com.*"})
public class WikiApplication {

    private static  final  Logger LOG  = LoggerFactory.getLogger(WikiApplication.class);
    public static void main(String[] args) {
//         启动日志优化
        SpringApplication app = new SpringApplication(WikiApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功");
        LOG.info("地址: \thttp:127.0.0.1:{}",env.getProperty("server.port"));
    }
}
