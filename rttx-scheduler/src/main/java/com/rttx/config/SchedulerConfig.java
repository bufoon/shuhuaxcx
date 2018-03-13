package com.rttx.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
//@ComponentScan(basePackages = "com.xxl.job.executor.service.jobhandler")
public class SchedulerConfig {
    private Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Bean
    @ConfigurationProperties(prefix = "rttx.scheduler")
    public SchedulerProperties schedulerProperties(){
        return new SchedulerProperties();
    }

//    @Value("${rttx.scheduler.addresses}")
//    private String adminAddresses;
//
//    @Value("${rttx.scheduler.appname}")
//    private String appName;
//
//    @Value("${rttx.scheduler.ip}")
//    private String ip;
//
//    @Value("${rttx.scheduler.port}")
//    private int port;
//
//    @Value("${rttx.scheduler.accessToken}")
//    private String accessToken;

//    @Value("${xxl.job.executor.logpath}")
//    private String logPath;
//
//    @Value("${xxl.job.executor.logretentiondays}")
//    private int logRetentionDays;


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor(SchedulerProperties schedulerProperties) {
//        SchedulerProperties schedulerProperties = schedulerProperties();
        logger.info(">>>>>>>>>>> xxl-job config init."+schedulerProperties.getAdminAddresses());
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(schedulerProperties.getAdminAddresses());
        xxlJobExecutor.setAppName(schedulerProperties.getAppName());
        xxlJobExecutor.setIp(schedulerProperties.getIp());
        xxlJobExecutor.setPort(schedulerProperties.getPort());
        xxlJobExecutor.setAccessToken(schedulerProperties.getAccessToken());
//        xxlJobExecutor.setLogPath(logPath);
//        xxlJobExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobExecutor;
    }

}