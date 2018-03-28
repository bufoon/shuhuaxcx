package com.rttx.mysql.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.rttx.commons.base.AppInfo;
import com.rttx.commons.base.BaseConstants;
import com.rttx.mysql.config.mybatisplus.DbProperties;
import com.rttx.mysql.config.mybatisplus.SpringBootVFS;
import com.rttx.zookeeper.service.ZkService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"com.rttx.**.mapper.main"}, sqlSessionFactoryRef = "mainSqlSessionFactoryBean")
@EnableConfigurationProperties({AppInfo.class, DbProperties.class})
public class MainDsDruidConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DbProperties dbProperties;
    private final Interceptor[] interceptors;
    private final ResourceLoader resourceLoader;
    private final DatabaseIdProvider databaseIdProvider;

    public MainDsDruidConfig(AppInfo appInfo, ZkService zkService, DbProperties localDbProperties,
                             ObjectProvider<Interceptor[]> interceptorsProvider,
                             ResourceLoader resourceLoader,
                             ObjectProvider<DatabaseIdProvider> databaseIdProvider){
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.init(appInfo, zkService);
        if (this.dbProperties == null){
            this.dbProperties = localDbProperties;
            logger.info("【Database】读取本地的项目配置**********************************");
        } else {
            logger.info("【Database】读取[ZK]的项目配置**********************************");
        }
    }

    /**
     * 从ZK获取配置信息
     */
    public void init(AppInfo appInfo, ZkService zkService){
        if (appInfo != null && !appInfo.getExConfig()){
            return;
        }
        if (zkService == null){
            logger.error("ZK service is Null");
            return;
        }
        //zkService.mkDir(BaseConstants.YUN_WEI + "/storage/db");
        String yamlStr = zkService.getData(BaseConstants.YUN_WEI + "/storage/db");
        if (com.rttx.commons.utils.StringUtils.isEmpty(yamlStr)){
            logger.error("ZK config is Null");
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> object = yaml.loadAs(yamlStr, LinkedHashMap.class);
        if (object == null){
            logger.error("ZK config convert Map Null");
            return;
        }
        DbProperties zkProperties = null;
        try {
            zkProperties = JSON.parseObject(JSON.toJSONString(object), DbProperties.class);
        } catch (Exception e) {
            logger.error("ZK config convert Properties Object Error \n{}", ExceptionUtils.getStackTrace(e));
            return;
        }
        if (zkProperties == null){
            logger.error("ZK config convert Properties Object Null");
            return;
        }
        this.dbProperties = zkProperties;
    }

    @Bean(name = "mainDataSource")
    @Primary
    public DruidDataSource mainDataSource(){
        return dbProperties.getMainDataSource();
    }

    @Bean("mainSqlSessionFactoryBean")
    @Primary
    public MybatisSqlSessionFactoryBean mainSqlSessionFactoryBean() throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setPlugins(plugins());
        factory.setVfs(SpringBootVFS.class);
        factory.setDataSource(dbProperties.getMainDataSource());
        if (StringUtils.hasText(this.dbProperties.getMybatisPlus().getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.dbProperties.getMybatisPlus().getConfigLocation()));
        }
        MybatisConfiguration configuration = this.dbProperties.getMybatisPlus().getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.dbProperties.getMybatisPlus().getConfigLocation())) {
            configuration = new MybatisConfiguration();
            configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        }
        factory.setConfiguration(configuration);
        if (this.dbProperties.getMybatisPlus().getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.dbProperties.getMybatisPlus().getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.dbProperties.getMybatisPlus().getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.dbProperties.getMybatisPlus().getTypeAliasesPackage());
        }
        // TODO 自定义枚举包
        if (StringUtils.hasLength(this.dbProperties.getMybatisPlus().getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(this.dbProperties.getMybatisPlus().getTypeEnumsPackage());
        }
        if (StringUtils.hasLength(this.dbProperties.getMybatisPlus().getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.dbProperties.getMybatisPlus().getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.dbProperties.getMybatisPlus().resolveMapperLocations())) {
            factory.setMapperLocations(this.dbProperties.getMybatisPlus().resolveMapperLocations());
        }
        if (!ObjectUtils.isEmpty(this.dbProperties.getMybatisPlus().getGlobalConfig())) {
            factory.setGlobalConfig(this.dbProperties.getMybatisPlus().getGlobalConfig().convertGlobalConfiguration());
        }
        return factory;
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
    public PlatformTransactionManager mainTransactionManager(DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
