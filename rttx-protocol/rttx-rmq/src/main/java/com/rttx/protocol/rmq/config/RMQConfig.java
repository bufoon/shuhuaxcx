package com.rttx.protocol.rmq.config;

import com.rttx.protocol.rmq.support.FastJsonMessageConverter;
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

@Configuration
public class RMQConfig {

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer configurer;

    @ConfigurationProperties(prefix = "rttx.protocol.rmq")
    @Bean("rttxRabbitProperties")
    @Primary
    public RabbitProperties rabbitProperties() {
        return new RabbitProperties();
    }

    @Bean("rttxRabbitConnectionFactory")
    public CachingConnectionFactory rabbitConnectionFactory(@Qualifier("rttxRabbitProperties") RabbitProperties config)
            throws Exception {
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        if (config.determineHost() != null) {
            factory.setHost(config.determineHost());
        }
        factory.setPort(config.determinePort());
        if (config.determineUsername() != null) {
            factory.setUsername(config.determineUsername());
        }
        if (config.determinePassword() != null) {
            factory.setPassword(config.determinePassword());
        }
        if (config.determineVirtualHost() != null) {
            factory.setVirtualHost(config.determineVirtualHost());
        }
        if (config.getRequestedHeartbeat() != null) {
            factory.setRequestedHeartbeat(config.getRequestedHeartbeat());
        }
        RabbitProperties.Ssl ssl = config.getSsl();
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
        if (config.getConnectionTimeout() != null) {
            factory.setConnectionTimeout(config.getConnectionTimeout());
        }
        factory.afterPropertiesSet();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                factory.getObject());
        connectionFactory.setAddresses(config.determineAddresses());
        connectionFactory.setPublisherConfirms(config.isPublisherConfirms());
        connectionFactory.setPublisherReturns(config.isPublisherReturns());
        if (config.getCache().getChannel().getSize() != null) {
            connectionFactory
                    .setChannelCacheSize(config.getCache().getChannel().getSize());
        }
        if (config.getCache().getConnection().getMode() != null) {
            connectionFactory
                    .setCacheMode(config.getCache().getConnection().getMode());
        }
        if (config.getCache().getConnection().getSize() != null) {
            connectionFactory.setConnectionCacheSize(
                    config.getCache().getConnection().getSize());
        }
        if (config.getCache().getChannel().getCheckoutTimeout() != null) {
            connectionFactory.setChannelCheckoutTimeout(
                    config.getCache().getChannel().getCheckoutTimeout());
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
