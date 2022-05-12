package com.luhaoge.demoeurekaauth_server;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class DemoEurekaAuthServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoEurekaAuthServerApplication.class, args);
    }

}
