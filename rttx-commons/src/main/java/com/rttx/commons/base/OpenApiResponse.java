package com.rttx.commons.base;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/30 14:04
 * @Desc: as follows.
 * 响应数据
 */
public class OpenApiResponse<T> {

    private String code; // 响应数字码
    private String msg; // 响应消息码
    private String desc; // 描述
    private String traceId = UUID.randomUUID().toString().toUpperCase(); //响应记录ID
    private String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"); //yyyy-MM-dd HH:mm:ss服务端时间戳字符串
    private String sign;
    private T data;

    public OpenApiResponse() {
    }

    public OpenApiResponse(String code, String msg, String desc, T data) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
        this.data = data;
    }

    public OpenApiResponse(ResEnum resEnum, T data) {
        this(resEnum.getCode(), resEnum.getMsg(), resEnum.getDesc(), data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
