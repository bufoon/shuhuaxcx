package com.rttx.protocol.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.rttx.commons.base.AppInfo;
import com.rttx.protocol.dubbo.endpoint.DubboEndpoint;
import com.rttx.protocol.dubbo.endpoint.DubboOperationEndpoint;
import com.rttx.protocol.dubbo.health.DubboHealthIndicator;
import com.rttx.protocol.dubbo.metrics.DubboMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Dubbo common configuration
 *
 * @author xionghui
 * @email xionghui.xh@alibaba-inc.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({DubboProperties.class, AppInfo.class})
@Order(-1)
public class DubboAutoConfiguration {
  @Autowired
  private DubboProperties properties;
  @Autowired
  private AppInfo appInfo;

  @Bean
  @ConditionalOnMissingBean
  public ApplicationConfig dubboApplicationConfig() {

    ApplicationConfig appConfig = this.properties.getApplication();
    if (appConfig == null){
      appConfig = new ApplicationConfig();
      appConfig.setName(appInfo.getName());
    }

    return appConfig;
  }

  @Bean
  @ConditionalOnMissingBean
  public ProtocolConfig dubboProtocolConfig() {
    ProtocolConfig protocolConfig = this.properties.getProtocol();
    if (protocolConfig == null){
      protocolConfig = new ProtocolConfig();
      protocolConfig.setName("dubbo");
      protocolConfig.setPort(Integer.valueOf("6" +  + appInfo.getId()));
      protocolConfig.setThreads(200);
    }
    return protocolConfig;
  }

  @Bean
  @ConditionalOnMissingBean
  public RegistryConfig dubboRegistryConfig() {
    RegistryConfig registryConfig = this.properties.getRegistry();
    if (registryConfig == null) {
      registryConfig = new RegistryConfig();
      registryConfig.setAddress("multicast://224.0.0.0:1111");
    }
    return registryConfig;
  }

  @Bean
  public DubboHealthIndicator dubboHealthIndicator() {
    return new DubboHealthIndicator();
  }

  @Bean
  public DubboEndpoint dubboEndpoint() {
    return new DubboEndpoint();
  }

  @Bean
  public DubboMetrics dubboConsumerMetrics() {
    return new DubboMetrics();
  }


  @Bean
  public DubboOperationEndpoint dubboOperationEndpoint() {
    return new DubboOperationEndpoint();
  }

}
