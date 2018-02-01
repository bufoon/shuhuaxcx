package com.rttx.protocol.rmq.base;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/16 16:32
 * @Desc: as follows.
 * MQ 消息属性
 */
public class RMQProperties {
    private String exchange; //交换机
    private String rk; // 路由键
    private String queue; // 队列名

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRk() {
        return rk;
    }

    public void setRk(String rk) {
        this.rk = rk;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
