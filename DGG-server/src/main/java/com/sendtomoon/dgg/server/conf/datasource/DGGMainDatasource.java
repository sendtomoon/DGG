package com.sendtomoon.dgg.server.conf.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
@MapperScan(basePackages = DGGMainDatasource.PACKAGE, sqlSessionFactoryRef = "dggMainSqlSessionFactory")
public class DGGMainDatasource {
	// mysqldao扫描路径
	static final String PACKAGE = "com.sendtomoon.dgg.server.dao.mainsourcedao";
	// mybatis mapper扫描路径
	static final String MAPPER_LOCATION = "classpath:mapper/mainsource/*Mapper.xml";

	@Primary
	@Bean(name = "dggMainDatasource")
	@ConfigurationProperties("spring.datasource.druid.mysql")
	public DataSource mysqlDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "dggMainTransactionManager")
	@Primary
	public DataSourceTransactionManager mysqlTransactionManager() {
		return new DataSourceTransactionManager(mysqlDataSource());
	}

	@Bean(name = "dggMainSqlSessionFactory")
	@Primary
	public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("dggMainDatasource") DataSource dataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		// 如果不使用xml的方式配置mapper，则可以省去下面这行mapper location的配置。
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(DGGMainDatasource.MAPPER_LOCATION));
		return sessionFactory.getObject();
	}
}
