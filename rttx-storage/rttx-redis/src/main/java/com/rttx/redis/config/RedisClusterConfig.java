//package com.rttx.redis.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPoolConfig;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Configuration
//public class RedisClusterConfig {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Bean("customRedisProperties")
//    @Primary
//    @ConfigurationProperties(prefix = "rttx.storage.redis")
//    public RedisProperties redisProperties() {
//        return new RedisProperties();
//    }
//
//    @Bean
//    public JedisPoolConfig jedisPoolConfig(RedisProperties customRedisProperties) {
//        if (customRedisProperties == null){
//            logger.error("no config");
//        }
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        if (customRedisProperties.getPool() != null){
//            if (!StringUtils.isEmpty(customRedisProperties.getPool().getMaxIdle())) {
//                jedisPoolConfig.setMaxIdle(customRedisProperties.getPool().getMaxIdle());
//            }
//            if (!StringUtils.isEmpty(customRedisProperties.getPool().getMinIdle())) {
//                jedisPoolConfig.setMinIdle(customRedisProperties.getPool().getMinIdle());
//            }
//            if (!StringUtils.isEmpty(customRedisProperties.getPool().getMaxWait())) {
//                jedisPoolConfig.setMaxWaitMillis(customRedisProperties.getPool().getMaxWait());
//            }
//            if (!StringUtils.isEmpty(customRedisProperties.getPool().getMaxActive())) {
//                jedisPoolConfig.setMaxTotal(customRedisProperties.getPool().getMaxActive());
//            }
//        }
//
//        return jedisPoolConfig;
//    }
//
//    /**
//     * 自己建的redis集群连接
//     * @param customRedisProperties
//     * @param jedisPoolConfig
//     * @return
//     */
//    @Bean
//    @ConditionalOnProperty(name = "rttx.storage.redis", havingValue = "cluster")
//    public JedisCluster jedisCluster(RedisProperties customRedisProperties, JedisPoolConfig jedisPoolConfig) {
//        Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
//        List<String> nodes = customRedisProperties.getCluster().getNodes();
//        if (CollectionUtils.isEmpty(nodes)) {
//            logger.error("Redis Nodes is not config");
//            return null;
//        }
//        for (String node : nodes) {
//            hostAndPortSet.add(HostAndPort.parseString(node));
//        }
//
//        return new JedisCluster(hostAndPortSet, jedisPoolConfig);
//    }
//
//   /* @Bean
//    public RedisTemplate<?, ?> objectRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }*/
//
//    /**
//     * SpringBoot 提供
//     * @param customRedisProperties
//     * @return
//     */
//    @Bean
//    public RedisConnectionFactory connectionFactory(RedisProperties customRedisProperties) {
//        return new JedisConnectionFactory(new RedisClusterConfiguration(customRedisProperties.getCluster().getNodes()));
//    }
//
//}
