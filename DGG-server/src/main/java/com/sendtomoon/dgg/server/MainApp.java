package com.sendtomoon.dgg.server;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.sendtomoon.dgg.server.shiro.ShiroRealm;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages = "com.sendtomoon.dgg.server.dao")
@EnableAutoConfiguration
@EnableSwagger2
public class MainApp extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MainApp.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApp.class, args);
	}

	@Bean
	public AuthorizingRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setCachingEnabled(false);
		return shiroRealm;
	}
	
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager dwsm = new DefaultWebSessionManager();
		dwsm.setSessionDAO(sessionDAO);
		dwsm.setGlobalSessionTimeout(1800000);
		dwsm.setDeleteInvalidSessions(true);
		dwsm.setSessionValidationSchedulerEnabled(true);
		dwsm.setSessionValidationInterval(18000000);
		dwsm.setSessionIdCookieEnabled(true);
		dwsm.setSessionIdCookie(sessionIdCookie);
		return dwsm;
	}

	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(this.shiroRealm());
		

	}
}
