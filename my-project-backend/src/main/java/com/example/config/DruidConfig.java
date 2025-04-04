package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {

    /*
    * @Value 注解用于从 application.yml 或 application.yml 文件中读取配置值，并将其注入到类的字段中。
    * 在这里，我们通过 @Value 注解将数据库相关的配置值（例如数据库 URL、用户名、密码等）从配置文件中注入到类的成员变量中。
    * */
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.druid.initial-size}")
    private int initialSize;

    @Value("${spring.druid.min-idle}")
    private int minIdle;

    @Value("${spring.druid.max-active}")
    private int maxActive;

    @Value("${spring.druid.max-wait}")
    private int maxWait;

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName(driverClassName);
        // 设置连接池初始化时建立的连接数量。
        dataSource.setInitialSize(initialSize);
        // 设置连接池中最小空闲连接数。
        dataSource.setMinIdle(minIdle);
        // 设置连接池中最大活动连接数。
        dataSource.setMaxActive(maxActive);
        // 设置获取连接时的最大等待时间（单位：毫秒）
        dataSource.setMaxWait(maxWait);
        return dataSource;
    }
}
