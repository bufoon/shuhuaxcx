package com.rttx.protocol.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.qos.common.Constants;
import com.alibaba.fastjson.JSON;
import com.rttx.commons.base.AppInfo;
import com.rttx.commons.base.BaseConstants;
import com.rttx.commons.utils.StringUtils;
import com.rttx.protocol.dubbo.endpoint.DubboEndpoint;
import com.rttx.protocol.dubbo.endpoint.DubboOperationEndpoint;
import com.rttx.protocol.dubbo.health.DubboHealthIndicator;
import com.rttx.protocol.dubbo.metrics.DubboMetrics;
import com.rttx.zookeeper.service.ZkService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.yaml.snakeyaml.Yaml;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

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
  private Logger logger = LoggerFactory.getLogger(getClass());

  private DubboProperties properties;
  private AppInfo appInfo;

  public DubboAutoConfiguration(ZkService zkService, AppInfo appInfo, DubboProperties localProperties){
    // 从构造方法去初始化ZK信息，优先拿到配置数据
    this.appInfo = appInfo;
    this.init(zkService, appInfo);
    if (this.properties == null){
      this.properties = localProperties;
      logger.info("【Dubbo】读取本地的项目配置**********************************");
    } else {
      logger.info("【Dubbo】读取[ZK]的项目配置**********************************");
    }
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
    String yamlStr = zkService.getData(BaseConstants.YUN_WEI + "/protocol/dubbo");
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
    DubboProperties zkProperties = null;
    try {
      zkProperties = JSON.parseObject(JSON.toJSONString(object), DubboProperties.class);
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
      protocolConfig.setPort(getPort(Integer.valueOf("5" + appInfo.getId())));
      protocolConfig.setThreads(200);
    }
    protocolConfig.setPort(getPort(Integer.valueOf("5" + protocolConfig.getPort())));
    return protocolConfig;
  }

  private Integer getPort(Integer p){
    Integer port = 0;
    try{
      if(isPortAvailable(p)){
        port = p;
      }else{
        for(int i = 1; i < 20;i++){
          p += 5;
          if(isPortAvailable(p)){
            port = p;
            break;
          }
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return port;
  }

  public static boolean isPortAvailable(int port) {
    try {
      bindPort("0.0.0.0", port);
      bindPort(InetAddress.getLocalHost().getHostAddress(), port);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static void bindPort(String host, int port) throws Exception {
    Socket s = new Socket();
    try {
      s.bind(new InetSocketAddress(host, port));
    } finally {
      s.close();
    }

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
