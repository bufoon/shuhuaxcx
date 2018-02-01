package com.rttx.protocol.rmq.base;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/16 16:30
 * @Desc: as follows.
 *  rabbitMq 消息实体
 */
public class RMQMessage<T> {

    private String appId; //应用ID
    private String appName; //应用名
    private String sendTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
    private T data; //消息数据

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
