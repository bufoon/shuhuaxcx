package com.rttx.protocol.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * dubbo properties
 *
 * @author xionghui
 * @email xionghui.xh@alibaba-inc.com
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "rttx.protocol.dubbo")
public class DubboProperties {

  private ApplicationConfig application;

  private ProtocolConfig protocol;

  private RegistryConfig registry;

  public ApplicationConfig getApplication() {
    return application;
  }

  public void setApplication(ApplicationConfig application) {
    this.application = application;
  }

  public ProtocolConfig getProtocol() {
    return protocol;
  }

  public void setProtocol(ProtocolConfig protocol) {
    this.protocol = protocol;
  }

  public RegistryConfig getRegistry() {
    return registry;
  }

  public void setRegistry(RegistryConfig registry) {
    this.registry = registry;
  }
}
