package com.ecom.ecommerce.config;

import com.ecom.ecommerce.utility.AESGCMUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration for DB layer encryption/decryption
 */
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${db.username.encrypted}")
    private String encryptedUsername;

    @Value("${db.password.encrypted}")
    private String encryptedPassword;

    @Autowired
    AESGCMUtil aesgcmUtil;

    @Bean
    public DataSource dataSource() throws Exception {

        String username = aesgcmUtil.decrypt(encryptedUsername);
        String password = aesgcmUtil.decrypt(encryptedPassword);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}

