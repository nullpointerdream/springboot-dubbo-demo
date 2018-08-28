package com.y2game.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
/*@EnableDubboConfiguration*/
@ServletComponentScan
@ImportResource("classpath:dubbo.xml")
public class DubboWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboWebApplication.class, args);
	}
}
