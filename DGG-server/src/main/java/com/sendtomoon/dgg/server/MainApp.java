package com.sendtomoon.dgg.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.sendtomoon.dgg.server.dao")
@EnableAutoConfiguration
public class MainApp {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApp.class, args);
	}
}
