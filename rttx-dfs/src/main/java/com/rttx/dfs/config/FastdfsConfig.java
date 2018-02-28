package com.rttx.dfs.config;

import com.rttx.dfs.FastdfsPool;
import com.rttx.dfs.fastdfs.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/27 14:01
 * @Desc: as follows.
 */
@Configuration
public class FastdfsConfig {

    @Bean
    @ConfigurationProperties(prefix = "rttx.dfs")
    public FastdfsProperties fastdfsProperties(){
        return new FastdfsProperties();
    }

    @Bean
    public FastdfsPool fastdfsPool(FastdfsProperties properties){
        return new FastdfsPool(properties);
    }

    @Bean
    public StorageClient1 storageClient(FastdfsProperties properties){
        if (properties == null){
            return null;
        }
        StorageClient1 storageClient = null;
        try
        {
            ClientGlobal.init(properties);
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
