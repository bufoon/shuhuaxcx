package com.rttx.commons.base;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/19 17:56
 * @Desc: as follows.
 * 应用信息
 */
@ConfigurationProperties(prefix = "rttx.appInfo")
public class AppInfo implements Serializable {

    private static final long serialVersionUID = 4713252663637368049L;
    private int id;
    private String name;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
