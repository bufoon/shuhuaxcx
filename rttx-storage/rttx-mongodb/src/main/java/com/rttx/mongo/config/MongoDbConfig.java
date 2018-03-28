package com.rttx.mongo.config;

import com.alibaba.fastjson.JSON;
import com.rttx.commons.base.AppInfo;
import com.rttx.commons.base.BaseConstants;
import com.rttx.zookeeper.service.ZkService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/17 15:38
 * @Desc: as follows.
 */
@Configuration
@EnableConfigurationProperties({AppInfo.class})
public class MongoDbConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MongoProperties zkMongoProperties;


    public MongoDbConfig(AppInfo appInfo, ZkService zkService){
        this.init(appInfo, zkService);
    }

    /**
     * 从ZK获取配置信息
     */
    public void init(AppInfo appInfo, ZkService zkService){
        if (appInfo != null && !appInfo.getExConfig()){
            return;
        }
        if (zkService == null){
            logger.error("ZK service is Null");
            return;
        }
        //zkService.mkDir(BaseConstants.YUN_WEI + "/storage/mongo");
        String yamlStr = zkService.getData(BaseConstants.YUN_WEI + "/storage/mongo");
        if (com.rttx.commons.utils.StringUtils.isEmpty(yamlStr)){
            logger.error("ZK config is Null");
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> object = yaml.loadAs(yamlStr, LinkedHashMap.class);
        if (object == null){
            logger.error("ZK config convert Map Null");
            return;
        }
        MongoProperties zkProperties = null;
        try {
            zkProperties = JSON.parseObject(JSON.toJSONString(object), MongoProperties.class);
        } catch (Exception e) {
            logger.error("ZK config convert Properties Object Error \n{}", ExceptionUtils.getStackTrace(e));
            return;
        }
        if (zkProperties == null){
            logger.error("ZK config convert Properties Object Null");
            return;
        }
        this.zkMongoProperties = zkProperties;
    }


    @Bean
    @Primary
    public MongoProperties mongoProperties(){
        if (zkMongoProperties != null){
            logger.info("【MongoDB】读取[ZK]的项目配置**********************************");
            return zkMongoProperties;
        }
        logger.info("【MongoDB】读取本地的项目配置**********************************");
        return localMongoProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "rttx.storage.mongo")
    public MongoProperties localMongoProperties(){
        return new MongoProperties();
    }

}
