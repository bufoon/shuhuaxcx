package com.rttx.zookeeper.config;

import com.rttx.commons.base.AppInfo;
import com.rttx.commons.utils.PropertiesUtil;
import com.rttx.commons.utils.StringUtils;
import com.rttx.zookeeper.prop.CuratorProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(CuratorProperties.class)
public class ZkConfig {

    @Autowired
    private CuratorProperties curatorProperties;
    @Autowired
    private AppInfo appInfo;

    @Bean
    public CuratorFramework curatorFramework() throws Exception {
        if (curatorProperties == null){
            throw new Exception("");
        }
        // 从固定位置取配置信息
        Properties properties = PropertiesUtil.loadDefaultResource();
        if (appInfo != null && appInfo.getExConfig() && StringUtils.isNotEmpty(properties.getProperty("ZookeeperUrl"))){
            curatorProperties.setZkAddress(properties.getProperty("ZookeeperUrl"));
        }
        RetryPolicy retryPolicy = new RetryNTimes(curatorProperties.getRetryCount(), curatorProperties.getSleepTimes());
        CuratorFramework client = CuratorFrameworkFactory.newClient(curatorProperties.getZkAddress(),
                curatorProperties.getSessionTimeout(),curatorProperties.getConnectionTimeout(),
                retryPolicy);
        client.start();
        return client;
    }
}
