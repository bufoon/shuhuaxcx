package com.rttx.mysql.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(basePackages = {"com.rttx.**.mapper.read"}, sqlSessionFactoryRef = "readSqlSessionFactoryBean")
public class ReadDsDruidConfig {

    @Bean(name = "readDataSource")
    @ConfigurationProperties(prefix = "rttx.storage.mysql.read.datasource")
    public DruidDataSource readOnlyDataSource(){
        return new DruidDataSource();
    }

    @Bean("readSqlSessionFactoryBean")
    @ConfigurationProperties(prefix = "rttx.storage.mysql.read.mybatisPlus")
    @ConfigurationPropertiesBinding
    public MybatisSqlSessionFactoryBean readOnlySqlSessionFactoryBean(){
        MybatisSqlSessionFactoryBean readOnly = new MybatisSqlSessionFactoryBean();
        readOnly.setPlugins(plugins());
        readOnly.setDataSource(readOnlyDataSource());
        return readOnly;
    }

    private Interceptor[] plugins(){
        Interceptor pageInt = new PaginationInterceptor();
        Interceptor[] plugins = {pageInt};
        return plugins;
    }

    @Bean("readTransactionManager")
    public PlatformTransactionManager readTransactionManager() {
        return new DataSourceTransactionManager(readOnlyDataSource());
    }
}
