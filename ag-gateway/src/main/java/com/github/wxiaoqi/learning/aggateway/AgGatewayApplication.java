package com.github.wxiaoqi.learning.aggateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AgGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgGatewayApplication.class, args);
	}

}
