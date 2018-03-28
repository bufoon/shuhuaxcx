package com.rttx.protocol.rmq.config;

import com.alibaba.fastjson.JSON;
import com.rttx.commons.base.AppInfo;
import com.rttx.commons.base.BaseConstants;
import com.rttx.commons.utils.StringUtils;
import com.rttx.protocol.rmq.support.FastJsonMessageConverter;
import com.rttx.zookeeper.service.ZkService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class RMQConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer configurer;

    private RabbitProperties zkRabbitProperties;


    public RMQConfig(ZkService zkService, AppInfo appInfo){
        // 从构造方法去初始化ZK信息，优先拿到配置数据
        init(zkService, appInfo);
    }

    /**
     * 从ZK获取配置信息
     */
    public void init(ZkService zkService, AppInfo appInfo){
        // 是否引用外部（ZK）配置数据
        if (appInfo != null && !appInfo.getExConfig()){
            return;
        }
        if (zkService == null){
            logger.error("ZK service is Null");
            return;
        }
        String yamlStr = zkService.getData(BaseConstants.YUN_WEI + "/protocol/rmq");
        if (StringUtils.isEmpty(yamlStr)){
            logger.error("ZK config is Null");
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> object = yaml.loadAs(yamlStr, LinkedHashMap.class);
        if (object == null){
            logger.error("ZK config convert Map Null");
            return;
        }
        RabbitProperties zkProperties = null;
        try {
            zkProperties = JSON.parseObject(JSON.toJSONString(object), RabbitProperties.class);
        } catch (Exception e) {
            logger.error("ZK config convert Properties Object Error \n{}", ExceptionUtils.getStackTrace(e));
            return;
        }
        if (zkProperties == null){
            logger.error("ZK config convert Properties Object Null");
            return;
        }
        this.zkRabbitProperties = zkProperties;
    }


    @Bean
    @Primary
    public RabbitProperties rabbitProperties() {
        if (this.zkRabbitProperties != null){
            logger.info("【RabbitMQ】读取[ZK]的项目配置**********************************");
            return zkRabbitProperties;
        }
        logger.info("【RabbitMQ】读取本地的项目配置**********************************");
        return localRabbitProperties();
    }

    @ConfigurationProperties(prefix = "rttx.protocol.rmq")
    @Bean
    public RabbitProperties localRabbitProperties() {
        return new RabbitProperties();
    }

    @Bean("rttxRabbitConnectionFactory")
    public CachingConnectionFactory rabbitConnectionFactory(RabbitProperties rabbitProperties)
            throws Exception {
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        if (rabbitProperties.determineHost() != null) {
            factory.setHost(rabbitProperties.determineHost());
        }
        factory.setPort(rabbitProperties.determinePort());
        if (rabbitProperties.determineUsername() != null) {
            factory.setUsername(rabbitProperties.determineUsername());
        }
        if (rabbitProperties.determinePassword() != null) {
            factory.setPassword(rabbitProperties.determinePassword());
        }
        if (rabbitProperties.determineVirtualHost() != null) {
            factory.setVirtualHost(rabbitProperties.determineVirtualHost());
        }
        if (rabbitProperties.getRequestedHeartbeat() != null) {
            factory.setRequestedHeartbeat(rabbitProperties.getRequestedHeartbeat());
        }
        RabbitProperties.Ssl ssl = rabbitProperties.getSsl();
        if (ssl.isEnabled()) {
            factory.setUseSSL(true);
            if (ssl.getAlgorithm() != null) {
                factory.setSslAlgorithm(ssl.getAlgorithm());
            }
            factory.setKeyStore(ssl.getKeyStore());
            factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
            factory.setTrustStore(ssl.getTrustStore());
            factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
        }
        if (rabbitProperties.getConnectionTimeout() != null) {
            factory.setConnectionTimeout(rabbitProperties.getConnectionTimeout());
        }
        factory.afterPropertiesSet();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                factory.getObject());
        connectionFactory.setAddresses(rabbitProperties.determineAddresses());
        connectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
        if (rabbitProperties.getCache().getChannel().getSize() != null) {
            connectionFactory
                    .setChannelCacheSize(rabbitProperties.getCache().getChannel().getSize());
        }
        if (rabbitProperties.getCache().getConnection().getMode() != null) {
            connectionFactory
                    .setCacheMode(rabbitProperties.getCache().getConnection().getMode());
        }
        if (rabbitProperties.getCache().getConnection().getSize() != null) {
            connectionFactory.setConnectionCacheSize(
                    rabbitProperties.getCache().getConnection().getSize());
        }
        if (rabbitProperties.getCache().getChannel().getCheckoutTimeout() != null) {
            connectionFactory.setChannelCheckoutTimeout(
                    rabbitProperties.getCache().getChannel().getCheckoutTimeout());
        }
        return connectionFactory;
    }

    @Bean
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(@Qualifier(value = "rttxRabbitConnectionFactory") CachingConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new FastJsonMessageConverter());
        return template;
    }

    @Bean("rttxRabbitListenerContainerFactory1")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory1(@Qualifier(value = "rttxRabbitConnectionFactory") CachingConnectionFactory connectionFactory){
        return getSRLCFactory(connectionFactory, true, 1);
    }

    @Bean("rttxRabbitListenerContainerFactory10")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory10(@Qualifier(value = "rttxRabbitConnectionFactory") CachingConnectionFactory connectionFactory){
        return getSRLCFactory(connectionFactory, true, 1);
    }

    private SimpleRabbitListenerContainerFactory getSRLCFactory(ConnectionFactory cf, boolean ack, int consumers) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setAcknowledgeMode(ack ? AcknowledgeMode.MANUAL : AcknowledgeMode.AUTO); // 消息确认方式
        factory.setConcurrentConsumers(consumers);
        configurer.configure(factory, cf);
        return factory;
    }

}
