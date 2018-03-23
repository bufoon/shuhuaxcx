package com.rttx.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.rttx.redis.support.CustomRedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/19 17:43
 * @Desc: as follows.
 */
@Configuration
@ConditionalOnProperty(name = "rttx.storage.redis.enable", havingValue = "true")
@EnableConfigurationProperties(CustomRedisProperties.class)
public class RedisClientConfig {

    @Autowired
    private CustomRedisProperties properties;

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
    @ConditionalOnProperty(name = "rttx.storage.redis.jedis.enable", havingValue = "true")
    public RedisProperties redisProperties(){
        return this.properties.getJedis();
    }
    @Bean
    @ConditionalOnProperty(name = "rttx.storage.redis.jedis.enable", havingValue = "true")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        return template;
    }

}
