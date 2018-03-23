package com.rttx.redis.service.impl;

import com.rttx.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/19 18:34
 * @Desc: as follows.
 */
public class JedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    public JedisServiceImpl(RedisTemplate<String, ?> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String get(String key) {
        return String.valueOf(redisTemplate.opsForValue().get(key));
    }
}
