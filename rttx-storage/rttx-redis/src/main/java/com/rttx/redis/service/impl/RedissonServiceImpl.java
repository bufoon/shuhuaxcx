
package com.rttx.redis.service.impl;

import com.rttx.redis.service.RedisService;
import org.redisson.api.RedissonClient;


/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/20 17:35
 * @Desc: as follows.
 */


public class RedissonServiceImpl implements RedisService {

    private RedissonClient redissonClient;

    public RedissonServiceImpl(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }
    @Override
    public String get(String key) {
        return String.valueOf(redissonClient.getBucket(key).get());
    }
}

