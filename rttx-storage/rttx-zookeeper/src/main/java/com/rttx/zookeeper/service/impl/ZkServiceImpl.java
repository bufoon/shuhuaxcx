package com.rttx.zookeeper.service.impl;

import com.alibaba.fastjson.JSON;
import com.rttx.zookeeper.service.ZkService;
import com.rttx.zookeeper.support.NodeCacheGenericListener;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/16 11:31
 * @Desc: as follows.
 * zookeeper 操作服务类
 */
@Service
public class ZkServiceImpl implements ZkService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${rttx.appInfo.name}")
    private String appName; // 应用名称
    @Value("${rttx.appInfo.configPath}")
    private String configPath; // 配置路径
    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    public String getData(String path) {
        try {
            byte[] data = curatorFramework.getData().forPath(getPath(path));
            return new String(data, Charset.defaultCharset());
        } catch (Exception e) {
            logger.error("zk getData is error {}", ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    @Override
    public <V> String getData(String path, AtomicReference<V> v, final V defaultValue) {
        nodeDataChange(path, v, defaultValue);
        return getData(path);
    }

    @Override
    public void setData(String path, String data) {
        try {
            Stat stat = curatorFramework.setData().forPath(getPath(path), data.getBytes(Charset.defaultCharset()));
            logger.debug("zk set data: {}", JSON.toJSONString(stat));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <V> void nodeDataChange(String path, AtomicReference<V> v, final V defaultValue) {
        try {
            NodeCache nodeCache = new NodeCache(curatorFramework, getPath(path));
            nodeCache.getListenable().addListener(new NodeCacheGenericListener<>(v, defaultValue, nodeCache));
            nodeCache.start();
        } catch (Exception e) {
            logger.error("data change error. {}", ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public boolean exists(String path) {

        try {
            Stat stat = curatorFramework.checkExists().forPath(getPath(path));
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exists error.\n{}", ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    @Override
    public String mkDir(String path) {
        try {
            if (exists(path)){
                return "";
            }
            return mkDir(path, "");
        } catch (Exception e) {
            logger.error("mkDir error. \n{}", ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    @Override
    public String mkDir(String path, String data) {
        try {
            if (exists(getPath(path))){
                return "";
            }
            return curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(getPath(path), data.getBytes(Charset.defaultCharset()));
        } catch (Exception e) {
            logger.error("mkDir error. \n{}", ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    @Override
    public void delete(String path) {
        try {
            curatorFramework.delete().forPath(getPath(path));
        } catch (Exception e) {
            logger.error("zk delete error. \n{}", ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public List<String> getChildrens(String path) {
        try {
            return curatorFramework.getChildren().forPath(getPath(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.asList();
    }
    @Override
    public String getPath(String path){
        return configPath + path;
    }
}
