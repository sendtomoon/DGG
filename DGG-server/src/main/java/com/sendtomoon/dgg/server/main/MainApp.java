package com.sendtomoon.dgg.server.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.sendtomoon.dgg.server")
@MapperScan(basePackages="com.sendtomoon.dgg.server.dao")
@EnableAutoConfiguration
public class MainApp {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApp.class, args);
	}
}
