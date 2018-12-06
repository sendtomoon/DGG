package com.sendtomoon.dgg.server.conf;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DruidConfig {

	static final String MAPPER_LOCATION = "classpath:mapper/**/*Mapper.xml"; // 扫描的 xml 目录
	static final String CONFIG_LOCATION = "classpath:mybatis/mybatis-config-ipinfo.xml"; // 自定义的mybatis config 文件位置
	static final String TYPE_ALIASES_PACKAGE = "com.sendtomoon.dgg.server.dto"; // 扫描的 实体类 目录

	@Value("${spring.datasource.ipinfo.url}")
	private String ipinfoURL;

	@Value("${spring.datasource.ipinfo.username}")
	private String ipinfousername;

	@Value("${spring.datasource.ipinfo.password}")
	private String ipinfopassword;

	@Value("${spring.datasource.ipinfo.driverClassName}")
	private String ipinfodriverClassName;

	@Primary
	@Bean("ipinfoDataSource")
	public DataSource dataSourceOne() {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(ipinfoURL);
		datasource.setDriverClassName("org.mariadb.jdbc.Driver");
		datasource.setUsername(ipinfousername);
		datasource.setPassword(ipinfopassword);
		return datasource;
	}

	@Bean(name = "ipinfoTransactionManager")
	@Primary
	public DataSourceTransactionManager masterTransactionManager() {
		return new DataSourceTransactionManager(dataSourceOne());
	}

	@Bean(name = "ipinfoSqlSessionFactory")
	@Primary
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("ipinfoDataSource") DataSource masterDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(masterDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(DruidConfig.MAPPER_LOCATION));
		sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(DruidConfig.CONFIG_LOCATION));
		sessionFactory.setTypeAliasesPackage(DruidConfig.TYPE_ALIASES_PACKAGE);
		return sessionFactory.getObject();
	}
}
