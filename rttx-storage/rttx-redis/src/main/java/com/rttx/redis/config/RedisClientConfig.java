package com.rttx.redis.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.rttx.commons.base.AppInfo;
import com.rttx.commons.base.BaseConstants;
import com.rttx.commons.utils.StringUtils;
import com.rttx.redis.support.CustomRedisProperties;
import com.rttx.zookeeper.service.ZkService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/19 17:43
 * @Desc: as follows.
 */
@Configuration
@EnableConfigurationProperties(CustomRedisProperties.class)
public class RedisClientConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private CustomRedisProperties properties;

    public RedisClientConfig(ZkService zkService, AppInfo appInfo, CustomRedisProperties customRedisProperties){
        this.init(appInfo, zkService);
        if (this.properties == null){
            this.properties = customRedisProperties;
            logger.info("【Redis】读取本地的项目配置**********************************");
        } else {
            logger.info("【Redis】读取[ZK]的项目配置**********************************");
        }
    }

    /**
     * 初始化，如果ZK配置有信息，则从ZK中获取信息
     */
    public void init(AppInfo appInfo, ZkService zkService){
        if (appInfo != null && !appInfo.getExConfig()){
            return;
        }
        if (zkService == null){
            logger.error("ZK service is Null");
            return;
        }
        //zkService.mkDir(BaseConstants.YUN_WEI + "/storage/redis");
        String redisYamlStr = zkService.getData(BaseConstants.YUN_WEI + "/storage/redis");
        if (StringUtils.isEmpty(redisYamlStr)){
            logger.error("ZK config is Null");
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> object = yaml.loadAs(redisYamlStr, LinkedHashMap.class);
        if (object == null){
            logger.error("ZK config convert Map Null");
            return;
        }
        CustomRedisProperties zkProperties = null;
        try {
            zkProperties = JSON.parseObject(JSON.toJSONString(object), CustomRedisProperties.class);
        } catch (Exception e) {
            logger.error("ZK config convert Properties Object Error \n{}", ExceptionUtils.getStackTrace(e));
            return;
        }
        if (zkProperties == null){
            logger.error("ZK config convert Properties Object Null");
            return;
        }
        this.properties = zkProperties;

    }

    /**
     * redisson Client
     * @return
     */
    @Bean
    public RedissonClient redissonClient(){
        Config config;
        RedissonClient redisson = null;
        if (this.properties.getRedisson() == null){
            return null;
        }
        if (this.properties.getRedisson().getSingle() != null){
            // 单台服务模式
            config = this.properties.getRedisson().getSingle().singleServerConfig(properties);
            redisson = Redisson.create(config);
            return redisson;
        }
        if (this.properties.getRedisson().getMasterSlave() != null){
            // 主从服务模式
            config = this.properties.getRedisson().getMasterSlave().masterSlaveServersConfig(properties);
            redisson = Redisson.create(config);
            return redisson;
        }
        if (this.properties.getRedisson().getReplicated() != null){
            // 分片，云服务模式
            config = this.properties.getRedisson().getReplicated().replicatedServersConfig(properties);
            redisson = Redisson.create(config);
            return redisson;
        }
        if (this.properties.getRedisson().getSentinel() != null){
            // 哨兵服务模式
            config = this.properties.getRedisson().getSentinel().sentinelServersConfig(properties);
            redisson = Redisson.create(config);
            return redisson;
        }
        if (this.properties.getRedisson().getCluster() != null){
            // 集群服务模式
            config = this.properties.getRedisson().getCluster().clusterServersConfig(properties);
            redisson = Redisson.create(config);
            return redisson;
        }
        return redisson;
    }

    /**
     * springboot jedis默认客户端
     * @return
     */
    @Bean
    @Primary
    public RedisProperties redisProperties(){
        if (this.properties.getJedis() == null){
            return new RedisProperties();
        }
        return this.properties.getJedis();
    }
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        if (redisConnectionFactory == null){
            return null;
        }
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        return template;
    }

}
