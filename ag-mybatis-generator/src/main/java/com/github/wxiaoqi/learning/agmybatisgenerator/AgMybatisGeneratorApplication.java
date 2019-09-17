package com.github.wxiaoqi.learning.agmybatisgenerator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.github.wxiaoqi.learning.agmybatisgenerator.mapper")
@EnableFeignClients
public class AgMybatisGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgMybatisGeneratorApplication.class, args);
	}

}
