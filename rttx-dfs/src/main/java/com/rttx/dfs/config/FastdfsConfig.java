package com.rttx.dfs.config;

import com.alibaba.fastjson.JSON;
import com.rttx.commons.base.AppInfo;
import com.rttx.commons.base.BaseConstants;
import com.rttx.dfs.FastdfsPool;
import com.rttx.dfs.fastdfs.*;
import com.rttx.zookeeper.service.ZkService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/27 14:01
 * @Desc: as follows.
 */
@Configuration
@EnableConfigurationProperties({AppInfo.class, FastdfsProperties.class})
public class FastdfsConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private FastdfsProperties fastdfsProperties;

    public FastdfsConfig(AppInfo appInfo, ZkService zkService, FastdfsProperties localFastdfsProperties){
        this.init(appInfo, zkService);
        // 如果ZK未配置，用本地的配置
        if (this.fastdfsProperties == null){
            this.fastdfsProperties = localFastdfsProperties;
            logger.info("【FastDFS】读取本地的项目配置**********************************");
        } else {
            logger.info("【FastDFS】读取[ZK]的项目配置**********************************");
        }
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
        //zkService.mkDir(BaseConstants.YUN_WEI + "/storage/dfs");
        String yamlStr = zkService.getData(BaseConstants.YUN_WEI + "/storage/dfs");
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
        FastdfsProperties zkProperties = null;
        try {
            zkProperties = JSON.parseObject(JSON.toJSONString(object), FastdfsProperties.class);
        } catch (Exception e) {
            logger.error("ZK config convert Properties Object Error \n{}", ExceptionUtils.getStackTrace(e));
            return;
        }
        if (zkProperties == null){
            logger.error("ZK config convert Properties Object Null");
            return;
        }
        this.fastdfsProperties = zkProperties;
    }

    @Bean
    public FastdfsPool fastdfsPool(){
        return new FastdfsPool(fastdfsProperties);
    }

    @Bean
    public StorageClient1 storageClient(){
        if (fastdfsProperties == null){
            return null;
        }
        StorageClient1 storageClient = null;
        try
        {
            ClientGlobal.init(fastdfsProperties);
            System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null)
            {
                throw new IllegalStateException("getConnection return null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null)
            {
                throw new IllegalStateException("getStoreStorage return null");
            }
            storageClient = new StorageClient1(trackerServer, storageServer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return storageClient;
    }
}
