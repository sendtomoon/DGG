package com.sendtomoon.dgg.server.conf;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

public class DruidConfig {
	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource.druid.ipinfo")
	public DataSource dataSourceOne() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.druid.two")
	public DataSource dataSourceTwo(){
	    return DruidDataSourceBuilder.create().build();
	}
}
