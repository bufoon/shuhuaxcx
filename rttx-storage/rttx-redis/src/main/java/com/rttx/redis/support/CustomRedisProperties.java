package com.rttx.redis.support;


import com.alibaba.fastjson.JSON;
import com.rttx.commons.base.AppInfo;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/19 14:30
 * @Desc: as follows.
 */
@ConfigurationProperties(prefix = "rttx.storage.redis")
public class CustomRedisProperties {
    private RedisProperties jedis;
    private RedissonProperties redisson;

    public RedisProperties getJedis() {
        return jedis;
    }

    public void setJedis(RedisProperties jedis) {
        this.jedis = jedis;
    }

    public RedissonProperties getRedisson() {
        return redisson;
    }

    public void setRedisson(RedissonProperties redisson) {
        this.redisson = redisson;
    }

    public static void main(String[] args) {

        Yaml yaml = new Yaml();
        String ya = "id: 1\n" +
                "name: risk-basis-app\n" +
                "configPath : /AppConfig/RTTX/RISK/${rttx.appInfo.name}/ #ZK配置路径";
        Map<String, Object> object = yaml.loadAs(ya, HashMap.class);
        AppInfo appInfo = JSON.parseObject(JSON.toJSONString(object), AppInfo.class);
        System.out.println(JSON.toJSONString(object));
        System.out.println(appInfo.toString());
    }
}
