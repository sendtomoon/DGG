package com.sendtomoon.dgg.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.sendtomoon.dgg.server.*")
public class MainApp {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApp.class, args);
	}
}
