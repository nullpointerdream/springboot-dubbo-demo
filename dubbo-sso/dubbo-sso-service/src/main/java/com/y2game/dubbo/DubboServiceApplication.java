package com.y2game.dubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.y2game.dubbo.dao")
@ImportResource("classpath:dubbo.xml")
// @EnableDubboConfiguration//then add @EnableDubboConfiguration on Spring Boot Application, indicates that dubbo is enabled.(web or non-web application can use dubbo provider)
public class DubboServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboServiceApplication.class, args);
	}
}
