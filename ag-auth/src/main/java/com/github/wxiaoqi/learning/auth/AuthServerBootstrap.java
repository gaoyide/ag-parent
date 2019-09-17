package com.github.wxiaoqi.learning.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ace on 2017/9/6.
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.github.wxiaoqi.learning.auth")
@MapperScan("com.github.wxiaoqi.learning.auth.mapper")
public class AuthServerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerBootstrap.class,args);
    }
}