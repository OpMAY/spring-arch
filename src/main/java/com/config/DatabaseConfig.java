package com.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@EnableTransactionManagement
@Configuration
public class DatabaseConfig {

    @Autowired
    private ApplicationContext applicationContext;

    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    @Value("${DATABASE_URL}")
    private String url;
    @Value("${DATABASE_USERNAME}")
    private String username;
    @Value("${DATABASE_PASSWORD}")
    private String password;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        log.info("DataSource Initialized -> DriverClassName : {}, Url : {}, Username : {}, Password : {}", driverClassName, url, username, password);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        Resource config = applicationContext.getResource("classpath:/mybatis-config.xml");
        if (config != null) {
            factoryBean.setConfigLocation(config);
        } else {
            log.info("mybatis config file is not defined");
            throw new RuntimeException();
        }

        Resource[] locations = applicationContext.getResources("classpath:/sqls/*.xml");
        if (locations != null && locations.length != 0) {
            factoryBean.setMapperLocations(locations);
        } else {
            log.info("mybatis sql files is not defined");
        }
        factoryBean.setTypeAliasesPackage("com/model");
        log.info("SqlSessionFactory Initialized -> Config : {}, Locations : {}", "classpath:/mybatis-config.xml", "classpath:/sqls/*.xml");
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSession() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
        log.info("SqlSession Initialized");
        return sqlSessionTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManger() throws SQLException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        log.info("TransactionManger Initialized");
        return dataSourceTransactionManager;
    }
}