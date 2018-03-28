package com.rttx.mysql.config.mybatisplus;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/27 10:54
 * @Desc: as follows.
 */
@ConfigurationProperties(prefix = "rttx.storage.db")
public class DbProperties {
    private DruidDataSource mainDataSource;
    private DruidDataSource readDataSource;
    private MybatisPlusProperties mybatisPlus;

    public DruidDataSource getMainDataSource() {
        return mainDataSource;
    }

    public void setMainDataSource(DruidDataSource mainDataSource) {
        this.mainDataSource = mainDataSource;
    }

    public DruidDataSource getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(DruidDataSource readDataSource) {
        this.readDataSource = readDataSource;
    }

    public MybatisPlusProperties getMybatisPlus() {
        return mybatisPlus;
    }

    public void setMybatisPlus(MybatisPlusProperties mybatisPlus) {
        this.mybatisPlus = mybatisPlus;
    }
}
