package com.rttx.mysql.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(basePackages = {"com.rttx.**.mapper.main"}, sqlSessionFactoryRef = "mainSqlSessionFactoryBean")
public class MainDsDruidConfig {
    @Bean(name = "mainDataSource")
    @ConfigurationProperties(prefix = "rttx.storage.mysql.main.datasource")
    @Primary
    public DruidDataSource mainDataSource(){
        return new DruidDataSource();
    }

    @Bean("mainSqlSessionFactoryBean")
    @ConfigurationProperties(prefix = "rttx.storage.mysql.main.mybatisPlus")
    @Primary
    public MybatisSqlSessionFactoryBean mainSqlSessionFactoryBean(){
        MybatisSqlSessionFactoryBean main = new MybatisSqlSessionFactoryBean();
        main.setPlugins(plugins());
        main.setDataSource(mainDataSource());
        return main;
    }

    private Interceptor[] plugins(){
        Interceptor pageInt = new PaginationInterceptor();
        Interceptor[] plugins = {pageInt};
        return plugins;
    }

    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     */
    @Bean("mainTransactionManager")
    @Primary
    public PlatformTransactionManager mainTransactionManager() {
        return new DataSourceTransactionManager(mainDataSource());
    }

}
