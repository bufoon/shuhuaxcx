package com.rttx.redis.service;

import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.MultiKeyJedisClusterCommands;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/17 18:08
 * @Desc: as follows.
 */
public interface RedisCService extends JedisCommands, MultiKeyJedisClusterCommands {

    /**
     * 设置对象，JSON序列化
     * @param key
     * @param t
     * @param <T>
     */
    <T> String setJsonObj(String key, T t);

    /**
     * 获取JSON序列化对象
     * @param key
     * @param clazz
     * @return
     */
    <T> T getJsonObj(String key, Class<T> clazz);
}
