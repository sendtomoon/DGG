package com.sendtomoon.dgg.server.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.filter.DelegatingFilterProxy;

import net.sf.ehcache.CacheManager;

@Configuration
public class ShiroBean {

	@Bean(name = "shiroRealm")
	public AuthorizingRealm getShiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setCachingEnabled(false);
		return shiroRealm;
	}

	@Bean(name = "sessionManager")
	public DefaultWebSessionManager getSessionManager() {
		DefaultWebSessionManager dwsm = new DefaultWebSessionManager();
		dwsm.setSessionDAO(this.getSessionDAO());
		dwsm.setGlobalSessionTimeout(1800000);
		dwsm.setDeleteInvalidSessions(true);
		dwsm.setSessionValidationSchedulerEnabled(true);
		dwsm.setSessionValidationInterval(18000000);
		dwsm.setSessionIdCookieEnabled(true);
		dwsm.setSessionIdCookie(this.getSessionIdCookie());
		return dwsm;
	}

	@Bean(name = "sessionIdCookie")
	public Cookie getSessionIdCookie() {
		SimpleCookie sc = new SimpleCookie();
		sc.setName("DGGSession");
		sc.setHttpOnly(true);
		sc.setMaxAge(-1);
		return sc;
	}

	@Bean(name = "sessionDAO")
	public SessionDAO getSessionDAO() {
		EnterpriseCacheSessionDAO ecdao = new EnterpriseCacheSessionDAO();
		ecdao.setActiveSessionsCacheName("shiro-activeSessionCache");
		ecdao.setSessionIdGenerator(this.getSessionIdGenerator());
		return ecdao;
	}

	@Bean(name = "sessionIdGenerator")
	public SessionIdGenerator getSessionIdGenerator() {
		return new DGGSessionID();
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(this.getShiroRealm());
		securityManager.setSessionManager(this.getSessionManager());
		securityManager.setCacheManager(this.getCacheManager());
		return securityManager;
	}

	@Bean(name = "cacheManager")
	public org.apache.shiro.cache.CacheManager getCacheManager() {
		SpringCacheManagerWrapper wapper = new SpringCacheManagerWrapper();
		wapper.setCacheManager(this.getSpringCacheManager());
		return wapper;
	}

	@Bean(name = "springCacheManager")
	public org.springframework.cache.CacheManager getSpringCacheManager() {
		EhCacheCacheManager cm = new EhCacheCacheManager();
		cm.setCacheManager(this.getEhcacheManager());
		return cm;
	}

	@Bean(name = "ehcacheManager")
	public CacheManager getEhcacheManager() {
		Resource resource = new ClassPathResource("classpath:/ehcache.xml");
		EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
		bean.setConfigLocation(resource);
		return bean.getObject();
	}

	@Bean(name = "userLoginOnlyOneFilter")
	public UserLoginOnlyOneFilter getUserLoginOnlyOneFilter() {
		UserLoginOnlyOneFilter filter = new UserLoginOnlyOneFilter();
		filter.setCacheManager(this.getCacheManager());
		filter.setSessionManager(this.getSessionManager());
		filter.setMaxUser(1);
		filter.setLoginUrl("/login");
		return filter;
	}

	private Map<String, String> setinitmap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("targetFilterLifecycle", "true");
		return map;
	}

	private List<String> url() {
		List<String> list = new ArrayList<String>();
		list.add("/*");
		return list;
	}

	@DependsOn("lifecycleBeanPostProcessor")
	public void fff() {
		DefaultAdvisorAutoProxyCreator cre = new DefaultAdvisorAutoProxyCreator();
		cre.setProxyTargetClass(true);
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		LifecycleBeanPostProcessor lip = new LifecycleBeanPostProcessor();
		return lip;
	}

	@Bean
	public FilterRegistrationBean shiroFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setName("shiroFilter");
		registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
		registration.setUrlPatterns(this.url());
		registration.setFilter(new DelegatingFilterProxy());
		registration.setInitParameters(this.setinitmap());
		return registration;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean sfb = new ShiroFilterFactoryBean();
		sfb.setSecurityManager(this.getSecurityManager());
		sfb.setLoginUrl("/login");
		sfb.setSuccessUrl("/index");
		sfb.setUnauthorizedUrl("/login");
		sfb.setFilters(this.getFitlers());
		sfb.setFilterChainDefinitionMap(this.getFilterChainDefinitionMap());
		return sfb;
	}

	private Map<String, Filter> getFitlers() {
		Map<String, Filter> map = new HashMap<String, Filter>();
		map.put("loginCountCheck", this.getUserLoginOnlyOneFilter());
		return map;
	}

	private Map<String, String> getFilterChainDefinitionMap() {
		Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
		filterChainDefinitionManager.put("/", "anon");
		filterChainDefinitionManager.put("/index", "anon");
		filterChainDefinitionManager.put("/static/**", "anon");
		filterChainDefinitionManager.put("/druid/**", "anon");
		filterChainDefinitionManager.put("/**", "loginCountCheck,authc");
		return filterChainDefinitionManager;
	}
}
