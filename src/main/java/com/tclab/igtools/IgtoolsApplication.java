package com.tclab.igtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class IgtoolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgtoolsApplication.class, args);
	}

}
