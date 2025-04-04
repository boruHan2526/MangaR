package com.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis 配置类
 * 该类用于配置 MyBatis 相关的 Bean，并扫描 Mapper 接口
 */
@Configuration // 标记该类为 Spring 配置类
@MapperScan("com.example.mapper") // 指定 MyBatis 扫描 Mapper 接口所在的包
public class MyBatisConfig {

    /**
     * 配置 SqlSessionFactory
     *
     * @param dataSource 数据源（Spring 自动注入）
     * @return SqlSessionFactory 实例
     * @throws Exception 如果创建 SqlSessionFactory 失败，则抛出异常
     */

    // 在多数据源的配置文件中定义全局配置
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); // 创建 SqlSessionFactoryBean 实例
        factoryBean.setDataSource(dataSource); // 设置数据源

        factoryBean.setTypeAliasesPackage("com.example.entity.dto");// 确保扫描到实体类

        // 指定 Mapper XML 位置
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*.xml")); // 这里路径要与你的 XML 文件匹配

        // 修改注入sqlSessionFactory的时候把配置注入进去
        factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true); //配置驼峰命名

        return factoryBean.getObject(); // 返回 SqlSessionFactory 实例
    }

}
