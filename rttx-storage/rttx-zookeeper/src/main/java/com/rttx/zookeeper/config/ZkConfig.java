package com.rttx.zookeeper.config;

import com.rttx.zookeeper.prop.CuratorProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkConfig {

    @ConfigurationProperties(prefix = "rttx.storage.zookeeper")
    @Bean("rttxCuratorProperties")
    public CuratorProperties curatorProperties(){
        return new CuratorProperties();
    }

    @Bean
    public CuratorFramework curatorFramework(@Qualifier("rttxCuratorProperties") CuratorProperties curatorProperties) throws Exception {
        if (curatorProperties == null){
            throw new Exception("");
        }
        RetryPolicy retryPolicy = new RetryNTimes(curatorProperties.getRetryCount(), curatorProperties.getSleepTimes());
        CuratorFramework client = CuratorFrameworkFactory.newClient(curatorProperties.getZkAddress(),
                curatorProperties.getSessionTimeout(),curatorProperties.getConnectionTimeout(),
                retryPolicy);
        client.start();
        return client;
    }
}
